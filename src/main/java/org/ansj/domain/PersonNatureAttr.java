package org.ansj.domain;

/**
 * 人名标注pojo类
 * 
 * @author ansj
 * 
 */
public class PersonNatureAttr {

	public static final PersonNatureAttr NULL = new PersonNatureAttr();

	private int B = -1;//姓氏
	private int C = -1;//双名的首字
	private int D = -1;//双名的末字
	private int E = -1;//单名

	private int F = -1;//前缀
	private int G = -1;//后缀

	private int K = -1;//上文
	private int L = -1;//下文

	private int U = -1;//人名的上文和姓成词
	private int V = -1;//人名的末字和下文成词

	private int X = -1 ;//姓与双名的首字成词
	private int Z = -1;//双名本身成词

	private boolean active ;

	public boolean isActive() {
		return active;
	}

	public int getB() {
		active = true ;
		return B;
	}

	public int getC() {
		return C;
	}

	public int getD() {
		return D;
	}

	public int getE() {
		return E;
	}

	public int getF() {
		return F;
	}

	public int getG() {
		return G;
	}

	public int getK() {
		return K;
	}

	public int getL() {
		return L;
	}

	public int getU() {
		active = true ;
		return U;
	}

	public int getV() {
		return V;
	}

	public int getX() {
		active = true ;
		return X;
	}

	public int getZ() {
		return Z;
	}

	public void set(char c, int value){
		switch (c){
			case 'B':
				B = value;
				break;
			case 'C':
				C = value;
				break;
			case 'D':
				D = value;
				break;
			case 'E':
				E = value;
				break;
			case 'F':
				F = value;
				break;
			case 'G':
				G = value;
				break;
			case 'K':
				K = value;
				break;
			case 'L':
				L = value;
				break;
			case 'U':
				U = value;
				break;
			case 'V':
				V = value;
				break;
			case 'Z':
				Z = value;
				break;
		}
	}
}
