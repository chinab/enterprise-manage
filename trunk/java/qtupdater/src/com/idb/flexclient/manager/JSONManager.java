package com.idb.flexclient.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import com.idb.flexclient.domain.File;

public class JSONManager extends BaseManager {

	@Override
	public void requestHandler(HttpServletRequest request, HttpServletResponse response) {
		StringBuffer requestURL = request.getRequestURL();
		String productName = requestURL.substring(requestURL.lastIndexOf("/") + 1, requestURL.lastIndexOf(".jsonfile"));
		String urlPre = requestURL.substring(0, requestURL.lastIndexOf("/") + 1);

		try {
			Connection conn = getConn();
			PreparedStatement preparedStatement = conn
					.prepareStatement("select file.* from product,file where upper(product.name)=? and file.productId=product.id  order by file.createTime desc Limit 1 Offset 0;");
			preparedStatement.setString(1, productName.toUpperCase());
			ResultSet rs = preparedStatement.executeQuery();

			File file = null;
			while (rs.next()) {
				String id = rs.getString("id");
				String productId = rs.getString("productId");
				String version = rs.getString("version");
				String updateInfo = rs.getString("updateInfo");
				String files = rs.getString("files");
				String updatedFiles = rs.getString("updatedFiles");
				String createTime = rs.getString("createTime");
				file = new File(id, productId, version, updateInfo, files, updatedFiles, createTime);
			}
			String result = "{}";
			if (file != null) {
				result = createJson(file, urlPre);
			}
			rs.close();
			conn.close();

			request.setAttribute("result", result);
			forward(request, response, "/jsps/json.jsp");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String createJson(File file, String urlPre) {
		String id = file.getId();

		// Map<String, String> resultMap = new HashMap<String, String>();
		// resultMap.put("version", file.getVersion());
		// resultMap.put("rootUrl", urlPre + "files/" + id + "/");
		// resultMap.put("allFiles", file.getFiles());
		// resultMap.put("otherFiles", getOtherFiles(file.getFiles(),
		// file.getUpdatedFiles()));
		// resultMap.put("updatedFiles", updatedFilesStr);
		// resultMap.put("zipFile", urlPre + "files/" + id + ".zip");
		// resultMap.put("description", file.getUpdateInfo());
		String version = file.getVersion();

		String[] allFiles = file.getFiles().split(",");
		String[] updatedFiles = file.getUpdatedFiles().split(",");
		Set<String> otherFiles = getOtherFiles(allFiles, updatedFiles);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ret", true);
		JSONObject resObject = new JSONObject();
		JSONArray detailObject = new JSONArray();

		String versionOld = "20140101";

		for (String fileInfo : updatedFiles) {
			String[] split = fileInfo.split(": ");
			if (split.length == 2) {
				JSONObject fileObject = new JSONObject();

				String fileName = dbPath + "/" + id + split[0] + split[1];
				java.io.File f = new java.io.File(fileName);
				if (f.exists()) {
					fileObject.put("folder", split[0]);
					fileObject.put("filename", split[1]);
					fileObject.put("delflag", JSONNull.getInstance());
					fileObject.put("size", f.length());
					fileObject.put("version", version);
					detailObject.add(fileObject);
				}
			}
		}
		for (String fileInfo : otherFiles) {
			String[] split = fileInfo.split(": ");
			if (split.length == 2) {
				JSONObject fileObject = new JSONObject();

				String fileName = dbPath + "/" + id + split[0] + split[1];
				java.io.File f = new java.io.File(fileName);
				if (f.exists()) {
					fileObject.put("folder", split[0]);
					fileObject.put("filename", split[1]);
					fileObject.put("delflag", JSONNull.getInstance());
					fileObject.put("size", f.length());
					fileObject.put("version", versionOld);
					detailObject.add(fileObject);
				}
			}
		}
		resObject.put("detail", detailObject);

		JSONObject majorObject = new JSONObject();
		majorObject.put("urlRoot", "qtupdater/files/" + id+"/");
		majorObject.put("memo", file.getUpdateInfo());
		majorObject.put("update_version", versionOld);
		majorObject.put("version", version);

		resObject.put("major", majorObject);

		jsonObject.put("res", resObject);

		return jsonObject.toString();
	}

	private Set<String> getOtherFiles(String[] allFiles, String[] updatedFiles) {
		Set<String> others = new HashSet<String>();
		for (String all : allFiles) {
			others.add(all);
		}
		for (String updated : updatedFiles) {
			others.remove(updated);
		}
		return others;
	}
}
