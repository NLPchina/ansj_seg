package org.ansj.demo;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;

import java.util.Collection;

/**
 * 关键词提取的例子
 * @author ansj
 *
 */
public class KeyWordCompuerDemo {
	public static void main(String[] args) {
		KeyWordComputer kwc = new KeyWordComputer(20);
		String title = "中国海上搜救中心确定中国搜救船舶搜救方案";
		String content = " \n \n中新网北京3月10日电 10日上午，中国海上搜救中心组织召开马航失联客机海上搜救紧急会商会议，中国交通运输部副部长、中国海上搜救中心主任何建中对当前搜救工作做出部署：一要加强与马来西亚等多方搜救组织的沟通协调；二要根据搜救现场情况进一步完善搜救方案；三要加强信息交流共享，做好内外联动。 \n \n马航客机失联事件发生后，交通运输部启动一级应急响应，3月8日、9日4次召开马航失联客机应急反应领导小组工作会议，研判形势，部署搜寻工作。根据《国家海上搜救和重大海上溢油应急处置紧急会商工作制度》，交通运输部、国家海洋局、中国海警局、总参、海军等共同研究制定了中国船舶及航空器赴马航客机失联海域搜救方案，初步明确了“海巡31”、“南海救101”、“南海救115”、中国海警3411、海军528和999舰等6艘中国搜救船舶的海上搜救区域。 \n \n截至3月10日8时，中国海军528舰和中国海警3411舰已在相关区域开展搜救工作，预计交通运输部所属“南海救115”、“海巡31”轮、“南海救101”将先后于10日16时、11时17时、11日22时抵达马航客机疑似失联海域。中国海上搜救中心已将有关情况通报马来西亚海上搜救机构，并将与马来西亚、越南海上搜救机构保持密切联系，开展深度配合。同时，继续协调中国商船参与搜救。(完)(周音)";
		Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
		System.out.println(result);
	}
}
