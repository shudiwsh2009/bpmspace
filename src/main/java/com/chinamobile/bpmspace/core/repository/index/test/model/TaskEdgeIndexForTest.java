package com.chinamobile.bpmspace.core.repository.index.test.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;
import org.processmining.framework.models.petrinet.PetriNet;

import cn.edu.thss.iise.beehivez.server.metric.mcessimilarity.TaskLine4PetriNet;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.index.IndexForType;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.index.ModelIndex;
import com.chinamobile.bpmspace.core.repository.index.ProcessQueryResult;
import com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskedgeindex.PetriNetTaskEdgesDocument;
import com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskedgeindex.TaskEdgesQueryResultCollector;
import com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskindex.SemicolonAnalyzer;
import com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskindex.TransitionRelationDocument;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.MongoConcurrentMap;
import com.github.mongoutils.collections.SimpleFieldDBObjectSerializer;
import com.github.mongoutils.lucene.MapDirectory;
import com.github.mongoutils.lucene.MapDirectoryEntry;
import com.github.mongoutils.lucene.MapDirectoryEntrySerializer;
import com.mongodb.DBCollection;

public class TaskEdgeIndexForTest implements ModelIndex {
	private static final String indexDirectory = "TaskEdgeIndex";
	private IndexWriter indexWriter = null;

	// for test
	IndexReader reader = null;
	IndexSearcher searcher = null;
	public double indexInsertTime = 0.0;
	public double featureExtractionTime = 0.0;

	public double queryTime = 0.0;
	public double featureCompareTime = 0.0;
	public boolean isHit = false;

	@Override
	public IndexForType getType() {
		return IndexForType.PETRINET;
	}

