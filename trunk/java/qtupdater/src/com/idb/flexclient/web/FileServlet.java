package com.idb.flexclient.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idb.flexclient.manager.FileManager;

public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FileManager fileManager;

	public FileServlet() {
		super();
		fileManager = new FileManager();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		fileManager.requestHandler(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		fileManager.requestHandler(request, response);
	}

}
