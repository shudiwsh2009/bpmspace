/* Generated By:JJTree: Do not edit this line. ASTOp.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package cn.edu.thss.iise.beehivez.server.index.petrinetindex.rltMatrix.queryParser;

import cn.edu.thss.iise.beehivez.server.index.petrinetindex.rltMatrix.RltConstants;

public class ASTOp extends SimpleNode implements RltConstants {
	public ASTOp(int id) {
		super(id);
	}

	public ASTOp(Parser p, int id) {
		super(p, id);
	}

	@Override
	public void jjtSetValue(Object o) {
		if (o instanceof String) {
			String s = (String) o;
			for (int i = 0; i < ARR_RELATIONS.length; i++) {
				if (s.equals(ARR_RELATIONS[i])) {
					value = ARR_BIT_RELATIONS[i];
					break;
				}
			}
		} else
			value = o;
	}

	public void setValue(String image) {
		// TODO Auto-generated method stub

	}

}
/*
 * JavaCC - OriginalChecksum=7423f95c955334843a45b528e98b6103 (do not edit this
 * line)
 */
