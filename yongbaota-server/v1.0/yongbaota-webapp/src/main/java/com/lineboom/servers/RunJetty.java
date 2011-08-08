package com.lineboom.servers;

import java.io.File;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class RunJetty {
	public static void main(String[] args) throws Exception {
	    Server server = new Server(9090); //也可以改成其它端口
	    File rootDir = new File(RunJetty.class.getResource("/").getPath()).getParentFile().getParentFile();

	    String webAppPath = new File(rootDir, "src/main/webapp").getPath();
	    new WebAppContext(server, webAppPath, "/yongbaota");
	    server.start();
	}
}