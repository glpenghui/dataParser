package nc.liat6.data.reader.impl;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import nc.liat6.data.reader.AbstractReader;
import nc.liat6.data.reader.bean.Source;
import nc.liat6.data.util.IOHelper;

/**
 * CSV文件读取
 * 
 * @author 6tail
 * 
 */
public class CsvReader extends AbstractReader implements Closeable{
  /** 默认的文件编码 */
  public static final String DEFAULT_ENCODE = "GBK";
  /** 文件编码 */
  public static String ENCODE = DEFAULT_ENCODE;
  /** 回车符 */
  public static String CR = "\r";
  /** 换行符 */
  public static String LF = "\n";
  /** 列间隔符 */
  public static final String SPACE = ",";
  /** 双引号 */
  public static final String QUOTE = "\"";
  private BufferedReader reader;
  /** 缓存 */
  private StringBuffer buffer = new StringBuffer();
  /** 标识数据内容是否包含在引号之间 */
  private boolean quoted = false;

  public CsvReader(Source source){
    super(source);
  }

  /**
   * close
   * 
   */
  public void close(){
    IOHelper.closeQuietly(reader);
  }

  /**
   * 按间隔符拆分字符串
   * 
   * @param s 字符串
   * @param sp 间隔符
   * @return 拆分后的列表
   */
  private List<String> split(String s,String sp){
    List<String> l = new ArrayList<String>();
    String r = s;
    while(r.contains(sp)){
      int space = r.indexOf(sp);
      l.add(r.substring(0,space));
      r = r.substring(space+sp.length());
    }
    l.add(r);
    return l;
  }

  /**
   * 按照CSV格式规范将拆散的本来是一列的数据合并
   * 
   * @param segs 拆散的列
   * @return 合并后的列
   */
  private List<String> combin(List<String> segs){
    List<String> l = new ArrayList<String>();
    for(String o:segs){
      String t = o.replace(QUOTE+QUOTE,"");
      if(t.startsWith(QUOTE)){
        if(!quoted){
          quoted = true;
          buffer.append(o);
          if(t.endsWith(QUOTE)){
            if(!t.equals(QUOTE)){
              l.add(buffer.toString());
              buffer.delete(0,buffer.length());
              quoted = false;
            }
          }
        }else{
          if(t.equals(QUOTE)){
            buffer.append(SPACE);
            buffer.append(o);
            l.add(buffer.toString());
            buffer.delete(0,buffer.length());
            quoted = false;
          }else{
            l.add(buffer.toString());
            buffer.delete(0,buffer.length());
            buffer.append(o);
            quoted = true;
          }
        }
      }else if(t.endsWith(QUOTE)){
        if(quoted){
          buffer.append(SPACE);
          buffer.append(o);
          l.add(buffer.toString());
          buffer.delete(0,buffer.length());
          quoted = false;
        }else{
          l.add(o);
        }
      }else{
        if(quoted){
          buffer.append(SPACE);
          buffer.append(o);
        }else{
          l.add(o);
        }
      }
    }
    return l;
  }

  protected String readLine(){
    try{
      return reader.readLine();
    }catch(IOException e){
      e.printStackTrace();
      close();
    }
    return null;
  }

  /**
   * 读取下一行
   * 
   * @return 一行数据，如果没有下一行，返回null
   * @throws IOException
   */
  public List<String> nextLine(){
    buffer.delete(0,buffer.length());
    quoted = false;
    String line = readLine();
    if(null==line){
      return null;
    }
    List<String> l = new ArrayList<String>();
    String r = line;
    if(!r.contains(QUOTE)){
      l.addAll(split(r,SPACE));
    }else{
      String t = r.replace(QUOTE+QUOTE,"");
      int count = t.length()-t.replace(QUOTE,"").length();
      while(count%2==1){
        String nextLine = readLine();
        if(null==nextLine){
          nextLine = "\"";
        }
        r = r+CR+LF+nextLine;
        String nt = nextLine.replace(QUOTE+QUOTE,"");
        int ncount = nt.length()-nt.replace(QUOTE,"").length();
        count += ncount;
      }
      List<String> segs = split(r,SPACE);
      l.addAll(combin(segs));
    }
    int n = l.size();
    List<String> cols = new ArrayList<String>();
    for(int i = 0;i<n;i++){
      String col = l.get(i);
      if(col.equals(QUOTE)){
        col = "";
      }else if(col.equals(QUOTE+QUOTE)){
        col = "";
      }else if(col.startsWith(QUOTE)&&col.endsWith(QUOTE)){
        col = col.replace(QUOTE+QUOTE,QUOTE);
        col = col.substring(QUOTE.length());
        col = col.substring(0,col.length()-QUOTE.length());
      }
      cols.add(col);
    }
    return cols;
  }

  public void load() throws IOException{
    switch(source.getSourceType()){
      case file:
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(source.getFile()),ENCODE));
        break;
      case inputStream:
        reader = new BufferedReader(new InputStreamReader(source.getInputStream(),ENCODE));
        break;
    }
    stop = false;
    quoted = false;
    buffer.delete(0,buffer.length());
  }
}