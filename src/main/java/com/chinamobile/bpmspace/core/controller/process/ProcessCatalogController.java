package com.chinamobile.bpmspace.core.controller.process;

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

import com.chinamobile.bpmspace.core.domain.process.ProcessCatalog;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.ProcessCatalogRepository;

@Controller
@RequestMapping("processCatalog")
public class ProcessCatalogController {

	@RequestMapping(value = "getRootCatalogs", method = RequestMethod.GET)
	public void getRootCatalogs(HttpSession session,
			HttpServletResponse response) {
		ProcessCatalogRepository pcr = new ProcessCatalogRepository();
		JSONArray result = new JSONArray();
		try {
			List<ProcessCatalog> catalogs = pcr.getRootProcessCatalogs(session
					.getAttribute("userId").toString());
			for (ProcessCatalog c : catalogs) {
				JSONObject o = new JSONObject();
				o.put("title", c.getName());
				o.put("key", c.getId());
				if (pcr.getProcessCatalogs(c.getId()).size() == 0) {
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

	@RequestMapping(value = "getCatalogs", method = RequestMethod.GET)
	public void getCatalogs(@RequestParam("catalogId") String _catalogId,
			HttpServletResponse response) {
		ProcessCatalogRepository pcr = new ProcessCatalogRepository();
		JSONArray result = new JSONArray();
		try {
			List<ProcessCatalog> catalogs = pcr.getProcessCatalogs(_catalogId);
			for (ProcessCatalog c : catalogs) {
				JSONObject o = new JSONObject();
				o.put("title", c.getName());
				o.put("key", c.getId());
				if (pcr.getProcessCatalogs(c.getId()).size() == 0) {
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

	@RequestMapping(value = "addCatalog", method = RequestMethod.POST)
	public void addCatalog(@RequestParam("name") String _name,
			@RequestParam("parentId") String _parentId, HttpSession session,
			HttpServletResponse response) {
		ProcessCatalogRepository pcr = new ProcessCatalogRepository();
		JSONObject result = new JSONObject();
		try {
			String userId = session.getAttribute("userId").toString();
			String catalogId = pcr.addProcessCatalog(_name, _parentId, userId);
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
		ProcessCatalogRepository pcr = new ProcessCatalogRepository();
		JSONObject result = new JSONObject();
		try {
			pcr.removeProcessCatalog(_catalogId, session.getAttribute("userId")
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

}
