package com.chinamobile.bpmspace.core.controller.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinamobile.bpmspace.core.domain.highcharts.bean.DataBean;
import com.chinamobile.bpmspace.core.domain.log.Activity;
import com.chinamobile.bpmspace.core.domain.log.Case;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.InstanceRepository;
import com.chinamobile.bpmspace.core.repository.highcharts.sevice.ChartService;

@Controller
@RequestMapping("statisticCharts")
public class VisualizationController {
	@RequestMapping(value = "statisticInstanceChart", method = RequestMethod.GET)
	@ResponseBody
	public DataBean showInstanceChart(
			@RequestParam(value = "logId", required = false) String _logId,
			HttpServletResponse response) throws BasicException, ParseException {
		ChartService chartService = new ChartService();
		return chartService.getInstanceChartData(_logId);
	}

	@RequestMapping(value = "statisticActivityChart", method = RequestMethod.GET)
	@ResponseBody
	public DataBean showActivityChart(
			@RequestParam(value = "logId", required = false) String _logId,
			HttpServletResponse response) throws BasicException, ParseException {
		ChartService chartService = new ChartService();
		return chartService.getActivityChartData(_logId);
	}

	@RequestMapping(value = "statisticResourceChart", method = RequestMethod.GET)
	@ResponseBody
	public DataBean showResourceChart(
			@RequestParam(value = "logId", required = false) String _logId,
			HttpServletResponse response) throws BasicException, ParseException {
		ChartService chartService = new ChartService();
		return chartService.getResourceChartData(_logId);
	}

	@RequestMapping(value = "statisticActivityPieChart", method = RequestMethod.GET)
	@ResponseBody
	public void activityPieChart(@RequestParam("logId") String _logId,
			HttpServletResponse response) {
		InstanceRepository ir = new InstanceRepository();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			List<Case> instances = ir.getInstancesOfLog(_logId);
			// 活动名称
			List<String> activityNames = new ArrayList<String>();
			// 活动名称 -> 发生频率
			Map<String, Integer> activityOccurrence = new HashMap<String, Integer>();
			// 所有活动频率最大值
			int totalOccurrence = 0;
			int aveOccurrence = 0;

			for (Case instance : instances) {
				for (Activity activity : instance.getActivities()) {
					if (activityNames.contains(activity.getName())) {
						activityOccurrence.put(activity.getName(),
								activityOccurrence.get(activity.getName()) + 1);
					} else {
						activityNames.add(activity.getName());
						activityOccurrence.put(activity.getName(), 1);
					}
				}
				++totalOccurrence;
			}

			aveOccurrence = totalOccurrence / activityNames.size();
			for (String name : activityNames) {
				JSONObject object = new JSONObject();
				if (activityOccurrence.get(name) > aveOccurrence) {
					// 活动名称
					object.put("name", name);
					object.put("frequency", activityOccurrence.get(name));
					array.put(object);
				}
			}
			result.put("title", "这里针对高于整体活动平均频率的高发生率活动进行统计");
			result.put("divId", "activityPieChart");
			result.put("data", array);
		} catch (BasicException e) {
			result.put("state", "SUCCESS");
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

	@RequestMapping(value = "statisticResourcePieChart", method = RequestMethod.GET)
	@ResponseBody
	public void resourcePieChart(@RequestParam("logId") String _logId,
			HttpServletResponse response) {
		InstanceRepository ir = new InstanceRepository();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			List<Case> instances = ir.getInstancesOfLog(_logId);
			// 资源名称
			List<String> resourceNames = new ArrayList<String>();
			// 资源名称 -> 出现频率
			Map<String, Integer> resourceOccurrence = new HashMap<String, Integer>();
			// 资源出现最大频率
			int maxOccurrence = 0;

			for (Case instance : instances) {
				for (Activity activity : instance.getActivities()) {
					if (resourceNames.contains(activity.getActor())) {
						resourceOccurrence
								.put(activity.getActor(), resourceOccurrence
										.get(activity.getActor()) + 1);
					} else {
						resourceNames.add(activity.getActor());
						resourceOccurrence.put(activity.getActor(), 1);
					}
					if (resourceOccurrence.get(activity.getActor()) > maxOccurrence) {
						maxOccurrence = resourceOccurrence.get(activity
								.getActor());
					}
				}
			}
			for (String name : resourceNames) {
				JSONObject object = new JSONObject();
				if (resourceOccurrence.get(name) > maxOccurrence * 0.3) {
					// 资源名称
					object.put("name", name);
					object.put("frequency", resourceOccurrence.get(name));
					array.put(object);
				}
			}
			result.put("title", "这里针对高于最大出现频率30%的活跃资源进行统计");
			result.put("divId", "resourcePieChart");
			result.put("data", array);
		} catch (BasicException e) {
			result.put("state", "SUCCESS");
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
