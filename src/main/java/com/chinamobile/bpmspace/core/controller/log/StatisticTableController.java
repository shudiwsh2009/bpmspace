package com.chinamobile.bpmspace.core.controller.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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

import com.chinamobile.bpmspace.core.domain.log.Activity;
import com.chinamobile.bpmspace.core.domain.log.Case;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.repository.InstanceRepository;
import com.chinamobile.bpmspace.core.util.TimeUtil;

@Controller
@RequestMapping("statisticTable")
public class StatisticTableController {

	@RequestMapping(value = "instanceStatistic", method = RequestMethod.GET)
	public void instanceStatistic(@RequestParam("logId") String _logId,
			HttpServletResponse response) {
		InstanceRepository ir = new InstanceRepository();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			List<Case> instances = ir.getInstancesOfLog(_logId);
			for (Case instance : instances) {
				JSONArray object = new JSONArray();
				int numOfActivity = instance.getActivities().size();
				long startTime = Long.MAX_VALUE, endTime = 0, totalTime = 0, midCostTime = 0;
				for (Activity a : instance.getActivities()) {
					long _start = a.getStartTime();
					long _end = a.getEndTime();
					if (_start > _end) {
						long tmp = _start;
						_start = _end;
						_end = tmp;
					}
					if (_start < startTime) {
						startTime = _start;
					}
					if (_end > endTime) {
						endTime = _end;
					}
					totalTime += (_end - _start);
				}
				if (numOfActivity != 0) {
					int midIndexOfActivity = (int) Math
							.ceil(numOfActivity / 2.0);
					Activity midActivity = instance.getActivities().get(
							midIndexOfActivity);
					long _start = midActivity.getStartTime();
					long _end = midActivity.getEndTime();
					long _midEnd = Math.max(_start, _end);
					midCostTime = _midEnd - startTime;
				}

				// 实例编号
				object.put(instance.getIdentifier());
				// 活动数目
				object.put(numOfActivity);
				// 开始时间
				object.put(TimeUtil.getCommonTime(startTime));
				// 结束时间
				object.put(TimeUtil.getCommonTime(endTime));
				// 平均耗时
				object.put(TimeUtil.getDaysAndHours(totalTime / numOfActivity));
				// 耗时中值
				object.put(TimeUtil.getDaysAndHours(midCostTime));
				// 总计耗时
				object.put(TimeUtil.getDaysAndHours(totalTime));
				array.put(object);
			}
			result.put("state", "SUCCESS");
			result.put("aaData", array);
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

	@RequestMapping(value = "activityStatistic", method = RequestMethod.GET)
	public void activityStatistic(@RequestParam("logId") String _logId,
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
			// 所有活动总频率
			int totalOccurrence = 0;
			// 活动名称 -> 耗时总计
			Map<String, Long> activityDurationTime = new HashMap<String, Long>();

			for (Case instance : instances) {
				for (Activity activity : instance.getActivities()) {
					long _start = activity.getStartTime();
					long _end = activity.getEndTime();
					if (_start > _end) {
						long tmp = _start;
						_start = _end;
						_end = tmp;
					}
					if (activityNames.contains(activity.getName())) {
						activityOccurrence.put(activity.getName(),
								activityOccurrence.get(activity.getName()) + 1);
						activityDurationTime.put(activity.getName(),
								activityDurationTime.get(activity.getName())
										+ (_end - _start));
					} else {
						activityNames.add(activity.getName());
						activityOccurrence.put(activity.getName(), 1);
						activityDurationTime.put(activity.getName(), _end
								- _start);
					}
					++totalOccurrence;
				}
			}
			// 返回统计
			for (String name : activityNames) {
				JSONArray object = new JSONArray();
				// 活动名称
				object.put(name);
				// 发生频率
				object.put(activityOccurrence.get(name));
				// 相对频率
				BigDecimal relativeOccurrence = new BigDecimal(
						(double) activityOccurrence.get(name)
								/ (double) totalOccurrence * 100);
				relativeOccurrence = relativeOccurrence.setScale(2,
						BigDecimal.ROUND_HALF_UP);
				object.put(relativeOccurrence.toString() + "%");
				// 平均耗时
				long averageDurationTime = activityDurationTime.get(name)
						/ activityOccurrence.get(name);
				object.put(TimeUtil.getDaysAndHours(averageDurationTime));
				// 总计耗时
				object.put(TimeUtil.getDaysAndHours(activityDurationTime
						.get(name)));
				array.put(object);
			}

			result.put("state", "SUCCESS");
			result.put("aaData", array);
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

	@RequestMapping(value = "resourceStatistic", method = RequestMethod.GET)
	public void resourceStatistic(@RequestParam("logId") String _logId,
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
			// 所有资源总频率
			int totalOccurrence = 0;
			// 资源名称 -> 耗时总计
			Map<String, Long> resourceDurationTime = new HashMap<String, Long>();

			for (Case instance : instances) {
				for (Activity activity : instance.getActivities()) {
					long _start = activity.getStartTime();
					long _end = activity.getEndTime();
					if (_start > _end) {
						long tmp = _start;
						_start = _end;
						_end = tmp;
					}
					if (resourceNames.contains(activity.getActor())) {
						resourceOccurrence
								.put(activity.getActor(), resourceOccurrence
										.get(activity.getActor()) + 1);
						resourceDurationTime.put(activity.getActor(),
								resourceDurationTime.get(activity.getActor())
										+ (_end - _start));
					} else {
						resourceNames.add(activity.getActor());
						resourceOccurrence.put(activity.getActor(), 1);
						resourceDurationTime.put(activity.getActor(), _end
								- _start);
					}
					++totalOccurrence;
				}
			}
			// 返回统计
			for (String name : resourceNames) {
				JSONArray object = new JSONArray();
				// 资源名称
				object.put(name);
				// 出现频率
				object.put(resourceOccurrence.get(name));
				// 相对频率
				BigDecimal relativeOccurrence = new BigDecimal(
						(double) resourceOccurrence.get(name)
								/ (double) totalOccurrence * 100);
				relativeOccurrence = relativeOccurrence.setScale(2,
						BigDecimal.ROUND_HALF_UP);
				object.put(relativeOccurrence.toString() + "%");
				// 平均耗时
				long averageDurationTime = resourceDurationTime.get(name)
						/ resourceOccurrence.get(name);
				object.put(TimeUtil.getDaysAndHours(averageDurationTime));
				// 总计耗时
				object.put(TimeUtil.getDaysAndHours(resourceDurationTime
						.get(name)));
				array.put(object);
			}

			result.put("state", "SUCCESS");
			result.put("aaData", array);
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

	@RequestMapping(value = "variantsStatistic", method = RequestMethod.GET)
	public void variantsStatistic(@RequestParam("caseId") String _caseId,
			HttpServletResponse response) {
		InstanceRepository ir = new InstanceRepository();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		Case instance = ir.getCaseById(_caseId);
		long _temp = instance.getActivities().get(0).getStartTime();
		for (Activity activity : instance.getActivities()) {
			JSONArray object = new JSONArray();
			long _start = activity.getStartTime();
			long _end = activity.getEndTime();

			if (_start > _end) {
				long tmp = _start;
				_start = _end;
				_end = tmp;
			}

			object.put(activity.getName());
			object.put(activity.getActor());
			object.put(TimeUtil.getCommonTime(activity.getStartTime()));
			object.put(TimeUtil.getCommonTime(activity.getEndTime()));
			object.put(TimeUtil.getHoursAndMinute(_end - _start));

			if (_temp > _start) {
				long tmp = _temp;
				_temp = _start;
				_start = tmp;
			}

			object.put(TimeUtil.getMinute(_start - _temp));

			array.put(object);
			_temp = _end;
		}
		result.put("state", "SUCCESS");
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

	@RequestMapping(value = "variantsCatalog", method = RequestMethod.GET)
	public void variantsCatalog(@RequestParam("logId") String _logId,
			HttpServletResponse response) {
		InstanceRepository ir = new InstanceRepository();
		JSONArray result = new JSONArray();

		try {
			List<Case> instances = ir.getInstancesOfLog(_logId);
			// 分类名称
			List<Integer> variantNames = new ArrayList<Integer>();
			// 分类名称 -> 实例id列表
			Map<Integer, ArrayList<String>> variantsList = new HashMap<Integer, ArrayList<String>>();
			// 实例id -> 实例名称
			Map<String, String> caseNameList = new HashMap<String, String>();

			for (Case instance : instances) {
				int numOfActivity = instance.getActivities().size();

				caseNameList.put(instance.getId(), instance.getIdentifier());

				if (variantsList.containsKey(numOfActivity)) {
					variantsList.get(numOfActivity).add(instance.getId());
				} else {
					variantsList.put(numOfActivity, new ArrayList<String>());
					variantsList.get(numOfActivity).add(instance.getId());
					variantNames.add(numOfActivity);
				}
			}
			// 返回统计

			Collections.sort(variantNames);

			for (Integer variant : variantNames) {
				JSONObject o = new JSONObject();
				JSONArray childArray = new JSONArray();
				o.put("title", "包含有" + variant + "个活动的实例");
				o.put("isFolder", true);
				for (String cases : variantsList.get(variant)) {
					JSONObject object = new JSONObject();
					object.put("title", caseNameList.get(cases));
					object.put("key", cases);
					childArray.put(object);
				}
				o.put("children", childArray);
				result.put(o);
			}

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
