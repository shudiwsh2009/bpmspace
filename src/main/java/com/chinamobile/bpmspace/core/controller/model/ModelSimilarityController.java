package com.chinamobile.bpmspace.core.controller.model;

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

import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.model.similarity.ModelSimilarityRepository;

@Controller
@RequestMapping("model")
public class ModelSimilarityController {

	@RequestMapping(value = "similarityAlgorithm", method = RequestMethod.POST)
	public void similarityAlgorithm(HttpSession session,
			HttpServletResponse response) {
		ModelSimilarityRepository msr = new ModelSimilarityRepository();
		JSONObject result = new JSONObject();
		// try {
		List<String> algorithms = msr.getSimilarityMeasureAlgorithms();
		JSONArray array = new JSONArray();
		for (String a : algorithms) {
			array.put(a);
		}
		result.put("algorithms", array);
		result.put("state", "SUCCESS");
		// } catch (BasicException e) {
		// result.put("state", "FAILED");
		// result.put("message", e.getInfo());
		// }

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

	@RequestMapping(value = "similarityInRepository", method = RequestMethod.POST)
	public void similarityInRepository(
			@RequestParam("processId1") String processId1,
			@RequestParam("processId2") String processId2,
			@RequestParam("algorithm") String algorithm, HttpSession session,
			HttpServletResponse response) {
		ModelSimilarityRepository msr = new ModelSimilarityRepository();
		JSONObject result = new JSONObject();
		try {
			float sim = msr.similarityInRepository(processId1, processId2,
					algorithm);
			result.put("state", "SUCCESS");
			if (Float.isNaN(sim)) {
				result.put("similarity", "NaN");
			} else {
				BigDecimal bd = new BigDecimal(sim);
				bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
				result.put("similarity", bd.toString());
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

	@RequestMapping(value = "similarityInFile", method = RequestMethod.POST)
	public void similarityInFile(@RequestParam("filepath1") String filepath1,
			@RequestParam("filepath2") String filepath2,
			@RequestParam("algorithm") String algorithm, HttpSession session,
			HttpServletResponse response) {
		ModelSimilarityRepository msr = new ModelSimilarityRepository();
		JSONObject result = new JSONObject();
		try {
			float sim = msr.similarityInFile(filepath1, filepath2, algorithm);
			result.put("state", "SUCCESS");
			if (Float.isNaN(sim)) {
				result.put("similarity", "NaN");
			} else {
				BigDecimal bd = new BigDecimal(sim);
				bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
				result.put("similarity", bd.toString());
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

	@RequestMapping(value = "similarityInFileAndRepository", method = RequestMethod.POST)
	public void similarityInFileAndRepository(
			@RequestParam("filepath") String filepath,
			@RequestParam("processId") String processId,
			@RequestParam("algorithm") String algorithm, HttpSession session,
			HttpServletResponse response) {
		ModelSimilarityRepository msr = new ModelSimilarityRepository();
		JSONObject result = new JSONObject();
		try {
			float sim = msr.similarityInFileAndRepository(filepath, processId,
					algorithm);
			result.put("state", "SUCCESS");
			if (Float.isNaN(sim)) {
				result.put("similarity", "NaN");
			} else {
				BigDecimal bd = new BigDecimal(sim);
				bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
				result.put("similarity", bd.toString());
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
