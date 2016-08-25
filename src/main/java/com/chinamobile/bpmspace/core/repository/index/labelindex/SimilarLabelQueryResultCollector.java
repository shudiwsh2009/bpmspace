package com.chinamobile.bpmspace.core.repository.index.labelindex;

import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

import cn.edu.thss.iise.beehivez.server.util.StringSimilarityUtil;

public class SimilarLabelQueryResultCollector extends Collector {
	private int docBase;
	private Scorer scorer;
	private IndexReader reader;
	private HashSet<String> queryTermSet;
	private float similarity;
	private TreeSet<SimilarLabelQueryResult> queryResult = new TreeSet<SimilarLabelQueryResult>();

	public SimilarLabelQueryResultCollector(IndexReader reader,
			HashSet<String> queryTermSet, float similarity) {
		this.reader = reader;
		this.queryTermSet = queryTermSet;
		this.similarity = similarity;
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}

	@Override
	public void collect(int doc) throws IOException {
		int docNum = doc + docBase;

		String label = reader.document(docNum).get(LabelDocument.FIELD_LABEL);
		TermFreqVector termFreqVector = reader.getTermFreqVector(docNum,
				LabelDocument.FIELD_LABEL);
		HashSet<String> docTermSet = new HashSet<String>();
		for (String str : termFreqVector.getTerms()) {
			docTermSet.add(str);
		}

		float score = StringSimilarityUtil.diceSemanticSimilarity(queryTermSet,
				docTermSet);

		if (score >= this.similarity) {
			queryResult.add(new SimilarLabelQueryResult(label, score));
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
	 * @return the queryResult
	 */
	public TreeSet<SimilarLabelQueryResult> getQueryResult() {
		return queryResult;
	}

}
