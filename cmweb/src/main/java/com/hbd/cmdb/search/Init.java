package com.hbd.cmdb.search;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hbd.cmdb.BaseInfo;
import com.hbd.cmdb.index.IndexInfo;

public class Init {
	Map<String, List<CmdbEntry<String, Integer>>> indexMap = new HashMap<String, List<CmdbEntry<String, Integer>>>();
	Map<String, String> subjectSummaryMap = new HashMap<String, String>();

	public Map<String, List<CmdbEntry<String, Integer>>> getIndexMap() {
		return indexMap;
	}


	public Map<String, String> getSubjectSummaryMap() {
		return subjectSummaryMap;
	}

	/**
	 * init indexMap
	 */
	public void initIndex() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(IndexInfo.wordIndexFile),"UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] word = line.split(":");
				if (word.length != 2) {
					continue;
				}
				String[] subject = word[1].split(";");
				if (subject.length < 1) {
					continue;
				}
				List<CmdbEntry<String, Integer>> list = new ArrayList<CmdbEntry<String, Integer>>();
				for (int i = 0; i < subject.length; i++) {
					String[] count = subject[i].split(",");
					CmdbEntry<String, Integer> entry = new CmdbEntry<String, Integer>();
					entry.setKey(count[0]);
					entry.setValue(Integer.valueOf(count[1]));
					list.add(entry);
				}
				indexMap.put(word[0], list);
			}
		} catch (Exception e) {
			try {
				br.close();
			} catch (Throwable e1) {
				e1.printStackTrace();
				throw new RuntimeException("init failed.");
			}
		}
	}

	void initSubject() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(BaseInfo.totalSubjectFile),"UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] sub = line.split(":");
				subjectSummaryMap.put(sub[0], sub[1]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("init failed.");
		}
	}
	
	
}
