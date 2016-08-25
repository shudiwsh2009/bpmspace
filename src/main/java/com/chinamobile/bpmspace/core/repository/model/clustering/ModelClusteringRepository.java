package com.chinamobile.bpmspace.core.repository.model.clustering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.thss.iise.bpmdemo.analysis.core.similarity.SimilarityUtil;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.google.gson.Gson;
import com.ibm.bpm.analyzer.Calculation;
import com.ibm.bpm.analyzer.GenerateNewick;

public class ModelClusteringRepository {

	private MongoAccess mongo = new MongoAccess();

	public static void main(String[] args) throws BasicException, IOException {
		ModelClusteringRepository mcr = new ModelClusteringRepository();
		FileUtil.WEBAPP_ROOT = "D:/Program Files (x86)/apache-tomcat-8.0.9-windows-x64/apache-tomcat-8.0.9/webapps/bpmspace";
		String[] filepaths = {
				"C:\\Users\\Administrator\\Desktop\\bpmn\\bpmn\\normal4A账号管理流程.bpmn",
				"C:\\Users\\Administrator\\Desktop\\bpmn\\bpmn\\normalIT需求变更.bpmn.bpmn",
				"C:\\Users\\Administrator\\Desktop\\bpmn\\bpmn\\normal231个部门收文的流程跟踪.bpmn",
				"C:\\Users\\Administrator\\Desktop\\bpmn\\bpmn\\normal合同审批流程.bpmn",
				"C:\\Users\\Administrator\\Desktop\\bpmn\\bpmn\\normal会议费用申请审批流程.bpmn",
				"C:\\Users\\Administrator\\Desktop\\bpmn\\bpmn\\normal流程实例数据样本.bpmn" };
		System.out.println(mcr.modelClustering(filepaths, null,
				"53bbd271239d42b7e092487e"));
	}

