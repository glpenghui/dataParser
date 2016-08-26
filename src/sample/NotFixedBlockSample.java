package sample;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import nc.liat6.data.parser.IParser;
import nc.liat6.data.parser.ParserFactory;
import nc.liat6.data.parser.bean.Block;
import nc.liat6.data.parser.bean.Item;
import nc.liat6.data.parser.rule.IParserRule;
import nc.liat6.data.parser.rule.ParserRuleCheckBlockEnd;

/**
 * 不定高度的block解析示例
 * @author 6tail
 *
 */
public class NotFixedBlockSample{
  public static void main(String[] args) throws IOException{
    File file = new File("files\\不定高度的block示例.xls");

    IParserRule rule = new ParserRuleCheckBlockEnd(){
      private Map<Integer,String> values = new HashMap<Integer,String>();
      public Map<Integer,String> getEndRowValues(){
        if(values.isEmpty()){
          values.put(0,"");
          values.put(1,"");
          values.put(2,"");
          values.put(3,"");
        }
        return values;
      }
    };

    IParser parser = ParserFactory.getInstance().getParser(file,rule);

    Block block = null;
    while(null!=(block = parser.nextBlock())){
      for(int i=0,j=block.getHeight();i<j;i++){
        for(int x=0,y=block.getWidth();x<y;x++){
          Item item = block.getItemByGrid(i,x);
          System.out.print(item.getRow()+","+item.getCol()+"="+item.getContent()+"\t");
        }
        System.out.println();
      }
      System.out.println("________________________");
    }
  }
}