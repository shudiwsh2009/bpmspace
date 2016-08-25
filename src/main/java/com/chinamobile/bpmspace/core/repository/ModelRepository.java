package com.chinamobile.bpmspace.core.repository;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;

public class ModelRepository {
	private MongoAccess mongo = new MongoAccess();

	public Model getModel(String _modelId) throws EmptyFieldException,
			NoExistException {
		if (_modelId == null || _modelId.equals("")) {
			throw new EmptyFieldException("模型不存在");
		}
		Model model = mongo.getModelById(_modelId);
		if (model == null) {
			throw new NoExistException("模型不存在");
		}
		return model;
	}

}
