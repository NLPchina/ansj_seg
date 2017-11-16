package org.ansj.app.phrase;

import org.ansj.domain.Term;
import org.ansj.library.StopLibrary;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.nlpcn.commons.lang.util.CollectionUtil;
import org.nlpcn.commons.lang.util.MapCount;
import org.nlpcn.commons.lang.util.StringUtil;

import java.util.*;

public class PhraseExtractor {

    public static double DEFAULT_TERM_FREQUENCY = 10.24;
    public static int TERM_MAP_CAPACITY = 100000;
    public static int OCCURRENCE_MAP_CAPACITY = 100000;
    public static float FACTOR = 0.2F;
    public static int DEDUP_THRESHOLD = 2000;

    /**
     * 默认使用NLP的分词方式
     */
    private Analysis analysis = new NlpAnalysis();

    private StopRecognition sr;

    /**
     * 子串最大长度
     */
    private int length = 10;

    private int totalTerm;

    private final Map<String, Integer> termMap = new HashMap<String, Integer>(TERM_MAP_CAPACITY);
    private final Map<String, Occurrence> occurrenceMap = new HashMap<String, Occurrence>(OCCURRENCE_MAP_CAPACITY);

    public PhraseExtractor() {
        final StopRecognition defaultSR = StopLibrary.get();
        this.sr = new StopRecognition() {
            @Override
            public boolean filter(Term term) {
                String nature = term.getNatureStr();
                return StringUtil.isBlank(nature) ||
                        "null".equals(nature) ||
                        "w".equals(nature) ||
                        defaultSR != null && defaultSR.filter(term);
            }
        };
    }

    public PhraseExtractor setAnalysis(Analysis analysis) {
        this.analysis = analysis;
        return this;
    }

    public PhraseExtractor setStopRecognition(StopRecognition sr) {
        this.sr = sr;
        return this;
    }

    public PhraseExtractor setLength(int length) {
        this.length = length;
        return this;
    }

    /**
     * 从文本中提取子串
     *
     * @param text
     */
    public void fromText(String text) {
        String str;
        Occurrence occ;
        List<Term> terms = new ArrayList<Term>();
        StringBuilder sb = new StringBuilder();
        for (List<Term> sentence : seg2sentence(text)) {
            totalTerm += sentence.size();

            // 产生ngram, 总串长度不超过length
            for (int i = 0, j, len = sentence.size(); i < len; ++i) {
                for (j = i; j < len; ++j) {
                    str = sentence.get(j).getRealName();
                    if (termMap.containsKey(str)) {
                        termMap.put(str, termMap.get(str) + 1);
                    } else {
                        addTerm(str);
                    }

                    sb.append(str);
                    if (length < sb.length()) {
                        break;
                    }

                    terms.add(sentence.get(j));

                    str = sb.toString();
                    occ = occurrenceMap.containsKey(str) ? occurrenceMap.get(str) : new Occurrence(new ArrayList<Term>(terms));

                    // 添加左邻项
                    if (0 < i) {
                        occ.addLeftTerm(sentence.get(i - 1).getRealName());
                    }

                    // 添加右临项
                    if (j < len - 1) {
                        occ.addRightTerm(sentence.get(j + 1).getRealName());
                    }

                    // 递增出现频次
                    occ.increaseFrequency();

                    //
                    if (occ.getFrequency() == 1) {
                        addOccurrence(str, occ);
                    }
                }

                //
                terms.clear();
                sb.delete(0, sb.length());
            }
        }
    }

    private void addTerm(String t) {
        // 超过容量, 移除低频TERM
        int capacity = (int) (TERM_MAP_CAPACITY * (1 + FACTOR));
        if (capacity <= termMap.size()) {
            List<Map.Entry<String, Integer>> items = CollectionUtil.sortMapByValue(termMap, 1);
            for (Map.Entry<String, Integer> item : items.subList(TERM_MAP_CAPACITY, items.size())) {
                termMap.remove(item.getKey());
            }
        }

        //
        termMap.put(t, 1);
    }

