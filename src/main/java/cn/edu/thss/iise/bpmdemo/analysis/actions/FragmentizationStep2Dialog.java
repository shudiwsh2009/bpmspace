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

import cn.edu.thss.iise.bpmdemo.analysis.core.fragment.FragmentUtil;

public class FragmentizationStep2Dialog extends TitleAreaDialog {

	private String inputFile;
	private String outputFile;

	public FragmentizationStep2Dialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	public FragmentizationStep2Dialog(Shell parentShell, String inputFile,
			String outputFile) {
		super(parentShell);
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Fragmentization: working...");
		setMessage("In the process of fragment model into submodels.",
				IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label numberOfModelLabel = null;
		Label numberOfModelTypeLabel = null;

		numberOfModelLabel = new Label(parent, SWT.NONE);
		numberOfModelTypeLabel = new Label(parent, SWT.NONE);

		numberOfModelLabel.setVisible(false);
		numberOfModelTypeLabel.setVisible(false);

		Composite panel = new Composite(parent, SWT.NONE);

		ProgressBar pb1 = new ProgressBar(parent, SWT.NONE);
		pb1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// ��ʾ����������Сֵ
		pb1.setMinimum(0);
		// ��ʾ�����������ֵ
		pb1.setMaximum(10);

		FragmentUtil.fragment(inputFile, outputFile);

		new LongFragmentizationOperation(panel.getDisplay(), pb1,
				numberOfModelLabel, numberOfModelLabel).start();

		return parent;
	}

	public String getInputFile() {
		return inputFile;
	}

	public String getOutputFile() {
		return outputFile;
	}

	class LongFragmentizationOperation extends Thread {
		private Display display;
		private ProgressBar progressBar;
		private Label numberOfModelLabel, numberOfModelTypeLabel;

		public LongFragmentizationOperation(Display display,
				ProgressBar progressBar, Label numberOfModelLabel,
				Label numberOfModelTypeLabel) {
			this.display = display;
			this.progressBar = progressBar;
			this.numberOfModelLabel = numberOfModelLabel;
			this.numberOfModelTypeLabel = numberOfModelTypeLabel;
		}

		public void run() {
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
			// done show result
			numberOfModelLabel.setText("Number of Models:   88");
			numberOfModelTypeLabel.setText("Number of Types:    8");
			numberOfModelLabel.setVisible(true);
			numberOfModelTypeLabel.setVisible(true);
		}
	}

}
