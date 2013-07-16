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
	private Searcher searcher;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5402874960380395280L;

	public void init() {
		searcher = Searcher.getInstance();
	}

	@Override
	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
		String keys = req.getParameter("keys");
		if(keys==null||keys.equals("")){
			return;
		}
		String result = searcher.search(keys);
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


}
