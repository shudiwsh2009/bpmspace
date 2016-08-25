package cn.edu.thss.iise.xiaohan.abpcd.graph.util;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

import cn.edu.thss.iise.xiaohan.abpcd.graph.MatrixQueueEntry;
import cn.edu.thss.iise.xiaohan.abpcd.graph.QueueEntry;

public class SortedListPermutationGenerator {

	LinkedList<QueueEntry> elements;
	Stack<ElementPermutationGenerator<QueueEntry>> currentPermutation = new Stack<ElementPermutationGenerator<QueueEntry>>();
	Stack<ElementPermutationGenerator<QueueEntry>> tmp = new Stack<ElementPermutationGenerator<QueueEntry>>();
	private boolean hasMoreCombinations = true;
	private BigInteger nrCombinations = BigInteger.ONE;

	public BigInteger getNrCombinations() {
		return nrCombinations;
	}

	public SortedListPermutationGenerator(LinkedList<QueueEntry> elements) {
		LinkedList<QueueEntry> current = new LinkedList<QueueEntry>();
		QueueEntry tmp = null;

		for (QueueEntry entry : elements) {

			// first element
			if (tmp == null) {
				tmp = entry;
				current.add(tmp);
				continue;
			}
			// this is from the same set
			if (entry.getLabel().equals(tmp.getLabel())) {
				current.add(entry);
			} else {
				// we have a multiple gateways of the same type
				if (current.size() > 1
						&& current.get(0).getMatrixLabel() != null) {

					PriorityQueue<MatrixQueueEntry> entries = new PriorityQueue<MatrixQueueEntry>();

					for (QueueEntry q : current) {
						entries.add(new MatrixQueueEntry(q.getVertex(), q
								.getMatrixLabel(), q.getLabel(), q
								.getNodeSize()));
					}

					LinkedList<MatrixQueueEntry> sortedLabels = new LinkedList<MatrixQueueEntry>();

					while (!entries.isEmpty()) {
						sortedLabels.add(entries.poll());
					}

					parseSortedGws(sortedLabels);

				} else {
					ElementPermutationGenerator<QueueEntry> toAdd = new ElementPermutationGenerator<QueueEntry>(
							current);
					nrCombinations = nrCombinations.multiply(toAdd.getTotal());
					currentPermutation.push(toAdd);
				}
				tmp = entry;
				current = new LinkedList<QueueEntry>();
				current.add(tmp);
			}
		}
		if (current.size() > 0) {
			ElementPermutationGenerator<QueueEntry> toAdd = new ElementPermutationGenerator<QueueEntry>(
					current);
			nrCombinations = nrCombinations.multiply(toAdd.getTotal());
			currentPermutation.push(toAdd);
		}
	}

	public void parseSortedGws(LinkedList<MatrixQueueEntry> elements) {
		LinkedList<QueueEntry> current = new LinkedList<QueueEntry>();
		QueueEntry tmp = null;

		for (QueueEntry entry : elements) {
			// first element
			if (tmp == null) {
				tmp = entry;
				current.add(tmp);
				continue;
			}
			// this is from the same set
			if (entry.getMatrixLabel().equals(tmp.getMatrixLabel())) {
				current.add(entry);
			} else {
				// System.out.println("<><> ADDED GENERATOR " + current.size());
				ElementPermutationGenerator<QueueEntry> toAdd = new ElementPermutationGenerator<QueueEntry>(
						current);
				nrCombinations = nrCombinations.multiply(toAdd.getTotal());
				currentPermutation.push(toAdd);
				tmp = entry;
				current = new LinkedList<QueueEntry>();
				current.add(tmp);
			}
		}
		if (current.size() > 0) {
			ElementPermutationGenerator<QueueEntry> toAdd = new ElementPermutationGenerator<QueueEntry>(
					current);
			nrCombinations = nrCombinations.multiply(toAdd.getTotal());
			currentPermutation.push(toAdd);
		}
	}

	public LinkedList<QueueEntry> getNextCombination() {
		LinkedList<QueueEntry> combination = new LinkedList<QueueEntry>();

		for (int i = 0; i < currentPermutation.size(); i++) {
			ElementPermutationGenerator<QueueEntry> permutation = currentPermutation
					.elementAt(i);
			combination.addAll(permutation.getCurrentCombination());
		}

		while (!currentPermutation.isEmpty()) {
			ElementPermutationGenerator<QueueEntry> permutation = currentPermutation
					.pop();
			// there is no more permutation for this element
			if (!permutation.hasMore()) {
				tmp.push(permutation);
			}
			// put the next permutation of this element
			else {
				permutation.setNext();
				currentPermutation.push(permutation);
				while (!tmp.isEmpty()) {
					ElementPermutationGenerator<QueueEntry> tmpPerm = tmp.pop();
					tmpPerm.reset();
					currentPermutation.push(tmpPerm);
				}
				break;
			}
		}
		if (currentPermutation.size() == 0) {
			hasMoreCombinations = false;
		}
		return combination;
	}

	public boolean hasMoreConbinations() {
		return hasMoreCombinations;
	}

}