    private void addOccurrence(String k, Occurrence occurrence) {
        // 超过容量
        int capacity = (int) (OCCURRENCE_MAP_CAPACITY * (1 + FACTOR));
        if (capacity <= occurrenceMap.size()) {

            calculateScore();

            List<Map.Entry<String, Occurrence>> ordered = new ArrayList<Map.Entry<String, Occurrence>>(occurrenceMap.entrySet());
            Collections.sort(ordered, new Comparator<Map.Entry<String, Occurrence>>() {
                @Override
                public int compare(Map.Entry<String, Occurrence> o1, Map.Entry<String, Occurrence> o2) {
                    return -Double.compare(o1.getValue().getScore(), o2.getValue().getScore());
                }
            });

            for (Map.Entry<String, Occurrence> item : ordered.subList(OCCURRENCE_MAP_CAPACITY, ordered.size())) {
                occurrenceMap.remove(item.getKey());
            }
        }

        //
        occurrenceMap.put(k, occurrence);
    }

    public List<Map.Entry<String, Occurrence>> nbest(int size) {
        // 重新计算得分
        calculateScore();

        //
        Occurrence occ;
        Set<String> toRemove = new HashSet<>();
        List<Term> terms;
        for (Map.Entry<String, Occurrence> entry : occurrenceMap.entrySet()) {
            occ = entry.getValue();
            terms = occ.getTerms();
            if ((terms.size() < 2 && !terms.get(0).isNewWord() && entry.getKey().length() < 5) ||
                    entry.getKey().length() < 2 ||
                    Double.compare(Math.max(occ.getLeftEntropy(), occ.getRightEntropy()), 0) <= 0) {
                toRemove.add(entry.getKey());
            }
        }
        occurrenceMap.keySet().removeAll(toRemove);
        toRemove.clear();

        List<Map.Entry<String, Occurrence>> entryList = new ArrayList<Map.Entry<String, Occurrence>>(occurrenceMap.entrySet());

        // TODO: 如果短语量大, 去重会很慢
        if (occurrenceMap.size() < DEDUP_THRESHOLD) {
            dedup(entryList, toRemove);
            occurrenceMap.keySet().removeAll(toRemove);

            entryList.clear();
            entryList.addAll(occurrenceMap.entrySet());
        }

        // 排序
        Collections.sort(entryList, new Comparator<Map.Entry<String, Occurrence>>() {
            @Override
            public int compare(Map.Entry<String, Occurrence> o1, Map.Entry<String, Occurrence> o2) {
                return -Double.compare(o1.getValue().getScore(), o2.getValue().getScore());
            }
        });

        // 取size个
        List<Map.Entry<String, Occurrence>> phraseList = new ArrayList<Map.Entry<String, Occurrence>>(size);
        for (Map.Entry<String, Occurrence> entry : entryList) {
            if (phraseList.size() == size) {
                break;
            }

            phraseList.add(entry);
        }

        return phraseList;
    }

