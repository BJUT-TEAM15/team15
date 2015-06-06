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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import net.sfte.htlibrary.database.BookOperation;
import net.sfte.htlibrary.database.HtConnection;

public class LossBookDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LossBookDialog() {
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();

		JToolBar toolBar = new JToolBar();
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
		messagePanel
				.setBorder(BorderFactory.createTitledBorder(etched, "注意事项"));
		messagePanel.add(new JLabel("<html>图书遗失责任重大, 必须罚款,"
				+ "<br>或者自己购买相同图书归还!</html>"));
		panel.add(readerInfoPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(
				100, 100).setInsets(5));
		panel.add(messagePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100,
				20).setInsets(5));
		add(panel, BorderLayout.WEST);

		String[] columnNames = { "图书编号", "标准ISBN", "图书名称", "作者", "出版社", "价格",
				"馆藏总数", "应还日期" };
		model = new DefaultTableModel(columnNames, 0);
		bookTable = new JTable(model);
		bookTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		lossButton = new JButton("确认图书遗失");
		lossButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = bookTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(dialog, "请提供遗失图书的基本信息!",
							"错误操作", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int lossBookId = ((Number) bookTable.getValueAt(selectedRow, 0))
						.intValue();
				int readerId = ((Number) readerIdField.getValue()).intValue();
				int re = JOptionPane.showConfirmDialog(dialog, "下面的操作将会删除编号为 "
						+ lossBookId + " 的图书, " + "请确认该图书是否真的已遗失!", "确认操作",
						JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (re != JOptionPane.OK_OPTION)
					return;
				double price = ((Number) bookTable.getValueAt(selectedRow, 5))
						.doubleValue();
				JOptionPane.showMessageDialog(dialog, "此书价格为: " + price
						+ " 罚款双倍: " + price * 2 + " 或者买书赔偿!", "图书遗失",
						JOptionPane.INFORMATION_MESSAGE);
				if (BookOperation.lossBook(readerId, lossBookId)) {
					JOptionPane.showMessageDialog(dialog,
							"遗失图书信息已记录, 读者借书记录也已删除!", "操作完成",
							JOptionPane.INFORMATION_MESSAGE);
					fillBookTable();
				} else {
					JOptionPane.showMessageDialog(dialog,
							"数据库出现错误, 请管理员查证读者借书表!", "操作失败",
							JOptionPane.ERROR_MESSAGE);
					fillBookTable();
				}
			}
		});
		JPanel bookPanel = new JPanel();
		bookPanel.setLayout(new GridBagLayout());
		bookPanel.setBorder(BorderFactory
				.createTitledBorder(etched, "图书遗失操作面板"));
		bookPanel.add(new JLabel("请读者选择遗失的图书: "), new GBC(0, 0).setAnchor(
				GBC.EAST).setInsets(5));
		bookPanel.add(lossButton, new GBC(1, 0).setAnchor(GBC.EAST)
				.setInsets(5));
		bookPanel.add(new JScrollPane(bookTable), new GBC(0, 1, 4, 4).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));
		add(bookPanel, BorderLayout.CENTER);
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
			dialog.getRootPane().setDefaultButton(lossButton);
			dialog.pack();
		}
		dialog.setTitle("图书遗失处理");
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
		int rowCount = model.getRowCount();
		for (int i = 0; i < rowCount; i++)
			model.removeRow(0);
	}

	private void fillBookTable() {
		if (readerIdField.getText().equals(""))
			return;
		int rowCount = model.getRowCount();
		for (int i = 0; i < rowCount; i++)
			model.removeRow(0);
		try {
			Connection con = HtConnection.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT book.book_id, isbn, title, author, "
							+ "publisher, price, total_amount, revertible_date "
							+ "FROM book, borrowbook "
							+ "WHERE reader_id = ? AND "
							+ "book.book_id = borrowbook.book_id "
							+ "ORDER BY book.book_id");
			int readerId = Integer.parseInt(readerIdField.getText());
			pstmt.setInt(1, readerId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int columnCount = rs.getMetaData().getColumnCount();
				Object[] values = new Object[columnCount];
				for (int i = 0; i < columnCount; i++)
					values[i] = rs.getObject(i + 1);
				model.addRow(values);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private ReaderInfoPanel readerInfoPanel;

	private JFormattedTextField readerIdField;

	private JButton clearButton;

	private JButton exitButton;

	private JButton lossButton;

	private DefaultTableModel model;

	private JTable bookTable;

	private JDialog dialog = null;
}