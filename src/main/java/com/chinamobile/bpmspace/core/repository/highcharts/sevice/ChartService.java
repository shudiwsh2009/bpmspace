package com.chinamobile.bpmspace.core.repository.highcharts.sevice;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.chinamobile.bpmspace.core.domain.highcharts.bean.DataBean;
import com.chinamobile.bpmspace.core.domain.highcharts.bean.SeriesBean;
import com.chinamobile.bpmspace.core.domain.log.Activity;
import com.chinamobile.bpmspace.core.domain.log.Case;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.repository.InstanceRepository;

@Service
public class ChartService {
	public DataBean getInstanceChartData(String logId) throws ParseException,
			NoExistException, EmptyFieldException {
		InstanceRepository ir = new InstanceRepository();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		List<SeriesBean> list = new ArrayList<SeriesBean>();

		List<Integer> numEvents = new ArrayList<Integer>();
		List<String> xAxis = new ArrayList<String>();
		List<Integer> duration = new ArrayList<Integer>();
		List<Integer> durationAve = new ArrayList<Integer>();
		List<Case> instances = ir.getInstancesOfLog(logId);
		for (Case instance : instances) {
			Integer numOfActivity = instance.getActivities().size();
			long startTime = Long.MAX_VALUE, endTime = 0, totalTime = 0;
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

			int hours = (int) Math.ceil(((double) totalTime) / 1000 / 3600);

			xAxis.add(instance.getIdentifier());
			numEvents.add(numOfActivity);
			duration.add(hours);
			durationAve.add(hours / numOfActivity);
		}
		result.put("state", "SUCCESS");
		result.put("aaData", array);

		list.add(new SeriesBean("事件包含的活动数目(项)", "#9EF28E", numEvents));
		list.add(new SeriesBean("事件消耗的总时间（小时）", "#8CBAED", duration));
		list.add(new SeriesBean("事件消耗的平均时间（小时）", "#5E5E62", durationAve));

		return new DataBean("instanceChart", "这里针对日志实例的中活动数目、消耗时间进行统计",
				"实例包含活动(项)/消耗时间(小时)", "", xAxis, list);
	}

	public DataBean getActivityChartData(String logId) throws ParseException,
			NoExistException, EmptyFieldException {
		InstanceRepository ir = new InstanceRepository();
		List<SeriesBean> list = new ArrayList<SeriesBean>();

		List<String> xAxis = new ArrayList<String>();
		List<Integer> frequency = new ArrayList<Integer>();
		List<Integer> duration = new ArrayList<Integer>();
		List<Integer> durationAve = new ArrayList<Integer>();
		List<Case> instances = ir.getInstancesOfLog(logId);
		// 活动名称
		List<String> activityNames = new ArrayList<String>();
		// 活动名称 -> 发生频率
		Map<String, Integer> activityOccurrence = new HashMap<String, Integer>();
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
					activityDurationTime.put(activity.getName(), _end - _start);
				}
			}
		}
		// 返回统计
		for (String name : activityNames) {
			int hours = (int) Math
					.ceil((activityDurationTime.get(name)) / 1000 / 3600);
			// 活动名称
			xAxis.add(name);
			// 发生频率
			frequency.add(activityOccurrence.get(name));
			// 平均耗时
			durationAve.add(hours / activityOccurrence.get(name));
			// 总计耗时
			duration.add(hours);
		}

		list.add(new SeriesBean("活动在日志中的出现次数(次)", "#9EF28E", frequency));
		list.add(new SeriesBean("活动消耗的总时间（小时）", "#8CBAED", duration));
		list.add(new SeriesBean("活动消耗的平均时间（小时）", "#5E5E62", durationAve));

		return new DataBean("activityChart", "这里针对日志中活动发生频率、消耗时间进行统计",
				"活动频率(次)/消耗时间(小时)", "", xAxis, list);
	}

	public DataBean getResourceChartData(String logId) throws ParseException,
			NoExistException, EmptyFieldException {
		InstanceRepository ir = new InstanceRepository();
		List<SeriesBean> list = new ArrayList<SeriesBean>();

		List<String> xAxis = new ArrayList<String>();
		List<Integer> frequency = new ArrayList<Integer>();
		List<Integer> duration = new ArrayList<Integer>();
		List<Integer> durationAve = new ArrayList<Integer>();
		List<Case> instances = ir.getInstancesOfLog(logId);
		// 资源名称
		List<String> resourceNames = new ArrayList<String>();
		// 资源名称 -> 出现频率
		Map<String, Integer> resourceOccurrence = new HashMap<String, Integer>();
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
					resourceOccurrence.put(activity.getActor(),
							resourceOccurrence.get(activity.getActor()) + 1);
					resourceDurationTime.put(activity.getActor(),
							resourceDurationTime.get(activity.getActor())
									+ (_end - _start));
				} else {
					resourceNames.add(activity.getActor());
					resourceOccurrence.put(activity.getActor(), 1);
					resourceDurationTime
							.put(activity.getActor(), _end - _start);
				}
			}
		}
		// 返回统计
		for (String name : resourceNames) {
			int hours = (int) Math
					.ceil(resourceDurationTime.get(name) / 1000 / 3600);
			// 资源名称
			xAxis.add(name);
			// 出现频率
			frequency.add(resourceOccurrence.get(name));
			// 平均耗时
			durationAve.add(hours / resourceOccurrence.get(name));
			// 总计耗时
			duration.add(hours);
		}

		list.add(new SeriesBean("资源在日志中的出现次数(次)", "#9EF28E", frequency));
		list.add(new SeriesBean("资源工作的总时间（小时）", "#8CBAED", duration));
		list.add(new SeriesBean("资源工作的平均时间（小时）", "#5E5E62", durationAve));

		return new DataBean("resourceChart", "这里针对日志中资源出现次数、工作时间进行统计",
				"资源出现次数(次)/工作时间(小时)", "", xAxis, list);
	}
}
