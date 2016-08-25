package cn.edu.thss.iise.bpmdemo.analysis.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class SimilarityStep1Dialog extends TitleAreaDialog {

	private int leftSelectedIndex = -1;
	private int rightSelectedIndex = -1;
	private Table ltable = null;
	private Table rtable = null;
	private Shell shell;
	private List<String> leftList = new ArrayList<String>();
	private List<String> rightList = new ArrayList<String>();

	public SimilarityStep1Dialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		shell = parentShell;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Similarity:");
		setMessage("Choose the models to compare.",
				IMessageProvider.INFORMATION);
	}

	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.makeColumnsEqualWidth = true;
		layout.numColumns = 2;
		layout.marginLeft = 20;

		parent.setLayout(layout);

		// left part
		GridData leftData = new GridData();
		leftData.widthHint = 200;
		leftData.heightHint = 340;

		Composite leftPart = new Composite(parent, SWT.NONE);
		leftPart.setLayoutData(leftData);

		layout = new GridLayout();
		leftPart.setLayout(layout);

		ltable = new Table(leftPart, SWT.VIRTUAL | SWT.BORDER | SWT.SINGLE
				| SWT.FULL_SELECTION);
		leftData = new GridData();
		leftData.widthHint = 200;
		leftData.heightHint = 280;
		ltable.setLayoutData(leftData);
		ltable.setLinesVisible(true);
		ltable.setHeaderVisible(true);
		TableColumn lcolumn = new TableColumn(ltable, SWT.NONE);
		lcolumn.setText("Left Part");
		lcolumn.setResizable(true);
		lcolumn.setWidth(180);
		ltable.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				TableItem item = (TableItem) event.item;
				int index = ltable.indexOf(item);
				leftSelectedIndex = index;
			}
		});

		Button leftAddBtn = new Button(leftPart, SWT.PUSH);
		leftData = new GridData();
		leftData.widthHint = 90;
		leftAddBtn.setText("Add");
		leftAddBtn.setLayoutData(leftData);
		Button leftDelBtn = new Button(leftPart, SWT.PUSH);
		leftDelBtn.setText("Remove");
		leftDelBtn.setLayoutData(leftData);

		// right part
		GridData rightData = new GridData();
		rightData.widthHint = 200;
		rightData.heightHint = 340;
		Composite rightPart = new Composite(parent, SWT.NONE);
		rightPart.setLayoutData(rightData);
		layout = new GridLayout();
		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = true;
		rightPart.setLayout(layout);

		rtable = new Table(rightPart, SWT.VIRTUAL | SWT.BORDER | SWT.SINGLE
				| SWT.FULL_SELECTION);
		rightData = new GridData();
		rightData.widthHint = 200;
		rightData.heightHint = 280;
		rtable.setLayoutData(rightData);
		rtable.setHeaderVisible(true);
		rtable.setLinesVisible(true);

		TableColumn rcolumn = new TableColumn(rtable, SWT.NONE);
		rcolumn.setText("Right Part");
		rcolumn.setResizable(true);
		rcolumn.setWidth(180);

		rtable.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				TableItem item = (TableItem) event.item;
				int index = rtable.indexOf(item);
				rightSelectedIndex = index;
			}
		});

		Button rightAddBtn = new Button(rightPart, SWT.PUSH);
		rightData = new GridData();
		rightData.widthHint = 90;
		rightAddBtn.setText("Add");
		rightAddBtn.setLayoutData(rightData);
		Button rightDelBtn = new Button(rightPart, SWT.PUSH);
		rightDelBtn.setText("Remove");
		rightDelBtn.setLayoutData(rightData);

		leftDelBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (leftSelectedIndex != -1) {
					ltable.remove(leftSelectedIndex);
				}
			}
		});

		rightDelBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (rightSelectedIndex != -1) {
					rtable.remove(rightSelectedIndex);
				}
			}
		});

		leftAddBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterPath(System.getProperty("java.home"));
				dialog.setFilterExtensions(new String[] { "*.bpmn", "*.bpmn2" });
				dialog.setFilterNames(new String[] { "Text Files (*.bpmn)",
						"All Files (*.bpmn2)" });
				String file = dialog.open();
				// ��������Ӽ���process�Ĵ���
				leftList.add(file);
				TableItem ti = new TableItem(ltable, SWT.NONE);
				ti.setText(0, file.substring(file.lastIndexOf("\\") + 1));
			}
		});

		rightAddBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterPath(System.getProperty("java.home"));
				dialog.setFilterExtensions(new String[] { "*.bpmn", "*.bpmn2" });
				dialog.setFilterNames(new String[] { "Text Files (*.bpmn)",
						"All Files (*.bpmn2)" });
				String file = dialog.open();
				// ��������Ӽ���process�Ĵ���
				rightList.add(file);
				TableItem ti = new TableItem(rtable, SWT.NONE);
				ti.setText(String.valueOf(rightList.size() + 1));
				ti.setText(0, file.substring(file.lastIndexOf("\\") + 1));
			}
		});
		return parent;
	}

	@Override
	protected void okPressed() {
		super.okPressed();
		// transfer data
		// left table
		TableItem[] ltis = ltable.getItems();
		int i;
		for (i = 0; i < ltis.length; i++) {
			leftList.add(ltis[i].getText());
		}
		// right table
		TableItem[] rtis = rtable.getItems();
		for (i = 0; i < rtis.length; i++) {
			rightList.add(rtis[i].getText());
		}
	}

	public List<String> getLeftList() {
		return leftList;
	}

	public List<String> getRightList() {
		return rightList;
	}

}
