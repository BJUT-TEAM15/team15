package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.ModifyDeleteReaderDialog;

/**
 * This class defines the action delete Reader.
 * 
 * @author wenwen
 */
public class ReaderDeleteAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReaderDeleteAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "读者删除");
		putValue(Action.SMALL_ICON, new ImageIcon("images/deletereader.png"));
		putValue(Action.SHORT_DESCRIPTION, "读者帐户删除管理");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new ModifyDeleteReaderDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private ModifyDeleteReaderDialog dialog = null;
}