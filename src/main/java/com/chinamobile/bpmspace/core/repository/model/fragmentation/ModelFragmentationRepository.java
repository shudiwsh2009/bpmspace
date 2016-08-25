package com.chinamobile.bpmspace.core.repository.model.fragmentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cn.edu.thss.iise.xiaohan.abpcd.graph.EPCHelper;
import cn.edu.thss.iise.xiaohan.abpcd.graph.rpsdag.DBSCAN;
import cn.edu.thss.iise.xiaohan.abpcd.graph.rpsdag.RPSDag;
import cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc.EPC;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.chinamobile.bpmspace.core.util.TimeUtil;

public class ModelFragmentationRepository {

	private MongoAccess mongo = new MongoAccess();

	public static void main(String[] args) throws BasicException, IOException {
		ModelFragmentationRepository mfr = new ModelFragmentationRepository();
		FileUtil.WEBAPP_ROOT = "D:/Workspace/apache-tomcat-8.0.9/webapps/bpmspace/";
		String[] filepaths = { "C:\\Users\\shudi\\Desktop\\epc\\1.epml",
				"C:\\Users\\shudi\\Desktop\\epc\\2.epml",
				"C:\\Users\\shudi\\Desktop\\epc\\3.epml",
				"C:\\Users\\shudi\\Desktop\\epc\\4.epml",
				"C:\\Users\\shudi\\Desktop\\epc\\5.epml",
				"C:\\Users\\shudi\\Desktop\\epc\\6.epml" };
		System.out.println(mfr.modelFragmentation(filepaths, null,
				"53c64e84a3f5e7f6c66d7094"));
	}

	public String modelFragmentation(String[] filepaths, String[] processIds,
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

		String serverPath = FileUtil.FRAG_PREFIX + user.getId() + "_"
				+ System.nanoTime() + File.separator;
		String localPath = FileUtil.WEBAPP_ROOT + serverPath;
		File folder = new File(localPath + "model" + File.separator);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		List<Process> processList = new ArrayList<Process>();
		for (String pId : processIds) {
			Process p = mongo.getProcessById(pId);
			if (p == null || p.getType() != ProcessType.EPC) {
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
			throw new NoExistException("没有可供片段化的模型");
		}

		String dotFolderPath = this.rpst(filenames, localPath);
		try {
			File dotFolder = new File(dotFolderPath);
			File[] dotFiles = dotFolder.listFiles();
			if (dotFiles.length == 0) {
				return "";
			} else if (dotFiles.length == 1) {
				return serverPath + "dot" + File.separator
						+ dotFiles[0].getName();
			} else {
				String zipFilename = user.getUsername() + "_"
						+ TimeUtil.getCurrentYMD() + FileUtil.ZIP_SUFFIX;
				File zipFile = new File(dotFolderPath + zipFilename);
				ZipOutputStream zipOut = new ZipOutputStream(
						new FileOutputStream(zipFile));
				int temp = 0;
				for (File localFile : dotFiles) {
					InputStream input = new FileInputStream(localFile);
					zipOut.putNextEntry(new ZipEntry(localFile.getName()));
					while ((temp = input.read()) != -1) {
						zipOut.write(temp);
					}
					input.close();
				}
				zipOut.close();
				return serverPath + "dot" + File.separator + zipFilename;
			}
		} catch (FileNotFoundException e) {
			throw new NoExistException("本地文件找不到");
		} catch (IOException e) {
			throw new NoExistException("本地文件找不到");
		}
	}

	public String rpst(List<String> filenames, String localPath)
			throws BasicException {
		RPSDag engine = new RPSDag();
		DBSCAN scan = new DBSCAN();
		for (String f : filenames) {
			try {
				EPC epcmodel = EPC.loadEPML(f);
				epcmodel.cleanEPC();
				engine.addProcessModel(new EPCHelper(epcmodel, f), f, scan);
			} catch (Exception e) {
				System.out.println("Problem with model " + f + ".");
				continue;
			}
		}
		// ----------------------------------------------------------
		// The cluster process and test the experiment results
		System.err.println("Models Number   : " + scan.getModelsNumber());
		System.err.println("Graphs Number   : " + scan.getGraphsNumber());
		System.err.println("MapFile Number  : " + scan.getMapFileNumber());
		System.err.println("Fragments Number: " + scan.getFragmentsNumber()
				+ "\n");

		scan.createMTree();
		System.err
				.println("\nMTree has built, now print the fragments in DOT file");
		String dotFolder = localPath + "dot" + File.separator;
		File folder = new File(dotFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		scan.printFragments(dotFolder);
		return dotFolder;
	}
}
