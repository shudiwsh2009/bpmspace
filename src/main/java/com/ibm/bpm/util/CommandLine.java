package com.ibm.bpm.util;

import java.io.File;

import com.ibm.bpm.analyzer.GenerateNewick;

public class CommandLine {

	public static void main(String[] args) {
		try {
			String flag = args[0];
			if (flag.equals("compare")) {
				// compare process from a folder, generic a excel file
				CompareProcess.compare(new String[] { args[1] });
			} else if (flag.equals("generateNewick")) {
				// generate a newick tree from a excel file
				String excelPath = args[1];
				File file = new File(excelPath);
				File folder = file.getParentFile();
				GenerateNewick.generic(excelPath, folder.getPath()
						+ "/newick.tree");
			} else if (flag.equals("mergeProcess")) {
				// merge process from a folder
				MergeProcess.merge(new String[] { args[1] });
			} else if (flag.equals("extractFragment")) {
				// extract fragment from fragment config and process
				MergeFragment.extract(new String[] { args[1], args[2] });
			} else if (flag.equals("mergeFragment")) {
				// merge fragment from fragment config and fragments
				MergeFragment.mergeFragment(new String[] { args[1], args[2] });
			} else if (flag.equals("extractAndMergeFragment")) {
				// extract and merge fragment from fragment config and process
				MergeFragment
						.extractAndMerge(new String[] { args[1], args[2] });
			} else {
				System.out
						.println("Invalid parameter! Please input the correct parameters!");
			}
		} catch (Exception ex) {
			System.out.println("Operation failed, reason: " + ex.getMessage());
			System.out.println("Detail:");
			ex.printStackTrace();
		}
	}
}
