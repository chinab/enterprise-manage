package com.lineboom.enm.model.enmboot;

public class WebsiteMenu implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String websiteMenuName;
	private String websiteMenuHref;
	private Long websiteMenuType;

	public WebsiteMenu(Long id, String websiteMenuName, String websiteMenuHref,
			Long websiteMenuType) {
		super();
		this.id = id;
		this.websiteMenuName = websiteMenuName;
		this.websiteMenuHref = websiteMenuHref;
		this.websiteMenuType = websiteMenuType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWebsiteMenuName() {
		return websiteMenuName;
	}

	public void setWebsiteMenuName(String websiteMenuName) {
		this.websiteMenuName = websiteMenuName;
	}

	public String getWebsiteMenuHref() {
		return websiteMenuHref;
	}

	public void setWebsiteMenuHref(String websiteMenuHref) {
		this.websiteMenuHref = websiteMenuHref;
	}

	public Long getWebsiteMenuType() {
		return websiteMenuType;
	}

	public void setWebsiteMenuType(Long websiteMenuType) {
		this.websiteMenuType = websiteMenuType;
	}

}
