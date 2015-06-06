package net.sfte.htlibrary.ui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import net.sfte.htlibrary.ui.action.AboutAction;
import net.sfte.htlibrary.ui.action.AdminSettingAction;
import net.sfte.htlibrary.ui.action.BookStatisticAction;
import net.sfte.htlibrary.ui.action.BorrowBookAction;
import net.sfte.htlibrary.ui.action.ChangeImageAction;
import net.sfte.htlibrary.ui.action.ChangePasswordAction;
import net.sfte.htlibrary.ui.action.ChangeStatusBarAction;
import net.sfte.htlibrary.ui.action.CheckStorageAction;
import net.sfte.htlibrary.ui.action.DeleteReaderAction;
import net.sfte.htlibrary.ui.action.ExitAction;
import net.sfte.htlibrary.ui.action.GiveBackBookAction;
import net.sfte.htlibrary.ui.action.HangupAction;
import net.sfte.htlibrary.ui.action.LibraryInfoAction;
import net.sfte.htlibrary.ui.action.ListOrderAction;
import net.sfte.htlibrary.ui.action.LossBookAction;
import net.sfte.htlibrary.ui.action.LostBookQueryAction;
import net.sfte.htlibrary.ui.action.ModifyBookAction;
import net.sfte.htlibrary.ui.action.ModifyReaderAction;
import net.sfte.htlibrary.ui.action.NewBookAction;
import net.sfte.htlibrary.ui.action.OverdueBookAction;
import net.sfte.htlibrary.ui.action.ReaderInfoAction;
import net.sfte.htlibrary.ui.action.ReaderRegisterAction;
import net.sfte.htlibrary.ui.action.RenewBookAction;
import net.sfte.htlibrary.ui.action.SwitchUserAction;
import net.sfte.htlibrary.ui.action.SystemHelpAction;
import net.sfte.htlibrary.ui.action.TodayAction;
import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

