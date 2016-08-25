package com.chinamobile.bpmspace.core.repository.conformancechecker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.processmining.analysis.conformance.ConformanceLogReplayResult;
import org.processmining.analysis.conformance.ConformanceMeasurer;
import org.processmining.analysis.conformance.DiagnosticLogEvent;
import org.processmining.analysis.conformance.DiagnosticLogEventRelation;
import org.processmining.analysis.conformance.DiagnosticTransition;
import org.processmining.analysis.conformance.MaximumSearchDepthDiagnosis;
import org.processmining.analysis.conformance.StateSpaceExplorationMethod;
import org.processmining.analysis.conformance.StateSpaceExplorationResult;
import org.processmining.analysis.conformance.StructuralAnalysisMethod;
import org.processmining.analysis.conformance.StructuralAnalysisResult;
import org.processmining.framework.log.LogFile;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogReaderFactory;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Transition;
import org.processmining.framework.models.petrinet.algorithms.logReplay.AnalysisConfiguration;
import org.processmining.framework.models.petrinet.algorithms.logReplay.AnalysisMethodEnum;
import org.processmining.framework.models.petrinet.algorithms.logReplay.AnalysisResult;
import org.processmining.framework.models.petrinet.algorithms.logReplay.LogReplayAnalysisMethod;
import org.processmining.framework.ui.Progress;

import com.chinamobile.bpmspace.core.repository.processmining.miner.AlphaMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.AlphaPlusPlusMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.AlphaSharpMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.DupTGeneticMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.GeneticMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.HeuristicMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.Region_Miner;

public class ConformanceChecker {
	private PetriNet myNet = null;
	private LogReader myLog = null;

	private AnalysisResult myResult = null;
	private LogReplayAnalysisMethod logReplayAnalysis = null;

	// input params
	private String inputFilePath = null;
	private String algorithm = null;
	private boolean fitness = false;
	private boolean precision = false;
	private boolean structure = false;
	private boolean f = false;
	private boolean pSE = false;
	private boolean pPC = false;
	private boolean saB = false;
	private boolean aaB = false;
	private boolean saS = false;
	private boolean aaS = false;

	// output params
	private ArrayList<Float> checkingResult = new ArrayList<Float>();
	private Float f_fitness = -0.1f;// fitness
	private Float p_sBA = -0.1f;// Simple Behavioral Appropriateness
	private Float p_aBA = -0.1f;// Advanced Behavioral Appropriateness
	private Float p_dMF = -0.1f;// Degree of Model Flexibility
	private Float s_sSA = -0.1f;// Simple Structure Appropriateness
	private Float s_aSA = -0.1f;// Advanced Structure Appropriateness

	public ConformanceChecker(String inputFilePath, String algorithm,
			boolean fitness, boolean f, boolean pSE, boolean pPC,
			boolean precision, boolean saB, boolean aaB, boolean structure,
			boolean saS, boolean aaS) {
		this.inputFilePath = inputFilePath;
		this.algorithm = algorithm;
		this.fitness = fitness;
		this.precision = precision;
		this.structure = structure;
		this.f = f;
		this.pSE = pSE;
		this.pPC = pPC;
		this.saB = saB;
		this.aaB = aaB;
		this.saS = saS;
		this.aaS = aaS;
	}

