package info.kucharczyk.java.simple_people_base.repository.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerEntity {
    private Integer id;
    private String name;
    private String surname;
    private Integer age;

    public CustomerEntity(){}

    public CustomerEntity(Integer id, String name, String surname, Integer age) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    @Override
    public String toString() {
        return "CustomerEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                '}';
    }
}
