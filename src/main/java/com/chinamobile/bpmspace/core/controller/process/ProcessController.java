package com.chinamobile.bpmspace.core.controller.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.ModelRepository;
import com.chinamobile.bpmspace.core.repository.ProcessRepository;
import com.chinamobile.bpmspace.core.util.DateUtil;
import com.chinamobile.bpmspace.core.util.FileUtil;

@Controller
@RequestMapping("process")
public class ProcessController {

	@RequestMapping(value = "checkProcess", method = RequestMethod.POST)
	public void checkProcess(@RequestParam("processName") String _name,
			@RequestParam("processDescription") String _description,
			@RequestParam("catalogId") String _catalogId,
			@RequestParam("processType") String _typeStr, HttpSession session,
			HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {

			boolean flag = pr.checkProcess(_name, _description, _catalogId,
					Process.convertType(_typeStr),
					session.getAttribute("userId").toString());
			if (flag == true) {
				result.put("state", "SUCCESS");
			}
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
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

	@RequestMapping(value = "addProcess", method = RequestMethod.POST)
	public void addProcess(
			@RequestParam("processName") String _name,
			@RequestParam("processDescription") String _description,
			@RequestParam("catalogId") String _catalogId,
			@RequestParam("processType") String _typeStr,
			@RequestParam(value = "jsonPath", required = false, defaultValue = "") String _jsonPath,
			@RequestParam(value = "svgPath", required = false, defaultValue = "") String _svgPath,
			@RequestParam(value = "xmlPath", required = false, defaultValue = "") String _xmlPath,
			HttpSession session, HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {
			// String _petriPath = _xmlPath;
			String[] addResult = pr.addProcess(_name, _description, _catalogId,
					Process.convertType(_typeStr),
					session.getAttribute("userId").toString(), _jsonPath,
					_svgPath, _xmlPath);
			result.put("state", "SUCCESS");
			result.put("processId", addResult[0]);
			result.put("svgPath", addResult[1]);
			result.put("name", addResult[2]);
			result.put("type", addResult[3]);
			result.put("createInfo", addResult[4]);
			result.put("lastModifyInfo", addResult[5]);
			result.put("size", addResult[6]);
			result.put("revision", addResult[7]);
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
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

	@RequestMapping(value = "checkEditProcess", method = RequestMethod.POST)
	public void checkEditProcess(@RequestParam("processId") String _processId,
			HttpSession session, HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {
			String[] reStrings = pr.checkEditProcess(_processId, session
					.getAttribute("userId").toString());
			result.put("state", "SUCCESS");
			result.put("xmlPath", reStrings[0]);
			result.put("type", reStrings[1]);
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
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

	@RequestMapping(value = "editProcess", method = RequestMethod.POST)
	public void editProcess(
			@RequestParam("processId") String _processId,
			@RequestParam(value = "jsonPath", required = false, defaultValue = "") String _jsonPath,
			@RequestParam(value = "svgPath", required = false, defaultValue = "") String _svgPath,
			@RequestParam(value = "xmlPath", required = false, defaultValue = "") String _xmlPath,
			HttpSession session, HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {
			String[] addResult = pr.editProcess(_processId, session
					.getAttribute("userId").toString(), _jsonPath, _svgPath,
					_xmlPath);
			result.put("state", "SUCCESS");
			result.put("processId", addResult[0]);
			result.put("svgPath", addResult[1]);
			result.put("name", addResult[2]);
			result.put("type", addResult[3]);
			result.put("createInfo", addResult[4]);
			result.put("lastModifyInfo", addResult[5]);
			result.put("size", addResult[6]);
			result.put("revision", addResult[7]);
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
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

	@RequestMapping(value = "renameProcess", method = RequestMethod.POST)
	public void renameProcess(@RequestParam("value") String _name,
			@RequestParam("processId") String _processId, HttpSession session,
			HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {
			pr.renameProcess(_processId, _name, session.getAttribute("userId")
					.toString());
			result.put("state", "SUCCESS");
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
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

	@RequestMapping(value = "removeProcesses", method = RequestMethod.POST)
	public void removeProcesses(@RequestParam("processIds") String _processIds,
			HttpSession session, HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {
			String[] reStrings = pr.removeProcesses(_processIds, session
					.getAttribute("userId").toString());
			if (reStrings[0].equals("")) {
				result.put("state", "SUCCESS");
			} else {
				result.put("state", "PART");
				result.put("processIds", reStrings[0]);
				result.put("processNames", reStrings[1]);
				result.put("message", "部分流程用户无权限删除");
			}
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
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

	@RequestMapping(value = "moveProcesses", method = RequestMethod.POST)
	public void moveProcesses(
			@RequestParam("processIds") String _processIds,
			@RequestParam(value = "operator", required = false, defaultValue = "") String _operator,
			@RequestParam("targetCatalogId") String _targetCatalogId,
			HttpSession session, HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {
			String[] reStrings = pr.moveProcesses(_processIds,
					_targetCatalogId, _operator, session.getAttribute("userId")
							.toString());
			if (reStrings[0].equals("") && reStrings[2].equals("")) {
				result.put("state", "SUCCESS");
			} else {
				if (!reStrings[0].equals("")) {
					result.put("state", "PERMISSION");
					result.put("permissionIds", reStrings[0]);
					result.put("permissionNames", reStrings[1]);
					result.put("permissionMessage", "部分流程用户无权限移动");
				}
				if (!reStrings[2].equals("")) {
					result.put("state", "DUPLICATE");
					result.put("duplicateIds", reStrings[2]);
					result.put("duplicateNames", reStrings[3]);
					result.put("duplicateMessage", "目标目录存在同名流程");
				}
				if (!reStrings[0].equals("") && !reStrings[2].equals("")) {
					result.put("state", "BOTH");
				}
			}
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
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

	@RequestMapping(value = "cloneProcesses", method = RequestMethod.POST)
	public void cloneProcesses(
			@RequestParam("processIds") String _processIds,
			@RequestParam(value = "operator", required = false, defaultValue = "") String _operator,
			@RequestParam("targetCatalogId") String _targetCatalogId,
			HttpSession session, HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {
			String[] reStrings = pr.cloneProcesses(_processIds,
					_targetCatalogId, _operator, session.getAttribute("userId")
							.toString());
			if (reStrings[0].equals("") && reStrings[2].equals("")) {
				result.put("state", "SUCCESS");
			} else {
				if (!reStrings[0].equals("")) {
					result.put("state", "PERMISSION");
					result.put("permissionIds", reStrings[0]);
					result.put("permissionNames", reStrings[1]);
					result.put("permissionMessage", "部分流程用户无权限拷贝");
				}
				if (!reStrings[2].equals("")) {
					result.put("state", "DUPLICATE");
					result.put("duplicateIds", reStrings[2]);
					result.put("duplicateNames", reStrings[3]);
					result.put("duplicateMessage", "目标目录存在同名流程");
				}
				if (!reStrings[0].equals("") && !reStrings[2].equals("")) {
					result.put("state", "BOTH");
				}
			}
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
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

	@RequestMapping(value = "getProcesses", method = RequestMethod.GET)
	public void getProcesses(@RequestParam("catalogId") String _catalogId,
			@RequestParam(value = "iDisplayStart") String _indexStart,
			@RequestParam(value = "iDisplayLength") String _pageSize,
			HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		ModelRepository mr = new ModelRepository();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			List<Process> processes = pr.getProcesses(_catalogId, _indexStart,
					_pageSize);
			long totalCount = pr.countProcesses(_catalogId);
			for (Process p : processes) {
				JSONArray ja = new JSONArray();
				if (p.getRevision().size() == 0) {
					continue;
				}
				String modelId = p.getRevision()
						.get((long) p.getRevision().size()).getModelId();
				Model model = mr.getModel(modelId);
				String svgSrc = FileUtil.SVG_PREFIX
						+ FileUtil.nameGridFSFile(model.getCreatorId(),
								model.getProcessId(), model.getRevision())
						+ FileUtil.SVG_SUFFIX;
				// lvcheng 不要删了这句话。。
				ja.put("<embed src=\"" + "assets/details_open.png\">");
				ja.put(p.getId());
				ja.put(svgSrc);
				ja.put(p.getName());
				ja.put(Process.convertType(p.getType()));
				ja.put(p.getOwnerName() + " 于 "
						+ DateUtil.convertDate(p.getCreateTime()));
				ja.put(model.getCreatorName() + " 于 "
						+ DateUtil.convertDate(model.getCreateTime()));
				BigDecimal sizeBD = new BigDecimal(model.getSize() / 1024);
				sizeBD = sizeBD.setScale(1, BigDecimal.ROUND_HALF_UP);
				ja.put(sizeBD.toString() + " KB");
				ja.put(p.getRevision().size());
				array.put(ja);
			}
			result.put("aaData", array);
			result.put("iTotalRecords", totalCount);
			result.put("iTotalDisplayRecords", totalCount);// array.length()
			result.put("state", "SUCCESS");
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
			result.put("aaData", e.getInfo());
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

	@RequestMapping(value = "exportProcesses", method = RequestMethod.POST)
	public void exportProcesses(@RequestParam("processIds") String _processIds,
			HttpSession session, HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {
			String exportFile = pr.exportProcesses(_processIds, session
					.getAttribute("userId").toString());
			result.put("state", "SUCCESS");
			result.put("exports", exportFile);
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
		} catch (IOException e) {
			result.put("state", "FAILED");
			result.put("message", "文件不存在");
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

	@RequestMapping(value = "importProcesses", method = RequestMethod.POST)
	public void importProcesses(HttpSession session,
			HttpServletResponse response) {
		// ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();

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

	@RequestMapping(value = "importProcess", method = RequestMethod.POST)
	public void importProcess(
			@RequestParam("processName") String _name,
			@RequestParam("processDescription") String _description,
			@RequestParam("catalogId") String _catalogId,
			@RequestParam("processType") String _typeStr,
			@RequestParam(value = "jsonPath", required = false, defaultValue = "") String _jsonPath,
			@RequestParam(value = "xmlPath", required = false, defaultValue = "") String _xmlPath,
			HttpSession session, HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {
			String[] importResult = pr.importProcessPnml(_name, _description,
					_catalogId, Process.convertType(_typeStr), session
							.getAttribute("userId").toString(), _jsonPath,
					_xmlPath);
			result.put("state", "SUCCESS");
			result.put("processId", importResult[0]);
			result.put("svgPath", importResult[1]);
			result.put("name", importResult[2]);
			result.put("type", importResult[3]);
			result.put("createInfo", importResult[4]);
			result.put("lastModifyInfo", importResult[5]);
			result.put("size", importResult[6]);
			result.put("revision", importResult[7]);
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
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

	@RequestMapping(value = "pnmlToPng", method = RequestMethod.POST)
	public void pnmlToPng(@RequestParam("pnmlFile") String pnmlFile,
			HttpSession session, HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {
			String pngFile = pr.processPnmlToPng(pnmlFile);
			result.put("state", "SUCCESS");
			result.put("pngFile", pngFile);
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
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

	@RequestMapping(value = "epmlToPng", method = RequestMethod.POST)
	public void epmlToPng(@RequestParam("epmlFile") String epmlFile,
			HttpSession session, HttpServletResponse response) {
		ProcessRepository pr = new ProcessRepository();
		JSONObject result = new JSONObject();
		try {
			String pngFile = pr.processEpmlToPng(epmlFile);
			result.put("state", "SUCCESS");
			result.put("pngFile", pngFile);
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", e.getInfo());
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
