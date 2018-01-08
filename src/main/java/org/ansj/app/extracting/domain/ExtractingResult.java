package org.ansj.app.extracting.domain;

import org.ansj.app.extracting.ExtractingTask;
import org.ansj.domain.Term;

import java.util.*;

/**
 * Created by Ansj on 21/09/2017.
 */
public class ExtractingResult {

	private List<ExtractingTask> taskResults;


	public synchronized void add(ExtractingTask task) {
		if (taskResults == null) {
			taskResults = new ArrayList<>();
		}
		taskResults.add(task);
	}

	public List<List<Term>> findAll(){
		if (taskResults == null) {
			return Collections.emptyList();
		}

		List<List<Term>> result = new ArrayList<>(taskResults.size());
		RESULT:
		for (ExtractingTask task : taskResults) {
			List<Term> tempList = new ArrayList<>();
			for (List<Term> list : task.getList()) {
				if (list == null) {
					continue RESULT;
				}
				for (Term term : list) {
					tempList.add(term);
				}
			}
			result.add(tempList);

		}
		return result ;
	}

	public List<Map<String, String>> getAllResult() {
		if (taskResults == null) {
			return Collections.emptyList();
		}

		List<Map<String, String>> list = new ArrayList<>(taskResults.size());

		for (ExtractingTask task : taskResults) {
			list.add(getResult(task));
		}

		return list;

	}

	private Map<String, String> getResult(ExtractingTask task) {

		Rule rule = task.getRule();

		Map<String, String> result = new HashMap<>(rule.getAttr());
		if(rule.getAttr()!=null && rule.getAttr().size()>0) {
			result.putAll(rule.getAttr());
		}

		List[] list = task.getList();

		Map<String, int[]> groups = rule.getGroups();

		for (Map.Entry<String, int[]> entry : groups.entrySet()) {
			StringBuilder sb = new StringBuilder();

			for (int i : entry.getValue()) {
				if (list[i] == null) {
					continue;
				}
				for (Object o : list[i]) {
					sb.append(((Term) o).getName());
				}
			}

			result.put(entry.getKey(), sb.toString());
		}


		return result;

	}

}
