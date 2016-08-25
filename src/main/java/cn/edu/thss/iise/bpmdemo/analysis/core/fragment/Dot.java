package cn.edu.thss.iise.bpmdemo.analysis.core.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Dot {
	private List<DotNode> nodeList;

	public List<DotNode> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<DotNode> nodeList) {
		this.nodeList = nodeList;
	}

	public List<DotArc> getArcList() {
		return arcList;
	}

	public void setArcList(List<DotArc> arcList) {
		this.arcList = arcList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private List<DotArc> arcList;
	private String name;
	private int id;

	public Dot() {
		nodeList = new ArrayList<DotNode>();
		arcList = new ArrayList<DotArc>();
	}

	private ArrayList<DotNode> parseNode(String data) {
		ArrayList<DotNode> result = new ArrayList<DotNode>();
		int length = data.length();
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (int i = 0; i < length; i++)
			if (data.charAt(i) == '[') {
				positions.add(i);
			}
		for (int i = 0; i < positions.size(); i++) {
			int pos = positions.get(i);
			int nextPos;
			if (i + 1 < positions.size())
				nextPos = positions.get(i + 1);
			else
				nextPos = data.length();
			String singleNodeData = adjustString(data, pos, nextPos); // conduct
																		// single
																		// Node
			DotNode dotNode = getInfo(singleNodeData);
			result.add(dotNode);
		}
		return result;
	}

	private DotNode getInfo(String singleNodeData) {
		DotNode result = new DotNode();
		String name;
		String label;
		int type;
		// 1.name
		int pos1 = singleNodeData.indexOf(" ");
		name = singleNodeData.substring(0, pos1);
		// 2.type
		int typePos1 = singleNodeData.indexOf("shape");
		int typePos = singleNodeData.indexOf("=", typePos1);
		if (singleNodeData.charAt(typePos + 1) == 'c') {
			type = DotNode.Connector;
		} else {
			type = DotNode.Function;
		}
		result.setType(type);
		// 3.label
		int labelPos1 = singleNodeData.indexOf("label");
		int labelPos = singleNodeData.indexOf("=", labelPos1);
		labelPos += 2;
		StringBuffer sb = new StringBuffer();
		while (singleNodeData.charAt(labelPos) != '\"') {
			sb.append(singleNodeData.charAt(labelPos));
			labelPos++;
		}
		label = sb.toString();
		result.setLabel(label);
		result.setName(name);

		return result;

	}

	private String adjustString(String data, int pos, int nextPos) {
		int beginPos, endPos;
		beginPos = pos;
		endPos = nextPos;
		while (beginPos >= 0) {
			if (data.charAt(beginPos) == 'n'
					&& data.substring(beginPos).startsWith("node"))
				break;
			beginPos--;
		}
		while (endPos < data.length()) {
			if (data.charAt(endPos) == 'n'
					&& data.substring(endPos).startsWith("node"))
				break;
			endPos++;
		}
		String result = data.substring(beginPos, endPos);
		return result;
	}

	private ArrayList<DotArc> parseArcs(String data) {
		int length = data.length();
		ArrayList<DotArc> result = new ArrayList<DotArc>();
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (int i = 0; i < length - 1; i++) {
			if (data.charAt(i) == '-' && data.charAt(i + 1) == '>')
				positions.add(i);
		}
		for (Integer position : positions) {
			DotArc dotArc = parseArc(position, data);
			result.add(dotArc);
		}
		return result;
	}

	private DotArc parseArc(int position, String data) {
		DotArc result = new DotArc();
		int pos1begin = position, pos1end;
		int pos2begin = position, pos2end;
		String label1;
		String label2;
		while (!data.substring(pos1begin).startsWith("node"))
			pos1begin--;
		while (!data.substring(pos2begin).startsWith("node"))
			pos2begin++;
		pos1end = data.indexOf(" ", pos1begin);
		pos2end = data.indexOf(" ", pos2begin);
		if (pos2end == -1)
			pos2end = data.indexOf("}", pos2begin);
		label1 = data.substring(pos1begin, pos1end);
		label2 = data.substring(pos2begin, pos2end);
		if (label2.contains("\t")) {
			int pos = label2.indexOf("\t");
			label2 = label2.substring(0, pos);
		}
		result.fromName = label1;
		result.toName = label2;

		return result;
	}

	public void parseDotFromString(String rawData) {
		String[] dataList = rawData.split("]");
		for (String data : dataList) {
			if (data.contains("node") && data.contains("[")) {
				ArrayList<DotNode> dotNode = parseNode(data);
				nodeList.addAll(dotNode);
			}
			if (data.contains("->")) {
				ArrayList<DotArc> dotArc = parseArcs(data);
				arcList.addAll(dotArc);
			}
		}
	}

	public void parseDotFromFilePath(String filePath) throws IOException {

		// 1.name
		File file = new File(filePath);
		int dotPos = file.getName().lastIndexOf(".");
		this.name = file.getName().substring(0, dotPos);
		// 2.id
		int underlinePos = file.getName().indexOf("_");
		String idString = file.getName().substring(underlinePos + 1);
		this.id = Integer.parseInt(idString);
		// 3.content
		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis, "GBK");
		BufferedReader br = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String line = br.readLine();
		while (line != null) {
			sb.append(line);
			line = br.readLine();
		}
		parseDotFromString(sb.toString());
	}

	public List<DotArc> getInEdges(DotNode node) {
		List<DotArc> result = new ArrayList<DotArc>();
		for (DotArc arc : getArcList()) {
			if (arc.getToName().equals(node.getName()))
				result.add(arc);
		}
		return result;
	}

	public List<DotArc> getOutEdges(DotNode node) {
		List<DotArc> result = new ArrayList<DotArc>();
		for (DotArc arc : getArcList()) {
			if (arc.getFromName().contains(node.getName()))
				result.add(arc);
		}
		return result;
	}

	public static void main(String args[]) throws IOException {
		String fileName = "G:\\Graduate\\Projects\\中国移动\\work\\bpcd\\fragments\\fragment_26.dot";
		Dot dot = new Dot();
		dot.parseDotFromFilePath(fileName);
	}

	public DotNode getNode(String srcName) {
		DotNode result = null;
		for (DotNode node : getNodeList()) {
			if (node.getName().contains(srcName))
				result = node;
		}
		return result;
	}

}
