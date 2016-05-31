package org.ansj.demo;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.IOException;

public class myDefineDemo {
	public static void main(String[] args) throws IOException {
		Result terms = ToAnalysis.parse("hualai s了，华盛顿总理到访联合国，你知道共产党党性党章吗？你到底是何居心？真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合，坏蛤蟆，他说是这在的你蛤蟆，他们是大众经济没人是癞蛤蟆还.汕头市和亨玩具有限公司\n" +
				"庆云县宏远运输有限公司\n" +
				"义乌市高迪酒业有限公司\n" +
				"江阴双夏进出口有限公司\n" +
				"太原桓通贸易有限公司\n" +
				"杭州创力电器有限公司\n" +
				"广州市晟法贸易有限公司\n" +
				"武汉依之莎卫浴有限公司\n" +
				"常州市润华调节器材有限公司\n" +
				"江西婺华园蜂业有限责任公司\n" +
				"北京新世纪认证有限公司\n" +
				"南通华钰电力配套机械制造有限公司\n" +
				"宜昌市奔朗贸易有限责任公司\n" +
				"寿光市临峰能源有限责任公司\n" +
				"新疆天拓工贸有限公司\n" +
				"成都星瑞农业有限公司\n" +
				"嵩县华伊印刷有限公司\n" +
				"清河县奥尼特羊绒纺织有限公司\n" +
				"常州市创达热固塑料有限公司\n" +
				"上海瑞禾房地产发展有限公司\n" +
				"深圳市中建物资有限公司" +
				"热固塑料品中国制造热固塑料垃圾袋品尝起来口味好塑料袋子说不可以不是塑料袋塑料薄膜");
		System.out.println("result:" + terms);


	}



}
