package org.ansj.splitWord.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.ansj.dic.DicReader;
import org.ansj.domain.Term;
import org.ansj.library.InitDictionary;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.MyStaticValue;

import love.cq.util.AnsjArrays;
import love.cq.util.IOUtil;

/**
 * 机构名识别测试
 * 
 * @author ansj
 * 
 */
public class CompanyTest {
	public static void main(String[] args) throws IOException {
		// 江苏宏宝集团有限公司,宏宝集团,华夏银行股份有限公司苏州分行,张家港市宏大钢管有限公司
		String example = "江苏宏宝五金股份有限公司（以下简称“本公司”）于2012年11月9日接到实际控制人" + "江苏宏宝集团有限公司（以下简称“宏宝集团”）通知，" + "宏宝集团将其所持本公司无限售条件流通股份500万股（占公司总股本的2．72％）质押给"
				+ "华夏银行股份有限公司苏州分行，为" + "张家港市宏大钢管有限公司向华夏银行股份有限公司苏州分行" + "申请最高融资额提供担保，股权质押登记日为2012年11月8日，质押期限至2013年11月5日止；同日，" + "宏宝集团"
				+ "将其所持本公司无限售条件流通股份1000万股（占公司总股本的5．43％）质押给" + "江苏张家港农村商业银行股份有限公司，为张家港保税区" + "康龙国际贸易有限公司向"
				+ "江苏张家港农村商业银行股份有限公司申请的流动资金贷款提供担保，股权质押登记日为2012年11月8日，质押期限至2014年11月5日止。上述质押登记手续已在中国证券登记结算有限责任公司深圳分公司办理完毕。";
//		 String example =
//		 " 新浪体育讯　北京时间4月15日03:00(英国当地时间14日20:00)，2009/10赛季英格兰足球超级联赛第34轮一场焦点战在白鹿巷球场展开角逐，阿森纳客场1比2不敌托特纳姆热刺，丹尼-罗斯和拜尔先入两球，本特纳扳回一城。阿森纳仍落后切尔西6分(净胜球少15个)，夺冠几成泡影。热刺近 7轮联赛取得6胜，继续以1分之差紧逼曼城。";
//		 String example = "东华能源2012年第四次临时股东大会于2012年11月9日召开，审议通过了《关于同意投资设立“宁波福基石化有限公司”的议案》、《关于“张家港扬子江石化有限公司”新增40万吨/年聚丙烯项目的议案》、《关于对“宁波福基石化有限公司”授权的议案》、《关于对“张家港扬子江石化有限公司”授权的议案》、《关于提请股东大会延长董事会全权办理非公开发行股票事项授权有效期的议案》。" ;
		// List<Term> paser = NlpAnalysis
		// .paser("东华能源2012年第四次临时股东大会于2012年11月9日召开，审议通过了《关于同意投资设立“宁波福基石化有限公司”的议案》、《关于“张家港扬子江石化有限公司”新增40万吨/年聚丙烯项目的议案》、《关于对“宁波福基石化有限公司”授权的议案》、《关于对“张家港扬子江石化有限公司”授权的议案》、《关于提请股东大会延长董事会全权办理非公开发行股票事项授权有效期的议案》。");
		// ;
		// List<Term> paser =
		// NlpAnalysis.paser("事实上，HTC自诞生以来，多数时候都只是在为谷歌等公司代工生产移动终端。但它从2006年开始培育自己的HTC品牌，并在此后的五年时间里迅速成为仅次于诺基亚的全球第二大手机厂商，占有全球18.22%的智能手机份额，在北美智能手机市场的份额也曾一度达到23%，是全美最大的智能手机供应商。")
		// ;
		// List<Term> paser = NlpAnalysis
		// .paser("定增预案显示，截至2011年12月31日，蓝鼎集团资产总额为79.49亿元，净资产9.00亿元。2010年蓝鼎集团总资产64.21亿元，其中所有者权益2.19亿元。这意味着，2010年和2011年蓝鼎集团的资产负债率分别高达96.6%和88.34%。如此高的资产负债率在A股房地产类上市公司中较为少见。有关数据显示，在135家房地产上市公司中，2011年资产负债率高于88%的仅有3家公司，分别是*ST园城[10.61 -0.09% 股吧 研报](107.7%)、高新发展[6.72 -0.59% 股吧 研报](95.5%)以及鲁商置业[4.18 0.48% 股吧 研报](92%)。");
		List<Term> paser = NlpAnalysis.paser(example);
		System.out.println(paser);

		// BufferedReader companReader = MyStaticValue.getCompanReader();
		// String temp = null;
		// int[] ints = null;
		// int max = 0 ;
		// HashMap<String, Object> hm = new HashMap<String, Object>();
		// while ((temp = companReader.readLine()) != null) {
		// String[] split = temp.split("\t");
		// ints = new int[6];
		//
		// ints[0] = Integer.parseInt(split[1]);
		// ints[1] = Integer.parseInt(split[2]);
		// ints[2] = Integer.parseInt(split[3]);
		// ints[3] = Integer.parseInt(split[4]);
		// ints[4] = Integer.parseInt(split[5]);
		// int wordId = InitDictionary.getWordId(split[0]) ;
		// if(wordId>1&&InitDictionary.termNatures[wordId]!=null){
		// hm.put(split[0], ints);
		// ints[5] = InitDictionary.termNatures[wordId].allFreq;
		// }else{
		// System.out.println(split[0]);
		// }
		// max += ints[0] ;
		// max += ints[1] ;
		// max += ints[2] ;
		// max += ints[3] ;
		// max += ints[4] ;
		// max += ints[5] ;
		// }
		// companReader.close() ;
		// System.out.println(max);

		// Set<String> keySet = hm.keySet() ;
		//
		// StringBuilder sb = new StringBuilder() ;
		// for (String string : keySet) {
		// ints = (int[]) hm.get(string) ;
		// sb.append(string+"\t"+ints[0]+"\t"+ints[1]+"\t"+ints[2]+"\t"+ints[3]+"\t"+ints[4]+"\t"+ints[5]);
		// sb.append("\n");
		// }
		//
		// IOUtil.Writer("/Users/ansj/Desktop/result.txt", "UTF-8",
		// sb.toString()) ;

		// for (Term term : paser) {
		// System.out.println(term +"\t" + term.getTermNatures().companyAttr);
		// }
	}
}
