package info.kucharczyk.java.simple_people_base.model.factory;

import info.kucharczyk.java.simple_people_base.model.Contact;
import info.kucharczyk.java.simple_people_base.model.ContactType;

public class ContactFactory {
    public Contact createContact(String value) {
        Contact contact = new Contact();
        contact.setValue(value);

        ContactType type = ContactType.Unknown;
        if (value.matches("^[0-9 ]+$")) {
            type = ContactType.Phone;
        } else if (value.matches("^[^@]+@[^@]+$")) {
            type = ContactType.Email;
        } else if (value.matches("^jbr")) {
            type = ContactType.Jabber;
        }
        contact.setType(type);

        return contact;
    }
}
