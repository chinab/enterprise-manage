package com.lineboom.common.tools.tree;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

public class TreeTools {
	@SuppressWarnings("unchecked")
	public static String getTree(List treeNodeSupports){
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		for (Object treeNodeSupport : treeNodeSupports) {
			treeNodes.add(((TreeNodeSupport)treeNodeSupport).toTreeNode());
		}
		return JSONArray.fromObject(treeNodes).toString();
	}
}
