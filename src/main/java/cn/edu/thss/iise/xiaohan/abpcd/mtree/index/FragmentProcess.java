package cn.edu.thss.iise.xiaohan.abpcd.mtree.index;

import cn.edu.thss.iise.xiaohan.abpcd.graph.EPCHelper;
import cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc.EPC;
import de.bpt.hpi.graph.Graph;
import de.bpt.hpi.ogdf.rpst.ExpRPST;

public class FragmentProcess {

	public String filepath = null;
	public String filename = null;
	public Fragment fragment;

	public FragmentProcess() {

	}

	public FragmentProcess(String filepath) {
		this.filepath = filepath;
		this.filename = filepath.substring(filepath.lastIndexOf("\\") + 1);
		this.fragment = convertFragment(filepath);
	}

	public Fragment getFragment() {
		return this.fragment;
	}

	public void process(String filepath) {
		this.filepath = filepath;
		this.filename = filepath.substring(filepath.lastIndexOf("\\") + 1);
		this.fragment = convertFragment(filepath);
	}

	private Fragment convertFragment(String filepath) {

		EPC epcmodel = EPC.loadEPML(filepath);
		epcmodel.cleanEPC();

		EPCHelper epc = new EPCHelper(epcmodel, filepath);
		Graph graph = epc.getGraph();

		ExpRPST t = new ExpRPST(graph);
		Fragment f = new Fragment(t.getOriginalGraph(), filename);

		return f;
	}

}
