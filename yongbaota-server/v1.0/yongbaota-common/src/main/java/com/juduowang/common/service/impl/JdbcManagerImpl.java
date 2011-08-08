package com.juduowang.common.service.impl;

import com.juduowang.common.dao.JdbcEntityDao;
import com.juduowang.common.service.JdbcManager;

public class JdbcManagerImpl implements JdbcManager {
	protected JdbcEntityDao jdbcEntityDao;

	public Object get(String statementName, Long id) {
		return jdbcEntityDao.get(statementName, id);
	}

	public JdbcEntityDao getJdbcEntityDao() {
		return jdbcEntityDao;
	}

	public void setJdbcEntityDao(JdbcEntityDao jdbcEntityDao) {
		this.jdbcEntityDao = jdbcEntityDao;
	}

}
