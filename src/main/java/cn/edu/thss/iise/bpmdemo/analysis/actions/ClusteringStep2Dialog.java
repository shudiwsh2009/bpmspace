package cn.edu.thss.iise.bpmdemo.analysis.actions;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ClusteringStep2Dialog extends TitleAreaDialog {

	private String inputModelsFolder;

	public ClusteringStep2Dialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	public ClusteringStep2Dialog(Shell parentShell, String inputModelsFolder) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		this.inputModelsFolder = inputModelsFolder;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Clustering: ");
		setMessage("Following is the clustering tree.",
				IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		parent.setLayout(layout);
		/*
		 * BufferedImage bi = ClusterUtil.Cluster(inputModelsFolder, 453, 347);
		 * try { ImageIO.write(bi, "JPG", new File("D:\\yourImageName.JPG")); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		// image show
		final Shell shell = parent.getShell();
		Image image = new Image(shell.getDisplay(), "D:\\yourImageName.JPG");
		Label myLabel = new Label(parent, SWT.NONE);
		myLabel.setImage(image);

		return parent;
	}

	public String getInputModelsFolder() {
		return inputModelsFolder;
	}

}
