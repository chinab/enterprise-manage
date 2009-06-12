package com.lineboom.emn.enmboot.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.lineboom.common.action.BaseActionSupport;
import com.lineboom.emn.enmboot.service.EnmMenuService;
import com.lineboom.emn.enmboot.service.EnmRoleService;
import com.lineboom.emn.enmboot.service.EnmUserService;
import com.lineboom.enm.model.enmboot.EnmMenu;
import com.lineboom.enm.model.enmboot.EnmUser;

public class EnmMenuAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	private EnmMenuService enmMenuService;
	private EnmRoleService enmRoleService;
	private EnmUserService enmUserService;
	
	public void loginVal() throws Exception{
		String username = getStringParam("j_username");
		String password = getStringParam("j_password");
		List<EnmUser> enmUsers =enmUserService.getBy("enmUserName", username);
		if(enmUsers==null || enmUsers.size()<=0){
			renderJSON("{success:false,info:'用户名不存在'}");
		}else if(!enmUsers.get(0).getEnmUserPassword().equals(password)){
			renderJSON("{success:false,info:'密码错误'}");
		}else{
			getSession().setAttribute("user", enmUsers.get(0));
			renderJSON("{success:true}");
		}
	}
	
	public String login() throws Exception{
		if(getSession().getAttribute("user")==null){
			return "input";
		}else {
			return "success";
		}
	}

	public void getMenuList(){
		Long enmMenuParentId = getLongParam("enmMenuId");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("enmMenuParentId", enmMenuParentId);
		paramsMap.put("enmMenuType", 1L);
		List<EnmMenu> enmMenus = enmMenuService.find(paramsMap);
		JSONArray jsonArray = JSONArray.fromObject(enmMenus);
		renderJSON(jsonArray.toString());
	}
	
	public void getMenuTree(){
		Long enmMenuParentId = getLongParam("enmMenuId");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("enmMenuParentId", enmMenuParentId);
		paramsMap.put("enmMenuType", 1L);
		List<EnmMenu> enmMenus = enmMenuService.find(paramsMap);
		JSONArray jsonArray = JSONArray.fromObject(enmMenus);
		renderJSON(jsonArray.toString());
	}
	
	public EnmMenuService getEnmMenuService() {
		return enmMenuService;
	}

	public void setEnmMenuService(EnmMenuService enmMenuService) {
		this.enmMenuService = enmMenuService;
	}

	public EnmRoleService getEnmRoleService() {
		return enmRoleService;
	}

	public void setEnmRoleService(EnmRoleService enmRoleService) {
		this.enmRoleService = enmRoleService;
	}

	public EnmUserService getEnmUserService() {
		return enmUserService;
	}

	public void setEnmUserService(EnmUserService enmUserService) {
		this.enmUserService = enmUserService;
	}
	
}
