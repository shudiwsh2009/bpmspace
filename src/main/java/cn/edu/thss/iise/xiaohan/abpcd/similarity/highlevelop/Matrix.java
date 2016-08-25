package cn.edu.thss.iise.xiaohan.abpcd.similarity.highlevelop;

import java.util.ArrayList;

public class Matrix {

	/**
	 * @author
	 */
	int n;
	public ArrayList<String> names = new ArrayList<String>();
	public String[][] matrix = new String[n][n];

	public Matrix() {
		matrix = new String[n][n];
	}

	public ArrayList<String> getNames() {
		return names;
	}

	public void setNames(ArrayList<String> names) {
		this.names = names;
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
