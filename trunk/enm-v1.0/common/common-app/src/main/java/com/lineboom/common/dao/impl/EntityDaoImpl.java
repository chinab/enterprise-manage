package com.lineboom.common.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lineboom.common.dao.EntityDao;
import com.lineboom.common.model.Page;

/**
 * 公共dao实现类
 * @author yinshuwei
 * @param <PojoType>
 */
public class EntityDaoImpl<PojoType> extends HibernateDaoSupport implements EntityDao<PojoType>{
	private String pojoName;
	
	public void save(PojoType pojo) {
		getHibernateTemplate().save(pojo);
	}

	public void saveAll(Collection<PojoType> pojos){
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		for (PojoType pojo : pojos) {
			session.saveOrUpdate(pojo);
		}
		transaction.commit();
		session.flush();
		session.close();
		
	}
	
	public void saveObject(Object pojo){
		getHibernateTemplate().save(pojo);
	}
	
	public void saveOrUpdate(PojoType pojo) {
		try {
			getHibernateTemplate().saveOrUpdate(pojo);
		} catch (DataAccessException e) {
			throw new EntityDaoNotFindIdException("pojo exits a id is not find in dataBase.");
		}
	}
	public void delete(Number id) {
		getHibernateTemplate().delete(getHibernateTemplate().load(pojoName, id));
	}
	
	public void delete(Collection<Number> ids){
		if(ids==null || ids.size()<1) return;
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		for (Number id : ids) {
			session.delete(session.load(pojoName, id));
		}
		transaction.commit();
		session.flush();
		session.close();
	}

	public void delete(PojoType pojo) {
		getHibernateTemplate().delete(pojo);
	}
	
	public void deleteAll(Collection<PojoType> pojos) {
		getHibernateTemplate().deleteAll(pojos);
	}
	
	public void update(PojoType pojo) {
		getHibernateTemplate().update(pojo);
	}

	@SuppressWarnings("unchecked")
	public PojoType get(Number id) {
		return (PojoType)getHibernateTemplate().get(pojoName, id);
	}

	@SuppressWarnings("unchecked")
	public List<PojoType> getAll() {
		return getHibernateTemplate().find("from "+pojoName);
	}

	@SuppressWarnings("unchecked")
	public List<PojoType> getBy(String fieldName, Object obj) {
		String hql = "from "+pojoName+" obj where obj."+fieldName+"=?";
		return getHibernateTemplate().find(hql, obj);
	}

	@SuppressWarnings("unchecked")
	public List<PojoType> find(Map<String, Object> map) {
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
	public Page<PojoType> pagedQuery(int start, int limit) {
		String hql = "from "+pojoName+" obj";
		return (Page<PojoType>) getPage(start, limit, hql);
	}

	@SuppressWarnings("unchecked")
	public Page<PojoType> pagedQuery( String fieldName, Object obj, int start, int limit) {
		String hql = "from "+pojoName+" obj where obj."+fieldName+"=?";
		return (Page<PojoType>) getPage(start, limit, hql ,obj);
	}

	@SuppressWarnings("unchecked")
	public Page<PojoType> pagedQuery(Map<String, Object> map, int start, int limit) {
		String hql = "from "+pojoName+" obj where ";
		Object []objs = new Object[map.size()];
		int i=0;
		for (String	key : map.keySet()) {
			hql = hql + "obj."+key+"=?  and ";
			objs[i++] = map.get(key);
		}
		hql = hql +"1=1";
		return (Page<PojoType>) getPage(start, limit, hql , objs);
	}

	@SuppressWarnings("unchecked")
	public Page pagedQuery(int start, int limit, String hql, Object... objs) {
		return (Page) getPage(start, limit, hql, objs);
	}
	
	@SuppressWarnings("unchecked")
	private Page getPage(int start, int limit, String hql ,Object... objs) {
		Page page = new Page();
		List pojoTypes = null;
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
		pojoTypes = query.list();
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
		pojoTypes = query.list();
		transaction.commit();
		session.flush();
		session.close();
		
		
		page.setData(pojoTypes);
		page.setPageNo(start/limit+1);
		page.setTotalCount(totalCount);
		return page;
	}

	public EntityDao<PojoType> setPojoName(String pojoName) {
		this.pojoName = pojoName;
		return this;
	}
}
