package com.hbd.cmdb.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ansj.domain.Term;

import com.hbd.cmdb.index.IndexInfo;

public class Searcher {
	private static Searcher instance = new Searcher();

	private Searcher() {
		init();
	}

	public static Searcher getInstance() {
		return instance;
	}

	Map<String, List<CmdbEntry<String, Integer>>> indexMap;
	Map<String, String> subjectSummaryMap;

	public void init() {
		Init i = new Init();
		i.initIndex();
		i.initSubject();
		this.indexMap = i.getIndexMap();
		this.subjectSummaryMap = i.getSubjectSummaryMap();
		SearchUtil.splitWords("hello, 钱");// init ansj by firstly invoking.
	}

	public String search(String keyWords) {
		List<Term> terms = SearchUtil.splitWords(keyWords);
		Map<String, Integer> subjectMap = new HashMap<String, Integer>();
		for (Term term : terms) {
			String key = term.getName();
			System.out.println("search key:" + key);
			if (indexMap.containsKey(key)) {
				for (CmdbEntry<String, Integer> e : indexMap.get(key)) {
					String subject = e.getKey();
					int value = e.getValue();
					System.out.println(subjectSummaryMap.get(subject)+"   "+value);
					Integer count = subjectMap.get(subject);
					if (count == null) {
						subjectMap.put(subject, value);
					} else {
						subjectMap.put(subject, count + value);
					}
				}
			}
		}
		List<Entry<String, Integer>> list = IndexInfo.sortMap(subjectMap);

		return CmSearchUtil.result(list, subjectSummaryMap);
	}

}
