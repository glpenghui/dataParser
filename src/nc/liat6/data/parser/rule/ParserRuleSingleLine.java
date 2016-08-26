package nc.liat6.data.parser.rule;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 适用于简单的单行Block解析
 * 
 * @author 6tail
 *
 */
public abstract class ParserRuleSingleLine extends ParserRuleFixedBlockHeight{
  /** 数据开始行，从0开始计 */
  protected int bodyStartRow;
  /** 格子字段映射 */
  protected Map<String,String> bodyItemNames = new LinkedHashMap<String,String>();

  public Map<String,String> getBodyItemNames(){
    return bodyItemNames;
  }

  protected ParserRuleSingleLine(int bodyStartRow){
    this.bodyStartRow = bodyStartRow;
  }

  public int getBodyStartRow(){
    return bodyStartRow;
  }
}