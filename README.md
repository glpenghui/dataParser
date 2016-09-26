# dataParser
通用表格数据解析工具，目前支持xls、xlsx、csv、html、xml、txt形式的数据源。
> 支持jdk1.5及以上版本，jdk1.5需额外导入javax包，否则解析xlsx会报错。

##代码目录
1. lib：依赖的jar
* files：示例文件
* samples：示例代码
* captures：截图

##更新日志
1. 20160925 新增支持txt数据解析（含示例代码及文件）

##示例
###待解析excel内容
<table>
  <tr>
  <th></th>
  <th>A</th>
  <th>B</th>
  <th>C</th>
  <th>D</th>
  <th>E</th>
  </tr>
  <tr>
  <td>1</td>
  <td>序号</td>
  <td>姓名</td>
  <td>性别</td>
  <td>年龄</td>
  <td>民族</td>
  </tr>
  <tr>
  <td>2</td>
  <td>1</td>
  <td>张三</td>
  <td>男</td>
  <td>20</td>
  <td>汉族</td>
  </tr>
  <tr>
  <td>3</td>
  <td>2</td>
  <td>李四</td>
  <td>女</td>
  <td>18</td>
  <td>汉族</td>
  </tr>
  <tr>
  <td>4</td>
  <td>3</td>
  <td>王二</td>
  <td>男</td>
  <td>30</td>
  <td>满族</td>
  </tr>
</table>

###java代码
    //所有资源的解析都统一按以下几步走哦

    //待解析资源，支持xls、xlsx、csv、html、xml、txt、inputStream
    File file = new File("test.xls");

    //自定义解析规则，这里使用列对应字母，数据Block从第1行开始（行从0开始计）
    IParserRule rule = new ParserRuleLetter(1,"A","E");

    //通过工厂获取解析器接口
    IParser parser = ParserFactory.getInstance().getParser(file,rule);

    //循环读取所有Block
    Block block = null;
    while(null!=(block = parser.nextBlock())){
      //显示Item内容
      System.out.print(block.getItemContent("A"));
      System.out.print("\t");
      System.out.print(block.getItemContent("B"));
      System.out.print("\t");
      System.out.print(block.getItemContent("C"));
      System.out.print("\t");
      System.out.print(block.getItemContent("D"));
      System.out.print("\t");
      System.out.println(block.getItemContent("E"));
      System.out.println("_____________________________________");
    }

###输出结果

    1 张三  男 20  汉族
    _____________________________________
    2 李四  女 18  汉族
    _____________________________________
    3 王二  男 30  满族
    _____________________________________

##定义
###1. Block块
每一条独立的信息记录定义为Block，一个表格中一般会有N个Block,下图中框起来的视为一个Block。Block目前支持三种类型：1、表头Block（head），2、数据Block（body），3、fragment中的数据Block（body_in_fragment）。如下图中，从序号为1的记录开始的3个Block，即为数据Block；从表格第一行直到遇到数据行之间的部分，统一划入一个表头Block；fragment中的数据Block见下方另行说明。

单行Block示例：

![单行Block示例](https://github.com/6tail/dataParser/raw/master/captures/single_line_block.jpg)

多行Block示例：

![多行Block示例](https://github.com/6tail/dataParser/raw/master/captures/multiple_line_block.jpg)

###2. Item格子
每一个独立的单元格定义为Item，下图中框起来的都视为Item，一个Block中通常会有N个Item。

![Item示例](https://github.com/6tail/dataParser/raw/master/captures/item.jpg)

###3. Fragment分片
一个Block中可能会存在一块或多块相对独立的区域，这个区域中有多个小的Block，这些小Block用专门的类型以便与大Block区分（即body_in_fragment），这个区域定义为Fragment，下图中框起来的都是Fragment。

![Fragment示例](https://github.com/6tail/dataParser/raw/master/captures/fragment.jpg)