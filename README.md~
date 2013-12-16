Ansj中文分词
==================

在线测试地址<a href="http://demo.ansj.org">在线测试地址!</href>,

##maven
````
	<repositories>
		<repository>
			<id>mvn-repo</id>
			<url>http://ansjsun.github.io/mvn-repo/</url>
		</repository>
	</repositories>

	<dependencies>
        <dependency>
            <groupId>org.ansj</groupId>
            <artifactId>ansj_seg</artifactId>
            <version>0.9</version>
        </dependency>
    </dependencies>

````
##Download jar

````
首先需要分词程序的jar
https://github.com/ansjsun/mvn-repo/tree/gh-pages/org/ansj/ansj_seg
然后还需要导入tree的数据结构jar
https://github.com/ansjsun/mvn-repo/tree/gh-pages/org/ansj/tree_split
````

增加了对lucene的支持.如果不想编译文件可以直接到 https://github.com/ansjsun/mvn-repo/tree/gh-pages/org/ansj 这里下载jar包!




这是一个ictclas的java实现.基本上重写了所有的数据结构和算法.词典是用的开源版的ictclas所提供的.并且进行了部分的人工优化

内存中中文分词每秒钟大约100万字(速度上已经超越ictclas)

文件读取分词每秒钟大约30万字

准确率能达到96%以上

目前实现了.中文分词. 中文姓名识别 . 用户自定义词典

可以应用到自然语言处理等方面,适用于对分词效果要求搞的各种项目.

如果你在eclipse中跑这个项目.需要导入tree-split 的jar包.当然也可以incloud 这个项目https://github.com/ansjsun/tree_split 

如果你第一次下载只想测试测试效果可以调用这个简易接口

<pre><code>
 String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!" ;
 System.out.println(ToAnalysis.paser(str));
 
 ﻿[欢迎/, 使用/, ansj/, _/, seg/, ,/, (/, ansj/, 中文/, 分词/, )/, 在/, 这里/, 如果/, 你/, 遇到/, 什么/, 问题/, 都/, 可以/, 联系/, 我/, 房/, 我/, 一定/, 尽/, 我/, 所/, 能/, ./, 帮助/, 大家/, ./, ansj/, _/, seg/, 更/, 快/, ,/, 更/, 准/, ,/, 更/, 自由/, !/]
</code></pre>




----
##大事记要

#2013年12月12日
* 把由字构词的方式加到了分词中，对未登录词有了很大的提高。目前alaph一版。希望能尽快更新。加油 

#2013年9月26日
* 我更新完了发表此帖为止的一次更新。在核心辞典上作了一些手脚。这个版本更像以前的版本。在分词的颗粒度上保持了优良的传统。尤其是面向搜索的用户。一定要更新

#2013-08-28
* 经过无数网友的抗议。ansj终于支持了maven。在这里感谢帮我把项目转换到maven的那个兄弟。你qq我找不到了。名字我也忘记了。

#改进
* 断断续续修改了无数个版本。在csdn的搜索系统上。用12年的历史数据.检索分析等.ansj经受住了考验。但是根据网友和自己的发现。找到了项目中的很多不足于是。开工。。。。。
* 同时在改进的过程中。我认识了更多的朋友。太多了。恩还有在读这篇文章的你。感谢你们对这个小工具的支持。在这里不一一例举了。主要找你们的名字比较麻烦。而我有是个很懒惰的人

#崩溃
* 如大多数的开源者一样，项目带来了很多负担

比如。在你工作或者思考的时候。别人就会打断你的思路。qq or email 提出了数个问题。或者bug。当然这些中大多都是友善的很有意义的建议。一方面让我更加坚定做好这个开源分词的决心。另一方面也给我的工作生活带来了一些效率上的影响。大多数提问我都是会回答。而且尽可能的保持耐心。但是如果有怠慢的地方。我在这里对大家表示歉意。

#诞生
* 2012-9-7 日Ansj中文分词。在我整整一夜的奋斗中终于完成了，真的是一夜的奋斗。写着写着一抬头天亮了。当然中间的快乐与心酸这里就不牢骚了。

* 通过微薄@了52nlp希望他能帮我推广下。在他的帮助下。ansj结识了很多朋友。@完后我就去睡觉了。辗转的一个夜晚。当下午醒来的时候。很多人微薄@我。我开玩笑的和cq说。我火了。

* 同时也@了我的启蒙导师张华平老师。他对我表示了支持。在这里感谢他
