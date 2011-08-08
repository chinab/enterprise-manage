package com.juduowang.common.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;

import com.juduowang.common.dao.support.Page;


/**
 * Dao泛型基类
 * <p>
 * 继承此类，可以实现单表的增删改查
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 * 
 */
public interface EntityDao<T, PK extends Serializable> {
	/**
	 * 获取全部对象，相对于一个表中所有的记录
	 * 
	 * @return 对象列表
	 */
	public List<T> getAll();

	/**
	 * 根据ID获取对象
	 * 
	 * @param id
	 *            对象主键
	 * @return 对象
	 */
	public T get(PK id);

	/**
	 * 根据主键值load对象
	 * 
	 * @param id
	 *            对象主键
	 * @return 实体类对象
	 */
	public T load(PK id);

	/**
	 * 根据ID获取对象
	 * 
	 * @param id
	 *            对象主键
	 * @return 对象
	 */
	public T getPrivilege(PK id);

	/**
	 * 根据主键值load对象
	 * 
	 * @param id
	 *            对象主键
	 * @return 实体类对象
	 */
	public T loadPrivilege(PK id);

	/**
	 * 根据ID判断对象是否存在
	 * 
	 * @param id
	 * @return true:对象存在 false:对象不存在
	 */
	public boolean exists(PK id);

	/**
	 * 保存对象:处理插入和更新
	 * 
	 * @param entity
	 *            需要保存的对象
	 */
	public void save(T entity);

	/**
	 * 保存对象:处理插入和更新,不一定是本实体
	 * 
	 * @param entity
	 *            需要保存的对象
	 */
	public void saveObject(Object entity);

	/**
	 * 保存对象:只处理插入
	 * 
	 * @param entity
	 *            需要保存的对象
	 */
	public void saveOnly(T entity);

	/**
	 * 保存对象:处理插入和更新
	 * 
	 * @param entity
	 *            需要保存的对象
	 */
	public String savePrivilege(T entity);

	/**
	 * 游离或者自由状态下的实例可以通过调用merge()方法成为一个新的持久化实例。
	 * <p>
	 * 
	 * 
	 * 自由状态（transient）: 不曾进行持久化，未与任何Session相关联
	 * <p>
	 * 持久化状态（persistent）: 仅与一个Session相关联
	 * <p>
	 * 游离状态（detached）: 已经进行过持久化，但当前未与任何Session相关联
	 * <p>
	 * 游离状态的实例可以通过调用save()、persist()或者saveOrUpdate()方法进行持久化。
	 * <p>
	 * 持久化实例可以通过调用delete()变成游离状态。
	 * <p>
	 * 通过get()或load()方法得到的实例都是持久化状态的。
	 * <p>
	 * 游离状态的实例可以通过调用update()、0saveOrUpdate()、lock()或者replicate()进行持久化。
	 * <p>
	 * 游离或者自由状态下的实例可以通过调用merge()方法成为一个新的持久化实例。
	 * <p>
	 * save()和persist()将会引发SQL的INSERT，delete()会引发SQLDELETE，而update()或merge()
	 * 会引发SQLUPDATE。
	 * <p>
	 * 对持久化（persistent）实例的修改在刷新提交的时候会被检测到，它也会引起SQLUPDATE。
	 * <p>
	 * saveOrUpdate()或者replicate()会引发SQLINSERT或者UPDATE。
	 * <p>
	 * 
	 * @param entity
	 */
	public void merge(T entity);

	/**
	 * 删除对象
	 * 
	 * @param entity
	 *            将删除的对象
	 */
	public void remove(T entity);

	/**
	 * 根据ID删除对象
	 * 
	 * @param id
	 *            将删除的对象的ID
	 */
	public void removeById(PK id);

	/**
	 * 删除对象
	 * 
	 * @param entity
	 *            将删除的对象
	 */
	public String removePrivilege(T entity);

	/**
	 * 根据ID删除对象
	 * 
	 * @param id
	 *            将删除的对象的ID
	 */
	public String removePrivilegeById(PK id);

	/**
	 * 根据hql进行数据的删除
	 * 
	 * @param hql
	 *            删除hql语句
	 * @return 删除记录数
	 */
	public int removeByHql(String hql);

