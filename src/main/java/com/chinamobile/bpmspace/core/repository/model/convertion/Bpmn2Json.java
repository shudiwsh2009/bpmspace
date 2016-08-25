package com.chinamobile.bpmspace.core.repository.model.convertion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.chinamobile.bpmspace.core.repository.model.convertion.bpmn.Line;
import com.chinamobile.bpmspace.core.repository.model.convertion.bpmn.OriginalType;
import com.chinamobile.bpmspace.core.repository.model.convertion.bpmn.Shape;
import com.chinamobile.bpmspace.core.repository.model.convertion.bpmn.ShapeType;

public class Bpmn2Json {

	// 判断是否里面已有布局
	boolean haslayout = false;

	// 瀛樺偍json淇℃伅
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private ArrayList<Line> lines = new ArrayList<Line>();
	// 瀛樺偍妯″瀷鍜宨d瀵瑰簲鍏崇郴锛岀敤浜庤绠楄繛鎺?
	private HashMap<String, Shape> idtopostions = new HashMap<String, Shape>();
	// 瀛樺偍杩炴帴鍏崇郴

	private HashSet<String> allIDs = new HashSet<String>();
	private Hashtable<String, ArrayList<String>> connections = new Hashtable<String, ArrayList<String>>();

	private HashSet<String> alreadyLayoutIDs = new HashSet<String>();
	
