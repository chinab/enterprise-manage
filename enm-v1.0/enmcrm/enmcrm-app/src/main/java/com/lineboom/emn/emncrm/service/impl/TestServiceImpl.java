package com.lineboom.emn.emncrm.service.impl;

import com.lineboom.common.service.impl.EntityServiceImpl;
import com.lineboom.emn.emncrm.service.TestService;
import com.lineboom.enm.enmcrm.model.EsTest;

public class TestServiceImpl extends EntityServiceImpl<EsTest> implements TestService{
	public TestServiceImpl(){
		setPojoName("com.lineboom.enm.enmcrm.model.EsTest");
	}
}