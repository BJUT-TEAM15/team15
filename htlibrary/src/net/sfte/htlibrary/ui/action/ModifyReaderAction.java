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
		putValue(Action.NAME, "�����޸�");
		putValue(Action.SMALL_ICON, new ImageIcon("images/modifyreader.png"));
		putValue(Action.SHORT_DESCRIPTION, "�޸Ķ��߻�����Ϣ");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new ModifyDeleteReaderDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private ModifyDeleteReaderDialog dialog = null;
}