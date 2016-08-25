package com.ibm.bpm.util;

import java.io.File;
import java.io.FilenameFilter;

public class FilenameFilterBpmn implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		// TODO Auto-generated method stub
		return name.toLowerCase().endsWith(".bpmn");
	}

}
