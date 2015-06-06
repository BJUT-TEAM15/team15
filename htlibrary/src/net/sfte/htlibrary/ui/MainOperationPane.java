package net.sfte.htlibrary.ui;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import net.sfte.htlibrary.ui.action.BookSearchAction;
import net.sfte.htlibrary.ui.action.BookStatisticAction;
import net.sfte.htlibrary.ui.action.BorrowBookAction;
import net.sfte.htlibrary.ui.action.GiveBackBookAction;
import net.sfte.htlibrary.ui.action.LossBookAction;
import net.sfte.htlibrary.ui.action.ModifyBookAction;
import net.sfte.htlibrary.ui.action.NewBookAction;
import net.sfte.htlibrary.ui.action.OverdueBookAction;
import net.sfte.htlibrary.ui.action.ReaderDeleteAction;
import net.sfte.htlibrary.ui.action.ReaderInfoAction;
import net.sfte.htlibrary.ui.action.ReaderRegisterAction;
import net.sfte.htlibrary.ui.action.RenewBookAction;

/**
 * Main operation pane of htlibrary. Most operation are inside it.
 * 
 * @author wenwen
 */
public class MainOperationPane {
	public MainOperationPane(JFrame parent) {
		box = Box.createVerticalBox();
		box.add(Box.createVerticalStrut(20));

		JButton borrowButton = new JButton(new BorrowBookAction(parent));
		box.add(borrowButton);

		JButton backButton = new JButton(new GiveBackBookAction(parent));
		box.add(backButton);

		JButton renewButton = new JButton(new RenewBookAction(parent));
		box.add(renewButton);

		box.add(Box.createVerticalStrut(20));

		JButton bookStatisticButton = new JButton(new BookStatisticAction(
				parent));
		box.add(bookStatisticButton);

		JButton bookSearchButton = new JButton(new BookSearchAction(parent));
		box.add(bookSearchButton);

		JButton overdueBookButton = new JButton(new OverdueBookAction(parent));
		box.add(overdueBookButton);

		box.add(Box.createVerticalStrut(20));

		JButton newBookButton = new JButton(new NewBookAction(parent));
		box.add(newBookButton);

		JButton modifyBookButton = new JButton(new ModifyBookAction(parent));
		box.add(modifyBookButton);

		JButton deleteBookButton = new JButton(new LossBookAction(parent));
		box.add(deleteBookButton);

		box.add(Box.createVerticalStrut(20));

		JButton readerInfoButton = new JButton(new ReaderInfoAction(parent));
		box.add(readerInfoButton);

		JButton readerRegisterButton = new JButton(new ReaderRegisterAction(
				parent));
		box.add(readerRegisterButton);

		JButton readerDeleteButton = new JButton(new ReaderDeleteAction(parent));
		box.add(readerDeleteButton);

		box.add(Box.createVerticalStrut(20));
	}

	public Box getPane() {
		return box;
	}

	private Box box = null;
}