package net.sfte.htlibrary.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import net.sfte.htlibrary.database.HtConnection;

public class TodayStatisticDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TodayStatisticDialog() {
		setLayout(new GridLayout(0, 1));
		Border etched = BorderFactory.createEtchedBorder();
		setBorder(BorderFactory.createTitledBorder(etched, "今日信息"));

		newBooks = new JLabel("今日新进图书数量:         ", JLabel.CENTER);
		registerReaders = new JLabel("今日注册读者数量:          ", JLabel.CENTER);
		readers = new JLabel("本馆读者总计数量:          ", JLabel.CENTER);
		borrowBooks = new JLabel("今日借出图书数量:          ", JLabel.CENTER);
		overdueBooks = new JLabel("今日超期图书数量:          ", JLabel.CENTER);
		lossBooks = new JLabel("今日丢失图书数量:          ", JLabel.CENTER);

		add(newBooks);
		add(registerReaders);
		add(readers);
		add(borrowBooks);
		add(overdueBooks);
		add(lossBooks);
	}

	public void showDialog(Component parent) {
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.pack();
		}
		int newBookAmount = 0;
		int registerReaderAmount = 0;
		int readerAmount = 0;
		int borrowBookAmount = 0;
		int overdueBookAmount = 0;
		int lossBookAmount = 0;
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT SUM(total_amount) "
					+ "FROM book "
					+ "WHERE DATEDIFF('day', add_date, CURDATE()) = 0");
			if (rs.next())
				newBookAmount = rs.getInt(1);

			rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM reader "
					+ "WHERE DATEDIFF('day', register_date, CURDATE()) = 0");
			if (rs.next())
				registerReaderAmount = rs.getInt(1);

			rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM reader");
			if (rs.next())
				readerAmount = rs.getInt(1);

			rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM borrowbook "
					+ "WHERE DATEDIFF('day', borrow_date, CURDATE()) = 0");
			if (rs.next())
				borrowBookAmount = rs.getInt(1);

			rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM borrowbook "
					+ "WHERE DATEDIFF('day', revertible_date, CURDATE()) > 0");
			if (rs.next())
				overdueBookAmount = rs.getInt(1);

			rs = stmt.executeQuery("SELECT COUNT(*) " + "FROM lossbook "
					+ "WHERE DATEDIFF('day', loss_date, CURDATE()) = 0");
			if (rs.next())
				lossBookAmount = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// do nothing.
			}
		}

		newBooks.setText("今日新进图书数量:         " + newBookAmount);
		registerReaders.setText("今日注册读者数量:          " + registerReaderAmount);
		readers.setText("本馆读者总计数量:          " + readerAmount);
		borrowBooks.setText("今日借出图书数量:          " + borrowBookAmount);
		overdueBooks.setText("今日超期图书数量:          " + overdueBookAmount);
		lossBooks.setText("今日丢失图书数量:          " + lossBookAmount);

		dialog.setTitle("今日信息查询");
		dialog.setSize(300, 300);
		// set the Location of this dialog to the center
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension size = kit.getScreenSize();
		int width = size.width;
		int height = size.height;
		dialog.setLocation((width - dialog.getWidth()) / 2, (height - dialog
				.getHeight()) / 2);
		dialog.setVisible(true);
	}

	private JLabel newBooks;

	private JLabel registerReaders;

	private JLabel readers;

	private JLabel borrowBooks;

	private JLabel overdueBooks;

	private JLabel lossBooks;

	private JDialog dialog = null;
}