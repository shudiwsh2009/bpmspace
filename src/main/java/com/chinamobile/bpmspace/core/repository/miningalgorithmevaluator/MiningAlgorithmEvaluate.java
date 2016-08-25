package com.chinamobile.bpmspace.core.repository.miningalgorithmevaluator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.processmining.analysis.conformance.ConformanceLogReplayResult;
import org.processmining.analysis.conformance.ConformanceMeasurer;
import org.processmining.analysis.conformance.MaximumSearchDepthDiagnosis;
import org.processmining.analysis.conformance.StructuralAnalysisMethod;
import org.processmining.analysis.conformance.StructuralAnalysisResult;
import org.processmining.exporting.petrinet.PnmlExport;
import org.processmining.framework.log.LogFile;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogReaderFactory;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.algorithms.logReplay.AnalysisConfiguration;
import org.processmining.framework.models.petrinet.algorithms.logReplay.AnalysisMethodEnum;
import org.processmining.framework.models.petrinet.algorithms.logReplay.LogReplayAnalysisMethod;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.framework.ui.Progress;
import org.processmining.importing.pnml.PnmlImport;

import com.chinamobile.bpmspace.core.repository.loggenerator.generator.AverageWeightLPM;
import com.chinamobile.bpmspace.core.repository.loggenerator.generator.LogManager;
import com.chinamobile.bpmspace.core.repository.loggenerator.generator.LogProduceMethod;
import com.chinamobile.bpmspace.core.repository.miningalgorithmevaluator.evaluator.ExtensiveTarLPM;

public class MiningAlgorithmEvaluate {
	private String inputFilePath = null, outputFilePath = null,
			outputModelPath = null;
	private String resultFilePath = null;
	private String logGenerateAlgorithm = null;
	public int selectedMiningAlgorithm = 0;
	public int selectedSimilarityAlgorithm = 0;
	public int selectedStrSimilarityAlgorithm = 0;
	private double completeness = 0.0f;

	private ArrayList<Float> evaluationResult = new ArrayList<Float>();

	public Vector<String> miningAlgorithmList = null;
	public Vector<String> similarityAlgorithmList = null;
	public Vector<String> similarityStrAlgorithmList = null;
	private Float averageSim = 0.0f;
	private Float averageSimilarity = 0.0f;
	private Float meanDeviation = 0.0f;
	private Float averageStrSimilarity = 0.0f;
	private Float meanStrDeviation = 0.0f;

	private ArrayList<String> model = new ArrayList<String>();
	private ArrayList<Float> f = new ArrayList<Float>();
	private ArrayList<Float> aB = new ArrayList<Float>();
	private ArrayList<Float> aS = new ArrayList<Float>();
	private ArrayList<Float> similarityvaluevector = new ArrayList<Float>();
	private ArrayList<Float> similaritystrvaluevector = new ArrayList<Float>();
	private int lastMultiple = -1;
	private String lastLogAlg = null;
	private double lastCompleteness = -1;

	public Hashtable<String, PetriNet> map = new Hashtable<String, PetriNet>();
	public HashMap<String, String> logminemap = new HashMap<String, String>();
	public HashMap<String, String> modellogmap = new HashMap<String, String>();
	public HashMap<String, String> modelminemap = new HashMap<String, String>();
	public HashMap<Float, String> simmodelmap = new HashMap<Float, String>();

