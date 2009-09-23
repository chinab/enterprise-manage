package com.lineboom.emn.enmboot.service.impl;

import java.util.List;

import com.lineboom.common.service.impl.EntityServiceImpl;
import com.lineboom.emn.enmboot.service.EnmMenuService;
import com.lineboom.enm.model.enmboot.EnmMenu;

public class EnmMenuServiceImpl extends EntityServiceImpl<EnmMenu,Long> implements EnmMenuService {
	public EnmMenuServiceImpl(){
		setPojoClass(EnmMenu.class);
	}

	public void saveEnmMenu(EnmMenu enmMenu) {
		if (enmMenu.getEnmMenuParentId()==null) {
			enmMenu.setEnmMenuParentId(-1L);
		}
		saveOrUpdate(enmMenu);
	}

	public void deleteAndChildren(EnmMenu enmMenu) {
		List<EnmMenu> list = getBy("enmMenuParentId", enmMenu.getId());
		for (EnmMenu enmMenuChild : list) {
			deleteAndChildren(enmMenuChild);
		}
		delete(enmMenu);
	}
}