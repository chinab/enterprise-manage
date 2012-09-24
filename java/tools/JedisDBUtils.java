package com.artogrid.commons.jedis;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;

import com.artogrid.commons.jedis.exception.JedisUtilsException;
import com.artogrid.service.Constants;
import com.artogrid.service.utils.generic.PaginatorObjUtil;
import com.artogrid.service.utils.generic.PinyinUtil;
import com.idb.segmentation.CNSegmentation;
import com.idb.segmentation.Lexeme;

/**
 * 用redis做为数据库 直接存取工具
 * 
 * @author yinshuwei
 * 
 */
public class JedisDBUtils {
	private static final String KEY_SEPARATOR = ":";
	private static final String KEY_WILDCARD = "*";
	private static final String KEY_WORD_HIT_MARK = "^";
	private static final String KEY_START_OBJECT = "O";
	private static final String KEY_SET_OBJECT_IDS = "SOI";
	private static final String KEY_START_FIELDS = "F";
	private static final String KEY_START_PREFIX = "P";
	private static final String KEY_START_WORD_TO_ID = "WI";
	private static final String KEY_START_ID_TO_WORD = "IW";
	private static final int PREFIX_ONE_SIZE = 30;

	private final static Logger logger = Logger.getLogger(JedisDBUtils.class);

	private static SortingParams sortingParams;

	static {
		sortingParams = new SortingParams();
		sortingParams.alpha();
		sortingParams.asc();
	}

