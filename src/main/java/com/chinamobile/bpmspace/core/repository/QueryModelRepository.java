package com.chinamobile.bpmspace.core.repository;

import java.util.TreeSet;

import org.json.JSONArray;

import com.chinamobile.bpmspace.core.data.IndexMongoAccess;
import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.index.IndexDescriptor;
import com.chinamobile.bpmspace.core.domain.index.IndexState;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.index.ModelIndex;
import com.chinamobile.bpmspace.core.repository.index.ProcessQueryResult;
import com.chinamobile.bpmspace.core.util.ClassLoadUtil;

public class QueryModelRepository {

	/**
	 * get result by query
	 * 
	 * @param indexId
	 * @param query
	 * @return
	 * @throws BasicException
	 */
	public JSONArray queryByQueryLanguage(String indexId, String query)
			throws BasicException {
		IndexMongoAccess ima = new IndexMongoAccess();
		IndexDescriptor indexDesc = ima.getIndexDescriptorByID(indexId);

		if (indexDesc == null) {
			throw new BasicException("Index with id(" + indexId
					+ ") doesn't exist!");
		}

		if (indexDesc.getState() != IndexState.START) {
			throw new BasicException("Index with id(" + indexId
					+ ") is not active!");
		}

		TreeSet<ProcessQueryResult> modelList = null;
		try {
			ModelIndex index = (ModelIndex) ClassLoadUtil.loadIndexInstance("",
					indexDesc.getClass_name());
			index.open();
			modelList = index.getProcessModels(query, 0.5f);
			index.close();
		} catch (Exception e) {
			throw new BasicException(e.getMessage());
		}

		MongoAccess ma = new MongoAccess();
		JSONArray ja = new JSONArray();
		for (ProcessQueryResult pqr : modelList) {

			Model model = ma.getModelById(pqr.getProcess_id());
			Process pro = ma.getProcessById(model.getProcessId());

			JSONArray j = new JSONArray();

			j.put(pro.getName());
			j.put(pro.getType().toString());
			j.put(model.getCreateTime().toString());
			j.put(model.getCreateTime().toString());
			j.put(model.getSize() + "kb");
			j.put(model.getCreatorName());
			ja.put(j);

		}

		return ja;
	}

	/**
	 * 
	 * @param indexId
	 * @param queryModel
	 * @return
	 * @throws BasicException
	 */
	public JSONArray queryByModel(String indexId, Model queryModel)
			throws BasicException {
		IndexMongoAccess ima = new IndexMongoAccess();
		IndexDescriptor indexDesc = ima.getIndexDescriptorByID(indexId);

		if (indexDesc == null) {
			throw new BasicException("Index with id(" + indexId
					+ ") doesn't exist!");
		}

		if (indexDesc.getState() != IndexState.START) {
			throw new BasicException("Index with id(" + indexId
					+ ") is not active!");
		}

		TreeSet<ProcessQueryResult> modelList = null;
		try {
			ModelIndex index = (ModelIndex) ClassLoadUtil.loadIndexInstance("",
					indexDesc.getClass_name());
			index.open();
			modelList = index.getProcessModels(queryModel, 0.5f);
			index.close();
		} catch (Exception e) {
			throw new BasicException(e.getMessage());
		}

		MongoAccess ma = new MongoAccess();
		JSONArray ja = new JSONArray();
		for (ProcessQueryResult pqr : modelList) {

			Model model = ma.getModelById(pqr.getProcess_id());
			Process pro = ma.getProcessById(model.getProcessId());

			JSONArray j = new JSONArray();

			j.put(pro.getName());
			j.put(pro.getType().toString());
			j.put(model.getCreateTime().toString());
			j.put(model.getCreateTime().toString());
			j.put(model.getSize() + "kb");
			j.put(model.getCreatorName());
			ja.put(j);

		}

		return ja;
	}

}
