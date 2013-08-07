package com.hbd.cmdb.search;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;

public class MainServlet extends HttpServlet {
	private Searcher searcher;

	private MustacheFactory mustacheFactory = null ;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5402874960380395280L;

	public void init() {
		String realPath = this.getServletContext().getRealPath(".");
		searcher = Searcher.getInstance();
		mustacheFactory = new DefaultMustacheFactory(new File(realPath));
	}

	@Override
	protected void doGet(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
			IOException {
		String keys = req.getParameter("keys");
		if(keys==null||keys.equals("")){
			return;
		}
		List<String> result = searcher.search(keys);
		resp.setContentType("text/html");
		mustacheFactory.compile("result.html").execute(resp.getWriter(), result).flush();
	    resp.getWriter().close();
	}

	protected void doPost(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
	}


}
