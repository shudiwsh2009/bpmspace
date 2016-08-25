package com.chinamobile.bpmspace.core.controller.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.chinamobile.bpmspace.core.domain.log.CaseEventList;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.model.FileMeta;
import com.chinamobile.bpmspace.core.repository.CaseEventListRepository;
import com.chinamobile.bpmspace.core.repository.DurationIndexRepository;
import com.chinamobile.bpmspace.core.repository.InstanceRepository;
import com.chinamobile.bpmspace.core.repository.LengthIndexRepository;
import com.chinamobile.bpmspace.core.repository.LogRepository;
import com.chinamobile.bpmspace.core.repository.index.logindex.aeventindex.AEventIndex;
import com.chinamobile.bpmspace.core.repository.index.logindex.durationindex.DurationIndex;
import com.chinamobile.bpmspace.core.repository.index.logindex.eventindex.EventIndex;
import com.chinamobile.bpmspace.core.repository.index.logindex.lengthindex.LengthIndex;
import com.chinamobile.bpmspace.core.repository.model.convertion.ModelConvertionRepository;
import com.chinamobile.bpmspace.core.util.FileUtil;

@Controller
@RequestMapping("uploadfiles")
public class FileController {

	LinkedList<FileMeta> files = new LinkedList<FileMeta>();
	FileMeta fileMeta = null;
	String logName = "";
	/***************************************************
	 * URL: /rest/controller/upload upload(): receives files
	 * 
	 * @param request
	 *            : MultipartHttpServletRequest auto passed
	 * @param response
	 *            : HttpServletResponse auto passed
	 * @return LinkedList<FileMeta> as json format
	 ****************************************************/

	public String catalogIdString = "";

