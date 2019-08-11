package info.kucharczyk.java.simple_people_base.model;

import lombok.Data;

@Data
public class Contact {
    private ContactType type;
    private String value;

    public Contact(){}

    public static Contact fromString(String value){
        Contact contact = new Contact();
        contact.setValue(value);
        ContactType type = ContactType.Unknown;
        if(value.matches("^[0-9 ]+$")){
            type = ContactType.Phone;
        } else if(value.matches("^[^@]+@[^@]+$")){
            type = ContactType.Email;
        } else if(value.matches("^jbr")){
            type = ContactType.Jabber;
        }
        contact.setType(type);
        return contact;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
