package com.hbd.cmdb.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.processing.Processor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.hbd.cmdb.index.IndexInfo;

public class MainServlet extends HttpServlet {
	Map<String, List<CmdbEntry<String, Integer>>> indexMap;
	Map<String, String> subjectSummaryMap;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5402874960380395280L;

	public void init() {
		Init i = new Init();
		i.initIndex();
		i.initSubject();
		this.indexMap = i.getIndexMap();
		this.subjectSummaryMap = i.getSubjectSummaryMap();
		List<CmdbEntry<String, Integer>> s = indexMap.get("go");
		subjectSummaryMap.get("");
	}

	@Override
	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
		String keys = req.getParameter("keys");
		if(keys==null||keys.equals("")){
			return;
		}
		List<Entry<String, Integer>> list = search(keys);
		String result = CmSearchUtil.result(list, subjectSummaryMap);
		final PrintWriter out = resp.getWriter();
		out.println("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/></head><body>");
		out.println(result);
		out.println("</body></html>");
		out.flush();
		out.close();
	}

	protected void doPost(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
	}

	public List<Entry<String, Integer>> search(String keyWords) {
		List<Term> terms = ToAnalysis.paser(keyWords);
		Map<String, Integer> subjectMap = new HashMap<String, Integer>();
		for (Term term : terms) {
			String key = term.getName();
			System.out.println("search key:" + key);
			if (indexMap.containsKey(key)) {
				for (CmdbEntry<String, Integer> e : indexMap.get(key)) {
					String subject = e.getKey();
					int value = e.getValue();
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

		return list;
	}

}
