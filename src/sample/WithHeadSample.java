package sample;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import nc.liat6.data.parser.IParser;
import nc.liat6.data.parser.ParserFactory;
import nc.liat6.data.parser.bean.Block;
import nc.liat6.data.parser.rule.IParserRule;
import nc.liat6.data.parser.rule.ParserRuleFixedBlockHeight;

/**
 * 带表头的解析示例
 * @author 6tail
 *
 */
public class WithHeadSample{
  public static void main(String[] args) throws IOException{
    File file = new File("files\\带表头的.xls");
    //自定义解析规则
    IParserRule rule = new ParserRuleFixedBlockHeight(){
      public Map<String,String> getHeadItemNames(){
        Map<String,String> names = new HashMap<String,String>();
        names.put("0,0","表名");
        names.put("1,1","填报人");
        return names;
      }
      public int getBodyStartRow(){
        return 4;
      }
      public Map<String,String> getBodyItemNames(){
        Map<String,String> names = new HashMap<String,String>();
        names.put("0,0","序号");
        names.put("0,1","name");
        names.put("0,2","性别");
        names.put("0,3","age");
        names.put("0,4","民族");
        return names;
      }
    };

    IParser parser = ParserFactory.getInstance().getParser(file,rule);

    Block block = null;
    while(null!=(block = parser.nextBlock())){
      switch(block.getType()){
        case head:
          System.out.println("表名："+block.getItemByName("表名").getContent());
          System.out.println("填报人："+block.getItemByName("填报人").getContent());
          break;
        case body:
          System.out.print(block.getItemByName("序号").getContent());
          System.out.print("\t");
          System.out.print(block.getItemByName("name").getContent());
          System.out.print("\t");
          System.out.print(block.getItemByName("性别").getContent());
          System.out.print("\t");
          System.out.print(block.getItemByName("age").getContent());
          System.out.print("\t");
          System.out.println(block.getItemByName("民族").getContent());
          break;
         default:
      }
      System.out.println("_____________________________________");
    }
  }
}