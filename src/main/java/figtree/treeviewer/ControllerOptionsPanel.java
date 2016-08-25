package figtree.treeviewer;

import jam.panels.OptionsPanel;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * @author Andrew Rambaut
 * @version $Id$
 */
public class ControllerOptionsPanel extends OptionsPanel {

	public ControllerOptionsPanel() {
		super();
	}

	public ControllerOptionsPanel(int hGap, int vGap) {
		super(hGap, vGap);
	}

	protected void adjustComponent(JComponent comp) {
		setComponentLook(comp);
	}

	public static void setComponentLook(JComponent comp) {
		comp.putClientProperty("Quaqua.Component.visualMargin", new Insets(0,
				0, 0, 0));
		Font font = UIManager.getFont("SmallSystemFont");
		if (font != null) {
			comp.setFont(font);
		}
		comp.putClientProperty("JComponent.sizeVariant", "small");
		if (comp instanceof JSpinner && font != null) {
			((JSpinner.NumberEditor) ((JSpinner) comp).getEditor())
					.getTextField().setFont(font);
		}
		if (comp instanceof JButton) {
			comp.putClientProperty("JButton.buttonType", "roundRect");
		}
		if (comp instanceof JComboBox) {
			// comp.putClientProperty("JComboBox.isSquare", Boolean.TRUE);
		}
		if (!(comp instanceof JTextField)) {
			comp.setFocusable(false);
		}
	}
}
