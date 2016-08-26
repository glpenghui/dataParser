package nc.liat6.data.parser.rule;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象解析规则
 * 
 * @author 6tail
 *
 */
public abstract class AbstractParserRule implements IParserRule{
  /**
   * 根据列标计算横坐标，A对应0,AA对应26
   * 
   * @param label 列标，A、B、C、AB之类的
   * @return 横坐标
   */
  protected int getPos(String label){
    char[] letters = label.toUpperCase().toCharArray();
    int n = 0;
    int size = letters.length;
    for(int i = 0;i<size;i++){
      int p = letters[size-i-1];
      p -= 64;
      if(0==i){
        p -= 1;
      }
      n += p*Math.pow(26,i);
    }
    return n;
  }

  /**
   * 根据横坐标计算列标，暂不支持超过2位列标的
   * 
   * @param pos 横坐标，0对应A
   * @return 列标
   */
  protected String getLabel(int pos){
    StringBuilder s = new StringBuilder();
    int a = pos/26;
    if(a>0){
      s.append((char)(a+65-1));
    }
    s.append((char)(pos%26+65));
    return s.toString();
  }

  public int getBodyStartRow(){
    return 0;
  }

  public int getBodyEndRow(){
    return -1;
  }

  public int getBodyStartCol(){
    return 0;
  }

  public int getBodyEndCol(){
    return -1;
  }

  public Map<String,IParserRule> getFragmentRule(){
    return new HashMap<String,IParserRule>();
  }

  public Map<String,String> getHeadItemNames(){
    return new HashMap<String,String>();
  }
}