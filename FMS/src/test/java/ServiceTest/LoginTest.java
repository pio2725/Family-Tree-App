package ServiceTest;

import DAO.Database;
import DAO.UserDAO;
import Model.User;
import Reqeust.LoginRequest;
import Reqeust.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;
import Service.LoginService;
import Service.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    private LoginService loginService;
    private Database db;
    private User user1;

    @BeforeEach
    public void setUp() throws Exception {
        loginService = new LoginService();
        user1 = new User("inohpak", "dlsdh123", "pio2725@gmail.com", "inoh", "Pak", "m", "asdf123");

        db = new Database();
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        loginService = null;
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void loginTestPass() throws Exception {

        try {
            Connection connection = db.openConnection();
            UserDAO userDAO = new UserDAO(connection);
            userDAO.insertUser(user1);
            db.closeConnection(true);

            LoginRequest loginRequest = new LoginRequest(user1.getUserName(), user1.getPassword());

            LoginResult compareResult = new LoginResult();
            compareResult.setUserName(user1.getUserName());

            LoginResult loginResult = loginService.login(loginRequest);
            compareResult.setAuthToken(loginResult.getAuthToken());
            compareResult.setPersonID(loginResult.getPersonID());

            assertEquals(compareResult.getAuthToken(), loginResult.getAuthToken());
            assertEquals(compareResult.getUserName(), loginResult.getUserName());
            assertEquals(compareResult.getPersonID(), loginResult.getPersonID());
        }
        catch (DAO.DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void loginTestFail() throws Exception {

        try {
            Connection connection = db.openConnection();
            UserDAO userDAO = new UserDAO(connection);
            userDAO.insertUser(user1);
            db.closeConnection(true);

            LoginRequest loginRequest = new LoginRequest("inno", user1.getPassword());

            LoginResult compareResult = new LoginResult();
            compareResult.setMessage("error invalid username or password");

            LoginResult loginResult = loginService.login(loginRequest);

            assertEquals(loginResult.getMessage(), compareResult.getMessage());
        }
        catch (DAO.DataAccessException e) {
            db.closeConnection(false);
        }
    }
}
