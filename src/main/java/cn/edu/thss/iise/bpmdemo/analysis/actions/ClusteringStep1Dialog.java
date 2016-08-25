package cn.edu.thss.iise.bpmdemo.analysis.actions;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

public class ClusteringStep1Dialog extends TitleAreaDialog {

	private Text inputModel1FileText;
	private String inputModel1File;

	public ClusteringStep1Dialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create() {
		super.create();
		setTitle("Clustering: ");
		setMessage("Select the folder contains models involve in clustering.",
				IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;

		parent.setLayout(layout);

		// image show
		final Shell shell = parent.getShell();

		// import file button
		Label label1 = new Label(parent, SWT.NONE);
		label1.setText("Choose models folder:");

		Composite importFilePanel = new Composite(parent, SWT.NONE);
		FormLayout formLayout1 = new FormLayout();
		importFilePanel.setLayout(formLayout1);

		inputModel1FileText = new Text(importFilePanel, SWT.NONE);
		FormData fd1 = new FormData();
		fd1.left = new FormAttachment(0, 0);
		fd1.top = new FormAttachment(0, 0);
		fd1.width = 375;
		fd1.height = 25;
		inputModel1FileText.setLayoutData(fd1);

		Button inputBtn = new Button(importFilePanel, SWT.NONE);
		inputBtn.setText("Browse Model");
		FormData fd2 = new FormData();
		fd2.left = new FormAttachment(0, 385);
		fd2.top = new FormAttachment(0, 0);
		fd2.height = 25;
		inputBtn.setLayoutData(fd2);

		inputBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
				dialog.setMessage("Choose the folder contains the BPMN files!");
				String file = dialog.open();

				inputModel1FileText.setText(file);
			}
		});
		return parent;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	protected void saveInput() {
		inputModel1File = inputModel1FileText.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public String getInputModel1File() {
		return inputModel1File;
	}

}
