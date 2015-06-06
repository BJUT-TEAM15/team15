package net.sfte.htlibrary.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.sfte.htlibrary.ui.ExtensionFileFilter;
import net.sfte.htlibrary.ui.ImagePreviewer;
import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

public class ChangeImageAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private Component parent = null;

	private JFileChooser fileChooser = null;
	
	public static final int aboutDialog = 1;
	public static final int mainPanel = 2;
	private int imageType;

	public ChangeImageAction(Component parent, int imageType) {
		this.parent = parent;
		this.imageType = imageType;
		putValue(Action.NAME, "更换背景图片");
		putValue(Action.SHORT_DESCRIPTION, "替换当前使用的背景图片");

		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("images"));

		ExtensionFileFilter filter = new ExtensionFileFilter();
		filter.addExtension("jpg");
		filter.addExtension("jpeg");
		filter.addExtension("gif");
		filter.addExtension("png");
		filter.setDescription("图像文件(*.jpg, *.jpeg, *.gif, *.png)");
		fileChooser.setFileFilter(filter);
		
		fileChooser.setAccessory(new ImagePreviewer(fileChooser));
	}

	public void actionPerformed(ActionEvent e) {
		int result = fileChooser.showOpenDialog(parent);
		if (result != JFileChooser.APPROVE_OPTION)
			return;
		File selectedFile = fileChooser.getSelectedFile();
		String copyToPath = getCopyToFilePath(selectedFile);
		File copyTo = new File(copyToPath);
		boolean success = true;
		try {
			copyFile(selectedFile, copyTo);
			if (imageType == ChangeImageAction.mainPanel)
				HtLibraryAuthorInfo.setMainPanelImage(copyToPath);
			else
				HtLibraryAuthorInfo.setAboutDialogImage(copyToPath);
			success = HtLibraryAuthorInfo.storeInfo();
		} catch (IOException ex) {
			success = false;
		}
		if (success) {
			JOptionPane.showMessageDialog(parent, "背景图片更换为 "
					+ copyToPath + ", 系统下次启动时生效!", "更换成功",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(parent, "更换背景图片失败, 请重试!", "更换失败",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private String getCopyToFilePath(File copyFrom) {
		String from = copyFrom.getName();
		String path = copyFrom.getAbsolutePath();
		if (path.endsWith("images" + File.separator + "library.jpg") ||
			path.endsWith("images" + File.separator + "htMM.png")) {
			return "images/" + from;
		}
		String to = "library" + from;
		if (imageType != mainPanel)
			to = "htMM" + from;
		return "images/" + to;
	}

	private void copyFile(File in, File out) throws IOException {
		String inPath = in.getAbsolutePath();
		String outPath = out.getAbsolutePath();
		if (inPath.equals(outPath))
			return ;
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		byte[] buf = new byte[1024];
		int i = 0;
		while ((i = fis.read(buf)) != -1) {
			fos.write(buf, 0, i);
		}
		fis.close();
		fos.close();
	}
}
