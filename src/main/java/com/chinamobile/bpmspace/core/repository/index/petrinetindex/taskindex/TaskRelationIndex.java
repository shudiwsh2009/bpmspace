package com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskindex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Transition;

import cn.edu.thss.iise.beehivez.server.parameter.GlobalParameter;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.index.IndexForType;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.repository.index.ModelIndex;
import com.chinamobile.bpmspace.core.repository.index.ProcessQueryResult;
import com.chinamobile.bpmspace.core.repository.index.labelindex.LabelIndex;
import com.chinamobile.bpmspace.core.repository.index.query.queryparser.QueryParser;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.MongoConcurrentMap;
import com.github.mongoutils.collections.SimpleFieldDBObjectSerializer;
import com.github.mongoutils.lucene.MapDirectory;
import com.github.mongoutils.lucene.MapDirectoryEntry;
import com.github.mongoutils.lucene.MapDirectoryEntrySerializer;
import com.mongodb.DBCollection;

public class TaskRelationIndex implements ModelIndex {

	private static final String indexDirectory = "TaskRelationIndex";
	private IndexWriter indexWriter = null;
	private LabelIndex labelIndex = null;

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

			Document doc = TransitionRelationDocument.Document(pn, pno.getId());

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
	public void close() {
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
	public boolean destroy() {
		MongoAccess.MONGO.dropCollection(indexDirectory);
		return true;
	}

	@Override
	public TreeSet<ProcessQueryResult> getProcessModels(Object o,
			float similarity) {
		TreeSet<ProcessQueryResult> ret = new TreeSet<ProcessQueryResult>();

		try {
			if (o instanceof String) {
				String query = (String) o;

				// analyze the query
				StringReader sr = new StringReader(query);
				BufferedReader br = new BufferedReader(sr);
				QueryParser parser = new QueryParser(br);
				if (GlobalParameter.isEnableSimilarLabel()) {
					parser.setSemanticAide(this.labelIndex,
							GlobalParameter.getLabelSemanticSimilarity());
				}
				Query q = parser.parse();
				// System.out.println("before optimization");
				// System.out.println(q.toString());
				br.close();
				sr.close();

				// optimize the query here
				// bq = parser.optimize(bq);
				// System.out.println("after optimization");
				// System.out.println(bq.toString());

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

				RelationQueryResultCollector collector = new RelationQueryResultCollector(
						reader);
				searcher.search(q, collector);
				ret = collector.getQueryResult();
				searcher.close();
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	@Override
	public float getStorageSizeInMB() {
		return 0;
	}

	@Override
	public boolean open() {
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

			labelIndex = new LabelIndex(indexDirectory);
			labelIndex.open();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean supportGraphQuery() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportSimilarLabel() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportSimilarQuery() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportTextQuery() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public IndexForType getType() {
		// TODO Auto-generated method stub
		return IndexForType.PETRINET;
	}
}
