package com.juduowang.common.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import com.juduowang.common.dao.EntityDao;
import com.juduowang.common.dao.support.Page;
import com.juduowang.common.service.EntityManager;


/**
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 * 
 * @param <T>
 * @param <PK>
 */
public class EntityManagerImpl<T, PK extends Serializable> implements
		EntityManager<T, PK> {
	protected final Log log = LogFactory.getLog(getClass());

	protected EntityDao<T, PK> entityDao;

	public EntityManagerImpl(EntityDao<T, PK> entityDao) {
		this.entityDao = entityDao;
	}

	public boolean exists(PK id) {
		return entityDao.exists(id);
	}

	@SuppressWarnings("unchecked")
	public List find(String hql, Object... values) {
		return entityDao.find(hql, values);
	}

	public List<T> find(Criterion... criterion) {
		return entityDao.find(criterion);
	}

	public List<T> findBy(String name, Object value) {
		return entityDao.findBy(name, value);
	}

	public List<T> findByLike(String name, String value) {
		return entityDao.findByLike(name, value);
	}

	public T findUniqueBy(String name, Object value) {
		return entityDao.findUniqueBy(name, value);
	}

	public T get(PK id) {
		return entityDao.get(id);
	}

	public T load(PK id) {
		return entityDao.get(id);
	}

	public T getPrivilege(PK id) {
		return entityDao.getPrivilege(id);
	}

	public T loadPrivilege(PK id) {
		return entityDao.loadPrivilege(id);
	}

	public List<T> getAll() {
		return entityDao.getAll();
	}

	public Page pagedQuery(int pageNo, int pageSize, Object... objects) {
		return entityDao.pagedQuery(pageNo, pageSize, objects);
	}

	public Criteria getEntityCriteria() {
		return entityDao.getCriteria();
	}

	public boolean isNotUnique(T entity, String names) {
		return entityDao.isNotUnique(entity, names);
	}

	public void remove(T entity) {
		entityDao.remove(entity);
	}

	public void removeById(PK id) {
		entityDao.removeById(id);
	}

	public void save(T entity) {
		entityDao.save(entity);
	}

	public void saveOnly(T entity) {
		entityDao.saveOnly(entity);
	}

	public String removePrivilege(T entity) {
		return entityDao.removePrivilege(entity);
	}

	public String removePrivilegeById(PK id) {
		return entityDao.removePrivilegeById(id);
	}

	public String savePrivilege(T entity) {
		// entityDao.save(entity);
		return entityDao.savePrivilege(entity);
	}

	public EntityDao<T, PK> getEntityDao() {
		return entityDao;
	}

	public boolean existsInBlazeds(Object id) {
		if (id instanceof Integer) {
			id = Long.valueOf(id.toString());
		}
		return entityDao.exists((PK) id);
	}

	public List<T> findByInBlazeds(String name, Object value) {
		List<T> findBy = null;
		if (value instanceof Integer) {
			try {
				Long v = Long.valueOf(value.toString());
				findBy = findBy(name, v);
			} catch (Exception e) {
			}
		}

		if (findBy == null)
			return findBy(name, value);
		else
			return findBy;
	}

	public T getInBlazeds(Object id) {
		if (id instanceof Integer) {
			id = Long.valueOf(id.toString());
		}
		return entityDao.get((PK) id);
	}

	public T loadInBlazeds(Object id) {
		if (id instanceof Integer) {
			id = Long.valueOf(id.toString());
		}
		return entityDao.load((PK) id);
	}

	public void removeByIdInBlazeds(Object id) {
		if (id instanceof Integer) {
			id = Long.valueOf(id.toString());
		}
		entityDao.removeById((PK) id);

	}
}
