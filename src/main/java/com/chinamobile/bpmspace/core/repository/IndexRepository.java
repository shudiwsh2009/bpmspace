package com.chinamobile.bpmspace.core.repository;

import java.util.List;

import org.json.JSONArray;

import com.chinamobile.bpmspace.core.data.IndexMongoAccess;
import com.chinamobile.bpmspace.core.domain.index.IndexCategory;
import com.chinamobile.bpmspace.core.domain.index.IndexDescriptor;
import com.chinamobile.bpmspace.core.domain.index.IndexForType;
import com.chinamobile.bpmspace.core.domain.index.IndexState;
import com.chinamobile.bpmspace.core.domain.log.Case;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.repository.index.Index;
import com.chinamobile.bpmspace.core.repository.index.ModelIndex;
import com.chinamobile.bpmspace.core.util.ClassLoadUtil;
import com.chinamobile.bpmspace.core.util.FileUtil;

public class IndexRepository {

	/**
	 * 1.check:same class 2.save the info to db
	 * 
	 * @param className
	 * @param desc
	 */
	public String register(String cat, String className, String desc)
			throws BasicException {
		String type = "";
		String supportedQueryType = "";
		IndexCategory indexCategory = mapStringtoIndexCategory(cat);
		IndexMongoAccess ima = new IndexMongoAccess();

		// 1.check if same name class exists
		List<IndexDescriptor> list = ima
				.getIndexDescriptorByClassName(className);
		if (list != null && !list.isEmpty()) {
			throw new BasicException(
					"There exists index with same class name, cannot register again!");
		}

		// 2.check if the class exits and check if the class is the
		// implementation of specified interface
		// 3.get index type
		// String jarPath = FileUtil.WEBAPP_ROOT + "WEB-INF\\lib\\index1.jar";
		String jarPath = "D:\\index1.jar";
		Index index = (Index) ClassLoadUtil.loadIndexInstance(jarPath,
				className);
		type = index.getType().toString();
		if (index.supportGraphQuery()) {
			supportedQueryType = "graph";

		} else if (index.supportTextQuery()) {
			supportedQueryType = "text";
		}

		// 4.update database
		IndexDescriptor idesc = new IndexDescriptor();
		idesc.setClass_name(className);
		idesc.setCategory(indexCategory);
		idesc.setDescription(desc);
		idesc.setState(IndexState.STOP);
		idesc.setType(type);
		idesc.setSupportedQueryType(supportedQueryType);
		ima.addIndexDescriptor(idesc);

		return idesc.getId();
	}

	/**
	 * construct an index specified in java class named @param class_name
	 * 
	 * @param class_name
	 */
	public void start(String index_id) throws BasicException {
		IndexMongoAccess ima = new IndexMongoAccess();
		IndexDescriptor indexDesc = ima.getIndexDescriptorByID(index_id);
		String className = indexDesc.getClass_name();

		// 1.construct the index
		String jarPath = FileUtil.WEBAPP_ROOT + "WEB-INF\\lib\\index1.jar";
		Index index = (Index) ClassLoadUtil.loadIndexInstance(jarPath,
				className);
		// index.open();
		// index.create();
		// index.close();

		// 2.change state
		ima.updateIndexDescriptorSatete(index_id, IndexState.START);
	}

	/**
	 * set the state as STOP
	 * 
	 * @param index_id
	 */
	public void stop(String index_id) throws BasicException {
		IndexMongoAccess ima = new IndexMongoAccess();
		IndexDescriptor indexDesc = ima.getIndexDescriptorByID(index_id);
		String className = indexDesc.getClass_name();

		// 1.call destroy function first
		String jarPath = FileUtil.WEBAPP_ROOT + "WEB-INF\\lib\\index1.jar";
		Index index = (Index) ClassLoadUtil.loadIndexInstance(jarPath,
				className);
		index.destroy();

		// 2.change state
		ima.updateIndexDescriptorSatete(index_id, IndexState.STOP);
	}

