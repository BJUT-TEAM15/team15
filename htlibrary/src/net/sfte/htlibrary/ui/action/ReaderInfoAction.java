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
		putValue(Action.NAME, "读者信息");
		putValue(Action.SMALL_ICON, new ImageIcon("images/readerinfo.png"));
		putValue(Action.SHORT_DESCRIPTION, "读者基本信息及借书情况");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new ReaderInfoDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private ReaderInfoDialog dialog = null;
}