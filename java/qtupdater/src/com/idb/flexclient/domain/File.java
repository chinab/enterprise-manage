package com.idb.flexclient.domain;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

public class File {
	private String id;
	private String productId;
	private String version;
	private String updateInfo;
	private String files;
	private String updatedFiles;
	private String createTime;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public File() {
	}

	public File(String id, String productId, String version, String updateInfo, String files, String updatedFiles, String createTime) {
		super();
		this.id = id;
		this.productId = productId;
		this.version = version;
		this.updateInfo = updateInfo;
		this.files = files;
		this.updatedFiles = updatedFiles;
		this.createTime = createTime;
	}

	public String getUpdatedFiles() {
		return updatedFiles;
	}

	public void setUpdatedFiles(String updatedFiles) {
		this.updatedFiles = updatedFiles;
	}

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public static SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public static void setDateFormat(SimpleDateFormat dateFormat) {
		File.dateFormat = dateFormat;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}

	public String getCreateTime() {
		if (StringUtils.isNotBlank(createTime)) {
			try {
				long parseLong = Long.parseLong(createTime);
				Date date = new Date(parseLong);
				return dateFormat.format(date);
			} catch (NumberFormatException e) {

			}
		}
		return "";
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
