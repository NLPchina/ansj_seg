package org.ansj.app.crf.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.app.crf.Config;
import org.ansj.app.crf.Model;
import org.ansj.app.crf.SplitWord;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.ObjConver;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.tuples.Pair;

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

		Map<String, Integer> featureIndex = loadConfig(br);

		StringBuilder sb = new StringBuilder();
		for (int[] t1 : config.getTemplate()) {
			sb.append(Arrays.toString(t1) + " ");
		}

		LOG.info("featureIndex is " + featureIndex);
		LOG.info("load template ok template : " + sb);

		int[] statusCoven = loadTagCoven(br);

		List<Pair<String, String>> loadFeatureName = loadFeatureName(featureIndex, br);

		LOG.info("load feature ok feature size : " + loadFeatureName.size());

		featureTree = new SmartForest<float[]>();

		loadFeatureWeight(br, statusCoven, loadFeatureName);

		LOG.info("load wapiti model ok ! use time :" + (System.currentTimeMillis() - start));

	}

	/**
	 * 加载特征权重
	 * 
	 * @param br
	 * @param featureNames
	 * @param statusCoven
	 * @throws Exception
	 */
	private void loadFeatureWeight(BufferedReader br, int[] statusCoven, List<Pair<String, String>> featureNames) throws Exception {

		int key = 0;

		int offe = 0;

		int tag = 0; // 赏析按标签为用来转换

		int len = 0; // 权重数组的大小

		int min, max = 0; // 设置边界

		String name = null; // 特征名称

		float[] tempW = null; // 每一个特征的权重

		String temp = br.readLine();

		for (Pair<String, String> pair : featureNames) {

			if (temp == null) {
				LOG.warning(pair.getValue0() + "\t" + pair.getValue1() + " not have any weight ,so skip it !");
				continue;
			}

			char fc = Character.toUpperCase(pair.getValue0().charAt(0));

			len = fc == 'B' ? Config.TAG_NUM * Config.TAG_NUM : fc == 'U' ? Config.TAG_NUM : fc == '*' ? (Config.TAG_NUM + Config.TAG_NUM * Config.TAG_NUM) : 0;

			if (len == 0) {
				throw new Exception("unknow feature type " + pair.getValue0());
			}

			min = max;
			max += len;
			if (fc == 'B') { // 特殊处理转换特征数组
				for (int i = 0; i < len; i++) {
					String[] split = temp.split("=");
					int from = statusCoven[i / Config.TAG_NUM];
					int to = statusCoven[i % Config.TAG_NUM];
					status[from][to] = ObjConver.getFloatValue(split[1]);
					temp = br.readLine();
				}
			} else {

				name = pair.getValue1();

				tempW = new float[len];

				do {
					String[] split = temp.split("=");

					key = ObjConver.getIntValue(split[0]);

					if (key >= max) { // 如果超过边界那么跳出
						break;
					}

					offe = key - min;

					tag = statusCoven[offe];

					tempW[tag] = ObjConver.getFloatValue(split[1]);

				} while ((temp = br.readLine()) != null);

				this.featureTree.add(name, tempW); // 将特征增加到特征🌲中

				// printFeatureTree(name, tempW);
			}

		}

	}

	/**
	 * 增加特征到特征数中
	 * 
	 * @param cs
	 * @param tempW
	 */

	private static void printFeatureTree(String cs, float[] tempW) {
		String name = "*";
		if (tempW.length == 4) {
			name = "U";
		}

		name += "*" + ((int) cs.charAt(cs.length() - 1) - Config.FEATURE_BEGIN + 1) + ":" + cs.substring(0, cs.length() - 1);
		for (int i = 0; i < tempW.length; i++) {
			if (tempW[i] != 0) {
				System.out.println(name + "\t" + Config.getTagName(i / 4 - 1) + "\t" + Config.getTagName(i % 4) + "\t" + tempW[i]);
			}

		}

	}

	/**
	 * 加载特征值 //11:*6:_x-1/的,
	 * 
	 * @param featureIndex
	 * 
	 * @param br
	 * @return
	 * @throws Exception
	 */

	private List<Pair<String, String>> loadFeatureName(Map<String, Integer> featureIndex, BufferedReader br) throws Exception {
		String temp = br.readLine();// #qrk#num
		int featureNum = ObjConver.getIntValue(StringUtil.matcherFirst("\\d+", temp)); // 找到特征个数

		List<Pair<String, String>> featureNames = new ArrayList<Pair<String, String>>();

		for (int i = 0; i < featureNum; i++) {
			temp = br.readLine();

			String[] split = temp.split(":");

			if (split.length == 2) {
				featureNames.add(Pair.with(split[1], ""));
				continue;
			} else {

				String name = split[2];

				if (split.length > 3) {
					for (int j = 3; j < split.length; j++) {
						name += ":" + split[j];
					}
				}

				// 去掉最后的空格
				name = name.substring(0, name.length() - 1);

				int lastFeatureId = featureIndex.get(split[1]);

				if ("/".equals(name)) {
					name = "//";
				}

				if (name.contains("//")) {
					name = name.replaceAll("//", "/XIEGANG/");
				}
				String featureName = toFeatureName(name.trim().split("/"), lastFeatureId);

				featureNames.add(Pair.with(split[1], featureName));

			}
		}

		return featureNames;

	}

	private String toFeatureName(String[] split, int lastFeatureId) throws Exception {

		StringBuilder result = new StringBuilder();

		for (String str : split) {
			if ("".equals(str)) {
				continue;
			} else if (str.length() == 1) {
				result.append(str.charAt(0));
			} else if (str.equals("XIEGANG")) {
				result.append('/');
			} else if (str.startsWith("num")) {
				result.append((char) (Config.NUM_BEGIN + ObjConver.getIntValue(str.replace("num", ""))));
			} else if (str.startsWith("en")) {
				result.append((char) (Config.EN_BEGIN + ObjConver.getIntValue(str.replace("en", ""))));
			} else if (str.startsWith("_x-")) {
				result.append(Config.BEGIN);
			} else if (str.startsWith("_x+")) {
				result.append(Config.END);
			} else {
				throw new Exception("can find feature named " + str + " in " + Arrays.toString(split));
			}
		}

		result.append((char) (lastFeatureId + Config.FEATURE_BEGIN));

		return result.toString();
	}

	/**
	 * 加载特征标签转换
	 * 
	 * @param br
	 * @return
	 * @throws Exception
	 */
	private int[] loadTagCoven(BufferedReader br) throws Exception {

		int[] conver = new int[Config.TAG_NUM + Config.TAG_NUM * Config.TAG_NUM];

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

		for (int i = Config.TAG_NUM; i < conver.length; i++) {
			conver[i] = conver[(i - 4) / Config.TAG_NUM] * Config.TAG_NUM + conver[i % Config.TAG_NUM] + Config.TAG_NUM;
		}

		return conver;
	}

	/**
	 * 加载特征模板
	 * 
	 * @param br
	 * @return
	 * @throws IOException
	 */
	private Map<String, Integer> loadConfig(BufferedReader br) throws IOException {

		Map<String, Integer> featureIndex = new HashMap<String, Integer>();

		String temp = br.readLine();// #rdr#8/0/0

		int featureNum = ObjConver.getIntValue(StringUtil.matcherFirst("\\d+", temp)); // 找到特征个数

		List<int[]> list = new ArrayList<int[]>();

		for (int i = 0; i < featureNum; i++) {
			temp = br.readLine();

			List<String> matcherAll = StringUtil.matcherAll("\\[.*?\\]", temp);

			if (matcherAll.size() == 0) {
				continue;
			}

			int[] is = new int[matcherAll.size()];
			for (int j = 0; j < is.length; j++) {
				is[j] = ObjConver.getIntValue(StringUtil.matcherFirst("[-\\d]+", matcherAll.get(j)));
			}

			featureIndex.put(temp.split(":")[1], list.size());

			list.add(is);
		}

		int[][] template = new int[list.size()][0]; // 构建特征模板

		for (int i = 0; i < template.length; i++) {
			template[i] = list.get(i);
		}

		config = new Config(template);

		return featureIndex;
	}

	public static void main(String[] args) throws Exception {

		WapitiCRFModel model = new WapitiCRFModel("wapiti");
		model.loadModel("/Users/sunjian/Documents/src/Wapiti/test/model.dat");

		List<String> list = new ArrayList<String>();
		list.add("李宇春《再不疯狂我们就老了》MV首播】李宇春新专辑同名第二主打《再不疯狂我们就老了》MV今日正式发布。这首歌与《似火年华》，以“疯狂”为概念的对话曲目，采用一曲双词的方式。李宇春与韩寒，同时在一首歌里，讲述了两种截然相反，却本质同归的态度");
		list.add("上个月在天津术语学会上见到冯老，言谈中感觉到冯老对机器翻译的深厚感情和殷切希望。是啊，机器翻译事业还年轻，我辈细流，心驰沧海，愿倾尽绵薄之力，浇灌此常青之树。");
		list.add("发表了博文 《多语言信息网络时代的语言学家：冯志伟》 - 冯志伟与老伴郑初阳 多语言信息网络时代的语言学家：冯志伟桂清扬 冯志伟，教育部语言文字应用研究所研究员，博士生导师，所学术委员会");
		list.add("Facebook CEO 马克·扎克伯格亮相了周二 TechCrunch Disrupt大会，并针对公司不断下挫的股价、移动战略、广告业务等方面发表了讲话。自 5 月公司 IPO后，扎克伯格极少公开露面，这也是他首次在重要场合公开接受采访");
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
		list.add("中新网10月20日电据日本共同社报道，日本民主党代理干事长安住淳20日表示，首相野田佳彦将履行“近期”解散众院举行大选的承诺，预计在“公债发行特例法案”获得通过等条件具备时解散众院。");
		list.add("邓颖超生前最喜欢的画像");
		list.add("习近平和朱镕基情切照相");
		list.add("能不能试试这个 西伯利亚雅特大教堂位于俄罗斯东西伯利亚地区");
		list.add("李克强");
		for (String string : list) {
			System.out.println(new SplitWord(model).cut(string));
		}
		;
	}
}
