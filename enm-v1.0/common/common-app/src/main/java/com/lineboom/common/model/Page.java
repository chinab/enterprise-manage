package com.lineboom.common.model;

import java.util.List;

/**
 * 分页用的vo
 * @author yinshuwei
 *
 * @param <PT>
 */
public class Page<PT> {
	private List<PT> data ;
	private int totalCount;
	private int pageNo;
	
	public List<PT> getData() {
		return data;
	}
	public void setData(List<PT> data) {
		this.data = data;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return data.size();
	}
}
