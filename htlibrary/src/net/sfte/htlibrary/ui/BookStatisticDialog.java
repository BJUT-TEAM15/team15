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

public class BookStatisticDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookStatisticDialog() {
		setLayout(new GridLayout(0, 1));
		Border etched = BorderFactory.createEtchedBorder();
		setBorder(BorderFactory.createTitledBorder(etched, "统计结果"));

		totalLabel = new JLabel("图书馆总藏书:          ", JLabel.CENTER);
		timesLabel = new JLabel("借阅图书次数:          ", JLabel.CENTER);
		borrowLabel = new JLabel("在借图书数量:          ", JLabel.CENTER);
		existLabel = new JLabel("现存图书数量:          ", JLabel.CENTER);
		overdueLabel = new JLabel("超期图书数量:          ", JLabel.CENTER);
		lossLabel = new JLabel("丢失图书数量:          ", JLabel.CENTER);
		add(totalLabel);
		add(timesLabel);
		add(existLabel);
		add(borrowLabel);
		add(overdueLabel);
		add(lossLabel);
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
		int totalAmount = 0;
		int times = 0;
		int borrowAmount = 0;
		int existAmount = 0;
		int overdueAmount = 0;
		int lossAmount = 0;
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT SUM(total_amount), "
					+ "SUM(amount), SUM(borrowed_times) FROM book");
			if (rs.next()) {
				totalAmount = rs.getInt(1);
				existAmount = rs.getInt(2);
				times = rs.getInt(3);
			}

			rs = stmt.executeQuery("SELECT COUNT(*) FROM borrowbook");
			if (rs.next())
				borrowAmount = rs.getInt(1);

			rs = stmt.executeQuery("SELECT COUNT(*) FROM borrowbook "
					+ "WHERE DATEDIFF('day', revertible_date, CURDATE()) > 0");
			if (rs.next())
				overdueAmount = rs.getInt(1);

			rs = stmt.executeQuery("SELECT COUNT(*) FROM lossbook");
			if (rs.next())
				lossAmount = rs.getInt(1);
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
		totalLabel.setText("图书馆总藏书:          " + totalAmount);
		timesLabel.setText("借阅图书次数:          " + times);
		borrowLabel.setText("在借图书数量:          " + borrowAmount);
		existLabel.setText("现存图书数量:          " + existAmount);
		overdueLabel.setText("超期图书数量:          " + overdueAmount);
		lossLabel.setText("丢失图书数量:          " + lossAmount);

		dialog.setTitle("图书统计--库存盘点--馆藏查询");
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

	private JLabel totalLabel;

	private JLabel timesLabel;

	private JLabel borrowLabel;

	private JLabel existLabel;

	private JLabel overdueLabel;

	private JLabel lossLabel;

	private JDialog dialog = null;
}