package Model;

/** An unique Authorization Token for the user */
public class AuthToken {

    /** The User's authorization token */
    private String authToken;

    /** The username of the user */
    private String userName;

    /** Create an Authorization Token with a given value
     * @param authToken unique authorization token for the user
     * @param userName the username of the user
     */
    public AuthToken(String authToken, String userName) {
        this.authToken = authToken;
        this.userName = userName;
    }

    /** Create an empty token */
    public AuthToken() {}

    /** Create token based on user data
     * @param user given user data
     */
    public AuthToken(User user) {
        this.authToken = new RandomGenerator().getRandomString();
        this.userName = user.getUserName();
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof AuthToken) {
            AuthToken oToken = (AuthToken) o;
            return oToken.getAuthToken().equals(getAuthToken()) &&
                    oToken.getUserName().equals(getUserName());
        }
        else {
            return false;
        }
    }
}
