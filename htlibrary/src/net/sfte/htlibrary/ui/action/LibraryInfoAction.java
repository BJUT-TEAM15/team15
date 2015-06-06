package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sfte.htlibrary.ui.LibraryInfoDialog;

/**
 * This class is the action listener of libraryInfo menuItem. It shows a dialog
 * to display the library infomation, and User can modify or custom their's
 * system.
 * 
 * @author wenwen
 */
public class LibraryInfoAction implements ActionListener {
	public LibraryInfoAction(Component parent) {
		this.parent = parent;
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new LibraryInfoDialog();
		dialog.showDialog(parent);
	}

	private Component parent = null;

	LibraryInfoDialog dialog = null;
}