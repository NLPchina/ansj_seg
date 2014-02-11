#tree-split

>tree-split是一个小巧好用的。tire树数据库。
>
>里面封装了我一些常用的工具类。比如IOUtil 。 StringUtil。
>
>tire树就不解释了。也就是词典树，个人认为是最好的分词数据结构。没有之一。
>
>tree-split 中的tire采用首字hash 。次字二分查找的方式。同时也支持参数可配置的形式。。
>


#下面是tree-split的一个使用说明


<pre><code>
		/**
		 * 词典的构造.一行一个词后面是参数.可以从文件读取.可以是read流.
		 */
		String dic = "中国\t1\tzg\n人名\t2\n中国人民\t4\n人民\t3\n孙健\t5\nCSDN\t6\njava\t7\njava学习\t10\n";
		Forest forest = Library.makeForest(new BufferedReader(new StringReader(dic)));

		/**
		 * 删除一个单词
		 */
		Library.removeWord(forest, "中国");
		/**
		 * 增加一个新词
		 */
		Library.insertWord(forest, "中国人");
		String content = "中国人名识别是中国人民的一个骄傲.孙健人民在CSDN中学到了很多最早iteye是java学习笔记叫javaeye但是java123只是一部分";
		GetWord udg = forest.getWord(content);

		String temp = null;
		while ((temp = udg.getFrontWords()) != null)
			System.out.println(temp + "\t\t" + udg.getParam(1) + "\t\t" + udg.getParam(2));
<code></pre>



###jar包下载地址:
[http://maven.ansj.org/org/ansj/tree_split/](http://maven.ansj.org/org/ansj/tree_split/)

### maven

==================
>1. 第一步在你的pom.xml中加入.

````
<project...>
	....
	
	<repositories>
		<repository>
			<id>mvn-repo</id>
			<url>http://maven.ansj.org/</url>
		</repository>
	</repositories>
	....
</project>
````


2. 在dependencies标签中粘贴如下:(其实version 以最新的为标准.)

````
	<dependencies>
		....
		<dependency>
			<groupId>org.ansj</groupId>
			<artifactId>tree_split</artifactId>
			<version>1.2</version>
		</dependency>
		....
	</dependencies>
````


#获得源码

https://github.com/ansjsun/tree_split
