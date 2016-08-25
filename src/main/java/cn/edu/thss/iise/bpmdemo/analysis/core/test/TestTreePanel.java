package cn.edu.thss.iise.bpmdemo.analysis.core.test;

import jam.controlpalettes.BasicControlPalette;
import jam.controlpalettes.ControlPalette;

import java.awt.BorderLayout;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import jebl.evolution.io.ImportException;
import jebl.evolution.io.NewickImporter;
import jebl.evolution.trees.Tree;
import figtree.application.FigTreePanel;
import figtree.treeviewer.ExtendedTreeViewer;

public class TestTreePanel extends JFrame {
	ExtendedTreeViewer treeViewer;
	ControlPalette controlPalette;

	public TestTreePanel() throws IOException, ImportException {
		String treeFileName = "C:\\Users\\Guo-68\\Desktop\\�½��ļ��� (3)\\�½��ļ���\\test\\result\\newick.tree";
		treeViewer = new ExtendedTreeViewer(this);
		Tree tree;
		Reader reader = new FileReader(treeFileName);
		NewickImporter importer = new NewickImporter(reader, true);
		List<Tree> trees = importer.importTrees();
		treeViewer.setTrees(trees);

		controlPalette = new BasicControlPalette(200,
				BasicControlPalette.DisplayMode.ONLY_ONE_OPEN);
		Map<String, Object> settings = new HashMap<String, Object>();

		controlPalette.getSettings(settings);
		controlPalette.setSettings(settings);
		treeViewer.getContentPane().setSize(800, 800);
		FigTreePanel figTreePanel = new FigTreePanel(this, treeViewer,
				controlPalette);
		System.out.println(figTreePanel.getSize().toString());
		getContentPane().add(figTreePanel, BorderLayout.CENTER);
	}

	public static void main(String agrs[]) throws IOException, ImportException {
		TestTreePanel frame = new TestTreePanel();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(100, 100);
		frame.setVisible(true);
		frame.treeViewer.showTree(0);
	}

}
