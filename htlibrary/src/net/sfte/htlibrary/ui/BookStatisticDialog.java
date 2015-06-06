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
		setBorder(BorderFactory.createTitledBorder(etched, "ͳ�ƽ��"));

		totalLabel = new JLabel("ͼ����ܲ���:          ", JLabel.CENTER);
		timesLabel = new JLabel("����ͼ�����:          ", JLabel.CENTER);
		borrowLabel = new JLabel("�ڽ�ͼ������:          ", JLabel.CENTER);
		existLabel = new JLabel("�ִ�ͼ������:          ", JLabel.CENTER);
		overdueLabel = new JLabel("����ͼ������:          ", JLabel.CENTER);
		lossLabel = new JLabel("��ʧͼ������:          ", JLabel.CENTER);
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
		totalLabel.setText("ͼ����ܲ���:          " + totalAmount);
		timesLabel.setText("����ͼ�����:          " + times);
		borrowLabel.setText("�ڽ�ͼ������:          " + borrowAmount);
		existLabel.setText("�ִ�ͼ������:          " + existAmount);
		overdueLabel.setText("����ͼ������:          " + overdueAmount);
		lossLabel.setText("��ʧͼ������:          " + lossAmount);

		dialog.setTitle("ͼ��ͳ��--����̵�--�ݲز�ѯ");
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