	public MiningAlgorithmEvaluate(String inputFilePath, String outputFilePath,
			String logGenerateAlgorithm, String miningAlgorithm,
			String similarityAlgorithm, String similarityStrAlgorithm,
			double completeness) {
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
		this.logGenerateAlgorithm = logGenerateAlgorithm;
		this.completeness = completeness;
		String temp = outputFilePath.substring(0,
				outputFilePath.lastIndexOf("."));
		outputModelPath = temp.replaceAll("logs", "new_models") + ".pnml";
		resultFilePath = temp.replaceAll("logs", "result") + ".xls";
		loadAlgorithmList();

		// mining algorithms
		if (miningAlgorithm.equals("alpha")) {
			selectedMiningAlgorithm = 0;
		} else if (miningAlgorithm.equals("alphaPlusPlus")) {
			selectedMiningAlgorithm = 1;
		} else if (miningAlgorithm.equals("alphaSharp")) {
			selectedMiningAlgorithm = 2;
		} else if (miningAlgorithm.equals("genetic")) {
			selectedMiningAlgorithm = 3;
		} else if (miningAlgorithm.equals("dtGenetic")) {
			selectedMiningAlgorithm = 4;
		} else if (miningAlgorithm.equals("heuristic")) {
			selectedMiningAlgorithm = 5;
		} else {
			selectedMiningAlgorithm = 6;
		}

		// similarity algorithms
		if (similarityAlgorithm.equals("JaccardTARSimilarity")) {
			selectedSimilarityAlgorithm = 0;
		} else if (similarityAlgorithm.equals("ExtensiveTARSimilarity")) {
			selectedSimilarityAlgorithm = 1;
		} else if (similarityAlgorithm.equals("BPSSimilarity")) {
			selectedSimilarityAlgorithm = 2;
		} else if (similarityAlgorithm.equals("CausalFootprintSimilarity")) {
			selectedSimilarityAlgorithm = 3;
		}

		// similarity structure algorithms
		if (similarityStrAlgorithm.equals("ContextBasedSimilarity")) {
			selectedStrSimilarityAlgorithm = 0;
		} else if (similarityStrAlgorithm.equals("JaccardStructureSimilarity")) {
			selectedStrSimilarityAlgorithm = 1;
		} else if (similarityStrAlgorithm.equals("CausalFootprintSimilarity")) {
			selectedStrSimilarityAlgorithm = 2;
		} else if (similarityStrAlgorithm.equals("DependencyGraphSimilarity")) {
			selectedStrSimilarityAlgorithm = 3;
		}
	}

	public void loadAlgorithmList() {
		miningAlgorithmList = new Vector<String>();
		similarityAlgorithmList = new Vector<String>();
		similarityStrAlgorithmList = new Vector<String>();

		// add mining algorithms
		miningAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.processmining.miner.AlphaMiner");
		miningAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.processmining.miner.AlphaPlusPlusMiner");
		miningAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.processmining.miner.AlphaSharpMiner");
		miningAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.processmining.miner.GeneticMiner");
		miningAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.processmining.miner.DupTGeneticMiner");
		miningAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.processmining.miner.HeuristicMiner");
		miningAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.processmining.miner.Region_Miner");

		// add similarity metric algorithms
		similarityAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.miningalgorithmevaluator.similarityAlgorithm.JaccardTARSimilarity");
		similarityAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.miningalgorithmevaluator.similarityAlgorithm.ExtensiveTARSimilarity");
		similarityAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.miningalgorithmevaluator.similarityAlgorithm.BPSSimilarity");
		similarityAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.miningalgorithmevaluator.similarityAlgorithm.CausalFootprintSimilarity");

		// add similarity structure algorithms
		similarityStrAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.miningalgorithmevaluator.similarityAlgorithm.ContextBasedSimilarity");
		similarityStrAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.miningalgorithmevaluator.similarityAlgorithm.JaccardStructureSimilarity");
		similarityStrAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.miningalgorithmevaluator.similarityAlgorithm.CausalFootprintSimilarity");
		similarityStrAlgorithmList
				.add("com.chinamobile.bpmspace.core.repository.miningalgorithmevaluator.similarityAlgorithm.DependencyGraphSimilarity");

	}

