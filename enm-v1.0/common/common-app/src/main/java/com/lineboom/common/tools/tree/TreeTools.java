package com.lineboom.common.tools.tree;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

public class TreeTools {
	public static String getTree(List<TreeNodeSupport> treeNodeSupports){
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		for (TreeNodeSupport treeNodeSupport : treeNodeSupports) {
			treeNodes.add(treeNodeSupport.toTreeNode());
		}
		return JSONArray.fromObject(treeNodes).toString();
	}
}
