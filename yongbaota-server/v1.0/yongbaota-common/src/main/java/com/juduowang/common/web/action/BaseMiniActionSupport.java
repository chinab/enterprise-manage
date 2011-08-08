package com.juduowang.common.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.juduowang.common.service.EntityManager;


public abstract class BaseMiniActionSupport extends BaseCondtionActionSupport{
	private static final long serialVersionUID = 9164354891212588836L;

	protected transient final Log log = LogFactory.getLog(getClass());
	
	/**
	 * 获得URL路径，如rp3中可以获得http://192.168.32.99:8080/
	 * @return 目录路径
	 */
	protected String getRealUrlRoot(){
		StringBuffer requestURL = getRequest().getRequestURL();
		String servletPath = getRequest().getServletPath();
		return requestURL.substring(0, requestURL.indexOf(servletPath));
	}
	
	private String paramValue(String paramName) {
		String paramValue = getRequest().getParameter(paramName) ;
		if(paramValue==null){
			System.err.println("获取页面参数"+paramName+"失败");
			return null;
		}
		return paramValue;
	}
	
	/**
	 * getParam 从页面获取不同类型参数（String,Long,Integer,Double,Boolean）
	 * @param paramName
	 * @param dataType
	 * @return 获得的参数值
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
	protected Integer getIntegerParam(String paramName) {
		String paramValue = paramValue(paramName);
		try {
			Integer result = Integer.valueOf(paramValue);
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
	}
	protected Boolean getBooleanParam(String paramName){
		String paramValue = paramValue(paramName);
		try {
			Boolean result = Boolean.valueOf(paramValue);
			if(result==null) throw new RuntimeException("转换类型错误");
			return result;
		} catch (RuntimeException e) {
			System.err.println("转换类型错误");
		}
		return false;
	}//end of getParam
	

	/**
	 * 用登陆者的信息来获得他所在的组织单位的ID
	 * @return 组织单位ID
	 */
	protected Long getUserId(){
		try {
			String userInfo = (String) getSession().getAttribute("RC3UserInfo");
			JSONObject object;
			object = JSONObject.fromObject(userInfo);
			return Long.valueOf(object.get("userId").toString());
		} catch (RuntimeException e) {
			System.err.println("请登录,没有获取到登录信息");
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected boolean deleteEntity(EntityManager manager,String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		for(int i = 0;i < jsonArray.size();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String pk = jsonObject.get("id").toString();
			manager.removeById(new Long(pk));
		}

		return true;
	}
	
	@SuppressWarnings("unchecked")
	protected String deletePrivilegeEntity(EntityManager manager,String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String noPrivilege = null;

		for(int i = 0;i < jsonArray.size();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String pk = jsonObject.get("id").toString();
			String tag = manager.removePrivilegeById(new Long(pk));
			if (tag.equals("success")) {
				//this.renderJSON("{success:true,info:'删除成功!'}");
			} else if (tag.equals("fail")){
				if (noPrivilege == null) {
					noPrivilege = pk;
				} else {
					noPrivilege = noPrivilege + "," + pk;
				}
				//this.renderJSON("{success:'fail',info:'删除成功!'}");
			}
		}

		return noPrivilege;
	}

	/**
	 * 从容器的上下文中，获得request
	 * 
	 * @return request
	 */
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 从容器的上下文中，获得response
	 * 
	 * @return response
	 */
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * 从容器的上下文中，获得Session
	 * 
	 * @return Session
	 */
	protected HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 直接输出纯字符串
	 * @param text 需要输出的字串文本
	 */
	public void renderText(String text) {
		try {
			getResponse().setContentType("text/plain;charset=UTF-8");
			getResponse().addHeader("Content-disposition", "inline;");
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 直接输出纯字符串
	 * 并下载保存
	 * @param text 需要输出的字串文本
	 */
	public void renderText(String text,String filename) {
		try {
			getResponse().setContentType("text/plain;charset=UTF-8");
			getResponse().addHeader("Content-disposition", "attachment;filename="+filename);
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 把字串以HTML的方式，输出到response中
	 * @param text 需要输出的字串文本
	 */
	public void renderHtml(String text) {
		try {
			getResponse().setContentType("text/html;charset=UTF-8");
			getResponse().addHeader("Content-disposition", "inline;");
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 把字串以HTML的方式，输出到response中<br>
	 * 并下载保存
	 * @param text 需要输出的字串文本
	 */
	public void renderHtml(String text,String filename) {
		try {
			getResponse().setContentType("text/html;charset=UTF-8");
			getResponse().addHeader("Content-disposition", "attachment;filename="+filename);
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 把字串以XML的方式，输出到response中<p>
	 * 仅对xml字串
	 * @param text 需要输出的字串文本
	 */
	public void renderXML(String text) {
		try {
			getResponse().setContentType("text/xml;charset=UTF-8");
			getResponse().addHeader("Content-disposition", "inline;");
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 把字串以XML的方式，输出到response中<p>
	 * 仅对xml字串
	 * 并下载保存
	 * @param text 需要输出的字串文本
	 */
	public void renderXML(String text,String filename) {
		try {
			getResponse().setContentType("text/xml;charset=UTF-8");
			getResponse().addHeader("Content-disposition", "attachment;filename="+filename);
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 把字串以JSON的方式，输出到response中，以便ext中使用
	 * @param text 需要输出的字串文本
	 */
	public void renderJSON(String text) {
		try {
			getResponse().setContentType("text/plain;charset=UTF-8");
			getResponse().addHeader("Content-disposition", "inline;");
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 把字串以JSON的方式，输出到response中，以便ext中使用
	 * 并下载保存
	 * @param text 需要输出的字串文本
	 */
	public void renderJSON(String text,String filename) {
		try {
			getResponse().setContentType("text/x-json;charset=UTF-8");
			getResponse().addHeader("Content-disposition", "attachment;filename="+filename);
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	
	/****
	 * 从登录信息RC3UserInfo获得userId
	 * @return
	 */
	public Long getRC3UserId(){
		Long userId = null;
		JSONObject useInfo = JSONObject.fromObject(this.getSession()
				.getAttribute("RC3UserInfo"));
		if (useInfo != null) {
			userId = Long.valueOf(useInfo.get("userId").toString());
		} else {
			System.out.println("RC3UserInfo没有用户的登录信息，请登录！");
		}
		return userId;
	}
	
	/***
	 * 从登录信息RC3UserInfo获得organizeId
	 * @return
	 */
	public Long getOrganizeId(){
		Long userId = null;
		JSONObject useInfo = JSONObject.fromObject(this.getSession()
				.getAttribute("RC3UserInfo"));
		if (useInfo != null) {
			userId = Long.valueOf(useInfo.get("organizeId").toString());
		} else {
			System.out.println("RC3UserInfo没有用户的登录信息，请登录！");
		}
		return userId;
	}
	
	/****
	 * 获得session中RC3UserInfo中各个属性的值
	 * @param field 属性的名称
	 * @return
	 */
	public String getInfoFromRC3Info(String field){
		String userId = null;
		JSONObject useInfo = JSONObject.fromObject(this.getSession()
				.getAttribute("RC3UserInfo"));
		if (useInfo != null) {
			userId = useInfo.get(field).toString();
		} else {
			System.out.println("RC3UserInfo没有用户的登录信息，请登录！");
		}
		return userId;
	}
	
	/****
	 * 获得session中COMPANY_INFO中各个属性的值
	 * @param field 属性的名称
	 * @return
	 */
	public String getInfoFromCompanyInfo(String field){
		String companyInfoId = null;
		JSONObject companyInfo = JSONObject.fromObject(this.getSession()
				.getAttribute("COMPANY_INFO"));
		if (companyInfo != null) {
			companyInfoId = companyInfo.get(field).toString();
		} else {
			System.out.println("COMPANY_INFO没有公司信息，请设置！");
		}
		return companyInfoId;
	}
	
	/****
	 * 从COMPANY_INFO获取公司的Id
	 * @return
	 */
	public Long getCompanyInfoId(){
		Long companyInfoId = null;
		JSONObject companyInfo = JSONObject.fromObject(this.getSession()
				.getAttribute("COMPANY_INFO"));
		if (companyInfo != null) {
			companyInfoId = Long.valueOf(companyInfo.get("id").toString());
		} else {
			System.out.println("COMPANY_INFO没有公司信息，请设置！");
		}
		return companyInfoId;
	}
	
}