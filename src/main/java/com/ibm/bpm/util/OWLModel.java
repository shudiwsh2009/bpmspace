package com.ibm.bpm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.ibm.bpm.model.OWLClass;
import com.ibm.bpm.model.Property;

public class OWLModel {
	public class ActivityEntity {
		public ActivityEntity() {
			super();
			this.incoming = new ArrayList<String>();
			this.outcoming = new ArrayList<String>();
			this.contain = new ArrayList<String>();
		}

		private String name;
		private ArrayList<String> incoming;
		private ArrayList<String> outcoming;
		private ArrayList<String> contain;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ArrayList<String> getIncoming() {
			return incoming;
		}

		public void setIncoming(ArrayList<String> incoming) {
			this.incoming = incoming;
		}

		public ArrayList<String> getOutcoming() {
			return outcoming;
		}

		public void setOutcoming(ArrayList<String> outcoming) {
			this.outcoming = outcoming;
		}

		public ArrayList<String> getContain() {
			return contain;
		}

		public void setContain(ArrayList<String> contain) {
			this.contain = contain;
		}
	}

	private Element readFile(String fileName) throws FileNotFoundException,
			ParserConfigurationException, SAXException, IOException {
		File f = new File(fileName);
		InputStream input = new FileInputStream(f);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(input);
		Element element = document.getDocumentElement();
		return element;
	}

	public Element getElement(String filepath) throws Exception {
		Element element = readFile(filepath);
		return element;
	}

	public ArrayList<OWLClass> getOWLList(Element element) {
		NodeList nodes = element.getChildNodes();
		ArrayList<OWLClass> owlList = new ArrayList<OWLClass>();

		Element classElement;
		NodeList allClass = element.getElementsByTagName("owl:Class");
		// System.out.println("allClass size:" + allClass.getLength());
		for (int i = 0; i < allClass.getLength(); i++) {
			OWLClass owl = new OWLClass();
			Node node = allClass.item(i);
			String ref = ((Element) node).getAttribute("rdf:about").split("#")[1];
			owl.setRef(ref);
			classElement = (Element) allClass.item(i);
			NodeList subclassof = classElement
					.getElementsByTagName("owl:equivalentClass");
			// System.out.println("subclassof size:" + subclassof.getLength());
			Property[] propList = null;
			if (subclassof.getLength() != 0)
				propList = new Property[subclassof.getLength()];
			for (int j = 0; j < subclassof.getLength(); j++) {
				NodeList restriction = ((Element) subclassof.item(j))
						.getElementsByTagName("owl:Restriction");
				// System.out.println("restriction size:" +
				// restriction.getLength());

				if (restriction.getLength() != 0) {
					NodeList onProperty = ((Element) restriction.item(0))
							.getElementsByTagName("owl:onProperty");
					Property prop = new Property();
					ref = ((Element) onProperty.item(0)).getAttribute(
							"rdf:resource").split("#")[1];
					prop.setOnProperty_name("onProperty");
					prop.setOnProperty_value(ref);
					NodeList someValuesFrom = ((Element) restriction.item(0))
							.getElementsByTagName("owl:someValuesFrom");
					if (someValuesFrom.getLength() != 0) {
						// System.out.println("someValuesFrom length:"+
						// someValuesFrom.getLength());
						// System.out.println("someValuesFrom:" +
						// ((Element)someValuesFrom.item(0)).getAttribute("rdf:resource"));
						ref = ((Element) someValuesFrom.item(0)).getAttribute(
								"rdf:resource").split("#")[1];
						prop.setValuesFrom_name("someValuesFrom");
						prop.setValuesFrom_value(ref);
						propList[j] = prop;
					}
					NodeList allValuesFrom = ((Element) restriction.item(0))
							.getElementsByTagName("owl:allValuesFrom");
					if (allValuesFrom.getLength() != 0) {
						// System.out.println("allValuesFrom length:"+
						// allValuesFrom.getLength());
						// System.out.println("allValuesFrom:" +
						// ((Element)allValuesFrom.item(0)).getAttribute("rdf:resource"));
						ref = ((Element) allValuesFrom.item(0)).getAttribute(
								"rdf:resource").split("#")[1];
						prop.setValuesFrom_name("allValuesFrom");
						prop.setValuesFrom_value(ref);
						propList[j] = prop;
					}
					NodeList onClass = ((Element) restriction.item(0))
							.getElementsByTagName("owl:onClass");
					if (onClass.getLength() != 0) {
						// System.out.println("allValuesFrom length:"+
						// allValuesFrom.getLength());
						// System.out.println("allValuesFrom:" +
						// ((Element)allValuesFrom.item(0)).getAttribute("rdf:resource"));
						ref = ((Element) onClass.item(0)).getAttribute(
								"rdf:resource").split("#")[1];
						prop.setValuesFrom_name("onClass");
						prop.setValuesFrom_value(ref);
						propList[j] = prop;
					}

				}
			}
			owl.setProp(propList);
			// System.out.println(owl);
			// System.out.println("rdf " + owl.getRdf());
			// System.out.println("onProperty " + owl.getProp());
			// System.out.println("someValuesFrom " + owl.getProp());
			if (subclassof.getLength() != 0)
				owlList.add(owl);
		}
		return owlList;

	}

