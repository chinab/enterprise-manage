package com.lineboom.emn.enmboot.service.impl;

import com.lineboom.common.service.impl.EntityServiceImpl;
import com.lineboom.emn.enmboot.service.EnmUserService;
import com.lineboom.enm.model.enmboot.EnmUser;

public class EnmUserServiceImpl extends EntityServiceImpl<EnmUser> implements EnmUserService {
	public EnmUserServiceImpl(){
		setPojoClass(EnmUser.class);
	}
}
