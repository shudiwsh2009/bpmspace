package com.chinamobile.bpmspace.core.repository.index.labelindex;

public class SimilarLabelQueryResult implements
		Comparable<SimilarLabelQueryResult> {

	private String label;
	private float similarity = 1;

	public SimilarLabelQueryResult(String label, float similarity) {
		this.label = label;
		this.similarity = similarity;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the similarity
	 */
	public float getSimilarity() {
		return similarity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SimilarLabelQueryResult) {
			SimilarLabelQueryResult temp = (SimilarLabelQueryResult) obj;
			if (this.label.equals(temp.label)
					&& this.similarity == temp.similarity) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(label =" + this.label + " , similarity = " + this.similarity
				+ ")";
	}

	@Override
	public int compareTo(SimilarLabelQueryResult o) {
		if (this.similarity > o.similarity) {
			return 1;
		} else if (this.similarity < o.similarity) {
			return -1;
		} else {
			return this.label.compareTo(o.label);
		}
	}
}
