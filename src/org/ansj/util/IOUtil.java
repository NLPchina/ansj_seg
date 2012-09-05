package org.ansj.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;


public class IOUtil {
	private static InputStream is = null ;
	private static FileOutputStream fos = null ;
	public static InputStream getInputStream(String path){
		try {
			return new FileInputStream(path) ;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}
	public static BufferedReader getReader(String path , String charEncoding) throws UnsupportedEncodingException{
		is = getInputStream(path) ;
		return new BufferedReader(new InputStreamReader(is,charEncoding)) ;
	}
	
	public static RandomAccessFile getRandomAccessFile(String path , String charEncoding) throws FileNotFoundException{
		is = getInputStream(path) ;
		if(is!=null){
			return new RandomAccessFile(new File(path),"r") ;
		}
		return null ;
	}
	public static void Writer(String path , String charEncoding , String content){
		try {
			fos = new FileOutputStream(new File(path)) ;
			fos.write(content.getBytes()) ;
			fos.close() ;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close(){
		if(is!=null){
			try {
				is.close() ;
			} catch (IOException e) {
				is = null ;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			is = null ;
		}
	}
}
