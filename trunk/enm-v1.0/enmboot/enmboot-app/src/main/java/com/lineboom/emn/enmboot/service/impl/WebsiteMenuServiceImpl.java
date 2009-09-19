package com.lineboom.emn.enmboot.service.impl;

import java.util.List;

import com.lineboom.common.service.impl.EntityServiceImpl;
import com.lineboom.emn.enmboot.service.WebsiteMenuService;
import com.lineboom.enm.model.enmboot.WebsiteMenu;

public class WebsiteMenuServiceImpl extends EntityServiceImpl<WebsiteMenu> implements WebsiteMenuService {
	public WebsiteMenuServiceImpl(){
		setPojoClass(WebsiteMenu.class);
	}
	
	public String[] getWebsiteMenuNames(){
		List<WebsiteMenu> websiteMenus = getAll();
		if(websiteMenus!=null && websiteMenus.size()>0){
			String []websiteMenuNames = new String[websiteMenus.size()];
			int i=0;
			for (String websiteMenuName : websiteMenuNames) {
				websiteMenuNames[i++] = websiteMenuName;
			}
			return websiteMenuNames;
		}
		return null;
	}
}