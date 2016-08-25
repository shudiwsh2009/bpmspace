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
import com.chinamobile.bpmspace.core.repository.model.statistics.ModelStatisticsRepository;

@Controller
@RequestMapping("model")
public class ModelStatisticsController {

	// @RequestMapping(value = "modelStatistics", method = RequestMethod.POST)
	// public void modelStatistics(@RequestParam("path") String path,
	// @RequestParam("processIds") String processIds,
	// @RequestParam("options") String options, HttpSession session,
	// HttpServletResponse response) {
	// ModelStatisticsRepository msr = new ModelStatisticsRepository();
	// JSONObject result = new JSONObject();
	// try {
	// String output = msr.modelStatistics(path, processIds, options,
	// session.getAttribute("userId").toString());
	// result.put("state", "SUCCESS");
	// result.put("output", output);
	// } catch (BasicException e) {
	// result.put("state", "FAILED");
	// result.put("message", e.getInfo());
	// }
	//
	// // send response
	// response.setContentType("application/json");
	// response.setCharacterEncoding("UTF-8");
	// PrintWriter out;
	// try {
	// out = response.getWriter();
	// out.write(result.toString());
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	@RequestMapping(value = "modelStatistics", method = RequestMethod.POST)
	public void modelFragmentation(
			@RequestParam(value = "filepaths[]", required = false) String[] filepaths,
			@RequestParam(value = "processIds[]", required = false) String[] processIds,
			@RequestParam("options") String options, HttpSession session,
			HttpServletResponse response) {
		ModelStatisticsRepository msr = new ModelStatisticsRepository();
		JSONObject result = new JSONObject();
		try {
			String output = msr.modelStatistics(filepaths, processIds, options,
					session.getAttribute("userId").toString());
			result.put("state", "SUCCESS");
			result.put("output", output);
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

	@RequestMapping(value = "deleteStatisticsTempFiles", method = RequestMethod.POST)
	public void deleteStatisticsTempFiles(
			@RequestParam("filepaths") String filepaths, HttpSession session,
			HttpServletResponse response) {
		ModelStatisticsRepository msr = new ModelStatisticsRepository();
		JSONObject result = new JSONObject();
		msr.deleteStatisticsTempFiles(filepaths, session.getAttribute("userId")
				.toString());
		result.put("state", "SUCCESS");

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
