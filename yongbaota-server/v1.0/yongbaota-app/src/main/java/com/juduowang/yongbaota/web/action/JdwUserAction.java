package com.juduowang.yongbaota.web.action;

import com.juduowang.common.web.action.BaseActionSupport;
import com.juduowang.yongbaota.model.JdwUser;
import com.juduowang.yongbaota.service.JdwUserManager;

public class JdwUserAction extends BaseActionSupport {

	private JdwUserManager jdwUserManager;

	public void save() {
		JdwUser jdwUser = new JdwUser();
		jdwUser.setName(getStringParam("name"));
		jdwUser.setPassword(getStringParam("password"));
		jdwUserManager.save(jdwUser);
	}

	public void setJdwUserManager(JdwUserManager jdwUserManager) {
		this.jdwUserManager = jdwUserManager;
	}
}
