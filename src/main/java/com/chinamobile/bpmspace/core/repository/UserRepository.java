package com.chinamobile.bpmspace.core.repository;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.CatalogType;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.log.LogCatalog;
import com.chinamobile.bpmspace.core.domain.process.ProcessCatalog;
import com.chinamobile.bpmspace.core.exception.DuplicateFieldException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;

public class UserRepository {
	private MongoAccess mongo = new MongoAccess();

	public String register(String _username, String _password)
			throws DuplicateFieldException, EmptyFieldException {
		if (_username == null || _username.equals("")) {
			throw new EmptyFieldException("用户名为空");
		} else if (_password == null || _password.equals("")) {
			throw new EmptyFieldException("密码为空");
		}
		if (mongo.getUserByUsername(_username) != null) {
			throw new DuplicateFieldException("用户名已被注册");
		}
		User newUser = mongo.addUser(_username, _password);

		ProcessCatalog privateRootProcessCatalog = mongo.addProcessCatalog(
				"Private Catalog", "", CatalogType.PRIVATE, newUser.getId(),
				newUser.getId(), newUser.getUsername());
		ProcessCatalog shareRootProcessCatalog = mongo.addProcessCatalog(
				"Share Catalog", "", CatalogType.SHARE, newUser.getId(),
				newUser.getId(), newUser.getUsername());
		newUser.setPrivateProcessCatalogId(privateRootProcessCatalog.getId());
		newUser.setShareProcessCatalogId(shareRootProcessCatalog.getId());

		LogCatalog privateRootInstanceCatalog = mongo.addLogCatalog(
				"Private Catalog", "", CatalogType.PRIVATE, newUser.getId(),
				newUser.getId(), newUser.getUsername());
		LogCatalog shareRootInstanceCatalog = mongo.addLogCatalog(
				"Share Catalog", "", CatalogType.PRIVATE, newUser.getId(),
				newUser.getId(), newUser.getUsername());
		newUser.setPrivateLogCatalogId(privateRootInstanceCatalog.getId());
		newUser.setShareLogCatalogId(shareRootInstanceCatalog.getId());

		newUser = mongo.saveUser(newUser);
		return newUser.getId();
	}

	public String login(String _username, String _password)
			throws EmptyFieldException, NoExistException {
		if (_username == null || _username.equals("")) {
			throw new EmptyFieldException("用户名为空");
		} else if (_password == null || _password.equals("")) {
			throw new EmptyFieldException("密码为空");
		}
		User user = mongo.getUserByUsername(_username);
		if (user == null || !user.getPassword().equals(_password)) {
			throw new NoExistException("用户名或密码不正确");
		}
		return user.getId();
	}
}
