package sample;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import nc.liat6.data.parser.IParser;
import nc.liat6.data.parser.ParserFactory;
import nc.liat6.data.parser.bean.Block;
import nc.liat6.data.parser.rule.IParserRule;
import nc.liat6.data.parser.rule.ParserRuleSingleLine;
import nc.liat6.data.parser.rule.impl.ParserRuleColIndex;
import nc.liat6.data.parser.rule.impl.ParserRuleLetter;

/**
 * 最简单的文件解析示例
 * @author 6tail
 *
 */
public class MostSimpleSample{
  
  public static void useLabel() throws IOException{
    //xls格式
    File file = new File("files\\最简单的.xls");

    //使用列对应字母，数据从第1行开始（行从0开始计）
    IParserRule rule = new ParserRuleLetter(1,"A","E");

    IParser parser = ParserFactory.getInstance().getParser(file,rule);

    Block block = null;
    while(null!=(block = parser.nextBlock())){
      System.out.print(block.getItemContent("A"));
      System.out.print("\t");
      System.out.print(block.getItemContent("B"));
      System.out.print("\t");
      System.out.print(block.getItemContent("C"));
      System.out.print("\t");
      System.out.print(block.getItemContent("D"));
      System.out.print("\t");
      System.out.println(block.getItemContent("E"));
      System.out.println("_____________________________________");
    }
  }
  
  /**
   * 前面有空行的
   * @throws IOException
   */
  public static void startsWithBlankLines() throws IOException{
    //xls格式
    File file = new File("files\\前面有空行的.xls");

    //使用列对应字母，数据从第1行开始（行从0开始计）
    IParserRule rule = new ParserRuleLetter(1,"A","E");

    IParser parser = ParserFactory.getInstance().getParser(file,rule);

    Block block = null;
    while(null!=(block = parser.nextBlock())){
      System.out.print(block.getItemByName("A").getContent());
      System.out.print("\t");
      System.out.print(block.getItemByName("B").getContent());
      System.out.print("\t");
      System.out.print(block.getItemByName("C").getContent());
      System.out.print("\t");
      System.out.print(block.getItemByName("D").getContent());
      System.out.print("\t");
      System.out.println(block.getItemByName("E").getContent());
      System.out.println("_____________________________________");
    }
  }
  
  public static void useIndex() throws IOException{
    //xlsx格式
    File file = new File("files\\最简单的.xlsx");
    //使用列下标，数据从第1行开始（行从0开始计）
    IParserRule rule = new ParserRuleColIndex(1,0,4);

    IParser parser = ParserFactory.getInstance().getParser(file,rule);

    Block block = null;
    while(null!=(block = parser.nextBlock())){
      System.out.print(block.getItemByGrid(0,0).getContent());
      System.out.print("\t");
      System.out.print(block.getItemByGrid(0,1).getContent());
      System.out.print("\t");
      System.out.print(block.getItemByGrid(0,2).getContent());
      System.out.print("\t");
      System.out.print(block.getItemByGrid(0,3).getContent());
      System.out.print("\t");
      System.out.println(block.getItemByGrid(0,4).getContent());
      System.out.println("_____________________________________");
    }
  }
  
  public static void useName() throws IOException{
    //csv格式
    File file = new File("files\\最简单的.csv");
    //自定义解析规则
    IParserRule rule = new ParserRuleSingleLine(1){
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
      System.out.print(block.getItemByName("序号").getContent());
      System.out.print("\t");
      System.out.print(block.getItemByName("name").getContent());
      System.out.print("\t");
      System.out.print(block.getItemByName("性别").getContent());
      System.out.print("\t");
      System.out.print(block.getItemByName("age").getContent());
      System.out.print("\t");
      System.out.println(block.getItemByName("民族").getContent());
      System.out.println("_____________________________________");
    }
  }
  
  public static void main(String[] args) throws IOException{
    useLabel();
    useIndex();
    useName();
    startsWithBlankLines();
  }
}