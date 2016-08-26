package nc.liat6.data.parser.rule.impl;

import nc.liat6.data.parser.rule.ParserRuleSingleLine;

/**
 * 以列下标命名格子的默认解析规则，适用于简单的单行Block解析，又不想自定义规则的
 * 
 * @author 6tail
 *
 */
public class ParserRuleColIndex extends ParserRuleSingleLine{
  
  /**
   * 指定数据开始行的构造方法
   * 
   * @param startRow 数据开始行，从0开始计
   * @param startCol 数据开始列，从0开始计
   * @param endCol 数据结束列，包含
   */
  public ParserRuleColIndex(int startRow,int startCol,int endCol){
    super(startRow);
    for(int i = startCol;i<=endCol;i++){
      bodyItemNames.put("0,"+i,i+"");
    }
  }

  /**
   * 默认构造方法，数据从第0行，0列开始，26列(含)结束
   */
  public ParserRuleColIndex(){
    this(0,0,26);
  }
}