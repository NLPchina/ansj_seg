package org.ansj.util.filter;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

import javax.management.RuntimeErrorException;

import org.ansj.util.IOUtil;
import org.ansj.util.MyStaticValue;

public class StopWord {
	private static HashSet<String> hs = null ;
	public static HashSet<String> getFilterSet(){
		if(hs!=null){
			return hs ;
		}
		return init() ;
	}
	private static HashSet<String> init() {
		// TODO Auto-generated method stub
		hs =  new HashSet<String>()  ;
		try {
			BufferedReader reader = IOUtil.getReader(MyStaticValue.rb.getString("stop"), "UTF-8") ;
			String temp = null ;
			while((temp=reader.readLine())!=null){
				hs.add(temp) ;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e) ;
		}
		return hs ;
	}
}
