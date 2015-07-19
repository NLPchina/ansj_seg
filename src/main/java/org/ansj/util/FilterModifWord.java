package org.ansj.util;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.nlpcn.commons.lang.tire.domain.Forest;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.ansj.util.AnsjContext.CONTEXT;

/**
 * 停用词过滤,修正词性到用户词性.
 */
public class FilterModifWord {

    private static final String TAG = "#";

    private final Set<String> filter;

    private final boolean isTag;

    private FilterModifWord(final Set<String> filter, final boolean isTag) {
        this.filter = filter;
        this.isTag = isTag;
    }

    public FilterModifWord() {
        this(new HashSet<>(), false);
    }

    public FilterModifWord withStopWords(final String... filterWords) {
        final Set<String> filter = new LinkedHashSet<>(this.filter);
        filter.addAll(asList(filterWords));
        return new FilterModifWord(filter, this.isTag);
    }

    public FilterModifWord withStopNatures(final String... filterNatures) {
        final Set<String> filter = new LinkedHashSet<>(this.filter);
        filter.addAll(asList(filterNatures).stream().map(natureStr -> TAG + natureStr).collect(toList()));
        return new FilterModifWord(filter, true);
    }

    /*
     * 停用词过滤并且修正词性
     */
    public List<Term> modifResult(final List<Term> all) {
        final List<Term> result = new ArrayList<>();
        for (Term term : all) {
            if (this.contains(term)) {
                continue;
            }
            final String[] params = CONTEXT().getUserDefineLibrary().getParams(term.getName());
            if (params != null) {
                term.setNature(new Nature(params[0]));
            }
            result.add(term);
        }
        return result;
    }

    /*
     * 停用词过滤并且修正词性
     */
    public List<Term> modifResult(final List<Term> all, final Forest... forests) {
        final List<Term> result = new ArrayList<>();
        for (final Term term : all) {
            if (this.contains(term)) {
                continue;
            }
            for (final Forest forest : forests) {
                final String[] params = UserDefineLibrary.getParams(forest, term.getName());
                if (params != null) {
                    term.setNature(new Nature(params[0]));
                }
            }
            result.add(term);
        }
        return result;
    }

    private boolean contains(final Term term) {
        return this.filter.size() > 0 && (this.filter.contains(term.getName()) ||
                this.filter.contains(TAG + term.getNature().natureStr));
    }
}