	public FileInputStream getFile(String path) {
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("");
			e.printStackTrace();
		}
		return fi;
	}

	public void getConnections() {
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			String aId = line.getId();
			String fromId = line.getFromId();
			String toId = line.getToId();
			if (connections.containsKey(fromId)) {
				connections.get(fromId).add(aId);
			} else {
				ArrayList<String> a = new ArrayList<String>();
				a.add(aId);
				connections.put(fromId, a);
			}
			if (connections.containsKey(aId)) {
				connections.get(aId).add(toId);
			} else {
				ArrayList<String> a = new ArrayList<String>();
				a.add(toId);
				connections.put(aId, a);
			}
		}
	}

	public void getShapInfo(String path) {
		FileInputStream in = getFile(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(in);
			// root <university>
			Element definitions = doc.getDocumentElement();
			if (definitions == null)
				return;
			// all net node
			NodeList subroots = definitions.getChildNodes();
			// 涓嶅彧鏈変竴涓猵rocess
			for (int i = 0; i < subroots.getLength(); i++) {
				Node subroot = subroots.item(i);
				if (subroot != null
						&& subroot.getNodeType() == Node.ELEMENT_NODE) {
					if (subroot.getNodeName().equals("process")) {
						NodeList nodeshapes = subroot.getChildNodes();
						for (int j = 0; j < nodeshapes.getLength(); j++) {
							Node nodeshape = nodeshapes.item(j);
							// System.out.println(nodeshape.getNodeName());
							if (nodeshape.getNodeName().equals(
									ShapeType.userTask.toString())) {
								Shape shape = new Shape();
								String id = nodeshape.getAttributes()
										.getNamedItem("id").getNodeValue();
								String name = nodeshape.getAttributes()
										.getNamedItem("name").getNodeValue();
								shape.setShapeType(ShapeType.userTask
										.toString());
								shape.setOriginalType(OriginalType.rec);
								shape.setId(id);
								shape.setName(name);

								shapes.add(shape);
								allIDs.add(id);
								idToShapePosition(shape);
							}
							if (nodeshape.getNodeName().equals(
									ShapeType.complexGateway.toString())) {
								Shape shape = new Shape();
								String id = nodeshape.getAttributes()
										.getNamedItem("id").getNodeValue();
								String name = nodeshape.getAttributes()
										.getNamedItem("name").getNodeValue();
								shape.setId(id);
								shape.setName(name);
								shape.setShapeType(ShapeType.complexGateway
										.toString());
								shape.setOriginalType(OriginalType.ccir);

								shapes.add(shape);
								allIDs.add(id);
								idToShapePosition(shape);
							}
							if (nodeshape.getNodeName().equals(
									ShapeType.startEvent.toString())) {
								Shape shape = new Shape();
								String id = nodeshape.getAttributes()
										.getNamedItem("id").getNodeValue();
								String name = nodeshape.getAttributes()
										.getNamedItem("name").getNodeValue();
								shape.setId(id);
								shape.setName(name);
								shape.setShapeType(ShapeType.startEvent
										.toString());
								shape.setOriginalType(OriginalType.ecir);

								shapes.add(shape);
								allIDs.add(id);
								idToShapePosition(shape);
							}
							if (nodeshape.getNodeName().equals(
									ShapeType.endEvent.toString())) {
								Shape shape = new Shape();
								String id = nodeshape.getAttributes()
										.getNamedItem("id").getNodeValue();
								String name = nodeshape.getAttributes()
										.getNamedItem("name").getNodeValue();
								shape.setId(id);
								shape.setName(name);
								shape.setShapeType(ShapeType.endEvent
										.toString());
								shape.setOriginalType(OriginalType.ecir);

								shapes.add(shape);
								allIDs.add(id);
								idToShapePosition(shape);
							}

							if (nodeshape.getNodeName().equals(
									ShapeType.sequenceFlow.toString())) {
								Line line = new Line();
								String id = nodeshape.getAttributes()
										.getNamedItem("id").getNodeValue();
								String fromId = nodeshape.getAttributes()
										.getNamedItem("sourceRef")
										.getNodeValue();
								String toId = nodeshape.getAttributes()
										.getNamedItem("targetRef")
										.getNodeValue();
								line.setId(id);
								line.setFromId(fromId);
								line.setToId(toId);
								lines.add(line);
								allIDs.add(id);
							}

						}
					}
					if (subroot.getNodeName().equals("<bpmndi:processDiagram")) {

						// 根据id取出shape 设置shape的width，height，x，y
						haslayout = true;
						NodeList laneCompartments = subroot.getChildNodes();
						for (int j = 0; j < laneCompartments.getLength(); j++) {
							Node laneCompartment = laneCompartments.item(j);
							// System.out.println(nodeshape.getNodeName());
							if (laneCompartment == null)
								continue;
							if (laneCompartment.getNodeName().equals(
									"bpmndi:laneCompartment")) {
								NodeList nodeshapes = laneCompartment
										.getChildNodes();
								for (int m = 0; m < nodeshapes.getLength(); m++) {
									Node shape = nodeshapes.item(m);
									if (shape == null)
										continue;
									String id = shape.getAttributes()
											.getNamedItem("id").getNodeValue();
									String swidth = shape.getAttributes()
											.getNamedItem("width")
											.getNodeValue();
									String sheight = shape.getAttributes()
											.getNamedItem("height")
											.getNodeValue();
									String sx = shape.getAttributes()
											.getNamedItem("x").getNodeValue();
									String sy = shape.getAttributes()
											.getNamedItem("y").getNodeValue();
									Float fwidth = Float.valueOf(swidth);
									Float fheight = Float.valueOf(sheight);
									Float fx = Float.valueOf(sx);
									Float fy = Float.valueOf(sy);
									int width = fwidth.intValue();
									int height = fheight.intValue();
									int x = fx.intValue();
									int y = fy.intValue();
									Shape temp = idtopostions.get(id);
									temp.setWidth(width);
									temp.setHeight(height);
									temp.setX(x);
									temp.setY(y);
								}
							}

						}
					}
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	public String createJson() {
		autoLayout();
		StringBuilder sb = new StringBuilder();
		sb = buildHead(sb);
		sb = buildJson(sb);
		sb = buildBottom(sb);
		return sb.toString();
	}

	private void autoLayout() {
		if (haslayout) {
			return;
		}
		// 找到所有开始的节点,方法是找到所有入度为0的点
		HashSet<String> allstarts = ConvertionUtil.findStarts(allIDs,
				connections);
		int [] countD = new int[500];
		
		for (String startId : allstarts) {
			
			ArrayDeque<String> queue = new ArrayDeque<String>();
			queue.push(startId);
			
			
			while (queue.isEmpty() == false) {

				String id = queue.poll();
				//保证子图的位置相同
				ArrayList<String> fchilds = connections.get(id);
				if(fchilds==null)
					continue;
				if(idtopostions.get(id).getLayer()<0){
					//没有被设置过层级
					idtopostions.get(id).setLayer(0);
				}
				
				setShape(id, idtopostions.get(id).getLayer(), countD[idtopostions.get(id).getLayer()]);
				
				for (String fid : fchilds) {

					if (connections.get(fid) == null)
						continue;
					// 得到子图形的id
					
					String sid = connections.get(fid).get(0);
					queue.push(sid);
					if(idtopostions.get(sid).getLayer()<0){
						//没有被设置过层级
						idtopostions.get(sid).setLayer(idtopostions.get(id).getLayer()+1);
					}
					setShape(sid, idtopostions.get(sid).getLayer(), countD[idtopostions.get(sid).getLayer()]=countD[idtopostions.get(sid).getLayer()]+1);

				}
			}
		}

	}

	private void setShape(String id, int countC, int countD) {
		
		if(alreadyLayoutIDs.contains(id)){
			return;
		}
		alreadyLayoutIDs.add(id);
		int width = 0, height = 0;
		if (idtopostions.get(id).getOriginalType().equals(OriginalType.rec)) {
			width = 100;
			height = 80;
		} else if (idtopostions.get(id).getOriginalType()
				.equals(OriginalType.ccir)) {
			width = 40;
			height = 40;
		} else if (idtopostions.get(id).getOriginalType()
				.equals(OriginalType.ecir)) {
			width = 30;
			height = 30;
		}

		//取消自动换行
		//int[] positionX = { 300, 500, 700, 900, 1100, 1100, 900, 700, 500, 300 };
		int initX = 100;
		int initY = 80;
		
		int spaceX = 150;
		int spaceY = 100;
		/*for(int i = 0 ;i<shapes.size();i++){
			if(shapes.get(i).getId().equals(id)){
				Shape shape = shapes.get(i);
				shape.setWidth(width);
				shape.setHeight(height);
				shape.setX(initX + spaceX*countC);
				System.out.println(id);
				System.out.println(initX + spaceX*countC);
				shape.setY(initY + spaceY*countD);
			}
		}*/
		
		idtopostions.get(id).setWidth(width);
		idtopostions.get(id).setHeight(height);
	
		idtopostions.get(id).setX(initX + spaceX*(countC) - width/2);
		idtopostions.get(id).setY(initY + spaceY*(countD) - height/2);
	}

	private StringBuilder buildJson(StringBuilder sb) {
		// TODO Auto-generated method stub
		for (int i = 0; i < shapes.size(); i++) {
			Shape shape = shapes.get(i);
			String id = shape.getId();
			String name = shape.getName();
			int x = shape.getX();
			int y = shape.getY();
			int width = shape.getWidth();
			int height = shape.getHeight();

			if (shape.getShapeType().equals(ShapeType.startEvent.toString())) {
				sb.append("{" + "\"resourceId\": \"" + id + "\","
						+ "\"properties\": {" + "\"name\": \"" + name + "\","
						+ "\"documentation\": \"\"," + "\"auditing\": \"\","
						+ "\"monitoring\": \"\","
						+ "\"eventdefinitionref\": \"\","
						+ "\"eventdefinitions\": \"\","
						+ "\"dataoutputassociations\": \"\","
						+ "\"dataoutput\": \"\"," + "\"outputset\": \"\","
						+ "\"trigger\": \"None\"," + "\"bgcolor\": \"#ffffff\""
						+ "}," + "\"stencil\": {"
						+ " \"id\": \"StartNoneEvent\"" + " },"
						+ "\"childShapes\": []," + " \"outgoing\": [");
				if (connections.get(id) != null) {
					for (int j = 0; j < connections.get(id).size(); j++) {
						sb.append("{" + "\"resourceId\": \""
								+ connections.get(id).get(j) + "\"" + " },");
					}
					sb = removeLastComma(sb);
				} else {
					sb.append("{" + "\"resourceId\": \"\"" + " }");
				}
				sb.append(" ]," + " \"bounds\": {" + " \"lowerRight\": {"
						+ " \"x\": " + (x + width) + "," + " \"y\": "
						+ (y + height) + "" + "}," + " \"upperLeft\": {"
						+ " \"x\": " + (x) + "," + "\"y\": " + (y) + "" + " }"
						+ "}," + " \"dockers\": []" + "},");
			}
			if (shape.getShapeType().equals(ShapeType.userTask.toString())) {
				sb.append("{" + "\"resourceId\": \"" + id + "\","
						+ "\"properties\": {" + "\"name\": \"" + name + "\","
						+ "\"documentation\": \"\"," + "\"auditing\": \"\","
						+ "\"monitoring\": \"\"," + "\"categories\": \"\","
						+ "\"startquantity\": \"1\","
						+ "\"completionquantity\": \"1\","
						+ "\"isforcompensation\": \"\","
						+ "\"assignments\": \"\"," + "\"callacitivity\": \"\","
						+ "\"tasktype\": \"User\","
						+ "\"implementation\": \"webService\","
						+ "\"resources\": \"\"," + "\"messageref\": \"\","
						+ "\"operationref\": \"\"," + "\"instantiate\": \"\","
						+ "\"script\": \"\"," + "\"script_language\": \"\","
						+ "\"looptype\": \"None\"," + "\"testbefore\": \"\","
						+ "\"loopcondition\": \"\"," + "\"loopmaximum\": \"\","
						+ "\"loopcardinality\": \"\","
						+ "\"loopdatainput\": \"\","
						+ "\"loopdataoutput\": \"\","
						+ "\"inputdataitem\": \"\","
						+ "\"outputdataitem\": \"\","
						+ "\"complexbehaviordefinition\": \"\","
						+ "\"behavior\": \"all\","
						+ "\"completioncondition\": \"\","
						+ "\"onebehavioreventref\": \"signal\","
						+ "\"nonebehavioreventref\": \"signal\","
						+ "\"properties\": \"\"," + "\"datainputset\": \"\","
						+ "\"dataoutputset\": \"\","
						+ "\"bgcolor\": \"#ffffcc\"" + "}," + "\"stencil\": {"
						+ " \"id\": \"Task\"" + " }," + "\"childShapes\": [],"
						+ " \"outgoing\": [");
				if (connections.get(id) != null) {
					for (int j = 0; j < connections.get(id).size(); j++) {
						sb.append("{" + "\"resourceId\": \""
								+ connections.get(id).get(j) + "\"" + " },");
					}
					sb = removeLastComma(sb);
				} else {
					sb.append("{" + "\"resourceId\": \"\"" + " }");
				}
				sb.append(" ]," + " \"bounds\": {" + " \"lowerRight\": {"
						+ " \"x\": " + (x + width) + "," + " \"y\": "
						+ (y + height) + "" + "}," + " \"upperLeft\": {"
						+ " \"x\": " + (x) + "," + "\"y\": " + (y) + "" + " }"
						+ "}," + " \"dockers\": []" + "},");
			}

			if (shape.getShapeType().equals(ShapeType.endEvent.toString())) {
				sb.append("{" + "\"resourceId\": \"" + id + "\","
						+ "\"properties\": {" + "\"name\": \"" + name + "\","
						+ "\"documentation\": \"\"," + "\"auditing\": \"\","
						+ "\"monitoring\": \"\","
						+ "\"eventdefinitionref\": \"\","
						+ "\"eventdefinitions\": \"\","
						+ "\"datainputassociations\": \"\","
						+ "\"dataoutput\": \"\"," + "\"datainput\": \"\","
						+ "\"trigger\": \"None\"," + "\"inputset\": \"\","
						+ "\"bgcolor\": \"#ffffff\"" + "}," + "\"stencil\": {"
						+ " \"id\": \"EndNoneEvent\"" + " },"
						+ "\"childShapes\": []," + " \"outgoing\": [");
				if (connections.get(id) != null) {
					for (int j = 0; j < connections.get(id).size(); j++) {
						sb.append("{" + "\"resourceId\": \""
								+ connections.get(id).get(j) + "\"" + " },");
					}
					sb = removeLastComma(sb);
				} else {
					sb.append("{" + "\"resourceId\": \"\"" + " }");
				}
				sb.append(" ]," + " \"bounds\": {" + " \"lowerRight\": {"
						+ " \"x\": " + (x + width) + "," + " \"y\": "
						+ (y + height) + "" + "}," + " \"upperLeft\": {"
						+ " \"x\": " + (x) + "," + "\"y\": " + (y) + "" + " }"
						+ "}," + " \"dockers\": []" + "},");
			}

			if (shape.getShapeType()
					.equals(ShapeType.complexGateway.toString())) {
				sb.append("{" + "\"resourceId\": \"" + id + "\","
						+ "\"properties\": {" + "\"name\": \"" + name + "\","
						+ "\"documentation\": \"\"," + "\"auditing\": \"\","
						+ "\"monitoring\": \"\"," + "\"categories\": \"\","
						+ "\"assignments\": \"\"," + "\"pool\": \"\","
						+ "\"lanes\": \"\"," + "\"gates\": \"\","
						+ "\"gates_outgoingsequenceflow\": \"\","
						+ "\"gates_assignments\": \"\","
						+ "\"gatewaytype\": \"Complex\","
						+ "\"activationcondition\": \"\","
						+ "\"incomingcondition\": \"\","
						+ "\"outgoingcondition\": \"\","
						+ "\"bgcolor\": \"#ffffff\"" + "}," + "\"stencil\": {"
						+ " \"id\": \"ComplexGateway\"" + " },"
						+ "\"childShapes\": []," + " \"outgoing\": [");
				if (connections.get(id) != null) {
					for (int j = 0; j < connections.get(id).size(); j++) {
						sb.append("{" + "\"resourceId\": \""
								+ connections.get(id).get(j) + "\"" + " },");
					}
					sb = removeLastComma(sb);
				} else {
					sb.append("{" + "\"resourceId\": \"\"" + " }");
				}
				sb.append(" ]," + " \"bounds\": {" + " \"lowerRight\": {"
						+ " \"x\": " + (x + width) + "," + " \"y\": "
						+ (y + height) + "" + "}," + " \"upperLeft\": {"
						+ " \"x\": " + (x) + "," + "\"y\": " + (y) + "" + " }"
						+ "}," + " \"dockers\": []" + "},");
			}

		}

		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			String id = line.getId();
			String fromId = line.getFromId();
			String toId = line.getToId();
			Shape toshape = idtopostions.get(toId);
			Shape fromshape = idtopostions.get(fromId);

			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"name\": \"\","
					+ "\"documentation\": \"\"," + "\"auditing\": \"\","
					+ "\"monitoring\": \"\"," + "\"conditiontype\": \"None\","
					+ "\"conditionexpression\": \"\","
					+ "\"isimmediate\": \"\"," + "\"showdiamondmarker\": \"\""
					+ "}," + "\"stencil\": {" + " \"id\": \"SequenceFlow\""
					+ " }," + "\"childShapes\": []," + " \"outgoing\": [" + "{"
					+ "\"resourceId\": \"" + connections.get(id).get(0) + "\""
					+ " }" + " ]," + " \"bounds\": {" + " \"lowerRight\": {"
					+ " \"x\": " + (toshape.getX() + toshape.getWidth() / 2)
					+ "," + " \"y\": " + (toshape.getY()) + "" + "},"
					+ " \"upperLeft\": {" + " \"x\": "
					+ (fromshape.getX() + fromshape.getWidth() / 2) + ","
					+ "\"y\": " + (fromshape.getY() + fromshape.getHeight())
					+ "" + " }" + "}," + " \"dockers\": [");

			sb.append(

			" {" + "\"x\": " + fromshape.getWidth() / 2 + "," + " \"y\":"
					+ fromshape.getHeight() / 2 + "" + "}," + "{" + " \"x\": "
					+ toshape.getWidth() / 2 + "," + "\"y\": "
					+ toshape.getHeight() / 2 + "" + "}");

			sb.append(" ],");
			sb.append(" \"target\": {" + "\"resourceId\": \""
					+ connections.get(id).get(0) + "\"" + "}" + " },"

			);
		}
		sb = removeLastComma(sb);
		return sb;
	}

	private void idToShapePosition(Shape shape) {
		// TODO Auto-generated method stub
		idtopostions.put(shape.getId(), shape);
	}

	public StringBuilder removeLastComma(StringBuilder sb) {
		sb.deleteCharAt(sb.length() - 1);
		return sb;
	}

	private StringBuilder buildHead(StringBuilder sb) {
		// TODO Auto-generated method stub

		sb.append("{" + "\"resourceId\": \"oryx-canvas123\","
				+ "\"properties\": {" + "\"name\": \"\","
				+ "\"documentation\": \"\"," + "\"auditing\":\"\","
				+ "\"monitoring\":\"\"," + "\"version\":\"\","
				+ "\"author\":\"\"," + "\"language\":\"English\","
				+ "\"namespaces\":\"\","
				+ "\"targetnamespace\":\"http://www.omg.org/bpmn20\","
				+ "\"expressionlanguage\":\"http://www.w3.org/1999/XPath\","
				+ "\"typelanguage\":\"http://www.w3.org/2001/XMLSchema\","
				+ "\"creationdate\":\"\"," + "\"modificationdate\":\"\""
				+ " }," + "\"stencil\": {" + "\"id\": \"BPMNDiagram\"" + "},"
				+ "\"childShapes\": ["

		);
		return sb;
	}

	private StringBuilder buildBottom(StringBuilder sb) {
		// TODO Auto-generated method stub
		sb.append("]," + "\"bounds\": {" + "\"lowerRight\": {"
				+ " \"x\": 1485," + "\"y\": 1050" + "}," + "\"upperLeft\": {"
				+ "     \"x\": 0," + "       \"y\": 0" + "      }" + "    },"
				+ " \"stencilset\": {"
				+ "  \"url\": \"/oryx///stencilsets/bpmn2.0/bpmn2.0.json\","
				+ "\"namespace\": \"http://b3mn.org/stencilset/bpmn2.0#\""
				+ "}," + "   \"ssextensions\": []" + "}");
		return sb;
	}

	public static void main(String[] args) {
		String path = "lvcheng14092076043546637088945046537702.xml";
		Bpmn2Json bj = new Bpmn2Json();
		bj.getShapInfo(path);
		bj.getConnections();
		System.out.println(bj.createJson());
	}
}
