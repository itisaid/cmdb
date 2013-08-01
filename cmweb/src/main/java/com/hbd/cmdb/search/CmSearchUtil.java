package com.hbd.cmdb.search;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CmSearchUtil {
	static Map<String, String> subjectSummaryMap = Index.getInstance()
			.getSubjectSummaryMap();
	static int maxResult = 40;

	public static String result(List<Entry<String, Double>> list) {
		int num = 0;
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Double> e : list) {
			if (num++ > maxResult) {
				break;
			}
			String key = e.getKey();
			sb.append(subjectSummaryMap.get(key) + "</br>");
		}
		return sb.toString();
	}
}