    private void calculateScore() {
        Occurrence occ;
        Set<Map.Entry<String, Occurrence>> entries = occurrenceMap.entrySet();
        double totalPMI = 0., totalLeftEntropy = 0., totalRightEntropy = 0., temp;
        MapCount<String> frequencyMC = new MapCount<String>(), degreeMC = new MapCount<String>();
        for (Map.Entry<String, Occurrence> entry : entries) {
            occ = entry.getValue();

            // PMI
            occ.setPmi(calculateMutualInformation(entry.getKey(), occ.getTerms()));
            totalPMI += occ.getPmi();

            // 左邻熵
            occ.setLeftEntropy(calculateEntropy(occ.getLeftTerms()));
            totalLeftEntropy += occ.getLeftEntropy();

            // 右邻熵
            occ.setRightEntropy(calculateEntropy(occ.getRightTerms()));
            totalRightEntropy += occ.getRightEntropy();

            //
            temp = occ.getTerms().size() - 1;
            for (Term term : occ.getTerms()) {
                frequencyMC.add(term.getRealName());
                degreeMC.add(term.getRealName(), temp);
            }
        }

        Map<String, Double> scoreMap = new HashMap<String, Double>(frequencyMC.size());
        for (Map.Entry<String, Double> entry : frequencyMC.get().entrySet()) {
            scoreMap.put(entry.getKey(), (degreeMC.get().get(entry.getKey()) + entry.getValue()) / entry.getValue());
        }

        double total = 0.;
        for (Map.Entry<String, Occurrence> entry : entries) {
            occ = entry.getValue();

            temp = 0.;
            for (Term term : occ.getTerms()) {
                temp += scoreMap.get(term.getRealName());
            }
            occ.setScore(temp);

            total += temp;
        }

        for (Map.Entry<String, Occurrence> entry : entries) {
            occ = entry.getValue();
            occ.setScore(occ.getPmi() / totalPMI + occ.getLeftEntropy() / totalLeftEntropy + occ.getRightEntropy() / totalRightEntropy + occ.getScore() / total);
            calculateScore2(occ);
        }
    }

    private void calculateScore2(Occurrence occ) {
        String nature;
        nature = occ.getTerms().get(0).getNatureStr();
        if ("c".equals(nature) || "p".equals(nature) || 'u' == nature.charAt(0)) {
            occ.setScore(occ.getScore() * 0.001);
        }
        nature = occ.getTerms().get(occ.getTerms().size() - 1).getNatureStr();
        if ("c".equals(nature) || "p".equals(nature) || 'u' == nature.charAt(0)) {
            occ.setScore(occ.getScore() * 0.001);
        }
    }

    /**
     * 计算熵
     */
    private double calculateEntropy(MapCount<String> mc) {
        double totalFrequency = 0.;
        Set<Map.Entry<String, Double>> entrySet = mc.get().entrySet();
        for (Map.Entry<String, Double> entry : entrySet) {
            totalFrequency += entry.getValue();
        }

        double entropy = 0., p;
        for (Map.Entry<String, Double> entry : entrySet) {
            p = entry.getValue() / totalFrequency;
            entropy += -p * Math.log(p);
        }

        return entropy;
    }

    /**
     * 计算点互信息
     */
    private double calculateMutualInformation(String phrase, List<Term> terms) {
        int size = terms.size();
        if (size == 1) {
            return -Math.log(getFrequency(terms.get(0).getRealName()) / totalTerm);
        }

        double product = 1.;
        for (Term term : terms) {
            product *= getFrequency(term.getRealName());
        }

        return Math.log(occurrenceMap.get(phrase).getFrequency() * Math.pow(totalTerm, size - 1)) - Math.log(product);
    }

    private double getFrequency(String t) {
        return termMap.containsKey(t) ? termMap.get(t) : DEFAULT_TERM_FREQUENCY;
    }

    private void dedup(List<Map.Entry<String, Occurrence>> entryList, Set<String> toRemove) {
        Map.Entry<String, Occurrence> e1, e2;
        double pmi1, pmi2, entropy1, entropy2;
        for (int i = 0, size = entryList.size(); i < size; ++i) {
            e1 = entryList.get(i);
            pmi1 = e1.getValue().getPmi();
            entropy1 = (e1.getValue().getLeftEntropy() + e1.getValue().getRightEntropy()) / 2.0;

            for (int j = i + 1; j < size; ++j) {
                e2 = entryList.get(j);
                pmi2 = e2.getValue().getPmi();
                entropy2 = (e2.getValue().getLeftEntropy() + e2.getValue().getRightEntropy()) / 2.0;

                if (e1.getKey().contains(e2.getKey())) {
                    if (0 <= Double.compare(pmi1, pmi2) && 0 <= Double.compare(entropy1, entropy2)/* && 0. < entropy1*/) {
                        toRemove.add(entryList.get(j).getKey());
                    }
                } else if (e2.getKey().contains(e1.getKey())) {
                    if (0 <= Double.compare(pmi2, pmi1) && 0 <= Double.compare(entropy2, entropy1)/* && 0. < entropy2*/) {
                        toRemove.add(entryList.get(i).getKey());
                        break;
                    }
                } else if (.75 < calculateCosineSimilarity(e1.getValue().getTerms(), e2.getValue().getTerms())) {
                    if (e2.getKey().length() <= e1.getKey().length() && 0 <= Double.compare(pmi1, pmi2) && 0 <= Double.compare(entropy1, entropy2)/* && 0. < entropy1*/) {
                        toRemove.add(entryList.get(j).getKey());
                    } else if (e1.getKey().length() <= e2.getKey().length() && 0 <= Double.compare(pmi2, pmi1) && 0 <= Double.compare(entropy2, entropy1)/* && 0. < entropy2*/) {
                        toRemove.add(entryList.get(i).getKey());
                        break;
                    }
                }
            }
        }
    }