	public void generateOWL(ArrayList<ActivityEntity> entities, String filepath)
			throws ParserConfigurationException, IOException,
			TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		DOMImplementation domImpl = doc.getImplementation();
		DocumentType doctype = domImpl.createDocumentType("rdf:RDF", "",
				"a.dtd");

		doc.appendChild(doctype);
		doc.setXmlStandalone(true);

		// root
		URL namespaceURL = new URL(
				"http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		String namespace = namespaceURL.toString();
		Element root = doc.createElementNS(namespace, "rdf:RDF");// ,"http://www.semanticweb.org/ontologies/2013/2/Ontology1364610337771.owl#");
		doc.appendChild(root);
		Attr ns = doc
				.createAttributeNS("http://www.w3.org/2002/07/owl#", "owl");
		root.setAttribute("xmlns:owl", "http://www.w3.org/2002/07/owl#");
		root.setAttribute("xmlns",
				"http://www.semanticweb.org/ontologies/2013/2/Ontology1364610337771.owl#");
		root.setAttribute("xml:base",
				"http://www.semanticweb.org/ontologies/2013/2/Ontology1364610337771.owl");
		root.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema#");// xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
		Element owlOntology = doc.createElement("owl:Ontology");
		owlOntology
				.setAttribute("rdf:about",
						"http://www.semanticweb.org/ontologies/2013/2/Ontology1364610337771.owl");
		root.appendChild(owlOntology);
		Element owlObjectProperty = doc.createElement("owl:ObjectProperty");
		EntityReference er = doc.createEntityReference("Ontology1364610337771");
		Text text = doc.createTextNode("Contain");
		Attr attr = doc.createAttribute("rdf:about");
		attr.appendChild(text);
		attr.appendChild(er);
		// owlObjectProperty.setAttributeNode(attr);
		// owlObjectProperty.setAttribute("rdf:about",text.getData());
		owlObjectProperty.setAttribute("rdf:about",
				"&Ontology1364610337771;ContainedBy");
		root.appendChild(owlObjectProperty);
		owlObjectProperty = doc.createElement("owl:ObjectProperty");
		er = doc.createEntityReference("Ontology1364610337771");
		owlObjectProperty.setAttribute("rdf:about",
				"&Ontology1364610337771;Incoming");
		root.appendChild(owlObjectProperty);
		owlObjectProperty = doc.createElement("owl:ObjectProperty");
		er = doc.createEntityReference("Ontology1364610337771");
		owlObjectProperty.setAttribute("rdf:about",
				"&Ontology1364610337771;Outcoming");
		root.appendChild(owlObjectProperty);

		for (int i = 0; i < entities.size(); i++) {
			// owl:Class
			Element owlClass = doc.createElement("owl:Class");
			owlClass.setAttribute("rdf:about", "&Ontology1364610337771;"
					+ entities.get(i).getName());
			for (int j = 0; j < entities.get(i).getContain().size(); j++) {
				// owl:equivalentClass
				Element equivalentClass = doc
						.createElement("owl:equivalentClass");
				// owl:Restriction
				Element restriction = doc.createElement("owl:Restriction");
				// owl:onProperty
				Element onProperty = doc.createElement("owl:onProperty");
				onProperty.setAttribute("rdf:resource",
						"&Ontology1364610337771;" + "ContainedBy");
				// owl:someValuesFrom
				Element valueFrom = doc.createElement("owl:someValuesFrom");
				valueFrom.setAttribute("rdf:resource",
						"&Ontology1364610337771;"
								+ entities.get(i).getContain().get(j));
				restriction.appendChild(onProperty);
				restriction.appendChild(valueFrom);
				equivalentClass.appendChild(restriction);
				owlClass.appendChild(equivalentClass);
				root.appendChild(owlClass);
			}
			for (int j = 0; j < entities.get(i).getIncoming().size(); j++) {
				// owl:equivalentClass
				Element equivalentClass = doc
						.createElement("owl:equivalentClass");
				// owl:Restriction
				Element restriction = doc.createElement("owl:Restriction");
				// owl:onProperty
				Element onProperty = doc.createElement("owl:onProperty");
				onProperty.setAttribute("rdf:resource",
						"&Ontology1364610337771;" + "Incoming");
				// owl:someValuesFrom
				Element valueFrom = doc.createElement("owl:someValuesFrom");
				valueFrom.setAttribute("rdf:resource",
						"&Ontology1364610337771;"
								+ entities.get(i).getIncoming().get(j));
				restriction.appendChild(onProperty);
				restriction.appendChild(valueFrom);
				equivalentClass.appendChild(restriction);
				owlClass.appendChild(equivalentClass);
				root.appendChild(owlClass);
			}
			for (int j = 0; j < entities.get(i).getOutcoming().size(); j++) {
				// owl:equivalentClass
				Element equivalentClass = doc
						.createElement("owl:equivalentClass");
				// owl:Restriction
				Element restriction = doc.createElement("owl:Restriction");
				// owl:onProperty
				Element onProperty = doc.createElement("owl:onProperty");
				onProperty.setAttribute("rdf:resource",
						"&Ontology1364610337771;" + "Outcoming");
				// owl:someValuesFrom
				Element valueFrom = doc.createElement("owl:someValuesFrom");
				valueFrom.setAttribute("rdf:resource",
						"&Ontology1364610337771;"
								+ entities.get(i).getOutcoming().get(j));
				restriction.appendChild(onProperty);
				restriction.appendChild(valueFrom);
				equivalentClass.appendChild(restriction);
				owlClass.appendChild(equivalentClass);
				root.appendChild(owlClass);
			}

		}
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		File file = new File(filepath);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file);
		// add DocType
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "a.dtd");
		StreamResult xmlResult = new StreamResult(out);
		transformer.transform(source, xmlResult);

		FileUtil.replaceFileContent(filepath, "&amp;", "&");
	}

	public static void main(String[] args) {
		String filepath = "E:\\BEN\\CMCC\\ImplProject\\coding_v2\\input\\Fragment knowledge base_v1.1.owl";
		OWLModel model = new OWLModel();

		try {
			Element element = model.getElement(filepath);

			ArrayList<OWLClass> list = model.getOWLList(element);
			// System.out.println("list size:" + list.size());
			filepath = "E:\\BEN\\CMCC\\ImplProject\\coding_v2\\output\\test.owl";
			ArrayList<ActivityEntity> entityList = new ArrayList<ActivityEntity>();
			ActivityEntity entity = model.new ActivityEntity();
			for (int i = 0; i < list.size(); i++) {
				entity = model.new ActivityEntity();
				entity.setName(list.get(i).getRdf());
				ArrayList<String> prop = new ArrayList<String>();
				for (int j = 0; j < list.get(i).getProp().length; j++) {
					prop.add(list.get(i).getProp()[j].getValuesFrom_value());
				}
				entity.setContain(prop);
				entityList.add(entity);
			}
			model.generateOWL(entityList, filepath);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
