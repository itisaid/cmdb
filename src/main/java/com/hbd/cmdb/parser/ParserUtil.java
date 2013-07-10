package com.hbd.cmdb.parser;

import java.io.File;

import com.hbd.cmdb.BaseInfo;

public class ParserUtil {

	
	public static File[] listSubjectDir(){
		File dir = new File(BaseInfo.output);
		File[] fs = dir.listFiles();
		return fs;
	}
}
