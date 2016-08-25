package cn.edu.thss.iise.bpmdemo.analysis.core.difference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//������
class Vertex {
	public String label;
	public VertexState state;// ����״̬

	public Vertex(String lab) {
		label = lab;
		state = VertexState.UNVISITED;
	}

	public VertexState getState() {
		return state;
	}

	public void setState(VertexState state) {
		this.state = state;
	}

	public String toString() {
		return label;
	}

}

// ����ͼ���ڽӾ���ʵ��
class topoGraph {

	private final int MAX_VERTS = 30;
	private Vertex vertexList[]; // ��Ŷ��������
	private int adjMat[][]; // �ڽӾ���
	private int nVerts; // ��ǰ�Ķ�����

	public topoGraph() {
		vertexList = new Vertex[MAX_VERTS];

		adjMat = new int[MAX_VERTS][MAX_VERTS];
		nVerts = 0;
		for (int y = 0; y < MAX_VERTS; y++)
			for (int x = 0; x < MAX_VERTS; x++)
				adjMat[x][y] = 0;

	}

	public void addVertex(Vertex v)// ��ͼ�����һ������
	{
		vertexList[nVerts++] = v;
	}

	// ��ͼ������һ����,��start��end
	public void addEdge(int start, int end) {
		adjMat[start][end] = 1;

	}

	/**
	 * ����v�������������ڽ��
	 * 
	 * @param v
	 * @return
	 */
	private Set<Vertex> getNeighbors(Vertex v) {
		Set<Vertex> vSet = new HashSet<Vertex>();
		int index = getIndex(v);
		if (index == -1)
			return null;
		for (int i = 0; i < nVerts; i++)
			if (adjMat[index][i] == 1)
				vSet.add(vertexList[i]);

		return vSet;
	}

	// ���ض�����vertexList�����е�����
	private int getIndex(Vertex v) {
		for (int i = 0; i < nVerts; i++)
			if (vertexList[i] == v)
				return i;
		return -1;
	}

	/**
	 * ȫ���ڵ���Ϊδ����
	 */
	private void allUnVisted() {
		Vertex v = null;
		int len = nVerts;
		for (int i = 0; i < len; i++) {
			v = vertexList[i];
			if (v.getState() != VertexState.UNVISITED) {
				v.setState(VertexState.UNVISITED);
			}
		}
	}

	private boolean containsVertex(Vertex v) {
		int index = getIndex(v);
		if (index != -1)
			return true;
		else
			return false;

	}

	private VertexState getState(Vertex v) {

		return v.getState();
	}

	private VertexState setState(Vertex v, VertexState state) {

		VertexState preState = v.getState();
		v.setState(state);
		return preState;
	}

	/**
	 * ������ȱ���һ������
	 * 
	 * @param
	 * @param graph
	 * @param v
	 * @param checkCycle
	 * @return
	 */
	public List<Vertex> dfs(Vertex v, boolean checkCycle) {
		allUnVisted();
		List<Vertex> vList = new ArrayList<Vertex>();
		dfsHandler(v, checkCycle, vList);
		return vList;
	}

	private void dfsHandler(Vertex v, boolean checkCycle, List<Vertex> vList) {
		Set<Vertex> neighbors = null;
		if (!containsVertex(v)) {
			throw new IllegalStateException("�����ڸö���");
		}
		setState(v, VertexState.PASSED);

		neighbors = getNeighbors(v);
		VertexState state = null;
		for (Vertex neighbor : neighbors) {
			state = getState(neighbor);
			if (state == VertexState.UNVISITED) {// δ������
				// System.out.println(neighbor+",");
				dfsHandler(neighbor, checkCycle, vList);
			} else if (state == VertexState.PASSED && checkCycle) {//

				throw new IllegalStateException("����һ����");
			}
		}
		setState(v, VertexState.VISITED);// ���ʽ�����Ϊ�ѷ���
		vList.add(v);
		// System.out.println("++"+v);

	}

	/**
	 * ͼ����������
	 * 
	 * @param
	 * @param graph
	 * @return
	 */
	public List<Vertex> topo() {
		List<Vertex> vList = new ArrayList<Vertex>();
		allUnVisted();
		for (int i = 0; i < nVerts; i++) {
			if (getState(vertexList[i]) == VertexState.UNVISITED) {
				try {
					dfsHandler(vertexList[i], true, vList);
				} catch (IllegalStateException e) {
					throw new IllegalStateException("ͼ��һ����");
				}
			}
		}
		Collections.reverse(vList);
		return vList;
	}
}
