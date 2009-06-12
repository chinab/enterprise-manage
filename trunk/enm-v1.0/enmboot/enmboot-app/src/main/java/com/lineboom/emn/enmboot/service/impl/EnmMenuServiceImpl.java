package com.lineboom.emn.enmboot.service.impl;

import com.lineboom.common.service.impl.EntityServiceImpl;
import com.lineboom.emn.enmboot.service.EnmMenuService;
import com.lineboom.enm.model.enmboot.EnmMenu;

public class EnmMenuServiceImpl extends EntityServiceImpl<EnmMenu> implements EnmMenuService {
	public EnmMenuServiceImpl(){
		setPojoClass(EnmMenu.class);
	}
}