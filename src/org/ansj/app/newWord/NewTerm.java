package org.ansj.app.newWord;

import org.ansj.domain.Nature;
import org.ansj.domain.Term;

public class NewTerm  implements Comparable<NewTerm> , Cloneable {
	// 当前词
	private String name;
	// 当前词的起始位置
	private int offe;
	//权重
	public int weight = 0;
	//最可能词性
	public Nature maxNature;
	// term的版本号.不同的版本不可以合并
	private int version;
	
	private boolean isRemove = false ;

	
	private NewTerm from ;
	
	private NewTerm to ;

	public NewTerm(Term term) {
		super();
		this.name = term.getName();
		this.offe = term.getOffe();
		this.maxNature = term.getNatrue() ;
	}
	
	public NewTerm(NewTerm term){
		this.name = term.getName();
		this.maxNature = term.maxNature ;
		this.weight = term.weight;
	}


	public int getOffe() {
		return offe;
	}

	public void setOffe(int offe) {
		this.offe = offe;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name+"/"+this.weight;
	}

	public int getWeight() {
		return weight;
	}

	public void UpdateOffe(int baseOffe) {
		// TODO Auto-generated method stub
		this.offe += baseOffe;
	}



//	@Override
//	public int compareTo(CharcterTerm o) {
//		// TODO Auto-generated method stub
//		if (this.name.equals(o.name))
//			return 0;
//		if (this.weight > o.weight) {
//			return 1;
//		} else {
//			return -1;
//		}
//	}
//
//	@Override
//	public int hashCode() {
//		// TODO Auto-generated method stub
//		return name.hashCode();
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		// TODO Auto-generated method stub
//		CharcterTerm term = (CharcterTerm) obj;
//		return this.name.equals(term.getName());
//	}

//	public void addTerm(CharcterTerm ct) {
//		if (toMap == null) {
//			toMap = new HashMap<String, List<CharcterTerm>>();
//		}
//		List<CharcterTerm> all = toMap.get(ct.getName());
//
//		if (all == null) {
//			all = new ArrayList<CharcterTerm>();
//			toMap.put(ct.getName(), all) ;
//		}
//
//		ct.from = this ;
//		
//		this.to = ct ;
//		
//		all.add(ct) ;
//	}
	
	
//	public Map<String, List<CharcterTerm>> getToMap(){
//		return this.toMap ;
//	}
	
//	public void merger(int threshold){
//		if(this.toMap==null){
//			return ;
//		}
//		for (List<CharcterTerm> all : toMap.values()) {
//			if(all.size()>=threshold){
//				for (CharcterTerm charcterTerm : all) {
//					charcterTerm = charcterTerm.merger(charcterTerm.from) ;
//				}
//			}
//		}
//	}

	/**
	 * 进行term合并
	 * 
	 * @param term
	 * @param maxNature
	 */
	public void mergerFrom() {
		if(from==null||from.version>this.version) return ;
		this.name = from.name + this.getName();
		this.offe = from.offe ;
		from.isRemove = true ;
		this.version = from.version+1 ;
		this.from = from.from ;
	}


	public int getVersion() {
		return version;
	}


	public boolean isRemove() {
		return isRemove;
	}


	public NewTerm getFrom() {
		return from;
	}


	public void setFrom(NewTerm from) {
		this.from = from;
	}


	public void clean() {
		// TODO Auto-generated method stub
		this.weight += version ;
	}


	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.name.hashCode();
	}

	
	@Override
	public int compareTo(NewTerm o) {
		// TODO Auto-generated method stub
		if(this.weight<=o.weight){
			return 1 ;
		}else{
			return -1 ;
		}
	}


	public void updateScore(NewTerm charcterTerm) {
		// TODO Auto-generated method stub
		this.weight += 1 ;
	}

	
}