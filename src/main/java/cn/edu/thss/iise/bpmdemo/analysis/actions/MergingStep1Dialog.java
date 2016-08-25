package cn.edu.thss.iise.bpmdemo.analysis.actions;

import javax.swing.JFrame;

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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.thss.iise.bpmdemo.analysis.core.merge.MergeUtil;
import cn.edu.thss.iise.bpmdemo.charts.MemoryUsageDemo1;
import cn.edu.thss.iise.bpmdemo.charts.MemoryUsageDemo1.DataGenerator;

public class MergingStep1Dialog extends TitleAreaDialog {

	private Text inputModelFileText;
	private Text outputModelFileText;
	private String inputModelFile;
	private String outputModelFile;

	public MergingStep1Dialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create() {
		super.create();
		setTitle("Merging: ");
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
		label1.setText("Choose input model files:");

		Composite importFilePanel = new Composite(parent, SWT.NONE);
		FormLayout formLayout1 = new FormLayout();
		importFilePanel.setLayout(formLayout1);

		inputModelFileText = new Text(importFilePanel, SWT.NONE);
		FormData fd1 = new FormData();
		fd1.left = new FormAttachment(0, 0);
		fd1.top = new FormAttachment(0, 0);
		fd1.width = 375;
		fd1.height = 25;
		inputModelFileText.setLayoutData(fd1);

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
				dialog.setMessage("Choose the folder contains the bpmn models to be merged.");
				String file = dialog.open();

				inputModelFileText.setText(file);
			}
		});

		// import file button
		Label label2 = new Label(parent, SWT.NONE);
		label2.setText("Choose folder that contains the merged models:");

		Composite exportFilePanel = new Composite(parent, SWT.NONE);
		FormLayout formLayout2 = new FormLayout();
		exportFilePanel.setLayout(formLayout2);

		outputModelFileText = new Text(exportFilePanel, SWT.NONE);
		FormData fd3 = new FormData();
		fd3.left = new FormAttachment(0, 0);
		fd3.top = new FormAttachment(0, 0);
		fd3.width = 375;
		fd3.height = 25;
		outputModelFileText.setLayoutData(fd3);

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
				dialog.setFilterExtensions(new String[] { "*.bpmn", "*.bpmn2" });
				dialog.setFilterNames(new String[] { "Text Files (*.bpmn)",
						"All Files (*.bpmn2)" });
				String file = dialog.open();

				outputModelFileText.setText(file);
			}
		});

		return parent;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	protected void saveInput() {
		inputModelFile = inputModelFileText.getText();
		outputModelFile = outputModelFileText.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();

		JFrame jframe = new JFrame("Memory Usage Demo");
		MemoryUsageDemo1 memoryusagedemo = new MemoryUsageDemo1(30000);
		jframe.getContentPane().add(memoryusagedemo, "Center");
		jframe.setBounds(200, 120, 600, 280);
		jframe.setVisible(true);
		DataGenerator dg = (memoryusagedemo.new DataGenerator(100));
		dg.start();
		// call merge function
		MergeUtil.merge(inputModelFile, outputModelFile);
		dg.stop();
		jframe.dispose();
		MessageBox mb = new MessageBox(new Shell(), SWT.ICON_WARNING);
		mb.setText("��ʾ��");
		mb.setMessage("ģ�ͺϲ����");
		super.okPressed();
	}

	public String getInputModelFile() {
		return inputModelFile;
	}

	public String getOutputModelFile() {
		return outputModelFile;
	}

}
