package com.chinamobile.bpmspace.core.controller.index;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinamobile.bpmspace.core.repository.index.test.model.ModelIndexConstructionTest;

@Controller
@RequestMapping("indextest")
public class IndexTestController {

	@RequestMapping(value = "modelIndexConstruction", method = RequestMethod.POST)
	public void testModelIndexConstruction(@RequestParam("n") long modelNumber,
			@RequestParam("minT") int minTransitionsPerNet,
			@RequestParam("maxT") int maxTransitionsPerNet,
			@RequestParam("maxName") int maxTransitionNameLength,
			HttpSession session, HttpServletResponse response) {
		JSONObject result = new JSONObject();

		String _catalogId = "537052af6670107c3fb57094";
		String _ownerId = "537052af6670107c3fb57093";
		try {
			ModelIndexConstructionTest mict = new ModelIndexConstructionTest(
					_catalogId, _ownerId);
			mict.TestModelIndexConstruction(modelNumber, minTransitionsPerNet,
					maxTransitionsPerNet, maxTransitionNameLength);
			result.put("state", "Start Test");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("state", "Fail to Start Test");
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

	@RequestMapping(value = "modelIndexQuery", method = RequestMethod.POST)
	public void testModelIndexQuery(@RequestParam("n") long queryNumber,
			HttpSession session, HttpServletResponse response) {
		JSONObject result = new JSONObject();

		try {
			ModelIndexConstructionTest mict = new ModelIndexConstructionTest();
			mict.TestModelIndexQuery(queryNumber);
			result.put("state", "Start Test");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("state", "Fail to Start Test");
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
