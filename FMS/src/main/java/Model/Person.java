package Model;

/** A Person with an ID, associated user name, first name, last name, gender,
 *  and possible father, mother, and spouse ID
 */
public class Person {

    /** The person's ID */
    private String personID;

    /** The person's associated user name */
    private String associatedUsername;

    /** The person's first name */
    private String firstName;

    /** The person's last name */
    private String lastName;

    /** The person's gender */
    private String gender;

    /** The father ID of the person, if possible */
    private String fatherID;

    /** The mother ID of the person, if possible */
    private String motherID;

    /** The spouse ID of the person, if possible */
    private String spouseID;

    /** Creating a person with an ID, associated user name, first name, last name, gender,
     *  and possible father, mother, and spouse ID
     *  @param personID the ID of the person
     *  @param associatedUsername the associated name to which the person belongs
     *  @param firstName the first name of the person
     *  @param lastName the last name of the person
     *  @param gender the gender of the person
     *  @param fatherID the father's ID of the person, if possible
     *  @param motherID the mother's ID of the person, if possible
     *  @param spouseID the spouse's ID of the person, if possible
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public Person() {}

    public Person(User user) {
        this.personID = user.getPersonID();
        this.associatedUsername = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUserName() {
        return associatedUsername;
    }

    public void setAssociatedUserName(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Person) {
            Person oPerson = (Person) o;
            return oPerson.getPersonID().equals(getPersonID()) &&
                    oPerson.getAssociatedUserName().equals(getAssociatedUserName()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender().equals(getGender()) &&
                    oPerson.getFatherID().equals(getFatherID()) &&
                    oPerson.getMotherID().equals(getMotherID()) &&
                    oPerson.getSpouseID().equals(getSpouseID());
        }
        else {
            return false;
        }
    }
}
