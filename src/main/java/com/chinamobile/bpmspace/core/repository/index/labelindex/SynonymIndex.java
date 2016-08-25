package com.chinamobile.bpmspace.core.repository.index.labelindex;

import java.io.FileInputStream;

import org.apache.lucene.wordnet.SynonymMap;

import cn.edu.thss.iise.beehivez.server.parameter.GlobalParameter;

public class SynonymIndex {
	private static SynonymMap synMap = null;

	private SynonymIndex() {

	}

	public static SynonymMap getSynonymMap() {
		try {
			if (synMap == null) {
				synMap = new SynonymMap(new FileInputStream(
						GlobalParameter.wordnetPrologFilename));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return synMap;
	}

}
