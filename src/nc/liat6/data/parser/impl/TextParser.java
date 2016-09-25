package nc.liat6.data.parser.impl;

import java.util.List;
import java.util.Map;
import nc.liat6.data.parser.AbstractParser;
import nc.liat6.data.parser.bean.Block;
import nc.liat6.data.parser.bean.BlockType;
import nc.liat6.data.parser.bean.Item;
import nc.liat6.data.parser.rule.IParserRule;
import nc.liat6.data.reader.bean.Source;
import nc.liat6.data.reader.impl.TextReader;

/**
 * txt的解析器
 * 
 * @author 6tail
 *
 */
public class TextParser extends AbstractParser{
  /** 支持的文件后缀 */
  public static final String SUPPORT_FILE_SUFFIX = ".txt";

  public TextParser(Source source){
    super(new TextReader(source),SUPPORT_FILE_SUFFIX);
    name = "Text Parser";
  }
  
  protected Block parseBlock(List<List<String>> grids,IParserRule rule,BlockType type){
    Map<String,String> bodyItemNames = rule.getBodyItemNames();
    Block block = new Block();
    block.setType(type);
    int col = 0;
    for(String grid:bodyItemNames.keySet()){
      String[] region = grid.split(",",-1);
      int row = 0;
      int start = Integer.parseInt(region[0]);
      int size = Integer.parseInt(region[1]);
      String name = bodyItemNames.get(grid);
      Item item = new Item();
      item.setName(name);
      item.setRow(row);
      item.setCol(col++);
      String content = "";
      if(row<grids.size()){
        List<String> cols = grids.get(row);
        if(cols.size()>0){
          String line = cols.get(0);
          if(null!=line){
            try{
              byte[] bytes = line.getBytes("utf-8");
              byte[] data = new byte[size];
              System.arraycopy(bytes,start,data,0,size);
              content = new String(data,"utf-8");
            }catch(Exception e){}
          }
        }
      }
      item.setContent(content);
      block.addItem(item);
    }
    return block;
  }
}