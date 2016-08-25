package com.chinamobile.bpmspace.core.repository.index.petriindex.pathindex;

import java.io.IOException;
import java.util.TreeSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;
import org.processmining.framework.models.petrinet.PetriNet;

import cn.edu.thss.iise.beehivez.server.graph.isomorphism.Ullman4PetriNet;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.repository.index.ProcessQueryResult;
import com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskindex.TransitionRelationDocument;
import com.chinamobile.bpmspace.core.util.FileUtil;

public class LenghthOnePathCollector extends Collector {
	private int docBase;
	private Scorer scorer;
	private IndexReader reader;
	private PetriNet query;
	private MongoAccess ma;

	private TreeSet<ProcessQueryResult> queryResult = new TreeSet<ProcessQueryResult>();

	public LenghthOnePathCollector(IndexReader reader, PetriNet pn) {
		this.reader = reader;
		this.query = pn;
		this.ma = new MongoAccess();
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
		Model pno = ma.getModelById(strProcessID);
		PetriNet pn = FileUtil.readPetriNetFormFile(pno.getXmlFilename());
		if (!Ullman4PetriNet.subGraphIsomorphism(query, pn)) {
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
