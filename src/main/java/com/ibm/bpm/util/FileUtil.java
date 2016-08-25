package com.ibm.bpm.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {

	public static void replaceFileContent(String strFilePath, String replaced,
			String replaceBy) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(strFilePath)));
		String str, strDef = "";
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while ((str = br.readLine()) != null) {
			if (i == 0) {
				strDef = str;
			} else {
				sb.append("\r\n" + str.replace(replaced, replaceBy));
			}
			i++;
		}
		br.close();

		BufferedWriter bw = new BufferedWriter(new FileWriter(strFilePath));
		bw.write(strDef);
		bw.write(sb.toString());
		bw.close();
	}

	public static void copyFile(String sourceFile, String targetFile)
			throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// �½��ļ����������������л���
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// �½��ļ���������������л���
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// ��������
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// ˢ�´˻���������
			outBuff.flush();
		} finally {
			// �ر���
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}
}
