package cn.edu.thss.iise.bpmdemo.charts;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class MemoryUsageDemo1 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public class DataGenerator extends Timer implements ActionListener {

		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent actionevent) {
			long l = Runtime.getRuntime().freeMemory();
			long l1 = Runtime.getRuntime().totalMemory();

			addTotalObservation(l1);
			addFreeObservation(l);
		}

		public DataGenerator(int i) {
			super(i, null);
			addActionListener(this);
		}
	}

	private TimeSeries total;
	private TimeSeries free;

	public MemoryUsageDemo1(int i) {
		super(new BorderLayout());
		total = new TimeSeries("Total CPU Capacity", Millisecond.class);
		total.setMaximumItemAge(i);
		free = new TimeSeries("CPU Usage", Millisecond.class);
		free.setMaximumItemAge(i);
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(total);
		timeseriescollection.addSeries(free);
		DateAxis dateaxis = new DateAxis("Time");
		NumberAxis numberaxis = new NumberAxis("Memory");
		dateaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
		numberaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
		dateaxis.setLabelFont(new Font("SansSerif", 0, 14));
		numberaxis.setLabelFont(new Font("SansSerif", 0, 14));
		XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(
				true, false);
		xylineandshaperenderer.setSeriesPaint(0, Color.red);
		xylineandshaperenderer.setSeriesPaint(1, Color.green);
		xylineandshaperenderer.setSeriesStroke(0, new BasicStroke(1F, 0, 1));
		xylineandshaperenderer.setSeriesStroke(1, new BasicStroke(1F, 0, 1));
		XYPlot xyplot = new XYPlot(timeseriescollection, dateaxis, numberaxis,
				xylineandshaperenderer);
		dateaxis.setAutoRange(true);
		dateaxis.setLowerMargin(0.0D);
		dateaxis.setUpperMargin(0.0D);
		dateaxis.setTickLabelsVisible(true);
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		JFreeChart jfreechart = new JFreeChart("JVM Memory Usage", new Font(
				"SansSerif", 1, 24), xyplot, true);
		// ChartUtilities.applyCurrentTheme(jfreechart);
		ChartPanel chartpanel = new ChartPanel(jfreechart, true);
		chartpanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 4, 4),
				BorderFactory.createLineBorder(Color.black)));
		add(chartpanel);
	}

	public void addTotalObservation(double d) {
		total.add(new Millisecond(), d);
	}

	public void addFreeObservation(double d) {
		free.add(new Millisecond(), d);
	}

	public static void main(String args[]) {
		JFrame jframe = new JFrame("Memory Usage Demo");
		MemoryUsageDemo1 memoryusagedemo = new MemoryUsageDemo1(30000);
		jframe.getContentPane().add(memoryusagedemo, "Center");
		jframe.setBounds(200, 120, 600, 280);
		jframe.setVisible(true);
		(memoryusagedemo.new DataGenerator(100)).start();
		jframe.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent windowevent) {
				System.exit(0);
			}
		});
	}

}