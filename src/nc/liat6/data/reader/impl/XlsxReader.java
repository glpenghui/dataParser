package nc.liat6.data.reader.impl;

import java.io.IOException;
import java.util.List;
import nc.liat6.data.parser.exception.ParserException;
import nc.liat6.data.reader.AbstractReader;
import nc.liat6.data.reader.bean.Source;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;

public class XlsxReader extends AbstractReader{
  private Excel2007Reader realReader;

  public XlsxReader(Source source){
    super(source);
  }

  public void load() throws IOException{
    try{
      switch(source.getSourceType()){
        case file:
          OPCPackage.open(source.getFile());
          break;
        case inputStream:
          OPCPackage.open(source.getInputStream());
          break;
      }
    }catch(InvalidFormatException e){
      throw new ParserException(e);
    }
    stop = false;
    realReader = new Excel2007Reader(source);
    realReader.load();
  }

  public List<String> nextLine(){
    return realReader.nextLine();
  }

  public void stop(){
    super.stop();
    if(null!=realReader){
      realReader.stop();
    }
  }
}