package com.chinamobile.bpmspace.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.importing.pnml.PnmlImport;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.exception.NoExistException;

public class FileUtil {
	public static String PUBLIC_ROOT_PROCESS_CATALOG = "";
	public static String PUBLIC_ROOT_LOG_CATALOG = "";

	public static String JSON_PREFIX = "json" + File.separator;
	public static String JSON_SUFFIX = ".json";
	public static String SVG_PREFIX = "svg" + File.separator;
	public static String SVG_SUFFIX = ".svg";
	public static String XML_PREFIX = "xml" + File.separator;
	public static String XML_SUFFIX = ".xml";
	public static String BPMN_PREFIX = "bpmn" + File.separator;
	public static String BPMN_SUFFIX = ".bpmn";
	public static String EPML_PREFIX = "epml" + File.separator;
	public static String EPML_SUFFIX = ".epml";
	public static String PNML_PREFIX = "pnml" + File.separator;
	public static String PNML_SUFFIX = ".pnml";
	public static String EXPORT_PREFIX = "export" + File.separator;
	public static String EXPORT_SUFFIX = ".xml";
	public static String PNG_PREFIX = "png" + File.separator;
	public static String PNG_SUFFIX = ".png";
	public static String ZIP_SUFFIX = ".zip";

	// Module
	public static String SIM_PREFIX = "sim" + File.separator;
	public static String DIFF_PREFIX = "diff" + File.separator;
	public static String CHECK_PREFIX = "check" + File.separator;
	public static String STATISTICS_PREFIX = "statistics" + File.separator;
	public static String STATISTICS_SUFFIX = ".csv";
	public static String FRAG_PREFIX = "frag" + File.separator;
	public static String MERGE_PREFIX = "merge" + File.separator;
	public static String CLUSTER_PREFIX = "cluster" + File.separator;

	// GridFS
	public static String CONTENT_TYPE_JSON = "text/json";
	public static String CONTENT_TYPE_SVG = "image/svg+xml";
	public static String CONTENT_TYPE_XML = "text/xml";
	public static String CONTENT_TYPE_BPMN = "text/bpmn";
	public static String CONTENT_TYPE_EPC = "text/epml";
	public static String CONTENT_TYPE_PNML = "text/pnml";

	public static String WEBAPP_ROOT = "";
	public static String WEB_FOLDER = "WEB-INF" + File.separator;
	public static String LIB_FOLDER = FileUtil.WEB_FOLDER + "lib"
			+ File.separator;
	public static String CONFIG_FOLDER = FileUtil.WEB_FOLDER + "config"
			+ File.separator;

	public static String SPLITTER = "%_%";

	public static MongoAccess MONGO = new MongoAccess();

	/**
	 * 读取模型文件
	 * 
	 * @param _filePath
	 * @return [0]文件内容 [1]文件大小
	 */
	public static String[] readModelFile(String _filePath) {
		String[] result = new String[2];
		result[0] = "";
		try {
			File file = new File(_filePath);
			if (!(file.exists() && file.isFile())) {
				result[0] = "";
				result[1] = "0";
				return result;
			} else {
				result[1] = file.length() + "";
			}
			InputStreamReader input = new InputStreamReader(
					new FileInputStream(_filePath));
			int tempchar;
			while ((tempchar = input.read()) != -1) {
				if ((char) tempchar != '\r') {
					result[0] += (char) tempchar;
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static long getFileLength(String _filePath) {
		File file = new File(_filePath);
		long length = 0L;
		if (!(file.exists() && file.isFile())) {
			length = 0L;
		} else {
			length = file.length();
		}
		return length;
	}

	public static boolean exists(String _filePath) {
		File file = new File(_filePath);
		if (file.exists() && file.isFile()) {
			return true;
		}
		return false;
	}

	public static void writeModelFile(String _filePath, String _filename)
			throws NoExistException {
		FileUtil.MONGO.getFileByFilename(_filename, _filePath);
	}

	public static String nameGridFSFile(String _userId, String _processId,
			long _revision) {
		StringBuilder builder = new StringBuilder();
		builder.append(_userId);
		builder.append("_");
		builder.append(_processId);
		builder.append("_");
		builder.append(_revision);
		return builder.toString();
	}

	public static PetriNet readPetriNetFromString(String pnStr) {
		PnmlImport input = new PnmlImport();

		ByteArrayInputStream bin = new ByteArrayInputStream(pnStr.getBytes());
		try {
			PetriNet pn = input.read(bin);
			return pn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PetriNet readPetriNetFormFile(String pnXmlPath) {

		PnmlImport input = new PnmlImport();
		try {
			InputStream in = FileUtil.MONGO
					.getFileInputStreamByFilename(pnXmlPath);
			PetriNet pn = input.read(in);
			return pn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		/*
		 * PnmlImport input = new PnmlImport();
		 * 
		 * ByteArrayInputStream bin = new
		 * ByteArrayInputStream(pnStr.getBytes()); try { PetriNet pn =
		 * input.read(bin); return pn; } catch (Exception e) {
		 * e.printStackTrace(); return null; }
		 */
		/*
		 * PetriNet pn = new PetriNet(); Place p1= new Place("a",pn); Place p2=
		 * new Place("b",pn); Transition tt = new Transition("t1",pn);
		 * Transition tt1 = new Transition("t2",pn); pn.addPlace(p1);
		 * pn.addPlace(p2); pn.addTransition(tt); pn.addAndLinkEdge(new
		 * PNEdge(p1,tt),p1,tt); pn.addAndLinkEdge(new PNEdge(tt,p2),tt,p2);
		 * pn.addAndLinkEdge(new PNEdge(p2,tt1),p2,tt1); return pn;
		 */
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		FileUtil.deleteFile(file);
	}

	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					FileUtil.deleteFile(files[i]);
				}
			}
			file.delete();
		} else {

		}
	}

	public static void main(String[] args) {
		Map<String, String> envMap = System.getenv();
		for (Iterator<String> itr = envMap.keySet().iterator(); itr.hasNext();) {
			String key = itr.next();
			System.out.println(key + "=" + envMap.get(key));
		}
	}

}
