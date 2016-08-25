package cn.edu.thss.iise.bpmdemo.statistics.actions;

import java.io.IOException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jfree.ui.RefineryUtilities;

import cn.edu.thss.iise.bpmdemo.editor.fragment.NormalFragmentNodeFactory;

public class ModelReuseAction implements IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		ModelReusePanel demo;
		try {
			demo = new ModelReusePanel("Fragment Reuse Analysis",
					NormalFragmentNodeFactory.fragmentStatisticsFile);
			demo.pack();
			RefineryUtilities.centerFrameOnScreen(demo);
			demo.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub

	}

}
