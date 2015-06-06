package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.ReaderInfoDialog;

/**
 * Reader Info Action.
 * 
 * @author wenwen
 */
public class ReaderInfoAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReaderInfoAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "������Ϣ");
		putValue(Action.SMALL_ICON, new ImageIcon("images/readerinfo.png"));
		putValue(Action.SHORT_DESCRIPTION, "���߻�����Ϣ���������");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new ReaderInfoDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private ReaderInfoDialog dialog = null;
}