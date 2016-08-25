package cn.edu.thss.iise.bpmdemo.analysis.actions;

import javax.swing.JFrame;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import cn.edu.thss.iise.bpmdemo.analysis.core.fragment.FragmentUtil;
import cn.edu.thss.iise.bpmdemo.charts.MemoryUsageDemo1;
import cn.edu.thss.iise.bpmdemo.charts.MemoryUsageDemo1.DataGenerator;

public class FragmentizationAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub

		FragmentizationStep1Dialog dialog1 = new FragmentizationStep1Dialog(
				window.getShell());
		dialog1.create();

		if (dialog1.open() == Window.OK) {
			String inputModelFile = dialog1.getInputModelFile();
			String outputModelFile = dialog1.getOutputModelFile();
			dialog1.close();
			// ��ʼ������Ƭ������
			JFrame jframe = new JFrame("CPU Usage");
			MemoryUsageDemo1 memoryusagedemo = new MemoryUsageDemo1(30000);
			jframe.getContentPane().add(memoryusagedemo, "Center");
			jframe.setBounds(200, 120, 600, 280);
			jframe.setVisible(true);
			DataGenerator dg = (memoryusagedemo.new DataGenerator(100));
			dg.start();

			FragmentUtil.fragment(inputModelFile, outputModelFile);

			dg.stop();
			jframe.dispose();
			MessageBox mb = new MessageBox(window.getShell(), SWT.ICON_WARNING);
			mb.setText("��ʾ��");
			mb.setMessage("Ƭ�λ��ھ����");
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