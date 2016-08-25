package cn.edu.thss.iise.bpmdemo.analysis.actions;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import cn.edu.thss.iise.bpmdemo.analysis.core.mining.MiningUtil;

public class MiningStep2Dialog extends TitleAreaDialog {

	private String logFile = null;
	private String modelFile = null;

	public MiningStep2Dialog(Shell parentShell, String logFile, String modelFile) {
		super(parentShell);
		this.logFile = logFile;
		this.modelFile = modelFile;
	}

	public MiningStep2Dialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create() {
		super.create();
		setTitle("Mining: working...");
		setMessage("In the process of mining models from log files.",
				IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label1 = new Label(parent, SWT.NONE);
		label1.setText("in the process...");
		Composite panel = new Composite(parent, SWT.NONE);

		ProgressBar pb1 = new ProgressBar(parent, SWT.NONE);
		pb1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pb1.setMinimum(0);
		pb1.setMaximum(10);

		new LongMiningOperation(panel.getDisplay(), pb1).start();
		return parent;
	}

	public String getLogFile() {
		return logFile;
	}

	public String getModelFile() {
		return modelFile;
	}

	class LongMiningOperation extends Thread {
		private Display display;
		private ProgressBar progressBar;

		public LongMiningOperation(Display display, ProgressBar progressBar) {
			this.display = display;
			this.progressBar = progressBar;
		}

		public void run() {

			MiningUtil.mining(logFile, modelFile);

			for (int i = 0; i < 10; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				display.asyncExec(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (progressBar.isDisposed())
							return;
						progressBar.setSelection(progressBar.getSelection() + 1);
					}
				});
			}
		}
	}

}
