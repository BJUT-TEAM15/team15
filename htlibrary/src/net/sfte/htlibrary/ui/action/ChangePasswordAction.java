package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.ChangePasswordDialog;

/**
 * This class defines the action of change password of admin pop-up a new dialog
 * to change password.
 * 
 * @author wenwen
 */
public class ChangePasswordAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChangePasswordAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "修改密码");
		putValue(Action.SMALL_ICON, new ImageIcon("images/password.png"));
		putValue(Action.SHORT_DESCRIPTION, "修改管理员密码");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new ChangePasswordDialog();
		dialog.showDialog(parent);
	}

	private Component parent = null;

	private ChangePasswordDialog dialog = null;
}