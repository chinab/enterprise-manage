package com.juduowang.common.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.juduowang.common.dao.EntityDao;
import com.juduowang.common.dao.support.Page;
import com.juduowang.utils.BeanUtil;


/**
 * 负责为单个Entity对象提供CRUD操作的Hibernate DAO基类.
 * <p/>
 * 子类只要在类定义时指定所管理Entity的Class, 即拥有对单个Entity对象的CRUD操作. eg.
 * 
 * <pre>
 * public class UserManager extends HibernateEntityDao&lt;User&gt; {
 * 
 * }
 * </pre>
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 * @see com.juduowang.common.dao.EntityDao
 */

@SuppressWarnings("unchecked")
public class HibernateEntityDao<T, PK extends Serializable> extends HibernateDaoSupport implements EntityDao<T, PK> {
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

	private boolean isLogUse(Object obj) {
		Method[] methods = obj.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals("getLogUse")) {
				try {
					return BeanUtil.getPropertyValue(obj, "logUse").equals(1L);
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
		return true;

	}

	/**
	 * DAO所管理的Entity类型.
	 */
	protected Class<T> entityClass;

	/**
	 * 取得entityClass. JDK1.4不支持泛型的子类可以抛开Class<T> entityClass,重载此函数达到相同效果。
	 */
	protected Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * 在构造函数中将泛型T.class赋给entityClass
	 */
	public HibernateEntityDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public boolean exists(PK id) {
		T entity = (T) super.getHibernateTemplate().get(this.entityClass, id);
		if (entity == null) {
			return false;
		} else {
			return isLogUse(entity);
//			return true;
		}

	}

	public List find(String hql, Object... values) {
		if (!hasText(hql)) {
			log.warn("The hql is null!");
			return null;
		}
		return getHibernateTemplate().find(hql, values);
	}

	public List<T> find(Criterion... criterion) {
		Criteria criteria = getCriteria(criterion);
		return criteria.list();
	}

	public List<T> findBy(String name, Object value) {
		if (!hasText(name)) {
			log.warn("The Name is null");
			return null;
		}
		return getCriteria(Restrictions.eq(name, value)).list();
	}

	public List<T> findByLike(String name, String value) {
		if (!hasText(name)) {
			log.warn("The Name is null !");
			return null;
		}
		return getCriteria(Restrictions.like(name, value, MatchMode.ANYWHERE)).list();
	}

	public T findUniqueBy(String name, Object value) {
		if (!hasText(name)) {
			log.warn("The Name is null !");
			return null;
		}
		return (T) getCriteria(Restrictions.eq(name, value)).uniqueResult();
	}

	public T get(PK id) {
		return (T) getHibernateTemplate().get(entityClass, id);
	}

	public T load(PK id) {
		return (T) getHibernateTemplate().load(entityClass, id);
	}

	public T getPrivilege(PK id) {
		T t = (T) getHibernateTemplate().get(entityClass, id);
		this.getSession().flush();
		this.getSession().clear();
		return t;
	}

	public T loadPrivilege(PK id) {
		T load = (T) getHibernateTemplate().load(entityClass, id);
		this.getSession().flush();
		this.getSession().clear();
		return load;
	}

	public List<T> getAll() {
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

	public Criteria getCriteria(Criterion... criterion) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterion) {
			criteria.add(c);
		}
		if (hasLogUse(entityClass)) {
			criteria.add(Restrictions.eq("logUse", LOG_USE));
		}
		return criteria;
	}

	public String getIdName() {
		if (entityClass == null) {
			log.warn("The entity Class is null");
		}
		String idName = getSessionFactory().getClassMetadata(entityClass).getIdentifierPropertyName();

		Assert.hasText(idName, entityClass.getSimpleName() + "has no id column define");
		return idName;
	}

	public Query getQuery(String hql, Object... values) {
		if (!hasText(hql)) {
			log.warn("The hql is null!");
			return null;
		}
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

	public List findByNamedQuery(String queryName, Object... queryArgs) {
		Query namedQuery = super.getSession().getNamedQuery(queryName);
		String[] namedParameters = namedQuery.getNamedParameters();
		if (namedParameters.length == 0) {
			for (int i = 0; i < queryArgs.length; i++) {
				Object arg = queryArgs[i];
				namedQuery.setParameter(i, arg);
			}
		} else {
			if (queryArgs != null && queryArgs.length >= namedParameters.length) {
				for (int i = 0; i < namedParameters.length; i++) {
					Object arg = queryArgs[i];
					if (arg instanceof Collection) {
						namedQuery.setParameterList(namedParameters[i], (Collection) arg);
					} else {
						namedQuery.setParameter(namedParameters[i], arg);
					}
				}
			} else {
				return null;
			}
		}
		return namedQuery.list();
	}

	public boolean isNotUnique(T entity, String names) {
		if (!hasText(names)) {
			log.warn("The Name is null !");
			return true;
		}
		Criteria criteria = getCriteria().setProjection(Projections.rowCount());
		String[] nameList = names.split(",");
		try {
			// 循环加入
			for (String name : nameList) {
				criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(entity, name)));
			}

			// 以下代码为了如果是update的情况,排除entity自身.

			String idName = getIdName();
			// 通过反射取得entity的主键值
			Object id = PropertyUtils.getProperty(entity, idName);
			// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
			if (id != null)
				criteria.add(Restrictions.not(Restrictions.eq(idName, id)));

		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return (Integer) criteria.uniqueResult() > 0;
	}

