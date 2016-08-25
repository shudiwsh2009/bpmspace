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

import com.chinamobile.bpmspace.core.repository.model.convertion.bpmn.OriginalType;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JAnd;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JCF;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JData;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JEvent;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JFunction;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JITSystem;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JOr;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JOrganization;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JPI;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JPosition;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JRelation;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JTextNode;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.JXor;
import com.chinamobile.bpmspace.core.repository.model.convertion.epc.Shape;

public class Epc2Json {

	boolean haslayout = false;
	// 存储json信息
	private ArrayList<JEvent> events = new ArrayList<JEvent>();
	private ArrayList<JFunction> functions = new ArrayList<JFunction>();
	private ArrayList<JXor> xors = new ArrayList<JXor>();
	private ArrayList<JAnd> ands = new ArrayList<JAnd>();
	private ArrayList<JOr> ors = new ArrayList<JOr>();
	private ArrayList<JCF> cfs = new ArrayList<JCF>();
	private ArrayList<JPI> jpis = new ArrayList<JPI>();
	private ArrayList<JOrganization> organizations = new ArrayList<JOrganization>();
	private ArrayList<JPosition> positions = new ArrayList<JPosition>();
	private ArrayList<JData> jdatas = new ArrayList<JData>();
	private ArrayList<JITSystem> itsystems = new ArrayList<JITSystem>();
	private ArrayList<JTextNode> textnodes = new ArrayList<JTextNode>();
	private ArrayList<JRelation> relations = new ArrayList<JRelation>();

	private HashSet<String> allIDs = new HashSet<String>();
	private HashSet<String> alreadyLayoutIDs = new HashSet<String>();
	private HashMap<String, Shape> idtopostions = new HashMap<String, Shape>();
	// 存储连接关系
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

