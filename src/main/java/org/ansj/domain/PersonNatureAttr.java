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

	private float B = 0.000001F;//姓氏
	private float C = 0.000001F;//双名的首字
	private float D = 0.000001F;//双名的末字
	private float E = 0.000001F;//单名

	private float K = 0.0001F;//上文
	private float L = 0.0001F;//下文

	private float M = 0.0001F;//下文

	private float Y = 0.000001F;//姓与单名成词

	private float X = 0.000001F;//姓与双名的首字成词
	private float Z = 0.000001F;//双名本身成词

	private Float A = null;//双名本身成词

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
		if (A == null) {
			A = 1 - B - C - D - E - K - L - M  - X - Y - Z;
		}
		return A;
	}

	public void set(char c, float value) {
		switch (c) {
			case 'B':
				active = true;
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
				active = true;
				X = value;
				break;
			case 'Y':
				Y = value;
				break;
			case 'Z':
				Z = value;
				break;
			default:
				throw new LibraryException("person name status err " + c);
		}
	}
}
