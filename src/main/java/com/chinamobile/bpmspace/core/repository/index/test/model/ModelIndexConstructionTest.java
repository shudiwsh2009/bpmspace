package com.chinamobile.bpmspace.core.repository.index.test.model;

import java.io.ByteArrayInputStream;
import java.util.TreeSet;

import org.processmining.framework.models.petrinet.PetriNet;

import cn.edu.thss.iise.beehivez.server.generator.petrinet.MurataGenerator;
import cn.edu.thss.iise.beehivez.server.util.PetriNetUtil;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.domain.process.ProcessCatalog;
import com.chinamobile.bpmspace.core.domain.process.ProcessRevision;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.repository.index.ProcessQueryResult;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ModelIndexConstructionTest {

	private static String _name = "testProcess";
	private static String _description = "testDescription";
	private static ProcessType _type = ProcessType.PETRINET;
	private static String jsonContent = "json";
	private static String svgContent = "svg";
	private static double size = 1.2;

	private MongoAccess mongo = null;
	private ProcessCatalog catalog = null;
	private User user = null;
	private String _catalogId;
	private String _ownerId;

	public ModelIndexConstructionTest(String _catalogId, String _ownerId) {
		mongo = new MongoAccess();
		this._catalogId = _catalogId;
		this._ownerId = _ownerId;
		catalog = mongo.getProcessCatalogById(_catalogId);
		user = mongo.getUserById(_ownerId);

	}

	public ModelIndexConstructionTest() {
		mongo = new MongoAccess();
	}

	/**
	 * Randomly generate @param modelNumber models, add index
	 * 
	 * @param modelNumber
	 */
	public void TestModelIndexConstruction(long modelNumber,
			int minTransitionsPerNet, int maxTransitionsPerNet,
			int maxTransitionNameLength) {

		String colName = "IndexConstructionTest";
		if (MongoAccess.MONGO.collectionExists(colName)) {
			// MongoAccess.MONGO.dropCollection(colName);
		} else {
			MongoAccess.MONGO.createCollection(colName);
		}

		try {
			MurataGenerator generator = new MurataGenerator();
			TaskEdgeIndexForTest index = new TaskEdgeIndexForTest();
			index.open();
			long start, end;
			double accIndexInsertTime = 0.0;
			double accModelInsertTime = 0.0;
			double modelInsertTime;

			for (int count = 0; count < modelNumber; count++) {
				ModelIndexConstructionStatisticsItem item = new ModelIndexConstructionStatisticsItem();

				// 1.randomly generate model
				PetriNet pn = generator.generateModel(minTransitionsPerNet,
						maxTransitionsPerNet, -1, maxTransitionNameLength);
				String xmlContent = new String(PetriNetUtil.getPnmlBytes(pn));

				// 2.add to process database
				start = System.currentTimeMillis();
				Process newProcess = mongo.addProcess(_name, _description,
						_catalogId, _type, catalog.getRootUserId(), _ownerId,
						user.getUsername());
				String xmlFileName = FileUtil.nameGridFSFile(user.getId(),
						newProcess.getId(), 1L) + FileUtil.XML_SUFFIX;
				boolean xmlFileSave = saveFile(xmlContent, xmlFileName,
						FileUtil.CONTENT_TYPE_XML);
				Model newModel = mongo.addModel(newProcess.getId(), _ownerId,
						user.getUsername(), 1L, jsonContent, svgContent,
						xmlFileSave ? xmlFileName : "", size);
				newProcess.getRevision().put(
						newModel.getRevision(),
						new ProcessRevision(newModel.getId(), newModel
								.getCreatorId(), newModel.getCreateTime()));
				mongo.saveProcess(newProcess);
				end = System.currentTimeMillis();
				modelInsertTime = (end - start) / 1000.0;
				accModelInsertTime += modelInsertTime;
				item.accModelInsertTime = accModelInsertTime;
				item.modelInsertTime = modelInsertTime;
				item.modelId = newModel.getId();

				// 3.add index
				index.addProcessModel(newModel);
				accIndexInsertTime += index.indexInsertTime;
				item.accIndexInsertTime = accIndexInsertTime;
				item.featureExtractionTime = index.featureExtractionTime;
				item.indexInsertTime = index.indexInsertTime;

				MongoAccess.MONGO.save(item, colName);
			}

			index.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param queryNumber
	 */
	public void TestModelIndexQuery(long queryNumber) {

		String colName = "IndexQueryTest";
		if (MongoAccess.MONGO.collectionExists(colName)) {
			// MongoAccess.MONGO.dropCollection(colName);
		} else {
			MongoAccess.MONGO.createCollection(colName);
		}

		try {
			TaskEdgeIndexForTest index = new TaskEdgeIndexForTest();
			index.openReader();
			double accQueryTime = 0.0;
			double accFeatureCompareTime = 0.0;

			DBCollection coll = MongoAccess.MONGO.getCollection("model");
			if (coll.count() < queryNumber) {
				queryNumber = coll.count();
			}
			DBCursor cursor = coll.find();

			for (int count = 0; count < queryNumber; count++) {

				ModelIndexQueryStatisticsItem item = new ModelIndexQueryStatisticsItem();

				// 1. get query model
				DBObject dbObj = cursor.next();
				Model model = new Model();
				// model.setId((String)dbObj.get("_id"));

				model.setXmlFilename((String) dbObj.get("xmlFilename"));

				// 2.query using index
				TreeSet<ProcessQueryResult> rets = index.getProcessModels(
						model, 0.5f);
				accQueryTime += index.queryTime;
				accFeatureCompareTime += index.featureCompareTime;
				item.accFeatureCompareTime = accFeatureCompareTime;
				item.accQueryTime = accQueryTime;
				// item.isHit = index.isHit;
				item.isHit = true;
				item.queryTime = index.queryTime;
				item.featureCompareTime = index.featureCompareTime;
				item.retNumber = rets.size();

				MongoAccess.MONGO.save(item, colName);
			}

			index.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean saveFile(String _fileContent, String _filename,
			String _contentType) throws NoExistException {
		ByteArrayInputStream input = null;
		input = new ByteArrayInputStream(_fileContent.getBytes());
		MongoAccess.GRIDFS.store(input, _filename, _contentType);
		return true;
	}

}
