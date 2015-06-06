package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.RenewBookDialog;

/**
 * This class defines the renew book action.
 * 
 * @author wenwen
 */
public class RenewBookAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RenewBookAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "图书续借");
		putValue(Action.SMALL_ICON, new ImageIcon("images/renewbook.png"));
		putValue(Action.SHORT_DESCRIPTION, "续借图书管理");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new RenewBookDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private RenewBookDialog dialog = null;
}