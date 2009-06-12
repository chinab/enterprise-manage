package com.lineboom.enm.model.enmcrm;

/**
 * EsTest entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EsTest implements java.io.Serializable {

	// Fields

	private Long id;
	private String esTestName;

	// Constructors

	/** default constructor */
	public EsTest() {
	}

	/** full constructor */
	public EsTest(String esTestName) {
		this.esTestName = esTestName;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEsTestName() {
		return this.esTestName;
	}

	public void setEsTestName(String esTestName) {
		this.esTestName = esTestName;
	}

}