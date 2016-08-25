package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.processmining.framework.models.petrinet.PNEdge;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.Transition;

public class PetrinetToBPMN {

	/**
	 * convert to BPMN 2.0
	 * 
	 * @param args
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static void main(String[] args) throws JDOMException, IOException {
		// TODO Auto-generated method stub
		String input = " ";
		String output = " ";
		File inputFile = new File(input);
		Document doc;
		doc = readXMLFromFile(inputFile);

	}

	public static Document readXMLFromFile(File file) throws JDOMException,
			IOException {
		SAXBuilder sb = new SAXBuilder();
		Document result = sb.build(file);
		return result;
	}

	public void convert(PetriNet petriNet) {

		Place place = null;

		// 1.读取place
		for (int i = 0; i < petriNet.getPlaces().size(); i++) {
			place = petriNet.getPlaces().get(i);
			// String placeID = "";
			if (place.getIdentifier().equals("pstart")) { // �?��库所，start
															// place->Start
															// Event

			} else if (place.getIdentifier().equals("pend")) { // 结束库所，end
																// place->End
																// Event

			} else { // 其余库所 ->单一网关（Exclusive Gateway�?

			}
		}

		Transition transition = null;
		// 2.读取transitions,transition->task
		for (int i = 0; i < petriNet.getTransitions().size(); i++) {
			transition = petriNet.getTransitions().get(i);
			if (transition.getLogEvent().getEventType().equals("complete")) {
				String taskName = transition.getIdentifier();
				String taskId = "";
			}
		}

		PNEdge e = null;
		// 3.建立connections,arc -> sequenceFlow
		for (int i = 0; i < petriNet.getEdges().size(); i++) {
			e = (PNEdge) petriNet.getEdges().get(i);
			if (e.isPT()) {
				Place p = (Place) e.getSource();
				Transition t = (Transition) e.getDest();
			} else {
				Place p = (Place) e.getDest();
				Transition t = (Transition) e.getSource();
			}
		}

		Iterator it = petriNet.getEdges().iterator();
		while (it.hasNext()) {

		}

	}

}
