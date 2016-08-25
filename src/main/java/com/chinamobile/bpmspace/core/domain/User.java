package com.chinamobile.bpmspace.core.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User {
	@Id
	private String id;
	@Indexed
	private String username = "";
	private String password = "";
	// id of private root catalog
	private String privateProcessCatalogId = "";
	// id of share root catalog
	private String shareProcessCatalogId = "";

	// id of private root catalog instance
	private String privateLogCatalogId = "";
	// id of share root catalog instance
	private String shareLogCatalogId = "";

	// map from shared process id to shared time
	private Map<String, Date> inSharedProcess = new HashMap<String, Date>();

	public User() {

	}

	public User(String _username, String _password) {
		username = _username;
		password = _password;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrivateProcessCatalogId() {
		return privateProcessCatalogId;
	}

	public void setPrivateProcessCatalogId(String privateProcessCatalogId) {
		this.privateProcessCatalogId = privateProcessCatalogId;
	}

	public String getShareProcessCatalogId() {
		return shareProcessCatalogId;
	}

	public void setShareProcessCatalogId(String shareProcessCatalogId) {
		this.shareProcessCatalogId = shareProcessCatalogId;
	}

	public String getPrivateLogCatalogId() {
		return privateLogCatalogId;
	}

	public void setPrivateLogCatalogId(String privateLogCatalogId) {
		this.privateLogCatalogId = privateLogCatalogId;
	}

	public String getShareLogCatalogId() {
		return shareLogCatalogId;
	}

	public void setShareLogCatalogId(String shareLogCatalogId) {
		this.shareLogCatalogId = shareLogCatalogId;
	}

	public Map<String, Date> getInSharedProcess() {
		return inSharedProcess;
	}

	public void setInSharedProcess(Map<String, Date> inSharedProcess) {
		this.inSharedProcess = inSharedProcess;
	}

}
