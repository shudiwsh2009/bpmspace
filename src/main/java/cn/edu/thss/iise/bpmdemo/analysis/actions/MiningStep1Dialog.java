package cn.edu.thss.iise.bpmdemo.analysis.actions;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MiningStep1Dialog extends TitleAreaDialog {
	private Text logFileText;
	private Text modelFileText;
	private Image image = null; // image of log statistics
	private String logFile;
	private String modelFile;

	public MiningStep1Dialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Mining: Choose Log & Model File");
		setMessage("Select the folder contains log and model sfiles.",
				IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;

		parent.setLayout(layout);

		// image show
		final Shell shell = parent.getShell();
		// image = new Image(
		// shell.getDisplay(),"C:/Users/chenhz/Desktop/����ССѧ��/0703���Ҫ/img/logsts.JPG");
		// Label myLabel = new Label(parent,SWT.NONE );
		// myLabel.setImage( image );

		// import file button
		Label label1 = new Label(parent, SWT.NONE);
		label1.setText("Choose log files:");

		Composite importFilePanel = new Composite(parent, SWT.NONE);
		FormLayout formLayout1 = new FormLayout();
		importFilePanel.setLayout(formLayout1);

		logFileText = new Text(importFilePanel, SWT.NONE);
		FormData fd1 = new FormData();
		fd1.left = new FormAttachment(0, 0);
		fd1.top = new FormAttachment(0, 0);
		fd1.width = 375;
		fd1.height = 25;
		logFileText.setLayoutData(fd1);

		Button chooseLogBtn = new Button(importFilePanel, SWT.NONE);
		chooseLogBtn.setText("Browse Log");
		FormData fd2 = new FormData();
		fd2.left = new FormAttachment(0, 385);
		fd2.top = new FormAttachment(0, 0);
		fd2.height = 25;
		chooseLogBtn.setLayoutData(fd2);

		chooseLogBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
				dialog.setMessage("Choose the folder contains the log files!");
				String file = dialog.open();
				logFileText.setText(file);
			}
		});

		// import file button
		Label label2 = new Label(parent, SWT.NONE);
		label2.setText("Choose the folder contains models:");

		Composite exportFilePanel = new Composite(parent, SWT.NONE);
		FormLayout formLayout2 = new FormLayout();
		exportFilePanel.setLayout(formLayout2);

		modelFileText = new Text(exportFilePanel, SWT.NONE);
		FormData fd3 = new FormData();
		fd3.left = new FormAttachment(0, 0);
		fd3.top = new FormAttachment(0, 0);
		fd3.width = 375;
		fd3.height = 25;
		modelFileText.setLayoutData(fd3);

		Button chooseModelBtn = new Button(exportFilePanel, SWT.NONE);
		chooseModelBtn.setText("Browse Model");
		FormData fd4 = new FormData();
		fd4.left = new FormAttachment(0, 385);
		fd4.top = new FormAttachment(0, 0);
		fd4.height = 25;
		chooseModelBtn.setLayoutData(fd4);

		chooseModelBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
				dialog.setMessage("Choose the folder contains the bpmn files!");
				String file = dialog.open();
				modelFileText.setText(file);
			}
		});

		return parent;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	protected void saveInput() {
		logFile = logFileText.getText();
		modelFile = modelFileText.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public String getLogFile() {
		return logFile;
	}

	public String getModelFile() {
		return modelFile;
	}

}
