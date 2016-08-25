package com.chinamobile.bpmspace.core.controller.model;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.model.clustering.ModelClusteringRepository;

@Controller
@RequestMapping("model")
public class ModelClusteringController {

	@RequestMapping(value = "modelClustering", method = RequestMethod.POST)
	public void modelClustering(
			@RequestParam(value = "filepaths[]", required = false) String[] filepaths,
			@RequestParam(value = "processIds[]", required = false) String[] processIds,
			HttpSession session, HttpServletResponse response) {
		ModelClusteringRepository mcr = new ModelClusteringRepository();
		JSONObject result = new JSONObject();
		try {
			String clustering = mcr.modelClustering(filepaths, processIds,
					session.getAttribute("userId").toString());
			result.put("state", "SUCCESS");
			result.put("clustering", clustering);
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
