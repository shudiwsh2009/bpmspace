package com.chinamobile.bpmspace.core.repository.index.petrinetindex.tarindex;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.processmining.framework.models.petrinet.PetriNet;

import cn.edu.thss.iise.beehivez.server.util.PetriNetUtil;
import cn.edu.thss.iise.beehivez.server.util.TransitionLabelPair;

import com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskindex.SemicolonTokenizer;

public class PetriNetTARsDocument {
	public static final String FIELDPROCESSID = "process_id";
	public static final String FIELDTARS = "TARS";
	public static final String TARCONNECTOR = " > ";

	private PetriNetTARsDocument() {

	}

	// make a lucene document from a petri net object
	public static Document Document(PetriNet pn, String process_id) {
		Document doc = new Document();

		Field fProcessID = new Field(FIELDPROCESSID, process_id,
				Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
		doc.add(fProcessID);

		// get TARS from petri net and form a string with semicolon divided
		StringBuilder sb = new StringBuilder();

		HashSet<TransitionLabelPair> tars = PetriNetUtil
				.getTARSFromPetriNetByCFP(pn);
		Iterator<TransitionLabelPair> it = tars.iterator();
		while (it.hasNext()) {
			TransitionLabelPair tlp = it.next();
			sb.append(tlp.getFirst().trim() + TARCONNECTOR
					+ tlp.getSecond().trim() + SemicolonTokenizer.delimiter);
		}

		Field fTARs = new Field(FIELDTARS, new StringReader(sb.toString()),
				Field.TermVector.YES);
		fTARs.setOmitNorms(true);
		doc.add(fTARs);

		return doc;
	}

}
