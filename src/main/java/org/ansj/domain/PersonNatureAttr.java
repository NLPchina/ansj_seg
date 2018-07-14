package org.ansj.domain;

import org.ansj.exception.LibraryException;

import java.io.Serializable;

/**
 * 人名标注pojo类
 *
 * @author ansj
 */
public class PersonNatureAttr implements Serializable {

	public static final PersonNatureAttr NULL = new PersonNatureAttr();

	private float B = 0;//姓氏
	private float C = 0;//双名的首字
	private float D = 0;//双名的末字
	private float E = 0;//单名

	private float K = 0;//上文
	private float L = 0;//下文

	private float M = 0;//下文

	private float Y = 0;//姓与单名成词

	private float U = 0 ;//人名的上文和姓成词
	private float V = 0 ;//名的末字和下文成词

	private float X = 0;//姓与双名的首字成词
	private float Z = 0;//双名本身成词

	private float A = 0;//双名本身成词

	private boolean active;

	public boolean isActive() {
		return active;
	}

	public float getB() {
		return B;
	}

	public float getC() {
		return C;
	}

	public float getD() {
		return D;
	}

	public float getE() {
		return E;
	}

	public float getK() {
		return K;
	}

	public float getL() {
		return L;
	}

	public float getX() {
		return X;
	}

	public float getY() {
		return Y;
	}

	public float getZ() {
		return Z;
	}

	public float getM() {
		return M;
	}

	public float getA() {
		return A;
	}

	public float getU() {
		return U;
	}

	public float getV() {
		return V;
	}

	public void set(char c, float value) {
		switch (c) {
			case 'B':
				if(value > 0 ) {
					active = true;
				}
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
			case 'K':
				K = value;
				break;
			case 'L':
				L = value;
				break;
			case 'M':
				M = value;
				break;
			case 'X':
				if(value > 0 ) {
					active = true;
				}
				X = value;
				break;
			case 'Y':
				Y = value;
				break;
			case 'Z':
				Z = value;
				break;
			case 'A':
				A = value;
				break;
			case 'U':
				U = value;
				break;
			case 'V':
				U = value;
				break;
			default:
				throw new LibraryException("person name status err " + c);
		}
	}
}
