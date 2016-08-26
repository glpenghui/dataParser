package nc.liat6.data.parser.exception;

import java.io.IOException;

/**
 * 解析异常
 * @author 6tail
 *
 */
public class ParserException extends IOException{
  private static final long serialVersionUID = 6895456093265513725L;
  private static final String MESSAGE = "解析失败";

  public ParserException(){
    super(MESSAGE);
  }

  public ParserException(String message){
    super(message);
  }

  public ParserException(Throwable cause){
    this(MESSAGE,cause);
  }

  public ParserException(String message,Throwable cause){
    super(message);
    super.initCause(cause);
  }
}
