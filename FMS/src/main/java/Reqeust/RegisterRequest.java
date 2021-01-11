package Reqeust;

/** Body of the register request that contains information */
public class RegisterRequest {

    /** The name of the user */
    private String userName;

    /** The password of the user */
    private String password;

    /** The email of the user */
    private String email;

    /** The first name of the user */
    private String firstName;

    /** The last name of the user */
    private String lastName;

    /** The gender of the user */
    private String gender;

    /** Creating a request that contains user's name, passowrd, email, first and last name, and gender
     *  @param userName the name of the user
     *  @param password the password of the user
     *  @param email the email of the user
     *  @param firstName the first name of the user
     *  @param lastName the last name of the user
     *  @param gender the gender of the user
     */
    public RegisterRequest(String userName, String password, String email, String firstName, String lastName, String gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public RegisterRequest() {}

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
}
