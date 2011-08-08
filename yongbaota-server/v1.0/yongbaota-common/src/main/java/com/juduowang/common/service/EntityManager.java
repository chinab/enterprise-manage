package com.juduowang.common.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import com.juduowang.common.dao.support.Page;

/**
 * 管理基础类
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 *
 * @param <T>
 * @param <PK>
 */
public interface EntityManager<T, PK extends Serializable> {
	/**
	 * 获取全部对象，
	 * 相对于一个表中所有的记录
	 * 
	 * @return 对象列表
	 */	
	public List<T> getAll();
	
	public Page pagedQuery(int pageNo,int pageSize, Object... objects);

	/**
	 * 根据ID获取对象
	 * 
	 * @param id 对象主键
	 * @return 对象
	 * @see org.springframework.orm.ObjectRetrievalFailureException
	 */
	public T get(PK id);
	
	public T load(PK id);
	
	/**
	 * 根据ID获取对象
	 * 
	 * @param id 对象主键
	 * @return 对象
	 * @see org.springframework.orm.ObjectRetrievalFailureException
	 */
	public T getPrivilege(PK id);
	
	public T loadPrivilege(PK id);
	
	/**
	 * 根据ID判断对象是否存在
	 * 
	 * @param id
	 * @return
	 */
	public boolean exists(PK id);

	/**
	 * 保存对象:处理插入和更新
	 * 
	 * @param entity 需要保存的对象
	 */
	public void save(T entity);
	
	/**
     * 保存对象:只处理插入
     * 
     * @param entity 需要保存的对象
     */
	public void saveOnly(T entity);
	
	/**
     * 删除对象
     * 
     * @param entity 将删除的对象
     */
	public void remove(T entity);
	
	/**
     * 根据ID删除对象
     * 
     * @param id 将删除的对象的ID
     */
	public void removeById(PK id);	
	
	/**
     * 保存对象:处理插入和更新
     * 
     * @param entity 需要保存的对象
     */
	public String savePrivilege(T entity);
	
	/**
     * 删除对象
     * 
     * @param entity 将删除的对象
     */
	public String removePrivilege(T entity);
	
	/**
     * 根据ID删除对象
     * 
     * @param id 将删除的对象的ID
     */
	public String removePrivilegeById(PK id);
	
	/**
	 * 创建Criteria对象
	 *
	 * @param criterion 可变条件列表,Restrictions生成的条件
	 */
	public Criteria getEntityCriteria();
	
	/**
	 * hql查询.
	 *
	 * @param values 可变参数
	 *               用户可以如下四种方式使用
	 *               dao.find(hql)
	 *               dao.find(hql,arg0);
	 *               dao.find(hql,arg0,arg1);
	 *               dao.find(hql,new Object[arg0,arg1,arg2])
	 */
	@SuppressWarnings("unchecked")
	public List find(String hql, Object... values);
	
	/**
	 * 动态查询，如果不需要排序，则设置第一个参数为null
	 * @param order
	 * @param criterion
	 * @return
	 */
	public List<T> find(Criterion... criterion);
	
	/**
	 * 根据属性名和属性值查询对象.
	 *
	 * @return 符合条件的对象列表
	 */
	public List<T> findBy(String name, Object value);

	/**
	 * 根据属性名和属性值查询唯一对象.
	 *
	 * @return 符合条件的唯一对象
	 */
	public T findUniqueBy(String name, Object value);
	
	/**
	 * 根据属性名和属性值以Like AnyWhere方式查询对象.
	 */
	public List<T> findByLike(String name, String value);

	/**
	 * 判断对象某些属性的值在数据库中不存在重复
	 *
	 * @param names 在POJO里不能重复的属性列表,以逗号分割
	 *              如"name,loginid,password"
	 */
	public boolean isNotUnique(T entity, String names);
	
//	/*****
//	 * 根据给定的条件利用Java反射机制动态的构造异步列树
//	 * @param nodeId 节点编码 
//	 * @param idFieleName 显示的树节点Id的字段名称
//	 * @param parentFieldName 上级字段名称
//	 * @param displayName 节点显示名称的字段
//	 * @param className 要构造树的表对象的model名称
//	 * @param orderByFieldName 排序字段名称
//	 * @param modeList
//	 * @param fieldList
//	 * @param associatedFieldList
//	 * @return
//	 * @throws SecurityException
//	 * @throws NoSuchMethodException
//	 * @throws IllegalArgumentException
//	 * @throws IllegalAccessException
//	 * @throws InvocationTargetException
//	 */
//	public ColumnEditorTreeNode getModelColumnTreeNode(Long nodeId, String idFieleName, String parentFieldName, String displayName, String className, String orderByFieldName, List<String> modeList, List<String> fieldList, List<String> associatedFieldList) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;

	/**
	 * 根据ID获取对象 InBlazds
	 * 
	 * @param id 对象主键
	 * @return 对象
	 * @see org.springframework.orm.ObjectRetrievalFailureException
	 */
	public T getInBlazeds(Object id);
	
	public T loadInBlazeds(Object id);
	
	/**
	 * 根据ID判断对象是否存在 InBlazds
	 * 
	 * @param id
	 * @return
	 */
	public boolean existsInBlazeds(Object id);

	/**
     * 根据ID删除对象 InBlazds
     * 
     * @param id 将删除的对象的ID
     */
	public void removeByIdInBlazeds(Object id);
	
	/**
	 * 根据属性名和属性值查询对象. InBlazds
	 *
	 * @return 符合条件的对象列表
	 */
	public List<T> findByInBlazeds(String name, Object value);

}
