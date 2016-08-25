package com.chinamobile.bpmspace.core.repository.model.convertion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public class ConvertionUtil {
	public static HashSet<String> findStarts(HashSet<String> allids,Hashtable<String, ArrayList<String>> connections){
		HashSet<String> hasPreset = new HashSet<String>(); 
		for(String id : allids){
			if(connections.get(id)!=null){
				hasPreset.addAll(connections.get(id));
			}
		}
		HashSet<String> allstarts = allids;
		allstarts.removeAll(hasPreset);
		return allstarts;
	}
	
	public int a = 0;
	
	public static void main(String [] a1 ){
		int [] a =new int[1];
		System.out.println(a[0]);
	}
}
