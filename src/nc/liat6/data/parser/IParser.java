package nc.liat6.data.parser;

import java.io.IOException;
import nc.liat6.data.parser.bean.Block;

/**
 * 解析器接口
 * 
 * @author 6tail
 *
 */
public interface IParser{
  /**
   * 获取解析器名称
   * 
   * @return 解析器名称
   */
  String getName();

  /**
   * 读取下一个块，如果没有了，返回null。如果未设置head的解析规则，不读取head块。
   * 
   * @return 块
   */
  Block nextBlock() throws IOException;
}