package info.kucharczyk.java.simple_people_base.model;

public enum ContactType {
    Unknown(0),
    Email(1),
    Phone(2),
    Jabber(3);

    private final int number;

    ContactType(int number) {
        this.number = number;
    }

    public static ContactType fromString(String name){
        for (ContactType value : ContactType.values()) {
            if (value.name().compareToIgnoreCase(name) == 0) {
                return value;
            }
        }

        return ContactType.Unknown;
    }

    public int getNumber() {
        return number;
    }
}
