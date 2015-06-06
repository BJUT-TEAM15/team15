package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import net.sfte.htlibrary.database.HtConnection;

/**
 * This class used to display the reader's information. It alse serve as reader
 * borrow book query dialog.
 * 
 * @author wenwen
 */
public class ReaderInfoDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReaderInfoDialog() {
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();

		JToolBar toolBar = new JToolBar("ToolBar");
		toolBar.setFloatable(false);
		clearButton = new JButton("清除", new ImageIcon("images/clear.png"));
		clearButton.setBorderPainted(false);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initField();
			}
		});
		exitButton = new JButton("退出", new ImageIcon("images/exit.png"));
		exitButton.setBorderPainted(false);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		toolBar.add(clearButton);
		toolBar.addSeparator();
		toolBar.add(exitButton);
		add(toolBar, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		readerInfoPanel = new ReaderInfoPanel();
		readerIdField = readerInfoPanel.idField;
		readerIdField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillBookTable();
			}
		});
		readerIdField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				fillBookTable();
			}
		});
		JPanel messagePanel = new JPanel();
		messagePanel.add(new JLabel("<html>书是人类的精神食粮, "
				+ "是人进步<br>的阶梯, 请保管并爱护好图书 !</html>"));
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "友情提示"));
		panel.add(readerInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		panel.add(messagePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100,
				20).setInsets(5));
		add(panel, BorderLayout.WEST);

		JPanel borrowBookPanel = new JPanel();
		borrowBookPanel.setLayout(new GridBagLayout());
		borrowBookPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"读者借书表"));
		String[] columnNames = { "图书编号", "标准ISBN", "图书名称", "图书作者", "出版社",
				"图书价格", "借阅日期", "应还日期" };
		borrowBookTableModel = new DefaultTableModel(columnNames, 0);
		borrowBookTable = new JTable(borrowBookTableModel);
		borrowBookPanel.add(new JScrollPane(borrowBookTable), new GBC(0, 0)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));

		JPanel overdueBookPanel = new JPanel();
		overdueBookPanel.setLayout(new GridBagLayout());
		overdueBookPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"超期图书"));
		String[] colNames = { "图书编号", "标准ISBN", "图书名称", "图书价格", "借阅日期", "超期天数",
				"应还日期" };
		overdueBookTableModel = new DefaultTableModel(colNames, 0);
		overdueBookTable = new JTable(overdueBookTableModel);
		overdueBookPanel.add(new JScrollPane(overdueBookTable), new GBC(0, 0)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(borrowBookPanel, new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		mainPanel.add(overdueBookPanel, new GBC(0, 1).setFill(GBC.BOTH)
				.setWeight(100, 70).setInsets(5));
		add(mainPanel, BorderLayout.CENTER);
	}

	public void showDialog(Component parent) {
		initField();
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(exitButton);
			dialog.pack();
		}
		dialog.setTitle("读者信息与借阅情况查询");
		// set the Location of this dialog to the center
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension size = kit.getScreenSize();
		int width = size.width;
		int height = size.height;
		if (width <= 800)
			dialog.setSize(750, 600);
		else
			dialog.setSize(900, 640);
		dialog.setLocation((width - dialog.getWidth()) / 2, (height - dialog
				.getHeight()) / 2);
		dialog.setVisible(true);
	}

	private void initField() {
		readerInfoPanel.initField();
		clearTable();
	}

	private void clearTable() {
		int rowCount = borrowBookTableModel.getRowCount();
		for (int i = 0; i < rowCount; i++)
			borrowBookTableModel.removeRow(0);
		rowCount = overdueBookTableModel.getRowCount();
		for (int i = 0; i < rowCount; i++)
			overdueBookTableModel.removeRow(0);
	}

	private void fillBookTable() {
		if (readerIdField.getText().equals(""))
			return;
		clearTable();
		int readerId = ((Number) readerIdField.getValue()).intValue();
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT book.book_id, isbn, title, author, "
							+ "publisher, price, borrow_date, revertible_date "
							+ "FROM book, borrowbook "
							+ "WHERE reader_id = ? AND "
							+ "book.book_id = borrowbook.book_id");
			pstmt.setInt(1, readerId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int columnCount = rs.getMetaData().getColumnCount();
				Object[] values = new Object[columnCount];
				for (int i = 0; i < columnCount; i++)
					values[i] = rs.getObject(i + 1);
				borrowBookTableModel.addRow(values);
			}

			pstmt = con
					.prepareStatement("SELECT book.book_id, isbn, title, price, borrow_date, "
							+ "DATEDIFF('day', revertible_date, CURDATE()), revertible_date "
							+ "FROM book, borrowbook "
							+ "WHERE reader_id = ? AND book.book_id = borrowbook.book_id AND "
							+ "DATEDIFF('day', revertible_date, CURDATE()) > 0");
			pstmt.setInt(1, readerId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int columnCount = rs.getMetaData().getColumnCount();
				Object[] values = new Object[columnCount];
				for (int i = 0; i < columnCount; i++)
					values[i] = rs.getObject(i + 1);
				overdueBookTableModel.addRow(values);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private JButton clearButton;

	private JButton exitButton;

	private ReaderInfoPanel readerInfoPanel;

	private JFormattedTextField readerIdField;

	private DefaultTableModel borrowBookTableModel;

	private JTable borrowBookTable;

	private DefaultTableModel overdueBookTableModel;

	private JTable overdueBookTable;

	private JDialog dialog = null;
}