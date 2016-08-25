package cn.edu.thss.iise.bpmdemo.analysis.core.fragment;

public class DotNode {
	String label;
	int type;
	String name;
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static int Function = 1;
	public static int Connector = 2;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setIdFromName() {
		int ePos = name.indexOf("e");
		String integerString = name.substring(ePos + 1, name.length());
		this.setId(Integer.valueOf(integerString));
	}

	public void setName(String name) {
		this.name = name;
		setIdFromName();
	}

	public static int getFunction() {
		return Function;
	}

	public static void setFunction(int function) {
		Function = function;
	}

	public static int getConnector() {
		return Connector;
	}

	public static void setConnector(int connector) {
		Connector = connector;
	}

}
