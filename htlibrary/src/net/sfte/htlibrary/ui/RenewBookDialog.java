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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;

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
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import net.sfte.htlibrary.database.BookOperation;
import net.sfte.htlibrary.database.HtConnection;

public class RenewBookDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RenewBookDialog() {
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
				initBookInfoField();
				fillBorrowBookTable();
			}
		});
		readerIdField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				initBookInfoField();
				fillBorrowBookTable();
			}
		});
		JPanel messagePanel = new JPanel();
		messagePanel.add(new JLabel("续借期限为一个月, 可以再次续借!"));
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "续借规定"));
		panel.add(readerInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		panel.add(messagePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100,
				20).setInsets(5));
		add(panel, BorderLayout.WEST);

		bookIdField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		bookIdField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				setBookInfoField();
			}

			public void removeUpdate(DocumentEvent e) {
				setBookInfoField();
			}

			public void changedUpdate(DocumentEvent e) {
			}
		});
		titleField = new JTextField("");
		titleField.setEditable(false);
		authorField = new JTextField("");
		authorField.setEditable(false);
		publisherField = new JTextField("");
		publisherField.setEditable(false);
		priceField = new JFormattedTextField(NumberFormat.getNumberInstance());
		priceField.setEditable(false);
		borrowDateField = new JFormattedTextField(DateFormat.getDateInstance());
		borrowDateField.setEditable(false);
		revertibleDateField = new JFormattedTextField(DateFormat
				.getDateInstance());
		revertibleDateField.setEditable(false);

		renewButton = new JButton("续借图书");
		renewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkInput()) {
					JOptionPane.showMessageDialog(dialog,
							"请提供读者和图书完整信息, 否则无法续借", "操作失败",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				int readerId = Integer.parseInt(readerIdField.getText());
				int bookId = Integer.parseInt(bookIdField.getText());
				int overdueDays = BookOperation
						.getOverdueDays(readerId, bookId);
				// This book is over time, muse be returned back.
				if (overdueDays > 0) {
					JOptionPane.showMessageDialog(dialog,
							"该书已经超期, 所以无法续借, 请管理员进行还书操作", "图书超期",
							JOptionPane.ERROR_MESSAGE);
					dialog.setVisible(false);
					new GiveBackBookDialog().showDialog(parent);
					return;
				}
				if (BookOperation.renewBook(readerId, bookId)) {
					JOptionPane.showMessageDialog(dialog,
							"图书续借成功, 还书期限延长一个月, 请牢记还书日期!", "续借成功",
							JOptionPane.INFORMATION_MESSAGE);
					fillBorrowBookTable();
				} else {
					JOptionPane.showMessageDialog(dialog,
							"非常抱歉, 图书续借失败, 请管理员检查读者借书表!", "操作失败",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JPanel bookInfoPanel = new JPanel();
		bookInfoPanel.setLayout(new GridBagLayout());
		bookInfoPanel.setBorder(BorderFactory
				.createTitledBorder(etched, "续借图书"));
		bookInfoPanel.add(new JLabel("图书编号: "), new GBC(0, 0).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(bookIdField, new GBC(1, 0).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(renewButton, new GBC(2, 0).setAnchor(GBC.WEST)
				.setInsets(5));
		bookInfoPanel.add(new JLabel("   "), new GBC(3, 0).setFill(
				GBC.HORIZONTAL).setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("图书名称: "), new GBC(0, 1).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(titleField, new GBC(1, 1).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("图书作者: "), new GBC(2, 1).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(authorField, new GBC(3, 1).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("出版社: "), new GBC(0, 2)
				.setAnchor(GBC.EAST).setInsets(5));
		bookInfoPanel.add(publisherField, new GBC(1, 2).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("图书价格: "), new GBC(2, 2).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(priceField, new GBC(3, 2).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("应还日期: "), new GBC(0, 3).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(revertibleDateField, new GBC(1, 3).setFill(
				GBC.HORIZONTAL).setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("借书日期: "), new GBC(2, 3).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(borrowDateField, new GBC(3, 3)
				.setFill(GBC.HORIZONTAL).setWeight(30, 0).setInsets(5));

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridBagLayout());
		tablePanel.setBorder(BorderFactory.createTitledBorder(etched, "读者借书表"));
		String[] columnNames = { "图书编号", "图书名称", "图书作者", "出版社", "图书价格", "借书日期",
				"应还日期" };
		borrowBookModel = new DefaultTableModel(columnNames, 0);
		borrowBookTable = new JTable(borrowBookModel);
		borrowBookTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		borrowBookTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = borrowBookTable.getSelectedRow();
				if (row != -1) {
					Object obj = null;
					obj = borrowBookTable.getValueAt(row, 0);
					bookIdField.setValue(obj);
					obj = borrowBookTable.getValueAt(row, 1);
					titleField.setText(obj.toString());
					obj = borrowBookTable.getValueAt(row, 2);
					authorField.setText(obj.toString());
					obj = borrowBookTable.getValueAt(row, 3);
					publisherField.setText(obj.toString());
					obj = borrowBookTable.getValueAt(row, 4);
					priceField.setValue(obj);
					obj = borrowBookTable.getValueAt(row, 5);
					borrowDateField.setValue(obj);
					obj = borrowBookTable.getValueAt(row, 6);
					revertibleDateField.setValue(obj);
				}
			}
		});
		tablePanel.add(new JScrollPane(borrowBookTable), new GBC(0, 0).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(bookInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 10).setInsets(5));
		mainPanel.add(tablePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(
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
		this.parent = owner;
		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(renewButton);
			dialog.pack();
		}
		dialog.setTitle("图书续借管理");
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
		initBookInfoField();
		int rowCount = borrowBookModel.getRowCount();
		for (int i = 0; i < rowCount; i++)
			borrowBookModel.removeRow(0);
	}

	private void initBookInfoField() {
		bookIdField.setValue(null);
		titleField.setText("");
		authorField.setText("");
		publisherField.setText("");
		priceField.setValue(null);
		borrowDateField.setValue(null);
		revertibleDateField.setValue(null);
	}

	private boolean checkInput() {
		if (bookIdField.getValue() == null)
			return false;
		if (titleField.getText().trim().equals(""))
			return false;
		return true;
	}

	private void setBookInfoField() {
		try {
			int bookId = Integer.parseInt(bookIdField.getText().trim());
			int rowCount = borrowBookModel.getRowCount();
			boolean hasThisBook = false;
			for (int i = 0; i < rowCount; i++) {
				Object obj = null;
				obj = borrowBookModel.getValueAt(i, 0);
				int id = ((Number) obj).intValue();
				if (bookId == id) {
					hasThisBook = true;
					obj = borrowBookTable.getValueAt(i, 1);
					titleField.setText(obj.toString());
					obj = borrowBookTable.getValueAt(i, 2);
					authorField.setText(obj.toString());
					obj = borrowBookTable.getValueAt(i, 3);
					publisherField.setText(obj.toString());
					obj = borrowBookTable.getValueAt(i, 4);
					priceField.setValue(obj);
					obj = borrowBookTable.getValueAt(i, 5);
					borrowDateField.setValue(obj);
					obj = borrowBookTable.getValueAt(i, 6);
					revertibleDateField.setValue(obj);
				}
			}
			if (!hasThisBook) {
				titleField.setText("");
				authorField.setText("");
				publisherField.setText("");
				priceField.setValue(null);
				borrowDateField.setValue(null);
				revertibleDateField.setValue(null);
			}
		} catch (NumberFormatException e) {
			// do not set the book info field.
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
					.prepareStatement("SELECT book.book_id, title, author, publisher, "
							+ "price, borrow_date, revertible_date "
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

	private JButton clearButton;

	private JButton exitButton;

	private JButton renewButton;

	private ReaderInfoPanel readerInfoPanel;

	private JFormattedTextField readerIdField;

	private JFormattedTextField bookIdField;

	private JTextField titleField;

	private JTextField authorField;

	private JTextField publisherField;

	private JFormattedTextField priceField;

	private JFormattedTextField borrowDateField;

	private JFormattedTextField revertibleDateField;

	private DefaultTableModel borrowBookModel;

	private JTable borrowBookTable;

	private JDialog dialog = null;
	
	private Frame parent = null;
}