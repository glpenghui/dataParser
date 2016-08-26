package nc.liat6.data.reader;

import nc.liat6.data.reader.bean.Source;

/**
 * 抽象文件读取
 * 
 * @author 6tail
 *
 */
public abstract class AbstractReader implements IReader{
  /** 数据源 */
  protected Source source;
  /** 是否停止读取 */
  protected boolean stop;

  protected AbstractReader(Source source){
    this.source = source;
  }

  public Source getSource(){
    return source;
  }

  public void stop(){
    stop = true;
  }
}