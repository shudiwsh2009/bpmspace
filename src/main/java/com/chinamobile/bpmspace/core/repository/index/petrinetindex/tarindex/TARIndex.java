package com.chinamobile.bpmspace.core.repository.index.petrinetindex.tarindex;

import java.io.IOException;
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

import cn.edu.thss.iise.beehivez.server.parameter.GlobalParameter;
import cn.edu.thss.iise.beehivez.server.util.PetriNetUtil;
import cn.edu.thss.iise.beehivez.server.util.TransitionLabelPair;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.index.IndexForType;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.index.ModelIndex;
import com.chinamobile.bpmspace.core.repository.index.ProcessQueryResult;
import com.chinamobile.bpmspace.core.repository.index.labelindex.LabelIndex;
import com.chinamobile.bpmspace.core.repository.index.labelindex.SimilarLabelQueryResult;
import com.chinamobile.bpmspace.core.repository.index.petriindex.pathindex.LenghthOnePathCollector;
import com.chinamobile.bpmspace.core.repository.index.petriindex.pathindex.PathLenghOneIndexDocument;
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

public class TARIndex implements ModelIndex {
	private static final String indexDirectory = "TARIndex";
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

			Document doc = PetriNetTARsDocument.Document(pn, pno.getId());

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

		try {
			if (o instanceof Model) {

				PetriNet pn = FileUtil.readPetriNetFromString(((Model) o)
						.getXmlFilename());

				BooleanQuery bq = new BooleanQuery();
				bq.setMaxClauseCount(Integer.MAX_VALUE);

				// make it sure that every queryterm is unique
				HashSet<String> expandedTars = new HashSet();

				// expand the query tars with their similar ones
				HashSet<HashSet<String>> exQueryTars = new HashSet<HashSet<String>>();

				// calculate the tars
				Iterator<TransitionLabelPair> itTAR = PetriNetUtil
						.getTARSFromPetriNetByCFP(pn).iterator();

				if (GlobalParameter.isEnableSimilarLabel()) {
					// label similarity is enabled
					while (itTAR.hasNext()) {
						TransitionLabelPair tlp = itTAR.next();
						String tarString = tlp.getFirst().trim()
								+ PetriNetTARsDocument.TARCONNECTOR
								+ tlp.getSecond().trim();

						HashSet<String> similarTars = new HashSet<String>();

						TreeSet<SimilarLabelQueryResult> pres = labelIndex
								.getSimilarLabels(tlp.getFirst().trim(),
										GlobalParameter
												.getLabelSemanticSimilarity());

						TreeSet<SimilarLabelQueryResult> sucs = labelIndex
								.getSimilarLabels(tlp.getSecond().trim(),
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
								String tar = pre
										+ PetriNetTARsDocument.TARCONNECTOR
										+ suc;
								if (similarTars.add(tar)) {
									if (expandedTars.add(tar)) {
										Term term = new Term(
												PetriNetTARsDocument.FIELDTARS,
												tar);
										TermQuery termQuery = new TermQuery(
												term);
										bq.add(termQuery,
												BooleanClause.Occur.SHOULD);
									}
								}
							}
						}

						if (similarTars.size() == 0) {
							similarTars.add(tarString);
						}
						exQueryTars.add(similarTars);
					}
				} else {
					// label similarity is not enabled
					while (itTAR.hasNext()) {
						TransitionLabelPair tlp = itTAR.next();
						String tarString = tlp.getFirst().trim()
								+ PetriNetTARsDocument.TARCONNECTOR
								+ tlp.getSecond().trim();

						HashSet<String> similarTars = new HashSet<String>();
						similarTars.add(tarString);

						if (expandedTars.add(tarString)) {
							Term term = new Term(
									PetriNetTARsDocument.FIELDTARS, tarString);
							TermQuery termQuery = new TermQuery(term);
							bq.add(termQuery, BooleanClause.Occur.SHOULD);
						}
						exQueryTars.add(similarTars);
					}
				}

				// TermQuery termQuery = new TermQuery(new
				// Term(TransitionRelationDocument.fieldProcessID,modelId));

				//
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

				//
				TARsQueryResultCollector collector = new TARsQueryResultCollector(
						reader, exQueryTars, similarity);
				searcher.search(bq, collector);
				ret = collector.getQueryResult();
				searcher.close();
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	private TreeSet<ProcessQueryResult> query(PetriNet pn) {
		TreeSet<ProcessQueryResult> ret = new TreeSet<ProcessQueryResult>();
		BooleanQuery bq = new BooleanQuery();

		try {
			if (!GlobalParameter.isEnableSimilarLabel()) {
				HashSet<String> unique = new HashSet<String>();
				for (Transition t : pn.getTransitions()) {
					String path = t.getIdentifier();
					if (unique.add(path)) {
						Term term = new Term(
								PathLenghOneIndexDocument.fieldPath, path);
						bq.add(new TermQuery(term), BooleanClause.Occur.MUST);
					}
				}
			} else {
				// enable similar label
				HashSet<String> unique = new HashSet<String>();
				for (Transition t : pn.getTransitions()) {
					String label = t.getIdentifier();
					if (unique.add(label)) {
						HashSet<String> labels = new HashSet<String>();
						// labels.add(label);
						TreeSet<SimilarLabelQueryResult> simLabels = labelIndex
								.getSimilarLabels(label, GlobalParameter
										.getLabelSemanticSimilarity());
						Iterator<SimilarLabelQueryResult> it = simLabels
								.iterator();
						while (it.hasNext()) {
							label = it.next().getLabel();
							labels.add(label);
						}
						if (labels.size() == 0) {
							Term term = new Term(
									PathLenghOneIndexDocument.fieldPath, label);
							bq.add(new TermQuery(term),
									BooleanClause.Occur.MUST);
						} else if (labels.size() == 1) {
							Term term = new Term(
									PathLenghOneIndexDocument.fieldPath, label);
							bq.add(new TermQuery(term),
									BooleanClause.Occur.MUST);
						} else {
							BooleanQuery subQuery = new BooleanQuery();
							Iterator<String> itLabel = labels.iterator();
							while (itLabel.hasNext()) {
								Term term = new Term(
										PathLenghOneIndexDocument.fieldPath,
										itLabel.next());
								subQuery.add(new TermQuery(term),
										BooleanClause.Occur.SHOULD);
							}
							bq.add(subQuery, BooleanClause.Occur.MUST);
						}
					}
				}
			}// end of query
				// query the lucene
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

			LenghthOnePathCollector collector = new LenghthOnePathCollector(
					reader, pn);
			searcher.search(bq, collector);
			ret = collector.getQueryResult();
			searcher.close();
			reader.close();

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
