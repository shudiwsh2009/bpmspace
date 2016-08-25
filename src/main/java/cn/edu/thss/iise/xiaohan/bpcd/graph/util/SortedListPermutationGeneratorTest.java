package cn.edu.thss.iise.xiaohan.bpcd.graph.util;

import java.util.LinkedList;

import junit.framework.TestCase;
import cn.edu.thss.iise.xiaohan.bpcd.graph.QueueEntry;

public class SortedListPermutationGeneratorTest extends TestCase {
	public void testPermutations() {
		LinkedList<QueueEntry> list = new LinkedList<QueueEntry>();
		// list.add(new QueueEntry(0, "a", "a"));
		// list.add(new QueueEntry(1, "a", "a"));
		// list.add(new QueueEntry(2, "b", "b"));
		// list.add(new QueueEntry(3, "c", "c"));
		// list.add(new QueueEntry(4, "c", "c"));
		// list.add(new QueueEntry(5, "c", "c"));
		// list.add(new QueueEntry(6, "f", "f"));
		// list.add(new QueueEntry(7, "f"));
		// list.add(new QueueEntry(8, "g"));

		SortedListPermutationGenerator gen = new SortedListPermutationGenerator(
				list);

		while (gen.hasMoreConbinations()) {
			LinkedList<QueueEntry> perm = gen.getNextCombination();
			System.out.print("Permutation: ");
			for (QueueEntry i : perm)
				System.out.print(i);

			System.out.println();
		}
	}
}
