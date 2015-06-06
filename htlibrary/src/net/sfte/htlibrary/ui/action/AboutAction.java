package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

import net.sfte.htlibrary.ui.AboutDialog;
import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

/**
 * Unfinished
 * 
 * @author wenwen
 */
public class AboutAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "关于" + HtLibraryAuthorInfo.getLibraryName());
		putValue(Action.SMALL_ICON, new ImageIcon("images/about.png"));
		putValue(Action.SHORT_DESCRIPTION, "关于" + HtLibraryAuthorInfo.getLibraryFullName());
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null) {
			dialog = new AboutDialog();
			JPopupMenu menu = new JPopupMenu();
			menu.add(new ChangeImageAction(parent, ChangeImageAction.aboutDialog));
			dialog.setComponentPopupMenu(menu);
			// Java bug 4966109. Java 6.0 fixed it.
			dialog.addMouseListener(new MouseAdapter() {    });
		}
		dialog.showDialog(parent);
	}

	private Component parent;

	private AboutDialog dialog = null;
}