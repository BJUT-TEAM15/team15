package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import net.sfte.htlibrary.ui.TodayStatisticDialog;

/**
 * This class defines Today statistics information action.
 * 
 * @author wenwen
 */
public class TodayAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TodayAction(Component parent) {
		this.parent = parent;
		putValue(Action.NAME, "今日统计");
		putValue(Action.SMALL_ICON, new ImageIcon("images/today.png"));
		putValue(Action.SHORT_DESCRIPTION, "今日图书馆信息统计");
	}

	public void actionPerformed(ActionEvent e) {
		if (dialog == null)
			dialog = new TodayStatisticDialog();
		dialog.showDialog(parent);
	}

	private Component parent;

	private TodayStatisticDialog dialog = null;
}