package org.ansj.app.extracting;

import org.ansj.app.extracting.domain.ExtractingResult;
import org.ansj.app.extracting.domain.Rule;
import org.ansj.app.extracting.domain.Token;
import org.ansj.domain.Result;
import org.ansj.domain.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ansj on 21/09/2017.
 */
public class ExtractingTask implements Runnable {
	private static enum Action {
		STOP, NEXT_TOKEN, NEXT_TERM, NEXT_TERM_TOKEN, OVER
	}

	private ExtractingResult result;
	private Rule rule;
	private int index;
	private Result terms;
	private int step;

	private List[] list;


	public ExtractingTask(ExtractingResult result, Rule rule, int index, Result terms) {
		this.result = result;
		this.rule = rule;
		this.index = index;
		this.terms = terms;
		this.list = new ArrayList[rule.getTokens().size()];
	}


	@Override
	public void run() {//排除一切干扰我只看我自己能走到多远
		List<Token> tokens = rule.getTokens();
		Token token = tokens.get(0);
		loop: for (int i = index; i < terms.size(); i++) {
			Term term = terms.get(i);
			switch (validate(token, term)) {
				case STOP: //此路不通
					return;
				case OVER: //说明匹配完毕
					result.add(this);
					return;
				case NEXT_TERM:
					break;
				case NEXT_TOKEN:
					i--;
					token = token.getNext();
					if (token == null) {
						break loop;
					}
					break;
				case NEXT_TERM_TOKEN:
					token = token.getNext();
					break;
			}
		}

		boolean end = true;

		while (token!=null && (token = token.getNext()) != null) { //判断是否结尾
			if (token.getRange() == null || token.getRange()[0] != 0) {
				end = false;
			}
		}

		if (end) {
			result.add(this);

		}
	}

	/**
	 * token 和 term进行验证
	 *
	 * @param token
	 * @param term
	 * @return
	 */
	private Action validate(Token token, Term term) {

		if (token == null) {
			return Action.OVER;
		}

		int[] range = token.getRange();

		if (_validate(token, term)) {
			if (range != null && range[0] == 0) {
				return Action.NEXT_TOKEN;
			} else if (range != null && step >= range[0] && step <= range[1]) {
				step = 0;
				return Action.NEXT_TOKEN;
			} else {
				return Action.STOP;
			}
		}


		if (range == null) {
			insertInto(term, token.getIndex());
			if (token.getNext() == null) {
				return Action.OVER;
			} else {
				return Action.NEXT_TERM_TOKEN;
			}
		} else {
			if (step < range[0]) {
				step++;
				insertInto(term, token.getIndex());
				return Action.NEXT_TERM;
			}

			if (step > range[1]) {
				return Action.STOP;
			}

			step++;
			if (step > range[0]) {
				if (_validate(token.getNext(), term)) {
					insertInto(term, token.getIndex());
					if (token.getNext() == null) {
						if (step >= range[1]) {
							return Action.OVER;
						} else {
							return Action.NEXT_TERM;
						}
					} else {
						return Action.NEXT_TERM;
					}
				} else {
					step = 0;
					return Action.NEXT_TOKEN;
				}
			} else {
				insertInto(term, token.getNext().getIndex());
				return Action.NEXT_TERM;
			}

		}


	}

	private void insertInto(Term term, int index) {
		List<Term> tempList = this.list[index];
		if (tempList == null) {
			tempList = new ArrayList();
			this.list[index] = tempList;
		}
		tempList.add(term);
	}


	/**
	 * 验证term的名字或者词性是否符合条件
	 *
	 * @param token
	 * @param term
	 * @return true, 不符合，false  符合
	 */
	private boolean _validate(Token token, Term term) {

		if (token == null) {
			return true;
		}

		Set<String> terms = token.getTerms();
		if ((!terms.contains(Token.ALL)) && !terms.contains(term.getName()) && !(terms.contains(":" + term.getNatureStr()))) {
			return true;
		}

		boolean flag = token.getRegexs().size() != 0;

		for (String regex : token.getRegexs()) {
			if (term.getName().matches(regex)) {
				flag = false;
				break;
			}
		}

		return flag;
	}

	public int getIndex() {
		return index;
	}

	public Rule getRule() {
		return rule;
	}

	public List[] getList() {
		return list;
	}
}
