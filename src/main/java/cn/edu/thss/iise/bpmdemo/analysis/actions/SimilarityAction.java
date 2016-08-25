package cn.edu.thss.iise.bpmdemo.analysis.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class SimilarityAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		SimilarityStep1Dialog dialog1 = new SimilarityStep1Dialog(
				window.getShell());

		dialog1.create();

		if (dialog1.open() == Window.OK) {
			dialog1.close();
			SimilarityStep2Dialog dialog2 = new SimilarityStep2Dialog(
					window.getShell(), dialog1.getLeftList(),
					dialog1.getRightList());
			dialog2.open();
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
