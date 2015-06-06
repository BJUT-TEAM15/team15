package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.GiveBackBookDialog;

/**
 * This class defines the action of give back book.
 * 
 * @author wenwen
 */
public class GiveBackBookAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GiveBackBookAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "图书归还");
		putValue(Action.SMALL_ICON, new ImageIcon("images/backbook.png"));
		putValue(Action.SHORT_DESCRIPTION, "图书归还管理");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new GiveBackBookDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private GiveBackBookDialog dialog = null;
}