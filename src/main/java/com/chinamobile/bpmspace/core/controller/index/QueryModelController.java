package com.chinamobile.bpmspace.core.controller.index;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.QueryModelRepository;
import com.chinamobile.bpmspace.core.util.FileUtil;

@Controller
@RequestMapping("querymodel")
public class QueryModelController {

	@RequestMapping(value = "textquery", method = RequestMethod.POST)
	public void textQuery(@RequestParam("iid") String indexid,
			@RequestParam("q") String query, HttpSession session,
			HttpServletResponse response) {
		JSONObject result = new JSONObject();

		QueryModelRepository qmr = new QueryModelRepository();

		try {
			JSONArray list = qmr.queryByQueryLanguage(indexid, query);
			// result.put("state", "SUCCESS");
			result.put("aaData", list);
		} catch (BasicException e) {
			result.put("state", "FAIL");
			result.put("aaData", "[]");
			result.put("info", e.getInfo());
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

	@RequestMapping(value = "graphquery_file", method = RequestMethod.POST)
	public void graphQueryWithDraw(@RequestParam("iid") String indexid,
			@RequestParam("q") String xmlPath, HttpSession session,
			HttpServletResponse response) {
		JSONObject result = new JSONObject();

		QueryModelRepository qmr = new QueryModelRepository();

		try {
			Model model = new Model();
			String xmlContent = FileUtil.readModelFile(xmlPath)[0];
			model.setXmlFilename(xmlContent);
			JSONArray list = qmr.queryByModel(indexid, model);
			result.put("aaData", list);
		} catch (BasicException e) {
			result.put("state", "FAIL");
			result.put("aaData", "[]");
			result.put("info", e.getInfo());
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

	@RequestMapping(value = "graphquery_repository", method = RequestMethod.POST)
	public void graphQueryWithRepository(@RequestParam("iid") String indexid,
			@RequestParam("q") String modelId, HttpSession session,
			HttpServletResponse response) {
		JSONObject result = new JSONObject();

		QueryModelRepository qmr = new QueryModelRepository();

		try {
			MongoAccess ma = new MongoAccess();
			Model model = ma.getModelById(modelId);
			JSONArray list = qmr.queryByModel(indexid, model);
			// result.put("state", "SUCCESS");
			result.put("aaData", list);
		} catch (BasicException e) {
			result.put("state", "FAIL");
			result.put("aaData", "[]");
			result.put("info", e.getInfo());
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
