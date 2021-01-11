package Result;

/** The body of a person result with message */
public class PersonResultSingle extends  ResultParent {

    /** The associated username of the user */
    private String associatedUsername;

    /** The personID of the user */
    private String personID;

    /** The first name of the user */
    private String firstName;

    /** The last name of the user */
    private String lastName;

    /** The gender of the user */
    private String gender;

    /** The fatherID of the user if possible */
    private String fatherID;

    /** The motherID of the user if possible */
    private String motherID;

    /** The spouseID of the user if possible */
    private String spouseID;

    /** Creating a single person result with
     *  @param associatedUsername the associated username of the user
     *  @param personID the personID of the user
     *  @param  firstName the first name of the user
     *  @param  lastName the last name of the user
     *  @param  gender the gender of the user
     *  @param  fatherID the fatherID of the user
     *  @param  motherID the motherID of the user
     *  @param  spouseID the spouseID of the user
     */
    public PersonResultSingle(String associatedUsername, String personID, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public PersonResultSingle() {}


    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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
        if (o instanceof PersonResultSingle) {

            PersonResultSingle oResult = (PersonResultSingle) o;
            return oResult.getAssociatedUsername().equals(getAssociatedUsername()) &&
                    oResult.getPersonID().equals(getPersonID()) &&
                    oResult.getFirstName().equals(getFirstName()) &&
                    oResult.getLastName().equals(getLastName()) &&
                    oResult.getGender().equals(getGender()) &&
                    oResult.getFatherID().equals(getFatherID()) &&
                    oResult.getMotherID().equals(getMotherID()) &&
                    oResult.getSpouseID().equals(getSpouseID());
        }
        else {
            return false;
        }
    }
}
