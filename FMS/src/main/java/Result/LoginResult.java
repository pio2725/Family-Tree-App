package Result;

/** The body of a login result */
public class LoginResult extends  ResultParent {

    /** The authorization token of the user */
    private String authToken;

    /** The name of the user */
    private String userName;

    /** The person ID of the user */
    private String personID;

    /** Creating a login result with the token, username, personID, and a possible message
     *  @param authToken the unique access token of the user
     *  @param userName the username of the user
     *  @param personID the personID of the user
     */
    public LoginResult(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public LoginResult() {}

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
