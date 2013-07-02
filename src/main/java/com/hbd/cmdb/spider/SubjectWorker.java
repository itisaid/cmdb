package com.hbd.cmdb.spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class SubjectWorker implements Worker {

	Set<String> allSubject = new HashSet<String>();
	Set<String> finishedSubject = new HashSet<String>();
	String urlPrefix = "http://movie.douban.com/subject/";
	String fs = BaseInfo.path + "fs.txt";
	FileOutputStream out;

	@Override
	public void execute() throws Exception {
		init();
		try {
			for (String subject : allSubject) {
				if (!finishedSubject.contains(subject)) {
					loadSubject(subject);
				}
			}
		} finally {
			out.close();
		}

	}

	void loadSubject(String subject) throws Exception {
		String url = urlPrefix + subject;
		String res = SpiderUtil.request(url);
		File dir = new File(BaseInfo.output + subject);
		if (!dir.exists()) {
			dir.mkdir();
		}

		FileOutputStream subOs = new FileOutputStream(new File(BaseInfo.output
				+ subject + "/subject.html"));
		subOs.write(res.getBytes());
		subOs.close();

		out.write((subject + "\r\n").getBytes());
	}

	void init() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(
				BaseInfo.subjectPath));
		String line;
		while ((line = br.readLine()) != null) {
			allSubject.add(line);
		}
		br.close();

		br = new BufferedReader(new FileReader(fs));
		while ((line = br.readLine()) != null) {
			finishedSubject.add(line);
		}

		out = new FileOutputStream(new File(fs), true);
	}
}
