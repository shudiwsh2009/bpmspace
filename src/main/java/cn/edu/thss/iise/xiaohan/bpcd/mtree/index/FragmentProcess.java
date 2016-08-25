package cn.edu.thss.iise.xiaohan.bpcd.mtree.index;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import cn.edu.thss.iise.xiaohan.bpcd.graph.EPCHelper;
import cn.edu.thss.iise.xiaohan.bpcd.nl.tue.tm.is.epc.EPC;
import de.bpt.hpi.graph.Graph;

public class FragmentProcess {

	public String filepath = null;
	public String filename = null;
	public Fragment fragment = new Fragment();

	public FragmentProcess(String filepath) {
		this.filepath = filepath;
		this.filename = filepath.substring(filepath.lastIndexOf("\\") + 1);
		this.fragment = convertFragment(filepath);
	}

	public Fragment getFragment() {
		return this.fragment;
	}

	public Fragment convertFragment(String filepath) {

		try {
			EPC epcmodel = EPC.loadEPML(filepath);
			epcmodel.cleanEPC();

			EPCHelper epc = new EPCHelper(epcmodel, filepath);
			Graph graph = epc.getGraph();

			fragment.setGraph(graph);
			fragment.modelfiles.add(filename);

			return fragment;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fragment;
	}

}
