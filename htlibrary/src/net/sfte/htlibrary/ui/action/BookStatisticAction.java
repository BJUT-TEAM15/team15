package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.BookStatisticDialog;

/**
 * Unfinished
 * 
 * @author wenwen
 */
public class BookStatisticAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookStatisticAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "图书统计");
		putValue(Action.SMALL_ICON, new ImageIcon("images/statistics.png"));
		putValue(Action.SHORT_DESCRIPTION, "图书统计窗口");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new BookStatisticDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private BookStatisticDialog dialog = null;
}