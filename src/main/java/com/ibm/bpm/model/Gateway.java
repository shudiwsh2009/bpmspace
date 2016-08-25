package com.ibm.bpm.model;

public class Gateway extends ProcessNode {

	public Gateway() {
	}

	public Gateway(String name, String id, String gatewayDirection,
			GatewayType t) {
		this.name = name;
		this.id = id;
		this.gatewayDirection = gatewayDirection;
		this.type = t;
	}

	// String name; //gateway name
	// String id; //gateway id
	String gatewayDirection; // gatewayDirection
	GatewayType type;

	// public String getName() {
	// return name;
	// }
	//
	// public void setName(String name) {
	// this.name = name;
	// }
	//
	// public String getId() {
	// return id;
	// }
	//
	// public void setId(String id) {
	// this.id = id;
	// }

	public String getGatewayDirection() {
		return gatewayDirection;
	}

	public void setGatewayDirection(String gatewayDirection) {
		this.gatewayDirection = gatewayDirection;
	}

	public GatewayType getType() {
		return type;
	}

	public void setType(GatewayType type) {
		this.type = type;
	}

	public enum GatewayType {
		inclusiveGateway, exclusiveGateway
	}
}
