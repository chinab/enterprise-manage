package com.idb.flexclient.domain;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

public class Product {
	private String id;
	private String name;
	private String version;
	private String productInfo;
	private String createTime;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");

	public Product() {

	}

	public Product(String id, String name, String version, String productInfo, String createTime) {
		super();
		this.id = id;
		this.name = name;
		this.version = version;
		this.productInfo = productInfo;
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(String productInfo) {
		this.productInfo = productInfo;
	}

	public String getCreateTime() {
		if (StringUtils.isNotBlank(createTime)) {
			long parseLong = Long.parseLong(createTime);
			Date date = new Date(parseLong);
			return dateFormat.format(date);
		}
		return "";
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
