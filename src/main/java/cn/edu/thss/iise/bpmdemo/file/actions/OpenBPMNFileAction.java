package cn.edu.thss.iise.bpmdemo.file.actions;

import java.io.File;

import org.drools.eclipse.flow.bpmn2.editor.BPMNModelEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class OpenBPMNFileAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		/*
		 * String path =
		 * DroolsEclipsePlugin.getDefault().getStateLocation().makeAbsolute
		 * ().toFile().getAbsolutePath();
		 * 
		 * System.out.println("path: " + path); File file = new File(path);
		 * 
		 * if(file.isDirectory()) { try { File newFile = new File(path + "//" +
		 * "123.bpmn"); newFile.createNewFile(); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } }
		 */

		FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
		dialog.setFilterPath(System.getProperty("java.home"));
		dialog.setFilterExtensions(new String[] { "*.bpmn" });
		dialog.setFilterNames(new String[] { "Text Files (*.bpmn)" });
		String fileName = dialog.open();
		File file = new File(fileName);
		if (file != null && file.getName().endsWith(".bpmn")) {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchPage page = workbench.getActiveWorkbenchWindow()
					.getActivePage();
			IWorkspace workspace = ResourcesPlugin.getWorkspace();

			IPath location = Path.fromOSString(file.getAbsolutePath());
			IFile ifile = workspace.getRoot().getFileForLocation(location);
			/*
			 * String p =
			 * workspace.getRoot().getLocation().toFile().getAbsolutePath();
			 * System.out.print(p);
			 */
			try {
				IEditorPart editor = IDE.openEditor(page, ifile);
				if (editor instanceof BPMNModelEditor) {
					System.out.println("this is a bpmn model.");
				}
				/*
				 * IFileStore ifs = IDEResourceInfoUtils.getFileStore(fileName);
				 * 
				 * System.out.print("demo test"); IEditorPart editor =
				 * IDE.openEditor(page, ifs.toURI(),
				 * "org.drools.eclipse.flow.bpmn2.editor.RuleFlowModelEditor",
				 * true); if (editor instanceof BPMNModelEditor) {
				 * System.out.println("hello world!"); }
				 */
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		this.window = window;
	}

}
