package com.chinamobile.bpmspace.core.controller.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinamobile.bpmspace.core.domain.log.Activity;
import com.chinamobile.bpmspace.core.domain.log.Case;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.InstanceRepository;
import com.chinamobile.bpmspace.core.util.TimeUtil;

@Controller
@RequestMapping("instanceManagement")
public class CaseController {
	@RequestMapping(value = "instance_overview", method = RequestMethod.POST)
	@ResponseBody
	public void instanceAnalysis(@RequestParam("logId") String _logId,
			HttpServletResponse response) throws BasicException, ParseException {
		InstanceRepository ir = new InstanceRepository();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		// List<Instance> instances = ir.findInstanceByLogId(_logId);
		List<Case> instances = ir.getInstancesOfLog(_logId);

		int numEvents = 0;
		long startTime = 0L, endTime = 0L;

		long duration;
		int h, d;
		String durationString = null;

		for (Case i : instances) {
			numEvents = 0;
			for (Activity a : i.getActivities()) {
				numEvents++;
				if (numEvents == 1) {
					startTime = a.getStartTime();
				}
				endTime = a.getEndTime();
			}
			duration = (endTime - startTime) / 1000;

			d = (int) (duration / (24 * 60 * 60));
			if (d > 0) {
				h = (int) ((duration - d * 24 * 60 * 60) / (60 * 60));
				if (h > 0) {
					durationString = d + "天" + h + "小时";
					// System.out.println("耗时：" + d + "天，" + h + "小时。");
				}
			} else {
				h = (int) (duration / (60 * 60));
				durationString = d + "天" + h + "小时";
				// System.out.println("耗时：" + d + "天，" + h + "小时。");
			}

			JSONArray ja = new JSONArray();
			ja.put(i.getIdentifier());
			ja.put(numEvents);
			ja.put(TimeUtil.getCommonTime(startTime));
			ja.put(TimeUtil.getCommonTime(endTime));
			ja.put(durationString);

			array.put(ja);

		}
		result.put("aaData", array);

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

	@RequestMapping(value = "instanceDB_load", method = RequestMethod.GET)
	@ResponseBody
	public void getInstance(@RequestParam("logId") String _logId,
			HttpServletResponse response) throws BasicException {
		InstanceRepository ir = new InstanceRepository();

		// ModelRepository mr = new ModelRepository();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		List<Case> instances = ir.getInstancesOfLog(_logId);

		for (Case i : instances) {
			for (Activity a : i.getActivities()) {
				JSONArray ja = new JSONArray();

				ja.put(i.getIdentifier());
				ja.put("");
				ja.put(i.getOwnerId());
				ja.put(a.getName());
				ja.put(a.getActor());
				ja.put(TimeUtil.getCommonTime(a.getStartTime()));
				ja.put(TimeUtil.getCommonTime(a.getEndTime()));

				array.put(ja);
			}
		}
		result.put("aaData", array);

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