/**
 * This is the main frame of htLibrary system.
 * 
 * @author wenwen
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public MainFrame() {
		setFrameAttributes();

		// set the menu
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		createSystemOptionMenu();
		createBookManagementMenu();
		createBookOperationMenu();
		createReaderManagementMenu();
		createSystemSearchMenu();
		createStatisticMenu();
		createHelpMenu();

		// the toolBar of htlibrary
		createToolBar();

		createMainOperationPane();

		// main panel of htlibrary
		createMainPanel();

		createPopupMenu();

		// the status bar of htlibrary
		add(statusBar, BorderLayout.SOUTH);
		JPopupMenu statusBarPopupMenu = new JPopupMenu();
		statusBarPopupMenu.add(new ChangeStatusBarAction(this, statusBar));
		statusBar.setComponentPopupMenu(statusBarPopupMenu);
		// Java bug 4966109. Java 6.0 fixed it.
		statusBar.addMouseListener(new MouseAdapter() {    });

		// call this when you clicked close button of the window
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (exitDialog == null)
					exitDialog = new ExitDialog();
				exitDialog.showDialog(MainFrame.this);
			}
		});
	}

	private void createPopupMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		createPopupMenuItem(popupMenu, new BorrowBookAction(this), "ͼ�����",
				KeyEvent.VK_B);
		createPopupMenuItem(popupMenu, new GiveBackBookAction(this), "ͼ��黹",
				KeyEvent.VK_G);
		createPopupMenuItem(popupMenu, new RenewBookAction(this), "ͼ������",
				KeyEvent.VK_R);
		popupMenu.addSeparator();
		createPopupMenuItem(popupMenu, new ReaderInfoAction(this), "������Ϣ",
				KeyEvent.VK_I);
		createPopupMenuItem(popupMenu, new ReaderInfoAction(this), "���ߵǼ�");
		createPopupMenuItem(popupMenu, new ReaderInfoAction(this), "����ɾ��");
		popupMenu.addSeparator();
		createPopupMenuItem(popupMenu, new SystemHelpAction(this), "������Ϣ",
				KeyEvent.VK_H);
		createPopupMenuItem(popupMenu, new ChangeImageAction(this,
				ChangeImageAction.mainPanel), "��������ͼƬ");
		mainPanel.setComponentPopupMenu(popupMenu);
		// Java bug 4966109. Java 6.0 fixed it.
		mainPanel.addMouseListener(new MouseAdapter() {    });
	}

	private void createPopupMenuItem(JPopupMenu popupMenu, Action action,
			String name, int keyCode) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(keyCode,
				KeyEvent.CTRL_MASK));
		menuItem.addActionListener(action);
		popupMenu.add(menuItem);
	}

	private void createPopupMenuItem(JPopupMenu popupMenu, Action action,
			String name) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.addActionListener(action);
		popupMenu.add(menuItem);
	}

	private void createMainPanel() {
		mainPanel = new MainPanel();
		add(mainPanel, BorderLayout.CENTER);
	}

	private void createMainOperationPane() {
		MainOperationPane mainOperationPane = new MainOperationPane(this);
		Box mainBox = mainOperationPane.getPane();
		add(mainBox, BorderLayout.WEST);
		mainBox.setBorder(BorderFactory.createEtchedBorder());
	}

	private void createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);

		JButton adminButton = new JButton(new AdminSettingAction(this));
		setButton(adminButton);
		toolBar.add(adminButton);

		JButton changePasswordButton = new JButton(new ChangePasswordAction(
				this));
		setButton(changePasswordButton);
		toolBar.add(changePasswordButton);

		JButton hangupButton = new JButton(new HangupAction(this));
		setButton(hangupButton);
		toolBar.add(hangupButton);

		toolBar.addSeparator();

		JButton checkStorageButton = new JButton(new CheckStorageAction(this));
		setButton(checkStorageButton);
		toolBar.add(checkStorageButton);

		JButton todayButton = new JButton(new TodayAction(this));
		setButton(todayButton);
		toolBar.add(todayButton);

		JButton listOrderButton = new JButton(new ListOrderAction(this));
		setButton(listOrderButton);
		toolBar.add(listOrderButton);

		toolBar.addSeparator();

		JButton aboutButton = new JButton(new AboutAction(this));
		setButton(aboutButton);
		toolBar.add(aboutButton);

		JButton exitButton = new JButton(new ExitAction(this));
		setButton(exitButton);
		toolBar.add(exitButton);
	}

	private void createHelpMenu() {
		// Help Menu.
		JMenu helpMenu = new JMenu("ϵͳ����(H)");
		helpMenu.setMnemonic('H');
		menuBar.add(helpMenu);

		JMenuItem helpItem = new JMenuItem("ʹ�ð���");
		helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				InputEvent.CTRL_MASK));
		helpItem.addActionListener(new SystemHelpAction(this));
		helpMenu.add(helpItem);

		helpMenu.addSeparator();

		JMenuItem aboutItem = new JMenuItem("����");
		aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				InputEvent.CTRL_MASK));
		aboutItem.addActionListener(new AboutAction(this));
		helpMenu.add(aboutItem);
	}

	private void createStatisticMenu() {
		// statistic Menu.
		JMenu statisticMenu = new JMenu("ͳ������(S)");
		statisticMenu.setMnemonic('S');
		menuBar.add(statisticMenu);

		// book order statistic Menu Item.
		JMenuItem bookOrderItem = new JMenuItem("ͼ���������");
		bookOrderItem.addActionListener(new ListOrderAction(this));
		statisticMenu.add(bookOrderItem);

		JMenuItem readerOrderItem = new JMenuItem("���߽�������");
		readerOrderItem.addActionListener(new ListOrderAction(this));
		statisticMenu.add(readerOrderItem);
	}

	private void createSystemSearchMenu() {
		// System search menu
		JMenu systemQueryMenu = new JMenu("ϵͳ��ѯ(Q)");
		systemQueryMenu.setMnemonic('Q');
		menuBar.add(systemQueryMenu);

		// library storage info query.
		JMenuItem libraryQueryItem = new JMenuItem("�ݲ���Ϣ��ѯ");
		libraryQueryItem.addActionListener(new BookStatisticAction(this));
		systemQueryMenu.add(libraryQueryItem);

		systemQueryMenu.addSeparator();

		JMenuItem overdueBookQueryItem = new JMenuItem("ͼ�鳬�ڲ�ѯ");
		overdueBookQueryItem.addActionListener(new OverdueBookAction(this));
		systemQueryMenu.add(overdueBookQueryItem);

		JMenuItem lostBookQueryItem = new JMenuItem("ͼ�鶪ʧ��ѯ");
		lostBookQueryItem.addActionListener(new LostBookQueryAction(this));
		systemQueryMenu.add(lostBookQueryItem);

		systemQueryMenu.addSeparator();

		JMenuItem readerBookQueryItem = new JMenuItem("���߽��Ĳ�ѯ");
		readerBookQueryItem.addActionListener(new ReaderInfoAction(this));
		systemQueryMenu.add(readerBookQueryItem);

		systemQueryMenu.addSeparator();

		JMenuItem todayQueryItem = new JMenuItem("������Ϣ��ѯ");
		todayQueryItem.addActionListener(new TodayAction(this));
		systemQueryMenu.add(todayQueryItem);
	}

	private void createReaderManagementMenu() {
		// reader management menu
		JMenu readerManagementMenu = new JMenu("���߹���(R)");
		readerManagementMenu.setMnemonic('R');
		menuBar.add(readerManagementMenu);

		// reader information management Item.
		JMenuItem readerInfoItem = new JMenuItem("������Ϣ");
		readerInfoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				InputEvent.CTRL_MASK));
		readerInfoItem.addActionListener(new ReaderInfoAction(this));
		readerManagementMenu.add(readerInfoItem);

		readerManagementMenu.addSeparator();

		// new reader register management
		JMenuItem readerRegisterItem = new JMenuItem("���ߵǼ�");
		readerRegisterItem.addActionListener(new ReaderRegisterAction(this));
		readerManagementMenu.add(readerRegisterItem);

		// modify reader basic information.
		JMenuItem modifyReaderItem = new JMenuItem("�����޸�");
		modifyReaderItem.addActionListener(new ModifyReaderAction(this));
		readerManagementMenu.add(modifyReaderItem);

		readerManagementMenu.addSeparator();

		// delete reader accounts
		JMenuItem deleteReaderItem = new JMenuItem("����ɾ��");
		deleteReaderItem.addActionListener(new DeleteReaderAction(this));
		readerManagementMenu.add(deleteReaderItem);
	}

	private void createBookOperationMenu() {
		// book borrow and return back management menu.
		JMenu bookOperationMenu = new JMenu("ͼ��軹(B)");
		bookOperationMenu.setMnemonic('B');
		menuBar.add(bookOperationMenu);

		// borrow book management.
		JMenuItem borrowBookItem = new JMenuItem("ͼ�����");
		borrowBookItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
				InputEvent.CTRL_MASK));
		borrowBookItem.addActionListener(new BorrowBookAction(this));
		bookOperationMenu.add(borrowBookItem);

		// give back book management.
		JMenuItem giveBackBookItem = new JMenuItem("ͼ��黹");
		giveBackBookItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				InputEvent.CTRL_MASK));
		giveBackBookItem.addActionListener(new GiveBackBookAction(this));
		bookOperationMenu.add(giveBackBookItem);

		bookOperationMenu.addSeparator();

		// renew book management.
		JMenuItem renewBookItem = new JMenuItem("ͼ������");
		renewBookItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				InputEvent.CTRL_MASK));
		renewBookItem.addActionListener(new RenewBookAction(this));
		bookOperationMenu.add(renewBookItem);
	}

	private void createBookManagementMenu() {
		// book management menu.
		JMenu bookManagementMenu = new JMenu("ͼ�����(M)");
		bookManagementMenu.setMnemonic('M');
		menuBar.add(bookManagementMenu);

		// new book register.
		JMenuItem newBookItem = new JMenuItem("����Ǽ�");
		newBookItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		newBookItem.addActionListener(new NewBookAction(this));
		bookManagementMenu.add(newBookItem);

		// modify the basic information of book.
		JMenuItem modifyBookItem = new JMenuItem("ͼ���޸�");
		modifyBookItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				InputEvent.CTRL_MASK));
		modifyBookItem.addActionListener(new ModifyBookAction(this));
		bookManagementMenu.add(modifyBookItem);

		// loss book management.
		JMenuItem deleteBookItem = new JMenuItem("ͼ����ʧ");
		deleteBookItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				InputEvent.CTRL_MASK));
		deleteBookItem.addActionListener(new LossBookAction(this));
		bookManagementMenu.add(deleteBookItem);
	}

	private void createSystemOptionMenu() {
		// System option setting menu
		JMenu systemOptionMenu = new JMenu("ϵͳ����(O)");
		systemOptionMenu.setMnemonic('O');
		menuBar.add(systemOptionMenu);

		JMenuItem libraryInfoItem = new JMenuItem("ͼ�����Ϣ");
		libraryInfoItem.addActionListener(new LibraryInfoAction(this));
		systemOptionMenu.add(libraryInfoItem);

		JMenuItem statusBarInfoItem = new JMenuItem("״̬����Ϣ");
		statusBarInfoItem.addActionListener(new ChangeStatusBarAction(this,
				statusBar));
		systemOptionMenu.add(statusBarInfoItem);

		// ��������ͼƬ�˵���
		systemOptionMenu.add(new ChangeImageAction(this,
				ChangeImageAction.mainPanel));

		systemOptionMenu.addSeparator();

		JMenuItem adminSettingItem = new JMenuItem("����Ա����");
		adminSettingItem.addActionListener(new AdminSettingAction(this));
		systemOptionMenu.add(adminSettingItem);

		JMenuItem changePasswordItem = new JMenuItem("�޸�����");
		changePasswordItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				InputEvent.CTRL_MASK));
		changePasswordItem.addActionListener(new ChangePasswordAction(this));
		systemOptionMenu.add(changePasswordItem);

		systemOptionMenu.addSeparator();

		JMenuItem switchUserItem = new JMenuItem("�л��û�");
		switchUserItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				InputEvent.CTRL_MASK));
		switchUserItem.addActionListener(new SwitchUserAction(this));
		systemOptionMenu.add(switchUserItem);

		JMenuItem hangupItem = new JMenuItem("�뿪����");
		hangupItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				InputEvent.CTRL_MASK));
		hangupItem.addActionListener(new HangupAction(this));
		systemOptionMenu.add(hangupItem);

		JMenuItem exitItem = new JMenuItem("�˳�ϵͳ(X)", 'X');
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				InputEvent.ALT_MASK));
		exitItem.addActionListener(new ExitAction(this));
		systemOptionMenu.add(exitItem);
	}

	private void setFrameAttributes() {
		setTitle(HtLibraryAuthorInfo.getLibraryFullName()
				+ HtLibraryAuthorInfo.getVersion());
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		// set the icon of htLibrary application
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.getImage("images/library.png");
		setIconImage(img);
	}

	private void setButton(JButton b) {
		b.setBorderPainted(false);
		b.setVerticalTextPosition(JButton.BOTTOM);
		b.setHorizontalTextPosition(JButton.CENTER);
	}

	public static final int DEFAULT_WIDTH = 600;

	public static final int DEFAULT_HEIGHT = 500;

	private ExitDialog exitDialog = null;

	private JMenuBar menuBar;

	private MainPanel mainPanel;

	// it's special.
	private StatusBar statusBar = new StatusBar();
}