	// conformance checking
	public void Analysis() {
		AnalysisConfiguration myOptions = createAnalysisConfiguration();

		// log replay analysis method[LogReplayAnalysisMethod]
		LogReplayAnalysisMethod logReplayAnalysis = new LogReplayAnalysisMethod(
				myNet, myLog, new ConformanceMeasurer(), new Progress(0, 100));
		int maxSearchDepth = MaximumSearchDepthDiagnosis
				.determineMaximumSearchDepth(myNet);
		logReplayAnalysis.setMaxDepth(maxSearchDepth); // automatically set
														// maximum search depth
														// for log replay
		ConformanceLogReplayResult clrr = (ConformanceLogReplayResult) logReplayAnalysis
				.analyse(myOptions);

		// state space exploration method[StateSpaceExplorationMethod]
		StateSpaceExplorationMethod behAnalysis = new StateSpaceExplorationMethod(
				myNet, new Progress(0, 100));
		StateSpaceExplorationResult sser = (StateSpaceExplorationResult) behAnalysis
				.analyse(myOptions);

		// structural analysis method[StructuralAnalysisMethod]
		StructuralAnalysisMethod structuralAnalysis = new StructuralAnalysisMethod(
				myNet);
		StructuralAnalysisResult sar = (StructuralAnalysisResult) structuralAnalysis
				.analyse(myOptions);

		if (fitness == true)
			FitnessAnalysis(clrr);
		if (precision == true)
			PrecisionAnalysis(clrr, sser);
		if (structure == true)
			StructureAnalysis(sser, sar);
	}

	// [Fitness] analysis
	public void FitnessAnalysis(ConformanceLogReplayResult clrr) {
		// fitness[f_fitness]
		float fr = clrr.getFitnessMeasure();
		f_fitness = (float) (((int) (fr * 100)) / 100.0);
	}

