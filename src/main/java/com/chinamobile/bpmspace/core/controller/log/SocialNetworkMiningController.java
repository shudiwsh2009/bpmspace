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

import com.chinamobile.bpmspace.core.repository.socialnetworkmining.SocialNetworkMining;
import com.chinamobile.bpmspace.core.repository.socialnetworkmining.newSocialNetworkResults;

@Controller
@RequestMapping("socialnetworkmining")
public class SocialNetworkMiningController {
	@RequestMapping(value = "mining", method = RequestMethod.POST)
	public void cgenerate(@RequestParam("choice") int choice,
			@RequestParam("adjustment") double adjustment,
			@RequestParam("inputFilePath") String inputFilePath,
			HttpServletResponse response) {
		// output file ***.png
		String imagePath = inputFilePath.replaceAll("logs", "pngs");
		imagePath = imagePath.replaceAll(".mxml", ".png");

		// indexType
		int indexType = 0;
		switch (choice) {
		case 1:
			// Hand over of work
			indexType = 1101;
			break;
		case 2:
			// Working Together[Consider distance without causality(beta=0.5)]
			indexType = 2001;
			break;
		case 3:
			// Similar task[Similarity coefficient]
			indexType = 3002;
			break;
		default:
			indexType = 1101;
		}

		// mining
		SocialNetworkMining snm = new SocialNetworkMining(inputFilePath,
				indexType);
		newSocialNetworkResults snr = (newSocialNetworkResults) snm.mining();
		snr.adjustMatrix(adjustment);
		snr.export2png(imagePath);

		// png path
		String pngName = imagePath.substring(imagePath
				.lastIndexOf(File.separator) + 1);
		String temp = imagePath.substring(0,
				imagePath.lastIndexOf(File.separator));
		temp = temp.substring(0, temp.lastIndexOf(File.separator));
		String user = temp.substring(temp.lastIndexOf(File.separator) + 1);
		String resultPngPath = "snmfiles/" + user + "/pngs/" + pngName;

		JSONObject result = new JSONObject();
		result.put("state", "SUCCESS");
		result.put("png", resultPngPath);
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
