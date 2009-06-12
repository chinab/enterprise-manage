package com.lineboom.common.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lineboom.common.dao.EntityDao;
import com.lineboom.common.model.Page;

/**
 * 公共的service实现类
 * @author yinshuwei
 * @param <PojoType>
 */
public class EntityServiceImpl<PojoType> {
	private EntityDao<PojoType> entityDao;
	private String pojoName;
	
	public void save(PojoType pojo) {
		entityDao.setPojoName(pojoName).save(pojo);
	}
	
	public void saveAll(Collection<PojoType> pojos){
		entityDao.saveAll(pojos);
	}
	
	public void saveObject(Object pojo){
		entityDao.saveObject(pojo);
	}
	
	public void saveOrUpdate(PojoType pojo) {
		entityDao.saveOrUpdate(pojo);
	}
	
	public void delete(Number id) {
		entityDao.setPojoName(pojoName).delete(id);
	}

	public void delete(Collection<Number> ids){
		entityDao.setPojoName(pojoName).delete(ids);
	}
	
	public void delete(PojoType pojo) {
		entityDao.delete(pojo);
	}
	
	public void deleteAll(Collection<PojoType> pojos){
		entityDao.deleteAll(pojos);
	}
	
	public void update(PojoType pojo) {
		entityDao.update(pojo);
	}

	public PojoType get(Number id) {
		return entityDao.setPojoName(pojoName).get(id);
	}

	public List<PojoType> getAll() {
		return entityDao.setPojoName(pojoName).getAll();
	}

	public List<PojoType> getBy(String fieldName, Object obj) {
		return entityDao.setPojoName(pojoName).getBy(fieldName, obj);
	}

	public List<PojoType> find(Map<String, Object> map) {
		return entityDao.setPojoName(pojoName).find(map);
	}
	
	@SuppressWarnings("unchecked")
	public List find(String hql, Object... objs) {
		return entityDao.find(hql, objs);
	}
	
	public Page<PojoType> pagedQuery(int start, int limit) {
		return entityDao.setPojoName(pojoName).pagedQuery(start, limit);
	}
	
	public Page<PojoType> pagedQuery(String fieldName, Object obj, int start, int limit){
		return entityDao.setPojoName(pojoName).pagedQuery(fieldName, obj, start, limit);
	}
	
	public Page<PojoType> pagedQuery(Map<String, Object> map, int start, int limit){
		return entityDao.setPojoName(pojoName).pagedQuery(map, start, limit);
	}
	
	@SuppressWarnings("unchecked")
	public Page pagedQuery(int start, int limit, String hql,
			Object... objs) {
		return entityDao.pagedQuery(start, limit, hql, objs);
	}
		
	public void setEntityDao(EntityDao<PojoType> entityDao) {
		this.entityDao = entityDao;
	}

	@SuppressWarnings("unchecked")
	public void setPojoClass(Class pojoClass) {
		this.pojoName = pojoClass.getCanonicalName();
	}
}
