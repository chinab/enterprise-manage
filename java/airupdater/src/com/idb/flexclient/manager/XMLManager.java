package com.idb.flexclient.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idb.flexclient.domain.File;

public class XMLManager extends BaseManager {

	@Override
	public void requestHandler(HttpServletRequest request, HttpServletResponse response) {
		StringBuffer requestURL = request.getRequestURL();
		String productName = requestURL.substring(requestURL.lastIndexOf("/") + 1, requestURL.lastIndexOf(".xml"));
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
				String createTime = rs.getString("createTime");
				file = new File(id, productId, version, updateInfo, createTime);
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
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><update xmlns=\"http://ns.adobe.com/air/framework/update/description/2.5\"><version>" + file.getVersion()
				+ "</version><versionNumber>" + file.getVersion() + "</versionNumber><url>" + urlPre + "files/" + file.getId() + ".air</url><description>"
				+ file.getUpdateInfo() + "</description></update>";
	}
}
