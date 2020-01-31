package org.ansj.app.extracting;

import org.ansj.app.extracting.domain.ExtractingResult;
import org.ansj.app.extracting.exception.RuleFormatException;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ansj on 21/09/2017.
 */
public class ExtractingTest {

	@Test
	public void test() throws IOException, RuleFormatException {

		Extracting extracting = new Extracting(LexicalTest.class.getResourceAsStream("/rule.txt"), "utf-8") ;

		ExtractingResult result = null ;


         result = extracting.parse("2017年2月12日是一个特殊的日子");

		System.out.println(result.getAllResult()); ;

		result = extracting.parse("2017年2月12日是一个特殊的日子，我出生于1985年8月28日");

		System.out.println(result.getAllResult());

		result = extracting.parse("清华大学负责人孙健先生");

		System.out.println(result.getAllResult());


	}

	@Test
	public void test1() throws RuleFormatException {
		List<String> lines = new ArrayList<>() ;

		//填写规则 可以写多条
		lines.add("(:bName)(:*){0,3}(并发症)(有|都有)(哪些|什么)	名称:0;限定:2;目的:(个数)") ;
		lines.add("(:bName)(如何|怎么){0,1}(防治|避免)\t名称:0;限定:2") ;
		lines.add("(:nt|清华大学啊啊啊)(负责人)(:nr)\t公司名:0;人物:2");
		lines.add("(本期计提坏账准备金额)(:m)\tname:(计提);num:1") ;
		lines.add("(本期收回或转回坏账准备金额)(:m)\tname:(收回);num:1") ;

		lines.add("(:*)(测定)\t名称:0;aaa:1") ;




		Extracting extracting = new Extracting(lines) ;

		//插入自定义词典
		DicLibrary.insertOrCreate(DicLibrary.DEFAULT,"糖尿病","bName",1000);
		DicLibrary.insertOrCreate(DicLibrary.DEFAULT,"心脏病","bName",1000);
		DicLibrary.insertOrCreate(DicLibrary.DEFAULT,"感冒","bName",1000);


		System.out.println(extracting.parse("这个限定200").getAllResult());
		System.out.println(extracting.parse("这个限定200限定").getAllResult());

		//进行分析
		System.out.println(extracting.parse("糖尿病具有的并发症都有什么").getAllResult());
		System.out.println(extracting.parse("感冒的并发症有什么").getAllResult());
		System.out.println(extracting.parse("心脏病并发症都有哪些").getAllResult());
		System.out.println(extracting.parse("心脏病防治").getAllResult());
		System.out.println(extracting.parse("心脏病如何防治").getAllResult());
		System.out.println(ToAnalysis.parse("本期计提坏账准备金额2138030.52元；本期收回或转回坏账准备金额0.00元"));
		System.out.println(extracting.parse("清华大学负责人孙健先生").getAllResult());
		System.out.println(extracting.parse("清华大学啊啊啊负责人孙健先生").getAllResult());
		System.out.println(extracting.parse("本期计提坏账准备金额2138030.52元；本期收回或转回坏账准备金额0.00元").getAllResult());
	}

	@Test
	public void test2() throws RuleFormatException {
		List<String> lines = new ArrayList<>() ;

		//填写规则 可以写多条

		lines.add("(是很)(:a)(:u)\t特定:0;副词:1;形容词:2");
		lines.add("(:n)(:d)(:a)\t副词:0;形容词:1;名词:2");
		lines.add("(:d)(:a)(:u)(:n)\td:0;副词:1;形容词:2;名词:3");


		Extracting extracting = new Extracting(lines) ;


		System.out.println(ToAnalysis.parse("我这里有很优美的环境"));
		System.out.println(ToAnalysis.parse("我这里环境很优美"));
		System.out.println(ToAnalysis.parse("我这里的环境是很优美的"));

		System.out.println("------------------------------------------------------------");

		printRule(extracting.parseWithToAnalysis("我这里有很优美的环境").findAll());
		printRule(extracting.parseWithToAnalysis("我这里环境很优美").findAll());
		printRule(extracting.parseWithToAnalysis("我这里的环境是很优美的").findAll());


	}

	private void printRule(List<List<Term>> list) {
		for (List<Term> terms : list) {
			System.out.println(terms);
		}
	}
}
