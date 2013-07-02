package com.hbd.cmdb.spider;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class SpiderUtil {
	public static HttpClient httpClient;
	public static int lb = 0;

	public static void init() throws Exception {
		httpClient = new DefaultHttpClient();
		HttpHost[] proxys = new HttpHost[3];
		proxys[0] = new HttpHost("child-prc.intel.com", 911, "http");
		proxys[1] = new HttpHost("proxy-shz.intel.com", 911, "http");
		proxys[2] = new HttpHost("proxy-mu.intel.com", 911, "http");
		int i = lb++ % 3;
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxys[i]);
		Thread.sleep(1000);
	}

	public static String request(String url) throws Exception {
		String responseBody;
		try {
			init();
			HttpGet httpget = new HttpGet(url);

			System.out.println("executing request " + httpget.getURI());

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = SpiderUtil.httpClient.execute(httpget,
					responseHandler);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return responseBody;
	}
}
