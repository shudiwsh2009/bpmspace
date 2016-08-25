package com.chinamobile.bpmspace.core.repository.index.logindex.lengthindex;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.index.IndexForType;
import com.chinamobile.bpmspace.core.domain.log.Log;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.index.CaseIndex;
import com.chinamobile.bpmspace.core.repository.index.LogQueryResult;
import com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskindex.SemicolonAnalyzer;
import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.MongoConcurrentMap;
import com.github.mongoutils.collections.SimpleFieldDBObjectSerializer;
import com.github.mongoutils.lucene.MapDirectory;
import com.github.mongoutils.lucene.MapDirectoryEntry;
import com.github.mongoutils.lucene.MapDirectoryEntrySerializer;
import com.mongodb.DBCollection;

public class LengthIndex implements CaseIndex {
	private static final String indexDirectory = "LengthIndex";
	private IndexWriter indexWriter = null;
	private MongoAccess ma = new MongoAccess();

	@Override
	public IndexForType getType() {
		// TODO Auto-generated method stub
		return IndexForType.LOG;
	}

	@Override
	public boolean open() throws BasicException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		try {
			if (indexWriter != null) {
				indexWriter.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// labelIndex.close();
	}

	@Override
	public boolean create() {
		// TODO Auto-generated method stub
		try {
			List<Log> logList = ma.findAllLogs();
			// long totalCount = ma.countLogNumber();
			try {
				this.open();
			} catch (BasicException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (Log log : logList) {
				if (log.getIndexFlag(0) == null) {
					try {
						for (int i = 0; i < log.getLengthList().size(); i++) {
							Document doc = LengthIndexDocument.Document(log,
									log.getLengthList().get(i), log.getId());

							indexWriter.addDocument(doc);
							indexWriter.commit();

						}
						log.setIndexFlagTrue(0);
						ma.saveLog(log);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			indexWriter.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean destroy() {
		// TODO Auto-generated method stub
		try {
			MongoAccess.MONGO.dropCollection(indexDirectory);
			List<Log> logList = ma.findAllLogs();
			for (Log log : logList) {
				if (log.getIndexFlag(0) != null) {

					log.setIndexFlagFalse(0);
					ma.saveLog(log);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void addInstance(Object o) {
		try {
			this.open();
		} catch (BasicException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO Auto-generated method stub
		Log log = (Log) o;
		if (log.getIndexFlag(0) == null) {
			try {
				for (int i = 0; i < log.getLengthList().size(); i++) {
					Document doc = LengthIndexDocument.Document(log, log
							.getLengthList().get(i), log.getId());

					indexWriter.addDocument(doc);
					indexWriter.commit();

				}

				indexWriter.close();
				log.setIndexFlagTrue(0);
				ma.saveLog(log);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void del(Object o) {
		// TODO Auto-generated method stub
		Log log = (Log) o;
		String logId = log.getId();
		TermQuery termQuery = new TermQuery(new Term("log_id", logId));
		try {
			indexWriter.deleteDocuments(termQuery);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean supportSimilarQuery() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportSimilarLabel() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportTextQuery() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean supportGraphQuery() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<LogQueryResult> getLogsLTE(Object o, int start, int end) {
		List<LogQueryResult> ret = null;

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
			IndexReader reader = IndexReader.open(indexDir);
			IndexSearcher searcher = new IndexSearcher(reader);

			Query queryN = NumericRangeQuery.newIntRange(
					LengthIndexDocument.FIELDLENS, 1, end, true, true);

			LengthQueryResultCollector collector = new LengthQueryResultCollector(
					reader, start);
			searcher.search(queryN, collector);
			ret = collector.getQueryResult();
			searcher.close();
			reader.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ret;
	}

	public List<LogQueryResult> getLogsGTE(Object o, int start, int end) {
		List<LogQueryResult> ret = null;

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
			IndexReader reader = IndexReader.open(indexDir);
			IndexSearcher searcher = new IndexSearcher(reader);

			Query queryN = NumericRangeQuery.newIntRange(
					LengthIndexDocument.FIELDLENS, start, Integer.MAX_VALUE,
					true, true);

			LengthQueryResultCollector collector = new LengthQueryResultCollector(
					reader, start);
			searcher.search(queryN, collector);
			ret = collector.getQueryResult();
			searcher.close();
			reader.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ret;
	}

	public List<LogQueryResult> getLogsBetween(Object o, int start, int end) {
		List<LogQueryResult> ret = null;

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
			IndexReader reader = IndexReader.open(indexDir);
			IndexSearcher searcher = new IndexSearcher(reader);

			Query queryN = NumericRangeQuery.newIntRange(
					LengthIndexDocument.FIELDLENS, start, end, true, true);

			LengthQueryResultCollector collector = new LengthQueryResultCollector(
					reader, start);
			searcher.search(queryN, collector);
			ret = collector.getQueryResult();
			searcher.close();
			reader.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ret;
	}

	@Override
	public List<LogQueryResult> getLogs(Object o, int length) {
		List<LogQueryResult> ret = null;
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
			IndexReader reader = IndexReader.open(indexDir);
			IndexSearcher searcher = new IndexSearcher(reader);

			Query querOne = NumericRangeQuery.newIntRange(
					LengthIndexDocument.FIELDLENS, length, length, true, true);

			LengthQueryResultCollector collector = new LengthQueryResultCollector(
					reader, length);
			searcher.search(querOne, collector);
			ret = collector.getQueryResult();
			searcher.close();
			reader.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ret;
	}

	@Override
	public List<LogQueryResult> getLogs(Object o, String eventName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LogQueryResult> getLogs(String eventName,
			String _adjacentEventName) {
		// TODO Auto-generated method stub
		return null;
	}

}
