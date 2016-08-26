package nc.liat6.data.parser.rule;

import java.util.HashMap;
import java.util.Map;


/**
 * 根据标识自动检测块结束行的抽象解析规则
 * 
 * @author 6tail
 *
 */
public abstract class ParserRuleCheckBlockEnd extends AbstractParserRule{
  public RuleType getType(){
    return RuleType.check_block_end;
  }
  
  public Map<String,String> getBodyItemNames(){
    return new HashMap<String,String>();
  }
  
  public int getBodyBlockHeight(){
    return -1;
  }
}