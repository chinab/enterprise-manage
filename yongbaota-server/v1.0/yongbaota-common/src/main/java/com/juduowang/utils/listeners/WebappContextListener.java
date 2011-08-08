package com.juduowang.utils.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WebappContextListener implements ServletContextListener {
	public static ApplicationContext cxt ;
	public static ServletContext servletContext;
	
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent arg0) {
		cxt = WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext());
		servletContext = arg0.getServletContext();
	}
	
	
}