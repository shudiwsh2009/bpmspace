package com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskindex;

import java.io.StringReader;
import java.util.HashSet;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Transition;

import cn.edu.thss.iise.beehivez.server.util.TransitionLabelPair;

public class TransitionRelationDocument {
	public static final int PARALLELWITH = 0;
	public static final int PRECEDE = 1;
	public static final int EXCLUDE = 2;

	public static final String PARALLELWITHCONNECTOR = " == ";
	public static final String PRECEDECONNECTOR = " -> ";
	public static final String EXCLUDECONNECTOR = " ## ";

	public static final String[] RELATIONCONNECTORS = { PARALLELWITHCONNECTOR,
			PRECEDECONNECTOR, EXCLUDECONNECTOR };

	public static final String fieldProcessID = "process_id";
	public static final String fieldTask = "task";
	public static final String fieldParallelWith = "parallel";
	public static final String fieldPrecede = "precede";
	public static final String fieldExclude = "exclude";

	public static final String[] RELATIONFIELDS = { fieldParallelWith,
			fieldPrecede, fieldExclude };

	private TransitionRelationDocument() {
	}

	public static Document Document(PetriNet pn, String process_id) {
		Document doc = new Document();

		Field fProcessID = new Field(fieldProcessID, process_id,
				Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
		fProcessID.setOmitTermFreqAndPositions(true);
		doc.add(fProcessID);

		// add task
		StringBuilder sb = new StringBuilder();
		for (Transition t : pn.getTransitions()) {
			sb.append(t.getIdentifier().trim() + SemicolonTokenizer.delimiter);
		}
		Field fTask = new Field(fieldTask, new StringReader(sb.toString()),
				Field.TermVector.NO);
		fTask.setOmitNorms(true);
		fTask.setOmitTermFreqAndPositions(true);
		doc.add(fTask);

		// calculate the relations between transitions
		TransitionRelationBuilder tr = new TransitionRelationBuilder(pn);
		tr.calculateOnCFP();

		// add parallel
		sb = new StringBuilder();
		HashSet<TransitionLabelPair> parallel = tr.getParallel();
		for (TransitionLabelPair tlp : parallel) {
			sb.append(tlp.getFirst().trim() + PARALLELWITHCONNECTOR
					+ tlp.getSecond().trim() + SemicolonTokenizer.delimiter);
		}
		Field fParallel = new Field(fieldParallelWith, new StringReader(
				sb.toString()), Field.TermVector.NO);
		fParallel.setOmitNorms(true);
		fParallel.setOmitTermFreqAndPositions(true);
		doc.add(fParallel);

		// add precede
		sb = new StringBuilder();
		HashSet<TransitionLabelPair> precede = tr.getPrecede();
		for (TransitionLabelPair tlp : precede) {
			sb.append(tlp.getFirst().trim() + PRECEDECONNECTOR
					+ tlp.getSecond().trim() + SemicolonTokenizer.delimiter);
		}
		Field fDirectCause = new Field(fieldPrecede, new StringReader(
				sb.toString()), Field.TermVector.NO);
		fDirectCause.setOmitNorms(true);
		fDirectCause.setOmitTermFreqAndPositions(true);
		doc.add(fDirectCause);

		// add exclude
		sb = new StringBuilder();
		HashSet<TransitionLabelPair> exclude = tr.getExclude();
		for (TransitionLabelPair tlp : exclude) {
			sb.append(tlp.getFirst().trim() + EXCLUDECONNECTOR
					+ tlp.getSecond().trim() + SemicolonTokenizer.delimiter);
		}
		Field fExclude = new Field(fieldExclude,
				new StringReader(sb.toString()), Field.TermVector.NO);
		fExclude.setOmitNorms(true);
		fExclude.setOmitTermFreqAndPositions(true);
		doc.add(fExclude);

		return doc;
	}

}
