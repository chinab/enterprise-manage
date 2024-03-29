package com.idb.flexclient.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.idb.flexclient.domain.File;

public class XMLManager extends BaseManager {

	@Override
	public void requestHandler(HttpServletRequest request, HttpServletResponse response) {
		StringBuffer requestURL = request.getRequestURL();
		String productName = requestURL.substring(requestURL.lastIndexOf("/") + 1, requestURL.lastIndexOf(".xmlfile"));
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
			String result = "<?xml version=\"1.0\" encoding=\"utf-8\"?><update xmlns=\"http://ns.adobe.com/air/framework/update/description/2.5\"></update>";
			if (file != null) {
				result = createXML(file, urlPre);
			}
			rs.close();
			conn.close();

			request.setAttribute("result", result);
			forward(request, response, "/jsps/xml.jsp");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String createXML(File file, String urlPre) {
		String updatedFilesStr = file.getUpdatedFiles();
		String id = file.getId();

		String result = "<?xml version=\"1.0\" encoding=\"utf-8\"?><update><version>" + file.getVersion() + "</version><rootUrl>" + urlPre + "files/" + id + "/"
				+ "</rootUrl><allFiles>" + file.getFiles() + "</allFiles><otherFiles>" + getOtherFiles(file.getFiles(), file.getUpdatedFiles()) + "</otherFiles><updatedFiles>";
		result += updatedFilesStr;
		result += "</updatedFiles><zipFile><file>";
		result += urlPre + "files/" + id;
		result += ".zip</file></zipFile><description>" + file.getUpdateInfo() + "</description></update>";
		return result;
	}

	private String getOtherFiles(String allFiles, String updatedFiles) {
		String[] alls = allFiles.split(",");
		Set<String> others = new HashSet<String>();
		for (String all : alls) {
			others.add(all);
		}
		String[] updateds = updatedFiles.split(",");
		for (String updated : updateds) {
			others.remove(updated);
		}
		return StringUtils.join(others, ",");
	}
}
