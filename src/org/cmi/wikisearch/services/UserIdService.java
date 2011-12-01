package org.cmi.wikisearch.services;

import org.cmi.wikisearch.dao.UserIdDao;

public class UserIdService {

	UserIdDao userIdDao;
	
	public synchronized int nextId() {
		int id = userIdDao.getId();
		userIdDao.setId(id + 1);
		return id;
	}

	public UserIdDao getUserIdDao() {
		return userIdDao;
	}

	public void setUserIdDao(UserIdDao userIdDao) {
		this.userIdDao = userIdDao;
	}
}
