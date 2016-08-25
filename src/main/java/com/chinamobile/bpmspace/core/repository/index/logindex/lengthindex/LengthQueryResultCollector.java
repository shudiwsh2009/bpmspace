package com.chinamobile.bpmspace.core.repository.index.logindex.lengthindex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

import com.chinamobile.bpmspace.core.repository.index.LogQueryResult;

public class LengthQueryResultCollector extends Collector {
	private int docBase;
	private Scorer scorer;
	private IndexReader reader;

	// the query term set has already been expanded with its similar ones
	// private HashSet<HashSet<String>> exQueryTermSet;
	private int length;
	private List<LogQueryResult> queryResult = new ArrayList<LogQueryResult>();

	public List<LogQueryResult> getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(List<LogQueryResult> queryResult) {
		this.queryResult = queryResult;
	}

	public LengthQueryResultCollector(IndexReader reader, int length) {
		this.reader = reader;
		// this.exQueryTermSet = exQueryTermSet;
		this.length = length;
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}

	@Override
	public void collect(int doc) throws IOException {
		int docNum = doc + docBase;
		boolean hasLogIdInResult = false;
		// get the log id
		String strLogID = reader.document(docNum).get(
				LengthIndexDocument.FIELDLOGID);
		if (strLogID == null) {
			return;
		}
		for (LogQueryResult lqResult : queryResult) {
			if (lqResult.getLog_id().equals(strLogID)) {
				hasLogIdInResult = true;
				break;
			}
		}
		if (!hasLogIdInResult) {
			queryResult.add(new LogQueryResult(strLogID, length));
		}
		hasLogIdInResult = false;
	}

	@Override
	public void setNextReader(IndexReader reader, int docBase)
			throws IOException {
		this.docBase = docBase;
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;
	}

}
