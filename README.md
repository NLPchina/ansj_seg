Ansj中文分词
==================

在线测试地址<a href="http://demo.ansj.org">在线测试地址!</href>,

##maven
````
<repositories>
		<repository>
			<id>mvn-repo</id>
			<url>https://raw.github.com/ansjsun/mvn-repo/master</url>
		</repository>
	</repositories>


	<dependencies>
		<dependency>
			<groupId>org.ansj</groupId>
			<artifactId>tree_split</artifactId>
			<version>0.8</version>
		</dependency>
	</dependencies>

````
##Download jar

````
https://github.com/ansjsun/mvn-repo/tree/master/org/ansj/ansj_seg
````



增加了对lucene的支持.如果不想编译文件可以直接到 https://github.com/ansjsun/ansj_seg/tree/master/contrib 这里下载jar包!



这是一个ictclas的java实现.基本上重写了所有的数据结构和算法.词典是用的开源版的ictclas所提供的.并且进行了部分的人工优化

内存中中文分词每秒钟大约100万字(速度上已经超越ictclas)

文件读取分词每秒钟大约30万字

准确率能达到96%以上

目前实现了.中文分词. 中文姓名识别 . 用户自定义词典

可以应用到自然语言处理等方面,适用于对分词效果要求搞的各种项目.

如果你在eclipse中跑这个项目.需要导入tree-split 的jar包.当然也可以incloud 这个项目https://github.com/ansjsun/TreeSplitWord 

如果你第一次下载只想测试测试效果可以调用这个简易接口

<pre><code>
 String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!" ;
 System.out.println(ToAnalysis.paser(str));
 
 ﻿[欢迎/, 使用/, ansj/, _/, seg/, ,/, (/, ansj/, 中文/, 分词/, )/, 在/, 这里/, 如果/, 你/, 遇到/, 什么/, 问题/, 都/, 可以/, 联系/, 我/, 房/, 我/, 一定/, 尽/, 我/, 所/, 能/, ./, 帮助/, 大家/, ./, ansj/, _/, seg/, 更/, 快/, ,/, 更/, 准/, ,/, 更/, 自由/, !/]
</code></pre>


这是一个简单的分词效果,你可以在test目录中找到他.当然.个别歧异性的处理无法代表整体分词.仅做参考

<pre><code>
[脚下/f, 的/uj, 一大/j, 块/q, 方砖/n, 地面/n]
[长春/ns, 市长/n, 春节/t, 讲话/n]
[结婚/v, 的/uj, 和/c, 尚未/d, 结婚/v, 的/uj]
[结合/v, 成/v, 分子/n, 时/ng]
[旅游/vn, 和/c, 服务/vn, 是/v, 最/d, 好/a, 的/uj]
[邓颖/nr, 超生/v, 前/f, 最/d, 喜欢/v, 的/uj, 一个/m, 东西/n]
[中国/ns, 航天/n, 官员/n, 应邀/v, 到/v, 美国/ns, 与/p, 太空/s, 总署/n, 官员/n, 开会/v]
[上海/ns, 大学城/n, 书店/n]
[北京/ns, 大/a, 学生/n, 前来/v, 应聘/v]
[中外/j, 科学/n, 名著/n]
[为/p, 人民/n, 服务/vn]
[独立自主/i, 和/c, 平等互利/l, 的/uj, 原则/n]
[为/p, 人民/n, 办/v, 公益/n]
[这/r, 事/n, 的/uj, 确定/v, 不/d, 下来/v]
[费孝/nr, 通向/v, 人大常委会/nt, 提交/v, 书面/b, 报告/n]
[aaa/en, 分/q, 事实上/l, 发货/v, 丨/null, 和/c, 无/v, 哦/e, 喝/vg, 完/v, 酒/n]
[不好意思/a, 清清爽爽/z]
[长春市/ns, 春节/t, 讲话/n]
[中华人民共和国/ns, 万岁/n, 万岁/n, 万万岁/n]
[检察院/n, 鲍绍/nr, 检察长/n, 就是/d, 在/p, 世/ng, 诸葛/nr, ./m, 像/v, 诸葛亮/nr, 一样/u, 聪明/a]
[长春市/ns, 长春/ns, 药店/n]
[乒乓球拍/n, 卖/v, 完/v, 了/ul]
[计算机/n, 网络管理员/n, 用/p, 虚拟机/userDefine, 实现/v, 了/ul, 手机/n, 游戏/n, 下载/v, 和/c, 开源/v, 项目/n, 的/uj, 管理/vn, 金山/nz, 毒霸/nz]
[长春市/ns, 长春/ns, 药店/n]
[胡锦涛/nr, 与/p, 神/n, 九/m, 航天员/n, 首次/m, 实现/v, 天地/n, 双向/d, 视频/n, 通话/v]
[mysql/en, 不/d, 支持/v,  /null, 同台/v, 机器/n, 两个/m, mysql/en, 数据库/n, 之间/f, 做/v, 触发器/n]
[孙建/nr, 是/v, 一个/m, 好/a, 人/n, ./m, 他/r, 和/c, 蔡晴/nr, 是/v, 夫妻/n, 两/m,  /null, ,/null, 对于/p, 每/r, 一本/m, 好书/n, 他/r, 都/d, 原意/n, 一一/d, 读取/v, ../m, 他们/r, 都/d, 很/d, 喜欢/v, 元宵/n, ./m, 康燕/nr, 和/c, 他们/r, 住/v, 在/p, 一起/s, ./m, 我/r, 和/c, 马春亮/nr, ,/null, 韩鹏飞/nr, 都/d, 是/v, 好/a, 朋友/n, ,/null, 不/d, 知道/v, 什么/r, 原因/n]
[一年/m, 有/v, 三百六十五个/m, 日出/v,  /null, 我/r, 送/v, 你/r, 三百六十五个/m, 祝福/vn,  /null, 时钟/n, 每天/r, 转/v, 了/ul, 一千四百四十圈/m, 我/r, 的/uj, 心/n, 每天/r, 都/d, 藏/v, 着/uz,  /null, 一千四百四十多个/m, 思念/v,  /null, 每/r, 一天/m, 都/d, 要/v, 祝/v, 你/r, 快快乐乐/z,  /null,  /null, 每/r, 一分钟/m, 都/d, 盼望/v, 你/r, 平平安安/z,  /null, 吉祥/n, 的/uj, 光/n, 永远/d, 环绕/v, 着/uz, 你/r,  /null, 像/v, 那/r, 旭日东升/l, 灿烂/a, 无比/z,  /null]
[学校/n, 学费/n, 要/v, 一次性/d, 交/v, 一千元/m]
[发展/vn, 中国/ns, 家庭/n, 养猪/v, 事业/n]
[安徽省/ns, 是/v, 一个/m, 发展/vn, 中/f, 的/uj, 省/n]
[北京理工大学/nt, 办事处/n]
[审讯室/n, 里/f, 一直/d, 陪/v, 着/uz, 我们/r, 的/uj, 两个/m, 警察/n]
[一只/m, 胳膊/n, 两个/m, 警察/n]
[c/en, 语言/n, 怎么/r, 读写/v, ini/en, 文件/n]
[关卡/n, 编辑器/n]
[eclipse/en,  /null, 多/m, 项目/n, 依赖/v]
[苍/nr, 老师/n, 是/v, 一个/m, 好/a, 人/n]
<code></pre>
