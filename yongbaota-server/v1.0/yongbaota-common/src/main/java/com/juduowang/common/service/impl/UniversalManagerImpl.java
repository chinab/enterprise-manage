package com.juduowang.common.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.juduowang.common.dao.UniversalDao;
import com.juduowang.common.service.UniversalManager;


/**
 * 业务逻辑基础Service类 提供一组有用方法，实现普通的CRUD操作
 * 
 * <p>
 * <a href="UniversalManagerImpl.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 * @author yinshuwei
 * @author lufushi
 * @author zhuqianjin
 * @version 3.0
 */
@SuppressWarnings("unchecked")
public class UniversalManagerImpl implements UniversalManager {
	protected final Log log = LogFactory.getLog(getClass());
	protected UniversalDao dao;

	public void setUniversalDao(UniversalDao dao) {
		this.dao = dao;
	}

	public void setDao(UniversalDao dao) {
		this.dao = dao;
	}

	public void delete(Class entityClass, Serializable id) {
		dao.delete(entityClass, id);
	}

	public void delete(Object obj) {
		dao.delete(obj);
	}

	public void deleteAll(Collection objects) {
		dao.deleteAll(objects);
		
	}

	public void deleteByHql(String hql,Object... values) {
		dao.deleteByHql(hql,values);
		
	}

	public List executeFind(HibernateCallback action) {
		return dao.executeFind(action);
	}

	public List findByCriteria(Class entityClass, Criterion... criterion) {
		return dao.findByCriteria(entityClass, criterion);
	}

	public List findByHql(String hql) {
		return dao.findByHql(hql);
	}

	public List findByHql(String hql, Object... values) {
		return dao.findByHql(hql, values);
	}

	public List findByHqlUseSql(String hql, Object... values)
			throws ClassNotFoundException {
		return dao.findByHqlUseSql(hql, values);
	}

	public List findBySql(String sql) {
		return dao.findBySql(sql);
	}

	public List findBySql(String sql, Class entityClass) {
		return dao.findBySql(sql, entityClass);
	}

	public Object findOneByHql(String hql) {
		return dao.findOneByHql(hql);
	}

	public Object findOneByHql(String hql, Object... values) {
		return dao.findOneByHql(hql, values);
	}

	public Object get(Class entityClass, Serializable id) {
		return dao.get(entityClass, id);
	}

	public List getAll(Class entityClass) {
		return dao.getAll(entityClass);
	}
	
	public Object load(Class entityClass, Serializable id) {
		return dao.load(entityClass, id);
	}

	public Object save(Object obj) {
		return dao.save(obj);
	}

	public void saveAll(Collection objects) {
		dao.saveAll(objects);
	}

	public void saveOrUpdate(Object obj) {
		dao.saveOrUpdate(obj);
	}

	public void saveOrUpdateAll(Collection objects) {
		dao.saveOrUpdateAll(objects);
	}

	public void update(Object obj) {
		dao.update(obj);
	}

	public void updateAll(Collection objects) {
		dao.updateAll(objects);
	}
}
