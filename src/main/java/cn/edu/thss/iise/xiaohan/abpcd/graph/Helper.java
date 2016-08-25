package cn.edu.thss.iise.xiaohan.abpcd.graph;

import java.util.Collection;
import java.util.Map;

import de.bpt.hpi.graph.Graph;

public interface Helper {

	Graph getGraph();

	public void serializeToFile(Collection<Integer> vertices, String code);

	String serialize(Collection<Integer> vertices, String hashcode, int index);

	String serializeInContext(Collection<Integer> vertices,
			Collection<Integer> context, String hashcode, int inde);

	boolean isGateway(Integer id);

	String getGraphName();

	// max combinations in one region
	public int getMaxCombinationsInRegion();

	public void setMaxCombinationsInRegion(int maxCombinationsInRegion);

	// total number of combinations
	public void addTotalNumberOfCombinations(int nr);

	public int getTotalNumberOfCombinations();

	// number of regions
	public void addTotalNrOfRegions();

	public int getTotalNrOfRegions();

	void serializeToFile(String string,
			Map<Integer, Collection<Integer>> idOriginalVerticesMap, String code);
}
