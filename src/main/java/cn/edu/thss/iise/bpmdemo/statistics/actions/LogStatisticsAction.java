package cn.edu.thss.iise.bpmdemo.statistics.actions;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jfree.ui.RefineryUtilities;

import cn.edu.thss.iise.bpmdemo.analysis.core.statistic.LogStatisticUtil;
import cn.edu.thss.iise.bpmdemo.analysis.core.statistic.LogStatisticsInfo;
import cn.edu.thss.iise.bpmdemo.charts.LogStatisticsPanel;

public class LogStatisticsAction implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window = null;

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		DirectoryDialog dialog = new DirectoryDialog(window.getShell(),
				SWT.OPEN);
		dialog.setMessage("Choose the folder contains the log files!");
		String logFile = dialog.open();

		// input log file
		ArrayList<LogStatisticsInfo> logList = new ArrayList<LogStatisticsInfo>();
		File inputFolder = new File(logFile);
		File[] inputFiles = inputFolder.listFiles();
		for (File file : inputFiles) {
			try {
				if (file.isFile() && file.getName().endsWith(".xls")) {
					LogStatisticsInfo log = LogStatisticUtil
							.getLogStatisticsInfo(file.getAbsolutePath());
					log.logName = file.getName();
					logList.add(log);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (logList.size() == 0) {
			MessageBox mb = new MessageBox(window.getShell(), SWT.ICON_WARNING);
			mb.setText("��ʾ��");
			mb.setMessage("����·��������߲�����־�ļ�!");
			mb.open();
		} else {
			LogStatisticsPanel logStatisticsPanel = new LogStatisticsPanel(
					"Log Statistics", logList);
			logStatisticsPanel.pack();
			RefineryUtilities.centerFrameOnScreen(logStatisticsPanel);
			logStatisticsPanel.setVisible(true);
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
