package com.chinamobile.bpmspace.core.repository.index.labelindex;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.apache.lucene.wordnet.SynonymMap;
import org.tartarus.snowball.ext.EnglishStemmer;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.github.mongoutils.collections.DBObjectSerializer;
import com.github.mongoutils.collections.MongoConcurrentMap;
import com.github.mongoutils.collections.SimpleFieldDBObjectSerializer;
import com.github.mongoutils.lucene.MapDirectory;
import com.github.mongoutils.lucene.MapDirectoryEntry;
import com.github.mongoutils.lucene.MapDirectoryEntrySerializer;
import com.mongodb.DBCollection;

public class LabelIndex {
	private IndexWriter indexWriter = null;

	private Analyzer analyzer = null;
	private Directory indexDir = null;
	private EnglishStemmer stemer = null;
	private String INDEX_DIR = "";

	public LabelIndex(String parentDir) {
		INDEX_DIR = parentDir + "labelIndex";

		analyzer = new SnowballAnalyzer(Version.LUCENE_33, "English",
				StandardAnalyzer.STOP_WORDS_SET);
		stemer = new EnglishStemmer();

		try {
			DBCollection dbCollection = null;
			if (MongoAccess.MONGO.collectionExists(INDEX_DIR)) {
				dbCollection = MongoAccess.MONGO.getCollection(INDEX_DIR);
			} else {
				dbCollection = MongoAccess.MONGO.createCollection(INDEX_DIR);
			}
			// serializers + map-store
			DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>(
					"key");
			DBObjectSerializer<MapDirectoryEntry> valueSerializer = new MapDirectoryEntrySerializer(
					"value");
			ConcurrentMap<String, MapDirectoryEntry> store = new MongoConcurrentMap<String, MapDirectoryEntry>(
					dbCollection, keySerializer, valueSerializer);
			indexDir = new MapDirectory(store);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean open() {
		try {
			if (indexWriter != null) {
				indexWriter.close();
			}

			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36,
					analyzer);

			indexWriter = new IndexWriter(this.indexDir, iwc);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean create() {
		return true;
	}

	public void close() {
		try {
			if (indexWriter != null) {
				indexWriter.optimize();
				indexWriter.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
		close();
		MongoAccess.MONGO.dropCollection(INDEX_DIR);
	}

	// check whether the label is contained in this index
	public boolean contain(String label) {
		try {
			IndexReader reader = IndexReader.open(indexDir); // IndexReader.open(this.indexDir,
																// true);
			IndexSearcher searcher = new IndexSearcher(reader);
			// use the boolean query
			HashSet<String> queryTermSet = new HashSet<String>();
			TokenStream stream = analyzer.tokenStream(
					LabelDocument.FIELD_LABEL, new StringReader(label));
			CharTermAttribute charTermAttribute = stream
					.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) {
				queryTermSet.add(charTermAttribute.toString());
				// queryTermSet.add(stream.)
			}
			stream.end();
			stream.close();

			// construct the query
			BooleanQuery bq = new BooleanQuery();
			Iterator<String> it = queryTermSet.iterator();
			while (it.hasNext()) {
				String s = it.next();
				Term term = new Term(LabelDocument.FIELD_LABEL, s);
				TermQuery termQuery = new TermQuery(term);
				bq.add(termQuery, Occur.MUST);
			}

			ExactLabelQueryResultCollector collector = new ExactLabelQueryResultCollector(
					reader, label);
			searcher.search(bq, collector);
			boolean ret = collector.isExistQueryLabel();
			reader.close();
			return ret;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean addLabel(String label) {
		try {
			label = label.trim();
			if (contain(label)) {
				return false;
			}
			Document doc = LabelDocument.Document(label);
			indexWriter.addDocument(doc);
			indexWriter.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void deleteLabel() {
		System.out.println("unsupported now");
	}

	public TreeSet<SimilarLabelQueryResult> getSimilarLabels(String query,
			float similarity) {
		TreeSet<SimilarLabelQueryResult> ret = new TreeSet<SimilarLabelQueryResult>();
		if (query == null) {
			ret.add(new SimilarLabelQueryResult(null, 1));
			return ret;
		}
		try {
			IndexReader reader = IndexReader.open(this.indexDir);
			IndexSearcher searcher = new IndexSearcher(reader);

			// get terms from query
			HashSet<String> queryTermSet = new HashSet<String>();
			TokenStream stream = analyzer.tokenStream(
					LabelDocument.FIELD_LABEL, new StringReader(query));
			CharTermAttribute termAtt = stream
					.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) {
				queryTermSet.add(termAtt.toString());
			}
			stream.end();
			stream.close();

			// construct the query
			BooleanQuery bq = new BooleanQuery();
			Iterator<String> it = queryTermSet.iterator();
			SynonymMap synMap = SynonymIndex.getSynonymMap();
			HashSet<String> expandedQueryTermSet = new HashSet<String>(
					queryTermSet);

			while (it.hasNext()) {
				String s = it.next();
				Term term = new Term(LabelDocument.FIELD_LABEL, s);
				TermQuery termQuery = new TermQuery(term);
				bq.add(termQuery, Occur.SHOULD);
				// expand using synonyms
				for (String syn : synMap.getSynonyms(s)) {
					stemer.setCurrent(syn);
					stemer.stem();
					syn = stemer.getCurrent();
					if (expandedQueryTermSet.add(syn)) {
						term = new Term(LabelDocument.FIELD_LABEL, syn);
						termQuery = new TermQuery(term);
						bq.add(termQuery, Occur.SHOULD);
					}
				}
			}

			// search in the label index
			SimilarLabelQueryResultCollector collector = new SimilarLabelQueryResultCollector(
					reader, queryTermSet, similarity);
			searcher.search(bq, collector);
			ret = collector.getQueryResult();
			searcher.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public float getStorageSizeInMB() {

		return 0;
	}

}
