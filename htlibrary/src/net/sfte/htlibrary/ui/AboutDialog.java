package net.sfte.htlibrary.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

public class AboutDialog extends JPanel {
	private static final long serialVersionUID = 1L;

	private Image img = null;

	private JDialog dialog = null;

	private int width = 400;

	private int height = 280;
	
	private static final int MAX_WIDTH = 400;

	public AboutDialog() {
		this.setToolTipText("双击鼠标左键退出");
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					dialog.setVisible(false);
			}
		});
		try {
			File f = new File(HtLibraryAuthorInfo.getAboutDialogImage());
			img = ImageIO.read(f);
		} catch (IOException e) {
			try {
				img = ImageIO.read(new File("images/htMM.png"));
			} catch (Exception ex) {
				// do nothing.
			}
		} finally {
			if (img != null) {
				int imgWidth = img.getWidth(null);
				int imgHeight = img.getHeight(null);
				if (imgWidth > MAX_WIDTH) {
					double scale = ((double) imgWidth) / MAX_WIDTH;
					width = MAX_WIDTH;
					height = (int)(imgHeight / scale);
				} else {
					width = imgWidth;
					height = imgHeight;
				}
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (img != null) {
			g2.drawImage(img, 0, 0, getWidth(), getHeight(), 0, 0, img
					.getWidth(null), img.getHeight(null), null);
		} else {
			Paint paint = new GradientPaint(0, 0, Color.MAGENTA, 20, 20,
					Color.BLUE, true);
			g2.setPaint(paint);
			Font f = new Font("华文行楷", Font.PLAIN, 30);
			g2.setFont(f);
			g2.drawString(HtLibraryAuthorInfo.getLibraryFullName(), 100, 200);
		}
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
			dialog.setUndecorated(true);
			dialog.add(this);
			dialog.pack();
		}
		dialog.setSize(width, height);
		// set the Location of this dialog to the center
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}
}