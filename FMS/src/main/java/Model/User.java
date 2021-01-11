package Model;

import Reqeust.LoginRequest;
import Reqeust.RegisterRequest;

/**
 * A User with name, password, email, first name, last name, gender, and unique ID
 */

public class User {

    /** The User's name */
    private String userName;

    /** The User's password*/
    private String password;

    /** The User's email */
    private String email;

    /** The User's first name */
    private String firstName;

    /** The User's last name */
    private String lastName;

    /** The User's gender */
    private String gender;

    /** The User's unique ID */
    private String personID;

    /** Create a User with a given username, password, email, first and last name, gender and unique ID
     *  @param userName the user name of the user
     *  @param password the password of the user
     *  @param email    the email of the user
     *  @param firstName the first name of the user
     *  @param lastName the last name of the user
     *  @param gender the gender of the user
     *  @param personID the unique ID of the user
     */
    public User(String userName, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public User() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof User) {
            User oUser = (User) o;
            return oUser.getUserName().equals(getUserName()) &&
                    oUser.getPassword().equals(getPassword()) &&
                    oUser.getEmail().equals(getEmail()) &&
                    oUser.getFirstName().equals(getFirstName()) &&
                    oUser.getLastName().equals(getLastName()) &&
                    oUser.getGender().equals(getGender()) &&
                    oUser.getPersonID().equals(getPersonID());
        }
        else {
            return false;
        }
    }
}
