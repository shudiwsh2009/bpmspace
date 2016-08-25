package com.chinamobile.bpmspace.core.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.chinamobile.bpmspace.core.exception.BasicException;

public class FileRepository {
	public static void copyInputStreamToFile(InputStream in, String folder,
			String fileName) throws IOException, BasicException {

		File dir = new File(folder);
		File file = new File(folder + "\\" + fileName);

		// basic check for folder and file
		if (dir == null) {
			throw new BasicException("fail to open lib directory!");
		} else if (!dir.isDirectory()) {
			if (!dir.mkdir()) {
				throw new BasicException("fail to create lib!");
			}
		} else {
			if (file == null) {
				throw new BasicException("fail to create jar file!");
			} else if (file.exists()) {
				throw new BasicException("has jar file with same name!");
			}
		}

		// create
		if (!file.createNewFile()) {
			throw new BasicException("fail to create jar file!");
		}

		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((in.read(buffer)) != -1) {
				out.write(buffer);
			}
			out.flush();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
