package info.kucharczyk.java.simple_people_base.dataloader.personloader.csv;

import info.kucharczyk.java.simple_people_base.dataloader.personloader.PersonLoader;
import info.kucharczyk.java.simple_people_base.model.Contact;
import info.kucharczyk.java.simple_people_base.model.Person;
import info.kucharczyk.java.simple_people_base.dataloader.source.SingleFileSource;
import info.kucharczyk.java.simple_people_base.model.factory.ContactFactory;

import java.io.*;

public class CsvPersonLoader implements PersonLoader { // TODO exclude "CSVLoader" to base class
    private BufferedReader bufReader;
    private FileReader fileReader;
    private Person next;
    private ContactFactory contactFactory;

    public CsvPersonLoader(SingleFileSource source) throws FileNotFoundException {
        fileReader = new FileReader(source.getPath());
        bufReader = new BufferedReader(fileReader);
        contactFactory = new ContactFactory();
        loadNext();
    }

    private Person parseLine(String line){
        String[] cells = line.split(",");
        if (cells.length < 4) {
            return null;
        }

        String name = cells[0];
        String surname = cells[1];
        String age = cells[2];
        String city = cells[3];

        Person person = new Person();
        person.setName(name);
        person.setSurname(surname);
        if (!age.isEmpty())
            person.setAge(Integer.parseInt(age));
        person.setCity(city);

        for (int i = 4; i < cells.length; i++) {
            String value = cells[i];
            if (value.isEmpty()) {
                continue;
            }
            Contact contact = contactFactory.createContact(value);
            person.addContact(contact);
        }

        return person;
    }

    private void loadNext() {
        try {
            String line = bufReader.readLine();

            if (line == null) {
                next = null;
                return;
            }

            next = parseLine(line);
        } catch (IOException e) {
            e.printStackTrace();
            next = null;
        }
    }

    public void close() throws IOException {
        next = null;
        bufReader.close();
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
