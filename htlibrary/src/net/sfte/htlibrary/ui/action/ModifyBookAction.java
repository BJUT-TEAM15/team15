package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.ModifyBookDialog;

/**
 * This class defines the action of modiry book action.
 * 
 * @author wenwen
 */
public class ModifyBookAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModifyBookAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "图书修改");
		putValue(Action.SMALL_ICON, new ImageIcon("images/modifybook.png"));
		putValue(Action.SHORT_DESCRIPTION, "修改图书基本信息");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new ModifyBookDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private ModifyBookDialog dialog = null;
}