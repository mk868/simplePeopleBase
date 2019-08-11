package info.kucharczyk.java.simple_people_base;


import info.kucharczyk.java.simple_people_base.cli.AppUserInterface;
import info.kucharczyk.java.simple_people_base.cli.UserInputs;
import info.kucharczyk.java.simple_people_base.dataloader.personloader.PersonLoader;
import info.kucharczyk.java.simple_people_base.dataloader.personloader.PersonLoaderFactory;
import info.kucharczyk.java.simple_people_base.dataloader.personloader.csv.CsvPersonLoader;
import info.kucharczyk.java.simple_people_base.dataloader.personloader.xml.XmlPersonLoader;
import info.kucharczyk.java.simple_people_base.dataloader.source.SingleFileSource;
import info.kucharczyk.java.simple_people_base.model.Contact;
import info.kucharczyk.java.simple_people_base.model.Person;
import info.kucharczyk.java.simple_people_base.repository.entity.ContactEntity;
import info.kucharczyk.java.simple_people_base.repository.entity.CustomerEntity;
import info.kucharczyk.java.simple_people_base.repository.impl.ContactRepositoryImpl;
import info.kucharczyk.java.simple_people_base.repository.impl.CustomerRepositoryImpl;

import java.sql.Connection;
import java.sql.DriverManager;

public class App {
    public static void main(String[] args) throws Exception {
        AppUserInterface appUserInterface = new AppUserInterface();
        appUserInterface.init();
        if (!appUserInterface.parse(args)) {
            return;
        }
        UserInputs userInputs = appUserInterface.getinputs();

        SingleFileSource source = new SingleFileSource(userInputs.getInputPath());
        try (Connection conn = DriverManager.getConnection(userInputs.getConnectionString())) {
            CustomerRepositoryImpl customerRepository = new CustomerRepositoryImpl(conn);
            customerRepository.init();
            ContactRepositoryImpl contactRepository = new ContactRepositoryImpl(conn);
            contactRepository.init();

            PersonLoaderFactory personLoaderFactory = new PersonLoaderFactory();

            try (PersonLoader loader = personLoaderFactory.createLoader(source, userInputs.getType())) {
                while (loader.hasMoreElements()) {
                    Person person = loader.nextElement();

                    //TODO ModelMapper
                    //simple mapping + save to db
                    CustomerEntity customerEntity = CustomerEntity.builder()
                            .name(person.getName())
                            .surname(person.getSurname())
                            .age(person.getAge())
                            .build();
                    customerRepository.save(customerEntity);
                    System.out.println("Customer created (id: " + customerEntity + ")");//TODO use logger

                    for (Contact contact : person.getContacts()) {
                        ContactEntity contactEntity = ContactEntity.builder()
                                .id_customer(customerEntity.getId())
                                .type(contact.getType().getNumber())
                                .contact(contact.getValue())
                                .build();
                        contactRepository.save(contactEntity);
                        System.out.println("    Contact created (id: " + contactEntity + ")");
                    }
                }
            }
        }
    }
}