	/**
	 * 保存对象到redis
	 * 
	 * @param entityName
	 *            实体名
	 * @param idFieldName
	 *            主键名
	 * @param object
	 *            存入的对象
	 * @param dbIndex
	 *            数据库序号 idblogin:0 irs:1 bond:2 (1-7)
	 * @throws JedisUtilsException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * 
	 */
	public static void save(String entityName, String idFieldName, Object object, int dbIndex) throws JedisUtilsException, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				String id = BeanUtils.getSimpleProperty(object, idFieldName);
				deleteObject(entityName, dbIndex, jedis, Arrays.asList(id));
				Transaction t = jedis.multi();
				saveObject(entityName, idFieldName, object, t, dbIndex);
				t.exec();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
	}

	/**
	 * 保存对象集合
	 * 
	 * @param entityName
	 *            实体名
	 * @param idFieldName
	 *            主键名
	 * @param objects
	 *            存入的对象集合
	 * @param dbIndex
	 *            数据库序号 idblogin:0 irs:1 bond:2 (1-7)
	 * @throws JedisUtilsException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("rawtypes")
	public static void saveAll(String entityName, String idFieldName, Collection objects, int dbIndex) throws JedisUtilsException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				List<String> ids = new ArrayList<String>();
				for (Object object : objects) {
					ids.add(BeanUtils.getSimpleProperty(object, idFieldName));
				}
				deleteObject(entityName, dbIndex, jedis, ids);
				Transaction t = jedis.multi();
				for (Object object : objects) {
					saveObject(entityName, idFieldName, object, t, dbIndex);
				}
				t.exec();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
	}

	/**
	 * 根据主键来删除对象
	 * 
	 * @param entityName
	 * @param id
	 * @param dbIndex
	 * @throws JedisUtilsException
	 */
	public static void delete(String entityName, String id, int dbIndex) throws JedisUtilsException {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				deleteObject(entityName, dbIndex, jedis, Arrays.asList(id));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
	}

	/**
	 * 根据主键数组来删除对象
	 * 
	 * @param entityName
	 * @param ids
	 * @param dbIndex
	 * @throws JedisUtilsException
	 */
	public static void delete(String entityName, String[] ids, int dbIndex) throws JedisUtilsException {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				deleteObject(entityName, dbIndex, jedis, Arrays.asList(ids));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
	}

	/**
	 * 删除实体的所有对象
	 * 
	 * @param entityName
	 * @param dbIndex
	 * @throws JedisUtilsException
	 */
	public static void delete(String entityName, int dbIndex) throws JedisUtilsException {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				jedis.select(Constants.DB_INDEX_QUERY);
				String[] delQueryIndexKeys = jedis.keys(getQueryIndexKey(entityName, KEY_WILDCARD, dbIndex)).toArray(new String[0]);
				if (delQueryIndexKeys.length > 0) {
					jedis.del(delQueryIndexKeys);
				}

				jedis.select(dbIndex);
				String[] delObjectKeys = jedis.keys(getObjectKey(KEY_WILDCARD, entityName)).toArray(new String[0]);
				if (delObjectKeys.length > 0) {
					jedis.del(delObjectKeys);
				}
				jedis.del(getSetObjectIdsKey(entityName));

				jedis.select(Constants.DB_INDEX_QUERY);
				String[] delIdToWordKeys = jedis.keys(getIdToWordKey(entityName, KEY_WILDCARD, dbIndex)).toArray(new String[0]);
				if (delIdToWordKeys.length > 0) {
					jedis.del(delIdToWordKeys);
				}
				String[] delWordToIdKeys = jedis.keys(getWordToIdKey(entityName, KEY_WILDCARD, dbIndex)).toArray(new String[0]);
				if (delWordToIdKeys.length > 0) {
					jedis.del(delWordToIdKeys);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
	}

	/**
	 * 通过主键获取数据
	 * 
	 * @param entityName
	 * @param id
	 * @param dbIndex
	 * @return
	 * @throws JedisUtilsException
	 */
	public static Map<String, String> get(String entityName, String id, int dbIndex) throws JedisUtilsException {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				jedis.select(dbIndex);
				String objectKey = getObjectKey(id, entityName);
				if (jedis.exists(objectKey)) {
					return jedis.hgetAll(objectKey);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
		return null;
	}

	/**
	 * 查询实体中的所有数据
	 * 
	 * @param entityName
	 * @param dbIndex
	 * @return
	 * @throws JedisUtilsException
	 */
	public static List<Map<String, String>> getAll(String entityName, int dbIndex) throws JedisUtilsException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				jedis.select(dbIndex);
				Set<String> objectKeys = jedis.keys(getObjectKey(KEY_WILDCARD, entityName));
				for (String objectKey : objectKeys) {
					if (jedis.exists(objectKey)) {
						result.add(jedis.hgetAll(objectKey));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
		return result;
	}

	/**
	 * 查询实体中的所有数据(带分页)
	 * 
	 * @param entityName
	 * @param orderBy
	 * @param paginatorObjUtil
	 * @param dbIndex
	 * @return
	 * @throws JedisUtilsException
	 */
	public static PaginatorObjUtil getAll(String entityName, String orderBy, PaginatorObjUtil paginatorObjUtil, int dbIndex) throws JedisUtilsException {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				jedis.select(dbIndex);
				String setObjectIdsKey = getSetObjectIdsKey(entityName);
				Set<String> ids = jedis.smembers(setObjectIdsKey);
				int totalRecords = ids.size();
				paginatorObjUtil.setTotalRecords(totalRecords);
				if (totalRecords > paginatorObjUtil.getStartNum()) {
					SortingParams objectSortingParams = new SortingParams();
					objectSortingParams.limit(paginatorObjUtil.getStartNum(), paginatorObjUtil.getPageSize());
					if (orderBy != null)
						objectSortingParams.by(getObjectKey(KEY_WILDCARD, entityName) + "->" + orderBy);
					List<String> recordIds = jedis.sort(setObjectIdsKey, objectSortingParams);
					List<Map<String, String>> result = new ArrayList<Map<String, String>>();
					for (String id : recordIds) {
						String objectKey = getObjectKey(id, entityName);
						if (jedis.exists(objectKey)) {
							result.add(jedis.hgetAll(objectKey));
						}
					}

					paginatorObjUtil.setRecords(result);
				} else {
					paginatorObjUtil.setRecords(null);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
		return paginatorObjUtil;
	}

	/**
	 * 通过单字段查询符合条件的个数
	 * 
	 * @param entityName
	 * @param fieldName
	 * @param value
	 * @param dbIndex
	 * @return
	 * @throws JedisUtilsException
	 */
	public static int count(String entityName, String fieldName, String value, int dbIndex) throws JedisUtilsException {
		int totalSize = 0;
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				jedis.select(Constants.DB_INDEX_QUERY);
				String queryKey = getQueryIndexKey(entityName, KEY_WILDCARD, fieldName, value, dbIndex);
				Set<String> keys = jedis.keys(queryKey);
				if (keys != null) {
					totalSize = keys.size();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
		return totalSize;
	}

	/**
	 * 通过单字段查询id
	 * 
	 * @param entityName
	 * @param fieldName
	 * @param value
	 * @param dbIndex
	 * @return
	 * @throws JedisUtilsException
	 */
	public static Set<String> findIdsByField(String entityName, String fieldName, String value, int dbIndex) throws JedisUtilsException {
		Set<String> ids = new HashSet<String>();
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				jedis.select(Constants.DB_INDEX_QUERY);
				String queryKey = getQueryIndexKey(entityName, KEY_WILDCARD, fieldName, value, dbIndex);
				Set<String> keys = jedis.keys(queryKey);
				for (String queryIndexKey : keys) {
					String id = getIdByQueryIndexKey(queryIndexKey);
					if (id != null) {
						ids.add(id);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
		return ids;
	}

	/**
	 * 通过多字段查询id
	 * 
	 * @param entityName
	 * @param fieldNames
	 * @param values
	 * @param dbIndex
	 * @return
	 * @throws JedisUtilsException
	 */
	public static Set<String> findIdsByFields(String entityName, String[] fieldNames, String[] values, int dbIndex) throws JedisUtilsException {
		Set<String> ids = null;
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else if (fieldNames.length != values.length) {
			throw new JedisUtilsException("fieldNames length not equles values length!");
		} else {
			for (int i = 0; i < fieldNames.length; i++) {
				Set<String> idsTemp = findIdsByField(entityName, fieldNames[i], values[i], dbIndex);
				if (ids == null) {
					ids = idsTemp;
				} else {
					ids.retainAll(idsTemp);
				}
			}
		}
		return ids;
	}

	/**
	 * 通过多字段查询数据
	 * 
	 * @param entityName
	 * @param fieldNames
	 * @param values
	 * @param dbIndex
	 * @return
	 * @throws JedisUtilsException
	 */
	public static List<Map<String, String>> findByFields(String entityName, String[] fieldNames, String[] values, int dbIndex) throws JedisUtilsException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Set<String> ids = findIdsByFields(entityName, fieldNames, values, dbIndex);
		Jedis jedis = JedisFactory.createJedis();
		try {
			if (ids != null && ids.size() > 0) {
				jedis.select(dbIndex);
				for (String id : ids) {
					String objectKey = getObjectKey(id, entityName);
					if (jedis.exists(objectKey)) {
						result.add(jedis.hgetAll(objectKey));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JedisFactory.closeJedis(jedis);
		}
		return result;
	}

	/**
	 * 通过多字段搜索含关键字的数据(区分大小写)
	 * 
	 * @param entityName
	 * @param fieldNames
	 * @param value
	 * @param dbIndexs
	 * @return
	 * @throws JedisUtilsException
	 */
	public static List<Map<String, String>> searchByFields(String entityName, String[] fieldNames, String value, int... dbIndexs) throws JedisUtilsException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (int dbIndex : dbIndexs) {
			List<String> ids = searchIdsByFields(entityName, fieldNames, value, dbIndex);
			result.addAll(idsToObjects(entityName, ids, dbIndex));
		}
		return result;
	}

	/**
	 * 通过多字段搜索含关键字的ID(区分大小写)
	 * 
	 * @param entityName
	 * @param fieldNames
	 * @param value
	 * @param dbIndexs
	 * @return
	 * @throws JedisUtilsException
	 */
	public static List<String> searchIdsByFields(String entityName, String[] fieldNames, String value, int dbIndex) throws JedisUtilsException {
		if (dbIndex > 7 || (dbIndex < 1 && dbIndex != -1)) {
			throw new JedisUtilsException("dbIndex must in 1 to 7 or equles -1!");
		}
		List<String> ids = new ArrayList<String>();
		Jedis jedis = JedisFactory.createJedis();
		try {
			jedis.select(Constants.DB_INDEX_QUERY);
			Set<String> idsSet = new HashSet<String>();
			for (int i = 0; i < fieldNames.length; i++) {
				String queryKey = getQueryIndexKey(entityName, KEY_WILDCARD, fieldNames[i], KEY_WILDCARD + value + KEY_WILDCARD, dbIndex);
				Set<String> keysTemp = jedis.keys(queryKey);
				for (String queryIndexKey : keysTemp) {
					idsSet.add(getIdByQueryIndexKey(queryIndexKey));
				}
			}
			if (idsSet.size() > 0) {
				ids.addAll(idsSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JedisFactory.closeJedis(jedis);
		}

		return ids;
	}

	/**
	 * 通过单字段查询数据
	 * 
	 * @param entityName
	 * @param fieldName
	 * @param value
	 * @param dbIndex
	 * @return
	 * @throws JedisUtilsException
	 */
	public static List<Map<String, String>> findByField(String entityName, String fieldName, String value, int dbIndex) throws JedisUtilsException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				jedis.select(Constants.DB_INDEX_QUERY);
				String queryKey = getQueryIndexKey(entityName, KEY_WILDCARD, fieldName, value, dbIndex);
				Set<String> keys = jedis.keys(queryKey);
				if (keys.size() > 0) {
					List<String> ids = new ArrayList<String>();
					for (String queryIndexKey : keys) {
						ids.add(getIdByQueryIndexKey(queryIndexKey));
					}

					jedis.select(dbIndex);
					for (String id : ids) {
						String objectKey = getObjectKey(id, entityName);
						if (jedis.exists(objectKey)) {
							result.add(jedis.hgetAll(objectKey));
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
		return result;
	}

	/**
	 * 通过单字段搜索含关键字的数据(区分大小写)
	 * 
	 * @param entityName
	 * @param fieldName
	 * @param value
	 * @param dbIndexs
	 * @return
	 * @throws JedisUtilsException
	 */
	public static List<Map<String, String>> searchByField(String entityName, String fieldName, String value, int... dbIndexs) throws JedisUtilsException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (int dbIndex : dbIndexs) {
			List<String> ids = searchIdsByField(entityName, fieldName, value, dbIndex);
			result.addAll(idsToObjects(entityName, ids, dbIndex));
		}
		return result;
	}

	/**
	 * 通过单字段搜索含关键字的ID(区分大小时)
	 * 
	 * @param entityName
	 * @param fieldName
	 * @param value
	 * @param dbIndex
	 * @return
	 * @throws JedisUtilsException
	 */
	public static List<String> searchIdsByField(String entityName, String fieldName, String value, int dbIndex) throws JedisUtilsException {
		if (dbIndex > 7 || (dbIndex < 1 && dbIndex != -1)) {
			throw new JedisUtilsException("dbIndex must in 1 to 7 or equles -1!");
		}
		List<String> ids = new ArrayList<String>();

		Jedis jedis = JedisFactory.createJedis();
		try {
			jedis.select(Constants.DB_INDEX_QUERY);
			String queryKey = getQueryIndexKey(entityName, KEY_WILDCARD, fieldName, KEY_WILDCARD + value + KEY_WILDCARD, dbIndex);
			Set<String> keys = jedis.keys(queryKey);
			if (keys.size() > 0) {
				for (String queryIndexKey : keys) {
					ids.add(getIdByQueryIndexKey(queryIndexKey));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JedisFactory.closeJedis(jedis);
		}
		return ids;
	}

	/**
	 * 清理指定编号的数据库 <br>
	 * 只提供 1-7号数据库的清理功能
	 * 
	 * @param dbIndex
	 * @throws JedisUtilsException
	 */
	public static void clearDBByIndex(int dbIndex) throws JedisUtilsException {
		logger.info("Start Clear " + dbIndex + " DB");
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				jedis.select(dbIndex);
				Set<String> keys = jedis.keys(KEY_WILDCARD);
				if (keys.size() > 0) {
					jedis.del(keys.toArray(new String[0]));
				}
				logger.info("Clear " + dbIndex + " DB:" + keys.size());
				// 清理 14号库
				jedis.select(Constants.DB_INDEX_SEACH);
				Set<String> keysSearch = jedis.keys(getIndexDBKey(dbIndex));
				if (keysSearch.size() > 0) {
					jedis.del(keysSearch.toArray(new String[0]));
				}
				logger.info("Clear " + dbIndex + " Search:" + keysSearch.size());
				// 清理 15号库
				jedis.select(Constants.DB_INDEX_QUERY);
				Set<String> keysQuery = jedis.keys(getIndexDBKey(dbIndex));
				if (keysQuery.size() > 0) {
					jedis.del(keysQuery.toArray(new String[0]));
				}
				logger.info("Clear " + dbIndex + " QUERY:" + keysQuery.size());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
	}

	/**
	 * 对象转换为字符串 <br>
	 * 目前只提供了Date类型的转换
	 * 
	 * @param obj
	 *            对象
	 * @return 字符串
	 */
	private static String objectToString(Object obj) {
		if (obj == null) {
			return "";
		}
		if (obj instanceof Date) {
			return Long.toString(((Date) obj).getTime());
		}
		return obj.toString();
	}

	/**
	 * 保存对象 （已提供jedis的情况下）
	 * 
	 * @param entityName
	 * @param idFieldName
	 * @param object
	 * @param dbIndex
	 * @param jedis
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws JedisUtilsException
	 */
	private static void saveObject(String entityName, String idFieldName, Object object, Transaction t, int dbIndex) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, JedisUtilsException {
		@SuppressWarnings("unchecked")
		Map<String, Object> describe = PropertyUtils.describe(object);

		Set<String> fieldNames = describe.keySet();
		if (!fieldNames.contains(idFieldName)) {
			throw new JedisUtilsException("object has not the idFieldName:" + idFieldName + "!");
		}
		Object idObject = describe.get(idFieldName);
		if (idObject == null) {
			throw new JedisUtilsException("keyvalue of the object is null!");
		}
		String id = objectToString(idObject);
		String objectKey = getObjectKey(id, entityName);

		HashMap<String, String> objectValue = new HashMap<String, String>();
		t.select(Constants.DB_INDEX_QUERY);
		for (String fieldName : fieldNames) {
			Object valueObject = describe.get(fieldName);
			if (!"class".equals(fieldName) && valueObject != null) {
				String value = objectToString(valueObject);
				objectValue.put(fieldName, value);
				String queryIndexKey = getQueryIndexKey(entityName, id, fieldName, value, dbIndex);
				t.set(queryIndexKey, id);
			}
		}
		t.select(dbIndex);
		t.hmset(objectKey, objectValue);
		t.sadd(getSetObjectIdsKey(entityName), id);
	}

	/**
	 * 删除对象（已存在jedis的情况下）
	 * 
	 * @param entityName
	 * @param dbIndex
	 * @param jedis
	 * @param id
	 * @param objectKey
	 */
	private static void deleteObject(String entityName, int dbIndex, Jedis jedis, List<String> ids) {
		try {
			jedis.select(dbIndex);
			String setObjectIdsKey = getSetObjectIdsKey(entityName);
			Set<String> exitsIds = jedis.smembers(setObjectIdsKey);
			exitsIds.retainAll(ids);
			if (exitsIds == null || exitsIds.size() == 0) {
				return;
			}

			jedis.select(Constants.DB_INDEX_QUERY);
			Set<String> delQueryIndexKeySet = new HashSet<String>();
			Set<String> delObjectKeySet = new HashSet<String>();
			for (String id : exitsIds) {
				delQueryIndexKeySet.addAll(jedis.keys(getQueryIndexKey(entityName, id, KEY_WILDCARD, dbIndex)));
				delObjectKeySet.add(getObjectKey(id, entityName));
			}
			if (delQueryIndexKeySet.size() > 0) {
				String[] delQueryIndexKeys = delQueryIndexKeySet.toArray(new String[0]);
				jedis.del(delQueryIndexKeys);
			}

			jedis.select(dbIndex);
			if (delObjectKeySet.size() > 0) {
				String[] delObjectKeys = delObjectKeySet.toArray(new String[0]);
				jedis.del(delObjectKeys);
			}
			if (exitsIds.size() > 0) {
				jedis.srem(getSetObjectIdsKey(entityName), exitsIds.toArray(new String[0]));
			}

			jedis.select(Constants.DB_INDEX_QUERY);
			for (String id : exitsIds) {
				String idToWordKey = getIdToWordKey(entityName, id, dbIndex);
				Set<String> words = jedis.smembers(idToWordKey);
				jedis.del(idToWordKey);
				for (String word : words) {
					jedis.srem(getWordToIdKey(entityName, word, dbIndex), id);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/****************** 下面的方法使用了索引 ******************/
	/**
	 * 保存数据，并对指定字段做索引，以方便搜索
	 * 
	 * @param entityName
	 *            实体名
	 * @param idFieldName
	 *            主键字段名
	 * @param object
	 *            要保存的对象
	 * @param indexFieldNames
	 *            要做索引的字段
	 * @param dbIndex
	 *            数据库序号 idblogin:0 irs:1 bond:2 (1-7)
	 * @throws JedisUtilsException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static void saveHasIndex(String entityName, String idFieldName, Object object, String[] indexFieldNames, int dbIndex) throws JedisUtilsException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				String id = BeanUtils.getSimpleProperty(object, idFieldName);
				deleteObject(entityName, dbIndex, jedis, Arrays.asList(id));
				Transaction t = jedis.multi();
				saveObject(entityName, idFieldName, object, t, dbIndex);
				saveIndexs(entityName, idFieldName, object, indexFieldNames, null, t, dbIndex);
				t.exec();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
	}

	/**
	 * 保存多个数据，并对指定字段做索引，以方便搜索
	 * 
	 * @param entityName
	 *            实体名
	 * @param idFieldName
	 *            主键字段名
	 * @param objects
	 *            要保存的所有对象
	 * @param indexFieldNames
	 *            要做索引的字段
	 * @param dbIndex
	 *            数据库序号 idblogin:0 irs:1 bond:2 (1-7)
	 * @throws JedisUtilsException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("rawtypes")
	public static void saveAllHasIndex(String entityName, String idFieldName, Collection objects, String[] indexFieldNames, int dbIndex) throws JedisUtilsException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				List<String> ids = new ArrayList<String>();
				for (Object object : objects) {
					ids.add(BeanUtils.getSimpleProperty(object, idFieldName));
				}
				deleteObject(entityName, dbIndex, jedis, ids);
				Transaction t = jedis.multi();
				for (Object object : objects) {
					saveObject(entityName, idFieldName, object, t, dbIndex);
					saveIndexs(entityName, idFieldName, object, indexFieldNames, null, t, dbIndex);
				}
				t.exec();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
	}

	/**
	 * 保存数据，并对指定字段和关键字做索引，以方便搜索
	 * 
	 * @param entityName
	 *            实体名
	 * @param idFieldName
	 *            主键字段名
	 * @param object
	 *            要保存的对象
	 * @param indexFieldNames
	 *            要做索引的字段
	 * @param keyword
	 *            关键字
	 * @param dbIndex
	 *            数据库序号 idblogin:0 irs:1 bond:2 (1-7)
	 * @throws JedisUtilsException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static void saveHasIndexAndKeyword(String entityName, String idFieldName, Object object, String[] indexFieldNames, String keyword, int dbIndex)
			throws JedisUtilsException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				String id = BeanUtils.getSimpleProperty(object, idFieldName);
				deleteObject(entityName, dbIndex, jedis, Arrays.asList(id));
				Transaction t = jedis.multi();
				saveObject(entityName, idFieldName, object, t, dbIndex);
				saveIndexs(entityName, idFieldName, object, indexFieldNames, keyword, t, dbIndex);
				t.exec();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
	}

	/**
	 * 保存多个数据，并对指定字段和关键字做索引，以方便搜索
	 * 
	 * @param entityName
	 *            实体名
	 * @param idFieldName
	 *            主键字段名
	 * @param objects
	 *            要保存的所有对象
	 * @param indexFieldNames
	 *            要做索引的字段
	 * @param keywords
	 *            所有关键字(写objects一一对映)
	 * @param dbIndex
	 *            数据库序号 idblogin:0 irs:1 bond:2 (1-7)
	 * @throws JedisUtilsException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("rawtypes")
	public static void saveAllHasIndexAndKeywords(String entityName, String idFieldName, Collection objects, String[] indexFieldNames, Collection<String> keywords, int dbIndex)
			throws JedisUtilsException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		} else {
			Jedis jedis = JedisFactory.createJedis();
			try {
				List<String> ids = new ArrayList<String>();
				for (Object object : objects) {
					ids.add(BeanUtils.getSimpleProperty(object, idFieldName));
				}
				deleteObject(entityName, dbIndex, jedis, ids);
				Transaction t = jedis.multi();
				Iterator<String> iterator = keywords.iterator();
				for (Object object : objects) {
					saveObject(entityName, idFieldName, object, t, dbIndex);
					saveIndexs(entityName, idFieldName, object, indexFieldNames, iterator.hasNext() ? iterator.next() : null, t, dbIndex);
				}
				t.exec();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JedisFactory.closeJedis(jedis);
			}
		}
	}

	/**
	 * 通过索引进行数据搜索
	 * 
	 * @param entityName
	 *            实体名
	 * @param text
	 *            搜索文本
	 * @param dbIndexs
	 *            要搜索的数据库序号 idblogin:0 irs:1 bond:2 (1-7)
	 * @return
	 * @throws JedisUtilsException
	 */
	public static List<Map<String, String>> searchHasIndex(String entityName, String text, int... dbIndexs) throws JedisUtilsException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (int dbIndex : dbIndexs) {
			List<String> ids = searchIdsHasIndex(entityName, text, dbIndex);
			result.addAll(idsToObjects(entityName, ids, dbIndex));
		}
		return result;
	}

	/**
	 * 
	 * 通过索引进行数据ID搜索
	 * 
	 * @param entityName
	 *            实体名
	 * @param text
	 *            搜索文本
	 * @param dbIndex
	 *            要搜索的数据库序号 idblogin:0 irs:1 bond:2 (1-7)
	 * @return
	 * @throws JedisUtilsException
	 */
	public static List<String> searchIdsHasIndex(String entityName, String text, int dbIndex) throws JedisUtilsException {
		if (dbIndex > 7 || (dbIndex < 1 && dbIndex != -1)) {
			throw new JedisUtilsException("dbIndex must in 1 to 7 or equles -1!");
		}
		List<String> ids = null;
		Jedis jedis = JedisFactory.createJedis();
		try {
			jedis.select(Constants.DB_INDEX_SEACH);
			text = text.trim();
			if (StringUtils.isNotBlank(text)) {
				CNSegmentation segmentation = new CNSegmentation(new StringReader(text), true);
				Lexeme nextLexeme = segmentationNext(segmentation);
				while (nextLexeme != null) {
					String lexemeText = nextLexeme.getLexemeText();
					List<String> idsTemp = new ArrayList<String>();
					if (nextLexeme != null) {
						String prefixFirstChar = lexemeText.substring(0, 1);
						String prefixKeyAll = getPrefixKey(entityName, prefixFirstChar, dbIndex);
						Set<String> prefixKeys = jedis.keys(prefixKeyAll);
						for (String prefixKey : prefixKeys) {
							Long zrank = jedis.zrank(prefixKey, lexemeText);
							if (zrank != null) {
								int index = zrank.intValue();
								boolean hasNext = true;
								do {
									Set<String> prefixs = jedis.zrange(prefixKey, index, index += PREFIX_ONE_SIZE);
									hasNext = prefixs.size() > PREFIX_ONE_SIZE;
									for (String prefix : prefixs) {
										if (prefix.startsWith(lexemeText)) {
											if (prefix.endsWith(KEY_WORD_HIT_MARK)) {
												String wordToIdKeyAll = getWordToIdKey(entityName, prefix, dbIndex);
												Set<String> wordToIdKeys = jedis.keys(wordToIdKeyAll);
												for (String wordToIdKey : wordToIdKeys) {
													idsTemp.addAll(jedis.smembers(wordToIdKey));
												}
											}
										} else {
											hasNext = false;
											break;
										}
									}
								} while (hasNext);
							}
						}
					}
					if (ids == null) {
						ids = idsTemp;
					} else {
						ids.retainAll(idsTemp);
					}

					nextLexeme = segmentationNext(segmentation);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JedisFactory.closeJedis(jedis);
		}
		return ids;
	}

	private static List<Map<String, String>> idsToObjects(String entityName, List<String> ids, int dbIndex) {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		}
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Jedis jedis = JedisFactory.createJedis();
		try {
			jedis.select(dbIndex);
			for (String id : ids) {
				String key = getObjectKey(id, entityName);
				if (jedis.exists(key)) {
					result.add(jedis.hgetAll(key));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JedisFactory.closeJedis(jedis);
		}
		return result;
	}

	/**
	 * 生成前缀索引与键词索引
	 * 
	 * @param entityName
	 * @param id
	 * @param values
	 * @param jedis
	 */
	private static void saveIndexs(String entityName, String idFieldName, Object object, String[] indexFieldNames, String keyword, Transaction t, int dbIndex) {
		if (dbIndex > 7 || dbIndex < 1) {
			throw new JedisUtilsException("dbIndex must in 1 to 7!");
		}
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> describe = PropertyUtils.describe(object);

			Set<String> fieldNames = describe.keySet();
			if (!fieldNames.contains(idFieldName)) {
				throw new JedisUtilsException("object has not the idFieldName:" + idFieldName + "!");
			}
			Object idObject = describe.get(idFieldName);
			if (idObject == null) {
				throw new JedisUtilsException("keyvalue of the object is null!");
			}
			String id = objectToString(idObject);
			if (indexFieldNames != null) {
				String[] values;
				if (keyword != null) {
					values = new String[indexFieldNames.length + 1];
					values[indexFieldNames.length] = keyword;
				} else {
					values = new String[indexFieldNames.length];
				}
				for (int i = 0; i < indexFieldNames.length; i++) {
					values[i] = objectToString(describe.get(indexFieldNames[i]));
				}
				t.select(Constants.DB_INDEX_SEACH);

				Map<String, Set<String>> prefixsMap = new HashMap<String, Set<String>>();
				Set<String> text = new HashSet<String>();
				for (String value : values) {
					String[] split = value.split("\\s|\\*|\\(|\\)|（|）|－|-|_");
					for (String word : split) {
						text.add(word.trim());
					}
				}
				String fullText = StringUtils.join(text, " ").toLowerCase();
				CNSegmentation segmentation = new CNSegmentation(new StringReader(fullText), true);
				Lexeme nextLexeme = segmentationNext(segmentation);
				while (nextLexeme != null) {
					String lexemeText = nextLexeme.getLexemeText();

					String word = lexemeText + KEY_WORD_HIT_MARK;
					String prefixFirstChar = lexemeText.substring(0, 1);
					String wordToIdKey = getWordToIdKey(entityName, word, dbIndex);
					String pinyin = PinyinUtil.cn2Spell(lexemeText);
					String wordPinyin = pinyin + KEY_WORD_HIT_MARK;
					// Long zrank = t.zrank(getPrefixKey(entityName,
					// prefixFirstChar, dbIndex), word);
					// if (zrank == null || zrank.intValue() == -1) {
					createPrefixsIndex(prefixsMap, lexemeText, prefixFirstChar);
					if (!pinyin.equals(lexemeText)) {
						createPrefixsIndex(prefixsMap, pinyin, pinyin.substring(0, 1));
					}
					logger.debug("word:" + word);
					// }
					t.sadd(wordToIdKey, id);
					t.sadd(getIdToWordKey(entityName, id, dbIndex), word);

					t.sadd(getWordToIdKey(entityName, wordPinyin, dbIndex), id);
					t.sadd(getIdToWordKey(entityName, id, dbIndex), wordPinyin);
					nextLexeme = segmentationNext(segmentation);
				}

				Set<String> keySet = prefixsMap.keySet();
				for (String prefixFirstChar : keySet) {
					Set<String> prefixss = prefixsMap.get(prefixFirstChar);
					String prefixKey = getPrefixKey(entityName, prefixFirstChar, dbIndex);
					for (String prefixs : prefixss) {
						t.zadd(prefixKey, 1.0, prefixs);
					}
					t.sort(prefixKey, sortingParams);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

	}

	private static Lexeme segmentationNext(CNSegmentation segmentation) {
		Lexeme nextLexeme = null;
		try {
			nextLexeme = segmentation.next();
		} catch (IOException e1) {
			nextLexeme = null;
			e1.printStackTrace();
		}
		return nextLexeme;
	}

	private static void createPrefixsIndex(Map<String, Set<String>> prefixsMap, String text, String prefixFirstChar) {
		Set<String> prefixs = prefixsMap.get(prefixFirstChar);
		if (prefixs == null) {
			prefixs = new HashSet<String>();
			prefixsMap.put(prefixFirstChar, prefixs);
		}
		for (int i = 0; i < text.length(); i++) {
			prefixs.add(text.substring(0, i + 1));
		}
		prefixs.add(text + KEY_WORD_HIT_MARK);
	}

	/**
	 * 获取用于存储对象的key
	 * 
	 * @param id
	 * @param entityName
	 * @return key
	 */
	private static String getObjectKey(String id, String entityName) {
		return StringUtils.join(new String[] { KEY_START_OBJECT, entityName, id }, KEY_SEPARATOR);
	}

	/**
	 * 这个key放在业务库中，用来存储实体的所有id
	 * 
	 * @param entityName
	 * @return
	 */
	private static String getSetObjectIdsKey(String entityName) {
		return StringUtils.join(new String[] { KEY_SET_OBJECT_IDS, entityName }, KEY_SEPARATOR);
	}

	/**
	 * 从对象查询索引的key获得id
	 * 
	 * @param objectKey
	 * @return
	 */
	private static String getIdByQueryIndexKey(String queryIndexKey) {
		String[] strs = queryIndexKey.split(KEY_SEPARATOR);
		if (strs.length == 6) {
			return strs[2];
		}
		return null;
	}

	/**
	 * 获取用来存储对象查询索引的key
	 * 
	 * @param value
	 * @param fieldName
	 * @param id
	 * @param entityName
	 * @return key
	 */
	private static String getQueryIndexKey(String entityName, String id, String fieldName, String value, int dbIndex) {
		return StringUtils.join(new Object[] { KEY_START_FIELDS, entityName, id, fieldName, value, dbIndex == -1 ? KEY_WILDCARD : dbIndex }, KEY_SEPARATOR);
	}

	/**
	 * 获取用来存储对象查询索引的key <br>
	 * fieldName与value合为一个字段
	 * 
	 * @param entityName
	 * @param id
	 * @param fieldNameAndValue
	 * @return
	 */
	private static String getQueryIndexKey(String entityName, String id, String fieldNameAndValue, int dbIndex) {
		logger.debug("Test key----" + StringUtils.join(new Object[] { KEY_WILDCARD, dbIndex == -1 ? KEY_WILDCARD : dbIndex }, KEY_SEPARATOR));
		return StringUtils.join(new Object[] { KEY_START_FIELDS, entityName, id, fieldNameAndValue, dbIndex == -1 ? KEY_WILDCARD : dbIndex }, KEY_SEPARATOR);
	}

	private static String getQueryIndexKey(String entityName, String idAndFieldNameAndValue, int dbIndex) {
		return StringUtils.join(new Object[] { KEY_START_FIELDS, entityName, idAndFieldNameAndValue, dbIndex == -1 ? KEY_WILDCARD : dbIndex }, KEY_SEPARATOR);
	}

	private static String getWordToIdKey(String entityName, String word, int dbIndex) {
		return StringUtils.join(new Object[] { KEY_START_WORD_TO_ID, entityName, word, dbIndex == -1 ? KEY_WILDCARD : dbIndex }, KEY_SEPARATOR);
	}

	private static String getIdToWordKey(String entityName, String id, int dbIndex) {
		return StringUtils.join(new Object[] { KEY_START_ID_TO_WORD, entityName, id, dbIndex == -1 ? KEY_WILDCARD : dbIndex }, KEY_SEPARATOR);
	}

	private static String getPrefixKey(String entityName, String prefixFirstChar, int dbIndex) {
		return StringUtils.join(new Object[] { KEY_START_PREFIX, entityName, prefixFirstChar, dbIndex == -1 ? KEY_WILDCARD : dbIndex }, KEY_SEPARATOR);
	}

	private static String getIndexDBKey(int dbIndex) {
		return StringUtils.join(new Object[] { KEY_WILDCARD, dbIndex == -1 ? KEY_WILDCARD : dbIndex }, KEY_SEPARATOR);
	}

}
