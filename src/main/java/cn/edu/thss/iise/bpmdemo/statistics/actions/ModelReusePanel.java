package cn.edu.thss.iise.bpmdemo.statistics.actions;

/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * ------------------
 * PieChartDemo4.java
 * ------------------
 * (C) Copyright 2003, 2004, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: PieChartDemo4.java,v 1.21 2004/06/03 15:05:10 mungady Exp $
 *
 * Changes
 * -------
 * 11-Feb-2003 : Version 1 (DG);
 *
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieItemLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;
import org.jfree.util.PrintStreamLogTarget;

/**
 * A simple demonstration application showing how to create a pie chart using
 * data from a {@link DefaultPieDataset}. This chart has a lot of labels and
 * rotates, so it is useful for testing the label distribution algorithm.
 */
public class ModelReusePanel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String filePath = null;
	private DefaultPieDataset data1 = null;
	private String[][] data2 = null;

	/**
	 * Default constructor.
	 *
	 * @param title
	 *            the frame title.
	 * @throws IOException
	 */
	public ModelReusePanel(final String title, String filePath)
			throws IOException {

		super(title);
		this.filePath = filePath;

		JPanel panel = createPanel();
		setContentPane(panel);

	}

	private JPanel createPanel() throws IOException {
		createDataSet();

		// create the chart...
		final JFreeChart chart = ChartFactory.createPieChart("Reused Times", // chart
																				// title
				data1, // dataset
				false, // include legend
				true, false);

		// set the background color for the chart...
		chart.setBackgroundPaint(new Color(222, 222, 255));

		final PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setCircular(true);
		plot.setLabelGenerator(new StandardPieItemLabelGenerator("{0} = {2}",
				NumberFormat.getNumberInstance(), NumberFormat
						.getPercentInstance()));
		plot.setNoDataMessage("No data available");

		// add the chart to a panel...
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

		String[] columnNames = { "Name", "Reuse Times" };
		JTable table = new JTable(data2, columnNames);
		JPanel panel = new JPanel();
		panel.add(chartPanel);
		panel.add(table);
		final Rotator rotator = new Rotator(plot);
		rotator.start();
		return panel;
	}

	private void createDataSet() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				this.filePath)));
		String line = null;
		String name = null;
		int number = 0;
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		String[][] tempS = new String[100][2];
		int lineNo = 0;

		while ((line = reader.readLine()) != null) {
			String[] sg = line.split(",");
			name = sg[0];
			number = Integer.parseInt(sg[1]);
			tempS[lineNo][0] = name;
			tempS[lineNo][1] = sg[1];
			// ͳ��ʹ�ô����ı���
			if (map.containsKey(number)) {
				String temp = map.get(number);
				map.remove(number);
				map.put(number, temp + ", " + name);
			} else {
				map.put(number, name);
			}
			lineNo++;
		}

		// load pie data
		data1 = new DefaultPieDataset();
		Set<Integer> keys = map.keySet();
		for (Integer key : keys) {
			data1.setValue(key + " times: " + map.get(key), key);
		}

		// load table data
		data2 = new String[lineNo + 1][2];

		data2[0][0] = "Name";
		data2[0][1] = "Reuse Times";
		for (int i = 0; i < lineNo; i++) {
			data2[i + 1] = tempS[i];
		}

		reader.close();
	}

	/**
	 * The rotator.
	 *
	 */
	static class Rotator extends Timer implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/** The plot. */
		private PiePlot plot;

		/** The angle. */
		private int angle = 270;

		/**
		 * Constructor.
		 *
		 * @param plot
		 *            the plot.
		 */
		Rotator(final PiePlot plot) {
			super(100, null);
			this.plot = plot;
			addActionListener(this);
		}

		/**
		 * Modifies the starting angle.
		 *
		 * @param event
		 *            the action event.
		 */
		public void actionPerformed(final ActionEvent event) {
			this.plot.setStartAngle(angle);
			this.angle = this.angle + 1;
			if (this.angle == 360) {
				this.angle = 0;
			}
		}

	}

	/**
	 * Starting point for the demonstration application.
	 *
	 * @param args
	 *            ignored.
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		Log.getInstance().addTarget(new PrintStreamLogTarget());
		final ModelReusePanel demo = new ModelReusePanel(
				"Pie Chart Demo 4",
				"C:\\Users\\chenhz\\Documents\\Thss SVN\\THSS JBPM\\log(new)��BPMN�ļ�\\BPMN2.0�ļ�\\���\\fragment.sta");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}

}
