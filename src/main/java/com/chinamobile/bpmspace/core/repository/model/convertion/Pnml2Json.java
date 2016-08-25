package com.chinamobile.bpmspace.core.repository.model.convertion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.chinamobile.bpmspace.core.repository.model.convertion.pnml.JArc;
import com.chinamobile.bpmspace.core.repository.model.convertion.pnml.JPlace;
import com.chinamobile.bpmspace.core.repository.model.convertion.pnml.JTransition;

public class Pnml2Json {

	
	private ArrayList<JPlace> places = new ArrayList<JPlace>();
	private ArrayList<JTransition> transitions = new ArrayList<JTransition>();
	private ArrayList<JArc> arcs = new ArrayList<JArc>();

	private Hashtable<String, ArrayList<String>> connections = new Hashtable<String, ArrayList<String>>();

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

	public boolean fromPlace(String id) {
		for (int i = 0; i < places.size(); i++) {
			if (id.equals(places.get(i).getId())) {
				return true;
			}
		}
		return false;
	}

	public void getConnections() {
		for (int i = 0; i < arcs.size(); i++) {
			JArc arc = arcs.get(i);
			String aId = arc.getId();
			String fromId = arc.getFromId();
			String toId = arc.getToId();
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
			Element pnml = doc.getDocumentElement();
			if (pnml == null)
				return;

			// all net node
			NodeList nets = pnml.getChildNodes();
			// ֻ��һ��net
			Node net = null;
			for (int i = 0; i < nets.getLength(); i++) {
				net = nets.item(i);
				if (net != null && net.getNodeType() == Node.ELEMENT_NODE) {
					net = nets.item(i);
					break;
				}
			}

			NodeList shapes = net.getChildNodes();
			if (shapes == null)
				return;
			for (int i = 0; i < shapes.getLength(); i++) {
				Node shape = shapes.item(i);
				// ��ͼ�νڵ�
				if (shape != null && shape.getNodeType() == Node.ELEMENT_NODE) {
					if (shape.getNodeName().equals("place")) {
						JPlace place = new JPlace();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						place.setId(id);
						NodeList childs = shape.getChildNodes();
						for (int j = 0; j < childs.getLength(); j++) {
							Node child = childs.item(j);
							if (child != null
									& child.getNodeType() == Node.ELEMENT_NODE) {
								if (child.getNodeName().equals("graphics")) {
									NodeList childchilds = child
											.getChildNodes();
									for (int m = 0; m < childchilds.getLength(); m++) {
										Node childchild = childchilds.item(m);
										// ��ȡλ��
										if (childchild.getNodeName().equals(
												"position")) {
											String sfx = childchild
													.getAttributes()
													.getNamedItem("x")
													.getNodeValue();
											Float fx = Float.parseFloat(sfx);
											int x = fx.intValue();
											place.setX(x);

											String sfy = childchild
													.getAttributes()
													.getNamedItem("y")
													.getNodeValue();
											Float fy = Float.parseFloat(sfy);
											int y = fy.intValue();
											place.setY(y);

										}
										if (childchild.getNodeName().equals(
												"dimension")) {
											// ���ð뾶
											place.setR(30);
										}
									}
								}
								if (child.getNodeName().equals("name")) {
									NodeList childchilds = child
											.getChildNodes();
									for (int m = 0; m < childchilds.getLength(); m++) {
										Node childchild = childchilds.item(m);
										// ��ȡλ��
										if (childchild.getNodeName().equals(
												"text")) {
											String name = childchild
													.getTextContent();
											if (!name.startsWith("oryx"))
												place.setName(name);
										}
									}
								}

							}
						}
						places.add(place);
					}
					if (shape.getNodeName().equals("transition")) {
						JTransition transition = new JTransition();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						transition.setId(id);
						NodeList childs = shape.getChildNodes();
						for (int j = 0; j < childs.getLength(); j++) {
							Node child = childs.item(j);
							if (child != null
									& child.getNodeType() == Node.ELEMENT_NODE) {
								if (child.getNodeName().equals("graphics")) {
									NodeList childchilds = child
											.getChildNodes();
									for (int m = 0; m < childchilds.getLength(); m++) {
										Node childchild = childchilds.item(m);
										// ��ȡλ��
										if (childchild.getNodeName().equals(
												"position")) {
											String sfx = childchild
													.getAttributes()
													.getNamedItem("x")
													.getNodeValue();
											Float fx = Float.parseFloat(sfx);
											int x = fx.intValue();
											transition.setX(x);

											String sfy = childchild
													.getAttributes()
													.getNamedItem("y")
													.getNodeValue();
											Float fy = Float.parseFloat(sfy);
											int y = fy.intValue();
											transition.setY(y);

										}
										if (childchild.getNodeName().equals(
												"dimension")) {
											// ����
											transition.setWidth(40);
											transition.setHeight(40);
										}
									}
								}
								if (child.getNodeName().equals("name")) {
									NodeList childchilds = child
											.getChildNodes();
									for (int m = 0; m < childchilds.getLength(); m++) {
										Node childchild = childchilds.item(m);
										// ��ȡλ��
										if (childchild.getNodeName().equals(
												"text")) {
											String name = childchild
													.getTextContent();
											if (!name.startsWith("oryx"))
												transition.setName(name);
										}
									}
								}

							}
						}
						transitions.add(transition);
					}
					if (shape.getNodeName().equals("arc")) {
						JArc arc = new JArc();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						String fromId = shape.getAttributes()
								.getNamedItem("source").getNodeValue();
						String toId = shape.getAttributes()
								.getNamedItem("target").getNodeValue();
						arc.setId(id);
						arc.setFromId(fromId);
						arc.setToId(toId);

						arcs.add(arc);
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
		StringBuilder sb = new StringBuilder();
		sb = buildHead(sb);
		sb = buildJson(sb);
		sb = buildBottom(sb);
		return sb.toString();
	}

	private StringBuilder buildJson(StringBuilder sb) {
		// TODO Auto-generated method stub
		for (int i = 0; i < places.size(); i++) {
			JPlace place = places.get(i);
			String id = place.getId();
			String name = place.getName();
			int x = place.getX();
			int y = place.getY();
			int r = place.getR();
			System.out.println(r);
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"id\": \"\"," + "\"title\": \""
					+ place.getName() + "\"," + "\"numberoftokens\": \"\","
					+ "\"numberoftokens_text\": \"\","
					+ "\"numberoftokens_drawing\": \"0\","
					+ "\"external\": \"false\"," + "\"exttype\": \"Push\","
					+ "\"href\": \"\"," + "\"locatornames\": \"\","
					+ "\"locatortypes\": \"\"," + "\"locatorexpr\": \"\""
					+ "}," + "\"stencil\": {" + " \"id\": \"Place\"" + " },"
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
					+ " \"x\": " + (x - r) + "," + " \"y\": " + (y+r) + ""
					+ "}," + " \"upperLeft\": {" + " \"x\": " + (x + r) + ","
					+ "\"y\": " + (y-r) + "" + " }" + "},"
					+ " \"dockers\": []" + "},");
		}
		for (int i = 0; i < transitions.size(); i++) {
			JTransition transition = transitions.get(i);
			String id = transition.getId();
			String name = transition.getName();
			int x = transition.getX();
			int y = transition.getY();
			int w = transition.getWidth();
			int h = transition.getHeight();
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"id\": \"\"," + "\"title\": \""
					+ name + "\"," + "\"firetype\": \"Automatic\","
					+ "\"href\": \"\"," + "\"omodel\": \"\","
					+ " \"oform\": \"\"," + "\"guard\": \"\","
					+ "\"communicationchannel\": \"\","
					+ " \"communicationtype\": \"Default\"" + "},"
					+ "\"stencil\": {" + " \"id\": \"Transition\"" + " },"
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
					+ " \"x\": " + (x - w / 2) + "," + " \"y\": " + (y - h / 2)
					+ "" + "}," + " \"upperLeft\": {" + " \"x\": "
					+ (x + w / 2) + "," + "\"y\": " + (y + h / 2) + "" + " }"
					+ "}," + " \"dockers\": []" + "},");
		}
		for (int i = 0; i < arcs.size(); i++) {
			JArc arc = arcs.get(i);
			String id = arc.getId();
			String fromId = arc.getFromId();
			String toId = arc.getToId();

			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"id\": \"\","
					+ "\"label\": \"\"," + "\"transformation\": \"\"" + "},"
					+ "\"stencil\": {" + " \"id\": \"Arc\"" + " },"
					+ "\"childShapes\": []," + " \"outgoing\": [" + "{"
					+ "\"resourceId\": \"" + connections.get(id).get(0) + "\""
					+ " }" + " ]," + " \"bounds\": {" + " \"lowerRight\": {"
					+ " \"x\": " + 0 + "," + " \"y\": " + 0 + "" + "},"
					+ " \"upperLeft\": {" + " \"x\": " + 0 + "," + "\"y\": "
					+ 0 + "" + " }" + "}," + " \"dockers\": [");
			if (fromPlace(id)) {
				sb.append(

				" {" + "\"x\": 15," + " \"y\": 15" + "}," + "{" + " \"x\": 20,"
						+ "\"y\": 20" + "}");
			} else {
				sb.append(

				" {" + "\"x\": 20," + " \"y\": 20" + "}," + "{" + " \"x\": 15,"
						+ "\"y\": 15" + "}");
			}

			sb.append(" ]," + " \"target\": {" + "\"resourceId\": \""
					+ connections.get(id).get(0) + "\"" + "}" + " },"

			);
		}
		sb = removeLastComma(sb);
		return sb;
	}

	public StringBuilder removeLastComma(StringBuilder sb) {
		sb.deleteCharAt(sb.length() - 1);
		return sb;
	}

	private StringBuilder buildHead(StringBuilder sb) {
		// TODO Auto-generated method stub
		sb.append("{" + "\"resourceId\": \"oryx-canvas123\","
				+ "\"properties\": {" + "\"title\": \"\","
				+ "\"engine\": \"false\"," + "\"version\":\"\","
				+ "\"author\": \"\"," + "\"language\": \"English\","
				+ "\"creationdate\": \"\"," + "\"modificationdate\": \"\","
				+ "\"documentation\": \"\"" + " }," + "\"stencil\": {"
				+ "\"id\": \"Diagram\"" + "}," + "\"childShapes\": ["

		);
		return sb;
	}

	private StringBuilder buildBottom(StringBuilder sb) {
		// TODO Auto-generated method stub
		sb.append("]," + "\"bounds\": {" + "\"lowerRight\": {"
				+ " \"x\": 1485," + "\"y\": 1050" + "}," + "\"upperLeft\": {"
				+ "     \"x\": 0," + "       \"y\": 0" + "      }" + "    },"
				+ " \"stencilset\": {"
				+ "  \"url\": \"/oryx///stencilsets/petrinets/petrinet.json\","
				+ "\"namespace\": \"http://b3mn.org/stencilset/petrinet#\""
				+ "}," + "   \"ssextensions\": []" + "}");
		return sb;
	}

	public static void main(String[] args) {
		String path = "1_53bd6928239dbc396ce11571.pnml";
		Pnml2Json pj = new Pnml2Json();
		pj.getShapInfo(path);
		pj.getConnections();
		System.out.println(pj.createJson());

	}
}
