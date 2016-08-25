package com.chinamobile.bpmspace.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String convertDate(Date d) {
		DateFormat df = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分");
		return df.format(d);
	}

	public static void main(String[] args) {
		System.out.println(DateUtil.convertDate(new Date()));
	}
}
