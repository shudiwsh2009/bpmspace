package cn.edu.thss.iise.bpmdemo.analysis.actions;

import java.io.File;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jfree.ui.RefineryUtilities;

import cn.edu.thss.iise.bpmdemo.analysis.core.mining.MiningUtil;
import cn.edu.thss.iise.bpmdemo.charts.MeterChart;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class MiningAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public MiningAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {

		MiningStep1Dialog dialog1 = new MiningStep1Dialog(window.getShell());
		dialog1.create();

		if (dialog1.open() == Window.OK) {
			String logFile = dialog1.getLogFile();
			String modelFile = dialog1.getModelFile();
			dialog1.close();
			File inputFolder = new File(logFile);
			File[] inputFiles = inputFolder.listFiles();
			double i = 0;
			if (inputFiles == null || inputFiles.length == 0) {
				MessageBox mb = new MessageBox(window.getShell(),
						SWT.ICON_WARNING);
				mb.setText("��ʾ��");
				mb.setMessage("����·��������߲�����־�ļ�!");
			} else {
				MeterChart mc = new MeterChart("Log Mining Process",
						inputFiles.length);
				mc.pack();
				RefineryUtilities.centerFrameOnScreen(mc);
				mc.setVisible(true);
				for (File file : inputFiles) {
					try {
						int pos = file.getName().lastIndexOf(".");
						String fileName = file.getName().substring(0, pos);
						MiningUtil.fun(file.getAbsolutePath(), modelFile + "\\"
								+ fileName + ".bpmn");
						i++;
						mc.dataset.setValue(i);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			/*
			 * MiningStep2Dialog dialog2 = new
			 * MiningStep2Dialog(window.getShell(),logFile,modelFile);
			 * dialog2.open();
			 */
		}

	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}
