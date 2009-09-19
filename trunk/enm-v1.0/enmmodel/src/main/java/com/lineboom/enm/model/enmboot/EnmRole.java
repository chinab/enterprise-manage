package com.lineboom.enm.model.enmboot;

/**
 * EnmRole entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EnmRole implements java.io.Serializable {

	private static final long serialVersionUID = -7734708981656110441L;
	// Fields

	private Long id;
	private String enmRoleName;
	private String extend1;

	// Constructors

	/** default constructor */
	public EnmRole() {
	}

	/** full constructor */
	public EnmRole(String enmRoleName, String extend1) {
		this.enmRoleName = enmRoleName;
		this.extend1 = extend1;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEnmRoleName() {
		return this.enmRoleName;
	}

	public void setEnmRoleName(String enmRoleName) {
		this.enmRoleName = enmRoleName;
	}

	public String getExtend1() {
		return this.extend1;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}

}