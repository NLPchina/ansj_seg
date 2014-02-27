#nlp分词

### nlp分词是什么
> nlp分词是总能给你惊喜的一种分词方式.
> 
> 它可以识别出未登录词.但是它也有它的缺点.速度比较慢.稳定性差.ps:我这里说的慢仅仅是和自己的其他方式比较.应该是40w字每秒的速度吧.
> 
> 个人觉得nlp的适用方式.1.语法实体名抽取.未登录词整理.只要是对文本进行发现分析等工作





### 精准分词具有什么功能

><table>
<thead><tr>
<th>用户自定义词典</th>
<th align="center">数字识别</th>
<th align="center">人名识别</th>
<th align="center">机构名识别</th>
<th align="center">新词发现</th>
</tr></thead>
<tbody><tr>
<td align="center">√</td>
<td align="center">√</td>
<td align="center">√</td>
<td align="center">√</td>
<td align="center">√</td>
</tr></tbody>
</table>

### 一个简单的使用方式
> 
		List<Term> parse = NlpAnalysis.parse("洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹");
		System.out.println(parse);


>	result:[洁面仪/nw, 配合/v, 洁面/nw, 深层/b, 清洁/a, 毛孔/n,  , 清洁/a, 鼻孔/n, 面膜/n, 碎觉/nw, 使劲/v, 挤/v, 才/d, 能/v, 出/v, 一点点/m, 皱纹/n,  , 脸颊/n, 毛孔/n, 修复/v, 的/uj, 看不见/v, 啦/y,  , 草莓/n, 鼻/ng, 历史/n, 遗留问题/nz, 没辙/v,  , 脸/n, 和/c, 脖子/n, 差不多/l, 颜色/n, 的/uj, 皮肤/n, 才/d, 是/v, 健康/a, 的/uj,  , 长期/d, 使用/v, 安全/an, 健康/a, 的/uj, 比/p, 同龄人/n, 显/v, 小/a, 五/m, 到/v, 十岁/m,  , 28岁/m, 的/uj, 妹子/n, 看看/v, 你们/r, 的/uj, 鱼尾纹/n]
