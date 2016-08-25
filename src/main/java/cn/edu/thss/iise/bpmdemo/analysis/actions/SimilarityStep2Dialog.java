package cn.edu.thss.iise.bpmdemo.analysis.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import cn.edu.thss.iise.bpmdemo.analysis.core.similarity.SimilarityUtil;

public class SimilarityStep2Dialog extends TitleAreaDialog {

	private List<String> leftList = null;
	private List<String> rightList = null;

	public SimilarityStep2Dialog(Shell parentShell, List<String> l,
			List<String> r) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		this.leftList = l;
		this.rightList = r;
		if (this.leftList == null)
			leftList = new ArrayList<String>();
		if (this.rightList == null)
			rightList = new ArrayList<String>();
	}

	@Override
	public void create() {
		super.create();
		setTitle("Similarity:");
		setMessage("Following is the similarty comparation result.",
				IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		parent.setLayout(new GridLayout());
		int l, r;
		double sim;
		int i;
		int size1 = leftList.size() + 1;
		int size2 = rightList.size() + 1;
		String[][] items = new String[size1][size2];
		items[0][0] = "sim";
		for (i = 1; i < size2; i++) {
			String fileName = rightList.get(i - 1);
			items[0][i] = fileName.substring(fileName.lastIndexOf("\\") + 1);
		}
		for (i = 1; i < size1; i++) {
			String fileName = leftList.get(i - 1);
			items[i][0] = fileName.substring(fileName.lastIndexOf("\\") + 1);
		}
		// calculate the similarity
		for (l = 0; l < leftList.size(); l++) {
			for (r = 0; r < rightList.size(); r++) {
				sim = SimilarityUtil.similarity(leftList.get(l),
						rightList.get(r));
				items[l + 1][r + 1] = String.format("%.2f", sim);
			}
		}
		Table table = createTable(parent, SWT.NONE, items, size2);

		GridData data = new GridData();
		data.widthHint = 100 * size2;
		data.heightHint = 20 * size1;
		table.setLayoutData(data);

		return parent;
	}

	// Create the Table and TableColumns
	protected Table createTable(Composite parent, int mode,
			String[][] contents, int colNumber) {
		Table table = new Table(parent, mode | SWT.SINGLE | SWT.FULL_SELECTION
				| SWT.V_SCROLL | SWT.H_SCROLL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		int i;
		for (i = 0; i < colNumber; i++) {
			createTableColumn(table, SWT.NONE, "Column" + i, 100);
		}
		addTableContents(table, contents);
		return table;
	}

	protected TableColumn createTableColumn(Table table, int style,
			String title, int width) {
		TableColumn tc = new TableColumn(table, style);
		tc.setText(title);
		tc.setResizable(true);
		tc.setWidth(width);
		return tc;
	}

	protected void addTableContents(Table table, String[][] items) {
		for (int i = 0; i < items.length; i++) {
			String[] item = items[i];
			TableItem ti = new TableItem(table, SWT.NONE);
			ti.setText(item);
		}
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}
}