	// [Precision] analysis
	public void PrecisionAnalysis(ConformanceLogReplayResult clrr,
			StateSpaceExplorationResult sser) {
		if (sser.calculateImprovedBehavioralAppropriateness() == true) { // configurated
			// clean up previous results first
			cleanLogAndModelRelations(sser, clrr);
			// match the log-derived relations with the model-derived relations
			// and remember result in the diagnostic Petri net
			matchLogAndModelRelations(sser, clrr);
		}

		// Simple Behavioral Appropriateness[p_sBA]
		try {
			float sbar = clrr.getBehavioralAppropriatenessMeasure();
			p_sBA = (float) (((int) (sbar * 100)) / 100.0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Advanced Behavioral Appropriateness[p_aBA]
		try {
			float abar = sser.getImprovedBehavioralAppropriatenessMeasure();
			p_aBA = (float) (((int) (abar * 100)) / 100.0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Degree of Model Flexibility[p_dMF]
		float dmfr = sser.getDegreeOfModelFlexibility();
		p_dMF = (float) (((int) (dmfr * 100)) / 100.0);
	}

	// [Structure] analysis
	public void StructureAnalysis(StateSpaceExplorationResult sser,
			StructuralAnalysisResult sar) {
		// Simple Structure Appropriateness
		float ssar = sar.getStructuralAppropriatenessMeasure();
		s_sSA = (float) (((int) (ssar * 100)) / 100.0);

		// Advanced Structure Appropriateness
		float asar = sser.getImprovedStructuralAppropriatenessMeasure();
		s_aSA = (float) (((int) (asar * 100)) / 100.0);
	}

	// minging the input file and get myNet[PetriNet]
	public void Mining() {
		try {
			LogFile logFile = LogFile.getInstance(inputFilePath);
			myLog = LogReaderFactory.createInstance(null, logFile);

			if (algorithm.equals("alpha")) {
				AlphaMiner alpha = new AlphaMiner();
				myNet = alpha.mine(myLog);
			} else if (algorithm.equals("alphaPlusPlus")) {
				AlphaPlusPlusMiner alphaPlusPlus = new AlphaPlusPlusMiner();
				myNet = alphaPlusPlus.mine(myLog);
			} else if (algorithm.equals("alphaSharp")) {
				AlphaSharpMiner alphaSharp = new AlphaSharpMiner();
				myNet = alphaSharp.mine(myLog);
			} else if (algorithm.equals("genetic")) {
				GeneticMiner genetic = new GeneticMiner();
				myNet = genetic.mine(myLog);
			} else if (algorithm.equals("dtGenetic")) {
				DupTGeneticMiner dtGenetic = new DupTGeneticMiner();
				myNet = dtGenetic.mine(myLog);
			} else if (algorithm.equals("heuristic")) {
				HeuristicMiner heuristic = new HeuristicMiner();
				myNet = heuristic.mine(myLog);
			} else {
				Region_Miner region = new Region_Miner();
				myNet = region.mine(myLog);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// set analysis configuration
	private AnalysisConfiguration createAnalysisConfiguration() {
		AnalysisConfiguration analysisOptions = new AnalysisConfiguration();
		AnalysisConfiguration fitnessOptions = createFitnessAnalysisConfiguration();
		AnalysisConfiguration behAppropOptions = createPrecisionAnalysisConfiguration();
		AnalysisConfiguration structAppropOptions = createStructureAnalysisConfiguration();

		if (fitness == false)
			fitnessOptions.setSelected(false);
		if (precision == false)
			behAppropOptions.setSelected(false);
		if (structure == false)
			structAppropOptions.setSelected(false);

		// add to analysis configuration
		analysisOptions.addChildConfiguration(fitnessOptions);
		analysisOptions.addChildConfiguration(behAppropOptions);
		analysisOptions.addChildConfiguration(structAppropOptions);

		return analysisOptions;
	}

	// set fitness analysis configuration
	private AnalysisConfiguration createFitnessAnalysisConfiguration() {
		AnalysisConfiguration fitnessOptions = new AnalysisConfiguration();
		fitnessOptions.setName("Fitness");
		fitnessOptions.setToolTip("Fitness Analysis");
		fitnessOptions
				.setDescription("Fitness evaluates whether the observed process <i>complies with</i> the control flow specified by the process. "
						+ "One way to investigate the fitness is to replay the log in the Petri net. The log replay is carried out in a non-blocking way, i.e., if there are tokens missing "
						+ "to fire the transition in question they are created artificially and replay proceeds. While doing so, diagnostic data is collected and can be accessed afterwards.");

		// f
		AnalysisConfiguration f_option = new AnalysisConfiguration();
		f_option.setName("f");
		if (f == false)
			f_option.setSelected(false);
		f_option.setToolTip("Degree of fit based on missing and remaining tokens in the model during log replay");
		f_option.setDescription("The token-based <b>fitness</b> metric <i>f</i> relates the amount of missing tokens during log replay with the amount of consumed ones and "
				+ "the amount of remaining tokens with the produced ones. If the log could be replayed correctly, that is, there were no tokens missing nor remaining, it evaluates to 1.");
		f_option.setNewAnalysisMethod(AnalysisMethodEnum.LOG_REPLAY);

		// degree of successful execution
		AnalysisConfiguration fractionSE_option = new AnalysisConfiguration();
		fractionSE_option.setName("pSE");
		if (pSE == true)
			fractionSE_option.setSelected(false);
		fractionSE_option
				.setToolTip("The fraction of successfully executed process instances");
		fractionSE_option
				.setDescription("The <b>successful execution</b> metric <i>p<sub>SE</sub></i> determines the fraction of successfully executed process instances (taking the number of occurrences per trace into account).");
		fractionSE_option.setNewAnalysisMethod(AnalysisMethodEnum.LOG_REPLAY);

		// degree of proper completion
		AnalysisConfiguration fractionPC_option = new AnalysisConfiguration();
		fractionPC_option.setName("pPC");
		if (pPC == true)
			fractionPC_option.setSelected(false);
		fractionPC_option
				.setToolTip("The fraction of properly completed process instances");
		fractionPC_option
				.setDescription("The <b>proper completion</b> metric <i>p<sub>PC</sub></i> determines the fraction of properly completed process instances (taking the number of occurrences per trace into account).");
		fractionPC_option.setNewAnalysisMethod(AnalysisMethodEnum.LOG_REPLAY);

		// add to fitness options[fitnessOptions]
		fitnessOptions.addChildConfiguration(f_option);
		fitnessOptions.addChildConfiguration(fractionSE_option);
		fitnessOptions.addChildConfiguration(fractionPC_option);

		// indicate the type of analysis method that is needed
		fitnessOptions.addRequestedMethod(AnalysisMethodEnum.LOG_REPLAY);

		return fitnessOptions;
	}

	// set precision analysis configuration
	private AnalysisConfiguration createPrecisionAnalysisConfiguration() {
		AnalysisConfiguration behAppropOptions = new AnalysisConfiguration();
		behAppropOptions.setName("Precision");
		behAppropOptions.setToolTip("Behavioral Appropriateness Analysis");
		behAppropOptions
				.setDescription("Precision, or Behavioral Appropriateness, evaluates <i>how precisely</i> the model describes the observed process.");

		// saB
		AnalysisConfiguration aB_option = new AnalysisConfiguration();
		aB_option.setName("saB");
		if (saB == false)
			aB_option.setSelected(false);
		aB_option
				.setToolTip("Simple behavioral appropriateness based on the mean number of enabled transitions");
		aB_option
				.setDescription("The <b>simple behavioral appropriateness</b> metric <i>sa<sub>B</sub></i> is based on the mean number of enabled transitions during log replay "
						+ "(the greater the value the less behavior is allowed by the process model and the more precisely the behavior observed in the log is captured). "
						+ "Note that this metric should only be used as a comparative means for models without alternative duplicate tasks. "
						+ "Note further that in order to determine the mean number of enabled tasks in the presence of invisible tasks requires to build the state space "
						+ "from the current marking after each replay step. Since this may greatly decrease the performance of the computational process, you might want to swich this feature off.");
		aB_option.setNewAnalysisMethod(AnalysisMethodEnum.LOG_REPLAY);

		// aaB
		AnalysisConfiguration aaB_option = new AnalysisConfiguration();
		aaB_option.setName("aaB");
		if (aaB == false)
			aaB_option.setSelected(false);
		aaB_option
				.setToolTip("Advanced behavioral appropriateness based on activity relations that were not observed i the log");
		aaB_option
				.setDescription("The <b>advanced behavioral appropriateness</b> metric <i>aa<sub>B</sub></i> is based on successorship relations among activities with respect the event relations observed  in the log "
						+ "(the greater the value the more precisely the behavior observed in the log is captured).");
		aaB_option.setNewAnalysisMethod(AnalysisMethodEnum.LOG_REPLAY);
		aaB_option.setNewAnalysisMethod(AnalysisMethodEnum.STATE_SPACE);

		// add to precision options[behAppropOptions]
		behAppropOptions.addChildConfiguration(aB_option);
		behAppropOptions.addChildConfiguration(aaB_option);

		// indicate the type of analysis method that is needed
		behAppropOptions.addRequestedMethod(AnalysisMethodEnum.LOG_REPLAY);
		behAppropOptions.addRequestedMethod(AnalysisMethodEnum.STATE_SPACE); // for
																				// improved
																				// metric!

		return behAppropOptions;
	}

	// set structural analysis configuration
	private AnalysisConfiguration createStructureAnalysisConfiguration() {
		AnalysisConfiguration structAppropOptions = new AnalysisConfiguration();
		structAppropOptions.setName("Structure");
		structAppropOptions.setToolTip("Structural Appropriateness Analysis");
		structAppropOptions
				.setDescription("Structural Appropriateness evaluates whether the model describes the observed process in a <i>structurally suitable</i> way.");

		// saS
		AnalysisConfiguration aS_option = new AnalysisConfiguration();
		aS_option.setName("saS");
		if (saS == false)
			aS_option.setSelected(false);
		aS_option
				.setToolTip("Simple structural appropriateness based on the size of the process model");
		aS_option
				.setDescription("The <b>simple structural appropriateness</b> metric <i>sa<sub>S</sub></i> is a simple metric based on the graph size of the model "
						+ "(the greater the value the more compact is the model). "
						+ "Note that this metric should only be used as a comparative means for models allowing for the same amount of behavior.");
		aS_option.setNewAnalysisMethod(AnalysisMethodEnum.STRUCTURAL);

		// aaS
		AnalysisConfiguration aaS_option = new AnalysisConfiguration();
		aaS_option.setName("aaS");
		if (aaS == false)
			aaS_option.setSelected(false);
		aaS_option
				.setToolTip("Advanced structural appropriateness based on the punishement of redundant invisible and alternative duplicate tasks.");
		aaS_option
				.setDescription("The <b>advanced structural appropriateness</b> metric <i>aa<sub>S</sub></i> is based on the detection of redundant invisible tasks (simply superfluous) "
						+ "and alternative duplicate tasks (list alternative behavior rather than expressing it in a meaningful way).");
		aaS_option.setNewAnalysisMethod(AnalysisMethodEnum.STATE_SPACE);
		aaS_option.setNewAnalysisMethod(AnalysisMethodEnum.STRUCTURAL);

		// add to structural options[structAppropOptions]
		structAppropOptions.addChildConfiguration(aS_option);
		structAppropOptions.addChildConfiguration(aaS_option);

		// indicate the type of analysis method that is needed
		structAppropOptions.addRequestedMethod(AnalysisMethodEnum.STRUCTURAL);
		structAppropOptions.addRequestedMethod(AnalysisMethodEnum.STATE_SPACE); // for
																				// improved
																				// metric!

		return structAppropOptions;
	}

	// mining results
	public ArrayList<Float> getResults() {
		checkingResult.add(f_fitness);
		checkingResult.add(p_sBA);
		checkingResult.add(p_aBA);
		checkingResult.add(p_dMF);
		checkingResult.add(s_sSA);
		checkingResult.add(s_aSA);
		return checkingResult;
	}

	// for PrecisionAnalysis()
	public void cleanLogAndModelRelations(
			StateSpaceExplorationResult stateSpaceResult,
			ConformanceLogReplayResult replayResult) {
		// clean Always and Never relations stored at the Diagnostic transitions
		Iterator<Transition> transitions = stateSpaceResult.exploredPetriNet
				.getTransitions().iterator();
		while (transitions.hasNext()) {
			DiagnosticTransition trans = (DiagnosticTransition) transitions
					.next();
			trans.resetAlwaysAndNeverRelations();
		}
		// clean sometimes relation counter
		stateSpaceResult.resetSometimesRelationCounter();
		// normalize log-based relation with respect to activities contained in
		// model-based relation
		Collection<DiagnosticLogEvent> modelRelationElements = stateSpaceResult
				.getActivityRelations().getDiagnosticLogEvents();
		DiagnosticLogEventRelation logRelation = replayResult
				.getLogEventRelations();
		logRelation
				.completeRelationByExternalZeroEntries(modelRelationElements);
	}

	public void matchLogAndModelRelations(
			StateSpaceExplorationResult stateSpaceResult,
			ConformanceLogReplayResult replayResult) {

		DiagnosticLogEventRelation modelRelation = stateSpaceResult
				.getActivityRelations();
		DiagnosticLogEventRelation logRelation = replayResult
				.getLogEventRelations();

		// / match forward relations
		Iterator<DiagnosticLogEvent> modelElements = modelRelation
				.getDiagnosticLogEvents().iterator();
		while (modelElements.hasNext()) {
			DiagnosticLogEvent element = modelElements.next();
			// find all transitions belonging to these two log events (may
			// contain only one transition if
			// no duplicate task)
			ArrayList<Transition> transitions = stateSpaceResult.exploredPetriNet
					.findTransitions(element);

			// / walk through sometimes relation from model
			Iterator<DiagnosticLogEvent> modelSometimesFW = element
					.getSometimesRelationForwards(0).iterator();
			while (modelSometimesFW.hasNext()) {
				DiagnosticLogEvent targetElement = modelSometimesFW.next();
				// find all transitions belonging to this target relation
				// element (log event)
				ArrayList<Transition> targetTransitions = stateSpaceResult.exploredPetriNet
						.findTransitions(targetElement);
				// record sometimes relation counter
				stateSpaceResult.incSFModel();

				// if not found in sometimes relation in log -> must be in
				// always or never
				// TODO - either use toString() method in internal hashMaps or
				// make extra method producing this string!
				if (logRelation.areInSFRelation(
						element.getModelElementName() + element.getEventType(),
						targetElement.getModelElementName()
								+ targetElement.getEventType()) == true) {
					stateSpaceResult.incSFLog();
				} else if (logRelation.areInAFRelation(
						element.getModelElementName() + element.getEventType(),
						targetElement.getModelElementName()
								+ targetElement.getEventType()) == true) {
					// record transitions for AF relation (for all potential
					// duplicates)
					Iterator<Transition> transitionsIt = transitions.iterator();
					while (transitionsIt.hasNext()) {
						DiagnosticTransition from = (DiagnosticTransition) transitionsIt
								.next();
						Iterator<Transition> targetTransitionsIt = targetTransitions
								.iterator();
						while (targetTransitionsIt.hasNext()) {
							Transition to = targetTransitionsIt.next();
							from.addAlwaysFollows(to);
						}
					}
				} else if (logRelation.areInNFRelation(
						element.getModelElementName() + element.getEventType(),
						targetElement.getModelElementName()
								+ targetElement.getEventType()) == true) {
					// record transitions for NF relation (for all potential
					// duplicates)
					Iterator<Transition> transitionsIt = transitions.iterator();
					while (transitionsIt.hasNext()) {
						DiagnosticTransition from = (DiagnosticTransition) transitionsIt
								.next();
						Iterator<Transition> targetTransitionsIt = targetTransitions
								.iterator();
						while (targetTransitionsIt.hasNext()) {
							Transition to = targetTransitionsIt.next();
							from.addNeverFollows(to);
						}
					}
				}
			}

			// match backward relations
			Iterator<DiagnosticLogEvent> modelSometimesBW = element
					.getSometimesRelationsBackwards(0).iterator();
			while (modelSometimesBW.hasNext()) {
				DiagnosticLogEvent targetElement = modelSometimesBW.next();
				// find all transitions belonging to this target relation
				// element (log event)
				ArrayList<Transition> targetTransitions = stateSpaceResult.exploredPetriNet
						.findTransitions(targetElement);
				// record sometimes relation counter
				stateSpaceResult.incSBModel();

				// / if not found in sometimes relation in log -> must be in
				// always or never
				if (logRelation.areInSBRelation(
						element.getModelElementName() + element.getEventType(),
						targetElement.getModelElementName()
								+ targetElement.getEventType()) == true) {
					stateSpaceResult.incSBLog();
				} else if (logRelation.areInABRelation(
						element.getModelElementName() + element.getEventType(),
						targetElement.getModelElementName()
								+ targetElement.getEventType()) == true) {
					// record transitions for AB relation (for all potential
					// duplicates)
					Iterator<Transition> transitionsIt = transitions.iterator();
					while (transitionsIt.hasNext()) {
						DiagnosticTransition from = (DiagnosticTransition) transitionsIt
								.next();
						Iterator<Transition> targetTransitionsIt = targetTransitions
								.iterator();
						while (targetTransitionsIt.hasNext()) {
							Transition to = targetTransitionsIt.next();
							from.addAlwaysPrecedes(to);
						}
					}
				} else if (logRelation.areInNBRelation(
						element.getModelElementName() + element.getEventType(),
						targetElement.getModelElementName()
								+ targetElement.getEventType()) == true) {
					// record transitions for NB relation (for all potential
					// duplicates)
					Iterator<Transition> transitionsIt = transitions.iterator();
					while (transitionsIt.hasNext()) {
						DiagnosticTransition from = (DiagnosticTransition) transitionsIt
								.next();
						Iterator<Transition> targetTransitionsIt = targetTransitions
								.iterator();
						while (targetTransitionsIt.hasNext()) {
							Transition to = targetTransitionsIt.next();
							from.addNeverPrecedes(to);
						}
					}
				}
			}
		}
	}
}
