package org.ansj.app.crf.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.app.crf.Config;
import org.ansj.app.crf.Model;
import org.ansj.app.crf.SplitWord;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.ObjConver;
import org.nlpcn.commons.lang.util.StringUtil;

/**
 * 加载wapiti生成的crf模型,测试使用的wapiti版本为:Wapiti v1.5.0
 * 
 * wapiti 下载地址:https://wapiti.limsi.fr/#download 在这里感谢作者所做的工作.
 * 
 * @author Ansj
 *
 */
public class WapitiCRFModel extends Model {

	public WapitiCRFModel(String name) {
		super(name);
	}

	public void loadModel(String modelPath) throws Exception {

		BufferedReader br = IOUtil.getReader(modelPath, IOUtil.UTF8);

		long start = System.currentTimeMillis();

		LOG.info("load wapiti model begin!");

		String temp = br.readLine();

		LOG.info(temp); // #mdl#2#123

		loadConfig(br);

		StringBuilder sb = new StringBuilder();
		for (int[] t1 : config.getTemplate()) {
			sb.append(Arrays.toString(t1) + " ");
		}

		LOG.info("load template ok template : " + sb);

		int[] statusCoven = loadTagCoven(br);

		List<char[]> loadFeatureName = loadFeatureName(br);

		LOG.info("load feature ok feature size : " + loadFeatureName.size() + " it use memory about " + ((loadFeatureName.size() * Config.W_NUM * 4) / 1024 / 1024) + "m");

		featureTree = new SmartForest<float[]>();

		loadFeatureWeight(br, statusCoven, loadFeatureName);

		LOG.info("load wapiti model ok ! use time :" + (System.currentTimeMillis() - start));

	}

	/**
	 * 加载特征权重
	 * 
	 * @param br
	 * @param loadFeatureName
	 * @param statusCoven
	 * @throws IOException
	 */
	private void loadFeatureWeight(BufferedReader br, int[] statusCoven, List<char[]> featureNameList) throws IOException {
		String temp = null;
		int key = 0;
		float value = 0;

		int size = Config.W_NUM;

		int fIndex = -1;

		float[] tempW = null;

		int index = 0;

		int off = 0;

		int preTag = 0;

		int tag = 0;

		while ((temp = br.readLine()) != null) {
			String[] split = temp.split("=");

			key = ObjConver.getIntValue(split[0]);
			value = ObjConver.getFloatValue(split[1]);

			index = key / size;
			off = key % size;

			if (index != fIndex) {
				if (fIndex > 0) {
					this.featureTree.add(new String(featureNameList.get(fIndex)), tempW);
				}
				fIndex = index;
				tempW = new float[size];
			}

			if (off >= Config.TAG_NUM) {
				preTag = statusCoven[(off - Config.TAG_NUM) / Config.TAG_NUM];
				tag = statusCoven[off % Config.TAG_NUM];
				tempW[Config.TAG_NUM + preTag * Config.TAG_NUM + tag] = value;
			} else {
				preTag = 0;
				tag = statusCoven[off];
				tempW[tag] = value;
			}
		}
		this.featureTree.add(new String(featureNameList.get(fIndex)), tempW);
	}

	/**
	 * 增加特征到特征数中
	 * 
	 * @param cs
	 * @param tempW
	 */
	// private void printFeatureTree(char[] cs, double[] tempW) {
	// String name = "*" + ((int) cs[cs.length - 1] - Config.FEATURE_BEGIN + 1)
	// + ":" + new String(cs, 0, cs.length - 1);
	// for (int i = 0; i < tempW.length; i++) {
	// if (tempW[i] != 0) {
	// System.out.println(name + "\t" + (i / 4-1) + "\t" + (i % 4) + "\t" +
	// tempW[i]);
	// }
	// }
	//
	// }

	/**
	 * 加载特征值 //11:*6:_x-1/的,
	 * 
	 * @param br
	 * @return
	 * @throws Exception
	 */

