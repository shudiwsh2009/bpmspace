package cn.edu.thss.iise.bpmdemo.analysis.actions;

import javax.swing.JFrame;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jfree.ui.RefineryUtilities;

import cn.edu.thss.iise.bpmdemo.analysis.core.cluster.ClusterUtil;
import figtree.application.FigTreePanel;

public class ClusteringAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		ClusteringStep1Dialog dialog1 = new ClusteringStep1Dialog(
				window.getShell());
		dialog1.create();

		if (dialog1.open() == Window.OK) {
			String inputFolder = dialog1.getInputModel1File();
			dialog1.close();
			JFrame appFrame = new JFrame("Clustering Result");

			FigTreePanel fp = ClusterUtil.Cluster(appFrame, inputFolder, 800,
					500);

			appFrame.add(fp);
			appFrame.pack();
			appFrame.setVisible(true);
			fp.treeViewer.showTree(0);
			RefineryUtilities.centerFrameOnScreen(appFrame);

			/*
			 * ClusteringStep2Dialog dialog2 = new
			 * ClusteringStep2Dialog(window.getShell(),inputFolder);
			 * dialog2.open();
			 */
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
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		this.window = window;
	}

}
