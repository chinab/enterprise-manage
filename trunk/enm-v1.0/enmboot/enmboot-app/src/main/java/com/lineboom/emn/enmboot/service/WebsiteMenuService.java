package com.lineboom.emn.enmboot.service;

import com.lineboom.common.service.EntityService;
import com.lineboom.enm.model.enmboot.WebsiteMenu;

public interface WebsiteMenuService extends EntityService<WebsiteMenu>{
	public String[] getWebsiteMenuNames();
}
