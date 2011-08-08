package com.juduowang.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.juduowang.utils.DateUtil;
import com.juduowang.utils.FileUtil;



public class LogUtil { 
	private static String today = DateUtil.convertDateToString(new Date(), "yyyy-MM-dd");
	private static String now = DateUtil.convertDateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+":   ";
	private static String EXCEPTION_FILE_PATH = FileUtil.getAppFilesPathByBusiness(FileUtil.EXCEPTION_FILE_PATH);
	private static String DATA_FILE_PATH = FileUtil.getAppFilesPathByBusiness(FileUtil.DATA_FILE_PATH); 
	private static String DOC_FILE_PATH = FileUtil.getAppFilesPathByBusiness(FileUtil.DOC_FILE_PATH);
	private static String MEDIA_FILE_PATH = FileUtil.getAppFilesPathByBusiness(FileUtil.MEDIA_FILE_PATH);
	
	public static void logByDay(String LogContent){
		StringBuffer content = new StringBuffer();
		content.append(now);
		content.append(LogContent);
		content.append("\n");
		String docName = today+"Log.txt";
		
		try {
			FileUtil.appendContentToFile(content.toString().getBytes("GB2312"),EXCEPTION_FILE_PATH +"/log", docName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

