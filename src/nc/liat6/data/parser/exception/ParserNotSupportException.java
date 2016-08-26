package nc.liat6.data.parser.exception;

/**
 * 不支持该数据源的解析
 * 
 * @author 6tail
 *
 */
public class ParserNotSupportException extends ParserException{
  private static final long serialVersionUID = 5400137399075687873L;
  private static final String MESSAGE = "不支持该数据源的解析";

  public ParserNotSupportException(){
    super(MESSAGE);
  }

  public ParserNotSupportException(String message){
    super(message);
  }

  public ParserNotSupportException(Throwable cause){
    this(MESSAGE,cause);
  }

  public ParserNotSupportException(String message,Throwable cause){
    super(message,cause);
  }
}