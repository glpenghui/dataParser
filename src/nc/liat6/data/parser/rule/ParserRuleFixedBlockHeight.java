package nc.liat6.data.parser.rule;

import java.util.HashMap;
import java.util.Map;

/**
 * 块高度固定的抽象解析规则
 * 
 * @author 6tail
 *
 */
public abstract class ParserRuleFixedBlockHeight extends AbstractParserRule{
  public RuleType getType(){
    return RuleType.fiexd_block_height;
  }

  public int getBodyBlockHeight(){
    return 1;
  }

  public Map<Integer,String> getEndRowValues(){
    return new HashMap<Integer,String>();
  }
}