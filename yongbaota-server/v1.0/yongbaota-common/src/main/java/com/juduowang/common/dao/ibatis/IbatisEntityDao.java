package com.juduowang.common.dao.ibatis;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.juduowang.common.dao.JdbcEntityDao;
import com.juduowang.common.dao.support.Page;


/**
 * 负责为单个Entity对象提供CRUD操作的Hibernate DAO基类. <p/> 子类只要在类定义时指定所管理Entity的Class,
 * 即拥有对单个Entity对象的CRUD操作. 
 * eg.
 * <pre>
 * public class UserManager extends HibernateEntityDao&lt;User&gt; {
 * 
 * }
 * </pre>
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 * @see com.juduowang.common.dao.JdbcEntityDao
 */

@SuppressWarnings("unchecked")
public class IbatisEntityDao extends SqlMapClientDaoSupport implements JdbcEntityDao {
	protected final Log log = LogFactory.getLog(getClass());

	public boolean exists(String statementName, Long id) {

		Object entity = (Object) super.getSqlMapClientTemplate().queryForObject(statementName, id);
		if (entity == null) {
			return false;
		} else {
			return true;
		}

	}

	public Log getLog() {
		return log;
	}

	public List find(String statementName, Map map) {

		return getSqlMapClientTemplate().queryForList(statementName, map);
	}

	public List<Object> findBy(String statementName, Map map) {

		return (List<Object>) getSqlMapClientTemplate().queryForList(statementName, map);
	}

	public List<Object> findBy(String statementName, Object value) {

		return getSqlMapClientTemplate().queryForList(statementName, value);
	}

	public Object get(String statementName, Long id) {
		return (Object) getSqlMapClientTemplate().queryForObject(statementName, id);
	}

	public List<Object> getAll(String statementName) {
		return getSqlMapClientTemplate().queryForList(statementName);
	}

	public Page pagedQuery(String countStatementName, String statementName, int pageNo, int pageSize,
			Map countParameterMap, Map queryParameterMap) {
		// Count查询
		List countlist = getSqlMapClientTemplate().queryForList(countStatementName, countParameterMap);
		long totalCount = (Long) countlist.get(0);

		if (totalCount < 1)
			return new Page();
		// 实际查询返回分页对象
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		queryParameterMap.put("startIndex", startIndex);
		queryParameterMap.put("pageSize", pageSize);
		List list = getSqlMapClientTemplate().queryForList(statementName, queryParameterMap);

		return new Page(startIndex, totalCount, pageSize, list);
	}

	public void remove(String statementName) {
		getSqlMapClientTemplate().delete(statementName);
	}

	public void remove(String statementName, Map parameterMap) {
		getSqlMapClientTemplate().delete(statementName, parameterMap);
	}

	public void removeById(String statementName, Long id) {
		super.getSqlMapClientTemplate().delete(statementName, id);
	}

	public void save(String statementName, Object obj) {
		getSqlMapClientTemplate().insert(statementName, obj);
	}

	public void update(String statementName, Object obj) {
		getSqlMapClientTemplate().update(statementName, obj);
	}

	protected boolean hasText(String str) {
		if (str == null || str.trim().equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * 调用存储过程,通过jdbc的方式调用
	 * @param procedureName 过程名称
	 * @param values 过程参数
	 */
	public void executeProcedure(String procedureName, Map map) {
		if ("".equals(procedureName)) {
			return;
		}
		try {
			getSqlMapClientTemplate().insert(procedureName, map);

		} catch (Exception e) {
			throw new RuntimeException("执行存储过程异常: " + procedureName + "异常：" + e);
		}
	}

}
