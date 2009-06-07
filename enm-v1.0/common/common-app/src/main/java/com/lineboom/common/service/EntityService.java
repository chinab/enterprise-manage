package com.lineboom.common.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lineboom.common.model.Page;

/**
 * 公共的service接口
 * @author yinshuwei
 * @param <PojoType>
 */
public interface EntityService<PojoType> {
	public void save(PojoType pojo);
	public void saveAll(Collection<PojoType> pojos);
	public void saveObject(Object pojo);
	public void saveOrUpdate(PojoType pojo);
	public void delete(Number id);
	public void delete(Collection<Number> ids);
	public void delete(PojoType pojo);
	public void deleteAll(Collection<PojoType> pojos);
	public void update(PojoType pojo);
	public PojoType get(Number id);
	public List<PojoType> getAll();
	public List<PojoType> getBy(String fieldName, Object obj);
	public List<PojoType> find(Map<String, Object> map);
	@SuppressWarnings("unchecked")
	public List find(String hql, Object... objs);
	public Page<PojoType> pagedQuery(int start, int limit);
	public Page<PojoType> pagedQuery(String fieldName, Object obj, int start, int limit);
	public Page<PojoType> pagedQuery(Map<String, Object> map, int start, int limit);
	@SuppressWarnings("unchecked")
	public Page pagedQuery(int start, int limit, String hql, Object... objs);
}