package com.chinamobile.bpmspace.core.repository.index.petrinetindex.taskindex;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

public class SemicolonAnalyzer extends Analyzer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String,
	 * java.io.Reader)
	 */
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new SemicolonTokenizer(reader);
	}

	@Override
	public TokenStream reusableTokenStream(String fieldName, Reader reader)
			throws IOException {
		Tokenizer tokenizer = (Tokenizer) getPreviousTokenStream();
		if (tokenizer == null) {
			tokenizer = new SemicolonTokenizer(reader);
			setPreviousTokenStream(tokenizer);
		} else
			tokenizer.reset(reader);
		return tokenizer;
	}

}
