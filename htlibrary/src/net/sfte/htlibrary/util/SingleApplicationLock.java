/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */

package net.sfte.htlibrary.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin Date: 28.02.2006 Time: 08:29:56
 */
public class SingleApplicationLock {
	private static final Logger LOG = Logger
			.getLogger(SingleApplicationLock.class.getName());

	private FileChannel channel;

	private FileLock lock;

	private File file;

	public SingleApplicationLock(String lockFileName) {
		file = new File(lockFileName);
	}

	public boolean isAppActive() {
		try {
			// noinspection
			// ChannelOpenedButNotSafelyClosed,HardCodedStringLiteral,IOResourceOpenedButNotSafelyClosed
			channel = new RandomAccessFile(file, "rw").getChannel();

			try {
				lock = channel.tryLock();
			} catch (OverlappingFileLockException e) {
				// already locked
				closeLock();
				return true;
			}

			if (lock == null) {
				closeLock();
				return true;
			}

			Runtime.getRuntime().addShutdownHook(new Thread() {
				// destroy the lock when the JVM is closing
				public void run() {
					closeLock();
					deleteFile();
				}
			});
			return false;
		} catch (Exception e) {
			closeLock();
			return true;
		}
	}

	private void closeLock() {
		try {
			lock.release();
		} catch (Exception e) {
			if (LOG.isLoggable(Level.FINE))
				LOG.log(Level.FINE, "SingleApplicationLock.closeLock ", e);
		}
		try {
			channel.close();
		} catch (Exception e) {
			if (LOG.isLoggable(Level.FINE))
				LOG.log(Level.FINE, "SingleApplicationLock.closeLock ", e);
		}
	}

	private void deleteFile() {
		try {
			file.delete();
		} catch (Exception e) {
			if (LOG.isLoggable(Level.FINE))
				LOG.log(Level.FINE, "SingleApplicationLock.deleteFile ", e);
		}
	}
}
