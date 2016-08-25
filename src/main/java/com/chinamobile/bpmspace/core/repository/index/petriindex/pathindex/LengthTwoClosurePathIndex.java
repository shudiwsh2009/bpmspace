package com.chinamobile.bpmspace.core.repository.index.petriindex.pathindex;

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
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.Transition;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.index.IndexForType;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.index.ModelIndex;
import com.chinamobile.bpmspace.core.repository.index.ProcessQueryResult;
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

public class LengthTwoClosurePathIndex implements ModelIndex {
	private static final String indexDirectory2 = "LengthTwoClosurePathIndex";
	private static final String indexDirectory21 = "LengthTwoOneClosurePathIndex";

	private IndexWriter indexWriter2 = null;
	private IndexWriter indexWriter21 = null;

	@Override
	public IndexForType getType() {
		return IndexForType.PETRINET;
	}

	@Override
	public boolean open() throws BasicException {
		try {
			if (indexWriter2 != null) {
				indexWriter2.close();
			}
			DBCollection dbCollection2 = null;
			if (MongoAccess.MONGO.collectionExists(indexDirectory2)) {
				dbCollection2 = MongoAccess.MONGO
						.getCollection(indexDirectory2);
			} else {
				dbCollection2 = MongoAccess.MONGO
						.createCollection(indexDirectory2);
			}
			DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>(
					"key");
			DBObjectSerializer<MapDirectoryEntry> valueSerializer = new MapDirectoryEntrySerializer(
					"value");
			ConcurrentMap<String, MapDirectoryEntry> store = new MongoConcurrentMap<String, MapDirectoryEntry>(
					dbCollection2, keySerializer, valueSerializer);
			MapDirectory indexDir2 = new MapDirectory(store);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36,
					new SemicolonAnalyzer());
			indexWriter2 = new IndexWriter(indexDir2, iwc);

			if (indexWriter21 != null) {
				indexWriter21.close();
			}
			DBCollection dbCollection21 = null;
			if (MongoAccess.MONGO.collectionExists(indexDirectory21)) {
				dbCollection21 = MongoAccess.MONGO
						.getCollection(indexDirectory21);
			} else {
				dbCollection21 = MongoAccess.MONGO
						.createCollection(indexDirectory21);
			}
			DBObjectSerializer<String> keySerializer21 = new SimpleFieldDBObjectSerializer<String>(
					"key");
			DBObjectSerializer<MapDirectoryEntry> valueSerializer21 = new MapDirectoryEntrySerializer(
					"value");
			ConcurrentMap<String, MapDirectoryEntry> store21 = new MongoConcurrentMap<String, MapDirectoryEntry>(
					dbCollection21, keySerializer21, valueSerializer21);
			MapDirectory indexDir21 = new MapDirectory(store21);
			IndexWriterConfig iwc21 = new IndexWriterConfig(Version.LUCENE_36,
					new SemicolonAnalyzer());
			indexWriter21 = new IndexWriter(indexDir21, iwc21);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void close() throws BasicException {
		try {
			if (indexWriter2 != null) {
				indexWriter2.close();
			}
			if (indexWriter21 != null) {
				indexWriter21.close();
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
		MongoAccess.MONGO.dropCollection(indexDirectory2);
		MongoAccess.MONGO.dropCollection(indexDirectory21);
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

			// length 1
			Document doc21 = PathLenghOneIndexDocument
					.Document(pn, pno.getId());
			indexWriter21.addDocument(doc21);
			indexWriter21.commit();
			indexWriter21.close();

			// length 2
			Document doc2 = PathLengthTwoClosureDocument.Document(pn,
					pno.getId());
			indexWriter2.addDocument(doc2);
			indexWriter2.commit();
			indexWriter2.close();
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
			indexWriter2.deleteDocuments(termQuery);
			indexWriter21.deleteDocuments(termQuery);
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
		if (o instanceof Model) {
			PetriNet pn = FileUtil.readPetriNetFromString(((Model) o)
					.getXmlFilename());
			TreeSet<ProcessQueryResult> ret2 = query2(pn);
			if (ret2.size() < 1) {
				ret = query21(pn);
			} else {
				TreeSet<ProcessQueryResult> ret21 = query21(pn);
				if (ret21.size() > 1) {
					ret2.retainAll(ret21);
				}
				ret = ret2;
			}
		}
		return ret;
	}

	private TreeSet<ProcessQueryResult> query2(PetriNet pn) {
		TreeSet<ProcessQueryResult> ret = new TreeSet<ProcessQueryResult>();

		try {
			ArrayList<Transition> vT = (ArrayList<Transition>) pn
					.getTransitions().clone();
			BooleanQuery bq = new BooleanQuery();
			for (Place p : pn.getPlaces()) {
				Iterator it1 = p.getPredecessors().iterator();
				while (it1.hasNext()) {
					Transition t1 = (Transition) it1.next();
					Iterator it2 = p.getSuccessors().iterator();
					while (it2.hasNext()) {
						Transition t2 = (Transition) it2.next();
						Term term = new Term(
								PathLengthTwoClosureDocument.fieldPath, t1
										.getIdentifier().trim()
										+ "->"
										+ t2.getIdentifier().trim());
						bq.add(new TermQuery(term), BooleanClause.Occur.SHOULD);
						vT.remove(t1);
						vT.remove(t2);
					}
				}
			}
			// query the lucene
			DBCollection dbCollection = MongoAccess.MONGO
					.getCollection(indexDirectory2);
			DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>(
					"key");
			DBObjectSerializer<MapDirectoryEntry> valueSerializer = new MapDirectoryEntrySerializer(
					"value");
			ConcurrentMap<String, MapDirectoryEntry> store = new MongoConcurrentMap<String, MapDirectoryEntry>(
					dbCollection, keySerializer, valueSerializer);
			MapDirectory indexDir = new MapDirectory(store);

			IndexReader reader = IndexReader.open(indexDir);
			IndexSearcher searcher = new IndexSearcher(reader);

			LengthTwoClosurePathCollector collector = new LengthTwoClosurePathCollector(
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

	private TreeSet<ProcessQueryResult> query21(PetriNet pn) {
		TreeSet<ProcessQueryResult> ret = new TreeSet<ProcessQueryResult>();

		try {
			ArrayList<Transition> vT = (ArrayList<Transition>) pn
					.getTransitions().clone();
			BooleanQuery bq = new BooleanQuery();
			HashSet<String> unique = new HashSet<String>();

			for (Transition t : pn.getTransitions()) {
				String path = t.getIdentifier();
				if (unique.add(path)) {
					Term term = new Term(PathLenghOneIndexDocument.fieldPath,
							path);
					bq.add(new TermQuery(term), BooleanClause.Occur.MUST);
				}
			}

			// query the lucene
			DBCollection dbCollection = MongoAccess.MONGO
					.getCollection(indexDirectory21);
			DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>(
					"key");
			DBObjectSerializer<MapDirectoryEntry> valueSerializer = new MapDirectoryEntrySerializer(
					"value");
			ConcurrentMap<String, MapDirectoryEntry> store = new MongoConcurrentMap<String, MapDirectoryEntry>(
					dbCollection, keySerializer, valueSerializer);
			MapDirectory indexDir = new MapDirectory(store);

			IndexReader reader = IndexReader.open(indexDir);
			IndexSearcher searcher = new IndexSearcher(reader);

			LengthTwoClosurePathCollector collector = new LengthTwoClosurePathCollector(
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
	public boolean supportSimilarQuery() {
		return false;
	}

	@Override
	public boolean supportSimilarLabel() {
		return false;
	}

	@Override
	public boolean supportTextQuery() {
		return false;
	}

	@Override
	public boolean supportGraphQuery() {
		return true;
	}

	@Override
	public float getStorageSizeInMB() {
		// TODO Auto-generated method stub
		return 0;
	}

}
