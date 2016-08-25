package cn.edu.thss.iise.bpmdemo.charts;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * A simple demonstration application showing how to create a meter chart.
 */
public class MeterChart extends ApplicationFrame {

	public static DefaultValueDataset dataset;

	private static double logNumber = 0;

	/**
	 * Creates a new demo.
	 *
	 * @param title
	 *            the frame title.
	 */
	public MeterChart(String title, int number) {
		super(title);
		this.logNumber = number;
		JPanel chartPanel = createDemoPanel();
		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(chartPanel);

	}

	/**
	 * Creates a sample chart.
	 *
	 * @param dataset
	 *            a dataset.
	 *
	 * @return The chart.
	 */
	private static JFreeChart createChart(ValueDataset dataset) {
		CustomMeterPlot plot = new CustomMeterPlot(dataset);

		plot.addInterval(new MeterInterval("All", new Range(0.0, logNumber)));

		plot.addInterval(new MeterInterval("High", new Range(logNumber * 0.8,
				logNumber)));
		plot.setDialOutlinePaint(Color.white);
		plot.addInterval(new MeterInterval("Low", new Range(0.00,
				logNumber * 0.2), Color.RED, new BasicStroke(2.0f), null));
		plot.setUnits("Log");
		plot.setTickLabelsVisible(true);
		plot.setDialShape(DialShape.CHORD);
		plot.setValuePaint(Color.BLUE);

		plot.setTickLabelsVisible(true);

		plot.setRange(new Range(0, logNumber));

		plot.setTickLabelPaint(Color.ORANGE);

		JFreeChart chart = new JFreeChart("Log Minging Process",
				JFreeChart.DEFAULT_TITLE_FONT, plot, false);
		return chart;
	}

	/**
	 * Creates a panel for the demo (used by SuperDemo.java).
	 *
	 * @return A panel.
	 */
	public static JPanel createDemoPanel() {
		dataset = new DefaultValueDataset(0.0);
		JFreeChart chart = createChart(dataset);
		JPanel panel = new JPanel(new BorderLayout());
		/*
		 * JSlider slider = new JSlider(0, 100, 50);
		 * slider.setMajorTickSpacing(10); slider.setMinorTickSpacing(5);
		 * slider.setPaintLabels(true); slider.setPaintTicks(true);
		 * slider.addChangeListener(new ChangeListener() { public void
		 * stateChanged(ChangeEvent e) { JSlider s = (JSlider) e.getSource();
		 * dataset.setValue(new Integer(s.getValue())); } });
		 */
		panel.add(new ChartPanel(chart));
		// panel.add(BorderLayout.SOUTH, slider);
		return panel;
	}

	/**
	 * Starting point for the demonstration application.
	 *
	 * @param args
	 *            ignored.
	 */
	public static void main(String[] args) {
		/*
		 * MeterChart demo = new MeterChart("Meter Chart Demo 2"); demo.pack();
		 * RefineryUtilities.centerFrameOnScreen(demo); demo.setVisible(true);
		 */
	}
}