package com.chinamobile.bpmspace.core.controller.log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinamobile.bpmspace.core.domain.log.Log;
import com.chinamobile.bpmspace.core.domain.log.LogCatalog;
import com.chinamobile.bpmspace.core.exception.ActionRejectException;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.repository.InstanceCatalogRepository;
import com.chinamobile.bpmspace.core.repository.LogRepository;

@Controller
@RequestMapping("instanceCatalog")
public class LogCatalogController {

	@RequestMapping(value = "getRootCatalogs", method = RequestMethod.GET)
	public void getRootCatalogs(HttpSession session,
			HttpServletResponse response) throws EmptyFieldException,
			NoExistException, ActionRejectException {
		InstanceCatalogRepository icr = new InstanceCatalogRepository();
		JSONArray result = new JSONArray();
		try {
			List<LogCatalog> catalogs = icr.getRootInstanceCatalogs(session
					.getAttribute("userId").toString());
			for (LogCatalog c : catalogs) {
				JSONObject o = new JSONObject();
				o.put("title", c.getName());
				o.put("key", c.getId());
				if (icr.getInstanceCatalogs(c.getId()).size() == 0) {
					o.put("isLazy", false);
				} else {
					o.put("isLazy", true);
				}
				o.put("isFolder", true);
				result.put(o);
			}
		} catch (BasicException e) {

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

	@RequestMapping(value = "catalog_load", method = RequestMethod.GET)
	@ResponseBody
	public void getInstance(@RequestParam("catalogId") String _catalogId,
			HttpServletResponse response) throws BasicException {
		// InstanceRepository ir = new InstanceRepository();
		InstanceCatalogRepository icr = new InstanceCatalogRepository();
		LogRepository lr = new LogRepository();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		List<LogCatalog> instanceCatalogs = icr.getInstanceCatalogs(_catalogId);
		List<Log> logs = lr.findLogByCatalogId(_catalogId);

		for (LogCatalog ic : instanceCatalogs) {

			JSONArray ja = new JSONArray();

			ja.put(ic.getId());
			ja.put("文件夹");
			ja.put(ic.getName());
			if (ic.getParentId().equals("")) {
				ja.put(File.separator);
			} else {
				ja.put(getAbsolutePath(ic.getParentId()));
			}
			ja.put(ic.getCreateTime());

			array.put(ja);

		}

		for (Log l : logs) {

			JSONArray jb = new JSONArray();

			jb.put(l.getId());
			jb.put("日志文件");
			jb.put(l.getName());
			jb.put(getAbsolutePath(l.getCatalogId()));
			jb.put(l.getCreateTime());

			array.put(jb);

		}
		result.put("aaData", array);

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

	public String getAbsolutePath(String _itemId) throws EmptyFieldException,
			NoExistException {
		InstanceCatalogRepository icr = new InstanceCatalogRepository();
		// LogRepository lr = new LogRepository();

		LogCatalog ic = null;

		ic = icr.getInstanceCatalog(_itemId);

		if (ic.getParentId().equals("")) {
			return ic.getName() + File.separator;
		} else {
			return getAbsolutePath(ic.getParentId()) + ic.getName()
					+ File.separator;
		}

	}

	@RequestMapping(value = "getCatalogs", method = RequestMethod.GET)
	public void getCatalogsInstance(
			@RequestParam("catalogId") String _catalogId,
			HttpServletResponse response) {
		InstanceCatalogRepository icr = new InstanceCatalogRepository();
		JSONArray result = new JSONArray();
		LogRepository lr = new LogRepository();
		try {
			List<LogCatalog> catalogs = icr.getInstanceCatalogs(_catalogId);
			for (LogCatalog c : catalogs) {
				JSONObject o = new JSONObject();
				o.put("title", c.getName());
				o.put("key", c.getId());
				if (icr.getInstanceCatalogs(c.getId()).size() == 0) {
					o.put("isLazy", false);
				} else {
					o.put("isLazy", true);
				}
				o.put("isFolder", true);
				result.put(o);
			}

			List<Log> logs = lr.findLogByCatalogId(_catalogId);
			for (Log l : logs) {
				JSONObject o = new JSONObject();
				o.put("title", l.getName());
				o.put("key", l.getId());
				o.put("isLazy", false);
				o.put("expand", false);
				o.put("isFolder", false);
				result.put(o);
			}
		} catch (BasicException e) {

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

	@RequestMapping(value = "addCatalog", method = RequestMethod.GET)
	public void addCatalog(@RequestParam("name") String _name,
			@RequestParam("parentId") String _parentId, HttpSession session,
			HttpServletResponse response) {
		InstanceCatalogRepository icr = new InstanceCatalogRepository();
		JSONObject result = new JSONObject();
		try {
			String userId = session.getAttribute("userId").toString();
			String catalogId = icr.addInstanceCatalog(_name, _parentId, userId);
			result.put("state", "SUCCESS");
			result.put("catalogId", catalogId);
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

	@RequestMapping(value = "removeCatalog", method = RequestMethod.POST)
	public void removeCatalog(@RequestParam("catalogId") String _catalogId,
			HttpSession session, HttpServletResponse response) {
		InstanceCatalogRepository icr = new InstanceCatalogRepository();
		JSONObject result = new JSONObject();
		try {
			icr.removeInstanceCatalog(_catalogId, session
					.getAttribute("userId").toString());
			result.put("state", "SUCCESS");
		} catch (BasicException e) {
			result.put("state", "FAILED");
			result.put("message", "删除成功！");
			// result.put("message", e.getInfo());
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

	@RequestMapping(value = "moveLogs", method = RequestMethod.POST)
	public void moveProcesses(
			@RequestParam("logIds") String _logIds,
			@RequestParam(value = "operator", required = false, defaultValue = "") String _operator,
			@RequestParam("targetCatalogId") String _targetCatalogId,
			HttpSession session, HttpServletResponse response) {
		LogRepository lr = new LogRepository();
		JSONObject result = new JSONObject();
		try {
			String[] reStrings = lr.moveLogs(_logIds, _targetCatalogId,
					_operator, session.getAttribute("userId").toString());
			if (reStrings[0].equals("") && reStrings[2].equals("")) {
				result.put("state", "SUCCESS");
			} else {
				if (!reStrings[2].equals("")) {
					result.put("state", "DUPLICATE");
					result.put("duplicateIds", reStrings[2]);
					result.put("duplicateNames", reStrings[3]);
					result.put("duplicateMessage", "目标目录存在同名流程");
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

	@RequestMapping(value = "cloneLogs", method = RequestMethod.POST)
	public void cloneProcesses(
			@RequestParam("logIds") String _logIds,
			@RequestParam(value = "operator", required = false, defaultValue = "") String _operator,
			@RequestParam("targetCatalogId") String _targetCatalogId,
			HttpSession session, HttpServletResponse response) {
		LogRepository lr = new LogRepository();
		JSONObject result = new JSONObject();
		try {
			String[] reStrings = lr.cloneLogs(_logIds, _targetCatalogId,
					_operator, session.getAttribute("userId").toString());
			if (reStrings[0].equals("") && reStrings[2].equals("")) {
				result.put("state", "SUCCESS");
			} else {
				if (!reStrings[2].equals("")) {
					result.put("state", "DUPLICATE");
					result.put("duplicateIds", reStrings[2]);
					result.put("duplicateNames", reStrings[3]);
					result.put("duplicateMessage", "目标目录存在同名流程");
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
}
