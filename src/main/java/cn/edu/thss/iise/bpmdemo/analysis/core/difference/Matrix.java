package cn.edu.thss.iise.bpmdemo.analysis.core.difference;

import java.util.ArrayList;

public class Matrix {

	/**
	 * @author �ν��
	 */
	int n;
	public ArrayList<String> names = new ArrayList<String>();

	public ArrayList<String> getNames() {
		return names;
	}

	public void setNames(ArrayList<String> names) {
		this.names = names;
	}

	public String[][] matrix = new String[n][n];

	public Matrix() {
		matrix = new String[n][n];
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public String[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(String[][] matrix) {
		this.matrix = matrix;
	}

}
