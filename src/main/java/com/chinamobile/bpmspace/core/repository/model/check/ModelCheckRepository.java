package com.chinamobile.bpmspace.core.repository.model.check;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.Transition;
import org.processmining.importing.pnml.PnmlImport;

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

public class ModelCheckRepository {
	private MongoAccess mongo = new MongoAccess();

	public List<List<String>> checkInRepository(String processId)
			throws BasicException {
		// Empty field validity
		if (processId == null || processId.equals("")) {
			throw new EmptyFieldException("流程不存在");
		}
		// Database validity
		Process process = mongo.getProcessById(processId);
		if (process == null) {
			throw new NoExistException("流程不存在");
		}
		// Logical validity
		if (process.getType() != ProcessType.PETRINET) {
			throw new ActionRejectException("只能校验PetriNet模型");
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
		String modelFile = FileUtil.WEBAPP_ROOT + FileUtil.CHECK_PREFIX
				+ model.getXmlFilename();
		mongo.getFileByFilename(model.getXmlFilename(), modelFile);
		return this.checkInFile(modelFile);
	}

	public List<List<String>> checkInFile(String filepath)
			throws BasicException {
		// Empty field validity
		if (filepath == null || filepath.equals("")) {
			throw new EmptyFieldException("文件不存在");
		}
		File file = new File(filepath);
		if (!file.exists()) {
			throw new NoExistException("文件不存在");
		}
		FileInputStream fInput = null;
		try {
			fInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new BasicException("读取文件错误");
		}
		PnmlImport pnmlImport = new PnmlImport();
		PetriNet pn = null;
		try {
			pn = pnmlImport.read(fInput);
		} catch (Exception e) {
			throw new BasicException("读取模型除错");
		}
		if (pn == null) {
			throw new NoExistException("模型不存在");
		}
		PetriNetUtil.makeVisible(pn);
		return this.modelCheck(pn);
	}

	private List<List<String>> modelCheck(PetriNet pn) {
		List<String> places = new ArrayList<String>();
		List<String> transitions = new ArrayList<String>();
		// List<String> diagnosis = new ArrayList<String>();
		for (Place p : pn.getPlaces()) {
			places.add("Place " + p.getIdentifier());
		}
		for (Transition t : pn.getTransitions()) {
			transitions.add("Transition " + t.getIdentifier());
		}
		// WoflanAnalysisResult woflanResult = new WoflanAnalysisResult(pn);
		// diagnosis.add("workflow net: " + (woflanResult.isWorkflowNet() ?
		// "Passed" : "Failed"));
		// diagnosis.add("live: " + (woflanResult.isLive() ? "Passed" :
		// "Failed"));
		// diagnosis.add("bound: " + (woflanResult.isBounded() ? "Passed" :
		// "Failed"));
		// diagnosis.add("non dead: " + (woflanResult.isNonDead() ? "Passed" :
		// "Failed"));
		// diagnosis.add("sound wfnet: " + (woflanResult.isSoundWorkflowNet() ?
		// "Passed" : "Failed"));

		List<List<String>> result = new ArrayList<List<String>>();
		result.add(places);
		result.add(transitions);
		// result.add(diagnosis);
		return result;
	}

	public static void main(String[] args) throws Exception {
		File file = new File("C:\\Users\\picry\\Desktop\\Model\\Model\\M0.pnml");
		FileInputStream fInput = new FileInputStream(file);
		PnmlImport pnmlImport = new PnmlImport();
		PetriNet pn = pnmlImport.read(fInput);
		WoflanAnalysis wt = new WoflanAnalysis(pn);
		DefaultMutableTreeNode root = wt.getRoot();
		System.out.println(root.toString());
		// ModelCheckRepository mcr = new ModelCheckRepository();
		// List<List<String>> result =
		// mcr.checkInFile("C:\\Users\\picry\\Desktop\\Model\\Model\\M0.pnml");
		// System.out.println(result.toString());
	}

}
