package com.vDigit.rpm.io;

import java.util.HashMap;
import java.util.Map;

public class ReadDataFromServices {
	public String readFromProduction(String url) {
		return read(url, "prod-las-remote", "prod-las-remsleep");
	}

	public String read(String url, String user, String pass) {
		ReadDataFromURL readDataFromURL = new ReadDataFromURL();
		LineProcessor lp = new LineProcessor();
		readDataFromURL.setLineProcessor(lp);
		try {
			readDataFromURL.readData(url, getAuthenticatedRequestPropertiesMap(user, pass));
			String data = lp.getData();
			return data;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	class LineProcessor implements ReadDataFromStream.LineProcessor {
		private StringBuilder builder = new StringBuilder(2000);

		@Override
		public boolean processLine(String line) {
			line = new String(line).trim();
			if (line.equals("")) {
				return true;
			}
			builder.append(line + "\n");
			// TODO Auto-generated method stub
			return true;
		}

		public String getData() {
			return builder.toString();
		}

	}

	private Map<String, String> getAuthenticatedRequestPropertiesMap(String user, String pass) {
		Map<String, String> params = new HashMap<String, String>();
		String userpass = user + ":" + pass;
		String basicAuthOne = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
		String basicAuth = basicAuthOne;
		params.put("Authorization", basicAuth);
		return params;
	}

}
