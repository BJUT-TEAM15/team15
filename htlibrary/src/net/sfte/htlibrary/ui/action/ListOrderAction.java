package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.ListOrderDialog;

/**
 * Unfinished
 * 
 * @author wenwen
 */
public class ListOrderAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListOrderAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "排行榜单");
		putValue(Action.SMALL_ICON, new ImageIcon("images/order.png"));
		putValue(Action.SHORT_DESCRIPTION, "读者之星, 最受欢迎图书排行");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new ListOrderDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private ListOrderDialog dialog = null;
}