package net.sfte.htlibrary.ui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

/**
 * This file filter matches all files with a given set of extensions.
 */
public class ExtensionFileFilter extends FileFilter {
	/**
	 * Adds an extension that this file filter recognizes.
	 * 
	 * @param extension
	 *            a file extension (such as ".txt" or "txt")
	 */
	public void addExtension(String extension) {
		if (!extension.startsWith("."))
			extension = "." + extension;
		extensions.add(extension.toLowerCase());
	}

	/**
	 * Sets a description for the file set that this file filter recognizes.
	 * 
	 * @param aDescription
	 *            a description for the file set
	 */
	public void setDescription(String aDescription) {
		description = aDescription;
	}

	/**
	 * Returns a description for the file set that this file filter recognizes.
	 * 
	 * @return a description for the file set
	 */
	public String getDescription() {
		return description;
	}

	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		String name = f.getName().toLowerCase();

		// check if the file name ends with any of the extensions
		for (String extension : extensions)
			if (name.endsWith(extension))
				return true;
		return false;
	}

	private String description = "";

	private ArrayList<String> extensions = new ArrayList<String>();
}
