package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.AuthToken;
import Model.User;
import Reqeust.LoginRequest;
import Result.LoginResult;


/** log in a user */
public class LoginService {

    /** Creating a login service */
    public LoginService() {

    }

    /** Logs in the user and returns a token
     *  @param r the login request with the username and the password
     *  @return the login result with an auth token
     */
    public LoginResult login(LoginRequest r) {

        LoginResult loginResult = new LoginResult();
        Database db = new Database();

        try {
            db.setConnection();
            UserDAO userDAO = db.getUserDAO();
            AuthTokenDAO tokenDAO = db.getTokenDAO();

            User user = new User();
            user.setUserName(r.getUserName());
            user.setPassword(r.getPassword());

            if (userDAO.loginAuthentication(user)) {

                user = userDAO.findUserByName(r.getUserName());

                AuthToken token = new AuthToken(user);
                tokenDAO.insertAuthToken(token);

                loginResult.setAuthToken(token.getAuthToken());
                loginResult.setUserName(token.getUserName());
                loginResult.setPersonID(user.getPersonID());

                db.closeConnection(true);
                return loginResult;
            }
        }
        catch (DataAccessException e) {
            loginResult.setMessage(e.getMessage());
            try {
                db.closeConnection(false);
            }
            catch (DataAccessException ex) {
                loginResult.setMessage(ex.getMessage());
            }
        }
        return loginResult;
    }
}
