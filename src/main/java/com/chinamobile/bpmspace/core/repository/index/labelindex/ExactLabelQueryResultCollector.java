package com.chinamobile.bpmspace.core.repository.index.labelindex;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

public class ExactLabelQueryResultCollector extends Collector {
	private int docBase;
	private Scorer scorer;
	private IndexReader reader;
	private String queryLabel;
	private boolean existQueryLabel = false;

	public ExactLabelQueryResultCollector(IndexReader reader, String queryLabel) {
		this.reader = reader;
		this.queryLabel = queryLabel;
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}

	@Override
	public void collect(int doc) throws IOException {
		int docNum = doc + docBase;
		String docLabel = reader.document(docNum)
				.get(LabelDocument.FIELD_LABEL);
		if (queryLabel.equals(docLabel)) {
			this.existQueryLabel = true;
		}
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
	 * @return the existQueryLabel
	 */
	public boolean isExistQueryLabel() {
		return existQueryLabel;
	}

}
