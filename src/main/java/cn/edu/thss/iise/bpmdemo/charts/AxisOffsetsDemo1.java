package cn.edu.thss.iise.bpmdemo.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

public class AxisOffsetsDemo1 extends ApplicationFrame {
	private static final long serialVersionUID = 1L;

	public AxisOffsetsDemo1(String s) {
		super(s);
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(jpanel);
	}

	private static CategoryDataset createDataset() {
		String s = "S1";
		String s1 = "S2";
		String s2 = "S3";
		String s3 = "C1";
		String s4 = "C2";
		String s5 = "C3";
		String s6 = "C4";
		String s7 = "C5";
		String s8 = "C6";
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		defaultcategorydataset.addValue(1.0D, s, s3);
		defaultcategorydataset.addValue(4D, s, s4);
		defaultcategorydataset.addValue(3D, s, s5);
		defaultcategorydataset.addValue(5D, s, s6);
		defaultcategorydataset.addValue(5D, s, s7);
		defaultcategorydataset.addValue(6D, s, s8);
		return defaultcategorydataset;
	}

	private static JFreeChart createChart(String s,
			CategoryDataset categorydataset) {
		JFreeChart jfreechart = ChartFactory.createBarChart(s, "Category",
				"Value", categorydataset, PlotOrientation.VERTICAL, false,
				true, false);
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		categoryplot.setDomainGridlinesVisible(true);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		BarRenderer barrenderer = (BarRenderer) categoryplot.getRenderer();
		barrenderer.setDrawBarOutline(true);
		GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F,
				Color.green, 0.0F, 0.0F, new Color(0, 0, 64));
		barrenderer.setSeriesPaint(0, gradientpaint);

		CategoryAxis domainAxis = categoryplot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		return jfreechart;
	}

	public static JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart("Events per case", createDataset());
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		categoryplot.setAxisOffset(RectangleInsets.ZERO_INSETS);
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMinimumDrawWidth(0);
		chartpanel.setMinimumDrawHeight(0);
		JFreeChart jfreechart1 = createChart("Event classes per case",
				createDataset());
		ChartPanel chartpanel1 = new ChartPanel(jfreechart1);
		chartpanel1.setMinimumDrawWidth(0);
		chartpanel1.setMinimumDrawHeight(0);
		CategoryPlot categoryplot1 = (CategoryPlot) jfreechart1.getPlot();
		categoryplot1.setAxisOffset(new RectangleInsets(0, 0, 30, 40));
		DemoPanel1 demopanel = new DemoPanel1(new GridLayout(3, 1));

		demopanel.add(chartpanel);
		demopanel.add(chartpanel1);

		// demopanel.add(demopanel.l);
		// demopanel.add(demopanel.l);
		// demopanel.addChart(jfreechart);
		// demopanel.addChart(jfreechart1);
		return demopanel;
	}

	public static void main(String args[]) {
		AxisOffsetsDemo1 axisoffsetsdemo1 = new AxisOffsetsDemo1(
				"JFreeChart: AxisOffsetsDemo1.java");
		axisoffsetsdemo1.pack();
		RefineryUtilities.centerFrameOnScreen(axisoffsetsdemo1);
		axisoffsetsdemo1.setVisible(true);
	}

	static class DemoPanel1 extends JPanel implements ChartMouseListener {

		public DemoPanel1(java.awt.LayoutManager layoutmanager) {
			super(layoutmanager);
		}

		@Override
		public void chartMouseClicked(ChartMouseEvent chartmouseevent) {
			// TODO Auto-generated method stub
			org.jfree.chart.entity.ChartEntity chartentity = chartmouseevent
					.getEntity();
			if (chartentity instanceof CategoryItemEntity) {
				CategoryItemEntity categoryitementity = (CategoryItemEntity) chartentity;
				System.out.println(categoryitementity.getURLText());
			}
		}

		@Override
		public void chartMouseMoved(ChartMouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}
}
