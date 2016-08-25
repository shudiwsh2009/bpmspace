package com.chinamobile.bpmspace.core.repository.index.petriindex.pathindex;

import java.io.StringReader;
import java.util.Iterator;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.Transition;

import com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskindex.SemicolonTokenizer;

public class PathLengthTwoClosureDocument {
	public static final String fieldProcessID = "process_id";
	public static final String fieldPath = "path2";

	public static Document Document(PetriNet pn, String process_id) {
		Document doc = new Document();

		Field fProcessID = new Field(fieldProcessID, process_id,
				Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
		doc.add(fProcessID);

		StringBuilder sb = new StringBuilder();
		for (Place p : pn.getPlaces()) {
			Iterator it1 = p.getPredecessors().iterator();
			while (it1.hasNext()) {
				Transition t1 = (Transition) it1.next();
				Iterator it2 = p.getSuccessors().iterator();
				while (it2.hasNext()) {
					Transition t2 = (Transition) it2.next();
					sb.append(t1.getIdentifier().trim() + "->"
							+ t2.getIdentifier().trim()
							+ SemicolonTokenizer.delimiter);
				}
			}
		}
		Field fTask = new Field(fieldPath, new StringReader(sb.toString()),
				Field.TermVector.NO);
		fTask.setOmitNorms(true);
		doc.add(fTask);

		return doc;
	}

}
