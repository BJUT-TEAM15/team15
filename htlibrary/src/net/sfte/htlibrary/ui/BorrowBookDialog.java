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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import net.sfte.htlibrary.database.BookOperation;
import net.sfte.htlibrary.database.HtConnection;

/**
 * This class build a dialog used to management borrow book
 * 
 * @author wenwen
 */
public class BorrowBookDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BorrowBookDialog() {
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();

		JToolBar toolBar = new JToolBar("toolBar");
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
				fillBorrowBookTable();
			}
		});
		readerIdField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				fillBorrowBookTable();
			}
		});
		JPanel messagePanel = new JPanel();
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "注意事项"));
		messagePanel.add(new JLabel("<html>本馆规定借书期限为两个月,<br>"
				+ "续借一个月. 最多可借八本图<br>书, 不允许重复借相同的图书</html>"));
		panel.add(readerInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		panel.add(messagePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100,
				20).setInsets(5));
		add(panel, BorderLayout.WEST);

		JPanel bookPanel = new JPanel();
		bookPanel.setLayout(new GridBagLayout());
		bookPanel.setBorder(BorderFactory.createTitledBorder(etched, "选择图书"));
		bookIdField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		bookIdField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkBookAndFillTable();
			}
		});
		okButton = new JButton("确定");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkBookAndFillTable();
			}
		});
		String[] columnNames = { "图书编号", "标准ISBN", "图书名称", "图书作者", "出版社",
				"图书价格", "现存数量" };
		bookModel = new DefaultTableModel(columnNames, 0);
		bookTable = new JTable(bookModel);
		bookPanel.add(new JLabel("图书编号: "), new GBC(0, 0, 1, 1).setAnchor(
				GBC.EAST).setInsets(5));
		bookPanel.add(bookIdField, new GBC(1, 0, 1, 1).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setAnchor(GBC.WEST).setInsets(5));
		bookPanel.add(okButton, new GBC(2, 0, 1, 1).setAnchor(GBC.WEST)
				.setInsets(5));
		bookPanel.add(new JLabel("    "), new GBC(3, 0, 4, 1).setFill(
				GBC.HORIZONTAL).setWeight(100, 0));
		bookPanel.add(new JScrollPane(bookTable), new GBC(0, 1, 7, 4).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));

		JPanel borrowPanel = new JPanel();
		borrowPanel.setLayout(new GridBagLayout());
		borrowPanel.setBorder(BorderFactory.createTitledBorder(etched, "借阅操作"));
		todayField = new JFormattedTextField(DateFormat.getDateInstance());
		todayField.setValue(new Date());
		borrowButton = new JButton("借阅图书");
		borrowButton.addActionListener(new BorrowBookListener());

		String[] names = { "图书编号", "图书名称", "图书作者", "出版社", "图书价格", "应还日期" };
		borrowBookModel = new DefaultTableModel(names, 0);
		borrowBookTable = new JTable(borrowBookModel);
		borrowPanel.add(new JLabel("借阅日期: "), new GBC(0, 0, 1, 1).setAnchor(
				GBC.EAST).setInsets(5));
		borrowPanel.add(todayField, new GBC(1, 0, 1, 1).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setAnchor(GBC.WEST).setInsets(5));
		borrowPanel.add(borrowButton, new GBC(2, 0, 1, 1).setAnchor(GBC.WEST)
				.setInsets(5));
		borrowPanel.add(new JLabel("    "), new GBC(3, 0, 4, 1).setFill(
				GBC.HORIZONTAL).setWeight(100, 0));
		borrowPanel.add(new JLabel("已借图书: "), new GBC(0, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		borrowPanel.add(new JScrollPane(borrowBookTable), new GBC(0, 2, 7, 2)
				.setFill(GBC.BOTH).setWeight(100, 100).setInsets(5));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(bookPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(100,
				60).setInsets(5));
		mainPanel.add(borrowPanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
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
			dialog.getRootPane().setDefaultButton(borrowButton);
			dialog.pack();
		}
		dialog.setTitle("图书借阅管理");
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

	private class BorrowBookListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!checkInput()) {
				JOptionPane.showMessageDialog(dialog,
						"请输入读者和所借书的完整信息, 否则无法借书!", "输入错误",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			int amount = borrowBookModel.getRowCount();
			Object[] objs = { "确定" };
			if (amount >= 8) {
				JOptionPane.showOptionDialog(dialog,
						"该读者已达到最大借书量8本, 暂时不能再借, 请先还书!", "拒绝借书",
						JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
						null, objs, objs[0]);
				return;
			}
			int remainAmount = ((Number) bookModel.getValueAt(0, 6)).intValue();
			if (remainAmount <= 0) {
				JOptionPane.showOptionDialog(dialog, "对不起, 该书目前在馆数量为0, 请以后再借!",
						"图书借空", JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE, null, objs, objs[0]);
				return;
			}
			int readerId = Integer.parseInt(readerIdField.getText());
			int bookId = Integer.parseInt(bookIdField.getText());
			if (BookOperation.borrowBook(readerId, bookId)) {
				JOptionPane.showOptionDialog(dialog, "借书成功, 请爱护并保管好图书!",
						"借书成功", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, objs, objs[0]);
				checkBookAndFillTable();
				fillBorrowBookTable();
			} else {
				JOptionPane.showOptionDialog(dialog,
						"非常抱歉, 借书失败, 本馆不允许同时借相同的书!", "借书失败",
						JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
						null, objs, objs[0]);
			}
		}
	}

	private boolean checkInput() {
		if (readerIdField.getText().equals(""))
			return false;
		if (bookIdField.getText().equals(""))
			return false;
		if (bookModel.getRowCount() != 1)
			return false;
		return true;
	}

	private void initField() {
		readerInfoPanel.initField();
		bookIdField.setValue(null);
		int count = bookModel.getRowCount();
		for (int i = 0; i < count; i++)
			bookModel.removeRow(0);
		count = borrowBookModel.getRowCount();
		for (int i = 0; i < count; i++)
			borrowBookModel.removeRow(0);
		todayField.setValue(new Date());
	}

	private void checkBookAndFillTable() {
		int count = bookModel.getRowCount();
		for (int i = 0; i < count; i++)
			bookModel.removeRow(0);
		if (bookIdField.getText().equals(""))
			return;
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT book_id, isbn, title, author, "
							+ "publisher, price, amount FROM book "
							+ "WHERE book_id = ?");
			pstmt.setInt(1, Integer.parseInt(bookIdField.getText()));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int colCount = rs.getMetaData().getColumnCount();
				Object[] values = new Object[colCount];
				for (int i = 0; i < colCount; i++)
					values[i] = rs.getObject(i + 1);
				bookModel.addRow(values);
			} else {
				JOptionPane.showMessageDialog(dialog, "不存在此编号的图书, 请仔细检查图书!",
						"无效编号", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void fillBorrowBookTable() {
		if (readerIdField.getText().equals(""))
			return;
		int rowCount = borrowBookModel.getRowCount();
		for (int i = 0; i < rowCount; i++)
			borrowBookModel.removeRow(0);
		Connection con = null;
		try {
			con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT book.book_id, title, author, "
							+ "publisher, price , revertible_date "
							+ "FROM book, borrowbook "
							+ "WHERE reader_id = ? AND borrowbook.book_id = book.book_id "
							+ "ORDER BY revertible_date");
			pstmt.setInt(1, Integer.parseInt(readerIdField.getText()));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int columnCount = rs.getMetaData().getColumnCount();
				Object[] values = new Object[columnCount];
				for (int i = 0; i < columnCount; i++)
					values[i] = rs.getObject(i + 1);
				borrowBookModel.addRow(values);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private JFormattedTextField readerIdField;

	private JButton clearButton;

	private JButton exitButton;

	private JFormattedTextField bookIdField;

	private JButton okButton;

	private DefaultTableModel bookModel;

	private JTable bookTable;

	private DefaultTableModel borrowBookModel;

	private JTable borrowBookTable;

	private JButton borrowButton;

	private JFormattedTextField todayField;

	private ReaderInfoPanel readerInfoPanel;

	private JDialog dialog = null;
}