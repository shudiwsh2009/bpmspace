package cn.edu.thss.iise.bpmdemo.analysis.core.difference;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * @author �ν��
 */
public class QMC {

	public static void main(String[] args) {

		// check the number of input argumetns
		// System.out.println(args[0]);
		/*
		 * if(args.length != 2) {
		 * System.out.println("Usage: qmc inputFile outputFile");
		 * System.exit(0); }
		 */

		try {
			// create a new object
			QMC qmc = new QMC();
			// create new object for QuineMcCluskey
			QuineMcCluskey q = new QuineMcCluskey();
			// read input file into the class
			qmc.readInputFile(q);
			// perform the required minimization
			q.simplify();
			// write the output file
			qmc.writeOutputFile(q);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// read input file into QMC object to start solving
	// The input file contains a line per input minterm
	// 0-> for logic 0, and 1 for logic 1
	private void readInputFile(QuineMcCluskey qmc) {

		try {
			// file reader
			BufferedReader inFile = new BufferedReader(new FileReader(
					"D:/t1.txt"));
			// string to hold line input
			String line;
			// read the file input
			while ((line = inFile.readLine()) != null) {
				// add the line minterm into the QuineMcCluskey object
				qmc.addTerm(line);
			}
			// Always close a stream when you are done with
			inFile.close();
		} catch (Exception e) {
			// Handle FileNotFoundException,etc.
			System.out.println(e.getMessage());
		}
	}

	// write result of minimizing into output file
	// The output file contains a line per output minterms
	// 0-> for logic 0, and 1 for logic 1, and X for dont care
	public void writeOutputFile(QuineMcCluskey qmc) {

		try {
			// file reader
			PrintWriter outFile = new PrintWriter(new FileWriter("D:/t1o.txt",
					true));
			// string to hold output line
			String line;
			// write the output file term by term
			for (int i = 0; i < qmc.count; i++)
				outFile.println(qmc.getTerm(i));
			// Always close a stream when you are done with
			outFile.close();
		} catch (Exception e) {
			// Handle FileNotFoundException,etc.
			System.out.println(e.getMessage());
		}
	}

}
