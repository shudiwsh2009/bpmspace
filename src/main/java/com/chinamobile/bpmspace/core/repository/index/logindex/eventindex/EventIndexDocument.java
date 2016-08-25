package com.chinamobile.bpmspace.core.repository.index.logindex.eventindex;

import java.io.StringReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.chinamobile.bpmspace.core.domain.log.Log;

public class EventIndexDocument {
	public static final String FIELDLOGID = "log_id";
	public static final String FIELDEVENTS = "EVENTS";
	public static final String TARCONNECTOR = " > ";

	private EventIndexDocument() {

	}

	public static Document Document(Log ln, String eventName, String log_id) {
		Document doc = new Document();

		Field fLogID = new Field(FIELDLOGID, log_id, Field.Store.YES,
				Field.Index.NOT_ANALYZED_NO_NORMS);
		doc.add(fLogID);

		Field fEVENTs = new Field(FIELDEVENTS, new StringReader(eventName),
				Field.TermVector.YES);
		fEVENTs.setOmitNorms(true);
		doc.add(fEVENTs);

		// doc.add(new NumericField(FIELDLENS, Field.Store.YES,
		// true).setIntValue(length));
		return doc;
	}
}
