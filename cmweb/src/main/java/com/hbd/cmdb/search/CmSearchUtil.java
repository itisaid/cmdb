package com.hbd.cmdb.search;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CmSearchUtil {
	public static  String result(List<Entry<String, Integer>> list,Map<String, String> subjectSummaryMap) {
		int num = 0;
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Integer> e : list) {
			if (num++ > 20) {
				break;
			}
			String key = e.getKey();
			sb.append(subjectSummaryMap.get(key)+"</br>");
		}
		return sb.toString();
	}
}
