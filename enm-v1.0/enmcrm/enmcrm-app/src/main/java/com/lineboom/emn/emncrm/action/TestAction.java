package com.lineboom.emn.emncrm.action;

import com.lineboom.common.web.action.BaseActionSupport;
import com.lineboom.emn.emncrm.service.TestService;

public class TestAction extends BaseActionSupport{

	private static final long serialVersionUID = 1L;
	private TestService testService;
	
	public String getUnit(){
		getRequest().setAttribute("esTest", testService.get(1L));
		return "getUnit";
	}

	public void setTestService(TestService testService) {
		this.testService = testService;
	}
	
}
