package com.chinamobile.bpmspace.core.domain.process;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chinamobile.bpmspace.core.domain.CatalogType;
import com.chinamobile.bpmspace.core.domain.Permission;

@Document(collection = "processCatalog")
public class ProcessCatalog {
	@Id
	private String id;
	@Indexed
	private String name = "";
	// id of parent catalog
	@Indexed
	private String parentId = "";
	// catalog type: PUBLIC/PRIVATE/SHARE
	private CatalogType type = CatalogType.UNKNOWN;
	// whose repository does this catalog belong to
	private String rootUserId = "";
	// id of the owner
	@Indexed
	private String ownerId = "";
	// name of the owner
	private String ownerName = "";
	// time when created
	private Date createTime = new Date();
	// map from user id to permission
	private Map<String, Permission> userPermission = new HashMap<String, Permission>();

	public ProcessCatalog() {

	}

	public ProcessCatalog(String _name, String _parentId, CatalogType _type,
			String _rootUserId, String _ownerId, String _ownerName) {
		name = _name;
		parentId = _parentId;
		type = _type;
		rootUserId = _rootUserId;
		ownerId = _ownerId;
		ownerName = _ownerName;
		userPermission.put(_rootUserId, Permission.RW);
		if (!_ownerId.equals(_rootUserId)) {
			userPermission.put(_ownerId, Permission.RW);
		}
	}

	@Override
	public String toString() {
		return String.format("ProcessCatalog[id=%s, name='%s', parentId=%s, "
				+ "type='%s', ownerId=%s, ownerName=%s]", id, name, parentId,
				type, ownerId, ownerName);
	}

	public Permission getPermission(String _userId) {
		if (ownerId.equals(_userId) || rootUserId.equals(_userId)) {
			return Permission.RW;
		} else if (userPermission.containsKey(_userId)) {
			return userPermission.get(_userId);
		}
		return Permission.NO;
	}

	public static CatalogType convertType(String _type) {
		if (_type.equals("PUBLIC")) {
			return CatalogType.PUBLIC;
		} else if (_type.equals("PRIVATE")) {
			return CatalogType.PRIVATE;
		} else if (_type.equals("SHARE")) {
			return CatalogType.SHARE;
		}
		return CatalogType.UNKNOWN;
	}

	public static String convertType(CatalogType _type) {
		switch (_type) {
		case PUBLIC:
			return "PUBLIC";
		case PRIVATE:
			return "PRIVATE";
		case SHARE:
			return "SHARE";
		default:
			return "UNKNOWN";
		}
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public CatalogType getType() {
		return type;
	}

	public void setType(CatalogType type) {
		this.type = type;
	}

	public String getRootUserId() {
		return rootUserId;
	}

	public void setRootUserId(String rootUserId) {
		this.rootUserId = rootUserId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Map<String, Permission> getUserPermission() {
		return userPermission;
	}

	public void setUserPermission(Map<String, Permission> userPermission) {
		this.userPermission = userPermission;
	}
}
