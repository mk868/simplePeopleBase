package info.kucharczyk.java.simple_people_base.repository.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ContactEntity {
    private Integer id;
    private Integer id_customer;
    private Integer type;
    private String contact;

    public ContactEntity(){}

    public ContactEntity(Integer id, Integer id_customer, Integer type, String contact) {
        this.id = id;
        this.id_customer = id_customer;
        this.type = type;
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "ContactEntity{" +
                "id='" + id + '\'' +
                ", id_customer='" + id_customer + '\'' +
                ", type=" + type +
                ", contact='" + contact + '\'' +
                '}';
    }
}
