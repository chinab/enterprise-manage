package com.lineboom.emn.enmboot.action;

import java.util.List;

import net.sf.json.JSONArray;

import com.lineboom.common.action.BaseActionSupport;
import com.lineboom.common.tools.tree.TreeTools;
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

	@SuppressWarnings("unchecked")
	public void getMenuList(){
		Long enmMenuParentId = getLongParam("enmMenuId");
		List<EnmMenu> enmMenus = enmMenuService
				.find(
						"from EnmMenu em where em.enmMenuParentId=? and em.enmMenuType=? and "+(!enmMenuParentId.equals(-1L) ? "em.enmMenuDisable='Y'":"1=1")+" order by em.enmMenuDisplayNo",
						enmMenuParentId, 1L );
		JSONArray jsonArray = JSONArray.fromObject(enmMenus);
		renderJSON(jsonArray.toString());
	}

	@SuppressWarnings("unchecked")
	public void getMenuTree(){
		Long enmMenuParentId = getLongParam("node");
		List<EnmMenu> enmMenus = enmMenuService.find("from EnmMenu em where em.enmMenuParentId=? order by em.enmMenuDisplayNo", enmMenuParentId);
		renderJSON(TreeTools.getTree(enmMenus));
	}
	
	public void update(){
		Long enmMenuId = getLongParam("enmMenuId");
		String enmMenuName = getStringParam("enmMenuName");
		Long enmMenuType = getLongParam("enmMenuType");
		Long enmMenuDisplayNo = getLongParam("enmMenuDisplayNo");
		String enmMenuHref = getStringParam("enmMenuHref");
		String enmMenuIco = getStringParam("enmMenuIco");
		String enmMenuDisable = getStringParam("enmMenuDisable");
		EnmMenu enmMenu = enmMenuService.get(enmMenuId);
		enmMenu.setEnmMenuDisable(enmMenuDisable);
		enmMenu.setEnmMenuDisplayNo(enmMenuDisplayNo);
		enmMenu.setEnmMenuHref(enmMenuHref);
		enmMenu.setEnmMenuIco(enmMenuIco);
		enmMenu.setEnmMenuName(enmMenuName);
		enmMenu.setEnmMenuType(enmMenuType);
		enmMenuService.update(enmMenu);
	}
	
	public void insert(){
		Long enmMenuParentId = getLongParam("enmMenuParentId");
		String enmMenuName = getStringParam("enmMenuName");
		Long enmMenuType = getLongParam("enmMenuType");
		Long enmMenuDisplayNo = getLongParam("enmMenuDisplayNo");
		String enmMenuHref = getStringParam("enmMenuHref");
		String enmMenuIco = getStringParam("enmMenuIco");
		String enmMenuDisable = getStringParam("enmMenuDisable");
		EnmMenu enmMenu = new EnmMenu();
		enmMenu.setEnmMenuParentId(enmMenuParentId);
		enmMenu.setEnmMenuDisable(enmMenuDisable);
		enmMenu.setEnmMenuDisplayNo(enmMenuDisplayNo);
		enmMenu.setEnmMenuHref(enmMenuHref);
		enmMenu.setEnmMenuIco(enmMenuIco);
		enmMenu.setEnmMenuName(enmMenuName);
		enmMenu.setEnmMenuType(enmMenuType);
		enmMenuService.save(enmMenu);
	}
	
	
	public void edit(){
		Long enmMenuId = getLongParam("enmMenuId");
		renderJSON("{enmMenu:"+JSONArray.fromObject(enmMenuService.get(enmMenuId)).toString()+"}");
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
