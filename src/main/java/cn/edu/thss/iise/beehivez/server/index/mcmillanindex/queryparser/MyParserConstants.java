/* Generated By:JavaCC: Do not edit this line. MyParserConstants.java */
package cn.edu.thss.iise.beehivez.server.index.mcmillanindex.queryparser;

/**
 * Token literal values and constants. Generated by
 * org.javacc.parser.OtherFilesGen#start()
 */
public interface MyParserConstants {

	/** End of File. */
	int EOF = 0;
	/** RegularExpression Id. */
	int BINLOGOP = 5;
	/** RegularExpression Id. */
	int AND = 6;
	/** RegularExpression Id. */
	int OR = 7;
	/** RegularExpression Id. */
	int UNLOGOP = 8;
	/** RegularExpression Id. */
	int NOT = 9;
	/** RegularExpression Id. */
	int TASKCOMPOP = 10;
	/** RegularExpression Id. */
	int SETCOMOP = 11;
	/** RegularExpression Id. */
	int IDENTICAL = 12;
	/** RegularExpression Id. */
	int SUBSETOF = 13;
	/** RegularExpression Id. */
	int OVERLAP = 14;
	/** RegularExpression Id. */
	int SETOP = 15;
	/** RegularExpression Id. */
	int UNION = 16;
	/** RegularExpression Id. */
	int DIFFERENT = 17;
	/** RegularExpression Id. */
	int INTERSECT = 18;
	/** RegularExpression Id. */
	int EXIST = 19;
	/** RegularExpression Id. */
	int ANYALL = 20;
	/** RegularExpression Id. */
	int ANY = 21;
	/** RegularExpression Id. */
	int ALL = 22;
	/** RegularExpression Id. */
	int LIST = 23;
	/** RegularExpression Id. */
	int PROCESS = 24;
	/** RegularExpression Id. */
	int WHERE = 25;
	/** RegularExpression Id. */
	int IDENTIFIER = 26;
	/** RegularExpression Id. */
	int WORD = 27;
	/** RegularExpression Id. */
	int LETTER = 28;
	/** RegularExpression Id. */
	int PART_LETTER = 29;
	/** RegularExpression Id. */
	int VARIABLE = 30;
	/** RegularExpression Id. */
	int FLOATING_POINT_LITERAL = 31;
	/** RegularExpression Id. */
	int EXPONENT = 32;
	/** RegularExpression Id. */
	int LPAREN = 33;
	/** RegularExpression Id. */
	int RPAREN = 34;
	/** RegularExpression Id. */
	int LBRACE = 35;
	/** RegularExpression Id. */
	int RBRACE = 36;
	/** RegularExpression Id. */
	int LBRACKET = 37;
	/** RegularExpression Id. */
	int RBRACKET = 38;
	/** RegularExpression Id. */
	int SEMICOLON = 39;
	/** RegularExpression Id. */
	int COMMA = 40;
	/** RegularExpression Id. */
	int EQUAL = 41;

	/** Lexical state. */
	int DEFAULT = 0;

	/** Literal token values. */
	String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"",
			"<BINLOGOP>", "\"&&\"", "\"||\"", "<UNLOGOP>", "\"!\"",
			"<TASKCOMPOP>", "<SETCOMOP>", "\"identical\"", "\"subsetof\"",
			"\"overlap\"", "<SETOP>", "\"union\"", "\"different\"",
			"\"intersect\"", "\"exist\"", "<ANYALL>", "\"any\"", "\"all\"",
			"\"list\"", "\"process\"", "\"where\"", "<IDENTIFIER>", "<WORD>",
			"<LETTER>", "<PART_LETTER>", "<VARIABLE>",
			"<FLOATING_POINT_LITERAL>", "<EXPONENT>", "\"(\"", "\")\"",
			"\"{\"", "\"}\"", "\"[\"", "\"]\"", "\";\"", "\",\"", "\"=\"",
			"\"<\"", "\">\"", };

}
