package org.ansj.demo;

import java.io.IOException;
import java.io.StreamTokenizer;
import love.cq.util.IOUtil;

public class LibraryDemo {
    public static void main(String[] args) throws IOException {
        
        StreamTokenizer st = new StreamTokenizer(IOUtil.getReader("/home/ansj/workspace/ansj_seg/License.txt",IOUtil.UTF8)) ;
        while(st.nextToken()!=-1){
            System.out.println(st.sval);
        }
    }
}
