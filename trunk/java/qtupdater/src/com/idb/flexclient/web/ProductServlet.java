package com.idb.flexclient.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idb.flexclient.manager.ProductManager;

public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductManager productManager;

	public ProductServlet() {
		super();
		productManager = new ProductManager();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		productManager.requestHandler(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		productManager.requestHandler(request, response);
	}

}
