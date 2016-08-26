package nc.liat6.data.parser.impl;

import nc.liat6.data.parser.AbstractParser;
import nc.liat6.data.reader.bean.Source;
import nc.liat6.data.reader.impl.HtmlReader;

/**
 * html或xml表格，以及改后缀为xls或xlsx的解析器
 * 
 * @author 6tail
 *
 */
public class HtmlParser extends AbstractParser{
  /** 支持的文件后缀 */
  public static final String SUPPORT_FILE_SUFFIX = ".xls,.xlsx,.html,.xml";

  public HtmlParser(Source source){
    super(new HtmlReader(source),SUPPORT_FILE_SUFFIX);
    name = "Html Parser";
  }
}