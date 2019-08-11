package info.kucharczyk.java.simple_people_base.model;

import lombok.Data;

@Data
public class Contact {
    private ContactType type;
    private String value;

    public Contact(){}

    @Override
    public String toString() {
        return "Contact{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
