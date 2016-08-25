package cn.edu.thss.iise.bpmdemo.charts;

//Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://kpdus.tripod.com/jad.html
//Decompiler options: packimports(3) fieldsfirst ansi space 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Paint;

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
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class MouseOverDemo1 extends ApplicationFrame {
	static class DemoPanel extends JPanel implements ChartMouseListener {

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
				System.out.println(categoryitementity.getURLText());
			}
		}

		public DemoPanel(MyBarRenderer mybarrenderer) {
			super(new BorderLayout());
			renderer = mybarrenderer;
		}
	}

	static class MyBarRenderer extends BarRenderer {

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

	public MouseOverDemo1(String s) {
		super(s);
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(jpanel);
	}

	private static CategoryDataset createDataset() {
		String s = "First";
		String s1 = "Second";
		String s2 = "Third";
		String s3 = "Category 1";
		String s4 = "Category 2";
		String s5 = "Category 3";
		String s6 = "Category 4";
		String s7 = "Category 5";
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		defaultcategorydataset.addValue(1.0D, s, s3);
		defaultcategorydataset.addValue(4D, s, s4);
		defaultcategorydataset.addValue(3D, s, s5);
		defaultcategorydataset.addValue(5D, s, s6);
		defaultcategorydataset.addValue(5D, s, s7);
		defaultcategorydataset.addValue(5D, s1, s3);
		defaultcategorydataset.addValue(7D, s1, s4);
		defaultcategorydataset.addValue(6D, s1, s5);
		defaultcategorydataset.addValue(8D, s1, s6);
		defaultcategorydataset.addValue(4D, s1, s7);
		defaultcategorydataset.addValue(4D, s2, s3);
		defaultcategorydataset.addValue(3D, s2, s4);
		defaultcategorydataset.addValue(2D, s2, s5);
		defaultcategorydataset.addValue(3D, s2, s6);
		defaultcategorydataset.addValue(6D, s2, s7);
		return defaultcategorydataset;
	}

	private static JFreeChart createChart(CategoryDataset categorydataset) {
		JFreeChart jfreechart = ChartFactory.createBarChart("Mouseover Demo 1",
				"Category", "Value", categorydataset, PlotOrientation.VERTICAL,
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
		GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue,
				0.0F, 0.0F, new Color(0, 0, 64));
		GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F,
				Color.green, 0.0F, 0.0F, new Color(0, 64, 0));
		GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, Color.red,
				0.0F, 0.0F, new Color(64, 0, 0));
		mybarrenderer.setSeriesPaint(0, gradientpaint);
		mybarrenderer.setSeriesPaint(1, gradientpaint1);
		mybarrenderer.setSeriesPaint(2, gradientpaint2);
		return jfreechart;
	}

	public static JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart(createDataset());
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		MyBarRenderer mybarrenderer = (MyBarRenderer) categoryplot
				.getRenderer();
		DemoPanel demopanel = new DemoPanel(mybarrenderer);
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.addChartMouseListener(demopanel);
		demopanel.add(chartpanel);
		return demopanel;
	}

	public static void main(String args[]) {
		MouseOverDemo1 mouseoverdemo1 = new MouseOverDemo1("Mouseover Demo 1");
		mouseoverdemo1.pack();
		RefineryUtilities.centerFrameOnScreen(mouseoverdemo1);
		mouseoverdemo1.setVisible(true);
	}
}