package cn.edu.thss.iise.xiaohan.bpcd.test.rpsdag;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import cn.edu.thss.iise.xiaohan.bpcd.console.ConsoleDag;
import cn.edu.thss.iise.xiaohan.bpcd.graph.EPCHelper;
import cn.edu.thss.iise.xiaohan.bpcd.graph.rpsdag.RPSDag;
import cn.edu.thss.iise.xiaohan.bpcd.graph.rpsdag.db.DBHelperDb;
import cn.edu.thss.iise.xiaohan.bpcd.graph.rpsdag.db.DBHelperDb.Stmts;
import cn.edu.thss.iise.xiaohan.bpcd.mtree.index.Fragment;
import cn.edu.thss.iise.xiaohan.bpcd.mtree.index.FragmentProcess;
import cn.edu.thss.iise.xiaohan.bpcd.mtree.index.Stats;
import cn.edu.thss.iise.xiaohan.bpcd.mtree.index.Timer;
import cn.edu.thss.iise.xiaohan.bpcd.nl.tue.tm.is.epc.EPC;

public class RPSDagTest {

	public static void main(String[] args) throws Exception {

		int minsize = 4;
		int i = 0;

		String[] a = new String[20];
		a[0] = "-size";
		a[1] = "4";
		a[2] = "-folder";
		a[3] = System.getProperty("user.dir") + "\\models\\ibm\\b3";

		RPSDag engine = new RPSDag();
		if (a.length == 0 || (a.length == 1 && "-help".equals(a[0]))) {
			printHelp();
			return;
		}

		if (a[0].equals("-size")) {
			i = 2;
			try {
				minsize = Integer.parseInt(a[1]);
			} catch (Exception e) {
				System.out.println("Invalid input: size " + a[1]
						+ ". Using default, 4 for minimal clone size.");
			}
		}

		if (a[i].equals("-folder")) {
			i++;
			String foldername = a[i];
			File folder = new File(foldername);
			File[] listOfFiles = null;
			try {
				listOfFiles = folder.listFiles();
			} catch (Exception e) {
				System.out.println("Invalid input: folder " + a[i] + ".");
				return;
			}

			if (foldername.charAt(foldername.length() - 1) != '/') {
				foldername += "/";
			}

			Timer t = new Timer();
			for (int k = 0; k < listOfFiles.length; k++) {
				if (listOfFiles[i].isFile()) {
					String filepath = foldername + listOfFiles[k].getName();
					String filename = listOfFiles[k].getName();
					try {
						EPC epcmodel = EPC.loadEPML(filepath);
						epcmodel.cleanEPC();
						engine.addProcessModel(
								new EPCHelper(epcmodel, filepath), filename);
					} catch (Throwable e) {
						// ----------------------------------------------------------
						System.out.println("Problem with model " + filepath
								+ ".");
					}
				}
			}
			Timer.Times times = t.getTimes();
			System.err.println('\n' + "realTime = " + times.real / 1000.0);
		} else {
			for (; i < a.length; i++) {
				try {
					EPC epcmodel = EPC.loadEPML(a[i]);
					epcmodel.cleanEPC();
					engine.addProcessModel(new EPCHelper(epcmodel, a[i]), a[i]);
				} catch (Exception e) {
					System.out.println("Problem with model " + a[i] + ".");
				}
			}
		}
		// ---------------------------------------------------------------------------
		// Test the query operations of MTree
		testMTree();
		// ---------------------------------------------------------------------------

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DBHelperDb.connect();

			stmt = conn.prepareStatement(Stmts.GetLargestLocalClones.getSql());
			stmt.setInt(1, minsize);
			stmt.setInt(2, 2);
			rs = stmt.executeQuery();

			System.out
					.println('\n' + "cloneID \t clone size \t nrparents \t models");
			while (rs.next()) {
				int cloneid = rs.getInt(1);
				System.out.println();
				System.out.print(cloneid + "\t\t" + rs.getLong(2) + "\t\t"
						+ rs.getLong(3) + "\t");
				ConsoleDag cd = new ConsoleDag(engine.getDbHelper());

				HashSet<String> models = cd.getModels(rs.getInt(1));

				boolean printed = false;
				HashSet<String> printedModels = new HashSet<String>();
				for (String m : models) {
					if (!printedModels.contains(m)) {
						System.out.print("\n" + m + "  ");
						printedModels.add(m);
					}
					if (!printed) {
						EPC epcmodel = EPC.loadEPML(m);
						epcmodel.cleanEPC();
						printed = engine.printProcessModelClone(new EPCHelper(
								epcmodel), cloneid);
					}
				}

				System.out.print("\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				conn.close();
			}
		}

	}

	private static void testMTree() {
		Stats stats = new Stats();
		String name = "\\models\\testmtree\\B3.s00000473__s00003626.epml";
		String filepath = System.getProperty("user.dir") + name;

		FragmentProcess query = new FragmentProcess(filepath);

		System.out.println('\n' + "Query operation test...  " + query.filename);

		List<Fragment> fragments = stats.getNearest(query.fragment, 20, 10);

		for (int i = 0; i < fragments.size(); i++) {
			System.out.println("The fragment number is " + i);
			Fragment fg = fragments.get(i);

			for (String file : fg.modelfiles) {
				System.out.println(file);
			}
		}
	}

	private static void printHelp() {
		System.out
				.println("USAGE: \n"
						+ "ProcessRefactoring [-size min_size] -folder model_folder \n"
						+ "OR \n"
						+ "ProcessRefactoring [-size min_size] modelname1 modelname2 modelnamen \n"
						+ "-size - the size of minimal clone represented in the results (the result clone "
						+ "fragments are printed in the fragments/ folder).\n"
						+ "-folder - the path of the folder that contains models that are inserted to the dag (from the project path). "
						+ "If folder name is not given, the model names must be added."
						+ ".\n");
	}
}
