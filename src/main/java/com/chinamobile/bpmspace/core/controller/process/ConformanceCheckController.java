package com.chinamobile.bpmspace.core.controller.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinamobile.bpmspace.core.repository.conformancechecker.ConformanceChecker;

@Controller
@RequestMapping("conformancecheck")
public class ConformanceCheckController {

	@RequestMapping(value = "ccheck", method = RequestMethod.POST)
	public void ccheck(@RequestParam("inputFilePath") String inputFilePath,
			@RequestParam("algorithm") String algorithm,
			@RequestParam("fitness") boolean fitness,
			@RequestParam("f") boolean f, @RequestParam("pSE") boolean pSE,
			@RequestParam("pPC") boolean pPC,
			@RequestParam("precision") boolean precision,
			@RequestParam("saB") boolean saB, @RequestParam("aaB") boolean aaB,
			@RequestParam("structure") boolean structure,
			@RequestParam("saS") boolean saS, @RequestParam("aaS") boolean aaS,
			HttpServletResponse response) {
		// do the mining
		ConformanceChecker cc = new ConformanceChecker(inputFilePath,
				algorithm, fitness, f, pSE, pPC, precision, saB, aaB,
				structure, saS, aaS);
		cc.Mining();
		cc.Analysis();

		// get result
		ArrayList<Float> checkingResult = cc.getResults();

		JSONObject result = new JSONObject();
		result.put("state", "SUCCESS");
		result.put("f_fitness", checkingResult.get(0));
		result.put("p_sBA", checkingResult.get(1));
		result.put("p_aBA", checkingResult.get(2));
		result.put("p_dMF", checkingResult.get(3));
		result.put("s_sSA", checkingResult.get(4));
		result.put("s_aSA", checkingResult.get(5));

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
