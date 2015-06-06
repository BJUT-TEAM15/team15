package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.OverdueBookDialog;

/**
 * This class defines the overdue book action.
 * 
 * @author wenwen
 */
public class OverdueBookAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OverdueBookAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "超期图书");
		putValue(Action.SMALL_ICON, new ImageIcon("images/overdue.png"));
		putValue(Action.SHORT_DESCRIPTION, "超期图书查询");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new OverdueBookDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private OverdueBookDialog dialog = null;
}