#用maven导入

1. 第一步在你的pom.xml中加入.

````
<project...>
	....
	
	<distributionManagement>
		<repository>
			<id>mvn-repo</id>
			<url>scpexe://ansj/home/mvn-repo/ROOT</url>
		</repository>
	</distributionManagement>
	....
</project>
````


2. 在dependencies标签中粘贴如下:(其实version 以最新的为标准.)

````
	<dependencies>
		....
		<dependency>
			<groupId>org.ansj</groupId>
			<artifactId>ansj_seg</artifactId>
			<version>1.1</version>
		</dependency>
		....
	</dependencies>
````
