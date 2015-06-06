package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import net.sfte.htlibrary.database.HtConnection;

public class ListOrderDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListOrderDialog() {
		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();

		JToolBar toolBar = new JToolBar("ToolBar");
		toolBar.setFloatable(false);
		printButton = new JButton("打印", new ImageIcon("images/print.png"));
		printButton.setBorderPainted(false);
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					bookTable.print();
					readerTable.print();
				} catch (PrinterException exce) {
					exce.printStackTrace();
				}
			}
		});
		exitButton = new JButton("退出", new ImageIcon("images/exit.png"));
		exitButton.setBorderPainted(false);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		toolBar.add(printButton);
		toolBar.addSeparator();
		toolBar.add(exitButton);
		add(toolBar, BorderLayout.NORTH);

		readerQuery = "SELECT TOP 15 reader_id, student_id, reader_name, "
				+ "age, sex, academy, department, register_date, book_amount "
				+ "FROM reader " + "ORDER BY book_amount DESC";
		readerColumnNames = new String[] { "读者编号", "学号", "姓名", "年龄", "性别",
				"学院", "系别", "注册日期", "总借书量" };
		readerTableModel = HtConnection.getTableModel(readerQuery,
				readerColumnNames);
		readerTable = new JTable(readerTableModel);
		JPanel readerPanel = new JPanel();
		readerPanel.setLayout(new GridBagLayout());
		readerPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"读者借书排行榜 (前15位) "));
		readerPanel.add(new JScrollPane(readerTable), new GBC(0, 0).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));

		bookQuery = "SELECT TOP 10 book_id, isbn, title, author, publisher, "
				+ "price, total_amount, amount, add_date, borrowed_times "
				+ "FROM book " + "ORDER BY borrowed_times DESC";
		bookColumnNames = new String[] { "图书编号", "标准ISBN", "图书名称", "图书作者",
				"出版社", "图书价格", "馆藏总量", "现存数量", "入馆日期", "被借次数" };
		bookTableModel = HtConnection.getTableModel(bookQuery, bookColumnNames);
		bookTable = new JTable(bookTableModel);
		JPanel bookPanel = new JPanel();
		bookPanel.setLayout(new GridBagLayout());
		bookPanel.setBorder(BorderFactory.createTitledBorder(etched,
				"图书借阅排行榜 (前10位) "));
		bookPanel.add(new JScrollPane(bookTable), new GBC(0, 0).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(bookPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(100, 70)
				.setInsets(5));
		panel.add(readerPanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100,
				100).setInsets(5));
		add(panel, BorderLayout.CENTER);
	}

	public void showDialog(Component parent) {
		// update the two tables.
		bookTable.setModel(HtConnection.getTableModel(bookQuery,
				bookColumnNames));
		readerTable.setModel(HtConnection.getTableModel(readerQuery,
				readerColumnNames));
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
		dialog.setTitle("读者--图书借阅排行榜");
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

	private JButton printButton;

	private JButton exitButton;

	private ResultSetTableModel readerTableModel;

	private String readerQuery;

	private String[] readerColumnNames;

	private JTable readerTable;

	private ResultSetTableModel bookTableModel;

	private String bookQuery;

	private String[] bookColumnNames;

	private JTable bookTable;

	private JDialog dialog = null;
}