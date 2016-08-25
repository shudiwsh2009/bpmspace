package com.chinamobile.bpmspace.core.repository.processmining;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.processmining.exporting.DotPngExport;
import org.processmining.exporting.petrinet.PnmlExport;
import org.processmining.framework.log.LogFile;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogReaderFactory;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.plugin.ProvidedObject;

import com.chinamobile.bpmspace.core.repository.processmining.miner.AlphaMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.AlphaPlusPlusMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.AlphaSharpMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.DupTGeneticMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.GeneticMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.HeuristicMiner;
import com.chinamobile.bpmspace.core.repository.processmining.miner.Region_Miner;

public class ProcessMining {
	private AlphaMiner alpha = null;
	private AlphaPlusPlusMiner alphaPlusPlus = null;
	private AlphaSharpMiner alphaSharp = null;
	private GeneticMiner genetic = null;
	private DupTGeneticMiner dtGenetic = null;
	private HeuristicMiner heuristic = null;
	private Region_Miner region = null;

	private String inputFilePath = null, outputFilePath = null;
	private String outputFileName = null;
	private String imagePath = null;
	private String algorithm = null;
	private LogReader logReader = null;
	private PetriNet miningResult = null;

	public ProcessMining(String inputFile, String outputFile,
			String outputFileName, String algorithm) {
		inputFilePath = inputFile;
		outputFilePath = outputFile;
		imagePath = outputFilePath.replaceAll("models", "pngs");
		imagePath = imagePath.replaceAll(".pnml", ".png");
		this.outputFileName = outputFileName;
		this.algorithm = algorithm;
	}

	public void DoProcessMining() {
		try {
			LogFile logFile = LogFile.getInstance(inputFilePath);
			logReader = LogReaderFactory.createInstance(null, logFile);

			if (algorithm.equals("alpha")) {
				alpha = new AlphaMiner();
				miningResult = alpha.mine(logReader);
			} else if (algorithm.equals("alphaPlusPlus")) {
				alphaPlusPlus = new AlphaPlusPlusMiner();
				miningResult = alphaPlusPlus.mine(logReader);
			} else if (algorithm.equals("alphaSharp")) {
				alphaSharp = new AlphaSharpMiner();
				miningResult = alphaSharp.mine(logReader);
			} else if (algorithm.equals("genetic")) {
				genetic = new GeneticMiner();
				miningResult = genetic.mine(logReader);
			} else if (algorithm.equals("dtGenetic")) {
				dtGenetic = new DupTGeneticMiner();
				miningResult = dtGenetic.mine(logReader);
			} else if (algorithm.equals("heuristic")) {
				heuristic = new HeuristicMiner();
				miningResult = heuristic.mine(logReader);
			} else {
				region = new Region_Miner();
				miningResult = region.mine(logReader);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void export2png() {
		// get output image path, if no folder exists, create it
		String outputImageFolder = imagePath.substring(0,
				imagePath.lastIndexOf(File.separator));
		File dir = new File(outputImageFolder);
		dir.mkdirs();
		File outImage = new File(imagePath);

		// get provided object
		ProvidedObject po = new ProvidedObject("petrinet", miningResult);

		// dot png export
		DotPngExport dpe = new DotPngExport();

		try {
			OutputStream image = new FileOutputStream(outImage);
			dpe.export(po, image);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void export2pnml() {
		try {
			if (miningResult != null) {
				PnmlExport exportPlugin = new PnmlExport();
				Object[] objects = new Object[] { miningResult };
				ProvidedObject object = new ProvidedObject("temp", objects);

				// if no folder exists, create it
				String outputFileFolder = outputFileName.substring(0,
						outputFileName.lastIndexOf(File.separator));
				File dir = new File(outputFileFolder);
				dir.mkdirs();

				File file = new File(outputFilePath);
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				FileOutputStream outputStream = new FileOutputStream(
						outputFilePath);
				exportPlugin.export(object, outputStream);
				outputStream.close();

			} else {
				System.err.println("No Petri net could be constructed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
