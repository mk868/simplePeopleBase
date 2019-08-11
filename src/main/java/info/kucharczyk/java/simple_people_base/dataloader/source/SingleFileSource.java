package info.kucharczyk.java.simple_people_base.dataloader.source;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SingleFileSource implements Source {
    private String path;

    public SingleFileSource(){}

    public SingleFileSource(String path) {
        this.path = path;
    }
}
