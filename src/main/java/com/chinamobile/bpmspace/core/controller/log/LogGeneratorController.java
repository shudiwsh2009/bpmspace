package com.chinamobile.bpmspace.core.controller.log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinamobile.bpmspace.core.repository.loggenerator.LogGenerator;

@Controller
@RequestMapping("loggenerator")
public class LogGeneratorController {

	// customized log generate
	@RequestMapping(value = "cgenerate", method = RequestMethod.POST)
	public void cgenerate(@RequestParam("dS1") boolean dS1,
			@RequestParam("tS") boolean tS,
			@RequestParam("tarCompleteness") double tarCompleteness,
			@RequestParam("causalCompleteness") double causalCompleteness,
			@RequestParam("freqCompleteness") double freqCompleteness,
			@RequestParam("noiseTypeString") String noiseTypeString,
			@RequestParam("noiseFlag") int noiseFlag,
			@RequestParam("noiseDegree") double noiseDegree,
			@RequestParam("inputFilePath") String inputFilePath,
			@RequestParam("outputFileName") String outputFileName,
			HttpServletResponse response) {
		// output file ***.mxml
		String outputFilePath = outputFileName + ".mxml";

		int[] noiseType = new int[5];
		String[] param = noiseTypeString.split(",");
		for (int i = 0; i < 5; i++) {
			noiseType[i] = Integer.parseInt(param[i]);
		}
		LogGenerator lg = new LogGenerator(inputFilePath, outputFilePath, dS1,
				tS, tarCompleteness, causalCompleteness, freqCompleteness,
				noiseType, noiseFlag, noiseDegree);

		// generate log and export to a .mxml file
		lg.CustomizableLogGenerate();

		// file path
		String name = outputFileName.substring(outputFileName
				.lastIndexOf(File.separator) + 1);
		String temp = outputFileName.substring(0,
				outputFileName.lastIndexOf(File.separator));
		temp = temp.substring(0, temp.lastIndexOf(File.separator));
		String user = temp.substring(temp.lastIndexOf(File.separator) + 1);

		JSONObject result = new JSONObject();
		result.put("state", "SUCCESS");
		result.put("file_folder", "lgfiles");
		result.put("file_user", user);
		result.put("file_type", "logs");
		result.put("file_name", name);
		result.put("file_suffix", "mxml");
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

	// default log generate
	@RequestMapping(value = "dgenerate", method = RequestMethod.POST)
	public void dgenerate(@RequestParam("inputFilePath") String inputFilePath,
			@RequestParam("outputFileName") String outputFileName,
			HttpServletResponse response) {
		// output file ***.mxml
		String outputFilePath = outputFileName + ".mxml";

		LogGenerator lg = new LogGenerator(inputFilePath, outputFilePath);
		// generate log and export to a .mxml file
		lg.DefaultLogGenerate();

		JSONObject result = new JSONObject();
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
