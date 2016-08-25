package com.chinamobile.bpmspace.core.repository.index.petrinetindex.tarindex;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

import com.chinamobile.bpmspace.core.repository.index.ProcessQueryResult;

public class TARsQueryResultCollector extends Collector {
	private int docBase;
	private Scorer scorer;
	private IndexReader reader;

	// the query term set has already been expanded with its similar ones
	private HashSet<HashSet<String>> exQueryTermSet;
	private float similarity;
	private TreeSet<ProcessQueryResult> queryResult = new TreeSet<ProcessQueryResult>();

	public TARsQueryResultCollector(IndexReader reader,
			HashSet<HashSet<String>> exQueryTermSet, float similarity) {
		this.reader = reader;
		this.exQueryTermSet = exQueryTermSet;
		this.similarity = similarity;
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
				PetriNetTARsDocument.FIELDPROCESSID);
		if (strProcessID == null) {
			return;
		}

		// get the term set from current doc
		TermFreqVector termFreqVector = reader.getTermFreqVector(docNum,
				PetriNetTARsDocument.FIELDTARS);
		HashSet<String> docTermSet = new HashSet<String>();
		for (String str : termFreqVector.getTerms()) {
			docTermSet.add(str);
		}

		// calculate the tar similarity
		float numerator = 0;
		Iterator<HashSet<String>> itQuery = exQueryTermSet.iterator();
		while (itQuery.hasNext()) {
			HashSet<String> simTars = itQuery.next();
			Iterator<String> itTar = simTars.iterator();
			while (itTar.hasNext()) {
				String tar = itTar.next();
				if (docTermSet.contains(tar)) {
					numerator++;
					break;
				}
			}
		}

		float denominator = docTermSet.size() + exQueryTermSet.size()
				- numerator;

		float score = numerator / denominator;

		if (score >= this.similarity) {
			queryResult.add(new ProcessQueryResult(strProcessID, score));
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
	public TreeSet<ProcessQueryResult> getQueryResult() {
		return queryResult;
	}

}
