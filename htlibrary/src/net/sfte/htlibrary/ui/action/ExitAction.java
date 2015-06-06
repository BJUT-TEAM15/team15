package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.ExitDialog;
import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

/**
 * This class defines the exit action of htlibrary. It is created when users
 * click the exit Button or exit menu item.
 * 
 * @author wenwen
 */
public class ExitAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExitAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "退出系统");
		putValue(Action.SMALL_ICON, new ImageIcon("images/exit.png"));
		putValue(Action.SHORT_DESCRIPTION, "退出" + HtLibraryAuthorInfo.getLibraryFullName());
	}

	public void actionPerformed(ActionEvent e) {
		if (exitDialog == null)
			exitDialog = new ExitDialog();
		exitDialog.showDialog(parent);
	}

	private ExitDialog exitDialog = null;

	private Component parent = null;
}