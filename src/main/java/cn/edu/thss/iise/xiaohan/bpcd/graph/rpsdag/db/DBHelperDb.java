package cn.edu.thss.iise.xiaohan.bpcd.graph.rpsdag.db;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import cn.edu.thss.iise.xiaohan.bpcd.lcs.HashLCS;
import cn.edu.thss.iise.xiaohan.bpcd.lcs.HashString;
import de.bpt.hpi.graph.Pair;

public class DBHelperDb {

	static Connection con;
	private HashMap<String, Pair> labelHash = new HashMap<String, Pair>();
	public Map<String, Integer> gwMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> roots = new HashMap<String, Integer>();
	private HashMap<Integer, LinkedList<Integer>> parentChildMap = new HashMap<Integer, LinkedList<Integer>>();
	private HashMap<Integer, LinkedList<Integer>> deletedParentChildMap = new HashMap<Integer, LinkedList<Integer>>();

	// the data structures that hold not yet committed elements
	private HashMap<String, Pair> labelHashTmp = new HashMap<String, Pair>();
	private HashMap<Integer, ObjectPair<Integer, String>> nodeSizeMap = new HashMap<Integer, ObjectPair<Integer, String>>();
	private HashMap<Integer, LinkedList<Integer>> parentChildMapTmp = new HashMap<Integer, LinkedList<Integer>>();

	public enum Stmts {
		InsertHash("INSERT INTO labelhash (hash, size) VALUES (?, ?)"), InsertParentChild(
				"INSERT INTO parentchildmap (parent, child) VALUES(?, ?)"), RemoveParentChild(
				"DELETE FROM parentchildmap WHERE parent = ? AND child = ?"), InsertRootnode(
				"INSERT INTO roots (modelname, nodeid) VALUES(?, ?)"), GetHashCodes(
				"SELECT * FROM labelhash"), GetParentChild(
				"SELECT * FROM parentchildmap ORDER BY parent;"), GetRoots(
				"SELECT * FROM roots"), GetLargestLocalClones(
				"select pcm1.child, lh.size, count(pcm1.parent) from parentchildmap pcm1,"
						+ " labelhash lh where "
						+ "lh.nodeid = pcm1.child and lh.size >= ? "
						+ "group by pcm1.child, lh.size having count(pcm1.parent) >= ?"), ;

		private String sql;

		private Stmts(String sql) {
			this.sql = sql;
		}

		public String getSql() {
			return sql;
		}
	}

	public boolean isGateway(String id) {
		return gwMap.containsKey(id);
	}

	public boolean isGateway(Integer id) {
		return gwMap.values().contains(id);
	}

	public boolean isEntryOrExitNode(Integer id) {
		if (nodeSizeMap.get(id) == null) {
			return false;
		}
		String hash = nodeSizeMap.get(id).getSecond();
		if (hash.equalsIgnoreCase("_entry_") || hash.equalsIgnoreCase("_exit_")
				|| hash.equalsIgnoreCase("entry_node")
				|| hash.equalsIgnoreCase("exit_node")
				|| hash.equalsIgnoreCase("s00001057")
				|| hash.equalsIgnoreCase("s00001056")) {
			return true;
		}
		return false;
	}

	public class JarFileLoader extends URLClassLoader {
		public JarFileLoader(URL[] urls) {
			super(urls);
		}

		public void addFile(String path) throws MalformedURLException {
			String urlPath = "jar:file:/" + path + "!/";
			addURL(new URL(urlPath));
		}
	}

