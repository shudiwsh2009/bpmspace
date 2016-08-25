package com.chinamobile.bpmspace.core.repository.index.logindex.durationindex;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;

import com.chinamobile.bpmspace.core.domain.log.Log;

public class DurationIndexDocument {
	public static final String FIELDLOGID = "log_id";
	public static final String FIELDLENS = "DURATION";
	public static final String TARCONNECTOR = " > ";

	private DurationIndexDocument() {

	}

	public static Document Document(Log ln, int duration, String log_id) {
		Document doc = new Document();

		Field fLogID = new Field(FIELDLOGID, log_id, Field.Store.YES,
				Field.Index.NOT_ANALYZED_NO_NORMS);
		doc.add(fLogID);

		doc.add(new NumericField(FIELDLENS, Field.Store.YES, true)
				.setIntValue(duration));
		return doc;
	}
}
