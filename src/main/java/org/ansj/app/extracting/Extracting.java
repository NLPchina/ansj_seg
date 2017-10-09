package org.ansj.app.extracting;

import org.ansj.app.extracting.domain.ExtractingResult;
import org.ansj.app.extracting.domain.Rule;
import org.ansj.app.extracting.domain.RuleIndex;
import org.ansj.app.extracting.exception.RuleFormatException;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.util.MyStaticValue;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 抽取引擎
 * <p>
 * <p>
 * Created by Ansj on 28/08/2017.
 */
public class Extracting {

	private RuleIndex ruleIndex = new RuleIndex();

	public Extracting(){

	}

	public Extracting(List<String> lines) throws RuleFormatException {
		addRules(lines);
	}


	public Extracting(InputStream is, String encoding) throws RuleFormatException, IOException {
		try {
			List<String> lines = IOUtil.readFile2List(IOUtil.getReader(is, encoding));
			addRules(lines);
		} finally {
			is.close();
		}
	}

	public Extracting(Reader reader) throws RuleFormatException, IOException {
		try {
			List<String> lines = IOUtil.readFile2List(new BufferedReader(reader));
			addRules(lines);
		} finally {
			reader.close();
		}
	}


	public void addRules(List<String> lines) throws RuleFormatException {
		for (String line : lines) {
			addRuleStr(line);
		}
	}

	public void addRule(Rule rule) {
		ruleIndex.add(rule);
	}

	public void addRuleStr(String line) throws RuleFormatException {
		if (StringUtil.isNotBlank(line)) {
			addRule(Lexical.parse(line));
		}
	}

	public ExtractingResult parse(String content, Forest... forests) {
		Forest[] myForests = null;
		if (forests == null || forests.length == 0) {
			myForests = new Forest[]{ruleIndex.getForest(), DicLibrary.get()};
		} else {
			myForests = new Forest[forests.length + 1];
			myForests[0] = ruleIndex.getForest();
			for (int i = 0; i < forests.length; i++) {
				myForests[i + 1] = forests[i];
			}
		}

		Result terms = DicAnalysis.parse(content, myForests);

		List<ExtractingTask> tasks = new ArrayList<>();

		ExtractingResult result = new ExtractingResult();

		for (int i = 0; i < terms.size(); i++) {
			Term term = terms.get(i);

			Set<Rule> sets = new HashSet<>();

			Set<Rule> rules = ruleIndex.getRules(term.getName());
			if (rules != null) {
				sets.addAll(rules);
			}

			rules = ruleIndex.getRules(":" + term.getNatureStr());
			if (rules != null) {
				sets.addAll(rules);
			}

			rules = ruleIndex.getRules(":*");
			if (rules != null) {
				sets.addAll(rules);
			}

			for (Rule rule : sets) {
				tasks.add(new ExtractingTask(result, rule, i, terms));
			}
		}

		for (ExtractingTask task : tasks) {
			task.run();
		}

		return result;
	}
}