	public DBHelperDb(String db, String user, String passw) {
		try {
			URL urls[] = {};
			JarFileLoader cl = new JarFileLoader(urls);
			cl.addFile("lib/hsqldb.jar");

			Class.forName("org.hsqldb.jdbcDriver");
			con = DriverManager.getConnection(
					"jdbc:hsqldb:file:data/db;shutdown=true", user, passw);

			loadDataFromDB();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static Connection connect() throws Exception {
		if (con == null || con.isClosed()) {
			con = DriverManager.getConnection("jdbc:hsqldb:file:data/db",
					"refactor", "refactor");
		}
		return con;
	}

	public Integer addModelHash(String hash, int nodesize) {
		return addModelHash(hash, nodesize, true, null);
	}

	public Integer addModelHash(String hash, int nodesize, boolean usetimer) {
		return addModelHash(hash, nodesize, usetimer, null);
	}

	public Integer addModelHash(String hash, int nodesize,
			LinkedList<Integer> children) {
		return addModelHash(hash, nodesize, true, children);
	}

	public Integer addModelHash(String hash, int nodesize, boolean usetimer,
			LinkedList<Integer> children) {
		Integer result = -1;

		if (labelHash.containsKey(hash)) {
			return labelHash.get(hash).getFirst();
		}

		if (labelHashTmp.containsKey(hash)) {
			return labelHashTmp.get(hash).getFirst();
		}

		PreparedStatement stmt = null;
		PreparedStatement stmtId = null;
		ResultSet rs = null;
		try {
			Connection conn;

			conn = connect();

			stmt = conn.prepareStatement(Stmts.InsertHash.getSql());
			stmt.setString(1, hash);
			stmt.setInt(2, nodesize);
			stmt.execute();

			stmtId = conn.prepareStatement("CALL IDENTITY()");
			rs = stmtId.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			} else {
				// throw an exception from here
			}
			if (usetimer) {
				labelHashTmp.put(hash, new Pair(result, nodesize));
				nodeSizeMap.put(result, new ObjectPair<Integer, String>(
						nodesize, hash));
			} else {
				labelHash.put(hash, new Pair(result, nodesize));
				nodeSizeMap.put(result, new ObjectPair<Integer, String>(
						nodesize, hash));
			}

			if (nodesize > 1 && children != null) {
				getSubstringSuperstringRelations(hash, result, children);
			}
			return result;

		} catch (Exception e) {
			try {
				rollbackTransaction();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return -1;
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
		}
	}

	public Integer getnodeIdForHash(String hash) {
		if (labelHash.containsKey(hash)) {
			return labelHash.get(hash).getFirst();
		}

		if (labelHashTmp.containsKey(hash)) {
			return labelHashTmp.get(hash).getFirst();
		}
		return -1;
	}

	private boolean hasParentChildRelation(Integer parent, Integer child) {

		// parentchildmap contains relation
		LinkedList<Integer> children = parentChildMap.get(parent);
		if (children != null && children.contains(child)) {
			return true;
		}

		// tmp parenchildmap contains relation
		children = parentChildMapTmp.get(parent);
		if (children != null && children.contains(child)) {
			return true;
		}

		return false;
	}

	private boolean isRemovedParentChildRelation(Integer parent, Integer child) {

		LinkedList<Integer> children = deletedParentChildMap.get(parent);
		if (children != null && children.contains(child)) {
			return true;
		}
		return false;
	}

	private void deleteChildrenFromParentChildMap(Integer parent,
			Integer childrenRem) {
		LinkedList<Integer> children;
		// add relations to the tmp map
		children = parentChildMapTmp.get(parent);
		if (children != null) {
			children.remove(childrenRem);
		}

		children = parentChildMap.get(parent);
		if (children != null) {
			children.remove(childrenRem);
		}

		addToRemovedMap(parent, childrenRem);
	}

	private void addToRemovedMap(Integer parent, Integer childrenRem) {
		LinkedList<Integer> children;
		children = deletedParentChildMap.get(parent);
		if (children == null) {
			children = new LinkedList<Integer>();
			deletedParentChildMap.put(parent, children);
		}
		children.add(childrenRem);
	}

	private void addChildrenToParentChildMap(Integer parent,
			LinkedList<Integer> childrenAdd, boolean tmp) {
		LinkedList<Integer> children;
		// add relations to the tmp map
		if (tmp) {
			children = parentChildMapTmp.get(parent);
		} else {
			children = parentChildMap.get(parent);
		}

		// this relation did not exist before
		if (children == null) {
			if (tmp) {
				parentChildMapTmp.put(parent, childrenAdd);
			} else {
				parentChildMap.put(parent, childrenAdd);
			}
			return;
		}
		children.addAll(childrenAdd);
	}

	public void deleteParentChildRelations(Integer parent, Integer child) {
		PreparedStatement stmt = null;
		try {
			Connection conn;
			conn = connect();
			stmt = conn.prepareStatement(Stmts.RemoveParentChild.getSql());

			if (hasParentChildRelation(parent, child)) {
				stmt.setInt(1, parent);
				stmt.setInt(2, child);
				stmt.execute();
				deleteChildrenFromParentChildMap(parent, child);
			}
		} catch (Exception e) {
			try {
				rollbackTransaction();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public void addParentChildRelations(Integer parent,
			LinkedList<Integer> children) {
		PreparedStatement stmt = null;
		try {
			Connection conn;
			conn = connect();
			stmt = conn.prepareStatement(Stmts.InsertParentChild.getSql());

			LinkedList<Integer> childrenToAdd = new LinkedList<Integer>();
			for (Integer child : children) {
				if (!hasParentChildRelation(parent, child)
						&& !childrenToAdd.contains(child)
						&& !isRemovedParentChildRelation(parent, child)) {
					stmt.setInt(1, parent);
					stmt.setInt(2, child);
					stmt.addBatch();
					childrenToAdd.add(child);
				}
			}

			if (childrenToAdd.size() > 0) {
				stmt.executeBatch();
				addChildrenToParentChildMap(parent, childrenToAdd, true);
			}

		} catch (Exception e) {
			if (stmt != null) {
				try {
					stmt.clearBatch();
				} catch (SQLException ec) {
				}
			}
			try {
				rollbackTransaction();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public void addParentChildRelation(Integer parent, Integer child) {
		PreparedStatement stmt = null;
		try {
			Connection conn;
			conn = connect();
			stmt = conn.prepareStatement(Stmts.InsertParentChild.getSql());

			LinkedList<Integer> childrenToAdd = new LinkedList<Integer>();
			if (!hasParentChildRelation(parent, child)
					&& !isRemovedParentChildRelation(parent, child)) {
				stmt.setInt(1, parent);
				stmt.setInt(2, child);
				childrenToAdd.add(child);
				stmt.execute();
				addChildrenToParentChildMap(parent, childrenToAdd, true);
			}
		} catch (Exception e) {
			try {
				rollbackTransaction();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public void addRootNode(String modelname, Integer rootnode) {
		PreparedStatement stmt = null;
		try {
			Connection conn;
			conn = connect();
			stmt = conn.prepareStatement(Stmts.InsertRootnode.getSql());

			stmt.setString(1, modelname);
			stmt.setInt(2, rootnode);

			stmt.execute();
			commitTransaction();
			roots.put(modelname, rootnode);

		} catch (Exception e) {
			try {
				rollbackTransaction();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public Pair getIndexForHash(String hash) {
		if (labelHash.containsKey(hash)) {
			return labelHash.get(hash);
		}
		return labelHashTmp.get(hash);
	}

	public boolean hasIndexForHash(String hash) {
		return labelHash.containsKey(hash);
	}

	public void getMaxLocalClones(int sizeTreshold, int faninTreshold) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Connection conn;
			conn = connect();

			// load hashcodes
			stmt = conn.prepareStatement(Stmts.GetLargestLocalClones.getSql());
			stmt.setInt(1, sizeTreshold);
			stmt.setInt(2, faninTreshold);
			rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getInt(1) + " " + rs.getInt(2));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public void loadDataFromDB() {
		PreparedStatement stmt = null;
		PreparedStatement cleanStmt = null;
		ResultSet rs = null;
		try {
			Connection conn;
			conn = connect();

			// clean database, for standalone
			cleanStmt = conn
					.prepareStatement("ALTER TABLE LABELHASH ALTER COLUMN NODEID RESTART WITH 0");
			cleanStmt.execute();

			cleanStmt = conn.prepareStatement("DELETE FROM LABELHASH");
			cleanStmt.execute();
			cleanStmt = conn.prepareStatement("DELETE FROM PARENTCHILDMAP");
			cleanStmt.execute();
			cleanStmt = conn.prepareStatement("DELETE FROM ROOTS");
			cleanStmt.execute();

			// load hashcodes
			stmt = conn.prepareStatement(Stmts.GetHashCodes.getSql());
			rs = stmt.executeQuery();
			while (rs.next()) {
				String hash = rs.getString(1);
				Integer nodeId = rs.getInt(2);
				Integer size = rs.getInt(3);

				labelHash.put(hash, new Pair(nodeId, size));

				nodeSizeMap.put(nodeId, new ObjectPair<Integer, String>(size,
						hash));
				if (hash.equals("ANDjoin") || hash.equals("ANDsplit")
						|| hash.equals("ORjoin") || hash.equals("ORsplit")
						|| hash.equals("XORjoin") || hash.equals("XORsplit")) {
					gwMap.put(hash, nodeId);
				}
			}

			// load parent child map
			stmt = conn.prepareStatement(Stmts.GetParentChild.getSql());
			rs = stmt.executeQuery();
			while (rs.next()) {
				LinkedList<Integer> children = parentChildMap.get(rs.getInt(1));
				if (children == null) {
					children = new LinkedList<Integer>();
					parentChildMap.put(rs.getInt(1), children);
				}
				children.add(rs.getInt(2));
			}

			// load roots
			stmt = conn.prepareStatement(Stmts.GetRoots.getSql());
			rs = stmt.executeQuery();
			while (rs.next()) {
				roots.put(rs.getString(1), rs.getInt(2));
			}

			if (labelHash.size() > 0 && gwMap.size() != 6) {
				System.out
						.println("The labelhash table does not contain gw entries.");
				System.exit(0);
			}
			if (gwMap.size() == 0) {
				gwMap.put("ANDjoin", addModelHash("ANDjoin", 1, false));
				gwMap.put("ANDsplit", addModelHash("ANDsplit", 1, false));
				gwMap.put("ORjoin", addModelHash("ORjoin", 1, false));
				gwMap.put("ORsplit", addModelHash("ORsplit", 1, false));
				gwMap.put("XORjoin", addModelHash("XORjoin", 1, false));
				gwMap.put("XORsplit", addModelHash("XORsplit", 1, false));
				addModelHash("_entry_", 1, false);
				addModelHash("_exit_", 1, false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public void commitTransaction() throws Exception {
		Connection con = connect();
		con.commit();

		labelHash.putAll(labelHashTmp);
		for (Entry<Integer, LinkedList<Integer>> e : parentChildMapTmp
				.entrySet()) {
			addChildrenToParentChildMap(e.getKey(), e.getValue(), false);
		}
		labelHashTmp = new HashMap<String, Pair>();
		parentChildMapTmp = new HashMap<Integer, LinkedList<Integer>>();

	}

	public void startTransaction() throws Exception {
		Connection con = connect();
		con.setAutoCommit(false);
	}

	public void closeConnection() throws Exception {
		if (con != null && !con.isClosed()) {
			con.close();
		}
	}

	public void rollbackTransaction() throws Exception {
		Connection con = connect();
		con.rollback();
		labelHashTmp = new HashMap<String, Pair>();
		parentChildMapTmp = new HashMap<Integer, LinkedList<Integer>>();
		con.close();
	}

	public Set<String> getlabels() {
		return labelHash.keySet();
	}

	public HashMap<String, Pair> getLabelHash() {
		return labelHash;
	}

	public HashMap<String, Integer> getRoots() {
		return roots;
	}

	public HashMap<Integer, LinkedList<Integer>> getParentChildMap() {
		return parentChildMap;
	}

	public void getSubstringSuperstringRelations(String hash, Integer id,
			LinkedList<Integer> children) {
		if (labelHash.containsKey(hash)) {
			return;
		}
		String searchHash = "." + hash + ".";
		for (Entry<String, Pair> h : labelHash.entrySet()) {
			// we have rigid or bond
			if (h.getKey().toLowerCase().contains(".x.")) {
				continue;
			}

			if (h.getValue().getSecond() > 1) {
				String lHash = "." + h.getKey() + ".";
				// this hash is a substring of a hash we just added
				if (searchHash.indexOf(lHash) != -1) {
					children.add(h.getValue().getFirst());

					StringTokenizer st = new StringTokenizer(lHash, ".");
					while (st.hasMoreElements()) {
						try {
							Integer currentId = Integer
									.parseInt(st.nextToken());
							children.remove(currentId);
							addToRemovedMap(id, currentId);
						} catch (Exception e) {
						}
					}
				}
				// the hash is superstring of a hash we just added
				else if (lHash.indexOf(searchHash) != -1
						&& !hasParentChildRelation(h.getValue().getFirst(), id)) {
					LinkedList<Integer> child = new LinkedList<Integer>();
					child.add(id);
					addParentChildRelations(h.getValue().getFirst(), child);

					StringTokenizer st = new StringTokenizer(searchHash, ".");
					while (st.hasMoreElements()) {
						try {
							Integer currentId = Integer
									.parseInt(st.nextToken());
							deleteParentChildRelations(h.getValue().getFirst(),
									currentId);
						} catch (Exception e) {
						}
					}
				} else {
					for (String s : HashLCS.findAllNotOverlappingSubstrings(
							new HashString(searchHash), new HashString(lHash))) {

						// new hash
						if (getnodeIdForHash(s) == -1) {
							int size = 0;

							HashSet<Integer> tokens = new HashSet<Integer>();
							StringTokenizer st = new StringTokenizer(s, ".");
							while (st.hasMoreElements()) {
								try {
									Integer current = Integer.parseInt(st
											.nextToken());
									if (!isGateway(current)
											&& !isEntryOrExitNode(current)) {
										size += nodeSizeMap.get(current)
												.getFirst();
									}
									children.remove(current);
									addToRemovedMap(id, current);
									deleteParentChildRelations(h.getValue()
											.getFirst(), current);
									tokens.add(current);
								} catch (Exception e) {
									size += 1;
								}
							}
							Integer child = addModelHash(s, size, true);

							for (Integer c : tokens) {
								addParentChildRelation(child, c);
							}

							children.add(child);
							addParentChildRelation(h.getValue().getFirst(),
									child);
						} else {
							// the parentchildrelation from the node will be
							// added
							// as substring superstring relation
						}
					}
				}
			}
		}
		@SuppressWarnings("unchecked")
		HashMap<String, Pair> labelHashTmpCopy = (HashMap<String, Pair>) labelHashTmp
				.clone();
		for (Entry<String, Pair> h : labelHashTmpCopy.entrySet()) {
			// we have rigid or bond
			if (h.getKey().toLowerCase().contains(".x.")
			// we added this node in the section above
					|| children.contains(h.getValue().getFirst())) {
				continue;
			}

			if (h.getValue().getSecond() > 1 && !hash.equals(h.getKey())) {
				String lHash = "." + h.getKey() + ".";
				// this hash is a substring of a hash we just added
				if (searchHash.indexOf(lHash) != -1) {
					children.add(h.getValue().getFirst());
					StringTokenizer st = new StringTokenizer(lHash, ".");
					while (st.hasMoreElements()) {
						try {
							Integer currentId = Integer
									.parseInt(st.nextToken());
							children.remove(currentId);
							addToRemovedMap(id, currentId);
						} catch (Exception e) {
						}
					}
				}
				// the hash is superstring of a hash we just added
				else if (lHash.indexOf(searchHash) != -1
						&& !hasParentChildRelation(h.getValue().getFirst(), id)) {
					LinkedList<Integer> child = new LinkedList<Integer>();
					child.add(id);
					addParentChildRelations(h.getValue().getFirst(), child);

					StringTokenizer st = new StringTokenizer(searchHash, ".");
					while (st.hasMoreElements()) {
						try {
							Integer currentId = Integer
									.parseInt(st.nextToken());
							deleteParentChildRelations(h.getValue().getFirst(),
									currentId);
						} catch (Exception e) {
						}
					}
				} else {
					for (String s : HashLCS.findAllNotOverlappingSubstrings(
							new HashString(searchHash), new HashString(lHash))) {
						// new hash
						if (getnodeIdForHash(s) == -1) {
							int size = 0;

							HashSet<Integer> tokens = new HashSet<Integer>();
							StringTokenizer st = new StringTokenizer(s, ".");
							while (st.hasMoreElements()) {
								try {
									Integer current = Integer.parseInt(st
											.nextToken());
									if (!isGateway(current)
											&& !isEntryOrExitNode(current)) {
										size += nodeSizeMap.get(current)
												.getFirst();
									}
									children.remove(current);
									addToRemovedMap(id, current);
									deleteParentChildRelations(h.getValue()
											.getFirst(), current);
									tokens.add(current);
								} catch (Exception e) {
									size += 1;
								}
							}

							Integer child = addModelHash(s, size, true);

							for (Integer c : tokens) {
								addParentChildRelation(child, c);
							}

							children.add(child);
							addParentChildRelation(h.getValue().getFirst(),
									child);
						} else {
							// the parentchildrelation from the node will be
							// added
							// as substring superstring relation
						}
					}
				}
			}
		}
	}

	class ObjectPair<A, B> {

		private A o1;
		private B o2;

		public ObjectPair(A o1, B o2) {
			this.o1 = o1;
			this.o2 = o2;
		}

		public A getFirst() {
			return o1;
		}

		public B getSecond() {
			return o2;
		}
	}
}
