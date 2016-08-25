package com.chinamobile.bpmspace.core.controller.process;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinamobile.bpmspace.core.repository.processmining.ProcessMining;

@Controller
@RequestMapping("processmining")
public class ProcessMiningController {

	@RequestMapping(value = "mining", method = RequestMethod.POST)
	public void processMining(@RequestParam("algorithm") String algorithm,
			@RequestParam("inputFilePath") String inputFilePath,
			@RequestParam("outputFileName") String outputFileName,
			HttpServletResponse response) {
		// output file ***.pnml
		String outputFilePath = outputFileName + ".pnml";

		ProcessMining pm = new ProcessMining(inputFilePath, outputFilePath,
				outputFileName, algorithm);
		pm.DoProcessMining();
		// export petrinet to .png image
		pm.export2png();

		// export petrinet to .pnml file
		pm.export2pnml();

		// png path
		String name = outputFileName.substring(outputFileName
				.lastIndexOf(File.separator) + 1);
		String temp = outputFileName.substring(0,
				outputFileName.lastIndexOf(File.separator));
		temp = temp.substring(0, temp.lastIndexOf(File.separator));
		String user = temp.substring(temp.lastIndexOf(File.separator) + 1);
		String resultPngPath = "pmfiles/" + user + "/pngs/" + name + ".png";

		JSONObject result = new JSONObject();
		result.put("state", "SUCCESS");
		result.put("png", resultPngPath);
		result.put("file_folder", "pmfiles");
		result.put("file_user", user);
		result.put("file_type", "models");
		result.put("file_name", name);
		result.put("file_suffix", "pnml");
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
