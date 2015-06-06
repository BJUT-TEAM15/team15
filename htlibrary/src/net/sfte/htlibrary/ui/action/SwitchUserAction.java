package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.LoginDialog;

/**
 * This class defines the action of switch user
 * 
 * @author wenwen
 */
public class SwitchUserAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SwitchUserAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "�л��û�");
		putValue(Action.SMALL_ICON, new ImageIcon("images/switch.png"));
		putValue(Action.SHORT_DESCRIPTION, "�л�ʹ����һ������Ա�ʺ�");
	}

	public void actionPerformed(ActionEvent e) {
		// '2' denote this is a SwitchUserDialog.
		if (dialog == null)
			dialog = new LoginDialog("�л�ʹ����һ������Ա�ʺ�", 2);
		dialog.showDialog(parent, "�л��û�");
	}

	private Component parent = null;

	private LoginDialog dialog = null;
}