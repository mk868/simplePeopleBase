package info.kucharczyk.java.simple_people_base.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Person {
    private String name;
    private String surname;
    private Integer age;
    private String city;
    private List<Contact> contacts;

    public void addContact(Contact contact){
        this.contacts.add(contact);
    }

    public Person(){
        this.contacts = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}
