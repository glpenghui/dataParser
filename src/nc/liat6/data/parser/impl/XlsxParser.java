package nc.liat6.data.parser.impl;

import nc.liat6.data.parser.AbstractParser;
import nc.liat6.data.reader.bean.Source;
import nc.liat6.data.reader.impl.XlsxReader;

/**
 * xlsx格式的解析器
 * 
 * @author 6tail
 *
 */
public class XlsxParser extends AbstractParser{
  /** 支持的文件后缀 */
  public static final String SUPPORT_FILE_SUFFIX = ".xlsx";

  public XlsxParser(Source source){
    super(new XlsxReader(source),SUPPORT_FILE_SUFFIX);
    name = "xlsx";
  }
}