package cn.edu.thss.iise.bpmdemo.statistics.actions;

import java.awt.Font;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

public class TestDialog extends TitleAreaDialog {

	public TestDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create() {
		super.create();
		setTitle("Test Dialog");
		setMessage("helloo world~", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		/*
		 * Composite chartComp = new Composite(parent,SWT.NONE); FormLayout
		 * formLayout = new FormLayout(); chartComp.setLayout(formLayout); Shell
		 * shell = parent.getShell();
		 */
		JFreeChart chart = createChart(createDataset());

		final ChartComposite frame = new ChartComposite(parent, SWT.NONE,
				chart, true);

		GridData gridData = new GridData();
		gridData.widthHint = 400;
		gridData.heightHint = 290;
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		frame.setLayoutData(gridData);
		frame.pack();

		return parent;
	}

	@Override
	protected void okPressed() {

	}

	/**
	 * Creates a sample dataset.
	 * 
	 * @return A sample dataset.
	 */
	private static PieDataset createDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("One", new Double(43.2));
		dataset.setValue("Two", new Double(10.0));
		dataset.setValue("Three", new Double(27.5));
		dataset.setValue("Four", new Double(17.5));
		dataset.setValue("Five", new Double(11.0));
		dataset.setValue("Six", new Double(19.4));
		return dataset;
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return A chart.
	 */
	private static JFreeChart createChart(PieDataset dataset) {

		JFreeChart chart = ChartFactory.createPieChart("Pie Chart Demo 1", // chart
																			// title
				dataset, // data
				true, // include legend
				true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionOutlinesVisible(false);
		plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		plot.setNoDataMessage("No data available");
		plot.setCircular(false);
		plot.setLabelGap(0.02);
		return chart;

	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		TestDialog dialog = new TestDialog(shell);
		dialog.create();

		if (dialog.open() == Window.OK) {

		}
	}
}
