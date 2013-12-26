package org.ansj.app.crf.pojo;

public class TempFeature {
    public String name ;
    public int index ;
    public int sta ;
    
    public TempFeature(String name, int index, int sta) {
        super();
        this.name = name;
        this.index = index;
        this.sta = sta;
    }
    
    
    public TempFeature(char[] chars, int index, int sta) {
        super();
        this.name = new String(chars);
        this.index = index;
        this.sta = sta;
    }
    
    
}
