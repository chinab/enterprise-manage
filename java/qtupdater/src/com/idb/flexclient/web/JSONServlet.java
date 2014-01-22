package com.idb.flexclient.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idb.flexclient.manager.JSONManager;

public class JSONServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONManager jsonManager;

	public JSONServlet() {
		super();
		jsonManager = new JSONManager();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		jsonManager.requestHandler(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		jsonManager.requestHandler(request, response);
	}

}
