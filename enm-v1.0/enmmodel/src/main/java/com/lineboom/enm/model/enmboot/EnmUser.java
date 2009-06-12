package com.lineboom.enm.model.enmboot;

/**
 * EnmUser entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EnmUser implements java.io.Serializable {

	// Fields

	private Long id;
	private String enmUserName;
	private String enmUserPassword;
	private Long enmRoleId;
	private String extend1;

	// Constructors

	/** default constructor */
	public EnmUser() {
	}

	/** minimal constructor */
	public EnmUser(Long enmRoleId) {
		this.enmRoleId = enmRoleId;
	}

	/** full constructor */
	public EnmUser(String enmUserName, String enmUserPassword, Long enmRoleId,
			String extend1) {
		this.enmUserName = enmUserName;
		this.enmUserPassword = enmUserPassword;
		this.enmRoleId = enmRoleId;
		this.extend1 = extend1;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEnmUserName() {
		return this.enmUserName;
	}

	public void setEnmUserName(String enmUserName) {
		this.enmUserName = enmUserName;
	}

	public String getEnmUserPassword() {
		return this.enmUserPassword;
	}

	public void setEnmUserPassword(String enmUserPassword) {
		this.enmUserPassword = enmUserPassword;
	}

	public Long getEnmRoleId() {
		return this.enmRoleId;
	}

	public void setEnmRoleId(Long enmRoleId) {
		this.enmRoleId = enmRoleId;
	}

	public String getExtend1() {
		return this.extend1;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}

}