package cn.edu.thss.iise.xiaohan.abpcd.test.rpsdag;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cn.edu.thss.iise.xiaohan.abpcd.console.ConsoleDag;
import cn.edu.thss.iise.xiaohan.abpcd.graph.EPCHelper;
import cn.edu.thss.iise.xiaohan.abpcd.graph.rpsdag.DBSCAN;
import cn.edu.thss.iise.xiaohan.abpcd.graph.rpsdag.RPSDag;
import cn.edu.thss.iise.xiaohan.abpcd.graph.rpsdag.db.DBHelperDb;
import cn.edu.thss.iise.xiaohan.abpcd.graph.rpsdag.db.DBHelperDb.Stmts;
import cn.edu.thss.iise.xiaohan.abpcd.mtree.index.Fragment;
import cn.edu.thss.iise.xiaohan.abpcd.mtree.index.FragmentProcess;
import cn.edu.thss.iise.xiaohan.abpcd.mtree.index.Stats;
import cn.edu.thss.iise.xiaohan.abpcd.mtree.index.Timer;
import cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc.EPC;

public class RPSDagTest {

	public static void main(String[] args) throws Exception {

		int minsize = 3;
		int i = 0;

		String[] a = new String[20];
		a[0] = "-size";
		a[1] = "3"; // minsize
		a[2] = "-folder";
		a[3] = System.getProperty("user.dir") + "\\models\\ibm\\b3";

		RPSDag engine = new RPSDag();
		DBSCAN scan = new DBSCAN();

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
								new EPCHelper(epcmodel, filepath), filename,
								scan);
					} catch (Throwable e) {
						System.out.println("Problem with model " + filepath
								+ ".");
					}
				}
			}
			// ----------------------------------------------------------
			// The cluster process and test the experiment results
			clusterProcess(scan, t);
			// ----------------------------------------------------------
		} else {
			for (; i < a.length; i++) {
				try {
					EPC epcmodel = EPC.loadEPML(a[i]);
					epcmodel.cleanEPC();
					engine.addProcessModel(new EPCHelper(epcmodel, a[i]), a[i],
							scan);
				} catch (Exception e) {
					System.out.println("Problem with model " + a[i] + ".");
				}
			}
		}
		// ---------------------------------------------------------------------------
		// Test the query operations of MTree
		// testMTree();
		// ---------------------------------------------------------------------------
		// printCloneDetection(engine, minsize);
	}

	public static void clusterProcess(DBSCAN scan, Timer t) {
		System.err.println("Models Number   : " + scan.getModelsNumber());
		System.err.println("Graphs Number   : " + scan.getGraphsNumber());
		System.err.println("MapFile Number  : " + scan.getMapFileNumber());
		System.err.println("Fragments Number: " + scan.getFragmentsNumber()
				+ "\n");

		scan.createMTree();
		System.err
				.println("\nMTree has built, now print the fragments in DOT file");

		scan.printFragments();
		Timer.Times times = t.getTimes();
		System.err.println("realTime = " + times.real / 1000.0 + "\n");

		List<String> listFile = testModelType(2);
		List<Set<Fragment>> listBefore1 = scan.queryModelByRange(listFile);
		List<Set<Fragment>> listBefore2 = scan.queryModelByNumber(listFile);
		System.err.println("Cluster Beginning!");

		scan.cluster();
		times = t.getTimes();
		System.err.println("realTime = " + times.real / 1000.0 + "\n");

		List<Set<Fragment>> listAfter1 = scan.queryModelByRange(listFile);
		List<Set<Fragment>> listAfter2 = scan.queryModelByNumber(listFile);

		scan.recall(listFile, listAfter1, listBefore1);
		scan.precision(listFile, listAfter2, listBefore2);
	}

	private static List<String> testModelType(int type) {
		List<String> listFile = new ArrayList<String>();
		if (type == 1) {
			listFile = testSpecifiedModels();
			System.err.println("Test specified models, the model number is "
					+ listFile.size());
		} else if (type == 2) {
			listFile = testRandomModels();
			System.err.println("Test random models, the model number is "
					+ listFile.size());
		} else {
			System.err.println("The test model type is wrong!");
		}
		return listFile;
	}

	private static List<String> testSpecifiedModels() {
		List<String> listFile = new ArrayList<String>();
		String path = System.getProperty("user.dir") + "\\models\\ibm\\b3\\";

		int number = 10;
		String files[] = new String[number];
		files[0] = "B3.s00000031__s00001278.epml";
		files[1] = "B3.s00000021__s00001245.epml";
		files[2] = "B3.s00000025__s00001294.epml";
		files[3] = "B3.s00000027__s00001256.epml";
		files[4] = "B3.s00000029__s00001273.epml";
		files[5] = "B3.s00000019__s00001248.epml";
		files[6] = "B3.s00000033__s00001284.epml";
		files[7] = "B3.s00000035__s00001288.epml";
		files[8] = "B3.s00000041__s00001108.epml";
		files[9] = "B3.s00000043__s00001051.epml";

		for (int i = 0; i < number; i++) {
			try {
				String filepath = path + files[i];
				File file = new File(filepath);
				if (file.exists())
					listFile.add(filepath);
				else
					System.err.println("Not found: " + filepath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return listFile;
	}

	private static List<String> testRandomModels() {
		List<String> listFile = new ArrayList<String>();
		String foldername = System.getProperty("user.dir")
				+ "\\models\\ibm\\b3";
		File folder = new File(foldername);
		File[] listOfFiles = null;
		try {
			listOfFiles = folder.listFiles();
		} catch (Exception e) {
			System.out.println("Invalid input: folder " + foldername + ".");
		}

		int number = 20;
		Set<Integer> setRdm = new HashSet<Integer>();
		Random rdm = new Random(System.currentTimeMillis());
		for (int i = 0; i < number; i++) {
			int rd = Math.abs(rdm.nextInt()) % listOfFiles.length;
			if (!setRdm.contains(rdm)) {
				String filepath = listOfFiles[rd].getAbsolutePath();
				FragmentProcess process = new FragmentProcess(filepath);
				if (process.fragment.getGraph().getVertices().size() >= 6) {
					setRdm.add(rd);
					listFile.add(filepath);
					System.out.println("Number " + i + " "
							+ listOfFiles[rd].getName());
				} else
					i--;
			} else
				i--;
		}
		System.out.println();
		return listFile;
	}

	public static void testMTree() {
		Stats stats = new Stats();
		String name = "\\models\\testmtree\\B3.s00000031__s00001278.epml";
		String filepath = System.getProperty("user.dir") + name;

		FragmentProcess query = new FragmentProcess(filepath);

		System.out.println('\n' + "Query operation test...  " + query.filename);

		List<Fragment> fragments = stats.getNearest(query.fragment, 6, 10);

		for (int i = 0; i < fragments.size(); i++) {
			Fragment fg = fragments.get(i);
			if (fg.isOriginGraph())
				System.out.print("Origin graph! ");
			else
				System.out.print("Isn't origin! ");
			System.out.println("The fragment number is " + fg.ID);

			for (String file : fg.getModelFiles()) {
				System.out.println(file);
			}
		}
	}

	public static void printCloneDetection(RPSDag engine, int minsize) {
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
			int temp = 1;
			while (rs.next()) {
				int cloneid = rs.getInt(1);
				System.out.println();
				System.out.print(cloneid + "\t\t" + rs.getLong(2) + "\t\t"
						+ rs.getLong(3) + "\t");
				ConsoleDag cd = new ConsoleDag(engine.getDbHelper());
				temp++;
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
			System.err.println("\n" + temp);

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
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
