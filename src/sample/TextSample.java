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

/**
 * txt文件解析示例
 * @author 6tail
 *
 */
public class TextSample{
  public static void useName() throws IOException{
    File file = new File("files\\flts.txt");
    IParserRule rule = new ParserRuleSingleLine(0){
      public Map<String,String> getBodyItemNames(){
        Map<String,String> names = new HashMap<String,String>();
        names.put("0,8","航班起飞日期");
        names.put("8,8","航班起飞时间");
        names.put("16,2","销售航空公司");
        names.put("18,4","销售航班号");
        names.put("23,1","国内国际");
        names.put("24,30","航线");
        names.put("54,3","起飞机场");
        names.put("57,3","到达机场");
        names.put("60,10","机型");
        names.put("70,3","主舱位布局数");
        names.put("73,1","主舱位");
        names.put("74,1","子舱位");
        names.put("75,3","子舱位旅客量");
        names.put("78,3","团队旅客量");
        return names;
      }
    };

    IParser parser = ParserFactory.getInstance().getParser(file,rule);

    Block block = null;
    while(null!=(block = parser.nextBlock())){
      System.out.print(block.getItemContent("航班起飞日期"));
      System.out.print("\t");
      System.out.print(block.getItemContent("航班起飞时间"));
      System.out.print("\t");
      System.out.print(block.getItemContent("销售航空公司"));
      System.out.print("\t");
      System.out.print(block.getItemContent("销售航班号"));
      System.out.print("\t");
      System.out.print(block.getItemContent("国内国际"));
      System.out.print("\t");
      System.out.print(block.getItemContent("航线"));
      System.out.print("\t");
      System.out.print(block.getItemContent("起飞机场"));
      System.out.print("\t");
      System.out.print(block.getItemContent("到达机场"));
      System.out.print("\t");
      System.out.print(block.getItemContent("机型"));
      System.out.print("\t");
      System.out.print(block.getItemContent("主舱位布局数"));
      System.out.print("\t");
      System.out.print(block.getItemContent("主舱位"));
      System.out.print("\t");
      System.out.print(block.getItemContent("子舱位"));
      System.out.print("\t");
      System.out.print(block.getItemContent("子舱位旅客量"));
      System.out.print("\t");
      System.out.print(block.getItemContent("团队旅客量"));
      System.out.println();
    }
  }
  
  public static void main(String[] args) throws IOException{
    useName();
  }
}