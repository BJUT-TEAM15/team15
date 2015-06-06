package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.sfte.htlibrary.ui.LostBookQueryDialog;

/**
 * Lost book query action.
 * 
 * @author wenwen
 */
public class LostBookQueryAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LostBookQueryAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "丢失图书查询");
		// putValue(Action.SMALL_ICON, new ImageIcon(""));
		putValue(Action.SHORT_DESCRIPTION, "查看所有丢失的图书");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new LostBookQueryDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private LostBookQueryDialog dialog = null;
}