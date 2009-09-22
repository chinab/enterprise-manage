package com.lineboom.common.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lineboom.common.dao.VarEntityDao;
import com.lineboom.common.model.Page;

/**
 * 公共的service实现类
 * @author yinshuwei
 * @param <PojoType>
 */
public class EntityServiceImpl<PojoType,PK extends Serializable> {
	private VarEntityDao<PojoType,PK> varEntityDao;
	private String pojoName;
	
	public void save(PojoType pojo) {
		varEntityDao.setPojoName(pojoName).save(pojo);
	}
	
	public void saveAll(Collection<PojoType> pojos){
		varEntityDao.saveAll(pojos);
	}
	
	public void saveObject(Object pojo){
		varEntityDao.saveObject(pojo);
	}
	
	public void saveOrUpdate(PojoType pojo) {
		varEntityDao.saveOrUpdate(pojo);
	}
	
	public void delete(PK id) {
		varEntityDao.setPojoName(pojoName).delete(id);
	}

	public void delete(Collection<PK> ids){
		varEntityDao.setPojoName(pojoName).delete(ids);
	}
	
	public void delete(PojoType pojo) {
		varEntityDao.delete(pojo);
	}
	
	public void deleteAll(Collection<PojoType> pojos){
		varEntityDao.deleteAll(pojos);
	}
	
	public void update(PojoType pojo) {
		varEntityDao.update(pojo);
	}

	public PojoType get(PK id) {
		return varEntityDao.setPojoName(pojoName).get(id);
	}

	public List<PojoType> getAll() {
		return varEntityDao.setPojoName(pojoName).getAll();
	}

	public List<PojoType> getBy(String fieldName, Object obj) {
		return varEntityDao.setPojoName(pojoName).getBy(fieldName, obj);
	}

	public List<PojoType> find(Map<String, Object> map) {
		return varEntityDao.setPojoName(pojoName).find(map);
	}
	
	@SuppressWarnings("unchecked")
	public List find(String hql, Object... objs) {
		return varEntityDao.find(hql, objs);
	}
	
	public Page<PojoType> pagedQuery(int start, int limit) {
		return varEntityDao.setPojoName(pojoName).pagedQuery(start, limit);
	}
	
	public Page<PojoType> pagedQuery(String fieldName, Object obj, int start, int limit){
		return varEntityDao.setPojoName(pojoName).pagedQuery(fieldName, obj, start, limit);
	}
	
	public Page<PojoType> pagedQuery(Map<String, Object> map, int start, int limit){
		return varEntityDao.setPojoName(pojoName).pagedQuery(map, start, limit);
	}
	
	@SuppressWarnings("unchecked")
	public Page pagedQuery(int start, int limit, String hql,
			Object... objs) {
		return varEntityDao.pagedQuery(start, limit, hql, objs);
	}

	public void setVarEntityDao(VarEntityDao<PojoType, PK> varEntityDao) {
		this.varEntityDao = varEntityDao;
	}

	@SuppressWarnings("unchecked")
	public void setPojoClass(Class pojoClass) {
		this.pojoName = pojoClass.getCanonicalName();
	}
}
