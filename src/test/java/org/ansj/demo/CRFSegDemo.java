package org.ansj.demo;

import org.ansj.util.MyStaticValue;

public class CRFSegDemo {
	public static void main(String[] args) {
		System.out.println(MyStaticValue.getCRFSplitWord().cut("由于霍华德、阿尔德里奇(微博)和波什都因伤退出，内线又一次成为美国男篮的问题，以目前球队的大名单，两大内线钱德勒和勒夫出战奥运几乎可以确定。科朗吉洛接受福克斯体育的电话采访时明确表示奥运阵容已确认9人。2008年北京奥运夺冠的科比、詹姆斯、安东尼、保罗和德隆肯定亮相伦敦，2010年世锦赛上帮助球队夺冠的杜兰特、威斯布鲁克、勒夫和钱德勒也会入选奥运名单。"));
		System.out.println(MyStaticValue.getCRFSplitWord().cut("關注十八大：台港澳密集解讀十八大報告”"));
		System.out.println(MyStaticValue.getCRFSplitWord().cut("这次回家，我经济南下广州"));
		System.out.println(MyStaticValue.getCRFSplitWord().cut("从古至今为何经济南强北弱?军事则北强南弱?_百度知道"));
		System.out.println(MyStaticValue.getCRFSplitWord().cut("马云说，马云说，马云今天参加马云天天和中粮集团, 天安天安天安天安天安，我在天安门"));
	}
}
