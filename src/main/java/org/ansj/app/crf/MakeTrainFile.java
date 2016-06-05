package org.ansj.app.crf;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.ansj.app.crf.pojo.Element;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

/**
 * 生成crf 或者是 wapiti的训练语聊工具.
 * 
 * 执行:java org.ansj.app.crf.MakeTrainFile [inputPath] [outputPath]
 * 
 * @author Ansj
 *
 */
public class MakeTrainFile {
	public static void main(String[] args) {

		String inputPath = "/Users/sunjian/Documents/workspace/ansj_crf/result_n.txt";

		String outputPath = "/Users/sunjian/Documents/src/Wapiti/test/train.txt";

		if (args != null && args.length == 2) {
			inputPath = args[0];
			inputPath = args[1];
		}

		if (StringUtil.isBlank(inputPath) || StringUtil.isBlank(outputPath)) {
			System.out.println("org.ansj.app.crf.MakeTrainFile [inputPath] [outputPath]");
			return;
		}

		BufferedReader reader = null;

		FileOutputStream fos = null;

		try {

			reader = IOUtil.getReader(inputPath, "utf-8");

			fos = new FileOutputStream(outputPath);

			String temp = null;

			Config config = new Config(null);

			int i = 0;

			while ((temp = reader.readLine()) != null) {

				StringBuilder sb = new StringBuilder("\n");

				if (StringUtil.isBlank(temp)) {
					continue;
				}

				if (i == 0) {
					temp = StringUtil.trim(temp);
				}

				List<Element> list = config.makeToElementList(temp, "\\s+");

				for (Element element : list) {
					sb.append(element.nameStr() + " " + Config.getTagName(element.getTag()));
					sb.append("\n");
				}

				fos.write(sb.toString().getBytes(IOUtil.UTF8));

				System.out.println(++i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
