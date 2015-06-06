package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.AdminSettingDialog;

/**
 * This class defines the action of AdminSetting Item. Popup a dialog to add,
 * delete or modity a admin accounts.
 * 
 * @author wenwen
 */
public class AdminSettingAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdminSettingAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "������Ա");
		putValue(Action.SMALL_ICON, new ImageIcon("images/admin.png"));
		putValue(Action.SHORT_DESCRIPTION, "����, ɾ�����޸Ĺ���Ա�ʺ�");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new AdminSettingDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private AdminSettingDialog dialog = null;
}