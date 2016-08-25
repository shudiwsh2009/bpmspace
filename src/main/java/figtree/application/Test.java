package figtree.application;

public class Test {
	public static void main(String args[]) {
		int width = 300;
		int height = 300;
		String treeFile = "C:\\Users\\Guo-68\\Desktop\\新建文件夹 (3)\\新建文件夹\\test\\result\\newick.tree";
		String graphPath = "C:\\graph.jpg";
		FigTreePDF.createGraphic(width, height, treeFile, graphPath);
	}

}
