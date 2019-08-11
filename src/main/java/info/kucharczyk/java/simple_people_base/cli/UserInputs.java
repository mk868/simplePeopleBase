package info.kucharczyk.java.simple_people_base.cli;

import lombok.Data;

@Data
public class UserInputs {
    private String connectionString;
    private String inputPath;
    private String type;
}
