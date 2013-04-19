package com.idb.flexclient.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.idb.flexclient.domain.File;
import com.idb.flexclient.domain.FileGroup;
import com.idb.flexclient.domain.Product;

public class FileManager extends BaseManager {
	private static final String CONTENT_TYPE = "text/html,charset=UTF-8";
	private static final String AIR_CONTENT_TYPE = "text/html,charset=UTF-8";
	private static final String ENCODING = "UTF-8";

	public void requestHandler(HttpServletRequest request, HttpServletResponse response) {

		String method = request.getParameter("method");
		if (StringUtils.isNotBlank(method)) {
			if (method.equals("remove")) {
				String id = request.getParameter("id");
				remove(id);
				redirect(response, "FileServlet?method=all");
			} else if (method.equals("edit")) {
				String id = request.getParameter("id");
				String version = request.getParameter("version");
				String updateInfo = request.getParameter("updateInfo");
				File file = new File(id, null, version, updateInfo, null);
				edit(file);
				redirect(response, "FileServlet?method=all");
			} else if (method.equals("download")) {
				String id = request.getParameter("id");
				String filename = request.getParameter("filename");
				download(id, filename, request, response);
			} else if (method.equals("all")) {
				all(request, response);
			}
		} else if (ServletFileUpload.isMultipartContent(new ServletRequestContext(request))) {
			uplaod(request, response);
			redirect(response, "FileServlet?method=all");
		}
	}

	private void edit(File file) {
		try {
			Connection conn = getConn();
			PreparedStatement preparedStatement = conn.prepareStatement("update file set version=?, updateInfo=? where id=?");
			preparedStatement.setString(1, file.getVersion());
			preparedStatement.setString(2, file.getUpdateInfo());
			preparedStatement.setString(3, file.getId());
			preparedStatement.executeUpdate();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void remove(String id) {
		try {
			Connection conn = getConn();
			PreparedStatement preparedStatement = conn.prepareStatement("delete from file where id=?");
			preparedStatement.setString(1, id);
			preparedStatement.executeUpdate();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void download(String id, String filename, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType(AIR_CONTENT_TYPE);
			response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + ".air\"");

			String path = request.getSession().getServletContext().getRealPath("/files");
			java.io.File file = new java.io.File(path, id+".air");
			InputStream input = new FileInputStream(file);
			OutputStream output = response.getOutputStream();
			IOUtils.copy(input, output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void all(HttpServletRequest request, HttpServletResponse response) {
		try {
			Connection conn = getConn();
			Statement statFile = conn.createStatement();
			Statement statProduct = conn.createStatement();
			ResultSet rsFile = statFile.executeQuery("select * from file order by createTime desc;");
			ResultSet rsProduct = statProduct.executeQuery("select * from product order by createTime desc;");

			Map<String, List<File>> fileMap = new HashMap<String, List<File>>();
			while (rsFile.next()) {
				String id = rsFile.getString("id");
				String productId = rsFile.getString("productId");
				String version = rsFile.getString("version");
				String updateInfo = rsFile.getString("updateInfo");
				String createTime = rsFile.getString("createTime");
				File file = new File(id, productId, version, updateInfo, createTime);
				List<File> list = null;
				if (fileMap.containsKey(productId)) {
					list = fileMap.get(productId);
				} else {
					list = new ArrayList<File>();
					fileMap.put(productId, list);
				}
				list.add(file);
			}

			List<FileGroup> fileGroups = new ArrayList<FileGroup>();
			while (rsProduct.next()) {
				String id = rsProduct.getString("id");
				String name = rsProduct.getString("name");
				String version = rsProduct.getString("version");
				String productInfo = rsProduct.getString("productInfo");
				String createTime = rsProduct.getString("createTime");
				Product product = new Product(id, name, version, productInfo, createTime);

				FileGroup fileGroup = new FileGroup();
				fileGroup.setProduct(product);
				fileGroup.setFiles(fileMap.get(id));
				fileGroups.add(fileGroup);
			}

			rsFile.close();
			rsProduct.close();
			conn.close();

			request.setAttribute("fileGroups", fileGroups);
			forward(request, response, "/jsps/files.jsp");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void uplaod(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType(CONTENT_TYPE);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		String path = request.getSession().getServletContext().getRealPath("/files");
		java.io.File file = new java.io.File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		factory.setRepository(file);
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload su = new ServletFileUpload(factory);
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> list = su.parseRequest(request);

			Map<String, String> paramMap = new HashMap<String, String>();
			InputStream input = null;
			for (FileItem item : list) {
				if (item.isFormField()) {
					String fileName = item.getFieldName();
					String value = item.getString(ENCODING);
					paramMap.put(fileName, value);
				} else {
					input = item.getInputStream();
				}
			}
			String method = paramMap.get("method");
			if (StringUtils.isNotBlank(method) && method.equals("upload") && input != null) {
				File fileObj = new File();
				fileObj.setId(UUID.randomUUID().toString());
				fileObj.setProductId(paramMap.get("productId"));
				fileObj.setUpdateInfo(paramMap.get("updateInfo"));
				fileObj.setVersion(paramMap.get("version"));

				OutputStream output = new FileOutputStream(new java.io.File(path, fileObj.getId()+".air"));
				IOUtils.copy(input, output);
				input.close();
				output.close();

				Connection conn = getConn();
				PreparedStatement preparedStatement = conn.prepareStatement("insert into file(id, productId, version, updateInfo,createTime) values (?,?,?,?,?)");
				preparedStatement.setString(1, fileObj.getId());
				preparedStatement.setString(2, fileObj.getProductId());
				preparedStatement.setString(3, fileObj.getVersion());
				preparedStatement.setString(4, fileObj.getUpdateInfo());
				preparedStatement.setString(5, Long.toString(System.currentTimeMillis()));
				preparedStatement.executeUpdate();
				conn.close();
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