	/**
	 * return index descriptor list of category @param cat
	 * 
	 * @param cat
	 * @return
	 * @throws NoExistException
	 * @throws EmptyFieldException
	 */
	public String getIndexList(String cat) throws NoExistException,
			EmptyFieldException {
		IndexMongoAccess ima = new IndexMongoAccess();
		IndexCategory indexCategory = mapStringtoIndexCategory(cat);
		List<IndexDescriptor> indexDescriptorList = ima
				.getIndexDescriptorList(indexCategory);
		if (indexDescriptorList == null) {
			throw new NoExistException("The index list doesn't exist!");
		} else {
			JSONArray ja = new JSONArray(indexDescriptorList);
			// ja.put(indexDescriptorList);
			for (int i = 0; i < ja.length(); i++) {
				ja.getJSONObject(i).put("state",
						indexDescriptorList.get(i).getState().toString());
				ja.getJSONObject(i).put("category",
						indexDescriptorList.get(i).getCategory().toString());
				String cn = indexDescriptorList.get(i).getClass_name();
				ja.getJSONObject(i).put("class_name",
						cn.substring(cn.lastIndexOf('.') + 1));
			}

			return ja.toString();
		}
	}

	/**
	 * add a process to its category
	 * 
	 * @param process
	 */
	public void addToModelIndex(Model model, ProcessType type)
			throws BasicException {
		// 1.get index list which must be updated
		IndexForType it = getIndexForType(type);
		IndexMongoAccess ima = new IndexMongoAccess();
		if (it == IndexForType.UNKNOWN) {
			throw new BasicException("Unknow type for index!");
		}
		List<IndexDescriptor> iList = ima.getStartedIndexDescriptorByType(it
				.name());
		if (iList == null) {
			throw new BasicException("Fail to access index list!");
		}
		// 2.update index list one by one
		for (IndexDescriptor i : iList) {
			String jarPath = FileUtil.WEBAPP_ROOT + "WEB-INF\\lib\\index1.jar";
			ModelIndex index = (ModelIndex) ClassLoadUtil.loadIndexInstance(
					jarPath, i.getClass_name());
			index.open();
			index.addProcessModel(model);
			index.close();
		}

	}

	/**
	 * remove the process from according index
	 * 
	 * @param process
	 */
	public void removeFromModelIndex(Model model, ProcessType type)
			throws BasicException {
		// 1.get index list which must be updated
		IndexForType it = getIndexForType(type);
		IndexMongoAccess ima = new IndexMongoAccess();
		if (it == IndexForType.UNKNOWN) {
			throw new BasicException("Unknow type for index!");
		}
		List<IndexDescriptor> iList = ima.getStartedIndexDescriptorByType(it
				.name());
		if (iList == null) {
			throw new BasicException("Fail to access index list!");
		}
		// 2.update index list one by one
		// 2.update index list one by one
		for (IndexDescriptor i : iList) {
			String jarPath = FileUtil.WEBAPP_ROOT + "WEB-INF\\lib\\index1.jar";
			ModelIndex index = (ModelIndex) ClassLoadUtil.loadIndexInstance(
					jarPath, i.getClass_name());
			index.open();
			index.delProcessModel(model);
			index.close();
		}
	}

	/**
	 * add a case to its category
	 * 
	 * @param ins
	 */
	public void addToCaseIndex(Case ins) {

	}

	/**
	 * remove the case from according index
	 * 
	 * @param process
	 */
	public void removeFromCaseIndex(Case ins) {

	}

	/**
	 * utility function
	 * 
	 * @param cat
	 * @return
	 */
	private IndexCategory mapStringtoIndexCategory(String cat) {
		if ("m".equals(cat)) {
			return IndexCategory.MODEL;
		} else if ("i".equals(cat)) {
			return IndexCategory.INSTANCE;
		}
		return IndexCategory.UNKNOW;
	}

	private IndexForType getIndexForType(ProcessType type) {
		if (type == ProcessType.BPMN) {
			return IndexForType.BPMN;
		} else if (type == ProcessType.EPC) {
			return IndexForType.EPC;
		} else if (type == ProcessType.PETRINET) {
			return IndexForType.PETRINET;
		}
		return IndexForType.UNKNOWN;
	}
}
