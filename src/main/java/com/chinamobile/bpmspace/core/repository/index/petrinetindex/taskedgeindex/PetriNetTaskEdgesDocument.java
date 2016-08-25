package com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskedgeindex;

import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.processmining.framework.models.petrinet.PetriNet;

import cn.edu.thss.iise.beehivez.server.metric.mcessimilarity.TaskLine4PetriNet;

import com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskindex.SemicolonTokenizer;

public class PetriNetTaskEdgesDocument {
	public static final String FIELDPROCESSID = "process_id";
	public static final String FIELDTASKEDGES = "TASKEDGES";
	public static final String TASKEDGECONNECTOR = " > ";

	public PetriNetTaskEdgesDocument() {

	}

	// make a lucene document from a petri net object
	public static Document Document(PetriNet pn, String process_id) {
		Document doc = new Document();

		Field fProcessID = new Field(FIELDPROCESSID, process_id,
				Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
		doc.add(fProcessID);

		// get Task edges from petri net and form a string with semicolon
		// divided
		StringBuilder sb = new StringBuilder();

		ArrayList<TaskLine4PetriNet> tls = TaskLine4PetriNet
				.getAllTaskLinesOfPetriNet(pn);
		for (TaskLine4PetriNet tl : tls) {
			sb.append(tl.getSrcTransition().getIdentifier().trim()
					+ TASKEDGECONNECTOR
					+ tl.getDestTransition().getIdentifier().trim()
					+ SemicolonTokenizer.delimiter);
		}
		Field fTaskEdges = new Field(FIELDTASKEDGES, new StringReader(
				sb.toString()), Field.TermVector.YES);
		fTaskEdges.setOmitNorms(true);
		doc.add(fTaskEdges);

		return doc;
	}

}
