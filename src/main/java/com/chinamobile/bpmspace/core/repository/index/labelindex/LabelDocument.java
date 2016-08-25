package com.chinamobile.bpmspace.core.repository.index.labelindex;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class LabelDocument {
	public static final String FIELD_LABEL = "label";

	private LabelDocument() {

	}

	public static Document Document(String label) {
		Document doc = new Document();
		Field fLabel = new Field(FIELD_LABEL, label, Field.Store.YES,
				Field.Index.ANALYZED_NO_NORMS, Field.TermVector.YES);
		fLabel.setOmitTermFreqAndPositions(true);
		doc.add(fLabel);
		return doc;
	}

}
