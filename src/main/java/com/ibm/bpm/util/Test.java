package com.ibm.bpm.util;

import java.util.ArrayList;

import org.w3c.dom.Element;

import com.ibm.bpm.model.OWLClass;

public class Test {

	public static void main(String[] args) {
		try {
			// System.out.println(UUID.randomUUID());
			//
			// List<String> a = new ArrayList<String>();
			// String a1 = "aaaa";
			// String a2 = "bbbb";
			// String a3 = "cccc";
			// a.add(a1);
			// a.add(a2);
			// //a.add(a3);
			// List<String> b = new ArrayList<String>();
			// String b1 = "aaaa";
			// String b2 = "bbbb";
			// b.add(b1);
			// b.add(b2);
			// System.out.println(a.containsAll(b));
			// System.out.println(b.containsAll(a));

			// String aa = "aaaa/bbbb/cccc";
			// System.out.println(aa.lastIndexOf("/"));
			// aa = aa.substring(aa.lastIndexOf("/") + 1);
			// InputStream is = new ByteArrayInputStream(aa.getBytes());
			// StringBuffer out = new StringBuffer();
			// byte[] by = new byte[4096];
			// int n;
			// while ((n = is.read(by)) != -1) {
			// out.append(new String(by, 0, n));
			// }
			// System.out.println(out.toString());
			// System.out.println(aa);

			// OWLModel model = new OWLModel();
			// ArrayList<ActivityEntity> entityList = new
			// ArrayList<ActivityEntity>();
			// ActivityEntity entity = model.new ActivityEntity();
			// entity.setName("testOWL");
			// ArrayList<String> incoming = new ArrayList<String>();
			// incoming.add("aaaaa");
			// incoming.add("bbbbb");
			// entity.setIncoming(incoming);
			// ArrayList<String> outgoing = new ArrayList<String>();
			// outgoing.add("ccccc");
			// outgoing.add("ddddd");
			// entity.setOutcoming(outgoing);
			// ArrayList<String> contain = new ArrayList<String>();
			// contain.add("eeeee");
			// contain.add("fffff");
			// entity.setContain(contain);
			// entityList.add(entity);
			//
			// ActivityEntity entity1 = model.new ActivityEntity();
			// entity1.setName("testOWL111");
			// ArrayList<String> incoming1 = new ArrayList<String>();
			// incoming1.add("aaaaa111");
			// incoming1.add("bbbbb111");
			// entity1.setIncoming(incoming1);
			// ArrayList<String> outgoing1 = new ArrayList<String>();
			// outgoing1.add("ccccc111");
			// outgoing1.add("ddddd111");
			// entity1.setOutcoming(outgoing1);
			// ArrayList<String> contain1 = new ArrayList<String>();
			// contain1.add("eeeee111");
			// contain1.add("fffff444");
			// entity1.setContain(contain1);
			// entityList.add(entity1);
			// model.generateOWL(entityList, "D:/test/test111.owl");

			OWLModel model = new OWLModel();
			Element element = model
					.getElement("D:/test/config/SemanticTable.owl");
			ArrayList<OWLClass> list = model.getOWLList(element);
			list.size();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
