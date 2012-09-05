package org.ansj.splitWord;

import java.io.IOException;

import org.ansj.domain.Term;

public interface Analysis {
	public Term next() throws IOException;
}
