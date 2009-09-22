package com.lineboom.common.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lineboom.common.dao.VarEntityDao;
import com.lineboom.common.model.Page;

/**
 * 公共dao实现类
 * @author yinshuwei
 * @param <PT>
 */
public class VarEntityDaoImpl<PT,PK extends Serializable> extends HibernateDaoSupport implements VarEntityDao<PT, PK>{
	private String pojoName;
	
	public void save(PT pojo) {
		getHibernateTemplate().save(pojo);
	}

	public void saveAll(Collection<PT> pojos){
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		for (PT pojo : pojos) {
			session.saveOrUpdate(pojo);
		}
		transaction.commit();
		session.flush();
		session.close();
		
	}
	
	public void saveObject(Object pojo){
		getHibernateTemplate().save(pojo);
	}
	
	public void saveOrUpdate(PT pojo) {
		try {
			getHibernateTemplate().saveOrUpdate(pojo);
		} catch (DataAccessException e) {
			throw new EntityDaoNotFindIdException("pojo exits a id is not find in dataBase.");
		}
	}
	public void delete(PK id) {
		getHibernateTemplate().delete(getHibernateTemplate().load(pojoName, id));
	}
	
	public void delete(Collection<PK> ids){
		if(ids==null || ids.size()<1) return;
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		for (PK id : ids) {
			session.delete(session.load(pojoName, id));
		}
		transaction.commit();
		session.flush();
		session.close();
	}

	public void delete(PT pojo) {
		getHibernateTemplate().delete(pojo);
	}
	
	public void deleteAll(Collection<PT> pojos) {
		getHibernateTemplate().deleteAll(pojos);
	}
	
	public void update(PT pojo) {
		getHibernateTemplate().update(pojo);
	}

	@SuppressWarnings("unchecked")
	public PT get(PK id) {
		return (PT)getHibernateTemplate().get(pojoName, id);
	}

	@SuppressWarnings("unchecked")
	public List<PT> getAll() {
		return getHibernateTemplate().find("from "+pojoName);
	}

	@SuppressWarnings("unchecked")
	public List<PT> getBy(String fieldName, Object obj) {
		String hql = "from "+pojoName+" obj where obj."+fieldName+"=?";
		return getHibernateTemplate().find(hql, obj);
	}

	@SuppressWarnings("unchecked")
	public List<PT> find(Map<String, Object> map) {
		String hql = "from "+pojoName+" obj where ";
		Object []objs = new Object[map.size()];
		int i=0;
		for (String	key : map.keySet()) {
			hql = hql + "obj."+key+"=?  and ";
			objs[i++] = map.get(key);
		}
		hql = hql +"1=1";
		return getHibernateTemplate().find(hql, objs);
	}
	
	@SuppressWarnings("unchecked")
	public List find(String hql, Object... objs) {
		return getHibernateTemplate().find(hql, objs);
	}

	/**
	 * 分页的重载方法
	 */
	@SuppressWarnings("unchecked")
	public Page<PT> pagedQuery(int start, int limit) {
		String hql = "from "+pojoName+" obj";
		return (Page<PT>) getPage(start, limit, hql);
	}

	@SuppressWarnings("unchecked")
	public Page<PT> pagedQuery( String fieldName, Object obj, int start, int limit) {
		String hql = "from "+pojoName+" obj where obj."+fieldName+"=?";
		return (Page<PT>) getPage(start, limit, hql ,obj);
	}

	@SuppressWarnings("unchecked")
	public Page<PT> pagedQuery(Map<String, Object> map, int start, int limit) {
		String hql = "from "+pojoName+" obj where ";
		Object []objs = new Object[map.size()];
		int i=0;
		for (String	key : map.keySet()) {
			hql = hql + "obj."+key+"=?  and ";
			objs[i++] = map.get(key);
		}
		hql = hql +"1=1";
		return (Page<PT>) getPage(start, limit, hql , objs);
	}

	@SuppressWarnings("unchecked")
	public Page pagedQuery(int start, int limit, String hql, Object... objs) {
		return (Page) getPage(start, limit, hql, objs);
	}
	
	@SuppressWarnings("unchecked")
	private Page getPage(int start, int limit, String hql ,Object... objs) {
		Page page = new Page();
		List PTs = null;
		int totalCount = 0;
		HibernateTemplate hibernateTemplate = getHibernateTemplate();
		String hqlCount = "select count(*) " + hql.substring(hql.toLowerCase().indexOf("from"));
		List<Long>  ints = hibernateTemplate.find(hqlCount ,objs);
		if (ints.size()>0 && ints.get(0)!=null) {
			totalCount = (int)ints.get(0).longValue();
		}
		/**下面这段程序没有关闭session,导致后面没法执行
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < objs.length; i++) {
			query.setParameter(i, objs[i]);
		}
		query.setFirstResult(start);
		query.setMaxResults(limit);
		PTs = query.list();
		**/
		
		/**下面的代码已经关闭session),运行正确**/
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery(hql);
		for (int i = 0; i < objs.length; i++) {
			query.setParameter(i, objs[i]);
		}
		query.setFirstResult(start);
		query.setMaxResults(limit);
		PTs = query.list();
		transaction.commit();
		session.flush();
		session.close();
		
		
		page.setData(PTs);
		page.setPageNo(start/limit+1);
		page.setTotalCount(totalCount);
		return page;
	}

	public VarEntityDao<PT,PK> setPojoName(String pojoName) {
		this.pojoName = pojoName;
		return this;
	}
	
	public VarEntityDao<PT,PK> setPojoClass(Class<PT> pojoClass) {
		this.pojoName = pojoClass.getCanonicalName();
		return this;
	}
}