	public Page pagedQuery(int pageNo, int pageSize, Object... orderOrCriterion) {
		Criteria criteria = getCriteria();
		int i = 0;
		for (Object c : orderOrCriterion) {
			if (Order.class.isInstance(c)) {
				i++;
				criteria.addOrder((Order) c);
			}
			if (Criterion.class.isInstance(c)) {
				criteria.add((Criterion) c);
			}
		}
		if (i == 0) {
			criteria.addOrder(Order.asc("id"));
		}
		return pagedQuery(criteria, pageNo, pageSize);
	}

	public Page pagedQuery(Criteria criteria, int pageNo, int pageSize) {
		if (hasLogUse(entityClass)) {
			criteria.add(Restrictions.eq("logUse", LOG_USE));
		}
		Assert.isTrue(pageNo >= 0, "pageNo should start from 0");
		CriteriaImpl impl = (CriteriaImpl) criteria;

		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) BeanUtil.getDeclaredProperty(impl, "orderEntries");
			BeanUtil.setDeclaredProperty(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		// 执行查询
		Integer obj = (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
		long totalCount = obj.longValue();

		// 将之前的Projection和OrderBy条件重新设回去
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		try {
			BeanUtil.setDeclaredProperty(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			throw new InternalError(" Runtime Exception impossibility throw ");
		}

		// 返回分页对象
		if (totalCount < 1)
			return new Page();

		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		List<T> list = criteria.setFirstResult(startIndex).setMaxResults(pageSize).list();

		return new Page(startIndex, totalCount, pageSize, list);
	}

	public Page pagedQuery(String hql, int pageNo, int pageSize, Object... values) {
		Assert.hasText(hql);
		// Count查询
		String countQueryString = " select count (*) " + removeSelect(removeOrders(hql));
		List countlist = getHibernateTemplate().find(countQueryString, values);
		long totalCount = (Long) countlist.get(0);

		if (totalCount < 1)
			return new Page();
		// 实际查询返回分页对象
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		Query query = getQuery(hql, values);
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();

		return new Page(startIndex, totalCount, pageSize, list);
	}

	public void remove(T entity) {
		if (hasLogUse(entity.getClass())) {
			setBeanLogUse(entity, LOG_NO_USE);
			getHibernateTemplate().update(entity);
		} else {
			getHibernateTemplate().delete(entity);
		}
//		getHibernateTemplate().delete(entity);
	}

	public int removeByHql(String hql) {
		return getHibernateTemplate().getSessionFactory().openSession().createQuery(hql).executeUpdate();
	}

	public void removeById(PK id) {
		Object obj = load(id);
		if (hasLogUse(entityClass)) {
			setBeanLogUse(obj, LOG_NO_USE);
			getHibernateTemplate().update(obj);
		} else {
			getHibernateTemplate().delete(load(id));
		}
//		super.getHibernateTemplate().delete(this.load(id));
	}

	public void save(T entity) {
		setBeanLogUse(entity, LOG_USE);
		getHibernateTemplate().saveOrUpdate(entity);
	}

	public void saveObject(Object entity) {
		setBeanLogUse(entity, LOG_USE);
		getHibernateTemplate().saveOrUpdate(entity);
	}

	public void saveOnly(T entity) {
		setBeanLogUse(entity, LOG_USE);
		getHibernateTemplate().save(entity);
	}

	public String removePrivilege(T entity) {
		if (hasLogUse(entity.getClass())) {
			setBeanLogUse(entity, LOG_NO_USE);
			getHibernateTemplate().update(entity);
		} else {
			getHibernateTemplate().delete(entity);
		}
		return "success";
	}

	public String removePrivilegeById(PK id) {
		Object obj = load(id);
		if (hasLogUse(entityClass)) {
			setBeanLogUse(obj, LOG_NO_USE);
			getHibernateTemplate().update(obj);
		} else {
			getHibernateTemplate().delete(load(id));
		}
		return "success";
	}

	public String savePrivilege(T entity) {
		setBeanLogUse(entity, LOG_USE);
		getHibernateTemplate().saveOrUpdate(entity);
		// getSession().flush();
		return "success";
	}

	public void merge(T entity) {
		setBeanLogUse(entity, LOG_USE);
		getHibernateTemplate().merge(entity);

	}

	protected boolean hasText(String str) {
		if (str == null || str.trim().equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * 去除hql的select 子句，未考虑union的情况,，用于pagedQuery.
	 */
	private static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除hql的orderby 子句，用于pagedQuery.
	 */
	private static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
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