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
 * 多行Block解析示例
 * @author 6tail
 *
 */
public class MultiLineBlockSample{
  public static void main(String[] args) throws IOException{
    File file = new File("files\\多行Block.xls");
    //自定义解析规则
    IParserRule rule = new ParserRuleFixedBlockHeight(){
      public int getBodyStartRow(){
        return 2;
      }
      public int getBodyBlockHeight(){
        return 4;
      }
      public Map<String,String> getBodyItemNames(){
        Map<String,String> names = new HashMap<String,String>();
        names.put("0,1","姓名");
        names.put("0,3","性别");
        names.put("1,0","民族");
        names.put("2,0","爱好");
        return names;
      }
    };

    IParser parser = ParserFactory.getInstance().getParser(file,rule);

    Block block = null;
    while(null!=(block = parser.nextBlock())){
      System.out.println("姓名："+block.getItemByName("姓名").getContent());
      System.out.println("性别："+block.getItemByName("性别").getContent());
      System.out.println(block.getItemByName("民族").getContent());
      System.out.println(block.getItemByName("爱好").getContent());
      System.out.println("_____________________________________");
    }
  }
}