package com.hbd.cmdb.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Stack;

import com.hbd.cmdb.BaseInfo;
/**
 * create subject.txt
 * @author frank
 *
 */
public class SubjectParser {
	public static void main(String[] args) throws Exception {
		SubjectParser sp = new SubjectParser();
		sp.parse();
	}

	void parse() throws Exception {
		File[] fs = ParserUtil.listSubjectDir();
		FileOutputStream totalOut = new FileOutputStream(new File(
				BaseInfo.output + "total.txt"));
		for (File f : fs) {
			String subPath = f.getAbsolutePath();
			String subFile = subPath + "/subject.html";
			BufferedReader br = new BufferedReader(new FileReader(new File(
					subFile)));
			String line;
			StringBuffer content = new StringBuffer();
			while ((line = br.readLine()) != null) {
				if (line.indexOf("itemreviewed") != -1) {
					content.append(filterTag(line) + ", ");
					if ((line = br.readLine()) != null
							&& line.indexOf("year") != -1) {
						content.append(filterTag(line) + "\r\n");
					}
				}
				if (line.indexOf("\"info\"") != -1 && line.indexOf("div") != -1) {
					splitLine(content);
					content.append(getInfo(br, "div"));
				}
				if (line.indexOf("\"interest_sectl\"") != -1
						&& line.indexOf("div") != -1) {
					splitLine(content);
					content.append(getInfo(br, "div"));
				}
				if (line.indexOf("\"related_info\"") != -1
						&& line.indexOf("div") != -1) {
					splitLine(content);
					content.append(getInfo(br, "div"));
				}
			}
			br.close();
			byte[] out = content.toString().getBytes();
			totalOut.write(out);
			totalOut.write("-----------------\r\n".getBytes());
			FileOutputStream subOut = new FileOutputStream(new File(subPath
					+ "/subject.txt"));
			subOut.write(out);
			subOut.close();
		}
		totalOut.close();
	}

	String getInfo(BufferedReader br, String tag) throws Exception {
		Stack<Boolean> stack = new Stack<Boolean>();
		stack.push(true);
		String line;
		StringBuffer content = new StringBuffer();
		while ((line = br.readLine()) != null) {
			if (line.indexOf("<" + tag) != -1
					&& !(line.indexOf("</" + tag) != -1)) {
				stack.push(true);
			}
			if (!(line.indexOf("<" + tag) != -1)
					&& line.indexOf("</" + tag) != -1) {
				stack.pop();
			}
			if (stack.isEmpty()) {
				break;
			}
			String text = filterTag(line);
			if (text != null && !"".equals(text)) {
				content.append(text + "\r\n");
			}
		}
		return content.toString();
	}

	void splitLine(StringBuffer sb) {
		sb.append("-----\r\n");
	}

	String filterTag(String line) {
		int length = line.length();
		boolean isTag = false;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			if (line.charAt(i) == '<') {
				isTag = true;
			}
			if (!isTag) {
				sb.append(line.charAt(i));
			}
			if (line.charAt(i) == '>' && isTag) {
				isTag = false;
			}
		}
		return sb.toString().trim();
	}
}
