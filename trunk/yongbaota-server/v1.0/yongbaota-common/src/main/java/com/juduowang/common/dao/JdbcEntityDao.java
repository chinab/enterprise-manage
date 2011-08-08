package com.juduowang.common.dao;

import java.util.List;
import java.util.Map;

import com.juduowang.common.dao.support.Page;


/**
 * Dao泛型基类
 * <p>
 * 继承此类，可以实现单表的增删改查
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 * 
 */
public interface JdbcEntityDao {
	/**
	 * 获取全部对象，相对于一个表中所有的记录
	 * 
	 * @return 对象列表
	 */
	public List<Object> getAll(String statementName);

	/**
	 * 根据ID获取对象
	 * 
	 * @param id
	 *            对象主键
	 * @return 对象
	 */
	public Object get(String statementName, Long id);

	/**
	 * 根据ID判断对象是否存在
	 * 
	 * @param id
	 * @return true:对象存在 false:对象不存在
	 */
	public boolean exists(String statementName, Long id);

	/**
	 * 保存对象:处理插入和更新
	 * 
	 * @param entity
	 *            需要保存的对象
	 */
	public void save(String statementName, Object obj);

	/**
	 * 更新对象
	 * 
	 */
	public void update(String statementName, Object obj);

	/**
	 * 删除对象
	 * 
	 * @param entity
	 *            将删除的对象
	 */
	public void remove(String statementName);

	@SuppressWarnings("unchecked")
	public void remove(String statementName, Map parameterMap);

	/**
	 * 根据ID删除对象
	 * 
	 * @param id
	 *            将删除的对象的ID
	 */
	public void removeById(String statementName, Long id);

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
	public List find(String statementName, Map map);

	@SuppressWarnings("unchecked")
	public List<Object> findBy(String statementName, Map map);

	/**
	 * 根据属性名和属性值查询对象.
	 * 
	 * @param name
	 *            属性名
	 * @param value
	 *            属性值
	 * @return 符合条件的对象列表
	 */
	public List<Object> findBy(String statementName, Object value);

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
	public Page pagedQuery(String countStatementName, String statementName, int pageNo, int pageSize,
			Map countParameterMap, Map queryParameterMap);

	/**
	 * 调用存储过程,通过jdbc的方式调用
	 * 
	 * @param procedureName
	 *            过程名称
	 * @param values
	 *            过程参数
	 */
	@SuppressWarnings("unchecked")
	public void executeProcedure(String procedureName, Map map);

}
