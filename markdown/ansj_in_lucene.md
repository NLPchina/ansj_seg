#Ansj In Lucene

* Ansj目前有了lucene 什么版本的插件：
    * lucene3.x
    * lucene4.x
    > 对于3.x之前的版本，我表示不会写相关的jar了，lucene的分词插件不是很难写，如果非要说难写就是因为他没有一个很好的文档和说明，里面暗坑比较多。


* 如何获得lucene插件的jar包：
    > 你可以从我的maven仓库中下载[http://maven.ansj.org/org/ansj/](http://maven.ansj.org/org/ansj/) 打开这个

    > 找到   ansj_lucene3_plug/ , ansj_lucene4_plug/  从里面下载jar包把。maven的话同理。不多阐述

    > ````
        //实际使用中jar最好是最新版本的
        http://maven.ansj.org/org/ansj/ansj_lucene4_plug/1.3/ansj_lucene4_plug-1.3.jar

        http://maven.ansj.org/org/ansj/ansj_seg/1.3/ansj_seg-1.3-min.jar

        http://maven.ansj.org/org/ansj/tree_split/1.2/tree_split-1.2.jar

        需要这三个jar。。。

        使用例子
        https://github.com/ansjsun/ansj_seg/wiki/%E6%8A%8Aansj%E9%9B%86%E6%88%90%E5%9C%A8lucene%E4%B8%AD

    ````

* 如何用maven集成
   >````
   <dependency>
			<groupId>org.ansj</groupId>
			<artifactId>ansj_seg</artifactId>
			<version>1.3</version>
         <classifier>min</classifier>
		</dependency>
		<dependency>
			<groupId>org.ansj</groupId>
			<artifactId>ansj_lucene4_plug</artifactId>
			<version>1.3</version>
		</dependency>
````


* 如何获得Ansj lucene插件的源码：
    > 插件源码我已经集合到ansj_seg的项目中了。在plug目录下有这两个项目的源码。并且也是maven项目。你可以直接作为maven导入。
