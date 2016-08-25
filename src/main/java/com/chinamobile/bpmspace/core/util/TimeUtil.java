package com.chinamobile.bpmspace.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	/**
	 * 转换为形如“XX天XX小时”形式的时长
	 * 
	 * @param _time
	 * @return
	 */
	public static String getDaysAndHours(long _time) {
		int hours = (int) Math.ceil(((double) _time) / 1000 / 3600);
		int days = hours / 24;
		int leftHours = hours % 24;
		StringBuilder sb = new StringBuilder();
		if (days != 0) {
			sb.append(days);
			sb.append("天");
		}
		sb.append(leftHours);
		sb.append("小时");
		return sb.toString();
	}

	/**
	 * 转换为形如“XX分钟”形式的时长
	 * 
	 * @param _time
	 * @return
	 */
	public static String getMinute(long _time) {
		int minute = (int) Math.ceil(((double) _time) / 1000 / 60);
		StringBuilder sb = new StringBuilder();
		sb.append(minute);
		sb.append("分钟");
		return sb.toString();
	}

	/**
	 * 转换为形如“XX小时XX分钟”形式的时长
	 * 
	 * @param _time
	 * @return
	 */
	public static String getHoursAndMinute(long _time) {
		int minute = (int) Math.ceil(((double) _time) / 1000 / 60);
		int hours = minute / 60;
		int leftMinute = minute % 60;
		StringBuilder sb = new StringBuilder();
		if (hours != 0) {
			sb.append(hours);
			sb.append("小时");
		}
		sb.append(leftMinute);
		sb.append("分钟");
		return sb.toString();
	}

	/**
	 * 转换为形如“yyyy-MM-dd HH:mm:ss”形式的时刻
	 * 
	 * @param _time
	 * @return
	 */
	public static String getCommonTime(long _time) {
		Date date = new Date(_time);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public static String getCurrentYMD() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
}
