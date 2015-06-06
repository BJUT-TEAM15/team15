package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.NewBookDialog;

public class NewBookAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewBookAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "����Ǽ�");
		putValue(Action.SMALL_ICON, new ImageIcon("images/newbook.png"));
		putValue(Action.SHORT_DESCRIPTION, "��������Ǽ�");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new NewBookDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private NewBookDialog dialog = null;
}