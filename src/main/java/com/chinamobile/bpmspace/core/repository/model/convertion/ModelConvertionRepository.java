package com.chinamobile.bpmspace.core.repository.model.convertion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.processmining.framework.log.LogEvent;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Transition;
import org.processmining.framework.models.petrinet.algorithms.PnmlWriter;
import org.processmining.framework.ui.ComboBoxLogEvent;
import org.processmining.importing.pnml.PnmlImport;

import com.chinamobile.bpmspace.core.exception.BasicException;

public class ModelConvertionRepository {

	public void autoLayout(String inPnmlFile, String outPnmlFile)
			throws BasicException {
		try {
			FileInputStream fInput = new FileInputStream(new File(inPnmlFile));
			PnmlImport pnmlImport = new PnmlImport();
			PetriNet pn = pnmlImport.read(fInput);
			for (Transition t : pn.getTransitions()) {
				if (t.isInvisibleTask()) {
					LogEvent event = new LogEvent(ComboBoxLogEvent.NONE,
							ComboBoxLogEvent.NONE);
					t.setLogEvent(event);
				}
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPnmlFile));
			PnmlWriter.write(false, true, pn, bw);
			bw.close();
		} catch (Exception e) {
			throw new BasicException("未知错误");
		}
	}

	public void createPnmlJsonFile(String pnmlPath, String outJsonFile) {
		Pnml2Json pj = new Pnml2Json();
		pj.getShapInfo(pnmlPath);
		pj.getConnections();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outJsonFile));
			bw.write(pj.createJson());
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;

	}

	public void createEpcJsonFile(String epcPath, String outJsonFile) {
		// TODO Auto-generated method stub
		Epc2Json ej = new Epc2Json();
		ej.getShapInfo(epcPath);
		ej.getConnections();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outJsonFile));
			bw.write(ej.createJson());
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public void createBpmnJsonFile(String bpmnPath, String outJsonFile) {
		// TODO Auto-generated method stub
		Bpmn2Json bj = new Bpmn2Json();
		bj.getShapInfo(bpmnPath);
		bj.getConnections();
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outJsonFile));
			bw.write(bj.createJson());
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

}
