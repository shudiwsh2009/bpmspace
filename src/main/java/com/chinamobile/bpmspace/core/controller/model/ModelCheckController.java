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
import com.chinamobile.bpmspace.core.repository.model.check.ModelCheckRepository;

@Controller
@RequestMapping("model")
public class ModelCheckController {

	@RequestMapping(value = "checkInRepository", method = RequestMethod.POST)
	public void checkInRepository(@RequestParam("processId") String processId,
			HttpSession session, HttpServletResponse response) {
		ModelCheckRepository mcr = new ModelCheckRepository();
		JSONArray result = new JSONArray();
		try {
			List<List<String>> checkResult = mcr.checkInRepository(processId);
			List<String> places = checkResult.get(0);
			List<String> transitions = checkResult.get(1);
			// List<String> diagnosis = checkResult.get(2);
			int key = 0;
			JSONObject woflan = new JSONObject();
			woflan.put("title", "Woflan");
			woflan.put("isFolder", true);
			woflan.put("key", (++key) + "");
			JSONArray woflanChildren = new JSONArray();

			JSONObject netProperties = new JSONObject();
			netProperties.put("title", "Net Properties");
			netProperties.put("isFolder", true);
			netProperties.put("key", (++key) + "");
			JSONArray netPropertiesChildren = new JSONArray();

			JSONObject netPlaces = new JSONObject();
			netPlaces.put("title", "Places [" + places.size() + "]");
			netPlaces.put("isFolder", true);
			netPlaces.put("key", (++key) + "");
			JSONArray netPlacesChildren = new JSONArray();
			for (String place : places) {
				JSONObject o = new JSONObject();
				o.put("title", place);
				o.put("isFolder", false);
				o.put("key", (++key) + "");
				netPlacesChildren.put(o);
			}
			netPlaces.put("children", netPlacesChildren);

			JSONObject netTransitions = new JSONObject();
			netTransitions.put("title", "Transitions [" + transitions.size()
					+ "]");
			netTransitions.put("isFolder", true);
			netTransitions.put("key", (++key) + "");
			JSONArray netTransitionsChildren = new JSONArray();
			for (String transition : transitions) {
				JSONObject o = new JSONObject();
				o.put("title", transition);
				o.put("isFolder", false);
				o.put("key", (++key) + "");
				netTransitionsChildren.put(o);
			}
			netTransitions.put("children", netTransitionsChildren);

			netPropertiesChildren.put(netPlaces);
			netPropertiesChildren.put(netTransitions);
			netProperties.put("children", netPropertiesChildren);

			// JSONObject netDiagnosis = new JSONObject();
			// netDiagnosis.put("title", "Net Diagnosis");
			// netDiagnosis.put("isFolder", true);
			// netDiagnosis.put("key", (++key) + "");
			// JSONArray netDiagnosisChildren = new JSONArray();
			// for(String dg : diagnosis) {
			// JSONObject o = new JSONObject();
			// o.put("title", dg);
			// o.put("isFolder", false);
			// o.put("key", (++key) + "");
			// netDiagnosisChildren.put(o);
			// }
			// netDiagnosis.put("children", netDiagnosis);

			woflanChildren.put(netProperties);
			// woflanChildren.put(netDiagnosis);
			woflan.put("children", woflanChildren);
			result.put(woflan);
		} catch (BasicException e) {

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

	@RequestMapping(value = "checkInFile", method = RequestMethod.POST)
	public void checkInFile(@RequestParam("filepath") String filepath,
			HttpSession session, HttpServletResponse response) {
		ModelCheckRepository mcr = new ModelCheckRepository();
		JSONArray result = new JSONArray();
		try {
			List<List<String>> checkResult = mcr.checkInFile(filepath);
			List<String> places = checkResult.get(0);
			List<String> transitions = checkResult.get(1);
			// List<String> diagnosis = checkResult.get(2);
			int key = 0;
			JSONObject woflan = new JSONObject();
			woflan.put("title", "Woflan");
			woflan.put("isFolder", true);
			woflan.put("key", (++key) + "");
			JSONArray woflanChildren = new JSONArray();

			JSONObject netProperties = new JSONObject();
			netProperties.put("title", "Net Properties");
			netProperties.put("isFolder", true);
			netProperties.put("key", (++key) + "");
			JSONArray netPropertiesChildren = new JSONArray();

			JSONObject netPlaces = new JSONObject();
			netPlaces.put("title", "Places [" + places.size() + "]");
			netPlaces.put("isFolder", true);
			netPlaces.put("key", (++key) + "");
			JSONArray netPlacesChildren = new JSONArray();
			for (String place : places) {
				JSONObject o = new JSONObject();
				o.put("title", place);
				o.put("isFolder", false);
				o.put("key", (++key) + "");
				netPlacesChildren.put(o);
			}
			netPlaces.put("children", netPlacesChildren);

			JSONObject netTransitions = new JSONObject();
			netTransitions.put("title", "Transitions [" + transitions.size()
					+ "]");
			netTransitions.put("isFolder", true);
			netTransitions.put("key", (++key) + "");
			JSONArray netTransitionsChildren = new JSONArray();
			for (String transition : transitions) {
				JSONObject o = new JSONObject();
				o.put("title", transition);
				o.put("isFolder", false);
				o.put("key", (++key) + "");
				netTransitionsChildren.put(o);
			}
			netTransitions.put("children", netTransitionsChildren);

			netPropertiesChildren.put(netPlaces);
			netPropertiesChildren.put(netTransitions);
			netProperties.put("children", netPropertiesChildren);

			// JSONObject netDiagnosis = new JSONObject();
			// netDiagnosis.put("title", "Net Diagnosis");
			// netDiagnosis.put("isFolder", true);
			// netDiagnosis.put("key", (++key) + "");
			// JSONArray netDiagnosisChildren = new JSONArray();
			// for(String dg : diagnosis) {
			// JSONObject o = new JSONObject();
			// o.put("title", dg);
			// o.put("isFolder", false);
			// o.put("key", (++key) + "");
			// netDiagnosisChildren.put(o);
			// }
			// netDiagnosis.put("children", netDiagnosis);

			woflanChildren.put(netProperties);
			// woflanChildren.put(netDiagnosis);
			woflan.put("children", woflanChildren);
			result.put(woflan);
		} catch (BasicException e) {

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
