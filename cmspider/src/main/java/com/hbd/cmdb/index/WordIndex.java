package com.hbd.cmdb.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.hbd.cmdb.BaseInfo;
import com.hbd.cmdb.parser.ParserUtil;

public class WordIndex {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		WordIndex wi = new WordIndex();
		wi.index();
		System.out.println("-------------end");

	}

	public void index() throws Exception {
		File[] fs = ParserUtil.listSubjectDir();
		Map<String, Map<String, Integer>> indexMap = new HashMap<String, Map<String, Integer>>();
		for (File f : fs) {
			String subPath = f.getAbsolutePath();
			File reviewData = new File(subPath + BaseInfo.reviewFile);
			if (!reviewData.exists()) {
				continue;
			}
			BufferedReader br = new BufferedReader(new FileReader(reviewData));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("-----")) {
					continue;
				}
				indexWord(indexMap, line, subPath.split("/")[4]);
			}
			br.close();
		}
		FileOutputStream subOut = new FileOutputStream(new File(
				IndexInfo.wordIndexFile));
		for (Entry<String, Map<String, Integer>> indexEntry : indexMap
				.entrySet()) {
			String word = indexEntry.getKey();
			if (word == null || word.trim().equals("")
					|| word.indexOf(',') != -1 || word.indexOf(':') != -1
					|| word.indexOf(';') != -1) {
				continue;
			}
			Map<String, Integer> subjectMap = indexEntry.getValue();
			List<Entry<String, Integer>> list = IndexInfo.sortMap(subjectMap);
			int indexSize = 0;
			subOut.write((word + ":").getBytes());
			for (Entry<String, Integer> entry : list) {
				if (indexSize++ > 200) {
					break;
				}
				String subject = entry.getKey();
				int count = entry.getValue();
				subOut.write((subject + "," + count + ";").toString()
						.getBytes());
			}
			subOut.write("\r\n".getBytes());

		}
		subOut.close();
	}

	void indexWord(Map<String, Map<String, Integer>> indexMap, String line,
			String subject) {

		List<Term> terms = ToAnalysis.paser(line);
		for (Term term : terms) {
			String word = term.getName();
			Map<String, Integer> subjectMap = indexMap.get(word);
			if (subjectMap == null) {
				subjectMap = new HashMap<String, Integer>();
				subjectMap.put(subject, 1);
				indexMap.put(word, subjectMap);
			} else {
				Integer count = subjectMap.get(subject);
				if (count == null) {
					subjectMap.put(subject, 1);
				} else {
					subjectMap.put(subject, count + 1);
				}
			}
		}

	}
}