	private List<char[]> loadFeatureName(BufferedReader br) throws Exception {
		String temp = br.readLine();// #qrk#num
		int featureNum = ObjConver.getIntValue(StringUtil.matcherFirst("\\d+", temp)); // 找到特征个数

		List<char[]> featureNames = new ArrayList<char[]>();

		for (int i = 0; i < featureNum; i++) {
			temp = br.readLine();
			Matcher matcher = Pattern.compile(":").matcher(temp);
			matcher.find();
			int start = matcher.end() + 1;
			matcher.find();
			int end = matcher.start();

			String str = temp.substring(matcher.end(), temp.length() - 1);

			// 特征id
			int lastFeatureId = (ObjConver.getIntValue(temp.substring(start, end)) - 1);

			if ("/".equals(str)) {
				str = "//";
			}

			if (str.contains("//")) {
				final String XIEGANG = "/XIEGANG/";
				str = str.replaceAll("//", XIEGANG);
			}
			char[] featureChars = toFeatureChars(str.trim().split("/"), lastFeatureId);

			featureNames.add(featureChars);
		}

		return featureNames;

	}

	private char[] toFeatureChars(String[] split, int lastFeatureId) throws Exception {
		List<Character> result = new ArrayList<Character>();

		for (String str : split) {
			if ("".equals(str)) {
				continue;
			} else if (str.length() == 1) {
				result.add(str.charAt(0));
			} else if (str.equals("XIEGANG")) {
				result.add('/');
			} else if (str.startsWith("num")) {
				result.add((char) (Config.NUM_BEGIN + ObjConver.getIntValue(str.replace("num", ""))));
			} else if (str.startsWith("en")) {
				result.add((char) (Config.EN_BEGIN + ObjConver.getIntValue(str.replace("en", ""))));
			} else if (str.startsWith("_x-")) {
				result.add(Config.BEGIN);
			} else if (str.startsWith("_x+")) {
				result.add(Config.END);
			} else {
				throw new Exception("can find feature named " + str + " in " + Arrays.toString(split));
			}
		}

		char[] chars = new char[result.size() + 1];

		for (int i = 0; i < result.size(); i++) {
			chars[i] = result.get(i);
		}
		chars[result.size()] = (char) (lastFeatureId + Config.FEATURE_BEGIN);
		return chars;
	}

	/**
	 * 加载特征标签转换
	 * 
	 * @param br
	 * @return
	 * @throws Exception
	 */
	private int[] loadTagCoven(BufferedReader br) throws Exception {

		int[] conver = new int[Config.TAG_NUM];

		String temp = br.readLine();// #qrk#4

		// TODO: 这个是个写死的过程,如果标签发生改变需要重新来写这里
		for (int i = 0; i < Config.TAG_NUM; i++) {
			char c = br.readLine().split(":")[1].charAt(0);
			switch (c) {
			case 'S':
				conver[i] = Config.S;
				break;
			case 'B':
				conver[i] = Config.B;
				break;
			case 'M':
				conver[i] = Config.M;
				break;
			case 'E':
				conver[i] = Config.E;
				break;
			default:
				throw new Exception("err tag named " + c + " in model " + temp);
			}
		}
		return conver;
	}

	/**
	 * 加载特征模板
	 * 
	 * @param br
	 * @throws IOException
	 */
	private void loadConfig(BufferedReader br) throws IOException {

		String temp = br.readLine();// #rdr#8/0/0

		int featureNum = ObjConver.getIntValue(StringUtil.matcherFirst("\\d+", temp)); // 找到特征个数

		int[][] template = new int[featureNum][0]; // 构建特征模板

		for (int i = 0; i < template.length; i++) {
			temp = br.readLine();
			List<String> matcherAll = StringUtil.matcherAll("\\[.*?\\]", temp);
			template[i] = new int[matcherAll.size()];
			for (int j = 0; j < template[i].length; j++) {
				template[i][j] = ObjConver.getIntValue(StringUtil.matcherFirst("[-\\d]+", matcherAll.get(j)));
			}
		}

		config = new Config(template);
	}

