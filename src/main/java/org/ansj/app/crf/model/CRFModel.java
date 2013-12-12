//package org.ansj.train.model;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.ansj.train.pojo.Element;
//import org.ansj.train.pojo.Feature;
//
//public class CRFModel extends Model {
//	
//	/**
//	 * sum all the value z(x)
//	 * 
//	 * @param grad
//	 * @return
//	 */
//	private double sum(Map<String, Feature> grad) {
//		// TODO Auto-generated method stub
//		Collection<Feature> values = grad.values();
//		double l1 = 0;
//
//		for (Feature feature : values) {
//			l1 += feature.sum();
//		}
//		return l1;
//	}
//
//
//	// public static void main(String[] args) {
//	//
//	//
//	// // perform regularization
//	// double regLik = 0;
//	// for (Entry<String, Feature> entry : featureMap.entrySet()) {
//	// Feature f = new Feature(entry.getValue());
//	// grad.put(entry.getKey(), f);
//	// regLik += f.regLik;
//	// }
//	//
//	//
//	//
//	//
//	// calcGradient.calcGradient(list, featureMap);
//	// for (Entry<String, Feature> element : calcGradient.myGrad.entrySet()) {
//	// if (grad.containsKey(element.getKey())) {
//	// grad.get(element.getKey()).merge(element.getValue());
//	// } else {
//	// grad.put(element.getKey(), element.getValue());
//	// }
//	// }
//	// lik += calcGradient.myLik;
//	//
//	//
//	//
//	// double l1 = sum(grad);
//	//
//	// for (Entry<String, Feature> element : grad.entrySet()) {
//	// featureMap.get(element.getKey()).rate(element.getValue(), l1);
//	// }
//	// }
//
//	public Map<String, Feature> myGrad = new HashMap<>();
//	public double myLik;
//
//	/**
//	 * Function to calculate gradient
//	 * 
//	 * @param list
//	 * @param featureMap
//	 */
//	public void calcGradient(List<Element> list, Map<String, Feature> featureMap) {
//		this.clear();
//		int size = list.size() - 2;
//		for (int index = 2; index < size; index++) {
//			updateFeature(list, index);
//		}
//	}
//
//	private void calcBack(double[][] w, int fIndex, int sta) {
//		// b[i,r] = logsumexp([
//		// calc_b(x, i+1, k, w, e, b) + calc_e(x, i, r, k, w, e)
//		// for k in prev_states])
//	}
//
//	// def calc_e(x, i, l, r, w, e_prob):
//	// if (i, l, r) not in e_prob:
//	// e_prob[i,l,r] = dot(calc_feat(x, i, l, r), w)
//	// return e_prob[i,l,r]
//	public void calcE() {
//
//	}
//
//	public double dot(double[] A, double[] B) {
//		double sum = 0;
//		for (int i = 0; i < A.length; i++) {
//			sum += A[i] * B[i];
//		}
//		return sum;
//	}
//
//	private double logsumexp(double[] A) {
//		// find max A
//		double max = Double.MIN_VALUE;
//
//		for (double a : A) {
//			if (a > max) {
//				a = max;
//			}
//		}
//
//		double sum = 0;
//
//		for (double a : A) {
//			sum += Math.exp(a - max);
//		}
//		return Math.log(sum) + max;
//
//	}
//
//	public void clear() {
//		myGrad = new HashMap<>();
//		myLik = 0;
//	}
//
//	@Override
//	public void calculate(List<Element> list) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void compute() {
//		// TODO Auto-generated method stub
//		
//	}
//}
