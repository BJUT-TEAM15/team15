package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.LossBookDialog;

/**
 * This classes defines a Action of reader lost book management.
 * 
 * @author wenwen
 */
public class LossBookAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LossBookAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "图书遗失");
		putValue(Action.SMALL_ICON, new ImageIcon("images/lostbook.png"));
		putValue(Action.SHORT_DESCRIPTION, "读者遗失图书管理");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new LossBookDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private LossBookDialog dialog = null;
}