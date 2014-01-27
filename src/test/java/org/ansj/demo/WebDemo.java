package org.ansj.demo;

import org.ansj.app.web.AnsjServer;

/**
 * web服务测试 启动后访问：http://localhost:8888/ http://localhost:8888/page/index.html
 * 
 * @author ansj
 * 
 */
public class WebDemo {
	public static void main(String[] args) throws Exception {
		args = new String[] { "8888" };
		AnsjServer.main(args);
	}
}
