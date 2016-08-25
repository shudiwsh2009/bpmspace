package com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskedgeindex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
import org.processmining.framework.models.petrinet.Transition;

import cn.edu.thss.iise.beehivez.server.metric.mcessimilarity.TaskLine4PetriNet;
import cn.edu.thss.iise.beehivez.server.parameter.GlobalParameter;
import cn.edu.thss.iise.beehivez.server.util.PetriNetUtil;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.index.IndexForType;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.index.ModelIndex;
import com.chinamobile.bpmspace.core.repository.index.ProcessQueryResult;
import com.chinamobile.bpmspace.core.repository.index.labelindex.LabelIndex;
import com.chinamobile.bpmspace.core.repository.index.labelindex.SimilarLabelQueryResult;
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

public class TaskEdgeIndex implements ModelIndex {
	private static final String indexDirectory = "TaskEdgeIndex";
	private LabelIndex labelIndex = null;
	private IndexWriter indexWriter = null;

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

			this.labelIndex = new LabelIndex(indexDirectory);
			this.labelIndex.open();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		labelIndex.close();

	}

	@Override
	public boolean create() {
		try {
			labelIndex.create();

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
		try {
			// get the object of petri net and process id
			PetriNet pn = FileUtil.readPetriNetFormFile(pno.getXmlFilename());
			if (pn == null) {
				System.out
						.println("null petri net in addPetriNet of TransitionRelationIndex");
				return;
			}

			Document doc = PetriNetTaskEdgesDocument.Document(pn, pno.getId());

			indexWriter.addDocument(doc);
			indexWriter.commit();
			indexWriter.close();

			// update the label index
			for (Transition t : pn.getTransitions()) {
				labelIndex.addLabel(t.getIdentifier());
			}
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

	@Override
	public TreeSet<ProcessQueryResult> getProcessModels(Object o,
			float similarity) {

		TreeSet<ProcessQueryResult> ret = new TreeSet<ProcessQueryResult>();
		Model model = null;
		try {
			if (o instanceof Model) {

				model = (Model) o;
				PetriNet pn = FileUtil.readPetriNetFromString(model
						.getXmlFilename());

				DBCollection dbCollection = MongoAccess.MONGO
						.getCollection(indexDirectory);
				DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>(
						"key");
				DBObjectSerializer<MapDirectoryEntry> valueSerializer = new MapDirectoryEntrySerializer(
						"value");
				ConcurrentMap<String, MapDirectoryEntry> store = new MongoConcurrentMap<String, MapDirectoryEntry>(
						dbCollection, keySerializer, valueSerializer);
				MapDirectory indexDir = new MapDirectory(store);

				IndexReader reader = IndexReader.open(indexDir);
				IndexSearcher searcher = new IndexSearcher(reader);
				BooleanQuery bq = new BooleanQuery();
				bq.setMaxClauseCount(Integer.MAX_VALUE);

				// make it sure that every queryterm is unique
				HashSet<String> expandedTaskEdges = new HashSet();

				// expand the query task edges with their similar ones
				HashSet<HashSet<String>> exQueryTaskEdges = new HashSet<HashSet<String>>();

				// calculate the task edges of query Petri net
				ArrayList<TaskLine4PetriNet> tls = TaskLine4PetriNet
						.getAllTaskLinesOfPetriNet(pn);

				if (GlobalParameter.isEnableSimilarLabel()) {
					// label similarity is enabled
					for (TaskLine4PetriNet tl : tls) {
						String taskEdgeString = tl.getSrcTransition()
								.getIdentifier().trim()
								+ PetriNetTaskEdgesDocument.TASKEDGECONNECTOR
								+ tl.getDestTransition().getIdentifier().trim();

						HashSet<String> similarTaskEdges = new HashSet<String>();

						TreeSet<SimilarLabelQueryResult> pres = labelIndex
								.getSimilarLabels(tl.getSrcTransition()
										.getIdentifier().trim(),
										GlobalParameter
												.getLabelSemanticSimilarity());

						TreeSet<SimilarLabelQueryResult> sucs = labelIndex
								.getSimilarLabels(tl.getDestTransition()
										.getIdentifier().trim(),
										GlobalParameter
												.getLabelSemanticSimilarity());

						Iterator<SimilarLabelQueryResult> itPre = pres
								.iterator();
						while (itPre.hasNext()) {
							String pre = itPre.next().getLabel();
							Iterator<SimilarLabelQueryResult> itSuc = sucs
									.iterator();
							while (itSuc.hasNext()) {
								String suc = itSuc.next().getLabel();
								String taskEdge = pre
										+ PetriNetTaskEdgesDocument.TASKEDGECONNECTOR
										+ suc;
								if (similarTaskEdges.add(taskEdge)) {
									if (expandedTaskEdges.add(taskEdge)) {
										Term term = new Term(
												PetriNetTaskEdgesDocument.FIELDTASKEDGES,
												taskEdge);
										TermQuery termQuery = new TermQuery(
												term);
										bq.add(termQuery,
												BooleanClause.Occur.SHOULD);
									}
								}
							}
						}

						if (similarTaskEdges.size() == 0) {
							similarTaskEdges.add(taskEdgeString);
						}
						exQueryTaskEdges.add(similarTaskEdges);
					}
				} else {
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
				}

				TaskEdgesQueryResultCollector collector = new TaskEdgesQueryResultCollector(
						reader, exQueryTaskEdges, similarity);
				searcher.search(bq, collector);
				TreeSet<ProcessQueryResult> temp = collector.getQueryResult();
				searcher.close();
				reader.close();

				// verify the candidate model
				Iterator<ProcessQueryResult> it = temp.iterator();
				MongoAccess ma = new MongoAccess();
				while (it.hasNext()) {
					ProcessQueryResult pqr = it.next();
					String id = pqr.getProcess_id();
					Model pno = ma.getModelById(pqr.getProcess_id());

					PetriNet c = FileUtil.readPetriNetFormFile(pno
							.getXmlFilename());
					float mcesSimilarity = PetriNetUtil.mcesSimilarity(c, pn);
					if (mcesSimilarity >= similarity) {
						ret.add(new ProcessQueryResult(id, mcesSimilarity));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
