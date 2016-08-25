package cn.edu.thss.iise.bpmdemo.charts;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.CategoryItemEntity;

public class MouseEventPanel extends JPanel implements ChartMouseListener {
	private static final long serialVersionUID = 1L;
	// List<JFreeChart> charts;
	public JLabel l = new JLabel("hello world");

	public MouseEventPanel(java.awt.LayoutManager layoutmanager) {
		super(layoutmanager);
		// charts = new ArrayList<JFreeChart>();
	}

	/*
	 * public void addChart(JFreeChart jfreechart) { charts.add(jfreechart); }
	 * 
	 * public JFreeChart[] getCharts() { int i = charts.size(); JFreeChart
	 * ajfreechart[] = new JFreeChart[i]; for (int j = 0; j < i; j++)
	 * ajfreechart[j] = (JFreeChart) charts.get(j);
	 * 
	 * return ajfreechart; }
	 */
	@Override
	public void chartMouseClicked(ChartMouseEvent chartclickevent) {
		// TODO Auto-generated method stub
		org.jfree.chart.entity.ChartEntity chartentity = chartclickevent
				.getEntity();
		if (chartentity instanceof CategoryItemEntity) {
			CategoryItemEntity categoryitementity = (CategoryItemEntity) chartentity;
			l.setText((String) categoryitementity.getCategory());
		}
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
