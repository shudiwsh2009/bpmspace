package com.chinamobile.bpmspace.core.controller.model;

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

import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.model.differentiation.ModelDifferentiationRepository;

@Controller
@RequestMapping("model")
public class ModelDifferentiationController {

	@RequestMapping(value = "differentiationInRepository", method = RequestMethod.POST)
	public void differentiationInRepository(
			@RequestParam("processId1") String processId1,
			@RequestParam("processId2") String processId2, HttpSession session,
			HttpServletResponse response) {
		ModelDifferentiationRepository mdr = new ModelDifferentiationRepository();
		JSONObject result = new JSONObject();
		try {
			List<String> opList = mdr.differentiationInRepository(processId1,
					processId2);
			JSONArray array = new JSONArray();
			for (String op : opList) {
				array.put(op);
			}
			result.put("opList", array);
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

	@RequestMapping(value = "differentiationInFile", method = RequestMethod.POST)
	public void differentiationInFile(
			@RequestParam("filepath1") String filepath1,
			@RequestParam("filepath2") String filepath2, HttpSession session,
			HttpServletResponse response) {
		ModelDifferentiationRepository mdr = new ModelDifferentiationRepository();
		JSONObject result = new JSONObject();
		try {
			List<String> opList = mdr.differentiationInFile(filepath1,
					filepath2);
			JSONArray array = new JSONArray();
			for (String op : opList) {
				array.put(op);
			}
			result.put("opList", array);
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

	@RequestMapping(value = "differentiationInFileAndRepository", method = RequestMethod.POST)
	public void differentiationInFileAndRepository(
			@RequestParam("filepath") String filepath,
			@RequestParam("processId") String processId, HttpSession session,
			HttpServletResponse response) {
		ModelDifferentiationRepository mdr = new ModelDifferentiationRepository();
		JSONObject result = new JSONObject();
		try {
			List<String> opList = mdr.differentiationInFileAndRepository(
					filepath, processId);
			JSONArray array = new JSONArray();
			for (String op : opList) {
				array.put(op);
			}
			result.put("opList", array);
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
