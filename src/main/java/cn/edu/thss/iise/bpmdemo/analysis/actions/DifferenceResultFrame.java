package cn.edu.thss.iise.bpmdemo.analysis.actions;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.jfree.ui.RefineryUtilities;

public class DifferenceResultFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<String> list = null;

	public DifferenceResultFrame(String title, List<String> difList) {
		super(title);
		// TODO Auto-generated constructor stub
		this.list = difList;
		JPanel panel = createPannel();
		setContentPane(panel);
	}

	private JPanel createPannel() {
		JPanel panel = new JPanel(new GridLayout(1, 3));
		String[] columnNames = { "Difference" };
		panel.add(new JLabel("Image 1"));

		String[][] data = null;
		data = new String[list.size() + 1][1];
		data[0][0] = "Result:";
		for (int i = 0; i < list.size(); i++) {
			data[i + 1][0] = (i + 1) + ": " + list.get(i);
		}
		JTable table = new JTable(data, columnNames);
		panel.setName("Difference Analysis");
		panel.add(table);

		panel.add(new JLabel("Image 2"));

		panel.setPreferredSize(new Dimension(900, 300));
		return panel;
	}

	public static void main(String args[]) {
		List<String> list = new ArrayList<String>();
		list.add("hellworld1");
		list.add("hellworld2");
		list.add("hellworld3");
		list.add("hellworld4");
		list.add("hellworld5");
		list.add("hellworld6");
		DifferenceResultFrame differenceResultFrame = new DifferenceResultFrame(
				"Difference Result", list);
		differenceResultFrame.pack();
		RefineryUtilities.centerFrameOnScreen(differenceResultFrame);
		differenceResultFrame.setVisible(true);
	}

}
