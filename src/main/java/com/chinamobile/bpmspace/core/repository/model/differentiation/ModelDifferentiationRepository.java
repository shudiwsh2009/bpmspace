package com.chinamobile.bpmspace.core.repository.model.differentiation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.thss.iise.xiaohan.bpcd.graph.EPCHelper;
import cn.edu.thss.iise.xiaohan.bpcd.nl.tue.tm.is.epc.EPC;
import cn.edu.thss.iise.xiaohan.bpcd.similarity.highlevelop.HighLevelOP;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.ActionRejectException;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.util.FileUtil;

import de.bpt.hpi.graph.Graph;

public class ModelDifferentiationRepository {
	private MongoAccess mongo = new MongoAccess();

	public List<String> differentiationInFile(String filepath1, String filepath2)
			throws BasicException {
		if (filepath1 == null || filepath1.equals("") || filepath2 == null
				|| filepath2.equals("")) {
			throw new EmptyFieldException("文件不存在");
		}
		File file1 = new File(filepath1);
		File file2 = new File(filepath2);
		if (!file1.exists() || !file2.exists()) {
			throw new NoExistException("文件不存在");
		}
		Graph graph1 = null, graph2 = null;
		try {
			EPC epcmodel1 = EPC.loadEPML(filepath1);
			epcmodel1.cleanEPC();
			EPC epcmodel2 = EPC.loadEPML(filepath2);
			epcmodel2.cleanEPC();

			EPCHelper epc1 = new EPCHelper(epcmodel1, filepath1);
			EPCHelper epc2 = new EPCHelper(epcmodel2, filepath2);

			graph1 = epc1.getGraph();
			graph2 = epc2.getGraph();
		} catch (Exception e) {
			throw new BasicException("解析EPC模型出错");
		}
		if (graph1 == null || graph2 == null) {
			throw new NoExistException("模型不存在");
		}
		List<String> opList = new ArrayList<String>();
		try {
			opList = HighLevelOP.GetHighLevelOPList(graph1, graph2);
		} catch (Exception e) {
			throw new BasicException("未知错误");
		}
		return opList;
	}

	public List<String> differentiationInRepository(String processId1,
			String processId2) throws BasicException {
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
		if (process1.getType() != ProcessType.EPC
				|| process2.getType() != ProcessType.EPC) {
			throw new ActionRejectException("只能分析EPC模型的差异性");
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
		String modelFile1 = FileUtil.WEBAPP_ROOT + FileUtil.DIFF_PREFIX
				+ model1.getXmlFilename();
		String modelFile2 = FileUtil.WEBAPP_ROOT + FileUtil.DIFF_PREFIX
				+ model2.getXmlFilename();
		mongo.getFileByFilename(model1.getXmlFilename(), modelFile1);
		mongo.getFileByFilename(model2.getXmlFilename(), modelFile2);
		return this.differentiationInFile(modelFile1, modelFile2);
	}

	public List<String> differentiationInFileAndRepository(String filepath,
			String processId) throws BasicException {
		// Process 1
		if (processId == null || processId.equals("")) {
			throw new EmptyFieldException("流程不存在");
		}
		Process process = mongo.getProcessById(processId);
		if (process == null) {
			throw new NoExistException("流程不存在");
		}
		if (process.getType() != ProcessType.PETRINET) {
			throw new ActionRejectException("只能分析EPC模型的差异性");
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
		return this.differentiationInFile(filepath, modelFile);
	}
}
