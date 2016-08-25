package com.chinamobile.bpmspace.core.controller.process;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinamobile.bpmspace.core.repository.miningalgorithmevaluator.MiningAlgorithmEvaluate;

@Controller
@RequestMapping("miningevaluator")
public class MiningAlgorithmEvaluateController {

	@RequestMapping(value = "evaluate", method = RequestMethod.POST)
	public void evaluate(
			@RequestParam("inputFilePath") String inputFilePath,
			@RequestParam("outputFileName") String outputFileName,
			@RequestParam("miningAlgorithm") String miningAlgorithm,
			@RequestParam("logGenerateAlgorithm") String logGenerateAlgorithm,
			@RequestParam("similarityAlgorithm") String similarityAlgorithm,
			@RequestParam("similarityStrAlgorithm") String similarityStrAlgorithm,
			@RequestParam("completeness") double completeness,
			HttpServletResponse response) {
		// output file ***.mxml
		String outputFilePath = outputFileName + ".mxml";

		MiningAlgorithmEvaluate mae = new MiningAlgorithmEvaluate(
				inputFilePath, outputFilePath, logGenerateAlgorithm,
				miningAlgorithm, similarityAlgorithm, similarityStrAlgorithm,
				completeness);
		mae.PreMiningEvaluate();
		mae.MiningEvaluate();
		ArrayList<Float> evaluationResult = mae.EvaluationResult();

		// [.xls] file path
		String name = outputFileName.substring(outputFileName
				.lastIndexOf(File.separator) + 1);
		String temp = outputFileName.substring(0,
				outputFileName.lastIndexOf(File.separator));
		temp = temp.substring(0, temp.lastIndexOf(File.separator));
		String user = temp.substring(temp.lastIndexOf(File.separator) + 1);

		JSONObject result = new JSONObject();
		result.put("state", "SUCCESS");
		result.put("averageSimilarity", evaluationResult.get(0));
		result.put("meanDeviation", evaluationResult.get(1));
		result.put("averageStrSimilarity", evaluationResult.get(2));
		result.put("meanStrDeviation", evaluationResult.get(3));
		result.put("averageSim", evaluationResult.get(4));
		result.put("file_folder", "amefiles");
		result.put("file_user", user);
		result.put("file_type", "result");
		result.put("file_name", name);
		result.put("file_suffix", "xls");
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
