package com.lineboom.emn.enmboot.service;

import com.lineboom.common.service.EntityService;
import com.lineboom.enm.model.enmboot.EnmMenu;

public interface EnmMenuService extends EntityService<EnmMenu,Long>{
	public void saveEnmMenu(EnmMenu enmMenu);
}
