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

/**
 * This class make up a return back book dialog.
 * 
 * @author wenwen
 */
public class GiveBackBookDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GiveBackBookDialog() {
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();

		JToolBar toolBar = new JToolBar("toolBar");
		toolBar.setFloatable(false);
		clearButton = new JButton("���", new ImageIcon("images/clear.png"));
		clearButton.setBorderPainted(false);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initField();
			}
		});
		exitButton = new JButton("�˳�", new ImageIcon("images/exit.png"));
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

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
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
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "ע������"));
		messagePanel.add(new JLabel("<html>���ݹ涨��������Ϊ������,<br>"
				+ "����һ����. ����ÿ�췣��һ<br>ëǮ, ��ʧ����ͼ�齫�ϳ�</html>"));
		leftPanel.add(readerInfoPanel, new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(100, 100).setInsets(5));
		leftPanel.add(messagePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(
				100, 20).setInsets(5));
		add(leftPanel, BorderLayout.WEST);

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
		returnButton = new JButton("�黹ͼ��");
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkInput()) {
					JOptionPane.showMessageDialog(dialog,
							"���ṩ���ߺ�Ҫ�黹��ͼ����ϸ��Ϣ, �����޷�����!", "�������",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int readerId = Integer.parseInt(readerIdField.getText());
				int bookId = Integer.parseInt(bookIdField.getText());
				int overdueDays = BookOperation
						.getOverdueDays(readerId, bookId);
				// This book has overdues. And must pay the amercement.
				if (overdueDays > 0) {
					JOptionPane.showMessageDialog(dialog, "�����ѳ��� "
							+ overdueDays + "��, ���涨�跣�� " + (float) overdueDays
							/ 10 + "Ԫ! " + "�����Աȷ�����·���!", "���ڷ���",
							JOptionPane.INFORMATION_MESSAGE);
				}
				if (BookOperation.giveBackBook(readerId, bookId)) {
					JOptionPane.showMessageDialog(dialog, "��������ɹ����!", "�������",
							JOptionPane.INFORMATION_MESSAGE);
					fillBorrowBookTable();
					initBookInfoField();
				} else {
					JOptionPane.showMessageDialog(dialog,
							"�ǳ���Ǹ, ������ִ���, ������߽����!", "����ʧ��",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JPanel bookInfoPanel = new JPanel();
		bookInfoPanel.setLayout(new GridBagLayout());
		bookInfoPanel.setBorder(BorderFactory
				.createTitledBorder(etched, "�黹ͼ��"));
		bookInfoPanel.add(new JLabel("ͼ����: "), new GBC(0, 0).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(bookIdField, new GBC(1, 0).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(returnButton, new GBC(2, 0).setAnchor(GBC.WEST)
				.setInsets(5));
		bookInfoPanel.add(new JLabel("   "), new GBC(3, 0).setFill(
				GBC.HORIZONTAL).setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("ͼ������: "), new GBC(0, 1).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(titleField, new GBC(1, 1).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("ͼ������: "), new GBC(2, 1).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(authorField, new GBC(3, 1).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("������: "), new GBC(0, 2)
				.setAnchor(GBC.EAST).setInsets(5));
		bookInfoPanel.add(publisherField, new GBC(1, 2).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("ͼ��۸�: "), new GBC(2, 2).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(priceField, new GBC(3, 2).setFill(GBC.HORIZONTAL)
				.setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("Ӧ������: "), new GBC(0, 3).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(revertibleDateField, new GBC(1, 3).setFill(
				GBC.HORIZONTAL).setWeight(30, 0).setInsets(5));

		bookInfoPanel.add(new JLabel("��������: "), new GBC(2, 3).setAnchor(
				GBC.EAST).setInsets(5));
		bookInfoPanel.add(borrowDateField, new GBC(3, 3)
				.setFill(GBC.HORIZONTAL).setWeight(30, 0).setInsets(5));

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridBagLayout());
		tablePanel.setBorder(BorderFactory.createTitledBorder(etched, "���߽����"));
		String[] columnNames = { "ͼ����", "ͼ������", "ͼ������", "������", "ͼ��۸�", "��������",
				"Ӧ������" };
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

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(bookInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(100,
				10).setInsets(5));
		panel.add(tablePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100,
				100).setInsets(5));
		add(panel, BorderLayout.CENTER);
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
			// dialog.getRootPane().setDefaultButton(returnButton);
			dialog.pack();
		}
		dialog.setTitle("ͼ��黹����");
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

	private JButton clearButton;

	private JButton exitButton;

	private ReaderInfoPanel readerInfoPanel;

	private JFormattedTextField readerIdField;

	private DefaultTableModel borrowBookModel;

	private JTable borrowBookTable;

	private JFormattedTextField bookIdField;

	private JTextField titleField;

	private JTextField authorField;

	private JTextField publisherField;

	private JFormattedTextField priceField;

	private JFormattedTextField borrowDateField;

	private JFormattedTextField revertibleDateField;

	private JButton returnButton;

	private JDialog dialog = null;
}