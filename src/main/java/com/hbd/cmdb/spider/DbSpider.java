package com.hbd.cmdb.spider;

public class DbSpider {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		MovieCollector mc = new MovieCollector();
		mc.collect();
	}

}
