package cn.edu.thss.iise.bpmdemo.repository.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jfree.ui.ApplicationFrame;

public class ModelRepoAction implements IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		ApplicationFrame jframe = new ApplicationFrame("Set Respository Path");/*
																				 * JPanel
																				 * panel
																				 * =
																				 * new
																				 * JPanel
																				 * (
																				 * )
																				 * ;
																				 * jframe
																				 * .
																				 * setContentPane
																				 * (
																				 * contentPane
																				 * )
																				 */

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
