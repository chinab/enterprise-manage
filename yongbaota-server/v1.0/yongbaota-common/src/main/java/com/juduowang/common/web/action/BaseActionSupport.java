package com.juduowang.common.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.Criterion;

import com.juduowang.common.service.EntityManager;


public abstract class BaseActionSupport extends BaseCondtionActionSupport{
	private static final long serialVersionUID = 9164354891212588836L;

	protected transient final Log log = LogFactory.getLog(getClass());
	
	/**
	 * 定义struts2的result返回字符串
	 */
	public static final String CANCEL = "cancel";
	public static final String LIST = "list";
	public static final String EDIT = "edit";
	public static final String CREATE = "create";
	public static final String DELETE = "delete";
	public static final String VIEW = "view";
	public static final String DELETE_SUCCESS = "Ext.Msg.alert('提示','删除成功！')";
	public static final String SAVE_SUCCESS = "Ext.Msg.alert('提示','保存成功！')";
	
	protected boolean success;//操作状态，页面保存是否成功，通过此标志标识
	protected String info;
	protected long totalCount;
//	protected int start;
//	protected int limit;

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
	
	@SuppressWarnings("unchecked")
	@Deprecated
	protected Criterion condition(EntityManager manager,String conditions){
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected void saveMessage(String msg) {
		List messages = (List) getRequest().getSession().getAttribute("messages");
		if (messages == null) {
			messages = new ArrayList();
		}
		messages.add(msg);
		getRequest().getSession().setAttribute("messages", messages);
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
	
	/**
	 * 新添加的函数类库
	 * @return
	 */
	
	/**
	 * 
	 */
	
	/**
	 * 函数说明：
		     生成一组连续的整数或其他类型数据(如日期)
		 语法：
		     to(startExp,endExp{,stepExp})
		 参数说明：
		     startExp    整数数据开始的表达式
		     endExp      整数数据结束的表达式 
		     stepExp   整数数据步长的表达式
		 函数示例：
		     to(1,5)=list(1,2,3,4,5) 整数型为INT型 默认递增1
		     to(1,5,2)=list(1,3,5)   to(1,6,2) = list(1,3,5)
		     to(-5,-10,-2)=list(-5,-7,-9) 
		     to(-10,-8)=list(-10,-9,-8)
		     
	 */
	 
	public List<Object> to(int startNum,int endNum,Object ...step){
		System.out.println("1,2,3,4,5"); 
		return null;
	}
	
	/**
	 * 函数说明：
		     生成一组连续的整数或其他类型数据(如日期)
		 语法：
		     to(startDate,endDate,{,dateType},{,stepExp})
		 参数说明：
		     startExp    日期数据开始的表达式
		     endExp      日期数据结束的表达式 
		     dateType    日期数据结束的格式
		     stepExp     日期数据步长的表达式
		     startDate,endDate 都定义成Object型,可以为String,也可以为date型,要用反射机制判断
		     step 默认按每小时计算
		     其他参数这样表示:1就是小时,1/60就是分钟,1/3600就是秒,24就是一天
		     step 如果是两个参数的话,如为(24*30*3,24*30)这样的话,就代表list(第一季度,1月,2月,3月,第二季度,4月,5月,6月,第三季度,7月,8月,9月,第四季度,10月,11月,12月)
		     若开始时间不为某季度的第一月,则list(第一季度,2月,3月,第二季度,4月)
		 函数示例：
		     to(2008-01-01,2008-01-01)=list(0,1,2,3,4,5...24) 日期型 默认递增1小时
		     to(2008-01-01,2008-01-03,24)=list(2008-01-01, 2008-01-02, 2008-01-03)   to(1,6,2) = list(1,3,5)
	 */
	
	public List<Object> to(Object startDate,Object endDate,String dateType,Object...step){
		return null;
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
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
}
