package com.lineboom.emn.enmboot.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import net.sf.json.JSONArray;

import com.lineboom.common.tools.tree.TreeTools;
import com.lineboom.common.web.action.BaseActionSupport;
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
	private EnmMenu enmMenu;

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
	
	public void reverseMenuNode(){
		EnmMenu enmMenu = enmMenuService.get(getLongParam("enmMenuId"));
		Long enmMenuParentId = enmMenu.getEnmMenuParentId();
		if(enmMenuParentId.equals(-1L)){
			renderJSON("{enmMenuId:"+enmMenu.getId()+",enmMenuName:"+enmMenu.getEnmMenuName()+",nodeId:"+enmMenu.getId()+"}");
			return;
		}
		EnmMenu rootMenu = enmMenuService.get(enmMenuParentId);
		do{
			enmMenu = rootMenu;
			rootMenu = enmMenuService.get(enmMenuParentId);
			enmMenuParentId = rootMenu.getEnmMenuParentId();
		}while(!enmMenuParentId.equals(-1L));
		renderJSON("{enmMenuId:"+rootMenu.getId()+",enmMenuName:'"+rootMenu.getEnmMenuName()+"',nodeId:"+enmMenu.getId()+"}");
	}
	
	public void save(){
		enmMenuService.saveEnmMenu(enmMenu);
	}
	
	public void deleteNode(){
		enmMenuService.deleteAndChildren(enmMenu);
	}
	
	public void getXml() throws IOException{
		List<EnmMenu> all = enmMenuService.getAll();
		Document document = DocumentHelper.createDocument();
		Element datasEle = document.addElement("datas");
		for (EnmMenu enmMenu : all) {
			datasEle.addElement("data").addAttribute("href", enmMenu.getEnmMenuHref()).addAttribute("id", enmMenu.getId().toString());
		}
		
		HttpServletResponse response = getResponse();
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(document.asXML());
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

	public EnmMenu getEnmMenu() {
		return enmMenu;
	}

	public void setEnmMenu(EnmMenu enmMenu) {
		this.enmMenu = enmMenu;
	}
	
}
