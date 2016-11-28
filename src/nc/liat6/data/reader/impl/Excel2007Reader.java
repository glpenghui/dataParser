package nc.liat6.data.reader.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import nc.liat6.data.reader.bean.Source;
import nc.liat6.data.util.IOHelper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * xlsx文件读取，解决poi读取大文件引起内存疯涨溢出的问题，并且速度比poi快
 * 
 * @author 6tail
 *
 */
public class Excel2007Reader extends DefaultHandler implements Runnable{
  /** 每次读取的行数 */
  public static int queueSize = 5000;
  private boolean end;
  private boolean stop;
  private boolean nextIsString;
  private boolean isTElement;
  //上一次的内容
  private String lastContents;
  //日期格式
  private String dateFormat;
  private String prefPos = null;
  private String currentPos = null;
  private Source source;
  private StylesTable stylesTable;
  //共享字符串表
  private SharedStringsTable sst;
  private List<String> rowData = new ArrayList<String>();
  private Queue<List<String>> rowQueue = new LinkedBlockingQueue<List<String>>(queueSize);
  private Thread t;

  public Excel2007Reader(Source source){
    this.source = source;
  }

  public void run(){
    InputStream sheet = null;
    try{
      OPCPackage pkg = null;
      switch(source.getSourceType()){
        case file:
          pkg = OPCPackage.open(source.getFile());
          break;
        case inputStream:
          pkg = OPCPackage.open(source.getInputStream());
          break;
      }
      XSSFReader r = new XSSFReader(pkg);
      stylesTable = r.getStylesTable();
      sst = r.getSharedStringsTable();
      sheet = r.getSheetsData().next();
      XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
      parser.setContentHandler(this);
      parser.parse(new InputSource(sheet));
    }catch(Exception e){}finally{
      IOHelper.closeQuietly(sheet);
      end = true;
    }
  }

  /**
   * 根据列标计算横坐标，A对应0,AA对应26
   * 
   * @param label 列标，A、B、C、AB之类的
   * @return 横坐标
   */
  private int getPos(String label){
    char[] letters = label.toUpperCase().toCharArray();
    int n = 0;
    int size = letters.length;
    for(int i = 0;i<size;i++){
      int p = letters[size-i-1];
      p -= 64;
      if(0==i){
        p -= 1;
      }
      n += p*Math.pow(26,i);
    }
    return n;
  }

  private int diffPos(){
    return getPos(currentPos.toUpperCase())-getPos(prefPos.toUpperCase())-1;
  }

  public void startElement(String uri,String localName,String name,Attributes attributes) throws SAXException{
    while(rowQueue.size()>=queueSize){
      try{
        Thread.sleep(2);
      }catch(InterruptedException e){}
      if(stop){
        throw new RuntimeException("中止读取");
      }
    }
    if("row".equals(name)){
      prefPos = "@";
    }else if("c".equals(name)){
      dateFormat = null;
      String cellType = attributes.getValue("t");
      String cellStyle = attributes.getValue("s");
      String pos = attributes.getValue("r");
      currentPos = pos.replaceAll("\\d+","");
      if("s".equals(cellType)){
        nextIsString = true;
      }else{
        nextIsString = false;
      }
      if(null!=cellStyle){
        int cs = Integer.parseInt(cellStyle);
        XSSFCellStyle style = stylesTable.getStyleAt(cs);
        String format = style.getDataFormatString();
        if("m/d/yy".equals(format)){
          dateFormat = "yyyy-MM-dd";
        }
      }
    }
    //当元素为t时
    if("t".equals(name)){
      isTElement = true;
    }else{
      isTElement = false;
    }
    // 置空  
    lastContents = "";
  }

  public void endElement(String uri,String localName,String name) throws SAXException{
    if(stop){
      throw new RuntimeException("中止读取");
    }
    if(nextIsString){
      try{
        int idx = Integer.parseInt(lastContents);
        lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
      }catch(Exception e){}
    }
    if(isTElement){
      for(int i = 0,j = diffPos();i<j;i++){
        rowData.add("");
      }
      prefPos = currentPos;
      String value = lastContents.trim();
      rowData.add(value);
      isTElement = false;
    }else if("v".equals(name)){
      for(int i = 0,j = diffPos();i<j;i++){
        rowData.add("");
      }
      prefPos = currentPos;
      String value = lastContents.trim();
      if(value.length()>0){
        //日期格式处理
        if(null!=dateFormat){
          Date date = HSSFDateUtil.getJavaDate(Double.valueOf(value));
          value = new SimpleDateFormat(dateFormat).format(date);
        }
      }
      rowData.add(value);
    }else if(name.equals("row")){
      List<String> row = new ArrayList<String>(rowData.size());
      for(String s:rowData){
        row.add(s);
      }
      rowQueue.offer(row);
      rowData.clear();
    }
  }

  public void characters(char[] ch,int start,int length) throws SAXException{
    lastContents += new String(ch,start,length);
  }

  public void load(){
    end = false;
    stop = false;
    nextIsString = false;
    isTElement = false;
    lastContents = null;
    dateFormat = null;
    prefPos = null;
    currentPos = null;
    rowData.clear();
    rowQueue.clear();
    t = new Thread(this);
    t.start();
  }

  public List<String> nextLine(){
    if(stop) return null;
    List<String> row = rowQueue.poll();
    while(null==row){
      if(stop||end){
        break;
      }
      try{
        Thread.sleep(2);
      }catch(InterruptedException e){}
      row = rowQueue.poll();
    }
    return row;
  }

  public void stop(){
    stop = true;
  }
}