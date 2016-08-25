package cn.edu.thss.iise.bpmdemo.analysis.core.cluster;

import jam.controlpalettes.BasicControlPalette;
import jam.controlpalettes.ControlPalette;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import jebl.evolution.io.ImportException;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.trees.Tree;
import cn.edu.thss.iise.bpmdemo.analysis.core.similarity.SimilarityUtil;

import com.ibm.bpm.analyzer.Calculation;
import com.ibm.bpm.analyzer.GenerateNewick;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import figtree.application.FigTreePDF;
import figtree.application.FigTreePanel;
import figtree.treeviewer.ExtendedTreeViewer;

public class ClusterUtil {

	// public static void main(String args[])
	// {
	// String filePath =
	// "G:\\Graduate\\Projects\\2013-7~8BPM Keynote Demo\\Model\\bpmn文件reduced\\";
	// Cluster(filePath,800,800);
	// }

	static String treeFilePath = "tree.tree";

	// public static BufferedImage Cluster(String filePath, int width, int
	// height)
	// {
	// BufferedImage result = null;
	// //1.�������ƶ�
	// generateSimilarityXLS(filePath);
	// //2.ͳ��figtree
	// generateFigTree();
	// //3.תpdf
	// creatPDF(width, height);
	// //4.תͼƬ
	// try
	// {
	// result = convertPDFToImage();
	// }
	// catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return result;
	// }

	public static FigTreePanel Cluster(JFrame father, String filePath,
			int width, int height) {
		FigTreePanel result = null;

		generateSimilarityXLS(filePath);
		generateFigTree();
		try {
			result = generateFigTreePanel(father, height, width);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImportException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static FigTreePanel generateFigTreePanel(JFrame fatherFrame,
			int height, int width) throws IOException, ImportException {
		ExtendedTreeViewer treeViewer;
		ControlPalette controlPalette;
		treeViewer = new ExtendedTreeViewer();

		Reader reader = new FileReader(treeFilePath);
		NewickImporter importer = new NewickImporter(reader, true);
		List<Tree> trees = importer.importTrees();
		treeViewer.setTrees(trees);

		controlPalette = new BasicControlPalette(200,
				BasicControlPalette.DisplayMode.ONLY_ONE_OPEN);
		Map<String, Object> settings = new HashMap<String, Object>();

		controlPalette.getSettings(settings);
		controlPalette.setSettings(settings);

		FigTreePanel figTreePanel = new FigTreePanel(fatherFrame, treeViewer,
				controlPalette);
		figTreePanel.setSize(width, height);
		return figTreePanel;
	}

	private static BufferedImage convertPDFToImage() throws IOException {
		String pdfName = "tree.pdf";
		// load a pdf from a byte buffer
		File file = new File(pdfName);
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
				channel.size());
		PDFFile pdffile = new PDFFile(buf);
		PDFPage page = pdffile.getPage(0);

		// get the width and height for the doc at the default zoom
		Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
				(int) page.getBBox().getHeight());

		// generate the image
		Image img = page.getImage(rect.width, rect.height, // width &
															// height
				rect, // clip rect
				null, // null for the ImageObserver
				true, // fill background with white
				true // block until drawing is done
				);

		BufferedImage tag = new BufferedImage(rect.width, rect.height,
				BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(img, 0, 0, rect.width, rect.height, null);

		return tag;
	}

	private static void creatPDF(int width, int height) {
		String pdfName = "tree.pdf";
		FigTreePDF.createGraphic(width, height, treeFilePath, pdfName);
	}

	private static void generateFigTree() {
		// TODO Auto-generated method stub
		String excelPath = "similarity.xls";
		GenerateNewick.generic(excelPath, treeFilePath);
	}

	private static void generateSimilarityXLS(String filePath) {
		// TODO Auto-generated method stub
		File folder = new File(filePath);
		File[] files = folder.listFiles();
		int size = files.length;

		String[][] similarity;
		similarity = new String[size + 1][];
		for (int i = 0; i < size + 1; i++)
			similarity[i] = new String[size + 1];
		for (int i = 1; i < size + 1; i++) {
			similarity[0][i] = files[i - 1].getName();
			similarity[i][0] = files[i - 1].getName();
		}
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				if (i < j) {
					File file1 = files[i];
					File file2 = files[j];
					similarity[i + 1][j + 1] = String.valueOf(SimilarityUtil
							.similarity(file1.getAbsolutePath(),
									file2.getAbsolutePath()));
				} else if (i == j) {
					similarity[i + 1][j + 1] = "1";
				} else if (i > j) {
					similarity[i + 1][j + 1] = similarity[j + 1][i + 1];
				}
			}
		Calculation.writeExl(similarity, "similarity.xls");
	}

	public static void main(String[] args) {
		generateSimilarityXLS("D:\\Process Data Group\\02.Process Space\\GQL\\Model\\bpmn文件\\bpmn文件81\\fragments");
		generateFigTree();
	}

}
