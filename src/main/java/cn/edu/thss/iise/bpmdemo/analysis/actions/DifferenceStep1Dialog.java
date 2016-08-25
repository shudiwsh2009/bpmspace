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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DifferenceStep1Dialog extends TitleAreaDialog {

	private Text inputModel1FileText;
	private Text inputModel2FileText;
	private String inputModel1File;
	private String inputModel2File;

	public DifferenceStep1Dialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create() {
		super.create();
		setTitle("Difference: ");
		setMessage("Select the folder contains input & output models.",
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
		label1.setText("Choose model 1 file:");

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
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterPath(System.getProperty("java.home"));
				dialog.setFilterExtensions(new String[] { "*.bpmn", });
				dialog.setFilterNames(new String[] { "Text Files (*.bpmn)" });
				String file = dialog.open();
				inputModel1FileText.setText(file);
			}
		});

		// import file button
		Label label2 = new Label(parent, SWT.NONE);
		label2.setText("Choose model 2 file:");

		Composite exportFilePanel = new Composite(parent, SWT.NONE);
		FormLayout formLayout2 = new FormLayout();
		exportFilePanel.setLayout(formLayout2);

		inputModel2FileText = new Text(exportFilePanel, SWT.NONE);
		FormData fd3 = new FormData();
		fd3.left = new FormAttachment(0, 0);
		fd3.top = new FormAttachment(0, 0);
		fd3.width = 375;
		fd3.height = 25;
		inputModel2FileText.setLayoutData(fd3);

		Button outputBtn = new Button(exportFilePanel, SWT.NONE);
		outputBtn.setText("Browse Model");
		FormData fd4 = new FormData();
		fd4.left = new FormAttachment(0, 385);
		fd4.top = new FormAttachment(0, 0);
		fd4.height = 25;
		outputBtn.setLayoutData(fd4);

		outputBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterPath(System.getProperty("java.home"));
				dialog.setFilterExtensions(new String[] { "*.bpmn" });
				dialog.setFilterNames(new String[] { "Text Files (*.bpmn)" });
				String file = dialog.open();
				inputModel2FileText.setText(file);
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
		inputModel2File = inputModel2FileText.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public String getInputModel1File() {
		return inputModel1File;
	}

	public String getInputModel2File() {
		return inputModel2File;
	}

}
