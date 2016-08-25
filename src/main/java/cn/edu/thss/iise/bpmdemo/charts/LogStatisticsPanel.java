package cn.edu.thss.iise.bpmdemo.charts;

//Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://kpdus.tripod.com/jad.html
//Decompiler options: packimports(3) fieldsfirst ansi space 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Paint;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import cn.edu.thss.iise.bpmdemo.analysis.core.statistic.LogStatisticUtil;
import cn.edu.thss.iise.bpmdemo.analysis.core.statistic.LogStatisticsInfo;

public class LogStatisticsPanel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static LogInfoPanel logPanel = new LogInfoPanel();

	public static ArrayList<LogStatisticsInfo> logList = null;

	static class DemoPanel extends JPanel implements ChartMouseListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private MyBarRenderer renderer;

		public void chartMouseMoved(ChartMouseEvent chartmouseevent) {
			org.jfree.chart.entity.ChartEntity chartentity = chartmouseevent
					.getEntity();
			if (!(chartentity instanceof CategoryItemEntity)) {
				renderer.setHighlightedItem(-1, -1);
				return;
			} else {
				CategoryItemEntity categoryitementity = (CategoryItemEntity) chartentity;
				CategoryDataset categorydataset = categoryitementity
						.getDataset();
				categoryitementity.getURLText();
				renderer.setHighlightedItem(categorydataset
						.getRowIndex(categoryitementity.getCategoryIndex()),
						categorydataset.getColumnIndex(categoryitementity
								.getSeries()));
				return;
			}
		}

		public void chartMouseClicked(ChartMouseEvent chartmouseevent) {
			org.jfree.chart.entity.ChartEntity chartentity = chartmouseevent
					.getEntity();
			if (chartentity instanceof CategoryItemEntity) {
				CategoryItemEntity categoryitementity = (CategoryItemEntity) chartentity;
				String cat = (String) categoryitementity.getCategory();
				int logIndex = Integer.parseInt(cat.substring(4));
				logPanel.updateLogPane(logList.get(logIndex - 1));
			}
		}

		public DemoPanel(MyBarRenderer mybarrenderer) {
			// super(new BorderLayout());
			super(new GridLayout(3, 1));
			renderer = mybarrenderer;
		}
	}

	static class MyBarRenderer extends BarRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int highlightRow;
		private int highlightColumn;

		public void setHighlightedItem(int i, int j) {
			if (highlightRow == i && highlightColumn == j) {
				return;
			} else {
				highlightRow = i;
				highlightColumn = j;
				notifyListeners(new RendererChangeEvent(this));
				return;
			}
		}

		public Paint getItemOutlinePaint(int i, int j) {
			if (i == highlightRow && j == highlightColumn)
				return Color.yellow;
			else
				return super.getItemOutlinePaint(i, j);
		}

		MyBarRenderer() {
			highlightRow = -1;
			highlightColumn = -1;
		}
	}

	public LogStatisticsPanel(String title, ArrayList<LogStatisticsInfo> list) {
		super(title);
		logList = list;
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(jpanel);
	}

	private static CategoryDataset createDataset1() {
		String s1 = "Events per case";

		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();

		for (int i = 0; i < logList.size(); i++) {
			LogStatisticsInfo log = logList.get(i);
			defaultcategorydataset.addValue(log.eventsNumber, s1, "log "
					+ (i + 1));
		}
		return defaultcategorydataset;
	}

	private static CategoryDataset createDataset2() {
		String s2 = "Event Classes per case";

		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();

		for (int i = 0; i < logList.size(); i++) {
			LogStatisticsInfo log = logList.get(i);
			defaultcategorydataset.addValue(log.eventClassesNum, s2, "log "
					+ (i + 1));
		}
		return defaultcategorydataset;
	}

	private static JFreeChart createChart(CategoryDataset categorydataset,
			Color color) {
		JFreeChart jfreechart = ChartFactory.createBarChart("Log Statistcis",
				"Log", "Value", categorydataset, PlotOrientation.VERTICAL,
				true, true, false);
		jfreechart.setBackgroundPaint(Color.white);
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		categoryplot.setBackgroundPaint(Color.lightGray);
		categoryplot.setDomainGridlinePaint(Color.white);
		categoryplot.setDomainGridlinesVisible(true);
		categoryplot.setRangeGridlinePaint(Color.white);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		MyBarRenderer mybarrenderer = new MyBarRenderer();
		mybarrenderer.setDrawBarOutline(true);
		categoryplot.setRenderer(mybarrenderer);
		GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, color,
				0.0F, 0.0F, new Color(0, 0, 64));
		mybarrenderer.setSeriesPaint(0, gradientpaint);
		return jfreechart;
	}

	public static JPanel createDemoPanel() {
		// chart1
		JFreeChart jfreechart1 = createChart(createDataset1(), Color.blue);
		CategoryPlot categoryplot1 = (CategoryPlot) jfreechart1.getPlot();
		ChartPanel chartpanel1 = new ChartPanel(jfreechart1);

		MyBarRenderer mybarrenderer = (MyBarRenderer) categoryplot1
				.getRenderer();
		DemoPanel demopanel = new DemoPanel(mybarrenderer);

		chartpanel1.addChartMouseListener(demopanel);

		// chart2
		JFreeChart jfreechart2 = createChart(createDataset2(), Color.green);
		ChartPanel chartpanel2 = new ChartPanel(jfreechart2);
		chartpanel2.addChartMouseListener(demopanel);

		demopanel.add(chartpanel1);
		demopanel.add(chartpanel2);
		demopanel.add(logPanel);
		return demopanel;
	}

	static class LogInfoPanel extends JPanel {
		JLabel logName = new JLabel("Name:");
		JLabel processesNumber = new JLabel("Processes Number:");
		JLabel casesNumber = new JLabel("Cases Number:");
		JLabel eventsNumber = new JLabel("Events Number:");
		JLabel eventClassesNum = new JLabel("Event Classes Number:");
		JLabel eventTypesNum = new JLabel("Event Types Number:");
		JLabel originatorsNum = new JLabel("Originators Number:");
		JLabel minEventsPerCase = new JLabel("Min Events Per Case:");
		JLabel meanEventsPerCase = new JLabel("Mean Events Number Per Case:");
		JLabel maxEventsPerCase = new JLabel("Max Events Number Per Case:");
		JLabel minEventsClassesNumPerCase = new JLabel(
				"Min Event Classes Number Per Case:");
		JLabel meanEventsClassesNumPerCase = new JLabel(
				"Mean Event Classes Number Per Case:");
		JLabel maxEventsClassesNumPerCase = new JLabel(
				"Max Event Classes Number Per Case:");

		public LogInfoPanel() {
			super(new GridLayout(7, 2));
			createLogPanel();
		}

		public void createLogPanel() {
			add(logName);
			add(processesNumber);
			add(casesNumber);
			add(eventsNumber);
			add(eventClassesNum);
			add(eventTypesNum);
			add(originatorsNum);
			add(minEventsPerCase);
			add(meanEventsPerCase);
			add(maxEventsPerCase);
			add(minEventsClassesNumPerCase);
			add(meanEventsClassesNumPerCase);
			add(maxEventsClassesNumPerCase);
		}

		public void updateLogPane(LogStatisticsInfo log) {
			logName.setText("Name: " + log.logName);
			processesNumber.setText("Processes Number: " + log.processesNumber);
			casesNumber.setText("Cases Number: " + log.casesNumber);
			eventsNumber.setText("Events Number: " + log.eventsNumber);
			eventClassesNum.setText("Event Classes Number: "
					+ log.eventClassesNum);
			eventTypesNum.setText("Event Types Number: " + log.eventTypesNum);
			originatorsNum.setText("Originators Number: " + log.originatorsNum);
			minEventsPerCase.setText("Min Events Per Case: "
					+ log.minEventsPerCase);
			meanEventsPerCase.setText("Mean Events Number Per Case: "
					+ log.meanEventsPerCase);
			maxEventsPerCase.setText("Max Events Number Per Case: "
					+ log.maxEventsPerCase);
			minEventsClassesNumPerCase
					.setText("Min Event Classes Number Per Case: "
							+ log.minEventsClassesNumPerCase);
			meanEventsClassesNumPerCase
					.setText("Mean Event Classes Number Per Case: "
							+ log.meanEventsClassesNumPerCase);
			maxEventsClassesNumPerCase
					.setText("Max Event Classes Number Per Case: "
							+ log.maxEventsClassesNumPerCase);
		}
	}

	public static void main(String args[]) {
		ArrayList<LogStatisticsInfo> logList = new ArrayList<LogStatisticsInfo>();
		String logFile = "D:\\Lab\\log(new)��BPMN�ļ�\\log\\���";
		File inputFolder = new File(logFile);
		File[] inputFiles = inputFolder.listFiles();
		for (File file : inputFiles) {
			try {
				LogStatisticsInfo log = LogStatisticUtil
						.getLogStatisticsInfo(file.getAbsolutePath());
				log.logName = file.getName();
				logList.add(log);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		LogStatisticsPanel mouseoverdemo1 = new LogStatisticsPanel(
				"Mouseover Demo 1", logList);
		mouseoverdemo1.pack();
		RefineryUtilities.centerFrameOnScreen(mouseoverdemo1);
		mouseoverdemo1.setVisible(true);
	}
}