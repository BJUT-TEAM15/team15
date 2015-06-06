package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.ModifyDeleteReaderDialog;

/**
 * This class defines the action of modify reader action.
 * 
 * @author wenwen
 */
public class ModifyReaderAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModifyReaderAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "读者修改");
		putValue(Action.SMALL_ICON, new ImageIcon("images/modifyreader.png"));
		putValue(Action.SHORT_DESCRIPTION, "修改读者基本信息");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new ModifyDeleteReaderDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private ModifyDeleteReaderDialog dialog = null;
}