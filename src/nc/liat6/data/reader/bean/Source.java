package nc.liat6.data.reader.bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 数据源
 * 
 * @author 6tail
 *
 */
public class Source{
  /** 数据源类型 */
  private SourceType sourceType;
  /** 文件 */
  private File file;
  /** 输入流缓存 */
  private InputStreamCache inputStreamCache;
  
  public Source(){}
  
  public Source(File file){
    setFile(file);
  }
  
  public Source(InputStream inputStream) throws IOException{
    setInputStream(inputStream);
  }

  public SourceType getSourceType(){
    return sourceType;
  }

  public void setSourceType(SourceType sourceType){
    this.sourceType = sourceType;
  }

  public File getFile(){
    return file;
  }

  public void setFile(File file){
    this.file = file;
    this.sourceType = SourceType.file;
  }

  public InputStream getInputStream(){
    if(null==inputStreamCache) return null;
    return inputStreamCache.getInputStream();
  }

  public void setInputStream(InputStream inputStream) throws IOException{
    inputStreamCache = new InputStreamCache(inputStream);
    this.sourceType = SourceType.inputStream;
  }
  
  public String toString(){
    switch(sourceType){
      case file:
        return file.getAbsolutePath();
      case inputStream:
        return "inputStream source";
    }
    return "null source";
  }
}