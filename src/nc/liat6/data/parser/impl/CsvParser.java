package nc.liat6.data.parser.impl;

import nc.liat6.data.parser.AbstractParser;
import nc.liat6.data.reader.bean.Source;
import nc.liat6.data.reader.impl.CsvReader;

/**
 * csv格式的解析器
 * 
 * @author 6tail
 *
 */
public class CsvParser extends AbstractParser{
  /** 支持的文件后缀 */
  public static final String SUPPORT_FILE_SUFFIX = ".csv";

  public CsvParser(Source source){
    super(new CsvReader(source),SUPPORT_FILE_SUFFIX);
    name = "CSV Parser";
  }
}