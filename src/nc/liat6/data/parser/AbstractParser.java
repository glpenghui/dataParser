package nc.liat6.data.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nc.liat6.data.parser.bean.Block;
import nc.liat6.data.parser.bean.BlockType;
import nc.liat6.data.parser.bean.Fragment;
import nc.liat6.data.parser.bean.Item;
import nc.liat6.data.parser.rule.IParserRule;
import nc.liat6.data.parser.rule.ParserRuleCheckBlockEnd;
import nc.liat6.data.parser.rule.ParserRuleFixedBlockHeight;
import nc.liat6.data.reader.IReader;
import nc.liat6.data.reader.bean.Source;
import nc.liat6.data.reader.bean.SourceType;

/**
 * 抽象解析器
 * 
 * @author 6tail
 *
 */
public abstract class AbstractParser implements IParser{
  /** 解析器名称 */
  protected String name;
  /** 支持的文件名后缀，多个后缀以逗号分隔，如果为null或空字符串表示支持所有后缀 */
  protected String supportSuffix;
  /** 读取接口 */
  protected IReader reader;
  /** 解析规则接口 */
  protected IParserRule rule;
  /** 是否已跳过头部 */
  protected boolean headSkiped = false;
  /** 是否已完成 */
  protected boolean end = false;
  /** 内容（非空行）开始行，从0开始计 */
  protected int startRow;

  protected AbstractParser(IReader reader,String supportSuffix){
    this.reader = reader;
    this.supportSuffix = supportSuffix;
  }

  protected AbstractParser(IReader reader){
    this.reader = reader;
  }

  public String getName(){
    return name;
  }

  /**
   * 设置解析规则
   * 
   * @param rule 解析规则
   */
  public void setRule(IParserRule rule){
    this.rule = rule;
  }

  /**
   * 设置内容开始行，从0开始计
   * 
   * @param startRow 内容开始行，从0开始计
   */
  public void setStartRow(int startRow){
    this.startRow = startRow;
  }

  /**
   * 获取内容开始行，从0开始计，如果为-1，表示不支持。
   * 
   * @return 内容开始行，从0开始计，如果为-1，表示不支持。
   */
  public int getStartRow(){
    boolean canTry = false;
    if(null==supportSuffix||supportSuffix.length()<1) canTry = true;
    Source source = reader.getSource();
    if(SourceType.inputStream==source.getSourceType()) canTry = true;
    if(!canTry){
      File file = source.getFile();
      String fileName = file.getName();
      String fileSuffix = "";
      if(fileName.contains(".")){
        fileSuffix = fileName.substring(fileName.lastIndexOf("."));
      }
      fileSuffix = fileSuffix.toLowerCase();
      if(supportSuffix.toLowerCase().contains(fileSuffix)){
        canTry = true;
      }
    }
    if(!canTry) return -1;
    int startRow = 0;
    try{
      reader.load();
      List<String> line = null;
      while(null!=(line = reader.nextLine())){
        boolean empty = true;
        for(String content:line){
          if(content.trim().length()>0){
            empty = false;
          }
        }
        if(!empty){
          break;
        }
        startRow++;
      }
      reader.nextLine();
    }catch(Exception e){
      startRow = -1;
    }finally{
      reader.stop();
    }
    return startRow;
  }

  protected Block parseBlock(List<List<String>> grids,IParserRule rule,BlockType type){
    Map<String,String> bodyItemNames = rule.getBodyItemNames();
    Block block = new Block();
    block.setType(type);
    for(String grid:bodyItemNames.keySet()){
      String[] yx = grid.split(",",-1);
      int row = Integer.parseInt(yx[0]);
      int col = Integer.parseInt(yx[1]);
      String name = bodyItemNames.get(grid);
      Item item = new Item();
      item.setName(name);
      item.setRow(row);
      item.setCol(col);
      String content = "";
      if(row<grids.size()){
        List<String> cols = grids.get(row);
        if(null!=cols&&col<cols.size()){
          content = cols.get(col);
        }
      }
      item.setContent(content);
      block.addItem(item);
    }
    if(bodyItemNames.size()<1){
      for(int i=0,j=grids.size();i<j;i++){
        List<String> cols = grids.get(i);
        for(int x=0,y=cols.size();x<y;x++){
          Item item = new Item();
          item.setRow(i);
          item.setCol(x);
          item.setContent(cols.get(x));
          block.addItem(item);
        }
      }
    }
    //解析fragment
    Map<String,IParserRule> fragmentRules = rule.getFragmentRule();
    for(String name:fragmentRules.keySet()){
      IParserRule fragmentRule = fragmentRules.get(name);
      List<List<String>> fragmentGrids = new ArrayList<List<String>>();
      int startRow = fragmentRule.getBodyStartRow();
      int endRow = fragmentRule.getBodyEndRow();
      if(-1==endRow){
        endRow = grids.size()-1;
      }
      for(int i = startRow;i<=endRow;i++){
        List<String> line = grids.get(i);
        List<String> inBlockCols = new ArrayList<String>();
        int startCol = fragmentRule.getBodyStartCol();
        int endCol = fragmentRule.getBodyEndCol();
        if(-1==endCol){
          endCol = line.size()-1;
        }
        for(int x = startCol;x<=endCol;x++){
          String content = "";
          if(x<line.size()){
            content = line.get(x);
          }
          inBlockCols.add(content);
        }
        fragmentGrids.add(inBlockCols);
      }
      Fragment fragment = new Fragment();
      fragment.setName(name);
      int fragmentBlockHeight = fragmentRule.getBodyBlockHeight();
      for(int i = 0,j = fragmentGrids.size();i<j;i += fragmentBlockHeight){
        List<List<String>> blockGrids = new ArrayList<List<String>>();
        for(int x = 0,y = fragmentBlockHeight;x<y;x++){
          blockGrids.add(fragmentGrids.get(i*fragmentBlockHeight+x));
        }
        Block subBlock = parseBlock(blockGrids,fragmentRule,BlockType.body_in_fragment);
        fragment.addBlock(subBlock);
      }
      block.addFragment(fragment);
    }
    return block;
  }
  
