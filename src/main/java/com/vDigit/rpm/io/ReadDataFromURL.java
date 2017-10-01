package com.vDigit.rpm.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.vDigit.rpm.util.Util;

public class ReadDataFromURL extends ReadDataFromStream {
	public static final int DEFAULT_RETRY = 3;
	public int retries = DEFAULT_RETRY;

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getRetries() {
		return retries;
	}

	private boolean useProxy = false;

	public boolean isUseProxy() {
		return useProxy;
	}

	public void setUseProxy(boolean b) {
		this.useProxy = b;
	}

	public static class Response {
		public String content;
		public String responseURL;
		public String sourceURL;
		public int responseCode;
		public Map<String, List<String>> responseHeaders = new LinkedHashMap<String, List<String>>();

	}

	public Response read(String url) {
		return read(url, new HashMap<String, String>());
	}

	private URL createURL(String url) throws Exception {
		String proxySet = System.getProperty("proxySet");
		URL u = new URL(url);
		if (proxySet == null) {
			return u;
		}
		return u;
	}

	public Response read(String url, Map<String, String> hashMap) {
		ReadRequest request = new ReadRequest(url, hashMap, getRetries());
		Exception ex = null;
		while (request.getTries() != 0) {
			try {
				return read(request);
			} catch (Exception e) {
				ex = e;
				processExceptionWhenProxyIsUsedForConnectingToHost(request, e);
				request.tryAgain();
			}
			setProxy(null);
		}
		throw throwRuntimeException(ex);
	}

	private void processExceptionWhenProxyIsUsedForConnectingToHost(ReadRequest request, Exception e) {

		System.out.println(e);
		if (e instanceof UnknownHostException) {
			if (Util.isInternetDOWN()) {
				throw throwRuntimeException(e);
			}
		}
		Proxy p = getProxy();

		throw throwRuntimeException(e);
	}

	private RuntimeException throwRuntimeException(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException(e);
	}

	class ReadRequest {
		String url;
		Map<String, String> map;

		ReadRequest(String url, Map<String, String> map, int tries) {
			this.url = url;
			this.map = map;
			this.tries = tries;
		}

		public void tryAgain() {
			tries--;

		}

		public boolean canTryConnectingToHost() {
			if (tries == 0) {
				return false;
			} else {
				return true;
			}
		}

		public Integer getTries() {
			return tries;
		}

		int tries = DEFAULT_RETRY;
	}

	private Response read(ReadRequest rr) throws Exception {
		Response r = new Response();
		r.sourceURL = rr.url;
		URL u = createURL(rr.url);
		HttpURLConnection uc = createHttpURLConnection(rr.map, u);
		InputStream is = uc.getInputStream();
		r.content = readData(is);
		r.responseCode = uc.getResponseCode();
		r.responseURL = uc.getURL().toString();
		r.responseHeaders = uc.getHeaderFields();
		return r;
	}

	public String readData(String url) {
		return readData(url, new HashMap<String, String>());
	}

	public String readData(String url, Map<String, String> requestParameters) {
		try {
			InputStream is = createInputStream(url, requestParameters);
			return readData(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private InputStream createInputStream(String url, Map<String, String> requestParameters)
			throws MalformedURLException, IOException, Exception {
		URL u = createURL(url);
		HttpURLConnection uc = createHttpURLConnection(requestParameters, u);
		InputStream is = uc.getInputStream();
		return is;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setConnectionTimeout(Integer cto) {
		this.connectionTimeout = cto;
	}

	public void setReadTimeout(Integer rto) {
		this.readTimeout = rto;
	}

	private Integer connectionTimeout = 5000;
	private Integer readTimeout = 15000;

	private Proxy makeProxy() {
		Proxy p = getProxy();
		if (p != null) {
			return p;
		}
		p = createProxy();
		if (p != null) {
			setProxy(p);
		}
		return p;
	}

	private HttpURLConnection createHttpURLConnection(Map<String, String> requestParameters, URL u)
			throws IOException, Exception {

		Proxy p = makeProxy();

		HttpURLConnection uc = null;
		if (p == null) {
			uc = (HttpURLConnection) u.openConnection();
		} else {
			uc = (HttpURLConnection) u.openConnection(p);
		}

		for (Map.Entry<String, String> entry : requestParameters.entrySet()) {
			uc.setRequestProperty(entry.getKey(), entry.getValue());
		}
		process(uc);
		return uc;
	}

	private Proxy createProxy() {
		String proxySet = System.getProperty("proxySet");
		boolean useProxy = isUseProxy();
		if (proxySet == null && useProxy == false) {
			return null;
		}
		// Proxy proxy = proxyURLManager.getRandomProxy();
		Proxy proxy = null;
		System.out.println(proxy);
		return proxy;
	}

	// Callback method for child classes to do something..
	private void process(HttpURLConnection uc) throws Exception {
		uc.setConnectTimeout(getConnectionTimeout());
		uc.setReadTimeout(getReadTimeout());

		if (getUserAgent() != null)
			uc.setRequestProperty("User-Agent", getUserAgent());
		getHttpURLConnectionProcessor().process(uc);
	}

	private HttpURLConnectionProcessor hucp = new HttpURLConnectionProcessorImpl();

	public void setHttpURLConnectionProcessor(HttpURLConnectionProcessor hup) {
		this.hucp = hup;
	}

	public HttpURLConnectionProcessor getHttpURLConnectionProcessor() {
		return hucp;
	}

	public static interface HttpURLConnectionProcessor {
		void process(HttpURLConnection huc) throws Exception;
	}

	static class HttpURLConnectionProcessorImpl implements HttpURLConnectionProcessor {

		@Override
		public void process(HttpURLConnection huc) {
			// huc.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

		}

	}

	// public static final String DEFAULT_USER_AGENT =
	// "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.7; rv:38.0) Gecko/20100101
	// Firefox/38.0";
	public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.124 Safari/537.36";
	private String userAgent = DEFAULT_USER_AGENT;

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String ua) {
		this.userAgent = ua;
	}

	private Proxy proxy;

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	public Proxy getProxy() {
		return proxy;
	}
}
