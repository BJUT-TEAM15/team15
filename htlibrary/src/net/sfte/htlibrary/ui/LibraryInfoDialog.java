package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * This class defines the Library Infomation dialog. Users can custom theirs
 * Library's infomation. The Library Infomation is stored in a file named
 * "LibraryInfo".
 * 
 * @author wenwen
 */
public class LibraryInfoDialog extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * The main panel of the LibraryInfoDialog.
	 * 
	 * @param infos
	 *            user's Library information
	 */
	public LibraryInfoDialog() {
		infos = new String[7];
		// read library Information from File "LibraryInfo"
		infos = readInformation();
		setLayout(new BorderLayout());

		// Construct a panel to hold all these infomation.
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		libraryName = new JTextField(infos[0]);
		curatorField = new JTextField(infos[1]);

		DateFormat format = DateFormat.getDateInstance();
		format.setLenient(false);
		establishDate = new JFormattedTextField(format);
		establishDate.setInputVerifier(new InputVerifier() {
			public boolean verify(JComponent component) {
				JFormattedTextField field = (JFormattedTextField) component;
				return field.isEditValid();
			}
		});
		StringTokenizer stk = new StringTokenizer(infos[2], "-");
		int year = Integer.parseInt(stk.nextToken());
		int month = Integer.parseInt(stk.nextToken());
		int day = Integer.parseInt(stk.nextToken());
		GregorianCalendar g = new GregorianCalendar(year, month - 1, day);
		establishDate.setValue(g.getTime());

		phoneField = new JTextField(infos[3]);
		emailField = new JTextField(infos[4]);
		addressField = new JTextField(infos[5]);

		summaryArea = new JTextArea();
		summaryArea.setText(infos[6]);
		summaryArea.setLineWrap(true);

		panel.add(new JLabel("馆         名: "), new GBC(0, 0)
				.setAnchor(GBC.EAST).setInsets(5));
		panel.add(libraryName, new GBC(1, 0, 3, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		panel.add(new JLabel("馆         长: "), new GBC(0, 1)
				.setAnchor(GBC.EAST).setInsets(5));
		panel.add(curatorField, new GBC(1, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		panel.add(new JLabel("建馆时间: "), new GBC(2, 1).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(establishDate, new GBC(3, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		panel.add(new JLabel("联系电话: "), new GBC(0, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(phoneField, new GBC(1, 2).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("联系邮箱: "), new GBC(2, 2).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(emailField, new GBC(3, 2).setFill(GBC.HORIZONTAL).setWeight(
				100, 0).setInsets(5));
		panel.add(new JLabel("联系地址: "), new GBC(0, 3).setAnchor(GBC.EAST)
				.setInsets(5));
		panel.add(addressField, new GBC(1, 3, 3, 1).setFill(GBC.HORIZONTAL)
				.setWeight(100, 0).setInsets(5));
		panel.add(new JLabel("简         介: "), new GBC(0, 4, 1, 1).setAnchor(
				GBC.EAST).setInsets(5));
		panel.add(new JScrollPane(summaryArea), new GBC(1, 4, 3, 4).setFill(
				GBC.BOTH).setWeight(100, 100).setInsets(5));
		panel.setBorder(BorderFactory.createEtchedBorder());
		add(panel, BorderLayout.CENTER);

		Box buttonBox = Box.createHorizontalBox();
		saveButton = new JButton("保    存");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infos[0] = libraryName.getText();
				infos[1] = curatorField.getText();
				infos[2] = establishDate.getText();
				infos[3] = phoneField.getText();
				infos[4] = emailField.getText();
				infos[5] = addressField.getText();
				infos[6] = summaryArea.getText();
				try {
					PrintWriter out = new PrintWriter(new FileWriter(
							"LibraryInfo"));
					for (int i = 0; i < infos.length; i++) {
						if (i == 6)
							out.print(infos[i]);
						else
							out.println(infos[i]);
					}
					// errors occured.
					if (out.checkError()) {
						JOptionPane.showMessageDialog(null, "非常抱歉, 保存图书馆信息失败",
								"写入文件失败", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "成功保存图书馆信息",
								"操作完成", JOptionPane.INFORMATION_MESSAGE);
						dialog.setVisible(false);
					}
				} catch (IOException exception) {
					exception.printStackTrace();
					JOptionPane.showMessageDialog(null, "非常抱歉, 保存图书馆信息失败",
							"写入文件失败", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		exitButton = new JButton("退    出");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		buttonBox.add(Box.createHorizontalStrut(300));
		buttonBox.add(saveButton);
		buttonBox.add(Box.createHorizontalStrut(40));
		buttonBox.add(exitButton);
		add(buttonBox, BorderLayout.SOUTH);
	}

	public void showDialog(Component parent) {
		infos = readInformation();
		libraryName.setText(infos[0]);
		curatorField.setText(infos[1]);
		StringTokenizer stk = new StringTokenizer(infos[2], "-");
		int year = Integer.parseInt(stk.nextToken());
		int month = Integer.parseInt(stk.nextToken());
		int day = Integer.parseInt(stk.nextToken());
		GregorianCalendar g = new GregorianCalendar(year, month - 1, day);
		establishDate.setValue(g.getTime());
		phoneField.setText(infos[3]);
		emailField.setText(infos[4]);
		addressField.setText(infos[5]);
		summaryArea.setText(infos[6]);
		// locate the owner frame
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class,
					parent);
		/*
		 * if first time show this dialog, or if owner has changed, construct a
		 * new one.
		 */
		if (dialog == null || dialog.getOwner() != owner) {
			// a model dialog
			dialog = new JDialog(owner, true);
			dialog.add(this);
			/*
			 * Set the default button as okButton, so you can fill the username
			 * and password then simply press Enter key.
			 */
			dialog.getRootPane().setDefaultButton(saveButton);
			dialog.pack();
		}
		dialog.setTitle("图书馆信息");
		dialog.setSize(500, 350);
		dialog.setResizable(false);
		// set the Location of this dialog to the center
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension size = kit.getScreenSize();
		int width = size.width;
		int height = size.height;
		dialog.setLocation((width - dialog.getWidth()) / 2, (height - dialog
				.getHeight()) / 2);
		dialog.setVisible(true);
	}

	/**
	 * read the library information from local file.
	 */
	public String[] readInformation() {
		String[] information = new String[7];
		try {
			Scanner sc = new Scanner(new FileInputStream("LibraryInfo"));
			// read the first six row.
			for (int i = 0; i < infos.length - 1; i++)
				information[i] = sc.nextLine();
			// read the summary of the library, It might be multi row.
			StringBuilder builder = new StringBuilder("");
			while (sc.hasNextLine()) {
				builder.append(sc.nextLine());
				if (sc.hasNextLine())
					builder.append("\n");
			}
			// The seventh element of information
			information[6] = builder.toString();
		} catch (FileNotFoundException e) {
			System.err.println("File LibraryInfo NOT Found");
			e.printStackTrace();
		}
		return information;
	}

	private String[] infos;

	private JTextField libraryName;

	private JTextField curatorField;

	private JFormattedTextField establishDate;

	private JTextField phoneField;

	private JTextField emailField;

	private JTextField addressField;

	private JTextArea summaryArea;

	private JButton saveButton;

	private JButton exitButton;

	private JDialog dialog = null;
}