package cn.edu.thss.iise.bpmdemo.analysis.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jfree.ui.RefineryUtilities;

import cn.edu.thss.iise.bpmdemo.analysis.core.difference.DifferenceUtil;
import cn.edu.thss.iise.bpmdemo.charts.MemoryUsageDemo1;
import cn.edu.thss.iise.bpmdemo.charts.MemoryUsageDemo1.DataGenerator;

public class DifferenceAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		DifferenceStep1Dialog dialog1 = new DifferenceStep1Dialog(
				window.getShell());
		dialog1.create();
		if (dialog1.open() == Window.OK) {
			String inputModelFile1 = dialog1.getInputModel1File();
			String inputModelFile2 = dialog1.getInputModel2File();
			dialog1.close();
			JFrame jframe = new JFrame("CPU Usage");
			MemoryUsageDemo1 memoryusagedemo = new MemoryUsageDemo1(30000);
			jframe.getContentPane().add(memoryusagedemo, "Center");
			jframe.setBounds(200, 120, 600, 280);
			jframe.setVisible(true);
			DataGenerator dg = (memoryusagedemo.new DataGenerator(100));
			dg.start();
			// call merge function
			// ��ȡ ���컯�������
			List<String> differenceList = new ArrayList<String>();
			try {
				differenceList = DifferenceUtil.difference(inputModelFile1,
						inputModelFile2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dg.stop();
			jframe.dispose();
			// �����������ʾ
			DifferenceResultFrame differenceResultFrame = new DifferenceResultFrame(
					"Difference Result", differenceList);
			differenceResultFrame.pack();
			RefineryUtilities.centerFrameOnScreen(differenceResultFrame);
			differenceResultFrame.setVisible(true);
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
