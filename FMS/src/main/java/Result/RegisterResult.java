package Result;

/** The body of the register response */
public class RegisterResult extends ResultParent {

    /** The authToken of the user */
    private String authToken;

    /** The user name of the user */
    private String userName;

    /** The person ID of the user */
    private String personID;


    /** Creating a result body with authToken, username, person ID, and message if needed
     *  @param authToken the unique access token for the user
     *  @param userName the user name of the user
     *  @param personID the personID of the user
     */
    public RegisterResult(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public RegisterResult() {}

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

}
