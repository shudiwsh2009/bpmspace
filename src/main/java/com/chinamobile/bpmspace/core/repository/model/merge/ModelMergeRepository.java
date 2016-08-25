package com.chinamobile.bpmspace.core.repository.model.merge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jbpm.ruleflow.core.RuleFlowProcess;

import cn.edu.thss.iise.bpmdemo.analysis.core.util.DataUtil;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.ibm.bpm.util.MergeProcess;

public class ModelMergeRepository {

	private MongoAccess mongo = new MongoAccess();

	public String modelMerge(String[] filepaths, String[] processIds,
			String userId) throws BasicException {
		if (userId == null || userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		User user = mongo.getUserById(userId);
		if (user == null) {
			throw new NoExistException("用户不存在");
		}
		if (filepaths == null) {
			filepaths = new String[0];
		}
		if (processIds == null) {
			processIds = new String[0];
		}
		if (filepaths.length == 0 && processIds.length == 0) {
			throw new EmptyFieldException("未选取模型");
		}
		List<String> filenames = new ArrayList<String>();
		for (String f : filepaths) {
			File file = new File(f);
			if (file.exists()) {
				filenames.add(f);
			}
		}

		String serverPath = FileUtil.MERGE_PREFIX + user.getId() + "_"
				+ System.nanoTime() + File.separator;
		String localPath = FileUtil.WEBAPP_ROOT + serverPath;
		File folder = new File(localPath + "model" + File.separator);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		List<Process> processList = new ArrayList<Process>();
		for (String pId : processIds) {
			Process p = mongo.getProcessById(pId);
			if (p == null || p.getType() != ProcessType.BPMN) {
				continue;
			}
			processList.add(p);
		}
		for (Process p : processList) {
			if (p.getRevision().size() < 1) {
				continue;
			}
			String modelId = p.getRevision().get((long) p.getRevision().size())
					.getModelId();
			Model model = mongo.getModelById(modelId);
			if (model == null) {
				continue;
			}
			String modelFile = p.getName() + "_" + model.getXmlFilename();
			mongo.getFileByFilename(model.getXmlFilename(), localPath + "model"
					+ File.separator + modelFile);
			filenames.add(localPath + "model" + File.separator + modelFile);
		}
		if (filenames.size() == 0) {
			throw new NoExistException("没有可供合并的模型");
		}

		String mergeFile = "Merge_" + System.nanoTime()
				+ FileUtil.BPMN_SUFFIX;
		this.merge(filenames, localPath + mergeFile);
		return serverPath + mergeFile;
	}

	private ArrayList<com.ibm.bpm.model.Process> readHaifa(
			List<String> filenames) {
		// 1.转换JBPMtoHaifa
		ArrayList<org.kie.api.definition.process.Process> jbpmModelList = new ArrayList<org.kie.api.definition.process.Process>();
		ArrayList<com.ibm.bpm.model.Process> result = new ArrayList<com.ibm.bpm.model.Process>();
		for (String fName : filenames) {
			File file = new File(fName);
			List<org.kie.api.definition.process.Process> processes = new ArrayList<org.kie.api.definition.process.Process>();
			try {
				processes = DataUtil.importFromXmlFile(file.getAbsolutePath());
			} catch (IOException e) {
				continue;
			}
			if (processes.size() == 0) {
				continue;
			}
			RuleFlowProcess process = (RuleFlowProcess) processes.get(0);
			process = DataUtil.deMultiInstance(process);
			jbpmModelList.add(process);
		}
		for (org.kie.api.definition.process.Process process : jbpmModelList) {
			com.ibm.bpm.model.Process IBMProcess = DataUtil
					.convertProcesstoIBMProcess((RuleFlowProcess) process);
			result.add(IBMProcess);
		}
		return result;
	}

	private void outputModel(com.ibm.bpm.model.Process model,
			String outputFilePath) throws IOException {
		RuleFlowProcess resultModel = DataUtil
				.convertIBMProcesstoProcess(model);
		String modelString = DataUtil
				.convertBPMNProcessToXmlString(resultModel);
		FileWriter fw = new FileWriter(outputFilePath);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(modelString);
		bw.close();
	}

	private void merge(List<String> filenames, String outputFilePath)
			throws BasicException {
		// 1.转换JBPMtoHaifa
		ArrayList<com.ibm.bpm.model.Process> haifaModel = null;
		haifaModel = readHaifa(filenames);

		// for (int i = 0; i < haifaModel.size(); i++) {
		// com.ibm.bpm.model.Process p = haifaModel.get(i);
		// // BPMNModel m = new BPMNModel();
		// }
		// 2.merge
		com.ibm.bpm.model.Process mergedModel = MergeProcess.merge(haifaModel);
		// 3.转换海法toJBPM
		try {
			outputModel(mergedModel, outputFilePath);
		} catch (IOException e) {
			throw new BasicException("写入合并文件出错");
		}
	}

	public static void main(String[] args) throws BasicException {
		ModelMergeRepository mmr = new ModelMergeRepository();
		FileUtil.WEBAPP_ROOT = "D:/Workspace/apache-tomcat-8.0.9/webapps/bpmspace/";
		String[] filepaths = {
				"D:\\Process Data Group\\02.Process Space\\GQL\\Model\\bpmn文件\\bpmn文件81\\fragments\\Fragment_11.bpmn",
				"D:\\Process Data Group\\02.Process Space\\GQL\\Model\\bpmn文件\\bpmn文件81\\fragments\\Fragment_32.bpmn",
				"D:\\Process Data Group\\02.Process Space\\GQL\\Model\\bpmn文件\\bpmn文件81\\fragments\\Fragment_336.bpmn" };
		mmr.modelMerge(filepaths, null, "53c64e84a3f5e7f6c66d7094");
	}
}