  /**
   * 解析固定高度的块
   * @param rule 解析规则
   * @param type 块类型
   * @return 块
   */
  protected Block nextFixedHeightBlock(IParserRule rule,BlockType type){
    List<List<String>> grids = new ArrayList<List<String>>();
    int width = 0;
    int height = rule.getBodyBlockHeight();
    int startCol = rule.getBodyStartCol();
    for(int i = 0;i<height;i++){
      List<String> line = reader.nextLine();
      if(null==line){
        end = true;
        line = new ArrayList<String>();
      }
      List<String> inBlockCols = new ArrayList<String>();
      int endCol = rule.getBodyEndCol();
      if(-1==endCol){
        endCol = line.size()-1;
      }
      int lineWidth = endCol-startCol+1;
      if(width<lineWidth) width = lineWidth;
      for(int x=startCol;x<=endCol;x++){
        String content = "";
        if(x<line.size()){
          content = line.get(x);
        }
        inBlockCols.add(content);
      }
      grids.add(inBlockCols);
    }
    Block block = parseBlock(grids,rule,type);
    block.setWidth(width);
    block.setHeight(height);
    return block;
  }
  
  /**
   * 根据标识自动检测块结束行
   * @param rule 解析规则
   * @param type 块类型
   * @return 块
   */
  protected Block nextCheckEndBlock(IParserRule rule,BlockType type){
    ParserRuleCheckBlockEnd checkRule = (ParserRuleCheckBlockEnd)rule;
    List<List<String>> grids = new ArrayList<List<String>>();
    int width = 0;
    int height = 0;
    int startCol = rule.getBodyStartCol();
    boolean blockEnd = false;
    while(!blockEnd){
      List<String> line = reader.nextLine();
      if(null==line){
        blockEnd = true;
        end = true;
        line = new ArrayList<String>();
      }
      List<String> inBlockCols = new ArrayList<String>();
      int endCol = rule.getBodyEndCol();
      if(-1==endCol){
        endCol = line.size()-1;
      }
      int lineWidth = endCol-startCol+1;
      if(width<lineWidth) width = lineWidth;
      for(int x = startCol;x<=endCol;x++){
        String content = "";
        if(x<line.size()){
          content = line.get(x);
        }
        inBlockCols.add(content);
      }
      boolean hit = true;
      Map<Integer,String> values = checkRule.getEndRowValues();
      for(Integer col:values.keySet()){
        if(null==col) continue;
        if(col>=inBlockCols.size()) continue;
        if(!values.get(col).equals(inBlockCols.get(col))){
          hit = false;
        }
      }
      if(hit) blockEnd = true;
      grids.add(inBlockCols);
      height++;
    }
    Block block = parseBlock(grids,rule,type);
    block.setWidth(width);
    block.setHeight(height);
    return block;
  }

  protected Block nextBlock(IParserRule rule,BlockType type){
    switch(rule.getType()){
      case fiexd_block_height:return nextFixedHeightBlock(rule,type);
      case check_block_end:return nextCheckEndBlock(rule,type);
    }
    return null;
  }

  public Block nextBlock() throws IOException{
    if(end) return null;
    if(!headSkiped){
      //新的加载
      reader.load();
      //跳过开头的空行
      for(int i = 0;i<startRow;i++){
        reader.nextLine();
      }
      //构造一个head解析规则
      IParserRule headRule = new ParserRuleFixedBlockHeight(){
        public int getBodyStartCol(){
          return 0;
        }
        public int getBodyEndCol(){
          return -1;
        }
        public int getBodyStartRow(){
          return 0;
        }
        public int getBodyEndRow(){
          return -1;
        }
        public int getBodyBlockHeight(){
          return rule.getBodyStartRow();
        }
        public Map<String,String> getBodyItemNames(){
          return rule.getHeadItemNames();
        }
      };
      //解析head block
      Block block = nextBlock(headRule,BlockType.head);
      headSkiped = true;
      //当自定义了head解析规则，才返回head block
      if(rule.getHeadItemNames().size()>0){
        return block;
      }
    }
    //解析数据块
    Block block = nextBlock(rule,BlockType.body);
    //如果发现没有内容的数据块表示该结束了
    boolean isBlank = true;
    for(Item item:block.getItems()){
      String content = item.getContent();
      if(null!=content&&content.trim().length()>0){
        isBlank = false;
        break;
      }
    }
    if(isBlank){
      for(Fragment frag:block.getFragments()){
        if(frag.getBlocks().size()>0){
          isBlank = false;
          break;
        }
      }
    }
    return isBlank?null:block;
  }
}