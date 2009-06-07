package com.lineboom.common.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseActionSupport extends ActionSupport{
	protected void renderJSON(String content) {
		String contentType = "text/json;charset=UTF-8";
		renderContent(content, contentType);
	}

	protected void renderHtml(String content) {
		String contentType = "text/html;charset=UTF-8";
		renderContent(content, contentType);
	}

	protected void renderText(String content) {
		String contentType = "text/text;charset=UTF-8";
		renderContent(content, contentType);
	}

	protected void renderXML(String content) {
		String contentType = "text/xml;charset=UTF-8";
		renderContent(content, contentType);
	}

	private void renderContent(String content,
			String contentType) {
		try {
			getResponse().setContentType(contentType);
			getResponse().getWriter().print(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String paramValue(String paramName) {
		String paramValue = getRequest().getParameter(paramName) ;
		if(paramValue==null){
			System.err.println("请传来一个参数"+paramName);
			return null;
		}
		return paramValue;
	}
	/**
	 * getParam
	 * @param paramName
	 * @param dataType
	 * @return
	 */
	protected String getStringParam(String paramName) {
		String paramValue = paramValue(paramName);
		return paramValue;
	}
	protected Long getLongParam(String paramName) {
		String paramValue = paramValue(paramName);
		try {
			Long result = Long.valueOf(paramValue);
			if(result==null) throw new RuntimeException("转换类型错误");
			return result;
		} catch (RuntimeException e) {
			System.err.println("转换类型错误");
		}
		return null;
	}
	protected Double getDoubleParam(String paramName) {
		String paramValue = paramValue(paramName);
		try {
			Double result = Double.valueOf(paramValue);
			if(result==null) throw new RuntimeException("转换类型错误");
			return result;
		} catch (RuntimeException e) {
			System.err.println("转换类型错误");
		}
		return null;
	}//end of getParam

	protected HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	protected HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	protected ServletContext getAppliction(){
		return ServletActionContext.getServletContext();
	}
	protected HttpSession getSession(){
		return getRequest().getSession();
	}
}
