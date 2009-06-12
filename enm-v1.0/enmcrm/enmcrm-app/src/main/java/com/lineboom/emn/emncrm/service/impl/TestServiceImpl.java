package com.lineboom.emn.emncrm.service.impl;

import com.lineboom.common.service.impl.EntityServiceImpl;
import com.lineboom.emn.emncrm.service.TestService;
import com.lineboom.enm.model.enmcrm.EsTest;

public class TestServiceImpl extends EntityServiceImpl<EsTest> implements TestService{
	public TestServiceImpl(){
		setPojoClass(EsTest.class);
	}
}