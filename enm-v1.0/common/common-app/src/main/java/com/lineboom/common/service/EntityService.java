package com.lineboom.common.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lineboom.common.model.Page;

/**
 * 公共的service接口
 * @author yinshuwei
 * @param <PT>
 */
public interface EntityService<PT,PK extends Serializable> {
	public void save(PT pojo);
	public void saveAll(Collection<PT> pojos);
	public void saveObject(Object pojo);
	public void saveOrUpdate(PT pojo);
	public void delete(PK id);
	public void delete(Collection<PK> ids);
	public void delete(PT pojo);
	public void deleteAll(Collection<PT> pojos);
	public void update(PT pojo);
	public PT get(PK id);
	public List<PT> getAll();
	public List<PT> getBy(String fieldName, Object obj);
	public List<PT> find(Map<String, Object> map);
	@SuppressWarnings("unchecked")
	public List find(String hql, Object... objs);
	public Page<PT> pagedQuery(int start, int limit);
	public Page<PT> pagedQuery(String fieldName, Object obj, int start, int limit);
	public Page<PT> pagedQuery(Map<String, Object> map, int start, int limit);
	@SuppressWarnings("unchecked")
	public Page pagedQuery(int start, int limit, String hql, Object... objs);
}