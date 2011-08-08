package com.juduowang.common.web.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;


import com.juduowang.utils.DateUtil;
import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseCondtionActionSupport extends ActionSupport {
	private static final long serialVersionUID = -3286211710377866946L;

	/**
	 * 排序："{\"name\":\"id\",\"type\":\"order\",\"dataType\":\"asc\"},"
	 * @param conditions
	 * @return
	 */
	protected List<Object> condtionsAndOrder(String conditions) {
		if (conditions == null || conditions.equals(""))
			return null;
		List<Criterion> criterions = new ArrayList<Criterion>();
		List<Order> orders = new ArrayList<Order>();
		List<Object> objects = new ArrayList<Object>();

		JSONArray jsonArray = JSONArray.fromObject(conditions);
		//int j = 0;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jso = jsonArray.getJSONObject(i);
			String name = jso.get("name") == null ? null : jso.get("name").toString();
			String valueStr = jso.get("value") == null ? null : jso.get("value").toString();
			String type = jso.get("type") == null ? null : jso.get("type").toString();
			String dataType = jso.get("dataType") == null ? null : jso.get("dataType").toString();

			if (name == null || "".equals(name) || "undefined".equals(name))
				continue;

			//排序
			if ("order".equalsIgnoreCase(type)) {
				if ("desc".equalsIgnoreCase(dataType)) {
					orders.add(Order.desc(name));
				} else {
					orders.add(Order.asc(name));
				}

				continue;
			}

			if (valueStr == null || "".equals(valueStr) || "undefined".equals(valueStr))
				continue;

			if ("isnull".equalsIgnoreCase(type)) {
				criterions.add(Restrictions.isNull(name));
				//j++;
				continue;
			}

			if ("notin".equalsIgnoreCase(type)) {
				DetachedCriteria dc = this.subQuery(valueStr);
				if (dc == null)
					continue;

				criterions.add(Property.forName(name).notIn(dc));
				//j++;
				continue;
			}

			if ("in".equalsIgnoreCase(type)) {
				DetachedCriteria dc = this.subQuery(valueStr);
				if (dc == null)
					continue;

				criterions.add(Property.forName(name).in(dc));
				//j++;
				continue;
			}

			/**
			 * 日期处理
			 */
			Object value = null;
			try {
				if ("date".equalsIgnoreCase(dataType)) {
					value = DateUtil.convertStringToDate("yyyy-MM-dd", valueStr);
				} else if ("datetime".equalsIgnoreCase(dataType)) {
					value = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", valueStr);
				} else if ("time".equalsIgnoreCase(dataType)) {
					value = DateUtil.convertStringToDate("HH:mm:ss", valueStr);
				} else if ("year".equalsIgnoreCase(dataType)) {
					value = DateUtil.convertStringToDate("yyyy", valueStr);
				} else if ("month".equalsIgnoreCase(dataType)) {
					value = DateUtil.convertStringToDate("yyyy-MM", valueStr);
				} else if ("datescope".equalsIgnoreCase(dataType)) {
					Date startDate = DateUtil.convertStringToDate("yyyy-MM-dd", valueStr);
					Date endDate = DateUtil.calcDateByDay(startDate, 1);
					criterions.add(Restrictions.ge(name, startDate));
					criterions.add(Restrictions.lt(name, endDate));
					//j = j+2;
					continue;
				} else if ("yearscope".equalsIgnoreCase(dataType)) {
					Date startDate = DateUtil.convertStringToDate("yyyy", valueStr);
					Date endDate = DateUtil.calcDateByYear(startDate, 1);
					criterions.add(Restrictions.ge(name, startDate));
					criterions.add(Restrictions.lt(name, endDate));
					//j = j+2;
					continue;
				} else if ("monthscope".equalsIgnoreCase(dataType)) {
					Date startDate = DateUtil.convertStringToDate("yyyy-MM", valueStr);
					Date endDate = DateUtil.calcDateByMonth(startDate, 1);
					criterions.add(Restrictions.ge(name, startDate));
					criterions.add(Restrictions.lt(name, endDate));
					//j = j+2;
					continue;
				} else if ("long".equalsIgnoreCase(dataType)) {
					value = Long.valueOf(valueStr);
				} else {
					value = valueStr;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			/**
			 * 根据类型判断
			 */
			Criterion criterion = null;
			if ("like".equalsIgnoreCase(type) || "anywhere".equalsIgnoreCase(type)) {
				criterion = Restrictions.like(name, valueStr, MatchMode.ANYWHERE);
			} else if ("end".equalsIgnoreCase(type)) {
				criterion = Restrictions.like(name, valueStr, MatchMode.END);
			} else if ("start".equalsIgnoreCase(type)) {
				criterion = Restrictions.like(name, valueStr, MatchMode.START);
			} else if ("EXACT".equalsIgnoreCase(type)) {
				criterion = Restrictions.like(name, valueStr, MatchMode.EXACT);
			} else if (">".equalsIgnoreCase(type)) {
				criterion = Restrictions.gt(name, value);
			} else if (">=".equalsIgnoreCase(type)) {
				criterion = Restrictions.ge(name, value);
			} else if ("<".equalsIgnoreCase(type)) {
				criterion = Restrictions.lt(name, value);
			} else if ("<=".equalsIgnoreCase(type)) {
				criterion = Restrictions.le(name, value);
			} else if ("<>".equalsIgnoreCase(type)) {
				criterion = Restrictions.ne(name, value);
			} else {
				criterion = Restrictions.eq(name, value);
			}

			criterions.add(criterion);
		}

		objects.addAll(criterions);
		objects.addAll(orders);
		return objects;
	}

	@Deprecated
	protected Criterion[] conditions(String conditions) {
		if (conditions == null)
			return null;
		List<Criterion> list = new ArrayList<Criterion>();

		JSONArray jsonArray = JSONArray.fromObject(conditions);
		int j = 0;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jso = jsonArray.getJSONObject(i);
			String name = jso.get("name") == null ? null : jso.get("name").toString();
			String valueStr = jso.get("value") == null ? null : jso.get("value").toString();
			String type = jso.get("type") == null ? null : jso.get("type").toString();
			String dataType = jso.get("dataType") == null ? null : jso.get("dataType").toString();

			if (name == null || "".equals(name) || "undefined".equals(name))
				continue;
			if (valueStr == null || "".equals(valueStr) || "undefined".equals(valueStr))
				continue;

			if ("isnull".equalsIgnoreCase(type)) {
				list.add(Restrictions.isNull(name));
				j++;
				continue;
			}

			if ("notin".equalsIgnoreCase(type)) {
				DetachedCriteria dc = this.subQuery(valueStr);
				if (dc == null)
					continue;

				list.add(Property.forName(name).notIn(dc));
				j++;
				continue;
			}

			if ("in".equalsIgnoreCase(type)) {
				DetachedCriteria dc = this.subQuery(valueStr);
				if (dc == null)
					continue;

				list.add(Property.forName(name).in(dc));
				j++;
				continue;
			}

			Object value = null;

			try {
				if ("date".equalsIgnoreCase(dataType)) {
					value = DateUtil.convertStringToDate("yyyy-MM-dd", valueStr);
				} else if ("datetime".equalsIgnoreCase(dataType)) {
					value = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", valueStr);
				} else if ("time".equalsIgnoreCase(dataType)) {
					value = DateUtil.convertStringToDate("HH:mm:ss", valueStr);
				} else if ("year".equalsIgnoreCase(dataType)) {
					value = DateUtil.convertStringToDate("yyyy", valueStr);
				} else if ("month".equalsIgnoreCase(dataType)) {
					value = DateUtil.convertStringToDate("yyyy-MM", valueStr);
				} else if ("datescope".equalsIgnoreCase(dataType)) {
					Date startDate = DateUtil.convertStringToDate("yyyy-MM-dd", valueStr);
					Date endDate = DateUtil.calcDateByDay(startDate, 1);
					//list.add(Restrictions.between(name, startDate, endDate));
					//j++;
					list.add(Restrictions.ge(name, startDate));
					list.add(Restrictions.lt(name, endDate));
					j = j + 2;
					continue;
				} else if ("yearscope".equalsIgnoreCase(dataType)) {
					Date startDate = DateUtil.convertStringToDate("yyyy", valueStr);
					Date endDate = DateUtil.calcDateByYear(startDate, 1);
					//list.add(Restrictions.between(name, startDate, endDate));
					//j++;
					list.add(Restrictions.ge(name, startDate));
					list.add(Restrictions.lt(name, endDate));
					j = j + 2;
					continue;
				} else if ("monthscope".equalsIgnoreCase(dataType)) {
					Date startDate = DateUtil.convertStringToDate("yyyy-MM", valueStr);
					Date endDate = DateUtil.calcDateByMonth(startDate, 1);
					//list.add(Restrictions.between(name, startDate, endDate));
					//j++;
					list.add(Restrictions.ge(name, startDate));
					list.add(Restrictions.lt(name, endDate));
					j = j + 2;
					continue;
				} else if ("long".equalsIgnoreCase(dataType)) {
					value = Long.valueOf(valueStr);
				} else {
					value = valueStr;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Criterion criterion = null;
			if ("like".equalsIgnoreCase(type) || "anywhere".equalsIgnoreCase(type)) {
				criterion = Restrictions.like(name, valueStr, MatchMode.ANYWHERE);
			} else if ("end".equalsIgnoreCase(type)) {
				criterion = Restrictions.like(name, valueStr, MatchMode.END);
			} else if ("start".equalsIgnoreCase(type)) {
				criterion = Restrictions.like(name, valueStr, MatchMode.START);
			} else if ("EXACT".equalsIgnoreCase(type)) {
				criterion = Restrictions.like(name, valueStr, MatchMode.EXACT);
			} else if (">".equalsIgnoreCase(type)) {
				criterion = Restrictions.gt(name, value);
			} else if (">=".equalsIgnoreCase(type)) {
				criterion = Restrictions.ge(name, value);
			} else if ("<".equalsIgnoreCase(type)) {
				criterion = Restrictions.lt(name, value);
			} else if ("<=".equalsIgnoreCase(type)) {
				criterion = Restrictions.le(name, value);
			} else if ("<>".equalsIgnoreCase(type)) {
				criterion = Restrictions.ne(name, value);
			} else {
				criterion = Restrictions.eq(name, value);
			}

			list.add(criterion);
			j++;
		}

		Criterion[] c = new Criterion[j];
		return list.toArray(c);
	}

	/**
	 * 根据value，构造子查询的DetachedCriteria
	 * 子查询的Value传入格式
	 * 
	 * {\"table\":\"tableName\",\"field\":\"fieldName\",\"sc\":\"Conditions的格式\"}
	 */
	protected DetachedCriteria subQuery(String value) {

		JSONObject jso = JSONObject.fromObject(value);

		String tableName = jso.getString("table") == null ? null : jso.getString("table");
		String fieldName = jso.getString("field") == null ? null : jso.getString("field");
		String sc = jso.getString("sc") == null ? null : jso.getString("sc");

		if (tableName == null || tableName.equals("") || fieldName == null || fieldName.equals("")) {
			return null;
		}
		Criterion[] criterion = null;
		if (sc != null && !sc.equals("")) {
			criterion = this.conditions(sc);
		}

		DetachedCriteria dc;
		try {
			dc = DetachedCriteria.forClass(Class.forName(tableName));
			if (criterion != null) {
				for (int c = 0; c < criterion.length; c++) {
					Criterion cs = criterion[c];
					dc.add(cs);
				}
			}
			dc.setProjection(Property.forName(fieldName));
			return dc;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
