package com.lineboom.emn.enmboot.service.impl;

import com.lineboom.common.service.impl.EntityServiceImpl;
import com.lineboom.emn.enmboot.service.EnmRoleService;
import com.lineboom.enm.model.enmboot.EnmRole;

public class EnmRoleServiceImpl extends EntityServiceImpl<EnmRole,Long> implements EnmRoleService {
	public EnmRoleServiceImpl(){
		setPojoClass(EnmRole.class);
	}
}