	/**
	 * 创建Query对象.
	 * 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以返回Query后自行设置.
	 * <p>
	 * 留意可以连续设置,如:
	 * <p>
	 * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
	 * <p>
	 * 
	 * @param values
	 *            可变参数
	 *            <p>
	 *            用户可以如下四种方式使用:
	 *            <p>
	 *            dao.getQuery(hql);
	 *            <p>
	 *            dao.getQuery(hql,arg0);
	 *            <p>
	 *            dao.getQuery(hql,arg0,arg1);
	 *            <p>
	 *            dao.getQuery(hql,new Object[arg0,arg1,arg2]);
	 *            <p>
	 */
	public Query getQuery(String hql, Object... values);

	/**
	 * 创建Criteria对象
	 * 
	 * @param criterion
	 *            可变条件列表,Restrictions生成的条件
	 */
	public Criteria getCriteria(Criterion... criterion);

	/**
	 * hql查询.
	 * 
	 * @param hql
	 *            查询hql语句
	 * @param values
	 *            可变参数
	 *            <p>
	 *            用户可以如下四种方式使用:
	 *            <p>
	 *            dao.find(hql);
	 *            <p>
	 *            dao.find(hql,arg0);
	 *            <p>
	 *            dao.find(hql,arg0,arg1);
	 *            <p>
	 *            dao.find(hql,new Object[arg0,arg1,arg2]);
	 *            <p>
	 */
	@SuppressWarnings("unchecked")
	public List find(String hql, Object... values);

	/**
	 * 根据criterion进行动态查询
	 * 
	 * @param criterion
	 * @return 符合条件的对象列表
	 */
	public List<T> find(Criterion... criterion);

	/**
	 * 根据属性名和属性值查询对象.
	 * 
	 * @param name
	 *            属性名
	 * @param value
	 *            属性值
	 * @return 符合条件的对象列表
	 */
	public List<T> findBy(String name, Object value);

	/**
	 * 根据属性名和属性值查询唯一对象.
	 * 
	 * @param name
	 *            属性名
	 * @param value
	 *            属性值
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
	 * @param entity
	 * @param names
	 *            在POJO里不能重复的属性列表,以逗号分割
	 *            <p>
	 *            如"name,loginid,password"
	 * @return true 表示不存在此属性 false 表示存在此属性
	 */
	public boolean isNotUnique(T entity, String names);

	/**
	 * 获取主键的字段名
	 * 
	 * @return 主键字段名
	 */
	public String getIdName();

	/**
	 * 分页查询函数，使用Criteria.
	 * 
	 * @param pageNo
	 *            页号,从0开始.
	 */
	public Page pagedQuery(int pageNo, int pageSize, Object... orderOrCriterion);

	/**
	 * 分页查询函数，使用Criteria.
	 * 
	 * @param criteria
	 *            查询所需的criteria
	 * @param pageNo
	 *            页号,从0开始.
	 * @param pageSize
	 *            每页显示行数
	 * @return
	 */
	public Page pagedQuery(Criteria criteria, int pageNo, int pageSize);

	/**
	 * 分页查询函数，使用hql.
	 * 
	 * @param hql
	 *            查询hql语句
	 * @param pageNo
	 *            页号,从0开始.
	 * @param pageSize
	 *            每页显示行数
	 * @param values
	 *            条件值，与hql匹配
	 * @return 对象集的分页结果
	 */
	public Page pagedQuery(String hql, int pageNo, int pageSize,
			Object... values);

	/**
	 * 调用存储过程,通过jdbc的方式调用
	 * 
	 * @param procedureName
	 *            过程名称
	 * @param values
	 *            过程参数
	 */
	public void executeProcedure(String procedureName, Object... values);

	/**
	 * 调用批量查询存储过程
	 * 
	 * @param procedureName
	 *            *.hbm.xml文件中定义的存储过程名称
	 * @param values
	 *            过程所需的参数
	 * @return 查询结果列表
	 */
	@SuppressWarnings("unchecked")
	public List executeProcedureList(String procedureName, Object... values);

	/**
	 * 调用存储过程,通过jdbc的方式调用,并返回结果集
	 * 
	 * @param procedureName
	 *            过程名称
	 * @param parameters
	 *            传入的参数集
	 * @param values
	 *            过程out的结果集
	 * @return 存储过程执行后返回的结果
	 */
	public List<Object> executeProcedureWithResult(String procedureName,
			List<Object> parameters, List<Object> values);// {

	/**
	 * 根据NamedQuery查询
	 * 
	 * @param queryName
	 * @param queryArgs
	 * @return 查询结果列表
	 */
	@SuppressWarnings("unchecked")
	public List findByNamedQuery(String queryName, Object... queryArgs);
}
