package com.lineboom.common.dao.impl;

public class EntityDaoNotFindIdException extends RuntimeException {

	private static final long serialVersionUID = -7064234210672435406L;

	public EntityDaoNotFindIdException() {
		super();
	}

	public EntityDaoNotFindIdException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public EntityDaoNotFindIdException(String arg0) {
		super(arg0);
	}

	public EntityDaoNotFindIdException(Throwable arg0) {
		super(arg0);
	}

}
