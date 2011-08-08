package com.juduowang.common.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 * @author yinshuwei
 * @author lufushi
 * @author zhuqianjin
 * @version 3.0
 * 
 */
@SuppressWarnings("unchecked")
public interface UniversalManager {

	/**~增，改 **/
	/**
	 * 保存对象:处理插入
	 * @param obj
	 *            保存的对象
	 */
	public Object save(Object obj);
	
	/**
	 * 保存所有对象
	 * @param objects 对象集合
	 */
	public void saveAll(Collection objects);

	/**
	 * 更新对象
	 * @param obj 要更新的对象
	 */
	public void update(Object obj);

	/**
	 * 更新所有对象
	 * @param objects 对象集合
	 */
	public void updateAll(Collection objects);

	/**
	 * 保存or更新对象
	 * @param obj 要更新的对象
	 */
	public void saveOrUpdate(Object obj);

	/**
	 * 保存or更新所有对象
	 * @param objects 对象集合
	 */
	public void saveOrUpdateAll(Collection objects);
	
	/**~删 */
	/**
	 * 根据类名和Id  删除对象
	 * @param entityClass 域对象的类名
	 * @param id 主键值
	 */
	public void delete(Class entityClass, Serializable id);

	/**
	 * 删除对象
	 * @param obj 要删除的对象
	 */
	public void delete(Object obj);

	/**
	 * 删除所有对象
	 * @param objects 对象集合
	 */
	public void deleteAll(Collection objects);

	/**
	 * 根据hql来删除对实体对象
	 * @param hql hql语句
	 */
	public void deleteByHql(String hql,Object... values);
	
	/**~ 查*/
	/**
	 * 加载实体 没有对应id会报错
	 * @param entityClass 域对象的类名
	 * @param id
	 * @return
	 */
	public Object load(Class entityClass, Serializable id);

	/**
	 * 根据类名和Id  没有对应id返回null
	 * 
	 * @param entityClass
	 *            域对象的类名
	 * @param id
	 *            主键值
	 * @return a populated object
	 * @see org.springframework.orm.ObjectRetrievalFailureException
	 */
	public Object get(Class entityClass, Serializable id);
	
	/**
	 * 检索表的所有行，获取所有对象
	 * 
	 * @param entityClass
	 *            域对象的类名
	 * @return List of populated objects
	 */
	public List getAll(Class entityClass);
	
	/**
	 * 根据QBC条件检索表的所有行，获取对象
	 * @param entityClass 域对象的类名
	 * @param criterion QBC查询条件
	 * @return List of populated objects
	 */
	public List findByCriteria(Class entityClass, Criterion... criterion);

	/**
	 * 通过hql进行查询
	 * @param hql hql语句 
	 * @return List of populated objects
	 */
	public List findByHql(String hql);

	/**
	 * 通过hql进行查询
	 * @param hql hql语句 
	 * @param values 参数，条件值
	 * @return List of populated objects
	 */
	public List findByHql(String hql, Object... values);
	
	/**
	 * 通过hql进行查询,只取第一个对象
	 * @param hql hql语句 
	 * @return populated object
	 */
	public Object findOneByHql(String hql);

	/**
	 * 通过hql进行查询,只取第一个对象
	 * @param hql hql语句 
	 * @param values 参数，条件值
	 * @return populated object
	 */
	public Object findOneByHql(String hql, Object... values);
	
	/**
	 * 执行原生的SQL(查询)
	 * @param sql
	 * @return
	 */
	public List findBySql(String sql);
	
	/**
	 * 通过sql进行查询
	 * @param sql sql语句 
	 * @param entityClass 域对象的类名
	 * @return List of populated objects
	 */
	public List findBySql(String sql, Class entityClass);

	/**
	 * 该方法先把hql语句转为sql，最后执行原生SQL，支持各种数据库中的复杂函数，返回为一个list
	 * @param hql hql语句 
	 * @param values 参数，条件值
	 * @return List of populated objects
	 * @throws ClassNotFoundException
	 */
	public List findByHqlUseSql(String hql, Object... values) throws ClassNotFoundException;

	/**
	 * 执行回调函数 要实现HibernateCallback的doInHibernate(Session session)方法
	 * @param action HibernateCallback实现类
	 * @return List集合实例
	 */
	public List executeFind(HibernateCallback action);
}
