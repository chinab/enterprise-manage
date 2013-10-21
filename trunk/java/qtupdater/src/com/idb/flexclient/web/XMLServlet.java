package com.idb.flexclient.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idb.flexclient.manager.XMLManager;

public class XMLServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private XMLManager xmlManager;

	public XMLServlet() {
		super();
		xmlManager = new XMLManager();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		xmlManager.requestHandler(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		xmlManager.requestHandler(request, response);
	}

}
