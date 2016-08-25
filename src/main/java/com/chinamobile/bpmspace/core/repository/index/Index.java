package com.chinamobile.bpmspace.core.repository.index;

import com.chinamobile.bpmspace.core.domain.index.IndexForType;
import com.chinamobile.bpmspace.core.exception.BasicException;

public interface Index {
	// get type, for index maintain
	public IndexForType getType();

	// construct and destroy
	public boolean open() throws BasicException;

	public void close() throws BasicException;

	public boolean create();

	public boolean destroy();

	public boolean supportSimilarQuery();

	public boolean supportSimilarLabel();

	public boolean supportTextQuery();

	public boolean supportGraphQuery();

}
