package org.ansj.domain;

/**
 * 路径权重
 * 
 * @author ansj
 */
public class Path {
	public static final Path NULLPATH = new Path(TermNature.NULL, Term.NULL);
	public int index = 0;
	private TermNature tn;
	private Path from;
	private Path to;
	private double score;
	private Term term;

	public Path(TermNature termNature, Term term) {
		this.tn = termNature;
		this.term = term;
	}

	public Path getFrom() {
		return from;
	}

	public void setFrom(Path from) {
		this.from = from;
	}

	public Path getTo() {
		return to;
	}

	public void setTo(Path to) {
		this.to = to;
	}

	public double getScore() {
		return score;
	}

	public TermNature getTermNature() {
		return tn;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public String getNatureStr() {
		// TODO Auto-generated method stub
		return this.getTermNature().nature.natureStr;
	}

	public void setFromAndScore(Path from, double score) {
		// TODO Auto-generated method stub
		this.from = from;
		this.score = score;
		this.index = from.index + 1;
	}

	public String toString() {
		return this.term.getName() + "/" + this.tn.nature.natureStr;
	}

	public void setScore(double score) {
		// TODO Auto-generated method stub
		this.score = score;
	}

	public Path merage(Path to, TermNature termNature) {
		// TODO Auto-generated method stub
		this.getTerm().merage(to.getTerm()) ;
		this.setTo(to.getTo());
		this.getTo().setFrom(this);
		this.tn = termNature ;
		return this;
	}

}
