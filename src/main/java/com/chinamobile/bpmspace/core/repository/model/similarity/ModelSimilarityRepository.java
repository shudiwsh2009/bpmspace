package com.chinamobile.bpmspace.core.repository.model.similarity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.importing.pnml.PnmlImport;

import cn.edu.thss.iise.beehivez.server.metric.BTSSimilarity_Wang;
import cn.edu.thss.iise.beehivez.server.metric.BehavioralProfileSimilarity;
import cn.edu.thss.iise.beehivez.server.metric.CausalFootprintSimilarity;
import cn.edu.thss.iise.beehivez.server.metric.ContextBasedSimilarity;
import cn.edu.thss.iise.beehivez.server.metric.JaccardTARSimilarity;
import cn.edu.thss.iise.beehivez.server.metric.PetriNetSimilarity;
import cn.edu.thss.iise.beehivez.server.metric.ssdt.SSDTSimilarity;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.ActionRejectException;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.chinamobile.bpmspace.core.util.PetriNetUtil;

public class ModelSimilarityRepository {
	private MongoAccess mongo = new MongoAccess();

	public float similarityInRepository(String processId1, String processId2,
			String algorithm) throws BasicException {
		// Empty field validity
		if (processId1 == null || processId1.equals("")) {
			throw new EmptyFieldException("流程不存在");
		} else if (processId2 == null || processId2.equals("")) {
			throw new EmptyFieldException("流程不存在");
		}
		// Database validity
		Process process1 = mongo.getProcessById(processId1);
		Process process2 = mongo.getProcessById(processId2);
		if (process1 == null || process2 == null) {
			throw new NoExistException("流程不存在");
		}
		// Logical validity
		if (process1.getType() != ProcessType.PETRINET
				|| process2.getType() != ProcessType.PETRINET) {
			throw new ActionRejectException("只能计算PetriNet模型的相似度");
		}
		if (process1.getRevision().size() < 1
				|| process2.getRevision().size() < 1) {
			throw new NoExistException("模型不存在");
		}
		String modelId1 = process1.getRevision()
				.get((long) process1.getRevision().size()).getModelId();
		String modelId2 = process2.getRevision()
				.get((long) process2.getRevision().size()).getModelId();
		Model model1 = mongo.getModelById(modelId1);
		Model model2 = mongo.getModelById(modelId2);
		if (model1 == null || model2 == null) {
			throw new NoExistException("模型不存在");
		}
		String modelFile1 = FileUtil.WEBAPP_ROOT + FileUtil.SIM_PREFIX
				+ model1.getXmlFilename();
		String modelFile2 = FileUtil.WEBAPP_ROOT + FileUtil.SIM_PREFIX
				+ model2.getXmlFilename();
		mongo.getFileByFilename(model1.getXmlFilename(), modelFile1);
		mongo.getFileByFilename(model2.getXmlFilename(), modelFile2);
		return this.similarityInFile(modelFile1, modelFile2, algorithm);
	}

	public float similarityInFile(String filepath1, String filepath2,
			String algorithm) throws BasicException {
		// filepath1 = FileUtil.WEBAPP_ROOT + filepath1.replaceAll("/",
		// File.separator);
		// filepath2 = FileUtil.WEBAPP_ROOT + filepath2.replaceAll("/",
		// File.separator);
		// Empty field validity
		if (filepath1 == null || filepath1.equals("") || filepath2 == null
				|| filepath2.equals("")) {
			throw new EmptyFieldException("文件不存在");
		}
		File file1 = new File(filepath1);
		File file2 = new File(filepath2);
		if (!file1.exists() || !file2.exists()) {
			throw new NoExistException("文件不存在");
		}
		FileInputStream fInput1 = null, fInput2 = null;
		try {
			fInput1 = new FileInputStream(file1);
			fInput2 = new FileInputStream(file2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new BasicException("读取文件错误");
		}
		PnmlImport pnmlImport = new PnmlImport();
		PetriNet pn1 = null, pn2 = null;
		try {
			pn1 = pnmlImport.read(fInput1);
			pn2 = pnmlImport.read(fInput2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BasicException("读取模型出错");
		}
		if (pn1 == null || pn2 == null) {
			throw new NoExistException("模型不存在");
		}
		PetriNetUtil.makeVisible(pn1);
		PetriNetUtil.makeVisible(pn2);
		float sim = this.similarity(pn1, pn2, algorithm);
		return sim;
	}

	public float similarityInFileAndRepository(String filepath,
			String processId, String algorithm) throws BasicException {
		// Process 1
		if (processId == null || processId.equals("")) {
			throw new EmptyFieldException("流程不存在");
		}
		Process process = mongo.getProcessById(processId);
		if (process == null) {
			throw new NoExistException("流程不存在");
		}
		if (process.getType() != ProcessType.PETRINET) {
			throw new ActionRejectException("只能计算PetriNet模型的相似度");
		}
		if (process.getRevision().size() < 1) {
			throw new NoExistException("模型不存在");
		}
		String modelId = process.getRevision()
				.get((long) process.getRevision().size()).getModelId();
		Model model = mongo.getModelById(modelId);
		if (model == null) {
			throw new NoExistException("模型不存在");
		}
		String modelFile = FileUtil.WEBAPP_ROOT + FileUtil.SIM_PREFIX
				+ model.getXmlFilename();
		mongo.getFileByFilename(model.getXmlFilename(), modelFile);
		return this.similarityInFile(filepath, modelFile, algorithm);
	}

	public List<String> getSimilarityMeasureAlgorithms() {
		List<String> algorithms = new ArrayList<>();
		algorithms.add("Behavioral Profile");
		algorithms.add("Principal Transition Sequences");
		algorithms.add("Transition Adjacency Relations");
		algorithms.add("Shortest Succession Distance between Tasks");
		algorithms.add("Causal Footprint");
		algorithms.add("Context Based");
		return algorithms;
	}

	private float similarity(PetriNet pn1, PetriNet pn2, String algorithm)
			throws BasicException {
		PetriNetSimilarity measure = null;
		if (algorithm.equals("Behavioral Profile")) {
			measure = new BehavioralProfileSimilarity();
		} else if (algorithm.equals("Principal Transition Sequences")) {
			measure = new BTSSimilarity_Wang();
		} else if (algorithm.equals("Transition Adjacency Relations")) {
			measure = new JaccardTARSimilarity();
		} else if (algorithm
				.equals("Shortest Succession Distance between Tasks")) {
			measure = new SSDTSimilarity();
		} else if (algorithm.equals("Causal Footprint")) {
			measure = new CausalFootprintSimilarity();
		} else if (algorithm.equals("Context Based")) {
			measure = new ContextBasedSimilarity();
		} else {
			throw new NoExistException("不支持选定算法");
		}
		float sim = 0.0f;
		try {
			sim = measure.similarity(pn1, pn2);
		} catch (Exception e) {
			throw new BasicException("计算错误");
		}
		return sim;
	}
}
