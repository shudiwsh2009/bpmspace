package cn.edu.thss.iise.xiaohan.bpcd.mtree.index;

import java.util.ArrayList;
import java.util.List;

public class Stats {

	public static GraphMTree mtree;

	public GraphMTree createMTree(int minNodeCapacity) {
		mtree = new GraphMTree(minNodeCapacity);
		System.err.println("Creating M-Tree with minNodeCapacity="
				+ minNodeCapacity);

		System.out.println("CREATE-MTREE" + "\t" + "minNodeCapacity" + "="
				+ minNodeCapacity);
		System.err.println("M-Tree created");
		return mtree;
	}

	public List<Fragment> getNearest(Fragment querygraph) {
		GraphMTree.Query query = mtree.getNearest(querygraph);
		// List<GraphMTree.ResultItem> results = new
		// ArrayList<GraphMTree.ResultItem>();
		List<Fragment> fragments = new ArrayList<Fragment>();

		System.err.println("Query fragments...");
		int i = 0;
		for (GraphMTree.ResultItem ri : query) {
			// results.add(ri);
			fragments.add(ri.data);

			System.err.print("get fragments " + i + "\n");
			i++;
		}
		return fragments;
	}

	public List<Fragment> getNearest(Fragment querygraph, double d, int n) {
		GraphMTree.Query query = mtree.getNearest(querygraph, d, n);
		// List<GraphMTree.ResultItem> results = new
		// ArrayList<GraphMTree.ResultItem>();
		List<Fragment> fragments = new ArrayList<Fragment>();

		System.err.println("Query fragments...");
		int i = 0;
		for (GraphMTree.ResultItem ri : query) {
			// results.add(ri);
			fragments.add(ri.data);
			System.err.print("get fragments " + i + "\n");
			i++;
		}
		return fragments;
	}

	public void insertMTree(Fragment fragment) {
		mtree.add(fragment);

		System.out.print("Fragment added...  ");
		System.out.print(fragment.modelfiles.get(0) + '\n');
	}

	public void insertMTree(List<Fragment> fragments) {

		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			mtree.add(fragment);

			System.out.print("Fragments added...  ");
			System.out.print(fragment.modelfiles.get(0) + '\n');
		}
	}

	public void removeMTree(Fragment fragment) {
		System.err.println("Removing fragment...");

		mtree.remove(fragment);

		System.err.print("fragment removed...");
	}

	public void removeMTree(List<Fragment> fragments) {
		System.err.println("Removing fragments...");

		for (int i = 0; i < fragments.size(); i++) {
			Fragment graph = fragments.get(i);
			mtree.remove(graph);

			System.err.print("\r" + " fragments removed...");
		}
	}

}
