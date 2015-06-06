package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.ModifyDeleteReaderDialog;

/**
 * This class defines the action of delete Reader accounts
 * 
 * @author wenwen
 */
public class DeleteReaderAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeleteReaderAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "读者删除");
		putValue(Action.SMALL_ICON, new ImageIcon("images/deleteuser.png"));
		putValue(Action.SHORT_DESCRIPTION, "从数据库中删除读者");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new ModifyDeleteReaderDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private ModifyDeleteReaderDialog dialog = null;
}