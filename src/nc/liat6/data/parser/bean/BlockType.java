package nc.liat6.data.parser.bean;

/**
 * 块类型
 * 
 * @author 6tail
 *
 */
public enum BlockType{
  /** 头部块 */
  head,
  /** 数据块 */
  body,
  /** 分片中的数据块 */
  body_in_fragment
}