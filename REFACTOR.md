Ansj中文分词重构项目
==================
目标: 
* 在不改变原来代码逻辑的前提下进行重构
* 消灭 过多的 全局性的 static 变量
* 清理 精简 代码使之更易读切不易产生bug
* 更多地使用值对象等不可变对象
* 改善字典重加载
* 使用commons库减少自身代码量, 引入广泛使用的开源代码使代码易于更多人理解
* 升级到java8, 使用lombok使代码更短
* 升级到目前最新的maven-3.3.3

打包:
分词器
    
    mvn clean compile package install;

Lucene插件

    cd plug; mvn clean compile package install;
    
重构动机:
* 非常喜欢ansj分词器, 它基本能达到预期的效果
* 但是代码风格不够好, 包括nlp-lang包
* 在性能够用的情况下, 代码的可读性优先于性能
* 提高代码可读性有助于减少隐藏的bug 也有助于减少错误的使用
* 这么好的一个库, 代码不应该满足于实现功能, 应该便于更多人参考或学习

#####github as mvn-repo
see: http://www.cnblogs.com/lhfcws/p/3503707.html
see: https://github.com/github/maven-plugins
see: http://stackoverflow.com/questions/14013644/hosting-a-maven-repository-on-github

POM方言 https://github.com/takari/polyglot-maven
