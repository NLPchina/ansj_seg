package org.ansj.recognition;

import com.google.common.collect.ImmutableList;
import org.ansj.NewWord;
import org.ansj.Term;
import org.ansj.TermNatures;
import org.ansj.AnsjContext;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * 外国人名识别
 *
 * @author ansj
 */
public class ForeignPersonRecognition {

    private static class NameChar {

        private final char[] chars;

        public NameChar(final String chars) {
            this.chars = sortCharArray(chars);
        }

        public boolean contains(final String name) {
            return contains(name.charAt(0));
        }

        public boolean contains(final char c) {
            return Arrays.binarySearch(chars, c) > -1;
        }

        /**
         * 将一个字符串.转换成排序后的字符数组
         */
        public static char[] sortCharArray(String str) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            return chars;
        }
    }

    private static final NameChar INNAME;

    public static boolean isFName(final String name) {
        for (final char c : name.toCharArray()) {
            if (!INNAME.contains(c)) {
                return false;
            }
        }
        return true;
    }

    private static final List<NameChar> PRLIST;
    private static final Set<Character> ISNOTFIRST;

    static {
        INNAME = new NameChar("-·—丁万丘东丝中丹丽乃久义乌乔买于亚亨京什仑仓代以仲伊伍伏伐伦伯伽但佐佛佩依侯俄保儒克兰其兹内冈凯切列利别力加努劳勃勒北华卓南博卜卡卢卫厄历及古可史叶司各合吉吐君吾呼哈哥哲唐喀善喇喜嘉噶因图土圣坎坚坦埃培基堡塔塞增墨士夏多大夫奇奈奎契奥妮姆威娃娅娜孜季宁守安宜宰密察尔尕尤尧尼居山川差巴布希帕帝干平年库庞康廉弗强当彦彭彻彼律得德恩恰慈慕戈戴才扎托拉拜捷提摩敏敖敦文斐斯新施日旦旺昂明普智曼朗木本札朱李杜来杰林果查柯柴根格桑梅梵森楞次欣欧歇武比毕汀汉汗汤汶沁沃沙河治泉泊法波泰泼泽洛津济浦海涅温滕潘澳烈热爱牙特狄王玛玻珀珊珍班理琪琳琴瑞瑟瓜瓦甫申畔略登白皮盖盟相石祖福科穆立笆简米素索累约纳纽绍维罕罗翁翰考耶聂肉肯胡胥腓舍良色艾芙芬芭苏若英茂范茅茨茹荣莉莎莫莱莲菲萨葛蒂蒙虏蜜衣裴西詹让诗诸诺谢豪贝费贾赖赛赫路辛达迈连迦迪逊道那邦郎鄂采里金钦锡门阿陀陶隆雅雍雷霍革韦音额香马魏鲁鲍麦黎默黛齐");

        final NameChar trans_english = new NameChar("·-—阿埃艾爱安昂敖奥澳笆芭巴白拜班邦保堡鲍北贝本比毕彼别波玻博勃伯泊卜布才采仓查差柴彻川茨慈次达大戴代丹旦但当道德得登迪狄蒂帝丁东杜敦多额俄厄鄂恩尔伐法范菲芬费佛夫福弗甫噶盖干冈哥戈革葛格各根古瓜哈海罕翰汗汉豪合河赫亨侯呼胡华霍基吉及加贾坚简杰金京久居君喀卡凯坎康考柯科可克肯库奎拉喇莱来兰郎朗劳勒雷累楞黎理李里莉丽历利立力连廉良列烈林隆卢虏鲁路伦仑罗洛玛马买麦迈曼茅茂梅门蒙盟米蜜密敏明摩莫墨默姆木穆那娜纳乃奈南内尼年涅宁纽努诺欧帕潘畔庞培佩彭皮平泼普其契恰强乔切钦沁泉让热荣肉儒瑞若萨塞赛桑瑟森莎沙山善绍舍圣施诗石什史士守斯司丝苏素索塔泰坦汤唐陶特提汀图土吐托陀瓦万王旺威韦维魏温文翁沃乌吾武伍西锡希喜夏相香歇谢辛新牙雅亚彦尧叶依伊衣宜义因音英雍尤于约宰泽增詹珍治中仲朱诸卓孜祖佐伽娅尕腓滕济嘉津赖莲琳律略慕妮聂裴浦奇齐琴茹珊卫欣逊札哲智兹芙汶迦珀琪梵斐胥黛");
        final NameChar trans_russian = new NameChar("·-阿安奥巴比彼波布察茨大德得丁杜尔法夫伏甫盖格哈基加坚捷金卡科可克库拉莱兰勒雷里历利连列卢鲁罗洛马梅蒙米姆娜涅宁诺帕泼普奇齐乔切日萨色山申什斯索塔坦特托娃维文乌西希谢亚耶叶依伊以扎佐柴达登蒂戈果海赫华霍吉季津柯理琳玛曼穆纳尼契钦丘桑沙舍泰图瓦万雅卓兹");
        // 注释掉了日本人名. 表面上是抵制日货.背地里是处理不好..
        // final NameChar trans_japanese = new NameChar("安奥八白百邦保北倍本比滨博步部彩菜仓昌长朝池赤川船淳次村大代岛稻道德地典渡尔繁饭风福冈高工宫古谷关广桂贵好浩和合河黑横恒宏后户荒绘吉纪佳加见健江介金今进井静敬靖久酒菊俊康可克口梨理里礼栗丽利立凉良林玲铃柳隆鹿麻玛美萌弥敏木纳南男内鸟宁朋片平崎齐千前浅桥琴青清庆秋丘曲泉仁忍日荣若三森纱杉山善上伸神圣石实矢世市室水顺司松泰桃藤天田土万望尾未文武五舞西细夏宪相小孝新星行雄秀雅亚岩杨洋阳遥野也叶一伊衣逸义益樱永由有佑宇羽郁渊元垣原远月悦早造则泽增扎宅章昭沼真政枝知之植智治中忠仲竹助椎子佐阪坂堀荻菅薰浜濑鸠筱");
        PRLIST = ImmutableList.copyOf(newArrayList(
                trans_russian
                , trans_english
                //, trans_japanese
        ));
        ISNOTFIRST = newHashSet('-', '·', '—');
    }

    private Term[] terms;
    private List<NameChar> prList;
    private final List<Term> tempList;

    public ForeignPersonRecognition(final Term[] terms) {
        this.terms = terms;
        this.tempList = newArrayList();
        this.prList = null;
    }

    private void reset() {
        prList = newArrayList(PRLIST);
        tempList.clear();
    }

    public List<Term> recognition() {
        final List<Term> result = newArrayList();
        reset();
        for (final Term term : terms) {
            if (term == null) {
                continue;
            }
            // 如果名字的开始是人名的前缀,或者后缀.那么忽略
            if (tempList.isEmpty() && (term.getTermNatures().personAttr.end > 10 || term.length() == 1 && ISNOTFIRST.contains(term.firstChar()))) {
                continue;
            }
            if (term.getTermNatures() == TermNatures.NR || term.getTermNatures() == TermNatures.NW || term.length() == 1) {
                if (validate(term.getName())) {
                    tempList.add(term);
                }
            } else if (tempList.size() == 1) {
                reset();
            } else if (tempList.size() > 1) {
                tempList.forEach(t -> this.terms[t.getOffe()] = null);
                final int offe = tempList.get(0).getOffe();
                final Term t = new Term(concat(tempList), offe, TermNatures.NR);
                terms[offe] = t;
                result.add(t);
                reset();
            }
        }
        return result;
    }

    private boolean validate(final String name) {
        boolean flag = false;
        for (int j = 0; j < this.prList.size(); j++) {
            final NameChar nameChar = this.prList.get(j);
            if (nameChar.contains(name)) {
                flag = true;
            } else {
                this.prList.remove(j);
                j--;// 向后回退一位
            }
        }
        return flag;
    }

    static String concat(final List<Term> terms) {
        return terms.stream().map(Term::getName).collect(joining());
    }

    public List<NewWord> getNewWords() {
        return this.recognition()
                .stream()
                .map(term -> new NewWord(term.getName(), AnsjContext.natureLibrary.NATURE_NRF()))
                .collect(toList());
    }
}
