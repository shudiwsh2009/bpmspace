package com.chinamobile.bpmspace.core.repository.index.logindex.aeventindex;

import java.io.StringReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.chinamobile.bpmspace.core.domain.log.Log;

public class AEventIndexDocument {
	public static final String FIELDLOGID = "log_id";
	public static final String FIELDEVENTS = "EVENTS";
	public static final String FIELDNEXTEVENTS = "NEXTEVENTS";
	public static final String TARCONNECTOR = " > ";

	private AEventIndexDocument() {

	}

	public static Document Document(Log ln, String eventName,
			String nextEventName, String log_id) {
		Document doc = new Document();

		Field fLogID = new Field(FIELDLOGID, log_id, Field.Store.YES,
				Field.Index.NOT_ANALYZED_NO_NORMS);
		doc.add(fLogID);

		Field fEVENTs = new Field(FIELDEVENTS, new StringReader(eventName),
				Field.TermVector.YES);
		fEVENTs.setOmitNorms(true);
		doc.add(fEVENTs);

		Field fNEXTEVENTs = new Field(FIELDNEXTEVENTS, new StringReader(
				nextEventName), Field.TermVector.YES);
		fNEXTEVENTs.setOmitNorms(true);
		doc.add(fNEXTEVENTs);
		// doc.add(new NumericField(FIELDLENS, Field.Store.YES,
		// true).setIntValue(length));
		return doc;
	}
}