	public String modelClustering(String[] filepaths, String[] processIds,
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

		String serverPath = FileUtil.CLUSTER_PREFIX + user.getId() + "_"
				+ System.nanoTime() + File.separator;
		String localPath = FileUtil.WEBAPP_ROOT + serverPath;
		File folder = new File(localPath + "model" + File.separator);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		List<Process> processList = new ArrayList<Process>();
		for (String pId : processIds) {
			Process p = mongo.getProcessById(pId);
			if (p == null || p.getType() != ProcessType.BPMN) {
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
			throw new NoExistException("没有可供聚类的模型");
		}
		// 在聚类前预处理
		pretreatment(filenames);

		String xlsFilepath = localPath + "similarity.xls";
		this.generateSimialrityXLS(filenames, xlsFilepath);
		String treeFilepath = localPath + "tree.tree";
		this.generateFigTree(xlsFilepath, treeFilepath);

		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					treeFilepath));
			String tmp = "";
			while ((tmp = reader.readLine()) != null) {
				sb.append(tmp);
			}
			reader.close();
		} catch (Exception e) {
			throw new BasicException("读取树状图失败");
		}
		// hc.segementString(s);
		String result;
		try {
			result = this.getResult(sb.toString());
		} catch (Exception e) {
			throw new BasicException("渲染树状图失败");
		}
		return result;
	}

	private void pretreatment(List<String> filenames) {
		// TODO Auto-generated method stub
		for (String filePath : filenames) {
			File file = new File(filePath);
			dealFile(file);
		}
	}

	private void dealFile(File file) {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		BufferedWriter bw = null;
		String content = "";
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			content = line + System.getProperty("line.separator");
			while ((line = br.readLine()) != null) {
				content = content + line + System.getProperty("line.separator");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close(); // throw IOException
			} catch (IOException e) {
			}
		}

		content = content
				.replaceAll(
						"<(definitions .*?)>",
						"<definitions id=\"Definition\""
								+ " targetNamespace=\"http://www.jboss.org/drools\""
								+ " typeLanguage=\"http://www.java.com/javaTypes\""
								+ " expressionLanguage=\"http://www.mvel.org/2.0\""
								+ " xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\""
								+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
								+ " xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\""
								+ " xmlns:g=\"http://www.jboss.org/drools/flow/gpd\""
								+ " xmlns:tns=\"http://www.jboss.org/drools\">");
		content = content
				.replaceAll(
						"(<bpmndi:processDiagram[\\s\\S]*</bpmndi:processDiagram>)",
						"");
		content = content.replaceAll("diverging", "Diverging");
		content = content.replaceAll("converging", "Converging");
		System.out.println(content);
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close(); // throw IOException
			} catch (IOException e) {
			}
		}

	}

	private void generateFigTree(String xlsFilepath, String treeFilepath)
			throws BasicException {
		try {
			GenerateNewick.generic(xlsFilepath, treeFilepath);
		} catch (Exception e) {
			throw new BasicException("生成树状图错误");
		}
	}

	private void generateSimialrityXLS(List<String> filenames,
			String xlsFilepath) throws BasicException {
		int size = filenames.size();
		String[][] similarity;
		similarity = new String[size + 1][];
		for (int i = 0; i < size + 1; ++i) {
			similarity[i] = new String[size + 1];
		}
		for (int i = 1; i < size + 1; ++i) {
			File file = new File(filenames.get(i - 1));
			similarity[0][i] = file.getName();
			similarity[i][0] = file.getName();
		}
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				if (i < j) {
					File file1 = new File(filenames.get(i));
					File file2 = new File(filenames.get(j));
					similarity[i + 1][j + 1] = String.valueOf(SimilarityUtil
							.similarity(file1.getAbsolutePath(),
									file2.getAbsolutePath()));
				} else if (i == j) {
					similarity[i + 1][j + 1] = "1";
				} else if (i > j) {
					similarity[i + 1][j + 1] = similarity[j + 1][i + 1];
				}
			}
		}
		try {
			Calculation.writeExl(similarity, xlsFilepath);
		} catch (Exception e) {
			throw new BasicException("生成XLS文件错误");
		}
	}

	private String getResult(String s) throws Exception {
		System.out.println("result is " + s);
		GenerateID.id = 0;
		float rootf = this.getRoot(s);
		Node root = new Node();
		root.name = "" + rootf;
		getJsonString(root, s);
		Gson gson = new Gson();
		System.out.println(gson.toJson(root));
		return gson.toJson(root);
	}

	private float getRoot(String s) {
		String s1 = "", s2 = "";
		float nodeNum1 = 0f;
		for (int position = 0; position < s.length(); position++) {
			if (s.charAt(position) == ',') {
				if (isMatch(s, position)) {
					int length = s.length();
					s = s.substring(1, length - 1);
					position = position - 1;
					s1 = s.substring(0, position);
					s2 = s.substring(position + 1, s.length());
					break;

				}
			}
		}

		String temp = maohaoshao(s1, s2);
		if (countMaohao(temp) == 0) {
			return 0f;
		}
		String temp1 = temp.substring(temp.lastIndexOf(':') + 1, temp.length());
		nodeNum1 = getFloat(temp1);

		return nodeNum1 + getRoot(temp.substring(0, temp.lastIndexOf(':')));

	}

	private String maohaoshao(String s1, String s2) {
		if (countMaohao(s1) <= countMaohao(s2)) {
			return s1;
		} else {
			return s2;
		}
	}

	private int countMaohao(String s) {
		int count = 0;
		for (int i = 0; i < s.toCharArray().length; i++) {
			if (s.charAt(i) == ':') {
				count++;
			}
		}
		return count;
	}

	private void getJsonString(Node root, String s) {

		String s1 = "", s2 = "";
		float rootNum = getFloat(root.name);
		float nodeNum1 = 0f, nodeNum2 = 0f;

		for (int position = 0; position < s.length(); position++) {
			if (s.charAt(position) == ',') {
				if (isMatch(s, position)) {
					int length = s.length();
					s = s.substring(1, length - 1);
					position = position - 1;
					s1 = s.substring(0, position);
					s2 = s.substring(position + 1, s.length());
					String temp1 = s1.substring(s1.lastIndexOf(':') + 1,
							s1.length());
					nodeNum1 = getFloat(temp1);
					String temp2 = s2.substring(s2.lastIndexOf(':') + 1,
							s2.length());
					nodeNum2 = getFloat(temp2);
					break;
				}
			}
		}

		if ((rootNum - nodeNum1) < 0.0001 && countMaohao(s1) == 1) {
			Node newNode = new Node();
			String[] ss = s1.split(":");

			float f = Float.valueOf(ss[1]);
			float b = (float) (Math.round(f * 1000)) / 1000;
			newNode.name = ss[0] + ":" + b;
			root.children.add(newNode);

		} else {
			Node newNode = new Node();
			newNode.name = getString(rootNum - nodeNum1);
			root.children.add(newNode);
			getJsonString(newNode, s1.substring(0, s1.lastIndexOf(':')));
		}
		if ((rootNum - nodeNum2) < 0.0001 && countMaohao(s2) == 1) {
			Node newNode = new Node();
			String[] ss = s2.split(":");

			float f = Float.valueOf(ss[1]);
			float b = (float) (Math.round(f * 1000)) / 1000;
			newNode.name = ss[0] + ":" + b;
			root.children.add(newNode);

		} else {
			Node newNode = new Node();
			newNode.name = getString(rootNum - nodeNum2);
			root.children.add(newNode);
			getJsonString(newNode, s2.substring(0, s2.lastIndexOf(':')));
		}
	}

	private float getFloat(String substring) {
		// TODO Auto-generated method stub
		// 保留了三位有效数字
		float f = Float.valueOf(substring);
		float b = f;// (float)(Math.round(f*1000))/1000;
		return b;
	}

	private String getString(Float f) {
		return f.toString();
	}

	// 检测括号是否匹配,用于用逗号分隔字符串
	private boolean isMatch(String s, int position) {
		int length = s.length();
		s = s.substring(1, length - 1);
		position = position - 1;
		String s1 = s.substring(0, position);
		String s2 = s.substring(position + 1, s.length());
		if (kuoHaoMatch(s1) && kuoHaoMatch(s2)) {
			return true;
		}
		return false;
	}

	private boolean kuoHaoMatch(String s) {
		// 简单判断 ： 左括号和右括号的数量是否相等
		int countL = 0, countR = 0;
		for (char a : s.toCharArray()) {
			if (a == '(') {
				countL++;
			}
		}
		for (char a : s.toCharArray()) {
			if (a == ')') {
				countR++;
			}
		}
		return countL == countR;
	}
}
