package nc.liat6.data.parser.rule;

/**
 * 解析规则类型
 * 
 * @author 6tail
 *
 */
public enum RuleType{
  /** 固定的块高度 */
  fiexd_block_height,
  /** 根据标识自动检测块结束行 */
  check_block_end
}