	public void PreMiningEvaluate() {
		LogProduceMethod lpm;
		String logType = null;
		int multiple = 1;
		FileInputStream in = null;
		PnmlImport input = new PnmlImport();

		if (logGenerateAlgorithm.equals("AverageWeightLPM")) {
			// log generate algorithm: AverageWeightLPM
			lpm = new AverageWeightLPM();
			logType = lpm.getLogType();
			String cuLogAlg = "tar";

			if (inputFilePath.endsWith(".pnml")
					|| inputFilePath.endsWith(".xml")) {
				try {
					in = new FileInputStream(inputFilePath);
					PetriNet pn = input.read(in);
					in.close();

					map.put(outputFilePath, pn);// inModel -> petrinet
					modellogmap.put(outputFilePath, inputFilePath);// log ->
																	// inModel
					modelminemap.put(inputFilePath, outputModelPath);// inModel
																		// ->
																		// outModel
					logminemap.put(outputFilePath, outputModelPath);// log ->
																	// outModel

					File logFile = new File(outputFilePath);
					if ((!logFile.exists())
							|| (completeness != lastCompleteness)
							|| (!cuLogAlg.equals(lastLogAlg))
							|| (multiple != lastMultiple)) {
						File logFileFolder = new File(outputFilePath.substring(
								0, outputFilePath.lastIndexOf(File.separator)));
						logFileFolder.mkdirs();
						logFile.createNewFile();
						LogManager.generateLog(outputFilePath, 100, lpm, pn,
								completeness, multiple);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			lastLogAlg = cuLogAlg;
			lastCompleteness = completeness;
			lastMultiple = multiple;
		} else if (logGenerateAlgorithm.equals("ExtensiveTarLPM")) {
			// log generate algorithm: ExtensiveTarLPM
			lpm = new ExtensiveTarLPM();
			logType = lpm.getLogType();
			String cuLogAlg = "etar";

			if (inputFilePath.endsWith(".pnml")
					|| inputFilePath.endsWith(".xml")) {
				try {
					in = new FileInputStream(inputFilePath);
					PetriNet pn = input.read(in);
					in.close();

					map.put(outputFilePath, pn);// inModel -> petrinet
					modellogmap.put(outputFilePath, inputFilePath);// log ->
																	// inModel
					modelminemap.put(inputFilePath, outputModelPath);// inModel
																		// ->
																		// outModel
					logminemap.put(outputFilePath, outputModelPath);// log ->
																	// outModel

					File logFile = new File(outputFilePath);
					if ((!logFile.exists()) || (!cuLogAlg.equals(lastLogAlg))) {
						File logFileFolder = new File(outputFilePath.substring(
								0, outputFilePath.lastIndexOf(File.separator)));
						logFileFolder.mkdirs();
						logFile.createNewFile();
						LogManager.generateLog(outputFilePath, lpm, pn);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			lastLogAlg = cuLogAlg;
		} else {
			System.out.println("error: Choosing log generate algorithm!");
		}
	}

	public void MiningEvaluate() {
		if (map == null || map.size() <= 0) {
			return;
		}
		Float value1 = 0.0f; // averageSimilarity
		Float value2 = 0.0f; // meanDeviation
		Float value3 = 0.0f; // averageStrSimilarity
		Float value4 = 0.0f; // meanStrDeviation
		averageSim = 0.0f;
		similarityvaluevector = new ArrayList<Float>();
		f = new ArrayList<Float>();
		aB = new ArrayList<Float>();
		aS = new ArrayList<Float>();

		LogReader logReader = null;
		String miningAlgorithmName;
		Class miningAlgorithmClass;
		String similarityAlgorithmName;
		Class similarityAlgorithmClass;
		String similarityStrAlgorithmName;
		Class similarityStrAlgorithmClass;

		// create and write to the result [.xls] file.
		File resultfile = new File(resultFilePath);
		if (!resultfile.exists()) {
			try {
				File resultFolder = new File(resultFilePath.substring(0,
						resultFilePath.lastIndexOf(File.separator)));
				resultFolder.mkdirs();
				resultfile.createNewFile();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}

		// write the first line of result.xls
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(resultFilePath);
			bw = new BufferedWriter(fw);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bw.write("FileName", 0, 8);
			bw.write("\t");
			bw.write("BehSimilarity", 0, 13);
			bw.write("\t");
			bw.write("StrSimilarity", 0, 13);
			bw.write("\t");
			bw.write("Time", 0, 4);
			bw.write("\t");
			bw.write("aS", 0, 2);
			bw.write("\t");
			bw.write("f", 0, 1);
			bw.write("\t");
			bw.write("aB", 0, 2);
			bw.newLine();
			bw.flush();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		// do the evaluation
		try {
			Enumeration<String> en = map.keys();
			while (en.hasMoreElements()) {
				String logpath = en.nextElement();
				String modelpath = modellogmap.get(logpath);
				String minepath = logminemap.get(logpath);
				FileInputStream in1 = null;
				PnmlImport input = null;
				PetriNet pn = null;

				LogFile logFile = LogFile.getInstance(logpath);
				logReader = LogReaderFactory.createInstance(null, logFile);

				miningAlgorithmName = miningAlgorithmList
						.get(selectedMiningAlgorithm);
				miningAlgorithmClass = Class.forName(miningAlgorithmName);
				Object miningAlgorithmObject = miningAlgorithmClass
						.newInstance();
				Class ptype[] = new Class[1];
				ptype[0] = Class
						.forName("org.processmining.framework.log.LogReader");

				Method miningMethod = miningAlgorithmClass.getMethod("mine",
						ptype);
				Object m_args[] = new Object[1];
				m_args[0] = logReader;

				Long startTime = System.nanoTime();
				PetriNet miningModel = (PetriNet) miningMethod.invoke(
						miningAlgorithmObject, m_args);
				Long estimatedTime = System.nanoTime() - startTime;

				// generate a new model[.pnml] file from the log[.mxml] file
				if (miningModel != null) {
					PnmlExport exportPlugin = new PnmlExport();
					Object[] objects = new Object[] { miningModel };
					ProvidedObject object = new ProvidedObject("temp", objects);
					File file = new File(minepath);

					if (file.exists()) {
						file.delete();
					} else {
						File newModelFolder = new File(minepath.substring(0,
								minepath.lastIndexOf(File.separator)));
						newModelFolder.mkdirs();
					}

					file.createNewFile();
					FileOutputStream outputStream = new FileOutputStream(
							minepath);
					exportPlugin.export(object, outputStream);
					outputStream.close();
				} else {
					System.err.println("No Petri net could be constructed.");
				}

				// calculate Behavior[BehSimilarity] Similarity
				similarityAlgorithmName = similarityStrAlgorithmList
						.get(selectedSimilarityAlgorithm);
				similarityAlgorithmClass = Class
						.forName(similarityAlgorithmName);
				Object similarityAlgorithmObject = similarityAlgorithmClass
						.newInstance();
				Class stype[] = new Class[2];
				stype[0] = Class
						.forName("org.processmining.framework.models.petrinet.PetriNet");
				stype[1] = Class
						.forName("org.processmining.framework.models.petrinet.PetriNet");
				Method similarityMethod = similarityAlgorithmClass.getMethod(
						"similarity", stype);
				Object s_args[] = new Object[2];
				s_args[0] = map.get(logpath);
				s_args[1] = miningModel;
				Object result = similarityMethod.invoke(
						similarityAlgorithmObject, s_args);

				// calculate structure[StrSimilarity] Similarity
				similarityStrAlgorithmName = similarityStrAlgorithmList
						.get(selectedStrSimilarityAlgorithm);
				similarityStrAlgorithmClass = Class
						.forName(similarityStrAlgorithmName);
				Object similarityStrAlgorithmObject = similarityStrAlgorithmClass
						.newInstance();
				Class stype1[] = new Class[2];
				stype1[0] = Class
						.forName("org.processmining.framework.models.petrinet.PetriNet");
				stype1[1] = Class
						.forName("org.processmining.framework.models.petrinet.PetriNet");
				Method similarityStrMethod = similarityStrAlgorithmClass
						.getMethod("similarity", stype1);
				Object s_args1[] = new Object[2];
				s_args1[0] = map.get(logpath);
				s_args1[1] = miningModel;
				Object result1 = similarityStrMethod.invoke(
						similarityStrAlgorithmObject, s_args1);

				// deleting
				miningModel.clearGraph();
				miningModel.delete();
				(map.get(logpath)).clearGraph();
				(map.get(logpath)).delete();

				// write BehSimilarity & StrSimilarity to result[.xls]
				String rs = result.toString();
				String rs1 = result1.toString();
				Float ress = 0f;
				Float ress1 = 0f;
				if (!rs.equals("NaN")) {
					Float res = Float.parseFloat(rs);
					ress = (float) (((int) (res * 100)) / 100.0);
					similarityvaluevector.add(ress);
					model.add("");
				}
				if (!rs1.equals("NaN")) {
					Float res1 = Float.parseFloat(rs1);
					ress1 = (float) (((int) (res1 * 100)) / 100.0);
					similaritystrvaluevector.add(ress1);
				}
				int indexx = modelpath.lastIndexOf(File.separator);
				String name = modelpath.substring(indexx + 1,
						modelpath.length());
				bw.write(name, 0, name.length());
				bw.write("\t");
				bw.write(ress.toString(), 0, ress.toString().length());
				bw.write("\t");
				bw.write(ress1.toString(), 0, ress1.toString().length());
				bw.write("\t");
				bw.write(estimatedTime.toString(), 0, estimatedTime.toString()
						.length());
				bw.write("\t");
				bw.newLine();
				bw.flush();

				try {
					in1 = new FileInputStream(minepath);
					input = new PnmlImport();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					pn = input.read(in1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// get aS
				StructuralAnalysisResult sar = new StructuralAnalysisResult(
						new AnalysisConfiguration(), pn,
						new StructuralAnalysisMethod(pn));
				float aSr = sar.getStructuralAppropriatenessMeasure();
				Float aSre = (float) (((int) (aSr * 100)) / 100.0);
				aS.add(aSre);
				try {
					bw.write(aSre.toString(), 0, aSre.toString().length());
					bw.write("\t");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// get f[Fitness]
				AnalysisConfiguration myOptions = createAnalysisConfiguration();
				LogReplayAnalysisMethod logReplayAnalysis = new LogReplayAnalysisMethod(
						pn, logReader, new ConformanceMeasurer(), new Progress(
								0, 100));
				int maxSearchDepth = MaximumSearchDepthDiagnosis
						.determineMaximumSearchDepth(pn);
				logReplayAnalysis.setMaxDepth(maxSearchDepth); // automatically
																// set maximum
																// search depth
																// for log
																// replay
				ConformanceLogReplayResult clrr = (ConformanceLogReplayResult) logReplayAnalysis
						.analyse(myOptions);

				float fr = clrr.getFitnessMeasure();
				Float fre = (float) (((int) (fr * 100)) / 100.0);
				f.add(fre);
				try {
					bw.write(fre.toString(), 0, fre.toString().length());
					bw.write("\t");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// get aB
				float ab = 0;
				try {
					ab = clrr.getBehavioralAppropriatenessMeasure();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Float abb = (float) (((int) (ab * 100)) / 100.0);
				aB.add(abb);
				try {
					bw.write(abb.toString(), 0, abb.toString().length());
					bw.newLine();
					bw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// value1 & value2
		float s1 = 0;
		float s2 = 0;
		for (int i = 0; i < similarityvaluevector.size(); i++) {
			s1 += similarityvaluevector.get(i);
			s2 += similarityvaluevector.get(i) * similarityvaluevector.get(i);
		}
		float value11 = s1 / similarityvaluevector.size();
		float ex2 = s2 / similarityvaluevector.size();
		float dx = ex2 - value11 * value11;
		float value22 = (float) Math.sqrt(dx);
		value1 = (float) (((int) (value11 * 100)) / 100.0);
		value2 = (float) (((int) (value22 * 100)) / 100.0);
		averageSimilarity = value1;
		meanDeviation = value2;

		// value3 & value4
		float s3 = 0;
		float s4 = 0;
		for (int i = 0; i < similaritystrvaluevector.size(); i++) {
			s3 += similaritystrvaluevector.get(i);
			s4 += similaritystrvaluevector.get(i)
					* similaritystrvaluevector.get(i);
		}
		float value33 = s3 / similaritystrvaluevector.size();
		float ex3 = s2 / similaritystrvaluevector.size();
		float dx1 = ex3 - value33 * value33;
		float value44 = (float) Math.sqrt(dx);
		value3 = (float) (((int) (value33 * 100)) / 100.0);
		value4 = (float) (((int) (value44 * 100)) / 100.0);
		averageStrSimilarity = value3;
		meanStrDeviation = value4;

		// averageBehStrSim
		averageSim = (value1 + value3) / 2;
		averageSim = (float) (((int) (averageSim * 1000)) / 1000.0);
	}

	public ArrayList<Float> EvaluationResult() {
		evaluationResult.add(averageSimilarity);
		evaluationResult.add(meanDeviation);
		evaluationResult.add(averageStrSimilarity);
		evaluationResult.add(meanStrDeviation);
		evaluationResult.add(averageSim);

		return evaluationResult;
	}

	private AnalysisConfiguration createAnalysisConfiguration() {
		AnalysisConfiguration f_option = new AnalysisConfiguration();
		f_option.setName("f");
		f_option.setToolTip("Degree of fit based on missing and remaining tokens in the model during log replay");
		f_option.setDescription("The token-based <b>fitness</b> metric <i>f</i> relates the amount of missing tokens during log replay with the amount of consumed ones and "
				+ "the amount of remaining tokens with the produced ones. If the log could be replayed correctly, that is, there were no tokens missing nor remaining, it evaluates to 1.");
		f_option.setNewAnalysisMethod(AnalysisMethodEnum.LOG_REPLAY);

		AnalysisConfiguration fitnessOptions = new AnalysisConfiguration();
		fitnessOptions.setName("Fitness");
		fitnessOptions.setToolTip("Fitness Analysis");
		fitnessOptions
				.setDescription("Fitness evaluates whether the observed process <i>complies with</i> the control flow specified by the process. "
						+ "One way to investigate the fitness is to replay the log in the Petri net. The log replay is carried out in a non-blocking way, i.e., if there are tokens missing "
						+ "to fire the transition in question they are created artificially and replay proceeds. While doing so, diagnostic data is collected and can be accessed afterwards.");
		fitnessOptions.addChildConfiguration(f_option);

		AnalysisConfiguration aB_option = new AnalysisConfiguration();
		aB_option.setName("saB");
		aB_option
				.setToolTip("Simple behavioral appropriateness based on the mean number of enabled transitions");
		aB_option
				.setDescription("The <b>simple behavioral appropriateness</b> metric <i>sa<sub>B</sub></i> is based on the mean number of enabled transitions during log replay "
						+ "(the greater the value the less behavior is allowed by the process model and the more precisely the behavior observed in the log is captured). "
						+ "Note that this metric should only be used as a comparative means for models without alternative duplicate tasks. "
						+ "Note further that in order to determine the mean number of enabled tasks in the presence of invisible tasks requires to build the state space "
						+ "from the current marking after each replay step. Since this may greatly decrease the performance of the computational process, you might want to swich this feature off.");
		aB_option.setNewAnalysisMethod(AnalysisMethodEnum.LOG_REPLAY);

		AnalysisConfiguration behAppropOptions = new AnalysisConfiguration();
		behAppropOptions.setName("Precision");
		behAppropOptions.setToolTip("Behavioral Appropriateness Analysis");
		behAppropOptions
				.setDescription("Precision, or Behavioral Appropriateness, evaluates <i>how precisely</i> the model describes the observed process.");
		behAppropOptions.addChildConfiguration(aB_option);

		AnalysisConfiguration analysisOptions = new AnalysisConfiguration();
		analysisOptions.addChildConfiguration(fitnessOptions);
		analysisOptions.addChildConfiguration(behAppropOptions);

		return analysisOptions;
	}
}