	public static void main(String[] args) throws Exception {
		WapitiCRFModel model = new WapitiCRFModel("wapiti");
		model.loadModel("/Users/sunjian/Documents/src/Wapiti/test/model.dat");

		List<String> list = new ArrayList<String>();
		list.add("李宇春《再不疯狂我们就老了》MV首播】李宇春新专辑同名第二主打《再不疯狂我们就老了》MV今日正式发布。这首歌与《似火年华》，以“疯狂”为概念的对话曲目，采用一曲双词的方式。李宇春与韩寒，同时在一首歌里，讲述了两种截然相反，却本质同归的态度");
		list.add("上个月在天津术语学会上见到冯老，言谈中感觉到冯老对机器翻译的深厚感情和殷切希望。是啊，机器翻译事业还年轻，我辈细流，心驰沧海，愿倾尽绵薄之力，浇灌此常青之树。");
		list.add("发表了博文 《多语言信息网络时代的语言学家：冯志伟》 - 冯志伟与老伴郑初阳 多语言信息网络时代的语言学家：冯志伟 桂清扬 冯志伟，教育部语言文字应用研究所研究员，博士生导师，所学术委员会");
		list.add("Facebook CEO 马克·扎克伯格亮相了周二 TechCrunch Disrupt 大会，并针对公司不断下挫的股价、移动战略、广告业务等方面发表了讲话。自 5 月公司 IPO 后，扎克伯格极少公开露面，这也是他首次在重要场合公开接受采访");
		list.add("@新华社中国网事：#聚焦钓鱼岛#外交部长杨洁篪10日在外交部紧急召见日本驻华大使丹羽宇一郎，就日本政府非法“购买”钓鱼岛提出严正交涉和强烈抗议。当日，中国驻日本大使程永华也向日本外务省负责人提出严正交涉并递交了抗议照会。");
		list.add("阿米尔汗，8岁时出演一部轰动印度的电影，是公认的童星，长大后却一心打网球并获得过网球冠军。21岁爱上邻居家女孩，由于宗教原因两人决定私奔，现在过着幸福美满的生活。81届奥斯卡最佳影片《贫民窟的百万富翁》，他担任制片。2009年一部《三个白痴》震惊全球，他47岁");
		list.add("老郭动粗 师徒揭相声虚假繁荣");
		list.add("Facebook CEO 扎克伯格极少公开露面");
		list.add("徐德有说这是个错误!");
		list.add("而如今Facebook的CEO马克·扎克伯格表示，押在HTML5上是Facebook最大的错误。由于HTML5应用性能差到不能忍受");
		list.add("本报讯（记者胡笑红）已经过期的牛奶被销售经理修改日期,照样投放市场销售，记者昨天从蒙牛公司得到证实，蒙牛驻义乌经理王孙富和同伙赵宝峰因涉嫌生产销售伪劣产品罪已被当地批捕。");
		list.add("白玉萍是一个好人");
		list.add("张三同李四是好朋友");
		list.add("钟子期的名字能够被认出来么");
		list.add("綦玉冰");
		list.add("汤姆克鲁斯的英文名字很苦");
		list.add("曼城第23分钟遭遇打击，孔帕尼中线丢球，莫里森中路直塞，沙恩-朗拿球成单刀之势，米尔纳背后将其铲倒，主裁判克拉滕伯格认为米尔纳是最后一名防守球员，直接掏出红牌！曼奇尼在场边向第四官员抗议，认为莱斯科特已经补防到位。多兰斯主罚任意球打在人墙上高出。");
		list.add("中新网10月20日电 据日本共同社报道，日本民主党代理干事长安住淳20日表示，首相野田佳彦将履行“近期”解散众院举行大选的承诺，预计在“公债发行特例法案”获得通过等条件具备时解散众院。");
		list.add("邓颖超生前最喜欢的画像");
		list.add("习近平和朱镕基情切照相");
		list.add("能不能试试这个 西伯利亚雅特大教堂位于俄罗斯东西伯利亚地区") ;
		list.add("李克强");
		for (String string : list) {
			System.out.println(new SplitWord(model).cut(string));
		};
	}
}
