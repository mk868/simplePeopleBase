package info.kucharczyk.java.simple_people_base.dataloader.personloader;

import info.kucharczyk.java.simple_people_base.dataloader.personloader.csv.CsvPersonLoader;
import info.kucharczyk.java.simple_people_base.dataloader.personloader.xml.XmlPersonLoader;
import info.kucharczyk.java.simple_people_base.dataloader.source.SingleFileSource;

import java.util.IllegalFormatException;

public class PersonLoaderFactory {

    public PersonLoader createLoader(SingleFileSource source, String type) throws Exception{
        if(source == null){
            throw new IllegalArgumentException("source cannot be null");
        }
        if(source.getPath() == null){
            throw new IllegalArgumentException("source path cannot be null");
        }

        if(type == null || type.isEmpty()){
            type = getFileExtension(source.getPath());
        }
        type = type.toLowerCase();

        if (type.equals("xml")) {
            return new XmlPersonLoader(source);
        } else if (type.equals("csv")) {
            return new CsvPersonLoader(source);
        }

        throw new Exception("not known type " + type);//TODO custom exception class...
    }

    //or use apache Commons lib
    private String getFileExtension(String path){
        return path.substring(path.lastIndexOf('.') + 1);
    }
}
