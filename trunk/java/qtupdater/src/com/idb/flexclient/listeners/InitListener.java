package com.idb.flexclient.listeners;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.idb.flexclient.manager.BaseManager;

public class InitListener implements ServletContextListener {

	public InitListener() {
	}

	public void contextInitialized(ServletContextEvent arg0) {
		try {
			Connection conn = BaseManager.getConn();
			Statement stat = conn.createStatement();
			stat.executeUpdate("create table if not exists product (id, name, version,productInfo, createTime);");
			stat.executeUpdate("create table if not exists file (id, productId, version, updateInfo,files,updatedFiles,createTime);");
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void contextDestroyed(ServletContextEvent arg0) {
	}

}
