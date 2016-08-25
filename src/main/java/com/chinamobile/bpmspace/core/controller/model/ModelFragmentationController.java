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
import com.chinamobile.bpmspace.core.repository.model.fragmentation.ModelFragmentationRepository;

@Controller
@RequestMapping("model")
public class ModelFragmentationController {

	@RequestMapping(value = "modelFragmentation", method = RequestMethod.POST)
	public void modelFragmentation(
			@RequestParam(value = "filepaths[]", required = false) String[] filepaths,
			@RequestParam(value = "processIds[]", required = false) String[] processIds,
			HttpSession session, HttpServletResponse response) {
		ModelFragmentationRepository mfr = new ModelFragmentationRepository();
		JSONObject result = new JSONObject();
		try {
			String frag = mfr.modelFragmentation(filepaths, processIds, session
					.getAttribute("userId").toString());
			result.put("state", "SUCCESS");
			result.put("frag", frag);
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
