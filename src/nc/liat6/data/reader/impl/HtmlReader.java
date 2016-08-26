package nc.liat6.data.reader.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nc.liat6.data.reader.AbstractReader;
import nc.liat6.data.reader.bean.Source;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * HTML直接改后缀为xls或xlsx的文件读取
 * 
 * @author 6tail
 *
 */
public class HtmlReader extends AbstractReader{
  public static final int DOC_TYPE_UNKNOWN = 0;
  public static final int DOC_TYPE_HTML = 1;
  public static final int DOC_TYPE_XML = 2;
  public static final String DEFAULT_ENCODE = "GBK";
  public static String ENCODE = DEFAULT_ENCODE;
  protected Document doc;
  protected Elements trs;
  protected int rowCount;
  protected int rowReaded;
  protected int docType;

  public HtmlReader(Source source){
    super(source);
  }

  public void load() throws IOException{
    switch(source.getSourceType()){
      case file:
        doc = Jsoup.parse(source.getFile(),ENCODE);
        break;
      case inputStream:
        doc = Jsoup.parse(source.getInputStream(),ENCODE,null);
        break;
    }
    stop = false;
    rowReaded = 0;
    docType = DOC_TYPE_UNKNOWN;
    trs = doc.getElementsByTag("tr");
    if(trs.size()>0){
      docType = DOC_TYPE_HTML;
    }else{
      trs = doc.getElementsByTag("row");
      if(trs.size()>0){
        docType = DOC_TYPE_XML;
      }
    }
    rowCount = trs.size();
  }

  public List<String> nextLine(){
    if(rowReaded>=rowCount) return null;
    Element tr = trs.get(rowReaded);
    rowReaded++;
    List<String> line = new ArrayList<String>();
    Elements tds = tr.children();
    for(int i = 0,j = tds.size();i<j;i++){
      Element n = tds.get(i);
      line.add(n.text().trim());
    }
    return line;
  }
}