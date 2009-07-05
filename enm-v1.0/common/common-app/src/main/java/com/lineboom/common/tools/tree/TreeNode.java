package com.lineboom.common.tools.tree;

public class TreeNode {
	private Object id;
	private Object text;
	private Object iconCls;
	private Object href;
	private Object leaf;
	
	public TreeNode(Object id,Object text,Object iconCls,Object href,Object leaf){
		this.id = id;
		this.text = text;
		this.iconCls = iconCls;
		this.href = href;
		this.leaf = leaf;
	}
	
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public Object getText() {
		return text;
	}
	public void setText(Object text) {
		this.text = text;
	}

	public Object getIconCls() {
		return iconCls;
	}

	public void setIconCls(Object iconCls) {
		this.iconCls = iconCls;
	}

	public Object getHref() {
		return href;
	}

	public void setHref(Object href) {
		this.href = href;
	}

	public Object getLeaf() {
		return leaf;
	}

	public void setLeaf(Object leaf) {
		this.leaf = leaf;
	}
	
}