	@RequestMapping(value = "node_info_pass", method = RequestMethod.POST)
	public @ResponseBody void setCatalogId(
			@RequestParam("catalogId") String _catalogId, HttpSession session,
			HttpServletResponse response) {
		this.catalogIdString = _catalogId;
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> upload(
			MultipartHttpServletRequest request, HttpServletResponse response) {
		// response.setContentType("text/plain; charset=utf-8");
		// 1. build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		// 2. get each file
		while (itr.hasNext()) {

			// 2.1 get next MultipartFile
			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String fileName = "";
			try {
				// 解决中文乱码
				fileName = new String(value.getBytes("ISO8859-1"), "UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(fileName + " uploaded! " + files.size());

			// 2.2 if files > 10 remove the first from the list
			if (files.size() >= 10)
				files.pop();

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			fileMeta.setFileName(fileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());

			try {
				fileMeta.setBytes(mpf.getBytes());

				// copy file to local disk (make sure the path
				// "e.g. D:/temp/files" exists)
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						"D:/temp/files/" + mpf.getOriginalFilename()));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 2.4 add to files
			files.add(fileMeta);
		}
		// result will be like this
		// [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
		return files;
	}

	@RequestMapping(value = "modelSimilarity", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> modelSimilarity(
			MultipartHttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		// response.setContentType("text/plain; charset=utf-8");
		// 1. build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		// 2. get each file
		while (itr.hasNext()) {

			// 2.1 get next MultipartFile
			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String fileName = "";
			try {
				// 解决中文乱码
				fileName = new String(value.getBytes("ISO8859-1"), "UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(fileName + " uploaded! " + files.size());

			// 2.2 if files > 10 remove the first from the list
			if (files.size() >= 10)
				files.pop();

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			fileMeta.setFileName(fileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());

			try {
				fileMeta.setBytes(mpf.getBytes());

				// copy file to local disk (make sure the path
				// "e.g. D:/temp/files" exists)
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						"D:/temp/files/" + mpf.getOriginalFilename()));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 2.4 add to files
			files.add(fileMeta);
		}
		// result will be like this
		// [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
		return files;
	}

	@RequestMapping(value = "modelsimilarity", method = RequestMethod.POST)
	public @ResponseBody void modelsimilarity(
			MultipartHttpServletRequest request, HttpSession session,
			@RequestParam("index") String _index, HttpServletResponse response) {
		// response.setContentType("text/plain; charset=utf-8");
		// 1. build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		String fileName = "";
		String filePath = "";
		filePath = FileUtil.WEBAPP_ROOT + "pnmlsimilarity" + File.separator
				+ session.getAttribute("userId").toString() + File.separator
				+ _index;
		File dir = new File(filePath);
		dir.mkdirs();

		// 2. get each file
		while (itr.hasNext()) {

			// 2.1 get next MultipartFile
			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			try {
				// 解决中文乱码
				fileName = new String(value.getBytes("ISO8859-1"), "UTF-8");

			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(fileName + " uploaded! " + files.size());

			// 2.2 if files > 10 remove the first from the list
			if (files.size() >= 10)
				files.pop();

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			fileMeta.setFileName(fileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());

			try {
				fileMeta.setBytes(mpf.getBytes());

				// copy file to local disk (make sure the path
				// "e.g. D:/temp/files" exists)
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						filePath + File.separator + fileName));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 2.4 add to files

		}
		// result will be like this
		// [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			JSONObject result = new JSONObject();
			result.put("filepath", filePath + File.separator + fileName);
			System.out.println(filePath + File.separator + fileName);
			out.write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "modeldifferentiation", method = RequestMethod.POST)
	public @ResponseBody void modeldifferentiation(
			MultipartHttpServletRequest request, HttpSession session,
			@RequestParam("index") String _index, HttpServletResponse response) {
		// response.setContentType("text/plain; charset=utf-8");
		// 1. build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		String fileName = "";
		String filePath = "";
		filePath = FileUtil.WEBAPP_ROOT + "pnmldifferentiation"
				+ File.separator + session.getAttribute("userId").toString()
				+ File.separator + _index;
		File dir = new File(filePath);
		dir.mkdirs();

		// 2. get each file
		while (itr.hasNext()) {

			// 2.1 get next MultipartFile
			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			try {
				// 解决中文乱码
				fileName = new String(value.getBytes("ISO8859-1"), "UTF-8");

			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(fileName + " uploaded! " + files.size());

			// 2.2 if files > 10 remove the first from the list
			if (files.size() >= 10)
				files.pop();

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			fileMeta.setFileName(fileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());

			try {
				fileMeta.setBytes(mpf.getBytes());

				// copy file to local disk (make sure the path
				// "e.g. D:/temp/files" exists)
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						filePath + File.separator + fileName));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 2.4 add to files

		}
		// result will be like this
		// [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			JSONObject result = new JSONObject();
			result.put("filepath", filePath + File.separator + fileName);
			System.out.println(filePath + File.separator + fileName);
			out.write(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "logName_input", method = RequestMethod.POST)
	public @ResponseBody void logNameUpload(
			@RequestParam("logName") String _logName, HttpSession session,
			HttpServletResponse response) throws EmptyFieldException {
		JSONObject result = new JSONObject();
		if (_logName == null || _logName.equals("")) {
			result.put("state", "FAILED");
			throw new EmptyFieldException("日志名为空");
		}
		this.logName = _logName;
		result.put("state", "SUCCESS");

		// response body
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
	public @ResponseBody LinkedList<FileMeta> inputInstance(
			MultipartHttpServletRequest request, HttpSession session,
			HttpServletResponse response) throws BasicException, ParseException {
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		String logidTemp = null;
		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			String realPrePath = FileUtil.WEBAPP_ROOT + "instanceLog"
					+ File.separator
					+ session.getAttribute("userId").toString();
			String inputPrePath = realPrePath + File.separator + "logs";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String originalFileName = "";
			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println(originalFileName + " uploaded! " + files.size());

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			// 2.2 if files > 10 remove the first from the list
			if (files.size() >= 10) {
				files.pop();
			}

			/**
			 * 2.3 create new fileMeta
			 */
			try {
				File dir = new File(inputPrePath);
				dir.mkdirs();
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						inputPrePath + File.separator + newFileName));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/**
			 * 2.4 add to files files.add(fileMeta); write log into DB
			 */

			InstanceRepository instanceRepository = new InstanceRepository();
			LogRepository logRepository = new LogRepository();

			// for length index
			LengthIndexRepository lengthIndexRepository = new LengthIndexRepository();
			List<Integer> lengthList = new ArrayList<Integer>();
			int countOfInstance = 0;
			// for duration index
			DurationIndexRepository durationIndexRepository = new DurationIndexRepository();
			List<Integer> durationList = new ArrayList<Integer>();
			String startTimeInstance = null, endTimeInstance = null;
			int duration = 0;
			// for case name index
			List<String> eventList = new ArrayList<String>();

			// for adjacent case index
			CaseEventListRepository celr = new CaseEventListRepository();
			List<CaseEventList> cels = new ArrayList<CaseEventList>();
			String tmpId = null, nextId = null;
			String tmpPath = realPrePath + File.separator + "logs"
					+ File.separator + newFileName;
			File file = new File(tmpPath);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				InputStream is = new FileInputStream(file);
				Workbook wb = Workbook.getWorkbook(is);
				int sheet_size = wb.getNumberOfSheets();
				if (sheet_size > 1) {
					throw new EmptyFieldException("数据表多于一页");
				} else {
					if (logName == null || logName.equals("")) {
						throw new EmptyFieldException("日志表又为空了");
					}

					Sheet sheet = wb.getSheet(0);

					if (sheet.getRows() != 0) {
						fileMeta = new FileMeta();
						fileMeta.setFileName(logName);
						files.add(fileMeta);

						logidTemp = logRepository.addLog(logName, session
								.getAttribute("userId").toString(),
								catalogIdString, lengthList);
						fileMeta.setFileId(logidTemp);
					}
					logName = "";
					// colum值
					/**
					 * 0 ID 1 activityName 2 actor 3 startTime 4 endTime 5 path
					 * 6 ...
					 */
					String[] array = new String[7];
					String[] arrayNext = new String[7];
					// sheet.getRows()返回该页的总行数
					startTimeInstance = sheet.getCell(4, 1).getContents();
					for (int i = 1; i < sheet.getRows(); i++) {
						// sheet.getColumns()返回该页的总列数
						tmpId = sheet.getCell(1, i).getContents();
						if (i < sheet.getRows() - 1) {
							nextId = sheet.getCell(1, i + 1).getContents();
						} else {
							nextId = "";
						}
						for (int j = 1; j <= 7; j++) {
							String cellinfo = sheet.getCell(j, i).getContents();
							System.out.println(cellinfo);
							array[j - 1] = cellinfo;
						}
						if (nextId.equals("")) {
							arrayNext[5] = "";
						} else {
							for (int j = 1; j <= 7; j++) {
								String cellinfo = sheet.getCell(j, i + 1)
										.getContents();
								System.out.println(cellinfo);
								arrayNext[j - 1] = cellinfo;
							}
						}
						if (tmpId.equalsIgnoreCase(nextId)
								& tmpId.equalsIgnoreCase("")) {
							break;
						}
						instanceRepository.makeActivitySet(
								format.parse(array[3]).getTime(),
								format.parse(array[4]).getTime(), array[1],
								array[2], array[0]);
						if (tmpId.equalsIgnoreCase(nextId)
								& !tmpId.equalsIgnoreCase("")) {
							tmpId = null;
							nextId = null;
							countOfInstance++;
							if (eventList.indexOf(array[1]) < 0) {
								eventList.add(array[1]);
							}

							celr.getCaseEvent(array[1], arrayNext[1], logidTemp);
							// celr.addCaseEventToList(array[1], arrayNext[1],
							// logidTemp);

							continue;
						} else {
							String tttString = instanceRepository.add(
									logidTemp, tmpId, tmpId + "_name", tmpId
											+ "_description", session
											.getAttribute("userId").toString());
							System.out.println(tttString);
							instanceRepository.makeActivityListClean();

							celr.getCaseEvent(array[1], "", logidTemp);
							// celr.addCaseEventToList(array[1], "", logidTemp);

							tmpId = null;
							nextId = null;

							/**
							 * event index
							 */
							if (eventList.indexOf(array[1]) < 0) {
								eventList.add(array[1]);
							}
							/**
							 * duration index
							 */
							endTimeInstance = array[4];
							try {
								duration = getDuration(startTimeInstance,
										endTimeInstance);
							} catch (ParseException e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							if (durationList.indexOf(duration) < 0) {
								durationList.add(duration);
							}
							if (i == 320) {
								System.out.println("注意");
							}
							// durationIndexRepository.addDurationIndex(duration,
							// logidTemp);
							duration = 0;
							if (i + 1 >= sheet.getRows()) {
								continue;
							} else {
								startTimeInstance = sheet.getCell(4, i + 1)
										.getContents();
							}
							/**
							 * length index
							 */
							countOfInstance++;
							if (lengthList.indexOf(countOfInstance) < 0) {
								lengthList.add(countOfInstance);
							}
							// lengthIndexRepository.addLengthIndex(countOfInstance,
							// logidTemp);
							countOfInstance = 0;

						}
					}
					cels = celr.getCaseEventList();
					logRepository.updateLog(logidTemp, lengthList,
							durationList, eventList, cels);
					LengthIndex lIndex = new LengthIndex();
					DurationIndex dIndex = new DurationIndex();
					EventIndex eIndex = new EventIndex();
					AEventIndex aEIndex = new AEventIndex();

					lIndex.addInstance(logRepository.getLogByLogId(logidTemp));
					dIndex.addInstance(logRepository.getLogByLogId(logidTemp));
					eIndex.addInstance(logRepository.getLogByLogId(logidTemp));
					aEIndex.addInstance(logRepository.getLogByLogId(logidTemp));

					lengthList.clear();
					durationList.clear();
					eventList.clear();
					celr.flushList();

					fileMeta.setFileId(logidTemp);
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
			}

		}

		return files;
	}

	public static int getDuration(String startTimeInstance,
			String endTimeInstance) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (startTimeInstance.indexOf("-") > 0) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		} else if (startTimeInstance.indexOf("//") > 0) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
		Date sTime = null, eTime = null;
		try {
			sTime = sdf.parse(startTimeInstance);
			eTime = sdf.parse(endTimeInstance);
		} catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		int duration = (int) (eTime.getTime() - sTime.getTime())
				/ (1000 * 60 * 60);
		return duration;
	}

	@RequestMapping(value = "processmining", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> processmining(
			MultipartHttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		LinkedList<FileMeta> nfiles = new LinkedList<FileMeta>();
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			// webapps/bpmspace/pmfiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "pmfiles"
					+ File.separator
					+ session.getAttribute("userId").toString();
			String inputPrePath = realPrePath + File.separator + "logs";
			String outputPrePath = realPrePath + File.separator + "models";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String originalFileName = "";
			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			// 2.2 if files > 10 remove the first from the list
			if (nfiles.size() >= 10) {
				nfiles.pop();
			}

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			if (suffix.equals(".mxml") == false) {
				fileMeta.setInvalid();
			}
			fileMeta.setFileName(originalFileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			try {
				fileMeta.setBytes(mpf.getBytes());
				fileMeta.setInputFilePath(inputPrePath + File.separator
						+ newFileName);
				fileMeta.setOutputFileName(outputPrePath + File.separator
						+ fileNameWithoutSuffix);

				File dir = new File(inputPrePath);
				dir.mkdirs();
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						inputPrePath + File.separator + newFileName));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 2.4 add to files
			nfiles.add(fileMeta);
			files = nfiles;
		}

		return nfiles;
	}

	@RequestMapping(value = "modelimport", method = RequestMethod.POST)
	public @ResponseBody void modelimport(MultipartHttpServletRequest request,
			@RequestParam("processType") String _processType,
			HttpSession session, HttpServletResponse response) {
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		String newpnmlPath = "";
		String inputPrePath = "";
		String fileNameWithoutSuffix = "";
		String relativePath = "";
		String suffix = "";
		boolean isMatch = true;
		while (itr.hasNext()) {

			String newFileName = null;
			// webapps/bpmspace/pmfiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "modelpmfiles"
					+ File.separator
					+ session.getAttribute("userId").toString();

			inputPrePath = realPrePath + File.separator + "modelimport";
			String outputPrePath = realPrePath + File.separator + "models";
			relativePath = "modelpmfiles" + File.separator
					+ session.getAttribute("userId").toString()
					+ File.separator + "modelimport" + File.separator;
			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String originalFileName = "";
			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			suffix = suffix.toLowerCase();
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;
			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			if ((suffix.contains("pnml") && !_processType.equals("PETRINET"))
					||(suffix.contains("bpmn") && !_processType.equals("BPMN"))
					|| (suffix.contains("epml") && !_processType.equals("EPC"))) {
				isMatch = false;
				continue;
			}
			fileMeta.setFileName(originalFileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			try {
				fileMeta.setBytes(mpf.getBytes());
				fileMeta.setInputFilePath(inputPrePath + File.separator
						+ newFileName);
				fileMeta.setOutputFileName(outputPrePath + File.separator
						+ fileNameWithoutSuffix);

				File dir = new File(inputPrePath);
				dir.mkdirs();
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						inputPrePath + File.separator + newFileName));
				newpnmlPath = inputPrePath + File.separator + "new"
						+ newFileName;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 2.4 add to files
		}

		JSONObject result = new JSONObject();
		if (isMatch) {
			String rawModelPath = fileMeta.getInputFilePath();
			ModelConvertionRepository mc = new ModelConvertionRepository();
			String jsonpath = inputPrePath + File.separator
					+ fileNameWithoutSuffix + ".json";
			if (suffix.contains("pnml")) {
				// 处理pnml
				// 1,重新布局生成新文件
				// 2,根据新文件生成json文件，返回json路径
				try {
					mc.autoLayout(rawModelPath, newpnmlPath);
				} catch (BasicException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mc.createPnmlJsonFile(newpnmlPath, jsonpath);
			} else if (suffix.contains("epml")) {
				// 处理epc
				// 根据原始模型生成json文件 返回json路径
				mc.createEpcJsonFile(rawModelPath, jsonpath);
			}	else if (suffix.contains("bpmn")) {
				// 处理epc
				// 根据原始模型生成json文件 返回json路径
				mc.createBpmnJsonFile(rawModelPath, jsonpath);
			}

			result.put("jsonpath", relativePath + fileNameWithoutSuffix
					+ ".json");
		}
		// 获取了file的路径，要转换成另一种，然后生成json
		else {
			result.put("status", "FAILED");
			result.put("message", "上传的模型类型与所选类型不匹配");
		}
		// response body
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

	@RequestMapping(value = "modelcheck", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> modelcheck(
			MultipartHttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		LinkedList<FileMeta> nfiles = new LinkedList<FileMeta>();
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			// webapps/bpmspace/pmfiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "modelpmfiles"
					+ File.separator
					+ session.getAttribute("userId").toString();
			String inputPrePath = realPrePath + File.separator + "modelcheck";
			String outputPrePath = realPrePath + File.separator + "models";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String originalFileName = "";
			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			// 2.2 if files > 10 remove the first from the list
			if (nfiles.size() >= 10) {
				nfiles.pop();
			}

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			if (suffix.equals(".mxml") == false) {
				fileMeta.setInvalid();
			}
			fileMeta.setFileName(originalFileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			try {
				fileMeta.setBytes(mpf.getBytes());
				fileMeta.setInputFilePath(inputPrePath + File.separator
						+ newFileName);
				fileMeta.setOutputFileName(outputPrePath + File.separator
						+ fileNameWithoutSuffix);

				File dir = new File(inputPrePath);
				dir.mkdirs();
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						inputPrePath + File.separator + newFileName));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 2.4 add to files
			nfiles.add(fileMeta);
			files = nfiles;
		}

		return nfiles;
	}

	@RequestMapping(value = "modelMerge", method = RequestMethod.POST)
	public @ResponseBody void modelMerge(MultipartHttpServletRequest request,
			HttpSession session, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		LinkedList<FileMeta> nfiles = new LinkedList<FileMeta>();
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		String[] acceptsuffix = { ".bpmn" };
		String inputPrePath;
		String originalFileName = "";
		boolean accept = false;
		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			// webapps/bpmspace/pmfiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "merge"
					+ File.separator
					+ session.getAttribute("userId").toString();
			inputPrePath = realPrePath;
			String outputPrePath = realPrePath + File.separator + "models";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();

			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			for (int p = 0; p < acceptsuffix.length; p++) {
				if (suffix.equals(acceptsuffix[p])) {
					accept = true;
					break;
				}
			}

			if (accept) {
				// 2.2 if files > 10 remove the first from the list
				if (nfiles.size() >= 10) {
					nfiles.pop();
				}
				// 2.3 create new fileMeta
				fileMeta = new FileMeta();
				fileMeta.setFileName(originalFileName);
				fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
				fileMeta.setFileType(mpf.getContentType());
				try {
					fileMeta.setBytes(mpf.getBytes());
					fileMeta.setInputFilePath(inputPrePath + File.separator
							+ newFileName);
					fileMeta.setOutputFileName(outputPrePath + File.separator
							+ fileNameWithoutSuffix);

					File dir = new File(inputPrePath);
					dir.mkdirs();
					FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
							inputPrePath + File.separator + originalFileName));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 2.4 add to files
				try {
					out = response.getWriter();
					JSONObject result = new JSONObject();
					result.put("status", "sucess");
					result.put("filepath", inputPrePath + File.separator
							+ originalFileName);
					result.put("filename", originalFileName);
					out.write(result.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nfiles.add(fileMeta);
				files = nfiles;
			} else {

				try {
					out = response.getWriter();
					JSONObject result = new JSONObject();
					result.put("status", "failed");
					out.write(result.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	@RequestMapping(value = "modelFragmentation", method = RequestMethod.POST)
	public @ResponseBody void modelFragmentation(
			MultipartHttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		LinkedList<FileMeta> nfiles = new LinkedList<FileMeta>();
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		String[] acceptsuffix = { ".epml" };
		String inputPrePath;
		String originalFileName = "";
		boolean accept = false;
		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			// webapps/bpmspace/pmfiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "frag" + File.separator
					+ session.getAttribute("userId").toString();
			inputPrePath = realPrePath;
			String outputPrePath = realPrePath + File.separator + "models";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();

			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			for (int p = 0; p < acceptsuffix.length; p++) {
				if (suffix.equals(acceptsuffix[p])) {
					accept = true;
					break;
				}
			}

			if (accept) {
				// 2.2 if files > 10 remove the first from the list
				if (nfiles.size() >= 10) {
					nfiles.pop();
				}
				// 2.3 create new fileMeta
				fileMeta = new FileMeta();
				fileMeta.setFileName(originalFileName);
				fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
				fileMeta.setFileType(mpf.getContentType());
				try {
					fileMeta.setBytes(mpf.getBytes());
					fileMeta.setInputFilePath(inputPrePath + File.separator
							+ newFileName);
					fileMeta.setOutputFileName(outputPrePath + File.separator
							+ fileNameWithoutSuffix);

					File dir = new File(inputPrePath);
					dir.mkdirs();
					FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
							inputPrePath + File.separator + originalFileName));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 2.4 add to files
				try {
					out = response.getWriter();
					JSONObject result = new JSONObject();
					result.put("status", "sucess");
					result.put("filepath", inputPrePath + File.separator
							+ originalFileName);
					result.put("filename", originalFileName);
					out.write(result.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nfiles.add(fileMeta);
				files = nfiles;
			} else {

				try {
					out = response.getWriter();
					JSONObject result = new JSONObject();
					result.put("status", "failed");
					out.write(result.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	@RequestMapping(value = "modelCluster", method = RequestMethod.POST)
	public @ResponseBody void modelCluster(MultipartHttpServletRequest request,
			HttpSession session, HttpServletResponse response) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		LinkedList<FileMeta> nfiles = new LinkedList<FileMeta>();
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		String[] acceptsuffix = { ".bpmn" };
		String inputPrePath;
		String originalFileName = "";
		boolean accept = false;
		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			// webapps/bpmspace/pmfiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "cluster"
					+ File.separator
					+ session.getAttribute("userId").toString();
			inputPrePath = realPrePath;
			String outputPrePath = realPrePath + File.separator + "models";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();

			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			for (int p = 0; p < acceptsuffix.length; p++) {
				if (suffix.equals(acceptsuffix[p])) {
					accept = true;
					break;
				}
			}

			if (accept) {
				// 2.2 if files > 10 remove the first from the list
				if (nfiles.size() >= 10) {
					nfiles.pop();
				}
				// 2.3 create new fileMeta
				fileMeta = new FileMeta();
				fileMeta.setFileName(originalFileName);
				fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
				fileMeta.setFileType(mpf.getContentType());
				try {
					fileMeta.setBytes(mpf.getBytes());
					fileMeta.setInputFilePath(inputPrePath + File.separator
							+ newFileName);
					fileMeta.setOutputFileName(outputPrePath + File.separator
							+ fileNameWithoutSuffix);

					File dir = new File(inputPrePath);
					dir.mkdirs();
					FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
							inputPrePath + File.separator + originalFileName));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 2.4 add to files
				try {
					out = response.getWriter();
					JSONObject result = new JSONObject();
					result.put("status", "sucess");
					result.put("filepath", inputPrePath + File.separator
							+ originalFileName);
					result.put("filename", originalFileName);
					out.write(result.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nfiles.add(fileMeta);
				files = nfiles;
			} else {

				try {
					out = response.getWriter();
					JSONObject result = new JSONObject();
					result.put("status", "failed");
					out.write(result.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	@RequestMapping(value = "modelStatistics", method = RequestMethod.POST)
	public @ResponseBody void modelStatistics(
			MultipartHttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		LinkedList<FileMeta> nfiles = new LinkedList<FileMeta>();
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		String[] acceptsuffix = { ".pnml" };
		String inputPrePath;
		String originalFileName = "";
		boolean accept = false;
		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			// webapps/bpmspace/pmfiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "statistics"
					+ File.separator
					+ session.getAttribute("userId").toString();
			inputPrePath = realPrePath;
			String outputPrePath = realPrePath + File.separator + "models";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();

			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			for (int p = 0; p < acceptsuffix.length; p++) {
				if (suffix.equals(acceptsuffix[p])) {
					accept = true;
					break;
				}
			}

			if (accept) {
				// 2.2 if files > 10 remove the first from the list
				if (nfiles.size() >= 10) {
					nfiles.pop();
				}
				// 2.3 create new fileMeta
				fileMeta = new FileMeta();
				fileMeta.setFileName(originalFileName);
				fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
				fileMeta.setFileType(mpf.getContentType());
				try {
					fileMeta.setBytes(mpf.getBytes());
					fileMeta.setInputFilePath(inputPrePath + File.separator
							+ newFileName);
					fileMeta.setOutputFileName(outputPrePath + File.separator
							+ fileNameWithoutSuffix);

					File dir = new File(inputPrePath);
					dir.mkdirs();
					FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
							inputPrePath + File.separator + originalFileName));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 2.4 add to files
				try {
					out = response.getWriter();
					JSONObject result = new JSONObject();
					result.put("status", "sucess");
					result.put("filepath", inputPrePath + File.separator
							+ originalFileName);
					result.put("filename", originalFileName);
					out.write(result.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nfiles.add(fileMeta);
				files = nfiles;
			} else {

				try {
					out = response.getWriter();
					JSONObject result = new JSONObject();
					result.put("status", "failed");
					out.write(result.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	@RequestMapping(value = "loggenerator", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> loggenerator(
			MultipartHttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		LinkedList<FileMeta> nfiles = new LinkedList<FileMeta>();
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			// webapps/bpmspace/lgfiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "lgfiles"
					+ File.separator
					+ session.getAttribute("userId").toString();
			String inputPrePath = realPrePath + File.separator + "models";
			String outputPrePath = realPrePath + File.separator + "logs";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String originalFileName = "";
			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			// 2.2 if files > 10 remove the first from the list
			if (nfiles.size() >= 10) {
				nfiles.pop();
			}

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			if (suffix.equals(".pnml") == false) {
				fileMeta.setInvalid();
			}
			fileMeta.setFileName(originalFileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			try {
				fileMeta.setBytes(mpf.getBytes());
				fileMeta.setInputFilePath(inputPrePath + File.separator
						+ newFileName);
				fileMeta.setOutputFileName(outputPrePath + File.separator
						+ fileNameWithoutSuffix);

				File dir = new File(inputPrePath);
				dir.mkdirs();
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						inputPrePath + File.separator + newFileName));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 2.4 add to files
			nfiles.add(fileMeta);
			files = nfiles;
		}

		return nfiles;
	}

	@RequestMapping(value = "miningevaluator", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> miningevaluator(
			MultipartHttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		LinkedList<FileMeta> nfiles = new LinkedList<FileMeta>();
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			// webapps/bpmspace/amefiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "amefiles"
					+ File.separator
					+ session.getAttribute("userId").toString();
			String inputPrePath = realPrePath + File.separator + "models";
			String outputPrePath = realPrePath + File.separator + "logs";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String originalFileName = "";
			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println(originalFileName + " uploaded! " + files.size());

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			// 2.2 if files > 10 remove the first from the list
			if (nfiles.size() >= 10) {
				nfiles.pop();
			}

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			if (suffix.equals(".pnml") == false) {
				fileMeta.setInvalid();
			}
			fileMeta.setFileName(originalFileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			try {
				fileMeta.setBytes(mpf.getBytes());
				fileMeta.setInputFilePath(inputPrePath + File.separator
						+ newFileName);
				fileMeta.setOutputFileName(outputPrePath + File.separator
						+ fileNameWithoutSuffix);

				File dir = new File(inputPrePath);
				dir.mkdirs();
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						inputPrePath + File.separator + newFileName));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 2.4 add to files
			nfiles.add(fileMeta);
			files = nfiles;
		}

		return nfiles;
	}

	@RequestMapping(value = "conformancecheck", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> conformancecheck(
			MultipartHttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		LinkedList<FileMeta> nfiles = new LinkedList<FileMeta>();
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			// webapps/bpmspace/ccfiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "ccfiles"
					+ File.separator
					+ session.getAttribute("userId").toString();
			String inputPrePath = realPrePath + File.separator + "logs";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String originalFileName = "";
			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			// 2.2 if files > 10 remove the first from the list
			if (nfiles.size() >= 10) {
				nfiles.pop();
			}

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			if (suffix.equals(".mxml") == false) {
				fileMeta.setInvalid();
			}
			fileMeta.setFileName(originalFileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			try {
				fileMeta.setBytes(mpf.getBytes());
				fileMeta.setInputFilePath(inputPrePath + File.separator
						+ newFileName);

				File dir = new File(inputPrePath);
				dir.mkdirs();
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						inputPrePath + File.separator + newFileName));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 2.4 add to files
			nfiles.add(fileMeta);
			files = nfiles;
		}

		return nfiles;
	}

	@RequestMapping(value = "socialnetworkmining", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> socialnetworkmining(
			MultipartHttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		LinkedList<FileMeta> nfiles = new LinkedList<FileMeta>();
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			// webapps/bpmspace/snmfiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "snmfiles"
					+ File.separator
					+ session.getAttribute("userId").toString();
			String inputPrePath = realPrePath + File.separator + "logs";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String originalFileName = "";
			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			// 2.2 if files > 10 remove the first from the list
			if (nfiles.size() >= 10) {
				nfiles.pop();
			}

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			if (suffix.equals(".mxml") == false) {
				fileMeta.setInvalid();
			}
			fileMeta.setFileName(originalFileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			try {
				fileMeta.setBytes(mpf.getBytes());
				fileMeta.setInputFilePath(inputPrePath + File.separator
						+ newFileName);

				File dir = new File(inputPrePath);
				dir.mkdirs();
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						inputPrePath + File.separator + newFileName));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 2.4 add to files
			nfiles.add(fileMeta);
			files = nfiles;
		}

		return nfiles;
	}

	/***************************************************
	 * get(): get file as an attachment
	 * 
	 * @param response
	 *            : passed by the server
	 * @param value
	 *            : value from the URL
	 * @return void
	 ****************************************************/
	@RequestMapping(value = "get/{value}", method = RequestMethod.GET)
	public void get(HttpServletResponse response, @PathVariable String value) {
		FileMeta getFile = files.get(Integer.parseInt(value));
		try {
			response.setContentType(getFile.getFileType());
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ getFile.getFileName() + "\"");
			FileCopyUtils.copy(getFile.getBytes(), response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***************************************************
	 * download(): get file as an attachment
	 * 
	 * @param response
	 *            : passed by the server
	 * @param value
	 *            : value from the URL
	 * @return void
	 ****************************************************/
	@RequestMapping(value = "download/{folder}/{user}/{type}/{name}/{suffix}", method = RequestMethod.GET)
	public void download(HttpServletResponse response,
			@PathVariable("folder") String folder,
			@PathVariable("user") String user,
			@PathVariable("type") String type,
			@PathVariable("name") String name,
			@PathVariable("suffix") String suffix) {
		String filePath = FileUtil.WEBAPP_ROOT + folder + File.separator + user
				+ File.separator + type + File.separator + name + "." + suffix;
		try {
			File f = new File(filePath);
			String fileName = filePath.substring(filePath
					.lastIndexOf(File.separator) + 1);

			// get file into bytes[]
			FileInputStream fi = new FileInputStream(f);
			long fileSize = f.length();
			byte[] buffer = new byte[(int) fileSize];
			int offset = 0, numRead = 0;
			;
			while (offset < buffer.length
					&& (numRead = fi.read(buffer, offset, buffer.length
							- offset)) >= 0) {
				offset += numRead;
			}
			if (offset != buffer.length) {
				fi.close();
				throw new IOException("Could not completely read file "
						+ f.getName());
			}
			fi.close();

			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ fileName + "\"");
			FileCopyUtils.copy(buffer, response.getOutputStream());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "modelquery", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> modelquery(
			MultipartHttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {

			String suffix = null;
			String fileNameWithoutSuffix = null;
			String newFileName = null;
			// webapps/bpmspace/pmfiles/userId
			String realPrePath = FileUtil.WEBAPP_ROOT + "querymodel";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String originalFileName = "";
			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// System.out.println(originalFileName +" uploaded! "+files.size());

			suffix = originalFileName.substring(originalFileName
					.lastIndexOf("."));
			fileNameWithoutSuffix = UUID.randomUUID().toString();
			newFileName = fileNameWithoutSuffix + suffix;

			// 2.2 if files > 10 remove the first from the list
			if (files.size() >= 10) {
				files.pop();
			}

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			fileMeta.setFileName(originalFileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			fileMeta.setInputFilePath(realPrePath + File.separator
					+ newFileName);
			try {
				fileMeta.setBytes(mpf.getBytes());

				File dir = new File(realPrePath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(
						realPrePath + File.separator + newFileName));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 2.4 add to files
			files.add(fileMeta);

		}

		return files;
	}

	@RequestMapping(value = "modeljarfile", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> modeljarfile(
			MultipartHttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {
			String libPath = FileUtil.WEBAPP_ROOT + "WEB-INF\\lib";

			mpf = request.getFile(itr.next());
			String value = mpf.getOriginalFilename();
			String originalFileName = "";
			try {
				// 解决中文乱码
				originalFileName = new String(value.getBytes("ISO8859-1"),
						"UTF-8");
				;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// 2.2 if files > 10 remove the first from the list
			if (files.size() >= 10) {
				files.pop();
			}

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			fileMeta.setFileName(originalFileName);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			fileMeta.setInputFilePath(libPath + File.separator
					+ originalFileName);
			try {
				fileMeta.setBytes(mpf.getBytes());

				File dir = new File(libPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(libPath
						+ File.separator + originalFileName));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 2.4 add to files
			files.add(fileMeta);
		}

		return files;
	}
}