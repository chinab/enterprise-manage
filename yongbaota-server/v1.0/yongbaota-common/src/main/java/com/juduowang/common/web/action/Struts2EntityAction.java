package com.juduowang.common.web.action;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;


import com.juduowang.common.service.EntityManager;
import com.juduowang.utils.BeanUtil;
import com.juduowang.utils.Constants;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 负责管理单个Entity CRUD操作的Struts Action基类.
 * <p/>
 * 子类以以下方式声明,并实现将拥有默认的CRUD函数
 * <pre>
 * public class UserAction extends StrutsEntityAction<User, UserManager> {
 * 	private UserManager userManager;
 * <p/>
 * 	public void setUserManager(UserManager userManager) {
 * 		this.userManager = userManager;
 * 	}
 * }
 * </pre>
 * 此类仅演示一种封装的方式，大家可按自己的项目习惯进行重新封装。
 * <p/>
 * 目前封装了：
 * <p/>
 * 1.list、edit、view、save、delete 五种action的流程封装；<br/>
 * 2.doListEntity、doGetEntity、doNewEntity、doSaveEntity(),doDeleteEntity 五种业务函数调用，可在子类重载；<br/>
 * 3.initEntity、initForm两个FormBean与业务对象的初始函数及refrenceData,onInitForm,onInitEntity 三个回调函数；<br/>
 * 4.savedMessage、deletedMessage 两种业务成功信息，可在子类重载。<br/>
 *
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 */
public class Struts2EntityAction<T, PK extends Serializable, M extends EntityManager<T, PK>> extends ActionSupport {
	protected transient final Log log = LogFactory.getLog(getClass());

	private static final long serialVersionUID = -5837536584395505596L;
	public static final String CANCEL = "cancel";

	/**
	 * Action所管理的Entity类型.
	 */
	protected Class<T> entityClass;

	/**
	 * Action所管理的Entity的主键类型
	 */
	@SuppressWarnings("unchecked")
	protected Class idClass;

	private M entityManager;

	private T entity;

	private List<T> entities;

	private PK id;

	protected String from = null;
	protected String cancel = null;
	protected String delete = null;
	protected String save = null;

	static {
		registConverter();
	}

	public String cancel() {
		return CANCEL;
	}

	public String list() {
		entities = entityManager.getAll();
		return SUCCESS;
	}

	public String view() {
		if (id != null) {
			entity = entityManager.get(id);
			return SUCCESS;
		} else {
			return CANCEL;
		}
	}

	public String edit() {
		if (id != null) {
			entity = entityManager.get(id);
		} else {
			try {
				entity = entityClass.newInstance();
			} catch (InstantiationException e) {
				log.error(e);
			} catch (IllegalAccessException e) {
				log.error(e);
			}
		}

		return SUCCESS;
	}

	public String delete() {
		entityManager.remove(entity);
		saveMessage(getText("entity.deleted"));

		return SUCCESS;
	}

	public String save() throws Exception {
		if (cancel != null) {
			return CANCEL;
		}

		if (delete != null) {
			return delete();
		}

		boolean isNew = (BeanUtil.getPropertyValue(entity, Constants.ID) == null);

		entityManager.save(entity);

		String key = (isNew) ? "entity.added" : "entity.updated";
		saveMessage(getText(key));

		if (!isNew) {
			return INPUT;
		} else {
			return SUCCESS;
		}
	}

	/**
	 * 设置Struts 中数字<->字符串转换，字符串为空值时,数字默认为null，而不是0.
	 * 也可以在web.xml中设置struts的参数达到相同效果，在这里设置可以防止用户漏设web.xml.
	 */
	public static void registConverter() {
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new FloatConverter(null), Float.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);
		//		ConvertUtils.register(new DateConverter("yyyy-MM-dd"), Date.class);
		//		ConvertUtils.register(new DateConverter("yyyy-MM-dd HH:mm:ss.S"), Timestamp.class);
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
	 * 获得请求
	 * @return
	 */
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 获得响应
	 * @return
	 */
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * 获得会话
	 * @return
	 */
	protected HttpSession getSession() {
		return getRequest().getSession();
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public void setSave(String save) {
		this.save = save;
	}

	/**
	 * 直接输出纯字符串
	 */
	public void renderText(String text) {
		try {
			getResponse().setContentType("text/plain;charset=UTF-8");
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void renderJSON(String text) {
		try {
			getResponse().setContentType("text/json;charset=UTF-8");
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 直接输出纯HTML
	 */
	public void renderHtml(String text) {
		try {
			getResponse().setContentType("text/html;charset=UTF-8");
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 直接输出纯XML
	 */
	public void renderXML(String text) {
		try {
			getResponse().setContentType("text/xml;charset=UTF-8");
			getResponse().getWriter().write(text);
			getResponse().getWriter().close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	public List<T> getEntities() {
		return entities;
	}

	//	public void setEntities(List<T> entities) {
	//		this.entities = entities;
	//	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	//	public PK getId() {
	//		return id;
	//	}

	public void setId(PK id) {
		this.id = id;
	}

	public M getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(M entityManager) {
		this.entityManager = entityManager;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	@SuppressWarnings("unchecked")
	public Class getIdClass() {
		return idClass;
	}
}
