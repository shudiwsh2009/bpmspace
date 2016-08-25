package cn.edu.thss.iise.bpmdemo.analysis.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class MergingAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		MergingStep1Dialog dialog1 = new MergingStep1Dialog(window.getShell());
		dialog1.create();
		if (dialog1.open() == Window.OK) {
			// ��ʼ���д���
			dialog1.close();

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
