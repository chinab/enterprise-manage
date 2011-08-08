package com.juduowang.common.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.juduowang.common.dao.UniversalDao;
import com.juduowang.utils.BeanUtil;
import com.juduowang.utils.CollectionUtil;
import com.juduowang.utils.HibernateConfigurationHelper;


/**
 * 提供对一个对象的CRUD操作，不需要任何Spring的配置
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 * @author yinshuwei
 * @author lufushi
 * @author zhuqianjin
 * @version 3.0
 */
@SuppressWarnings("unchecked")
public class HibernateUniversalDao extends HibernateDaoSupport implements UniversalDao {
	protected final Log log = LogFactory.getLog(getClass());

	private final static Long LOG_USE = 1L;
	private final static Long LOG_NO_USE = 0L;

	private void setBeanLogUse(Object obj, Long logUse) {
		try {
			BeanUtil.setProperty(obj, "logUse", logUse);
		} catch (IllegalAccessException e) {
			// e.printStackTrace();
		} catch (InvocationTargetException e) {
			// e.printStackTrace();
		}
	}

	private boolean hasLogUse(Class entityClass) {
		Method[] methods = entityClass.getMethods();
		for (Method method : methods) {
			if (method.getName().equals("getLogUse")) {
				return true;
			}
		}
		return false;
	}

	/** ~ save update **/
	public Object save(Object o) {
		setBeanLogUse(o, LOG_USE);
		return getHibernateTemplate().merge(o);
	}

