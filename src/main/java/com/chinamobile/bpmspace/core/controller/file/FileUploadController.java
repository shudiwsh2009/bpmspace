package com.chinamobile.bpmspace.core.controller.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.FileRepository;
import com.chinamobile.bpmspace.core.repository.InstanceRepository;
import com.chinamobile.bpmspace.core.repository.LogRepository;
import com.chinamobile.bpmspace.core.util.FileUtil;

@Controller
@RequestMapping("fileupload")
public class FileUploadController {

	@RequestMapping(value = "indexlib", method = RequestMethod.POST)
	public void getIndexList(@RequestParam("cat") String cat,
			@RequestParam("index_file_path") MultipartFile myfile,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		String originalFilename = null;

		if (FileUtil.WEBAPP_ROOT == "") {
			// FileUtil.WEBAPP_ROOT =
			// "D:\\apache-tomcat-7.0.52\\webapps\\bpmspace\\";
		}

		String uploadPath = FileUtil.WEBAPP_ROOT + "WEB-INF\\lib";
		String libPath = FileUtil.WEBAPP_ROOT + "WEB-INF\\lib";

		if (myfile == null || myfile.isEmpty()) {
			result.put("state", "FAIL");
			result.put("info", "empty！");
		} else {
			originalFilename = myfile.getOriginalFilename();
			try {
				FileRepository fp = new FileRepository();
				// 测试是否存在同名文件，是否写入本地成功
				fp.copyInputStreamToFile(myfile.getInputStream(), uploadPath,
						originalFilename);

				/*
				 * Runtime runtime = Runtime.getRuntime(); String cmd =
				 * "xcopy "+ uploadPath+"\\"+originalFilename + " " + libPath;
				 * runtime.exec(cmd);
				 */

				// FileUtils.copyFileToDirectory(new
				// File(uploadPath+"\\"+originalFilename), new File(libPath));

				result.put("state", "SUCCESS");
			} catch (BasicException e) {
				result.put("state", "FAIL");
				result.put("info", e.getInfo());
			} catch (IOException e) {
				e.printStackTrace();
				result.put("state", "FAIL");
				result.put("info", "fail to write jar file！");
			}

		}

		// send response
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "processmining", method = RequestMethod.POST)
	@ResponseBody
	public void processMining(
			@RequestParam("logfileuploadpm") MultipartFile myfile,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		String suffix = null;
		String fileNameWithoutSuffix = null;
		String inputFileName = null;

		JSONObject result = new JSONObject();

		// webapps/bpmspace/pmfiles/userId
		String realPrePath = FileUtil.WEBAPP_ROOT + "pmfiles" + File.separator
				+ session.getAttribute("userId").toString();

		System.out.println(realPrePath);

		if (myfile == null || myfile.isEmpty()) {
			result.put("state", "FAIL");
			result.put("info", "empty！");
		} else {
			suffix = myfile.getOriginalFilename().substring(
					myfile.getOriginalFilename().lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			inputFileName = fileNameWithoutSuffix + suffix;

			System.out.println(fileNameWithoutSuffix + ""
					+ myfile.getOriginalFilename() + "/n" + inputFileName);

			try {
				FileRepository.copyInputStreamToFile(myfile.getInputStream(),
						realPrePath, inputFileName);
				result.put("state", "SUCCESS");
				result.put("inputFilePath", realPrePath + File.separator
						+ inputFileName);
				result.put("outputFileName", realPrePath + File.separator
						+ fileNameWithoutSuffix);
			} catch (BasicException e) {
				result.put("state", "FAIL");
				result.put("info", e.getInfo());
				System.out.println(e.getInfo());
			} catch (IOException e) {
				e.printStackTrace();
				result.put("state", "FAIL");
				result.put("info", "fail to write log file！");
			}
		}
		//

		// send response
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "input_instance", method = RequestMethod.POST)
	@ResponseBody
	public void getInputInstance(
			@RequestParam("log_file_path") MultipartFile myfile,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws BasicException {
		String suffix = null;
		String fileNameWithoutSuffix = null;
		String inputFileName = null;

		JSONObject result = new JSONObject();
		String realPrePath = "e:\\temp\\"
				+ session.getAttribute("userId").toString();
		// String realPrePath = FileUtil.WEBAPP_ROOT + "pmfiles" +
		// File.separator + session.getAttribute("userId").toString();
		// String realPrePath = "e:\\temp\\";
		if (myfile == null || myfile.isEmpty()) {
			result.put("state", "FAIL");
			result.put("info", "empty！");
		} else {
			suffix = myfile.getOriginalFilename().substring(
					myfile.getOriginalFilename().lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			inputFileName = fileNameWithoutSuffix + suffix;

			System.out.println(fileNameWithoutSuffix + ""
					+ myfile.getOriginalFilename() + "" + inputFileName);

			try {
				FileRepository.copyInputStreamToFile(myfile.getInputStream(),
						realPrePath, myfile.getOriginalFilename());
				result.put("state", "SUCCESS");
			} catch (BasicException e) {
				result.put("state", "FAIL");
				result.put("info", e.getInfo());
			} catch (IOException e) {
				e.printStackTrace();
				result.put("state", "FAIL");
				result.put("info", "fail to write log file！");
			}
		}
		// write log into db
		InstanceRepository instanceRepository = new InstanceRepository();
		LogRepository logRepository = new LogRepository();
		String tmpId = null, nextId = null;
		File file = new File(realPrePath + "\\" + myfile.getOriginalFilename());
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			InputStream is = new FileInputStream(file.getAbsolutePath());
			Workbook wb = Workbook.getWorkbook(is);
			int sheet_size = wb.getNumberOfSheets();
			for (int index = 0; index < sheet_size; index++) {
				Sheet sheet = wb.getSheet(index);
				String logName = sheet.getName();
				if (sheet.getRows() != 0) {
					logRepository.addLog("ID_" + logName, logName, session
							.getAttribute("userId").toString(), null);
				}
				// colum值
				/**
				 * 0 ID 1 activityName 2 actor 3 startTime 4 endTime 5 path 6
				 * ...
				 */
				String[] array = new String[7];
				// sheet.getRows()返回该页的总行数
				for (int i = 1; i < sheet.getRows(); i++) {
					// sheet.getColumns()返回该页的总列数
					tmpId = sheet.getCell(1, i).getContents();
					nextId = sheet.getCell(1, i + 1).getContents();
					for (int j = 1; j <= 7; j++) {
						String cellinfo = sheet.getCell(j, i).getContents();
						System.out.println(cellinfo);
						array[j - 1] = cellinfo;
					}
					if (tmpId.equalsIgnoreCase(nextId)
							& tmpId.equalsIgnoreCase("")) {
						break;
					}
					instanceRepository.makeActivitySet(format.parse(array[3])
							.getTime(), format.parse(array[4]).getTime(),
							array[1], array[2], array[0]);
					// instanceRepository.makeActivityList();
					if (tmpId.equalsIgnoreCase(nextId)
							& !tmpId.equalsIgnoreCase("")) {
						tmpId = null;
						nextId = null;
						continue;
					} else {
						instanceRepository.add("ID_" + logName, tmpId, tmpId
								+ "_name", tmpId + "_description", session
								.getAttribute("userId").toString());
						instanceRepository.makeActivityListClean();
						tmpId = null;
						nextId = null;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BasicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// send response
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
