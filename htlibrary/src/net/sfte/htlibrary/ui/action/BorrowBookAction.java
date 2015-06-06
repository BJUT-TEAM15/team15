package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.BorrowBookDialog;

/**
 * This class defines the borrow book action.
 * 
 * @author wenwen
 */
public class BorrowBookAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BorrowBookAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "ͼ�����");
		putValue(Action.SMALL_ICON, new ImageIcon("images/borrowbook.png"));
		putValue(Action.SHORT_DESCRIPTION, "�򿪽��鴰��, ���н��Ĺ���");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new BorrowBookDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private BorrowBookDialog dialog = null;
}