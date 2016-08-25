package com.chinamobile.bpmspace.core.repository.socialnetworkmining;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.processmining.framework.log.LogReader;
import org.processmining.mining.MiningResult;
import org.processmining.mining.fuzzymining.vis.DotTools;
import org.processmining.mining.snamining.model.SocialNetworkMatrix;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;

public class newSocialNetworkResults implements MiningResult {
	private LogReader log;

	private SocialNetworkMatrix snMatrix;
	private DoubleMatrix2D originalMatrix = null;

	private ResultTableModel dataMatrix;

	private double dMaxValue;
	private double dMinValue;

	public newSocialNetworkResults(LogReader log, SocialNetworkMatrix snMatrix) {
		this.log = log;
		this.snMatrix = snMatrix;

		initOriginalMatrix();
	}

	public void initOriginalMatrix() {
		dMaxValue = snMatrix.getMaxValue();
		dMinValue = snMatrix.getMinValue();
		originalMatrix = null;
		originalMatrix = DoubleFactory2D.sparse.make(
				snMatrix.getNodeNames().length, snMatrix.getNodeNames().length,
				0);
		originalMatrix.assign(snMatrix.getMatrix());
	}

	// start mining
	public void adjustMatrix(double adjustment) {
		DoubleMatrix2D tempMatrix = DoubleFactory2D.sparse.make(
				snMatrix.getNodeNames().length, snMatrix.getNodeNames().length,
				0);
		tempMatrix.assign(originalMatrix.copy());
		snMatrix.setMatrix(tempMatrix);
		getSNMatrix().applyThresholdValue(
				Double.valueOf(getThresholdFromSlider(adjustment)));
		removeIsolatedNodes();

		// getGraph(snMatrix.getNodeNames(), snMatrix.getMatrix());
	}

	public void removeIsolatedNodes() {
		getSNMatrix().removeDisconnectedOriginator();
		initOriginalMatrix();
	}

	protected double getThresholdFromSlider(double threshold) {
		// normalize threshold to minimal node frequency
		threshold = (dMaxValue - dMinValue) * threshold + dMinValue;
		return threshold;
	}

	public void export2png(String imagePath) {
		// get output image path, if no folder exists, create it
		String outputImageFolder = imagePath.substring(0,
				imagePath.lastIndexOf(File.separator));
		File dir = new File(outputImageFolder);
		dir.mkdirs();
		File outImage = new File(imagePath);

		try {
			OutputStream image = new FileOutputStream(outImage);
			StringWriter sWriter = new StringWriter();
			getGraph(snMatrix.getNodeNames(), snMatrix.getMatrix(), sWriter);
			sWriter.close();
			BufferedImage bImage = (BufferedImage) (new DotTools())
					.renderImage(sWriter.toString());
			ImageIO.write(bImage, "PNG", image);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getGraph(String[] users, DoubleMatrix2D matrix, StringWriter sw) {
		NumberFormat nf = NumberFormat.getInstance();

		nf.setMinimumFractionDigits(3);
		nf.setMaximumFractionDigits(3);
		try {

			sw.write("digraph G {ranksep=\".3\"; fontsize=\"8\"; remincross=true; margin=\"0.0,0.0\"; ");
			sw.write("fontname=\"Arial\";rankdir=\"LR\"; \n");
			sw.write("edge [arrowsize=\"0.5\",decorate=true,fontname=\"Arial\",fontsize=\"8\"];\n");
			sw.write("node [height=\".1\",width=\".2\",fontname=\"Arial\",fontsize=\"8\"];\n");

			for (int i = 0; i < users.length; i++) {
				sw.write("t" + i + " [shape=\"box\",label=\"" + users[i]
						+ "\"];\n");
			}

			for (int i = 0; i < matrix.rows(); i++) {
				for (int j = 0; j < matrix.columns(); j++) {
					double value = matrix.get(i, j);

					if (value > 0) {
						sw.write("t" + i + " -> t" + j + " [label=\""
								+ nf.format(value) + "\"];\n");
					}
				}
			}
			sw.write("}\n");
			sw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public ResultTableModel getResult() {
		dataMatrix = new ResultTableModel(snMatrix.getNodeNames(),
				snMatrix.getMatrix());
		return dataMatrix;
	}

	public LogReader getLogReader() {
		return log;
	}

	public JComponent getVisualization() {
		return null;
	}

	public SocialNetworkMatrix getSNMatrix() {
		return snMatrix;
	}
}

class ResultTableModel {

	private Object[][] data;
	private String[] users;

	public ResultTableModel(String[] users, DoubleMatrix2D data) {
		this.users = new String[users.length + 1];
		this.data = new Object[data.rows()][data.columns() + 1];

		this.users[0] = "";

		for (int i = 0; i < data.rows(); i++) {
			this.users[i + 1] = users[i];
			this.data[i][0] = users[i];
			for (int j = 0; j < data.columns(); j++)
				this.data[i][j + 1] = data.get(i, j);
		}

	}

	public String getColumnName(int col) {
		return users[col];
	}

	public int getRowCount() {
		return data.length;
	}

	public int getColumnCount() {
		return users.length;
	}

	public Object getValueAt(int row, int column) {
		return data[row][column];
	}
}
