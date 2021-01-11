package Reqeust;

/** The body of a login request */
public class LoginRequest {

    /** The username of the user */
    private String userName;

    /** The password of the user */
    private String password;

    /** Creating a login request with username and password
     *  @param userName the username of the user loggin in
     *  @param password the password of the user
     */
    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

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
}
