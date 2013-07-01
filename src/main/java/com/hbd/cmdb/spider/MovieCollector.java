package com.hbd.cmdb.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class MovieCollector {

	String rootUrl = "http://movie.douban.com/tag/";
	HttpClient httpClient = new DefaultHttpClient();

	public void collect() throws Exception {
		try {
			HttpGet httpget = new HttpGet(rootUrl);

			System.out.println("executing request " + httpget.getURI());

			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpClient.execute(httpget, responseHandler);
			processTag(responseBody);
			// System.out.println(responseBody);
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpClient.getConnectionManager().shutdown();
		}
	}

	void processTag(String tagPage) throws Exception {
		List<String> tagList = findLink(tagPage);
		System.out.println(tagList);
		for (String tag : tagList) {
			if (tag.charAt(0) == '.') {
				String tagLink = rootUrl + tag;
				HttpGet hg = new HttpGet(tagLink);
				ResponseHandler<String> res = new BasicResponseHandler();
				String rb = httpClient.execute(hg, res);
				List<String> movieLinkList = findLink(rb);
				for (String movieLink : movieLinkList) {
					if (isMovieLink(movieLink)) {
						System.out.println(movieLink);
					}
				}
			}
		}
	}

	boolean isMovieLink(String link) {
		if (link.startsWith("http://movie.douban.com/subject/")) {
			return true;
		} else {
			return false;
		}
	}

	List<String> findLink(String page) {
		List<String> linkList = new ArrayList<String>();
		int length = page.length();
		for (int i = 0; i < length; i++) {
			if (page.charAt(i) == '<' && page.charAt(i + 1) == 'a'
					&& page.charAt(i + 2) == ' ' && page.charAt(i + 3) == 'h'
					&& page.charAt(i + 4) == 'r' && page.charAt(i + 5) == 'e'
					&& page.charAt(i + 6) == 'f' && page.charAt(i + 7) == '='
					&& page.charAt(i + 8) == '"') {
				int j = i + 9;
				StringBuffer sb = new StringBuffer();
				char ca = page.charAt(j);
				while (ca != '"') {
					sb.append(ca);
					ca = page.charAt(++j);
				}
				linkList.add(sb.toString());
				i = j;
			}
		}
		return linkList;
	}
}
