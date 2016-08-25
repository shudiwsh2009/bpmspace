package com.chinamobile.bpmspace.core.repository.loggenerator;

import java.io.File;
import java.io.FileInputStream;

import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.importing.pnml.PnmlImport;

import com.chinamobile.bpmspace.core.repository.loggenerator.generator.AverageWeightLPM;
import com.chinamobile.bpmspace.core.repository.loggenerator.generator.CompleteParameters;
import com.chinamobile.bpmspace.core.repository.loggenerator.generator.CustomeizableLPM;
import com.chinamobile.bpmspace.core.repository.loggenerator.generator.LogManager;
import com.chinamobile.bpmspace.core.repository.loggenerator.generator.LogProduceMethod;
import com.chinamobile.bpmspace.core.repository.loggenerator.generator.NoiseParameters;

public class LogGenerator {
	private String inputFilePath = null, outputFilePath = null;
	private double completeness = 1.0;
	private int multiple = 1;
	private FileInputStream in = null;

	private CompleteParameters comPara = new CompleteParameters();
	private NoiseParameters noiPara = new NoiseParameters();
	private int tarType[] = { 1, 0 };
	private int freType[] = { 1, 0 };
	private int completeType = 1;

	public LogGenerator(String inputFilePath, String outputFilePath) {
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
	}

	public LogGenerator(String inputFilePath, String outputFilePath,
			boolean dS1, boolean tS, double tarCompleteness,
			double causalCompleteness, double freqCompleteness,
			int noiseType[], int noiseFlag, double noiseDegree) {
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;

		// completeness configure parameters
		double comDegree = 0.0;
		if (tarCompleteness > -1.0) {
			completeType = 1;
			if (dS1 == true) {
				tarType[1] = 1;
			}
			comPara.setParameter(tarType);
			comDegree = tarCompleteness;
		} else if (causalCompleteness > -1.0) {
			completeType = 2;
			comDegree = causalCompleteness;
		} else if (freqCompleteness > -1.0) {
			completeType = 3;
			if (tS == true) {
				freType[1] = 1;
			}
			comPara.setParameter(freType);
			comDegree = freqCompleteness;
		} else {
			System.out.println("No Completeness Selected!");
		}
		comPara.setCompleteType(completeType);
		comPara.setCompleteDegree(comDegree);

		// noise configure parameters
		noiPara.setNoiseType(noiseFlag);
		noiPara.setParameter(noiseType);
		noiPara.setNoiseDegree(noiseDegree);
	}

	public void CustomizableLogGenerate() {
		CustomeizableLPM lpm = new CustomeizableLPM();
		PnmlImport input = new PnmlImport();

		if (inputFilePath.endsWith(".pnml") || inputFilePath.endsWith(".xml")) {
			try {
				in = new FileInputStream(inputFilePath);
				PetriNet pn = input.read(in);
				in.close();

				// if no folder exists, create it
				String outputFileFolder = outputFilePath.substring(0,
						outputFilePath.lastIndexOf(File.separator));
				File dir = new File(outputFileFolder);
				dir.mkdirs();

				File outputFile = new File(outputFilePath);
				if (outputFile.exists()) {
					outputFile.delete();
				}
				outputFile.createNewFile();

				LogManager.generateLog(outputFilePath, lpm, pn, comPara,
						noiPara);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void DefaultLogGenerate() {
		LogProduceMethod lpm = new AverageWeightLPM();
		PnmlImport input = new PnmlImport();

		try {
			in = new FileInputStream(inputFilePath);
			PetriNet pn = input.read(in);
			in.close();

			// if no folder exists, create it
			String outputFileFolder = outputFilePath.substring(0,
					outputFilePath.lastIndexOf(File.separator));
			File dir = new File(outputFileFolder);
			dir.mkdirs();

			File outputFile = new File(outputFilePath);
			outputFile.createNewFile();

			LogManager.generateLog(outputFilePath, 100, lpm, pn, completeness,
					multiple);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
