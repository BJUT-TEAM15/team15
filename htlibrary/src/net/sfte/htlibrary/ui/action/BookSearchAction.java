package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.BookSearchDialog;

/**
 * Book search Action.
 * 
 * @author wenwen
 */
public class BookSearchAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookSearchAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "图书查询");
		putValue(Action.SMALL_ICON, new ImageIcon("images/query.png"));
		putValue(Action.SHORT_DESCRIPTION, "图书查询处理");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new BookSearchDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private BookSearchDialog dialog = null;
}