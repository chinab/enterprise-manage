package com.idb.flexclient.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.idb.flexclient.domain.Product;

public class ProductManager extends BaseManager {

	public void requestHandler(HttpServletRequest request, HttpServletResponse response) {
		String method = request.getParameter("method");
		if (StringUtils.isNotBlank(method)) {
			if (method.equals("add")) {
				String name = request.getParameter("name");
				String productInfo = request.getParameter("productInfo");
				add(name, productInfo);
				redirect(response, "ProductServlet?method=all");
			} else if (method.equals("remove")) {
				String id = request.getParameter("id");
				remove(id);
				redirect(response, "ProductServlet?method=all");
			} else if (method.equals("edit")) {
				String id = request.getParameter("id");
				String name = request.getParameter("name");
				String productInfo = request.getParameter("productInfo");
				Product product = new Product(id, name, null, productInfo, null);
				edit(product);
				redirect(response, "ProductServlet?method=all");
			} else if (method.equals("all")) {
				all(request, response);
			}
		}
	}

	private void all(HttpServletRequest request, HttpServletResponse response) {
		try {
			Connection conn = getConn();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select * from product order by createTime desc;");

			List<Product> products = new ArrayList<Product>();
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				String version = rs.getString("version");
				String productInfo = rs.getString("productInfo");
				String createTime = rs.getString("createTime");
				Product product = new Product(id, name, version, productInfo, createTime);
				products.add(product);
			}

			rs.close();
			conn.close();

			request.setAttribute("products", products);
			forward(request, response, "/jsps/products.jsp");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void edit(Product product) {
		try {
			Connection conn = getConn();
			PreparedStatement preparedStatement = conn.prepareStatement("update product set name=?, productInfo=? where id=?");
			preparedStatement.setString(1, product.getName());
			preparedStatement.setString(2, product.getProductInfo());
			preparedStatement.setString(3, product.getId());
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
			PreparedStatement preparedStatement = conn.prepareStatement("delete from product where id=?");
			preparedStatement.setString(1, id);
			preparedStatement.executeUpdate();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void add(String name, String productInfo) {
		try {
			Connection conn = getConn();
			PreparedStatement preparedStatement = conn.prepareStatement("insert into product(id, name, version,productInfo, createTime) values (?,?,?,?,?)");
			preparedStatement.setString(1, UUID.randomUUID().toString());
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, "");
			preparedStatement.setString(4, productInfo);
			preparedStatement.setString(5, Long.toString(System.currentTimeMillis()));
			preparedStatement.executeUpdate();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