	public void saveAll(final Collection objs) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				int i = 1;
				for (Object obj : objs) {
					setBeanLogUse(obj, LOG_USE);
					session.save(obj);
					if (i++ % 100 == 0) {
						session.flush();
						session.clear();
					}
				}
				return null;
			}
		});
		/**
		 * Session session = getSession(); Transaction transaction =
		 * session.beginTransaction(); int i=1; for (Object obj : objs) {
		 * session.save(obj); if (i++ % 100 == 0) { session.flush();
		 * session.clear(); } } transaction.commit(); session.close();
		 **/
	}

	public void update(Object obj) {
		getHibernateTemplate().update(obj);
	}

	public void updateAll(final Collection objs) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				int i = 1;
				for (Object obj : objs) {
					session.save(obj);
					if (i++ % 100 == 0) {
						session.flush();
						session.clear();
					}
				}
				return null;
			}
		});
		/**
		 * Session session = getSession(); Transaction transaction =
		 * session.beginTransaction(); int i=1; for (Object obj : objs) {
		 * session.update(obj); if (i++ % 100 == 0) { session.flush();
		 * session.clear(); } } transaction.commit(); session.close();
		 **/
	}

	public void saveOrUpdate(Object obj) {
		setBeanLogUse(obj, LOG_USE);
		getHibernateTemplate().saveOrUpdate(obj);
	}

	public void saveOrUpdateAll(Collection objs) {
		for (Object obj : objs) {
			setBeanLogUse(obj, LOG_USE);
		}
		getHibernateTemplate().saveOrUpdateAll(objs);
	}

	/** ~ delete */
	public void delete(Class entityClass, Serializable id) {
		Object obj = load(entityClass, id);
		if (hasLogUse(entityClass)) {
			setBeanLogUse(obj, LOG_NO_USE);
			getHibernateTemplate().update(obj);
		} else {
			getHibernateTemplate().delete(load(entityClass, id));
		}
	}

	public void delete(Object obj) {
		if (hasLogUse(obj.getClass())) {
			setBeanLogUse(obj, LOG_NO_USE);
			getHibernateTemplate().update(obj);
		} else {
			getHibernateTemplate().delete(obj);
		}
	}

	public void deleteAll(Collection objs) {
		Iterator iterator = objs.iterator();
		if (iterator.hasNext()) {
			if (hasLogUse(iterator.next().getClass())) {
				for (Object obj : objs) {
					setBeanLogUse(obj, LOG_NO_USE);
				}
				getHibernateTemplate().saveOrUpdateAll(objs);
			} else {
				getHibernateTemplate().deleteAll(objs);
			}
		}
	}

	@Deprecated
	public void deleteByHql(final String hql, final Object... values) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				int i = 0;
				for (Object value : values) {
					query.setParameter(i++, value);
				}
				query.executeUpdate();
				return null;
			}
		});
		/**
		 * Session session = getSession(); Query query =
		 * session.createQuery(hql); int i = 0; for (Object value : values) {
		 * query.setParameter(i++, value); } query.executeUpdate();
		 * releaseSession(session);
		 **/
	}

	/** ~ query */
	public Object load(Class entityClass, Serializable id) {
		return getHibernateTemplate().load(entityClass, id);
	}

	public Object get(Class entityClass, Serializable id) {
		return getHibernateTemplate().get(entityClass, id);
	}

	public List getAll(Class entityClass) {
		try {
			Object obj = entityClass.newInstance();
			if (hasLogUse(entityClass)) {
				setBeanLogUse(obj, LOG_USE);
				return getHibernateTemplate().findByExample(obj);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return getHibernateTemplate().loadAll(entityClass);
	}

	public List findByCriteria(Class entityClass, Criterion... criterion) {
		return getCriteria(entityClass, criterion).list();
	}

	public List findByHql(String hql) {
		return getHibernateTemplate().find(hql);
	}

	public List findByHql(String hql, Object... values) {
		if (values == null || values.length == 0) {
			return getHibernateTemplate().find(hql);
		}
		return getHibernateTemplate().find(hql, values);
	}

	public Object findOneByHql(String hql) {
		return CollectionUtil.first(findByHql(hql));
	}

	public Object findOneByHql(String hql, Object... values) {
		return CollectionUtil.first(findByHql(hql, values));
	}

	public List findBySql(final String sql) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createSQLQuery(sql).list();
			}
		});
		/**
		 * Session session = getSession(); session.beginTransaction(); List list
		 * = session.createSQLQuery(sql).list(); return list;
		 **/
	}

	public List findBySql(final String sql, final Class entityClass) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.addEntity(entityClass);
				return sqlQuery.list();
			}
		});
		/**
		 * Session session = getSession(); Transaction transaction =
		 * session.beginTransaction(); SQLQuery sqlQuery =
		 * session.createSQLQuery(sql); sqlQuery.addEntity(entityClass);
		 * session.flush(); session.clear(); transaction.commit();
		 * System.err.println(sqlQuery.list().size()); return sqlQuery.list();
		 **/
	}

	public List findBySql(final String sql, final Object... values) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				int i = 0;
				for (Object value : values) {
					sqlQuery.setParameter(i++, value);
				}
				return sqlQuery.list();
			}
		});
	}

	public Query getSqlQuery(final String sql, final Object... values) {
		return (Query) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
				return query;
			}
		});
	}

	public List findByHqlUseSql(String hql, Object... values) throws ClassNotFoundException {
		String sql = this.convertHQLToSQL(hql, values);
		if (sql != null && !sql.equals("")) {
			return this.findBySql(sql);
		} else {
			return null;
		}
	}

	public List executeFind(HibernateCallback action) {
		return this.getHibernateTemplate().executeFind(action);
	}

	public Query getQuery(final String hql, final Object... values) {
		return (Query) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
				return query;
			}
		});
		/**
		 * Query query = getSession().createQuery(hql); for (int i = 0; i <
		 * values.length; i++) { query.setParameter(i, values[i]); } return
		 * query;
		 **/
	}

	/**
	 * 根据以类别名为key，以类名称为Value的map，构造一个类属性map
	 * 
	 * @param classNameMap
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 */
	private Map<String, String> convertHqlPropertyToSqlProperty(Map<String, String> classNameMap)
			throws SecurityException, ClassNotFoundException {
		Map<String, String> resultMap = new HashMap<String, String>();
		for (String key : classNameMap.keySet()) {
			String value = classNameMap.get(key);
			Field[] fields = Class.forName(value).getDeclaredFields();// .getFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				Type fieldType = field.getGenericType();
				if (!fieldName.contains("UID") && !fieldType.toString().contains("Set")
						&& !fieldType.toString().contains("List")) {
					resultMap.put(key + "." + fieldName,
							key + "." + HibernateConfigurationHelper.getColumnName(Class.forName(value), fieldName));
				}
			}
		}
		return resultMap;
	}

	/**
	 * 根据以类别名为key，类名称为value的map，构造一个以model名称为key，表名称为value的map
	 * 
	 * @param classNameMap
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Map<String, String> convertClassNameToTableName(Map<String, String> classNameMap)
			throws ClassNotFoundException {
		Map<String, String> resultMap = new HashMap<String, String>();
		for (String key : classNameMap.keySet()) {
			String value = classNameMap.get(key);
			resultMap.put(value, HibernateConfigurationHelper.getTableName(Class.forName(value)));
		}
		return resultMap;
	}

	/**
	 * 将hql语句转换为sql语句
	 * 
	 * @param hql
	 * @param values
	 * @return
	 * @throws ClassNotFoundException
	 */
	private String convertHQLToSQL(String hql, Object... values) throws ClassNotFoundException {
		String sql = null;
		String fromToWhere = null;
		Map<String, String> className = new HashMap<String, String>();
		Map<String, String> hqlPropertyToSqlProperty = null;
		Map<String, String> classNameToTableName = null;
		int fromIndex = hql.indexOf("from");
		int whereIndex = hql.indexOf("where");

		if (fromIndex > 0 && whereIndex > 0) {
			fromToWhere = hql.substring(fromIndex + 4, whereIndex).trim();
		}

		StringTokenizer fromToWhereST = new StringTokenizer(fromToWhere, ",");
		while (fromToWhereST.hasMoreTokens()) {
			String[] modelObject = fromToWhereST.nextToken().split(" ");
			if (modelObject.length > 0) {
				className.put(modelObject[1], modelObject[0]);
			}
		}
		hqlPropertyToSqlProperty = this.convertHqlPropertyToSqlProperty(className);
		classNameToTableName = this.convertClassNameToTableName(className);

		for (String key : hqlPropertyToSqlProperty.keySet()) {
			String value = hqlPropertyToSqlProperty.get(key);
			hql = hql.replaceAll(key, value);
		}
		for (String key : classNameToTableName.keySet()) {
			hql = hql.replaceAll(key, classNameToTableName.get(key));
		}
		for (int i = 0; i < values.length; i++) {
			if (hql.indexOf("?") > 0) {
				hql = hql.substring(0, hql.indexOf("?") + 1).replace("?", values[i].toString())
						+ hql.substring(hql.indexOf(".") + 1);
			}
		}
		sql = hql;
		return sql;
	}

	/**
	 * 由QBC条件元，获得条件
	 * 
	 * @param entityClass
	 *            域对象的类名
	 * @param criterion
	 *            条件元数组
	 * @return QBC条件
	 */
	private Criteria getCriteria(final Class entityClass, final Criterion... criterion) {
		return (Criteria) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				for (Criterion c : criterion) {
					criteria.add(c);
				}
				if (hasLogUse(entityClass)) {
					criteria.add(Restrictions.eq("logUse", LOG_USE));
				}
				return criteria;
			}
		});
	}

	public void clear() {
		Session session = getSession();
		if (session != null) {
			session.flush();
			session.clear();
		}
	}

	public void flush() {
		Session session = getSession();
		if (session != null) {
			session.flush();
		}
	}

	public void evict(Object obj) {
		Session session = getSession();
		if (session != null) {
			session.evict(obj);
		}
	}

	/**
	 * 调用存储过程
	 * 
	 * @param procedureName
	 *            *.hbm.xml文件中定义的存储过程名称
	 * @param values
	 *            过程所需的参数
	 * @return
	 */
	public List executeProcedureList(String procedureName, Object... values) {
		if ("".equals(procedureName)) {
			return null;
		}

		Query query = super.getSession().getNamedQuery(procedureName);// session.getNamedQuery("getUserList").list();
		for (int i = 1, size = values.length; i <= size; i++) {
			query.setParameter(i, values[i - 1]);
		}

		return query.list();

	}

	/**
	 * 调用存储过程,通过jdbc的方式调用
	 * 
	 * @param procedureName
	 *            过程名称
	 * @param values
	 *            过程参数
	 */
	public void executeProcedure(String procedureName, Object... values) {
		if ("".equals(procedureName)) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("{call ").append(procedureName).append("(");
		for (int i = 1, size = values.length; i <= size; i++) {
			sb.append("?");
			if (size > 1 && i < size) {
				sb.append(",");
			}
		}
		sb.append(")}");

		Session session = super.getHibernateTemplate().getSessionFactory().getCurrentSession();// .getSession();

		session.beginTransaction();

		PreparedStatement st;
		try {

			st = session.connection().prepareStatement(sb.toString());

			for (int i = 1, size = values.length; i <= size; i++) {
				st.setObject(i, values[i - 1]);
			}

			st.execute();

			// session.getTransaction().commit();

		} catch (HibernateException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 调用存储过程,通过jdbc的方式调用,并返回结果集
	 * 
	 * @param procedureName
	 *            过程名称
	 * @param parameters
	 *            传入的参数集
	 * @param values
	 *            过程out的结果集
	 */
	public List<Object> executeProcedureWithResult(String procedureName, List<Object> parameters, List<Object> values) {
		if ("".equals(procedureName)) {
			return null;
		}
		List<Object> result = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("{call ").append(procedureName).append(" (");
		for (int i = 1, size = parameters.size(); i <= size; i++) {
			if (i > 1) {
				sb.append(",");
			}
			sb.append("?");
		}
		if (parameters != null && !parameters.isEmpty()) {
			sb.append(",");
		}
		for (int i = 1, size = values.size(); i <= size; i++) {
			if (i > 1 && i < size) {
				sb.append(",");
			}
			sb.append("?");
		}
		sb.append(")}");
		Session session = super.getHibernateTemplate().getSessionFactory().getCurrentSession();// .getSession();
		session.beginTransaction();
		try {
			CallableStatement proc = null;
			int valueType = Types.JAVA_OBJECT;
			proc = session.connection().prepareCall(sb.toString());
			int psize = parameters == null ? 0 : parameters.size();
			int vsize = values == null ? 0 : values.size();

			for (int i = 0, size = psize; i < size; i++) {
				proc.setObject(i + 1, parameters.get(i));
			}
			for (int j = 0, i = psize + 1; j < vsize; i++, j++) {
				Class cl = values.get(j).getClass();
				if (cl.getName().equals("java.lang.String"))
					valueType = Types.VARCHAR;
				else if (cl.getName().equals("java.lang.Long"))
					valueType = Types.INTEGER;
				else if (cl.getName().equals("java.util.Date"))
					valueType = Types.DATE;
				else if (cl.getName().equals("java.lang.Boolean"))
					valueType = Types.BOOLEAN;
				else if (cl.getName().equals("java.lang.Double"))
					valueType = Types.DOUBLE;
				else if (cl.getName().equals("java.lang.Float"))
					valueType = Types.FLOAT;
				else if (cl.getName().equals("java.lang.Character"))
					valueType = Types.CHAR;
				else if (cl.getName().equals("java.lang.Number"))
					valueType = Types.NUMERIC;

				proc.registerOutParameter(i, valueType);
				proc.setObject(i, values.get(j));// [i-1]);
			}

			proc.execute();

			for (int j = 0, i = psize + 1; j < vsize; i++, j++) {
				Class cl = values.get(j).getClass();
				if (cl.getName().equals("java.lang.String"))
					result.add(proc.getString(i));
				else if (cl.getName().equals("java.lang.Long"))
					result.add(proc.getLong(i));
				else if (cl.getName().equals("java.util.Date"))
					result.add(proc.getDate(i));
				else if (cl.getName().equals("java.lang.Boolean"))
					result.add(proc.getBoolean(i));
				else if (cl.getName().equals("java.lang.Double"))
					result.add(proc.getDouble(i));
				else if (cl.getName().equals("java.lang.Float"))
					result.add(proc.getFloat(i));
				else if (cl.getName().equals("java.lang.Character"))
					result.add(proc.getByte(i));
				else if (cl.getName().equals("java.lang.Number"))
					result.add(proc.getInt(i));

				else
					result.add(proc.getObject(i));

			}

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
