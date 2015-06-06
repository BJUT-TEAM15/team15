package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.sfte.htlibrary.ui.SystemHelpDialog;
import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

/**
 * Help
 * 
 * @author wenwen
 */
public class SystemHelpAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemHelpAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "使用帮助");
		putValue(Action.SHORT_DESCRIPTION, HtLibraryAuthorInfo.getLibraryFullName() + "使用帮助");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new SystemHelpDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private SystemHelpDialog dialog = null;
}