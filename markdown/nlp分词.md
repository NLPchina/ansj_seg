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
<th align="left">数字识别</th>
<th align="center">人名识别</th>
<th align="right">机构名识别</th>
<th align="right">新词发现</th>
</tr></thead>
<tbody><tr>
<td>Χ</td>
<td align="left">√</td>
<td align="center">√</td>
<td align="right">√</td>
<td align="right">√</td>
</tr></tbody>
</table>

### 一个简单的使用方式
> 
		List<Term> parse = NlpAnalysis.parse("让战士们过一个欢乐祥和的新春佳节。");
		System.out.println(parse);

