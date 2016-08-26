package nc.liat6.data.reader.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nc.liat6.data.reader.AbstractReader;
import nc.liat6.data.reader.bean.Source;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * xls文件读取
 * 
 * @author 6tail
 *
 */
public class XlsReader extends AbstractReader{
  /** xls工作簿 */
  private HSSFWorkbook wbs = null;
  /** xls Sheet */
  private HSSFSheet sheet = null;
  /** 当前行，从0开始计 */
  private int rowCount = 0;
  /** 它认为的最后一行，从0开始计，如果为-1则不以它为准 */
  private int lastRow = -1;

  public XlsReader(Source source){
    super(source);
  }

  public void load() throws IOException{
    switch(source.getSourceType()){
      case file:
        wbs = new HSSFWorkbook(new FileInputStream(source.getFile()));
        break;
      case inputStream:
        wbs = new HSSFWorkbook(source.getInputStream());
        break;
    }
    sheet = wbs.getSheetAt(0);
    lastRow = sheet.getLastRowNum();
    stop = false;
    rowCount = 0;
  }

  private List<String> getLine(int index){
    if(lastRow>-1){
      if(index>lastRow){
        return null;
      }
    }
    HSSFRow row = sheet.getRow(index);
    if(null==row){
      if(-1==lastRow){
        return null;
      }else{
        return new ArrayList<String>();
      }
    }
    int l = row.getLastCellNum();
    List<String> rs = new ArrayList<String>();
    for(int i = 0;i<l;i++){
      String v = "";
      HSSFCell cell = row.getCell(i);
      if(null!=cell){
        switch(cell.getCellType()){
          case HSSFCell.CELL_TYPE_NUMERIC:
            double value = cell.getNumericCellValue();
            try{
              short format = cell.getCellStyle().getDataFormat();
              SimpleDateFormat sdf = null;
              if(176==format||format==14||format==31||format==57||format==58){
                sdf = new SimpleDateFormat("yyyy-MM-dd");
              }else if(format==20||format==32){
                sdf = new SimpleDateFormat("HH:mm");
              }
              Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
              v = sdf.format(date);
              int len = v.length();
              if(10!=len&&5!=len){
                throw new IllegalArgumentException();
              }
            }catch(Exception e){
              try{
                BigDecimal d = new BigDecimal(value+"");
                v = d.longValue()+"";
              }catch(Exception ex){
                v = value+"";
              }
            }
            break;
          case HSSFCell.CELL_TYPE_STRING:
            v = (cell.getStringCellValue()+"").trim();
            break;
          case HSSFCell.CELL_TYPE_BOOLEAN:
            v = cell.getBooleanCellValue()+"";
            break;
          case HSSFCell.CELL_TYPE_FORMULA:
            try{
              v = String.valueOf(cell.getNumericCellValue());
            }catch(IllegalStateException e){
              v = String.valueOf(cell.getRichStringCellValue());
            }
            if(v.endsWith(".0")){
              try{
                Double.parseDouble(v);
                v = v.substring(0,v.lastIndexOf("."));
              }catch(Exception e){}
            }
            break;
          case HSSFCell.CELL_TYPE_BLANK:
            v = "";
            break;
          case HSSFCell.CELL_TYPE_ERROR:
            v = "";
            break;
          default:
            v = "";
            break;
        }
      }
      rs.add(v);
    }
    return rs;
  }

  public List<String> nextLine(){
    if(stop) return null;
    return getLine(rowCount++);
  }
}