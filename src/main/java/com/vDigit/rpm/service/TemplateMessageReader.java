package com.vDigit.rpm.service;

import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class TemplateMessageReader {

	private static final String UTF_8 = "utf-8";
	private static final Logger logger = LoggerFactory.getLogger(TemplateMessageReader.class);

	public String read(String templateName, Map<String, String> tokenReplace, String regex) {
		StringBuffer sb = new StringBuffer();
		try {
			org.springframework.core.io.Resource resource = new ClassPathResource(templateName);
			StringWriter writer = new StringWriter();
			IOUtils.copy(resource.getInputStream(), writer, UTF_8);
			String template = writer.toString();

			// Create pattern of the format "%(name|date)%"
			String patternString = String.format(regex, StringUtils.join(tokenReplace.keySet(), "|"));
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(template);

			while (matcher.find()) {
				matcher.appendReplacement(sb, tokenReplace.get(matcher.group(1)));
			}
			matcher.appendTail(sb);
		} catch (Exception e) {
			logger.error("Exception while processiong message template..." + templateName + "and regex..." + regex, e);
		}
		return sb.toString();
	}

}