	@Override
	public boolean open() throws BasicException {
		try {
			if (indexWriter != null) {
				indexWriter.close();
			}
			DBCollection dbCollection = null;
			if (MongoAccess.MONGO.collectionExists(indexDirectory)) {
				dbCollection = MongoAccess.MONGO.getCollection(indexDirectory);
			} else {
				dbCollection = MongoAccess.MONGO
						.createCollection(indexDirectory);
			}
			DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>(
					"key");
			DBObjectSerializer<MapDirectoryEntry> valueSerializer = new MapDirectoryEntrySerializer(
					"value");
			ConcurrentMap<String, MapDirectoryEntry> store = new MongoConcurrentMap<String, MapDirectoryEntry>(
					dbCollection, keySerializer, valueSerializer);
			MapDirectory indexDir = new MapDirectory(store);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36,
					new SemicolonAnalyzer());
			indexWriter = new IndexWriter(indexDir, iwc);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void close() throws BasicException {
		try {
			if (indexWriter != null) {
				indexWriter.close();
			}
			if (searcher != null) {
				searcher.close();
			}
			if (reader != null) {
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean create() {
		try {
			int pageSize = 100;
			int pageNo = 1;
			int count = 0;

			MongoAccess ma = new MongoAccess();

			long totalCount = ma
					.countProcessNumberWithType(ProcessType.PETRINET);
			while (count < totalCount) {
				List<Model> modelList = ma.getModelWithType(
						ProcessType.PETRINET, pageNo, pageSize);

				for (Model model : modelList) {
					this.addProcessModel(model);
				}
				pageNo += 1;
				count += pageSize;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean destroy() {
		MongoAccess.MONGO.dropCollection(indexDirectory);
		return true;
	}

	@Override
	public void addProcessModel(Object o) {
		Model pno = (Model) o;
		long start, end;
		try {
			start = System.currentTimeMillis();
			PetriNet pn = FileUtil.readPetriNetFormFile(pno.getXmlFilename());
			if (pn == null) {
				System.out
						.println("null petri net in addPetriNet of TransitionRelationIndex");
				return;
			}
			Document doc = PetriNetTaskEdgesDocument.Document(pn, pno.getId());
			end = System.currentTimeMillis();
			this.featureExtractionTime = (end - start) / 1000.0;

			start = System.currentTimeMillis();
			indexWriter.addDocument(doc);
			indexWriter.commit();
			end = System.currentTimeMillis();
			this.indexInsertTime = (end - start) / 1000.0;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delProcessModel(Object pno) {
		Model model = (Model) pno;
		String modelId = model.getId();
		TermQuery termQuery = new TermQuery(new Term(
				TransitionRelationDocument.fieldProcessID, modelId));
		try {
			indexWriter.deleteDocuments(termQuery);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * for test
	 */
	public void openReader() {
		try {
			DBCollection dbCollection = MongoAccess.MONGO
					.getCollection(indexDirectory);
			DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>(
					"key");
			DBObjectSerializer<MapDirectoryEntry> valueSerializer = new MapDirectoryEntrySerializer(
					"value");
			ConcurrentMap<String, MapDirectoryEntry> store = new MongoConcurrentMap<String, MapDirectoryEntry>(
					dbCollection, keySerializer, valueSerializer);
			MapDirectory indexDir;
			indexDir = new MapDirectory(store);
			reader = IndexReader.open(indexDir);
			searcher = new IndexSearcher(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public TreeSet<ProcessQueryResult> getProcessModels(Object o,
			float similarity) {
		long start, end;
		TreeSet<ProcessQueryResult> ret = new TreeSet<ProcessQueryResult>();
		Model model = null;
		this.isHit = false;
		try {
			if (o instanceof Model) {
				start = System.currentTimeMillis();
				model = (Model) o;
				PetriNet pn = FileUtil.readPetriNetFormFile(model
						.getXmlFilename());
				BooleanQuery bq = new BooleanQuery();
				bq.setMaxClauseCount(Integer.MAX_VALUE);

				// make it sure that every queryterm is unique
				HashSet<String> expandedTaskEdges = new HashSet();

				// expand the query task edges with their similar ones
				HashSet<HashSet<String>> exQueryTaskEdges = new HashSet<HashSet<String>>();

				// calculate the task edges of query Petri net
				ArrayList<TaskLine4PetriNet> tls = TaskLine4PetriNet
						.getAllTaskLinesOfPetriNet(pn);

				// label similarity is not enabled
				for (TaskLine4PetriNet tl : tls) {
					String taskEdgeString = tl.getSrcTransition()
							.getIdentifier().trim()
							+ PetriNetTaskEdgesDocument.TASKEDGECONNECTOR
							+ tl.getDestTransition().getIdentifier().trim();

					HashSet<String> similarTaskEdges = new HashSet<String>();
					similarTaskEdges.add(taskEdgeString);

					if (expandedTaskEdges.add(taskEdgeString)) {
						Term term = new Term(
								PetriNetTaskEdgesDocument.FIELDTASKEDGES,
								taskEdgeString);
						TermQuery termQuery = new TermQuery(term);
						bq.add(termQuery, BooleanClause.Occur.SHOULD);
					}
					exQueryTaskEdges.add(similarTaskEdges);
				}
				end = System.currentTimeMillis();
				this.featureCompareTime = (end - start) / 1000.0;

				start = System.currentTimeMillis();
				TaskEdgesQueryResultCollector collector = new TaskEdgesQueryResultCollector(
						reader, exQueryTaskEdges, similarity);
				searcher.search(bq, collector);
				TreeSet<ProcessQueryResult> temp = collector.getQueryResult();
				end = System.currentTimeMillis();

				this.queryTime = (end - start) / 1000.0;

				/*
				 * start = System.currentTimeMillis(); // verify the candidate
				 * model Iterator<ProcessQueryResult> it = temp.iterator();
				 * MongoAccess ma = new MongoAccess(); while (it.hasNext()) {
				 * ProcessQueryResult pqr = it.next(); String id =
				 * pqr.getProcess_id(); Model pno =
				 * ma.getModelById(pqr.getProcess_id());
				 * 
				 * if(!isHit){ if(id.equals(model.getId())){ this.isHit = true;
				 * } } PetriNet c =
				 * FileUtil.readPetriNetFormFile(pno.getXmlFilename()); float
				 * mcesSimilarity = PetriNetUtil.mcesSimilarity(c, pn); if
				 * (mcesSimilarity >= similarity) { ret.add(new
				 * ProcessQueryResult(id, mcesSimilarity)); } } end =
				 * System.currentTimeMillis(); this.featureCompareTime += (end -
				 * start)/1000.0;
				 */

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	@Override
	public boolean supportGraphQuery() {
		return true;
	}

	@Override
	public boolean supportSimilarQuery() {
		return true;
	}

	@Override
	public boolean supportTextQuery() {
		return false;
	}

	@Override
	public boolean supportSimilarLabel() {
		return true;
	}

	@Override
	public float getStorageSizeInMB() {
		// TODO Auto-generated method stub
		return 0;
	}

}
