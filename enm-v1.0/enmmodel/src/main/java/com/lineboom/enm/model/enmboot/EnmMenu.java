package com.lineboom.enm.model.enmboot;

import com.lineboom.common.tools.tree.TreeNode;
import com.lineboom.common.tools.tree.TreeNodeSupport;

/**
 * EnmMenu entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EnmMenu extends TreeNodeSupport implements java.io.Serializable {
	private static final long serialVersionUID = 7128898069911695748L;
	private Long id;
	private Long enmMenuParentId;
	private String enmMenuName;
	private String enmMenuHref;
	private String enmMenuIco;
	private Long enmMenuType;
	private String enmMenuDisable;
	private Long enmMenuDisplayNo;
	private String entexd1;

	// Constructors

	/** default constructor */
	public EnmMenu() {
	}

	/** minimal constructor */
	public EnmMenu(Long enmMenuParentId) {
		this.enmMenuParentId = enmMenuParentId;
	}

	/** full constructor */
	public EnmMenu(Long enmMenuParentId, String enmMenuName,
			String enmMenuHref, String enmMenuIco, Long enmMenuType,
			String enmMenuDisable, Long enmMenuDisplayNo, String entexd1) {
		this.enmMenuParentId = enmMenuParentId;
		this.enmMenuName = enmMenuName;
		this.enmMenuHref = enmMenuHref;
		this.enmMenuIco = enmMenuIco;
		this.enmMenuType = enmMenuType;
		this.enmMenuDisable = enmMenuDisable;
		this.enmMenuDisplayNo = enmMenuDisplayNo;
		this.entexd1 = entexd1;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEnmMenuParentId() {
		return this.enmMenuParentId;
	}

	public void setEnmMenuParentId(Long enmMenuParentId) {
		this.enmMenuParentId = enmMenuParentId;
	}

	public String getEnmMenuName() {
		return this.enmMenuName;
	}

	public void setEnmMenuName(String enmMenuName) {
		this.enmMenuName = enmMenuName;
	}

	public String getEnmMenuHref() {
		return this.enmMenuHref;
	}

	public void setEnmMenuHref(String enmMenuHref) {
		this.enmMenuHref = enmMenuHref;
	}

	public String getEnmMenuIco() {
		return this.enmMenuIco;
	}

	public void setEnmMenuIco(String enmMenuIco) {
		this.enmMenuIco = enmMenuIco;
	}

	public Long getEnmMenuType() {
		return this.enmMenuType;
	}

	public void setEnmMenuType(Long enmMenuType) {
		this.enmMenuType = enmMenuType;
	}

	public String getEnmMenuDisable() {
		return this.enmMenuDisable;
	}

	public void setEnmMenuDisable(String enmMenuDisable) {
		this.enmMenuDisable = enmMenuDisable;
	}

	public Long getEnmMenuDisplayNo() {
		return this.enmMenuDisplayNo;
	}

	public void setEnmMenuDisplayNo(Long enmMenuDisplayNo) {
		this.enmMenuDisplayNo = enmMenuDisplayNo;
	}

	public String getEntexd1() {
		return this.entexd1;
	}

	public void setEntexd1(String entexd1) {
		this.entexd1 = entexd1;
	}

	@Override
	protected TreeNode toTreeNode() {
		return new TreeNode(this.id,this.enmMenuName,this.enmMenuIco,this.enmMenuHref,this.enmMenuType.equals(2L));
	}

}