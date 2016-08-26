package nc.liat6.data.parser.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个独立的包含一条信息的完整块，可能对应excel中的一行数据，也可能是多行多列数据，视实际情况而定
 * 
 * @author 6tail
 *
 */
public class Block{
  /** 块宽度 */
  private int width;
  /** 块高度 */
  private int height;
  /** 块类型 */
  private BlockType type;
  /** 该块中按顺序读取的格们 */
  private List<Item> items = new ArrayList<Item>();
  /** 存放名称对应的格子下标，格式示例：姓名=1 */
  private Map<String,Integer> name4Item = new HashMap<String,Integer>();
  /** 存放位于块中的位置对应的格子下标，格式示例：row,col=3 */
  private Map<String,Integer> grid4Item = new HashMap<String,Integer>();
  /** 分片们 */
  private List<Fragment> fragments = new ArrayList<Fragment>();
  /** 存放名称对应的分片下标，格式示例：消费记录=1 */
  private Map<String,Integer> name4Fragment = new HashMap<String,Integer>();

  public int getWidth(){
    return width;
  }

  public void setWidth(int width){
    this.width = width;
  }

  public int getHeight(){
    return height;
  }

  public void setHeight(int height){
    this.height = height;
  }

  public BlockType getType(){
    return type;
  }

  public void setType(BlockType type){
    this.type = type;
  }

  /**
   * 添加一个格子
   * 
   * @param item 格子
   */
  public void addItem(Item item){
    int size = items.size();
    items.add(item);
    if(null!=item.getName()){
      name4Item.put(item.getName(),size);
    }
    grid4Item.put(item.getRow()+","+item.getCol(),size);
  }

  public void addFragment(Fragment fragment){
    int size = fragments.size();
    fragments.add(fragment);
    name4Fragment.put(fragment.getName(),size);
  }

  /**
   * 获取格子们
   * 
   * @return 格子们
   */
  public List<Item> getItems(){
    return items;
  }

  /**
   * 获取分片们
   * 
   * @return 分片们
   */
  public List<Fragment> getFragments(){
    return fragments;
  }

  /**
   * 通过名称获取格子，如果不存在，返回null
   * 
   * @param name 格子名称
   * @return 格子
   */
  public Item getItemByName(String name){
    return items.get(name4Item.get(name));
  }

  /**
   * 通过名称获取分片
   * 
   * @param name 分片名称
   * @return 分片
   */
  public Fragment getFragmentByName(String name){
    return fragments.get(name4Fragment.get(name));
  }

  /**
   * 通过在块中的位置获取格子
   * 
   * @param row 行，从0开始计
   * @param col 列，从0开始计
   * @return 格子
   */
  public Item getItemByGrid(int row,int col){
    Integer index = grid4Item.get(row+","+col);
    int size = items.size();
    Item item = null;
    if(null!=index){
      if(index>-1&&index<size){
        item = items.get(index);
      }
    }
    if(null==item){
      item = new Item();
      item.setCol(col);
      item.setRow(row);
      item.setContent("");
    }
    return item;
  }

  public String toString(){
    StringBuilder s = new StringBuilder();
    s.append("block ");
    s.append(":");
    if(items.size()>0){
      s.append(" items = [");
      for(int i = 0,j = items.size();i<j;i++){
        if(i>0){
          s.append(",");
        }
        s.append(items.get(i));
      }
      s.append("]");
    }
    if(fragments.size()>0){
      s.append(" sub fragments = [");
      for(int i = 0,j = fragments.size();i<j;i++){
        if(i>0){
          s.append(",");
        }
        s.append(fragments.get(i));
      }
      s.append("]");
    }
    return s.toString();
  }
}