package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import java.io.FileOutputStream;

import org.processmining.exporting.petrinet.PnmlExport;
import org.processmining.framework.log.LogFile;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogReaderFactory;
import org.processmining.framework.log.filter.DefaultLogFilter;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.mining.petrinetmining.PetriNetResult;

public class AlphaSharpMiner {

	private String inputFile = null, outputFile = null;
	private LogReader logReader = null;
	private PetriNet petriNet = null;

	// private LogFilter currentFilter = new SimpleLogFilter();

	public AlphaSharpMiner(String inputFile) {
		this.inputFile = inputFile;
		// this.outputFile = outputFile;
		// startMining();
		openLog();
		// mine();
		// Export();
	}

	public AlphaSharpMiner(String inputFile, String outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		// startMining();
		openLog();
		mine();
		Export();
	}

	/**
	 * Open the log, given the input file specified.
	 */
	public void openLog() {
		System.out.println("openLog");
		if (inputFile != null) {
			// Open the log.
			LogFile logFile = LogFile.getInstance(inputFile);
			try {
				System.out.println("filter1");
				logReader = LogReaderFactory
						.createInstance(new DefaultLogFilter(
								DefaultLogFilter.INCLUDE), logFile);
				// logReader = LogReaderFactory.createInstance(currentFilter,
				// logFile);
				System.out.println("filter2");
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		} else {
			System.err.println("No input file found.");
		}
	}

	/**
	 * Mine the log for a Petri net.
	 */
	public PetriNet mine() {
		System.out.println("mine");
		if (logReader != null) {
			// Mine the log for a Petri net.
			AlphaSharpProcessMiner miningPlugin = new AlphaSharpProcessMiner();
			PetriNetResult result = (PetriNetResult) miningPlugin
					.mine(logReader);
			petriNet = result.getPetriNet();
		} else {
			System.err.println("No log reader could be constructed.");
		}
		System.out.println("place" + petriNet.getPlaces().size());

		System.out.println("transition" + petriNet.getTransitions().size());
		return petriNet;
	}

	/**
	 * Export the mined Petri net to a PNML file.
	 */
	public void Export() {
		System.out.println("Export");
		if (petriNet == null) {
			System.out.println("empty");
		}
		if (petriNet != null) {
			// Export the Petri net as PNML.
			PnmlExport exportPlugin = new PnmlExport();
			Object[] objects = new Object[] { petriNet };
			ProvidedObject object = new ProvidedObject("temp", objects);
			FileOutputStream outputStream = null;
			try {
				if (outputFile != null) {
					outputStream = new FileOutputStream(outputFile);
				}
				// If no output file specified, write to System.out
				// However, some other thing smay get written to System.out as
				// well :-(.
				exportPlugin.export(object,
						(outputStream != null ? outputStream : System.out));
				System.out.println(outputFile + "succeeed");
				System.exit(0);
			} catch (Exception e) {
				System.err.println("Unable to write to file: " + e.toString());
			}
		} else {
			System.err.println("No Petri net could be constructed.");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AlphaSharpMiner alphaMiner = new AlphaSharpMiner("D://流程跟踪.mxml",
				"D://流程跟踪.pnml");
	}

}
