package cn.edu.thss.iise.xiaohan.bpcd.graph.util;

import java.math.BigInteger;
import java.util.LinkedList;

public class ElementPermutationGenerator<E extends Object> {

	private int[] a;
	private LinkedList<E> originalList;
	private BigInteger numLeft;
	private BigInteger total;

	// -----------------------------------------------------------
	// Constructor. WARNING: Don't make n too large.
	// Recall that the number of permutations is n!
	// which can be very large, even when n is as small as 20 --
	// 20! = 2,432,902,008,176,640,000 and
	// 21! is too big to fit into a Java long, which is
	// why we use BigInteger instead.
	// ----------------------------------------------------------

	public ElementPermutationGenerator(LinkedList<E> list) {
		int n = list.size();
		if (n < 1) {
			throw new IllegalArgumentException("Min 1");
		}
		originalList = new LinkedList<E>();
		for (int i = 0; i < n; i++) {
			originalList.add(list.get(i));
		}
		a = new int[n];
		total = getFactorial(n);
		reset();
	}

	// ------
	// Reset
	// ------

	public void reset() {
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		numLeft = new BigInteger(total.toString()).subtract(BigInteger.ONE);
	}

	// ------------------------------------------------
	// Return number of permutations not yet generated
	// ------------------------------------------------

	public BigInteger getNumLeft() {
		return numLeft;
	}

	// ------------------------------------
	// Return total number of permutations
	// ------------------------------------

	public BigInteger getTotal() {
		return total;
	}

	// -----------------------------
	// Are there more permutations?
	// -----------------------------

	public boolean hasMore() {
		return numLeft.compareTo(BigInteger.ZERO) == 1;
	}

	// ------------------
	// Compute factorial
	// ------------------

	private static BigInteger getFactorial(int n) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--) {
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		}
		return fact;
	}

	public LinkedList<E> getCurrentCombination() {
		LinkedList<E> result = new LinkedList<E>();

		for (int i = 0; i < a.length; i++) {
			result.add(originalList.get(a[i]));
		}
		return result;
	}

	// --------------------------------------------------------
	// Generate next permutation (algorithm from Rosen p. 284)
	// --------------------------------------------------------

	// public LinkedList<E> getNext() {
	//
	// if (numLeft.equals(total)) {
	// numLeft = numLeft.subtract(BigInteger.ONE);
	// return getCurrentCombination();
	// }
	//
	// int temp;
	//
	// // Find largest index j with a[j] < a[j+1]
	//
	// int j = a.length - 2;
	// while (a[j] > a[j + 1]) {
	// j--;
	// }
	//
	// // Find index k such that a[k] is smallest integer
	// // greater than a[j] to the right of a[j]
	//
	// int k = a.length - 1;
	// while (a[j] > a[k]) {
	// k--;
	// }
	//
	// // Interchange a[j] and a[k]
	//
	// temp = a[k];
	// a[k] = a[j];
	// a[j] = temp;
	//
	// // Put tail end of permutation after jth position in increasing order
	//
	// int r = a.length - 1;
	// int s = j + 1;
	//
	// while (r > s) {
	// temp = a[s];
	// a[s] = a[r];
	// a[r] = temp;
	// r--;
	// s++;
	// }
	//
	// numLeft = numLeft.subtract(BigInteger.ONE);
	// return getCurrentCombination();
	//
	// }

	public void setNext() {

		// if (numLeft.equals(total)) {
		// numLeft = numLeft.subtract(BigInteger.ONE);
		// }

		int temp;

		// Find largest index j with a[j] < a[j+1]
		int j = a.length - 2;
		while (a[j] > a[j + 1]) {
			j--;
		}

		// Find index k such that a[k] is smallest integer
		// greater than a[j] to the right of a[j]
		int k = a.length - 1;
		while (a[j] > a[k]) {
			k--;
		}

		// Interchange a[j] and a[k]

		temp = a[k];
		a[k] = a[j];
		a[j] = temp;

		// Put tail end of permutation after jth position in increasing order
		int r = a.length - 1;
		int s = j + 1;

		while (r > s) {
			temp = a[s];
			a[s] = a[r];
			a[r] = temp;
			r--;
			s++;
		}

		numLeft = numLeft.subtract(BigInteger.ONE);
	}

}