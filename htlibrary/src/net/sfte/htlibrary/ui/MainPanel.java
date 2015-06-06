package net.sfte.htlibrary.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import net.sfte.htlibrary.util.HtLibraryAuthorInfo;

/**
 * The main panel of htlibrary, It is used for displaying a background picture.
 * 
 * @author wenwen
 */
public class MainPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainPanel() {
		// read in the background picture.
		try {
			File f = new File(HtLibraryAuthorInfo.getMainPanelImage());
			img = ImageIO.read(f);
		} catch (IOException e) {
			try {
				img = ImageIO.read(new File("images/library.jpg"));
			} catch (Exception ex) {
				// do nothing.
			}
		}
	}

	private Image img;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// the background picture is found.
		if (img != null) {
			g2.drawImage(img, 5, 0, getWidth(), getHeight(), 0, 0, img
					.getWidth(null), img.getHeight(null), null);
		} else {
			// the picture is not found
			g2.drawString(HtLibraryAuthorInfo.getLibraryFullName() + "»¶Ó­Äú", getWidth() / 2,
					getHeight() / 2);
		}
	}
}