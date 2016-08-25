package com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskindex;

import java.io.IOException;
import java.util.TreeSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

import com.chinamobile.bpmspace.core.repository.index.ProcessQueryResult;

public class RelationQueryResultCollector extends Collector {
	private int docBase;
	private Scorer scorer;
	private IndexReader reader;

	private TreeSet<ProcessQueryResult> queryResult = new TreeSet<ProcessQueryResult>();

	public RelationQueryResultCollector(IndexReader reader) {
		this.reader = reader;
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}

	@Override
	public void collect(int doc) throws IOException {
		int docNum = doc + docBase;

		// get the process id
		String strProcessID = reader.document(docNum).get(
				TransitionRelationDocument.fieldProcessID);
		if (strProcessID == null) {
			return;
		}
		queryResult.add(new ProcessQueryResult(strProcessID, 1));
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

	/**
	 * @return the queryResult
	 */
	public TreeSet<ProcessQueryResult> getQueryResult() {
		return queryResult;
	}

}
