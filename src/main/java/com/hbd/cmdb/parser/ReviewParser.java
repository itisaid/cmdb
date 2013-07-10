package com.hbd.cmdb.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

public class ReviewParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void parse() throws Exception {
		File[] fs = ParserUtil.listSubjectDir();
		for (File f : fs) {
			String subPath = f.getAbsolutePath();
			File subDir = new File(subPath);
			File[] reviews = subDir.listFiles(new FileFilter() {
				public boolean accept(File dir) {
					String name = dir.getName();
					if (name.contains(".rev")) {
						return true;
					} else {
						return false;
					}
				}
			});
			StringBuffer content = new StringBuffer();
			for (File review : reviews) {
				BufferedReader br = new BufferedReader(new FileReader(review));
				String line;
				while ((line = br.readLine()) != null) {
					if (line.indexOf("v:description") != -1
							&& line.indexOf("div") != -1) {
						ParserUtil.splitLine(content);
						content.append(ParserUtil.filterTag(line));
					}
				}
				br.close();
			}
			FileOutputStream subOut = new FileOutputStream(new File(subPath
					+ "/review.data"));
			subOut.write(content.toString().getBytes());
			subOut.close();
		}
	}

}
