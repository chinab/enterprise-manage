package com.juduowang.yongbaota.service.impl;

import com.juduowang.common.dao.EntityDao;
import com.juduowang.common.service.impl.EntityManagerImpl;
import com.juduowang.yongbaota.model.JdwUser;
import com.juduowang.yongbaota.service.JdwUserManager;

public class JdwUserManagerImpl extends EntityManagerImpl<JdwUser, Long>
		implements JdwUserManager {

	public JdwUserManagerImpl(EntityDao<JdwUser, Long> entityDao) {
		super(entityDao);
	}

}
