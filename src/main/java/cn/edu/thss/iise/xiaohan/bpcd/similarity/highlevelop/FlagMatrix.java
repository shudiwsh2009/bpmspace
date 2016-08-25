package cn.edu.thss.iise.xiaohan.bpcd.similarity.highlevelop;

import java.util.ArrayList;

public class FlagMatrix {

	/**
	 * @author ËÎ½ð·ï
	 */
	ArrayList<String> nodeNames = new ArrayList<String>();
	String[][] flags;

	public ArrayList<String> getNodeNames() {
		return nodeNames;
	}

	public void setNodeNames(ArrayList<String> nodeNames) {
		this.nodeNames = nodeNames;
	}

	public String[][] getFlags() {
		return flags;
	}

	public void setFlags(String[][] flags) {
		this.flags = flags;
	}

}
