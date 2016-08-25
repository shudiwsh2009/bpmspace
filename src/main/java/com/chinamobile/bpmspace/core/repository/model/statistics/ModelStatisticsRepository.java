package com.chinamobile.bpmspace.core.repository.model.statistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.importing.pnml.PnmlImport;

import cn.edu.thss.iise.beehivez.server.metric.PetriNetMetrics;
import cn.edu.thss.iise.beehivez.server.util.PetriNetUtil;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.ActionRejectException;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.util.FileUtil;

public class ModelStatisticsRepository {
	private MongoAccess mongo = new MongoAccess();

	public String modelStatistics(String path, String processIds,
			String options, String userId) throws BasicException {
		if (userId == null || userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		User user = mongo.getUserById(userId);
		if (user == null) {
			throw new NoExistException("用户不存在");
		}
		if (path == null) {
			throw new EmptyFieldException("路径不存在");
		} else if (processIds == null) {
			throw new EmptyFieldException("模型不存在");
		} else if (path.equals("") && processIds.equals("")) {
			throw new EmptyFieldException("未选取模型");
		}

		// File folder = new File(path);
		// if (!folder.exists() || !folder.isDirectory()) {
		// throw new NoExistException("路径不存在");
		// }
		if (options.length() != 48) {
			throw new EmptyFieldException("特征选项错误");
		}
		for (char c : options.toCharArray()) {
			if (c != '0' && c != '1') {
				throw new ActionRejectException("特征选项错误");
			}
		}

		if (path.equals("")) {
			path = FileUtil.WEBAPP_ROOT + FileUtil.STATISTICS_PREFIX
					+ user.getId() + "_" + System.nanoTime() + File.separator;
		}
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		List<Process> processList = new ArrayList<Process>();
		String[] _processIds = processIds.split(":");
		for (String pId : _processIds) {
			Process p = mongo.getProcessById(pId);
			if (p == null || p.getType() != ProcessType.PETRINET) {
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
			mongo.getFileByFilename(model.getXmlFilename(), path + modelFile);
		}
		File[] fileList = folder.listFiles();
		List<File> tempExtractFile = new ArrayList<File>();
		for (File f : fileList) {
			if (f.getAbsolutePath().endsWith(FileUtil.PNML_SUFFIX)) {
				tempExtractFile.add(f);
			}
		}
		File[] toExtractFile = tempExtractFile.toArray(new File[0]);
		String outputPath = FileUtil.WEBAPP_ROOT + FileUtil.STATISTICS_PREFIX
				+ user.getUsername() + "_" + System.nanoTime()
				+ FileUtil.STATISTICS_SUFFIX;
		try {
			this.extractAndSaveFeature(userId, toExtractFile, options,
					outputPath);
			FileUtil.deleteFile(path);
			return outputPath;
		} catch (IOException e) {
			throw new BasicException("未知错误");
		}
	}

	public String modelStatistics(String[] filepaths, String[] processIds,
			String options, String userId) throws BasicException {
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

		if (options.length() != 48) {
			throw new EmptyFieldException("特征选项错误");
		}
		for (char c : options.toCharArray()) {
			if (c != '0' && c != '1') {
				throw new ActionRejectException("特征选项错误");
			}
		}

		String serverPath = FileUtil.STATISTICS_PREFIX + user.getId() + "_"
				+ System.nanoTime() + File.separator;
		String localPath = FileUtil.WEBAPP_ROOT + serverPath;
		File folder = new File(localPath + "model" + File.separator);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		List<Process> processList = new ArrayList<Process>();
		for (String pId : processIds) {
			Process p = mongo.getProcessById(pId);
			if (p == null || p.getType() != ProcessType.PETRINET) {
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

		String statisticsFile = "Feature_" + System.nanoTime()
				+ FileUtil.STATISTICS_SUFFIX;
		List<File> tempExtractFile = new ArrayList<File>();
		for (String fName : filenames) {
			File file = new File(fName);
			if (file.exists() && fName.endsWith(FileUtil.PNML_SUFFIX)) {
				tempExtractFile.add(file);
			}
		}
		File[] toExtractFile = tempExtractFile.toArray(new File[0]);
		try {
			this.extractAndSaveFeature(userId, toExtractFile, options,
					localPath + statisticsFile);
			return serverPath + statisticsFile;
		} catch (IOException e) {
			throw new BasicException("未知错误");
		}
	}

	private String extractAndSaveFeature(String username, File[] toExtractFile,
			String options, String outputPath) throws BasicException,
			IOException {
		File f = new File(outputPath);
		if (f.exists() == false) {
			f.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
		this.writeTitle(bw, options);
		for (File file : toExtractFile) {
			try {
				String fileName = file.getName();
				List<Double> statistics = this.extractSingleFeature(file,
						options);
				this.writeSingleFile(bw, statistics, fileName);
			} catch (BasicException e) {
				continue;
			}
		}
		bw.close();
		return outputPath;
	}

	private void writeSingleFile(BufferedWriter bw, List<Double> statistics,
			String fileName) throws IOException {
		bw.write(fileName + ",");
		for (Double d : statistics) {
			bw.write(String.valueOf(d) + ",");
		}
		bw.newLine();
	}

	private void writeTitle(BufferedWriter bw, String options)
			throws BasicException {
		try {
			char[] optionsArray = options.toCharArray();
			bw.write("file name,");

			if (optionsArray[0] == '1')
				bw.write("number of transitions,");
			if (optionsArray[1] == '1')
				bw.write("number of places,");
			if (optionsArray[2] == '1')
				bw.write("number of arcs,");
			if (optionsArray[3] == '1')
				bw.write("Edge density,");
			if (optionsArray[4] == '1')
				bw.write("max inDegree,");
			if (optionsArray[5] == '1')
				bw.write("max outDegree,");
			if (optionsArray[6] == '1')
				bw.write("number of tars,");

			if (optionsArray[7] == '1')
				bw.write("number of and-split,");
			if (optionsArray[8] == '1')
				bw.write("min degree of and-split,");
			if (optionsArray[9] == '1')
				bw.write("max degree of and-split,");
			if (optionsArray[10] == '1')
				bw.write("average degree of and-split,");
			if (optionsArray[11] == '1')
				bw.write("stdev degree of and-split,");

			if (optionsArray[12] == '1')
				bw.write("number of and-join,");
			if (optionsArray[13] == '1')
				bw.write("min degree of and-join,");
			if (optionsArray[14] == '1')
				bw.write("max degree of and-join,");
			if (optionsArray[15] == '1')
				bw.write("average degree of and-join,");
			if (optionsArray[16] == '1')
				bw.write("stdev degree of and-join,");

			if (optionsArray[17] == '1')
				bw.write("number of xor-split,");
			if (optionsArray[18] == '1')
				bw.write("min degree of xor-split,");
			if (optionsArray[19] == '1')
				bw.write("max degree of xor-split,");
			if (optionsArray[20] == '1')
				bw.write("average degree of xor-split,");
			if (optionsArray[21] == '1')
				bw.write("stdev degree of xor-split,");

			if (optionsArray[22] == '1')
				bw.write("number of xor-join,");
			if (optionsArray[23] == '1')
				bw.write("min degree of xor-join,");
			if (optionsArray[24] == '1')
				bw.write("max degree of xor-join,");
			if (optionsArray[25] == '1')
				bw.write("average degree of xor-join,");
			if (optionsArray[26] == '1')
				bw.write("stdev degree of xor-join,");

			if (optionsArray[27] == '1')
				bw.write("number of state in state-space,");
			if (optionsArray[28] == '1')
				bw.write("AND-XOR mismatch,");
			if (optionsArray[29] == '1')
				bw.write("Sequentiality,");
			if (optionsArray[30] == '1')
				bw.write("TS,");
			if (optionsArray[31] == '1')
				bw.write("CH,");
			if (optionsArray[32] == '1')
				bw.write("CFC,");
			if (optionsArray[33] == '1')
				bw.write("CYC,");
			if (optionsArray[34] == '1')
				bw.write("Diam,");
			if (optionsArray[35] == '1')
				bw.write("Separability,");
			if (optionsArray[36] == '1')
				bw.write("Structuredness,");
			if (optionsArray[37] == '1')
				bw.write("CNC,");
			if (optionsArray[38] == '1')
				bw.write("MaxDegree of conecetor,");
			if (optionsArray[39] == '1')
				bw.write("AverDegree of conector,");
			if (optionsArray[40] == '1')
				bw.write("Depth,");

			if (optionsArray[41] == '1')
				bw.write("Number of Invisible Tasks,");
			if (optionsArray[42] == '1')
				bw.write("Number of Duplicate Tasks,");
			if (optionsArray[43] == '1')
				bw.write("Non-Free Choice,");
			if (optionsArray[44] == '1')
				bw.write("Arbitary Cycle,");
			if (optionsArray[45] == '1')
				bw.write("Or-Join,");
			if (optionsArray[46] == '1')
				bw.write("Short-Loop,");
			if (optionsArray[47] == '1')
				bw.write("Nested-Loop,");
			bw.newLine();
		} catch (IOException e) {
			throw new ActionRejectException("文件读写失败");
		}
	}

	private List<Double> extractSingleFeature(File file, String options)
			throws BasicException {
		char[] optionsArray = options.toCharArray();

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

		PetriNetMetrics pnm = new PetriNetMetrics(pn);
		ArrayList<Double> singleResult = new ArrayList<Double>();
		if (optionsArray[0] == '1')
			singleResult.add((double) pnm.getNumberOfTransitions());
		if (optionsArray[1] == '1')
			singleResult.add((double) pnm.getNumberOfPlaces());
		if (optionsArray[2] == '1')
			singleResult.add((double) pnm.getNumberOfArcs());
		if (optionsArray[3] == '1')
			singleResult.add((double) pnm.getDensity());
		if (optionsArray[4] == '1')
			singleResult.add((double) pnm.getMaxInDegree());
		if (optionsArray[5] == '1')
			singleResult.add((double) pnm.getMaxOutDegree());
		if (optionsArray[6] == '1')
			singleResult.add((double) pnm.getNumberOfTARs());

		float[] ret = pnm.analyzeANDSplitDegree();
		if (optionsArray[7] == '1')
			singleResult.add((double) (Math.round(ret[0])));
		if (optionsArray[8] == '1')
			singleResult.add((double) (Math.round(ret[1])));
		if (optionsArray[9] == '1')
			singleResult.add((double) (Math.round(ret[2])));
		if (optionsArray[10] == '1')
			singleResult.add((double) ret[3]);
		if (optionsArray[11] == '1')
			singleResult.add((double) ret[4]);

		ret = pnm.analyzeANDJoinDegree();
		if (optionsArray[12] == '1')
			singleResult.add((double) (Math.round(ret[0])));
		if (optionsArray[13] == '1')
			singleResult.add((double) (Math.round(ret[1])));
		if (optionsArray[14] == '1')
			singleResult.add((double) (Math.round(ret[2])));
		if (optionsArray[15] == '1')
			singleResult.add((double) ret[3]);
		if (optionsArray[16] == '1')
			singleResult.add((double) ret[4]);

		ret = pnm.analyzeXORSplitDegree();
		if (optionsArray[17] == '1')
			singleResult.add((double) (Math.round(ret[0])));
		if (optionsArray[18] == '1')
			singleResult.add((double) (Math.round(ret[1])));
		if (optionsArray[19] == '1')
			singleResult.add((double) (Math.round(ret[2])));
		if (optionsArray[20] == '1')
			singleResult.add((double) ret[3]);
		if (optionsArray[21] == '1')
			singleResult.add((double) ret[4]);

		ret = pnm.analyzeXORJoinDegree();
		if (optionsArray[22] == '1')
			singleResult.add((double) (Math.round(ret[0])));
		if (optionsArray[23] == '1')
			singleResult.add((double) (Math.round(ret[1])));
		if (optionsArray[24] == '1')
			singleResult.add((double) (Math.round(ret[2])));
		if (optionsArray[25] == '1')
			singleResult.add((double) ret[3]);
		if (optionsArray[26] == '1')
			singleResult.add((double) ret[4]);

		int[] ret1 = pnm.analyzeStateSpace();
		if (optionsArray[27] == '1')
			singleResult.add((double) ret1[0]);
		// singleResult.add((double)ret1[1]);

		if (optionsArray[28] == '1')
			singleResult.add((double) pnm.getMismatch());

		if (optionsArray[29] == '1')
			singleResult.add((double) pnm.getSequentiality());

		if (optionsArray[30] == '1')
			singleResult.add((double) pnm.getTS());

		if (optionsArray[31] == '1')
			singleResult.add((double) pnm.getCH());

		if (optionsArray[32] == '1')
			singleResult.add((double) pnm.getCFC());

		if (optionsArray[33] == '1')
			singleResult.add((double) pnm.getCYC());

		if (optionsArray[34] == '1')
			singleResult.add((double) pnm.getDiam());

		if (optionsArray[35] == '1')
			singleResult.add((double) pnm.getSeparability());

		if (optionsArray[36] == '1')
			singleResult.add((double) pnm.getStructuredness());

		if (optionsArray[37] == '1')
			singleResult.add((double) pnm.getCNC());

		if (optionsArray[38] == '1')
			singleResult.add((double) pnm.getMaxDegree());

		if (optionsArray[39] == '1')
			singleResult.add((double) pnm.getAverDegree());

		if (optionsArray[40] == '1')
			singleResult.add((double) pnm.getDepth());
		if (optionsArray[41] == '1')
			singleResult.add((double) pn.getInvisibleTasks().size());
		if (optionsArray[42] == '1')
			singleResult.add((double) pn.getNumberOfDuplicateTasks());
		if (optionsArray[43] == '1')
			singleResult
					.add((double) PetriNetUtil.getNumberofNonFreeChoice(pn));
		if (optionsArray[44] == '1')
			singleResult
					.add((double) PetriNetUtil.getNumberofArbitaryCycle(pn));
		if (optionsArray[45] == '1')
			singleResult.add((double) PetriNetUtil.getNumberofOrJoin(pn));
		if (optionsArray[46] == '1')
			singleResult.add((double) PetriNetUtil.getNumberofSimpleLoop(pn));
		if (optionsArray[47] == '1')
			singleResult.add((double) PetriNetUtil.getNumberofNestedLoop(pn));
		return singleResult;
	}

	public void deleteStatisticsTempFiles(String filepaths, String userId) {
		String prefix = FileUtil.WEBAPP_ROOT + FileUtil.STATISTICS_PREFIX
				+ userId;
		String[] paths = filepaths.split(FileUtil.SPLITTER);
		for (String p : paths) {
			if (p.contains(prefix)) {
				File file = new File(p);
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			}
		}
	}
}
