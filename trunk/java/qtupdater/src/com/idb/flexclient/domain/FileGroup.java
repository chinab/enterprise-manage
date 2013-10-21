package com.idb.flexclient.domain;

import java.util.List;

public class FileGroup {
	private Product product;
	private List<File> files;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
}
