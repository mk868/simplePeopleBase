package info.kucharczyk.java.simple_people_base.dataloader.personloader.xml;

import info.kucharczyk.java.simple_people_base.dataloader.personloader.PersonLoader;
import info.kucharczyk.java.simple_people_base.model.Contact;
import info.kucharczyk.java.simple_people_base.model.ContactType;
import info.kucharczyk.java.simple_people_base.model.Person;
import info.kucharczyk.java.simple_people_base.dataloader.source.SingleFileSource;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class XmlPersonLoader implements PersonLoader { // TODO exclude "XmlLoader" to base class
    private FileReader fileReader;
    private XMLEventReader eventReader;
    private Person next;

    public XmlPersonLoader(SingleFileSource source) throws FileNotFoundException, XMLStreamException {
        fileReader = new FileReader(source.getPath());

        XMLInputFactory factory = XMLInputFactory.newInstance();
        eventReader = factory.createXMLEventReader(fileReader);
        loadNext();
    }

    private void loadNext() {
        Person person = null;

        try {
            while (eventReader.hasNext()) {
                XMLEvent xmlEvent = eventReader.nextEvent();

                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();

                    if ("person".equalsIgnoreCase(startElement.getName().getLocalPart())) {
                        person = new Person();
                    }

                    switch (startElement.getName().getLocalPart()) {
                        case "name":
                            Characters nameDataEvent = (Characters) eventReader.nextEvent();
                            person.setName(nameDataEvent.getData());
                            break;
                        case "surname":
                            Characters surnameDataEvent = (Characters) eventReader.nextEvent();
                            person.setSurname(surnameDataEvent.getData());
                            break;
                        case "age":
                            Characters ageDataEvent = (Characters) eventReader.nextEvent();
                            person.setAge(Integer.parseInt(ageDataEvent.getData()));
                            break;
                        case "city":
                            Characters cityDataEvent = (Characters) eventReader.nextEvent();
                            person.setCity(cityDataEvent.getData());
                            break;
                    }

                    if ("contact".equalsIgnoreCase(startElement.getName().getLocalPart())) {
                        Contact contact = new Contact();
                        @SuppressWarnings("unchecked")
                        Iterator<Attribute> iterator = startElement.getAttributes();

                        while (iterator.hasNext()) {
                            Attribute attribute = iterator.next();
                            QName name = attribute.getName();
                            if ("type".equalsIgnoreCase(name.getLocalPart())) {
                                contact.setType(ContactType.fromString(attribute.getValue()));
                            }
                        }
                        Characters contactDataEvent = (Characters) eventReader.nextEvent();
                        contact.setValue(contactDataEvent.getData());
                        person.addContact(contact);
                    }
                }

                if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if ("person".equalsIgnoreCase(endElement.getName().getLocalPart())) {
                        break;
                    }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        this.next = person;
    }

    public void close() throws IOException {
        next = null;
        fileReader.close();
    }

    public boolean hasMoreElements() {
        return next != null;
    }

    public Person nextElement() {
        Person current = next;
        loadNext();
        return current;
    }
}
