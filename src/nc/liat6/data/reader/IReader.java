package nc.liat6.data.reader;

import java.io.IOException;
import java.util.List;
import nc.liat6.data.reader.bean.Source;

/**
 * 文件读取接口
 * 
 * @author 6tail
 *
 */
public interface IReader{
  /**
   * 重新加载
   */
  void load() throws IOException;

  /**
   * 读取下一行数据，如果没有了或者停止读取，返回null
   * 
   * @return 行数据
   */
  List<String> nextLine();

  /**
   * 停止读取
   */
  void stop();

  /**
   * 获取读取的数据源
   * 
   * @return 数据源
   */
  Source getSource();
}