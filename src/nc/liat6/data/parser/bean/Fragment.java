package nc.liat6.data.parser.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 分片，分片中可能包括块，块中也可能包括分片，所谓你中有我，我中有你，用于复杂的表格
 * 
 * @author 6tail
 *
 */
public class Fragment{
  /** 分片名称 */
  private String name;
  /** 分片中的块们 */
  private List<Block> blocks = new ArrayList<Block>();

  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name = name;
  }

  /**
   * 添加一个块，只有非空的块才会添加进去
   * 
   * @param block 块
   */
  public void addBlock(Block block){
    boolean isBlank = true;
    for(Item item:block.getItems()){
      String content = item.getContent();
      if(null!=content&&content.trim().length()>0){
        isBlank = false;
        break;
      }
    }
    if(!isBlank){
      blocks.add(block);
    }
  }

  /**
   * 获取块们
   * 
   * @return 块们
   */
  public List<Block> getBlocks(){
    return blocks;
  }
  
  public String toString(){
    StringBuilder s = new StringBuilder();
    s.append("fragment ");
    s.append(":");
    if(blocks.size()>0){
      s.append(" blocks = [");
      for(int i = 0,j = blocks.size();i<j;i++){
        if(i>0){
          s.append(",");
        }
        s.append(blocks.get(i));
      }
      s.append("]");
    }
    return s.toString();
  }
}