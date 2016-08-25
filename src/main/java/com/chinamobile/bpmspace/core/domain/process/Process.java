package com.chinamobile.bpmspace.core.domain.process;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.chinamobile.bpmspace.core.domain.Permission;

@Document(collection = "process")
public class Process implements Cloneable {
	@Id
	private String id;
	@Indexed
	private String name = "";
	private String description = "";
	// id of catalog which contains this process
	@Indexed
	private String catalogId = "";
	private ProcessType type = ProcessType.UNKNOWN;
	private String rootUserId = "";
	@Indexed
	private String ownerId = "";
	private String ownerName = "";
	private Date createTime = new Date();
	// revision of this process
	private Map<Long, ProcessRevision> revision = new HashMap<Long, ProcessRevision>();
	// map of permission, contains element iff this process is shared
	private Map<String, Permission> userPermission = new HashMap<String, Permission>();

	public Process() {

	}

	public Process(String _name, String _description, String _catalogId,
			ProcessType _type, String _rootUserId, String _ownerId,
			String _ownerName) {
		name = _name;
		description = _description;
		catalogId = _catalogId;
		type = _type;
		rootUserId = _rootUserId;
		ownerId = _ownerId;
		ownerName = _ownerName;
		userPermission.put(_rootUserId, Permission.RW);
		if (!_ownerId.equals(_rootUserId)) {
			userPermission.put(_ownerId, Permission.RW);
		}
	}

	public Permission getPermission(String _userId) {
		if (ownerId.equals(_userId) || rootUserId.equals(_userId)) {
			return Permission.RW;
		} else if (userPermission.containsKey(_userId)) {
			return userPermission.get(_userId);
		}
		return Permission.NO;
	}

	public static ProcessType convertType(String _type) {
		if (_type.equals("BPMN")) {
			return ProcessType.BPMN;
		} else if (_type.equals("PETRINET")) {
			return ProcessType.PETRINET;
		} else if (_type.equals("EPC")) {
			return ProcessType.EPC;
		}
		return ProcessType.UNKNOWN;
	}

	public static String convertType(ProcessType _type) {
		switch (_type) {
		case BPMN:
			return "BPMN";
		case PETRINET:
			return "PETRINET";
		case EPC:
			return "EPC";
		default:
			return "UNKNOWN";
		}
	}

	@Override
	public Process clone() {
		Process process = null;
		try {
			process = (Process) super.clone();
			process.setId(null);
			process.setType(type);
			process.setCreateTime((Date) createTime.clone());
			process.setRevision(new HashMap<Long, ProcessRevision>());
			Iterator<Map.Entry<Long, ProcessRevision>> it = revision.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<Long, ProcessRevision> entry = it.next();
				process.getRevision().put(
						entry.getKey(),
						new ProcessRevision(entry.getValue().getModelId(),
								entry.getValue().getCreatorId(), (Date) entry
										.getValue().getCreateTime().clone()));
			}
			process.setUserPermission(new HashMap<String, Permission>());
			Iterator<Map.Entry<String, Permission>> it2 = userPermission
					.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry<String, Permission> entry = it2.next();
				process.getUserPermission().put(entry.getKey(),
						entry.getValue());
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return process;
	}

	@Override
	public String toString() {
		return String.format("Process[id=%s, name='%s', catalogId=%s, "
				+ "type='%s', ownerId=%s, ownerName=%s]", id, name, catalogId,
				type, ownerId, ownerName);
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public ProcessType getType() {
		return type;
	}

	public void setType(ProcessType type) {
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

	public Map<Long, ProcessRevision> getRevision() {
		return revision;
	}

	public void setRevision(Map<Long, ProcessRevision> revision) {
		this.revision = revision;
	}

	public Map<String, Permission> getUserPermission() {
		return userPermission;
	}

	public void setUserPermission(Map<String, Permission> userPermission) {
		this.userPermission = userPermission;
	}
}