    private double calculateCosineSimilarity(List<Term> left, List<Term> right) {
        MapCount<String> leftMC = new MapCount<String>();
        for (Term t : left) {
            leftMC.add(t.getRealName());
        }
        double d1 = 0.;
        for (double value : leftMC.get().values()) {
            d1 += Math.pow(value, 2);
        }
        if (Double.compare(d1, 0) <= 0) {
            return 0.;
        }

        MapCount<String> rightMC = new MapCount<String>();
        for (Term t : right) {
            rightMC.add(t.getRealName());
        }
        double d2 = 0.;
        for (double value : rightMC.get().values()) {
            d2 += Math.pow(value, 2);
        }
        if (Double.compare(d2, 0) <= 0) {
            return 0.;
        }

        double dotProduct = 0.;
        Set<String> intersection = new HashSet<String>(leftMC.get().keySet());
        intersection.retainAll(rightMC.get().keySet());
        for (String key : intersection) {
            dotProduct += leftMC.get().get(key) * rightMC.get().get(key);
        }

        return dotProduct / (Math.sqrt(d1) * Math.sqrt(d2));
    }

    /**
     * 分词断句, 输出句子形式
     *
     * @param text 待分词句子
     * @return 句子列表, 每个句子由一个单词列表组成
     */
    private List<List<Term>> seg2sentence(String text) {
        List<String> sentenceList = toSentenceList(text);
        List<List<Term>> resultList = new ArrayList<List<Term>>(sentenceList.size());
        for (String sentence : sentenceList) {
            resultList.add(analysis.parseStr(sentence).recognition(sr).getTerms());
        }
        return resultList;
    }

    private List<String> toSentenceList(String content) {
        char ch;
        StringBuilder sb = new StringBuilder();
        List<String> sentences = new LinkedList<String>();
        for (int i = 0, len = content.length(); i < len; ++i) {
            ch = content.charAt(i);
            if (sb.length() == 0 && (Character.isWhitespace(ch) || ch == ' ')) {
                continue;
            }
            sb.append(ch);

            switch (ch) {
                case '.':
                    if (i < len - 1 && content.charAt(i + 1) > 128) {
                        insertIntoList(sb, sentences);
                        sb = new StringBuilder();
                    }
                    break;
                case '…':
                    if (i < len - 1 && content.charAt(i + 1) == '…') {
                        sb.append('…');
                        ++i;
                        insertIntoList(sb, sentences);
                        sb = new StringBuilder();
                    }
                    break;
                case ' ':
                case '	':
                case ' ':
                case '。':
                case '，':
                case ',':
                case ';':
                case '；':
                case '!':
                case '！':
                case '?':
                case '？':
                case '\n':
                case '\r':
                    insertIntoList(sb, sentences);
                    sb = new StringBuilder();
                    break;
                default:
                    break;
            }
        }

        if (sb.length() > 0) {
            insertIntoList(sb, sentences);
        }

        return sentences;
    }

    private void insertIntoList(StringBuilder sb, List<String> sentences) {
        String content = sb.toString().trim();
        if (content.length() > 0) {
            sentences.add(content);
        }
    }
}