	public void getConnections() {
		for (int i = 0; i < cfs.size(); i++) {
			JCF jcf = cfs.get(i);
			String aId = jcf.getId();
			String fromId = jcf.getFromId();
			String toId = jcf.getToId();
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

		for (int i = 0; i < relations.size(); i++) {
			JRelation jcf = relations.get(i);
			String rId = jcf.getId();
			String fromId = jcf.getFromId();
			String toId = jcf.getToId();
			if (connections.containsKey(fromId)) {
				connections.get(fromId).add(rId);
			} else {
				ArrayList<String> a = new ArrayList<String>();
				a.add(rId);
				connections.put(fromId, a);
			}
			if (connections.containsKey(rId)) {
				connections.get(rId).add(toId);
			} else {
				ArrayList<String> a = new ArrayList<String>();
				a.add(toId);
				connections.put(rId, a);
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
			Element epml = doc.getDocumentElement();
			if (epml == null)
				return;

			// all net node
			NodeList directorys = epml.getChildNodes();
			// 只有一个net
			Node directory = null;
			for (int i = 0; i < directorys.getLength(); i++) {
				directory = directorys.item(i);
				if (directory != null
						&& directory.getNodeType() == Node.ELEMENT_NODE
						&& directory.getNodeName().equals("directory")) {
					directory = directorys.item(i);
					break;
				}
			}
			NodeList epcs = directory.getChildNodes();
			Node epc = null;
			for (int i = 0; i < epcs.getLength(); i++) {
				epc = epcs.item(i);
				if (epc != null && epc.getNodeType() == Node.ELEMENT_NODE) {
					epc = epcs.item(i);
					break;
				}
			}

			NodeList shapes = epc.getChildNodes();
			if (shapes == null)
				return;
			for (int i = 0; i < shapes.getLength(); i++) {
				Node shape = shapes.item(i);
				// 是图形节点
				if (shape != null && shape.getNodeType() == Node.ELEMENT_NODE) {
					if (shape.getNodeName().equals("event")) {
						JEvent jevent = new JEvent();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						jevent.setId(id);
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
										// 获取位置
										if (childchild.getNodeName().equals(
												"position")) {
											haslayout = true;
											String sfx = childchild
													.getAttributes()
													.getNamedItem("x")
													.getNodeValue();
											Float fx = Float.parseFloat(sfx);
											int x = fx.intValue();
											jevent.setX(x);

											String sfy = childchild
													.getAttributes()
													.getNamedItem("y")
													.getNodeValue();
											Float fy = Float.parseFloat(sfy);
											int y = fy.intValue();
											jevent.setY(y);

											String swidth = childchild
													.getAttributes()
													.getNamedItem("width")
													.getNodeValue();
											Float fwidth = Float
													.parseFloat(swidth);
											int width = fwidth.intValue();
											jevent.setWidth(width);

											String sheight = childchild
													.getAttributes()
													.getNamedItem("height")
													.getNodeValue();
											Float fheight = Float
													.parseFloat(sheight);
											int height = fheight.intValue();
											jevent.setHeight(height);
										}
									}
								}
								if (child.getNodeName().equals("name")) {
									String name = child.getTextContent();
									if (!name.startsWith("oryx"))
										jevent.setName(name);
								}
							}
						}
						allIDs.add(id);
						idToShapePosition(jevent);
						events.add(jevent);
					}
					if (shape.getNodeName().equals("function")) {
						JFunction jfunction = new JFunction();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						jfunction.setId(id);
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
										// 获取位置
										if (childchild.getNodeName().equals(
												"position")) {
											haslayout = true;
											String sfx = childchild
													.getAttributes()
													.getNamedItem("x")
													.getNodeValue();
											Float fx = Float.parseFloat(sfx);
											int x = fx.intValue();
											jfunction.setX(x);

											String sfy = childchild
													.getAttributes()
													.getNamedItem("y")
													.getNodeValue();
											Float fy = Float.parseFloat(sfy);
											int y = fy.intValue();
											jfunction.setY(y);

											String swidth = childchild
													.getAttributes()
													.getNamedItem("width")
													.getNodeValue();
											Float fwidth = Float
													.parseFloat(swidth);
											int width = fwidth.intValue();
											jfunction.setWidth(width);

											String sheight = childchild
													.getAttributes()
													.getNamedItem("height")
													.getNodeValue();
											Float fheight = Float
													.parseFloat(sheight);
											int height = fheight.intValue();
											jfunction.setHeight(height);
										}
										
									}
								}
								if (child.getNodeName().equals("name")) {
									String name = child.getTextContent();
									if (!name.startsWith("oryx"))
										jfunction.setName(name);
								}
							}
						}
						allIDs.add(id);
						idToShapePosition(jfunction);
						functions.add(jfunction);
					}
					if (shape.getNodeName().equals("and")) {
						JAnd jand = new JAnd();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						jand.setId(id);
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
										// 获取位置
										if (childchild.getNodeName().equals(
												"position")) {
											haslayout = true;
											String sfx = childchild
													.getAttributes()
													.getNamedItem("x")
													.getNodeValue();
											Float fx = Float.parseFloat(sfx);
											int x = fx.intValue();
											jand.setX(x);

											String sfy = childchild
													.getAttributes()
													.getNamedItem("y")
													.getNodeValue();
											Float fy = Float.parseFloat(sfy);
											int y = fy.intValue();
											jand.setY(y);

											String swidth = childchild
													.getAttributes()
													.getNamedItem("width")
													.getNodeValue();
											Float fwidth = Float
													.parseFloat(swidth);
											int width = fwidth.intValue();
											jand.setWidth(width);

										}
										
									}
								}
								if (child.getNodeName().equals("name")) {
									String name = child.getTextContent();
									if (!name.startsWith("oryx"))
										jand.setName(name);
								}
							}
						}
						allIDs.add(id);
						idToShapePosition(jand);
						ands.add(jand);
					}
					if (shape.getNodeName().equals("xor")) {
						JXor jxor = new JXor();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						jxor.setId(id);
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
										// 获取位置
										if (childchild.getNodeName().equals(
												"position")) {
											haslayout = true;
											String sfx = childchild
													.getAttributes()
													.getNamedItem("x")
													.getNodeValue();
											Float fx = Float.parseFloat(sfx);
											int x = fx.intValue();
											jxor.setX(x);

											String sfy = childchild
													.getAttributes()
													.getNamedItem("y")
													.getNodeValue();
											Float fy = Float.parseFloat(sfy);
											int y = fy.intValue();
											jxor.setY(y);

											String swidth = childchild
													.getAttributes()
													.getNamedItem("width")
													.getNodeValue();
											Float fwidth = Float
													.parseFloat(swidth);
											int width = fwidth.intValue();
											jxor.setWidth(width);

										}
										
									}
								}
								if (child.getNodeName().equals("name")) {
									String name = child.getTextContent();
									if (!name.startsWith("oryx"))
										jxor.setName(name);
								}
							}
						}
						allIDs.add(id);
						idToShapePosition(jxor);
						xors.add(jxor);
					}

					if (shape.getNodeName().equals("or")) {
						JOr jor = new JOr();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						jor.setId(id);
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
										// 获取位置
										if (childchild.getNodeName().equals(
												"position")) {
											haslayout = true;
											String sfx = childchild
													.getAttributes()
													.getNamedItem("x")
													.getNodeValue();
											Float fx = Float.parseFloat(sfx);
											int x = fx.intValue();
											jor.setX(x);

											String sfy = childchild
													.getAttributes()
													.getNamedItem("y")
													.getNodeValue();
											Float fy = Float.parseFloat(sfy);
											int y = fy.intValue();
											jor.setY(y);

											String swidth = childchild
													.getAttributes()
													.getNamedItem("width")
													.getNodeValue();
											Float fwidth = Float
													.parseFloat(swidth);
											int width = fwidth.intValue();
											jor.setWidth(width);

										}
										
									}
								}
								if (child.getNodeName().equals("name")) {
									String name = child.getTextContent();
									if (!name.startsWith("oryx"))
										jor.setName(name);
								}
							}
						}
						allIDs.add(id);
						idToShapePosition(jor);
						ors.add(jor);
					}

					if (shape.getNodeName().equals("arc")) {
						JCF jcf = new JCF();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						jcf.setId(id);
						NodeList childs = shape.getChildNodes();
						for (int j = 0; j < childs.getLength(); j++) {
							Node child = childs.item(j);
							if (child != null
									& child.getNodeType() == Node.ELEMENT_NODE) {
								if (child.getNodeName().equals("flow")) {
									String fromId = child.getAttributes()
											.getNamedItem("source")
											.getNodeValue();
									String toId = child.getAttributes()
											.getNamedItem("target")
											.getNodeValue();
									jcf.setFromId(fromId);
									jcf.setToId(toId);
								}
							}
						}
						allIDs.add(id);
						cfs.add(jcf);
					}
					if (shape.getNodeName().equals("processInterface")) {
						JPI jpi = new JPI();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						jpi.setId(id);
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
										// 获取位置
										if (childchild.getNodeName().equals(
												"position")) {
											String sfx = childchild
													.getAttributes()
													.getNamedItem("x")
													.getNodeValue();
											Float fx = Float.parseFloat(sfx);
											int x = fx.intValue();
											jpi.setX(x);

											String sfy = childchild
													.getAttributes()
													.getNamedItem("y")
													.getNodeValue();
											Float fy = Float.parseFloat(sfy);
											int y = fy.intValue();
											jpi.setY(y);

											String swidth = childchild
													.getAttributes()
													.getNamedItem("width")
													.getNodeValue();
											Float fwidth = Float
													.parseFloat(swidth);
											int width = fwidth.intValue();
											jpi.setWidth(width);

											String sheight = childchild
													.getAttributes()
													.getNamedItem("height")
													.getNodeValue();
											Float fheight = Float
													.parseFloat(sheight);
											int height = fheight.intValue();
											jpi.setHeight(height);

										}
										
									}
								}
								if (child.getNodeName().equals("name")) {
									String name = child.getTextContent();
									if (!name.startsWith("oryx"))
										jpi.setName(name);
								}
							}
						}
						allIDs.add(id);
						idToShapePosition(jpi);
						jpis.add(jpi);
					}
					if (shape.getNodeName().equals("participant")) {
						boolean isOrganization = false;
						boolean isPosition = false;
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						String name = "";
						int x = 0, y = 0, width = 0, height = 0;

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
										// 获取位置
										if (childchild.getNodeName().equals(
												"position")) {
											haslayout = true;
											String sfx = childchild
													.getAttributes()
													.getNamedItem("x")
													.getNodeValue();
											Float fx = Float.parseFloat(sfx);
											x = fx.intValue();

											String sfy = childchild
													.getAttributes()
													.getNamedItem("y")
													.getNodeValue();
											Float fy = Float.parseFloat(sfy);
											y = fy.intValue();
											String swidth = childchild
													.getAttributes()
													.getNamedItem("width")
													.getNodeValue();
											Float fwidth = Float
													.parseFloat(swidth);
											width = fwidth.intValue();
											String sheight = childchild
													.getAttributes()
													.getNamedItem("height")
													.getNodeValue();
											Float fheight = Float
													.parseFloat(sheight);
											height = fheight.intValue();
										}
										
									}
								}
								if (child.getNodeName().equals("name")) {
									if (!child.getTextContent().startsWith(
											"oryx"))
										name = child.getTextContent();
								}
								if (child.getNodeName().equals("attribute")) {
									String type = child.getAttributes()
											.getNamedItem("value")
											.getNodeValue();
									if (type.equals("Organization")) {
										isOrganization = true;
									} else if (type.equals("Position")) {
										isPosition = true;
									}
								}
							}
						}
						if (isOrganization) {
							JOrganization jo = new JOrganization();
							jo.setId(id);
							jo.setName(name);
							jo.setWidth(width);
							jo.setHeight(height);
							jo.setX(x);
							jo.setY(y);
							allIDs.add(id);
							idToShapePosition(jo);
							organizations.add(jo);
						} else if (isPosition) {
							JPosition jp = new JPosition();
							jp.setId(id);
							jp.setName(name);
							jp.setWidth(width);
							jp.setHeight(height);
							jp.setX(x);
							jp.setY(y);
							allIDs.add(id);
							idToShapePosition(jp);
							positions.add(jp);
						}

					}
					if (shape.getNodeName().equals("dataField")) {
						JData jdata = new JData();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						jdata.setId(id);
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
										// 获取位置
										if (childchild.getNodeName().equals(
												"position")) {
											haslayout = true;
											String sfx = childchild
													.getAttributes()
													.getNamedItem("x")
													.getNodeValue();
											Float fx = Float.parseFloat(sfx);
											int x = fx.intValue();
											jdata.setX(x);

											String sfy = childchild
													.getAttributes()
													.getNamedItem("y")
													.getNodeValue();
											Float fy = Float.parseFloat(sfy);
											int y = fy.intValue();
											jdata.setY(y);

											String swidth = childchild
													.getAttributes()
													.getNamedItem("width")
													.getNodeValue();
											Float fwidth = Float
													.parseFloat(swidth);
											int width = fwidth.intValue();
											jdata.setWidth(width);

											String sheight = childchild
													.getAttributes()
													.getNamedItem("height")
													.getNodeValue();
											Float fheight = Float
													.parseFloat(sheight);
											int height = fheight.intValue();
											jdata.setHeight(height);

										}
									}
								}
								if (child.getNodeName().equals("name")) {
									String name = child.getTextContent();
									if (!name.startsWith("oryx"))
										jdata.setName(name);
								}
							}
						}
						allIDs.add(id);
						idToShapePosition(jdata);
						jdatas.add(jdata);
					}
					if (shape.getNodeName().equals("application")) {
						JITSystem jsis = new JITSystem();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						jsis.setId(id);
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
										// 获取位置
										if (childchild.getNodeName().equals(
												"position")) {
											haslayout = true;
											String sfx = childchild
													.getAttributes()
													.getNamedItem("x")
													.getNodeValue();
											Float fx = Float.parseFloat(sfx);
											int x = fx.intValue();
											jsis.setX(x);

											String sfy = childchild
													.getAttributes()
													.getNamedItem("y")
													.getNodeValue();
											Float fy = Float.parseFloat(sfy);
											int y = fy.intValue();
											jsis.setY(y);

											String swidth = childchild
													.getAttributes()
													.getNamedItem("width")
													.getNodeValue();
											Float fwidth = Float
													.parseFloat(swidth);
											int width = fwidth.intValue();
											jsis.setWidth(width);

											String sheight = childchild
													.getAttributes()
													.getNamedItem("height")
													.getNodeValue();
											Float fheight = Float
													.parseFloat(sheight);
											int height = fheight.intValue();
											jsis.setHeight(height);

										}
									}
								}
								if (child.getNodeName().equals("name")) {
									String name = child.getTextContent();
									if (!name.startsWith("oryx"))
										jsis.setName(name);
								}
							}
						}
						idToShapePosition(jsis);
						allIDs.add(id);
						itsystems.add(jsis);
					}
					if (shape.getNodeName().equals("relation")) {
						JRelation jrelation = new JRelation();
						String id = shape.getAttributes().getNamedItem("id")
								.getNodeValue();
						String fromId = shape.getAttributes()
								.getNamedItem("from").getNodeValue();
						String toId = shape.getAttributes().getNamedItem("to")
								.getNodeValue();
						jrelation.setId(id);
						jrelation.setFromId(fromId);
						jrelation.setToId(toId);
						allIDs.add(id);
						relations.add(jrelation);
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
		int[] countD = new int[500];

		for (String startId : allstarts) {

			ArrayDeque<String> queue = new ArrayDeque<String>();
			queue.push(startId);

			while (queue.isEmpty() == false) {

				String id = queue.poll();
				// 保证子图的位置相同
				ArrayList<String> fchilds = connections.get(id);
				if (fchilds == null)
					continue;
				if (idtopostions.get(id).getLayer() < 0) {
					// 没有被设置过层级
					idtopostions.get(id).setLayer(0);
				}

				setShape(id, idtopostions.get(id).getLayer(),
						countD[idtopostions.get(id).getLayer()]);

				for (String fid : fchilds) {

					if (connections.get(fid) == null)
						continue;
					// 得到子图形的id

					String sid = connections.get(fid).get(0);
					queue.push(sid);
					if (idtopostions.get(sid).getLayer() < 0) {
						// 没有被设置过层级
						idtopostions.get(sid).setLayer(
								idtopostions.get(id).getLayer() + 1);
					}
					setShape(
							sid,
							idtopostions.get(sid).getLayer(),
							countD[idtopostions.get(sid).getLayer()] = countD[idtopostions
									.get(sid).getLayer()] + 1);

				}
			}
		}

	}

	private void setShape(String id, int countC, int countD) {

		if (alreadyLayoutIDs.contains(id)) {
			return;
		}
		alreadyLayoutIDs.add(id);
		int width = 0, height = 0;
		if (idtopostions.get(id).getType().equals("JData")
				|| idtopostions.get(id).getType().equals("JEvent")
				|| idtopostions.get(id).getType().equals("JFunction")
				|| idtopostions.get(id).getType().equals("JITSystem")
				|| idtopostions.get(id).getType().equals("JOrganization")
				|| idtopostions.get(id).getType().equals("JPI")
				|| idtopostions.get(id).getType().equals("JPosition")
				|| idtopostions.get(id).getType().equals("JTextNode")){
			width = 100;
			height = 60;
		} else if (idtopostions.get(id).getType().equals("JAnd")
				||idtopostions.get(id).getType().equals("JOr")
				||idtopostions.get(id).getType().equals("JXor")){
			width = 30;
			height = 30;
		} 

		// 取消自动换行
		// int[] positionX = { 300, 500, 700, 900, 1100, 1100, 900, 700, 500,
		// 300 };
		int initX = 100;
		int initY = 80;

		int spaceX = 100;
		int spaceY = 100;
		/*
		 * for(int i = 0 ;i<shapes.size();i++){
		 * if(shapes.get(i).getId().equals(id)){ Shape shape = shapes.get(i);
		 * shape.setWidth(width); shape.setHeight(height); shape.setX(initX +
		 * spaceX*countC); System.out.println(id); System.out.println(initX +
		 * spaceX*countC); shape.setY(initY + spaceY*countD); } }
		 */

		idtopostions.get(id).setWidth(width);
		idtopostions.get(id).setHeight(height);

		idtopostions.get(id).setX(initX + spaceX * (countC)- width/2);
		idtopostions.get(id).setY(initY + spaceY * (countD)- height/2);
	}

	private StringBuilder buildJson(StringBuilder sb) {
		// TODO Auto-generated method stub
		for (int i = 0; i < events.size(); i++) {
			JEvent event = events.get(i);
			String id = event.getId();
			String name = event.getName();
			int x = event.getX();
			int y = event.getY();
			int width = event.getWidth();
			int height = event.getHeight();
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"title\": \"" + name + "\","
					+ "\"frequency\": \"\"," + "\"description\": \"\","
					+ "\"bgcolor\": \"#ffafff\"" + "}," + "\"stencil\": {"
					+ " \"id\": \"Event\"" + " }," + "\"childShapes\": [],"
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
		for (int i = 0; i < functions.size(); i++) {
			JFunction function = functions.get(i);
			String id = function.getId();
			String name = function.getName();
			int x = function.getX();
			int y = function.getY();
			int width = function.getWidth();
			int height = function.getHeight();
			idToShapePosition(function);
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"title\": \"" + name + "\","
					+ "\"frequency\": \"\"," + "\"refuri\": \"\","
					+ "\"description\": \"\"," + "\"bgcolor\": \"#96ff96\""
					+ "}," + "\"stencil\": {" + " \"id\": \"Function\"" + " },"
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
		for (int i = 0; i < ands.size(); i++) {
			JAnd and = ands.get(i);
			String id = and.getId();
			String name = and.getName();
			int x = and.getX();
			int y = and.getY();
			int r = and.getWidth();
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"bgcolor\": \"#ffffff\"" + "},"
					+ "\"stencil\": {" + " \"id\": \"AndConnector\"" + " },"
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
					+ " \"x\": " + (x + r) + "," + " \"y\": " + (y + r) + ""
					+ "}," + " \"upperLeft\": {" + " \"x\": " + (x) + ","
					+ "\"y\": " + (y) + "" + " }" + "}," + " \"dockers\": []"
					+ "},");
		}
		for (int i = 0; i < xors.size(); i++) {
			JXor xor = xors.get(i);
			String id = xor.getId();
			String name = xor.getName();
			int x = xor.getX();
			int y = xor.getY();
			int r = xor.getWidth();
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"bgcolor\": \"#ffffff\"" + "},"
					+ "\"stencil\": {" + " \"id\": \"XorConnector\"" + " },"
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
					+ " \"x\": " + (x + r) + "," + " \"y\": " + (y + r) + ""
					+ "}," + " \"upperLeft\": {" + " \"x\": " + (x) + ","
					+ "\"y\": " + (y) + "" + " }" + "}," + " \"dockers\": []"
					+ "},");
		}
		for (int i = 0; i < ors.size(); i++) {
			JOr or = ors.get(i);
			String id = or.getId();
			String name = or.getName();
			int x = or.getX();
			int y = or.getY();
			int r = or.getWidth();
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"bgcolor\": \"#ffffff\"" + "},"
					+ "\"stencil\": {" + " \"id\": \"OrConnector\"" + " },"
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
					+ " \"x\": " + (x + r) + "," + " \"y\": " + (y + r) + ""
					+ "}," + " \"upperLeft\": {" + " \"x\": " + (x) + ","
					+ "\"y\": " + (y) + "" + " }" + "}," + " \"dockers\": []"
					+ "},");
		}
		for (int i = 0; i < jpis.size(); i++) {
			JPI pi = jpis.get(i);
			String id = pi.getId();
			String name = pi.getName();
			int x = pi.getX();
			int y = pi.getY();
			int width = pi.getWidth();
			int height = pi.getHeight();
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"title\": \"" + name + "\","
					+ "\"refuri\": \"\"," + "\"entry\": \"\","
					+ "\"refuri\": \"\"," + "\"description\": \"\","
					+ "\"bgcolor\": \"#ffffff\"" + "}," + "\"stencil\": {"
					+ " \"id\": \"ProcessInterface\"" + " },"
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
		for (int i = 0; i < organizations.size(); i++) {
			JOrganization po = organizations.get(i);
			String id = po.getId();
			String name = po.getName();
			int x = po.getX();
			int y = po.getY();
			int width = po.getWidth();
			int height = po.getHeight();
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"title\": \"" + name + "\","
					+ "\"description\": \"\"," + "\"bgcolor\": \"#ffffaf\""
					+ "}," + "\"stencil\": {" + " \"id\": \"Organization\""
					+ " }," + "\"childShapes\": []," + " \"outgoing\": [");
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
		for (int i = 0; i < positions.size(); i++) {
			JPosition po = positions.get(i);
			String id = po.getId();
			String name = po.getName();
			int x = po.getX();
			int y = po.getY();
			int width = po.getWidth();
			int height = po.getHeight();
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"title\": \"" + name + "\","
					+ "\"description\": \"\"," + "\"bgcolor\": \"#ffff80\""
					+ "}," + "\"stencil\": {" + " \"id\": \"Position\"" + " },"
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
		for (int i = 0; i < jdatas.size(); i++) {
			JData data = jdatas.get(i);
			String id = data.getId();
			String name = data.getName();
			int x = data.getX();
			int y = data.getY();
			int width = data.getWidth();
			int height = data.getHeight();
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"title\": \"" + name + "\","
					+ "\"description\": \"\"," + "\"bgcolor\": \"#dcdcdc\""
					+ "}," + "\"stencil\": {" + " \"id\": \"Data\"" + " },"
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
		for (int i = 0; i < itsystems.size(); i++) {
			JITSystem system = itsystems.get(i);
			String id = system.getId();
			String name = system.getName();
			int x = system.getX();
			int y = system.getY();
			int width = system.getWidth();
			int height = system.getHeight();
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"title\": \"" + name + "\","
					+ "\"description\": \"\"," + "\"bgcolor\": \"#dcdcdc\""
					+ "}," + "\"stencil\": {" + " \"id\": \"System\"" + " },"
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
		for (int i = 0; i < itsystems.size(); i++) {
			JITSystem system = itsystems.get(i);
			String id = system.getId();
			String name = system.getName();
			int x = system.getX();
			int y = system.getY();
			int width = system.getWidth();
			int height = system.getHeight();
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"title\": \"" + name + "\","
					+ "\"description\": \"\"," + "\"bgcolor\": \"#dcdcdc\""
					+ "}," + "\"stencil\": {" + " \"id\": \"System\"" + " },"
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

		for (int i = 0; i < cfs.size(); i++) {
			JCF cf = cfs.get(i);
			String id = cf.getId();
			String fromId = cf.getFromId();
			String toId = cf.getToId();
			Shape toshape = idtopostions.get(toId);
			Shape fromshape = idtopostions.get(fromId);

			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"probability\": \"\"" + "},"
					+ "\"stencil\": {" + " \"id\": \"ControlFlow\"" + " },"
					+ "\"childShapes\": []," + " \"outgoing\": [" + "{"
					+ "\"resourceId\": \"" + connections.get(id).get(0) + "\""
					+ " }" + " ]," + " \"bounds\": {" + " \"lowerRight\": {"
					+ " \"x\": " + (toshape.getX() + toshape.getWidth() / 2)
					+ "," + " \"y\": " + (toshape.getY()) + "" + "},"
					+ " \"upperLeft\": {" + " \"x\": "
					+ (fromshape.getX() + fromshape.getWidth() / 2) + ","
					+ "\"y\": " + (fromshape.getY() + fromshape.getHeight())
					+ "" + " }" + "}," + " \"dockers\": [");

			sb.append(

			" {" + "\"x\": "+fromshape.getWidth()/2+"," + " \"y\":"+fromshape.getHeight()/2+"" + "}," + "{" + " \"x\": "+toshape.getWidth()/2+","
					+ "\"y\": "+toshape.getHeight()/2+"" + "}");

			sb.append(" ]"

			+ " },"

			);
		}
		for (int i = 0; i < relations.size(); i++) {
			JRelation relation = relations.get(i);
			String id = relation.getId();
			String fromId = relation.getFromId();
			String toId = relation.getToId();

			Shape toshape = idtopostions.get(toId);
			Shape fromshape = idtopostions.get(fromId);
			sb.append("{" + "\"resourceId\": \"" + id + "\","
					+ "\"properties\": {" + "\"description\": \"\""
					+ "\"informationflow\": \"False\"" + "},"
					+ "\"stencil\": {" + " \"id\": \"Relation\"" + " },"
					+ "\"childShapes\": []," + " \"outgoing\": [" + "{"
					+ "\"resourceId\": \"" + connections.get(id).get(0) + "\""
					+ " }" + " ]," + " \"bounds\": {" + " \"lowerRight\": {"
					+ " \"x\": " + (toshape.getX() + toshape.getWidth() / 2)
					+ "," + " \"y\": " + (toshape.getY()) + "" + "},"
					+ " \"upperLeft\": {" + " \"x\": "
					+ (fromshape.getX() + fromshape.getWidth() / 2) + ","
					+ "\"y\": " + (fromshape.getY() + fromshape.getHeight())
					+ "" + " }" + "}," + " \"dockers\": [");

			sb.append(

			" {" + "\"x\": "+fromshape.getWidth()/2+"," + " \"y\":"+fromshape.getHeight()/2+"" + "}," + "{" + " \"x\": "+toshape.getWidth()/2+","
					+ "\"y\": "+toshape.getHeight()/2+"" + "}");

			sb.append(" ]"

			+ " },"

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
				+ "\"properties\": {" + "\"title\": \"\","
				+ "\"version\":\"\"," + "\"author\": \"\"" + " },"
				+ "\"stencil\": {" + "\"id\": \"Diagram\"" + "},"
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
				+ "  \"url\": \"/oryx///stencilsets/epc/epc.json\","
				+ "\"namespace\": \"http://b3mn.org/stencilset/epc#\"" + "},"
				+ "   \"ssextensions\": []" + "}");
		return sb;
	}

	public static void main(String[] args) {
		String path = "1.epml";
		Epc2Json pj = new Epc2Json();
		pj.getShapInfo(path);
		pj.getConnections();
		System.out.println(pj.createJson());

	}
}
