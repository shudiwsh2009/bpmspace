package com.chinamobile.bpmspace.core.repository.index.test.model;

/***********************************************************
 *      This software is part of the ProM package          *
 *             http://www.processmining.org/               *
 *                                                         *
 *            Copyright (c) 2003-2006 TU/e Eindhoven       *
 *                and is licensed under the                *
 *            Common Public License, Version 1.0           *
 *        by Eindhoven University of Technology            *
 *           Department of Information Systems             *
 *                 http://is.tm.tue.nl                     *
 *                                                         *
 **********************************************************/

import java.util.Iterator;

import org.processmining.framework.models.petrinet.PNNode;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.Transition;
import org.processmining.framework.models.petrinet.TransitionCluster;

import att.grappa.Edge;

/**
 * Writes a PetriNet to a PNML file. Tokens are not stored in the PNML file.
 * 
 * @author Peter van den Brand
 * @version 1.0
 */

public class PetriNetConvertor {

	public final static String basicPntdUri = "http://www.informatik.hu-berlin.de/top/pnml/basicPNML.rng";

	public final static String workflowPntdUri = "http://www.processmining.org/workflownet1.0";

	private static int Y;
	private static int X;

	private static boolean PNKernel = true;

	private PetriNetConvertor() {
	}

	public static String write(PetriNet net) {

		String pnml = "";

		Iterator<TransitionCluster> it;
		pnml += "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n";
		pnml += "<pnml>\n";
		pnml += "<net id=\"workflownet\" type=\"" + basicPntdUri + "\">\n";

		pnml += writePlaces(net);
		pnml += writeTransitions(net);
		pnml += writeArcs(net);

		pnml += "</net>\n";
		pnml += "</pnml>";

		return pnml;
	}

	private static String writeArcs(PetriNet net) {
		String pnml = "";

		Iterator<Edge> e = net.getEdges().iterator();

		while (e.hasNext()) {
			Edge edge = e.next();
			PNNode head = (PNNode) edge.getHead(), tail = (PNNode) edge
					.getTail();

			pnml += "\t<arc id=\"arc_" + edge.getName() + "\" " + "source=\""
					+ head.getIdentifier() + "\" " + "target=\""
					+ tail.getIdentifier() + "\">\n";

			pnml += "\t</arc>\n";
		}
		return pnml;
	}

	private static String writePlaces(PetriNet net) {
		String pnml = "";
		Iterator<Place> e = net.getPlaces().iterator();

		while (e.hasNext()) {
			Place p = e.next();

			pnml += "\t<place id=\"" + p.getIdentifier() + "\" >\n";

			pnml += "\t\t<name>\n";
			pnml += "\t\t\t<text>" + p.getName() + "</text>\n";

			pnml += "\t\t</name>\n";

			pnml += "\t</place>\n";
		}
		return pnml;

	}

	private static String writeTransitions(PetriNet net) {
		String pnml = "";
		Iterator<Transition> e = net.getTransitions().iterator();

		while (e.hasNext()) {
			Transition t = e.next();
			pnml += "\t<transition id=\"" + t.getIdentifier() + "\" >\n";

			pnml += "\t\t<name>\n";
			pnml += "\t\t\t<text>" + t.getName() + "</text>\n";

			pnml += "\t\t</name>\n";

			pnml += "\t</transition>\n";
		}
		return pnml;
	}
}