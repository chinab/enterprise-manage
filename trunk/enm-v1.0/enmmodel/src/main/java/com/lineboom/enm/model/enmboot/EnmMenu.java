package com.lineboom.enm.model.enmboot;

/**
 * EnmMenu entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EnmMenu extends treenodes implements java.io.Serializable {

	// Fields

	private Long id;
	private Long enmMenuParentId;
	private String enmMenuName;
	private String enmMenuHref;
	private String enmMenuIco;
	private Long enmMenuType;
	private String extend1;

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
			String extend1) {
		this.enmMenuParentId = enmMenuParentId;
		this.enmMenuName = enmMenuName;
		this.enmMenuHref = enmMenuHref;
		this.enmMenuIco = enmMenuIco;
		this.enmMenuType = enmMenuType;
		this.extend1 = extend1;
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

	public String getExtend1() {
		return this.extend1;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}
	
	public TreeNode toTreeNode(){
		
	}
}