package nc.liat6.data.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import nc.liat6.data.parser.exception.ParserNotSupportException;
import nc.liat6.data.parser.impl.CsvParser;
import nc.liat6.data.parser.impl.HtmlParser;
import nc.liat6.data.parser.impl.TextParser;
import nc.liat6.data.parser.impl.XlsParser;
import nc.liat6.data.parser.impl.XlsxParser;
import nc.liat6.data.parser.rule.IParserRule;
import nc.liat6.data.reader.bean.Source;

/**
 * 解析器工厂
 * 
 * @author 6tail
 *
 */
public class ParserFactory{
  /** 解析器工厂实例 */
  private static ParserFactory instance;

  private ParserFactory(){}

  /**
   * 获取解析器工厂实例
   * 
   * @return 解析器工厂实例
   */
  public static synchronized ParserFactory getInstance(){
    if(null==instance){
      instance = new ParserFactory();
    }
    return instance;
  }
  
  /**
   * 获取解析器
   * @param inputStream 输入流
   * @param parserRule 解析规则
   * @return 解析器接口
   * @throws IOException IO异常
   */
  public IParser getParser(InputStream inputStream,IParserRule parserRule) throws IOException{
    return getParser(new Source(inputStream),parserRule);
  }

  /**
   * 获取解析器
   * 
   * @param file 待解析的文件
   * @param parserRule 解析规则
   * @return 解析器接口
   * @throws ParserNotSupportException 文件不支持解析的异常
   */
  public IParser getParser(File file,IParserRule parserRule) throws ParserNotSupportException{
    return getParser(new Source(file),parserRule);
  }
  
  /**
   * 获取解析器
   * 
   * @param source 待解析的数据源
   * @param parserRule 解析规则
   * @return 解析器接口
   * @throws ParserNotSupportException 文件不支持解析的异常
   */
  protected IParser getParser(Source source,IParserRule parserRule) throws ParserNotSupportException{
    List<AbstractParser> ps = new ArrayList<AbstractParser>();
    List<AbstractParser> parsers = new ArrayList<AbstractParser>();
    ps.add(new XlsParser(source));
    ps.add(new XlsxParser(source));
    ps.add(new CsvParser(source));
    ps.add(new HtmlParser(source));
    ps.add(new TextParser(source));
    for(String name:parserRule.orderBy()){
      for(AbstractParser parser:ps){
        if(parser.getName().equalsIgnoreCase(name)){
          parsers.add(parser);
          break;
        }
      }
    }
    for(AbstractParser parser:parsers){
      parser.setRule(parserRule);
      int startRow = parser.getStartRow();
      if(startRow>-1){
        System.out.println("[√] "+parser.getName()+" >> "+source);
        parser.setStartRow(startRow);
        return parser;
      }else{
        System.out.println("[×] "+parser.getName()+" >> "+source);
      }
    }
    throw new ParserNotSupportException(source+"");
  }
}