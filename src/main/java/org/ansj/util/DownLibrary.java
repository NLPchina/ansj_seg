package org.ansj.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.nlpcn.commons.lang.util.StringUtil;

public class DownLibrary {
	public static void main(String[] args) throws Exception {
		down();
		unzip("library.zip", ".", false);
	}

	private static void down() throws MalformedURLException, IOException, FileNotFoundException {
		MyStaticValue.LIBRARYLOG.info("to down library ! please wait a moment");

		URL url = new URL("http://maven.nlpcn.org/down/library.zip");

		URLConnection conn = url.openConnection();

		long length = conn.getContentLength();

		InputStream openStream = conn.getInputStream();

		byte[] bytes = new byte[10240];

		int temp = 0;

		FileOutputStream fos = new FileOutputStream("library.zip");

		int len = 0;
		while ((temp = openStream.read(bytes)) != -1) {
			fos.write(bytes, 0, temp);
			len += temp;
			MyStaticValue.LIBRARYLOG.info("down " + len + "/" + length);
		}

		fos.flush();
		fos.close();
	}

	@SuppressWarnings("unchecked")
	public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception {

		File zipFile = new File(zipFilePath);
		// 如果解压后的文件保存路径包含压缩文件的文件名，则追加该文件名到解压路径
		if (includeZipFileName) {
			String fileName = zipFile.getName();
			if (StringUtil.isBlank(fileName)) {
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
			unzipFilePath = unzipFilePath + File.separator + fileName;
		}
		// 创建解压缩文件保存的路径
		File unzipFileDir = new File(unzipFilePath);
		if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
			unzipFileDir.mkdirs();
		}

		// 开始解压
		ZipEntry entry = null;
		String entryFilePath = null, entryDirPath = null;
		File entryFile = null, entryDir = null;
		int index = 0, count = 0, bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		ZipFile zip = new ZipFile(zipFile);
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
		// 循环对压缩包里的每一个文件进行解压
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			// 构建压缩包中一个文件解压后保存的文件全路径
			entryFilePath = unzipFilePath + File.separator + entry.getName();
			// 构建解压后保存的文件夹路径
			index = entryFilePath.lastIndexOf(File.separator);
			if (index != -1) {
				entryDirPath = entryFilePath.substring(0, index);
			} else {
				entryDirPath = "";
			}
			entryDir = new File(entryDirPath);
			// 如果文件夹路径不存在，则创建文件夹
			if (!entryDir.exists() || !entryDir.isDirectory()) {
				entryDir.mkdirs();
			}

			// 创建解压文件
			entryFile = new File(entryFilePath);

			if (entryFile.isDirectory()) {
				continue;
			}

			if (entryFile.exists()) {
				System.err.println(entryFile.getAbsolutePath() + " has in ! so skip it!");
				continue;
			}

			System.out.println("to wirte file " + entryFile.getAbsolutePath());

			// 写入文件
			bos = new BufferedOutputStream(new FileOutputStream(entryFile));
			bis = new BufferedInputStream(zip.getInputStream(entry));
			while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
				bos.write(buffer, 0, count);
			}
			bos.flush();
			bos.close();
		}
	}
}
