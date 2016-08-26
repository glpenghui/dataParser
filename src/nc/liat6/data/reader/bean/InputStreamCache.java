package nc.liat6.data.reader.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 输入流缓存，以便多次使用该输入流
 * 
 * @author 6tail
 *
 */
public class InputStreamCache{
  /** 缓冲区大小 */
  public static int BUFFER_SIZE = 10240;
  /** 缓存输出流 */
  private ByteArrayOutputStream os;

  public InputStreamCache(InputStream inputStream) throws IOException{
    if(null==inputStream) return;
    os = new ByteArrayOutputStream();
    byte[] buffer = new byte[BUFFER_SIZE];
    int l;
    while(-1!=(l = inputStream.read(buffer))){
      os.write(buffer,0,l);
    }
    os.flush();
  }

  /**
   * 获取输入流，可重复获取
   * 
   * @return 输入流
   */
  public InputStream getInputStream(){
    if(null==os){
      return null;
    }
    return new ByteArrayInputStream(os.toByteArray());
  }
}