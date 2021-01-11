package ServiceTest;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Reqeust.FillRequest;
import Result.FillResult;
import Service.FillService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FillServiceTest {

    private FillService fillService;
    private User user1;
    private Person person1;
    private Event event1;
    private AuthToken token1;
    private Database db;

    @BeforeEach
    public void setUp() throws Exception {
        fillService = new FillService();
        user1 = new User("inohpak", "dlsdh123", "pio2725@gmail.com", "inoh", "Pak", "m", "asdf123");
        person1 = new Person("asdf123", "inohPak", "inoh", "Pak", "m", "afjdkl1", "dkdil2", "fkdji4");
        event1 = new Event("eventID123", "inohPak", "asdf123", 33.33f, 25.12f, "Korea", "Seoul", "Birth", 1994);
        token1 = new AuthToken("ckd83kd8", "inohPak");

        db = new Database();
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        fillService = null;
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void fillServiceTestPass() throws DataAccessException {

        try {
            Database db = new Database();
            Connection connection = db.openConnection();
            UserDAO userDAO = new UserDAO(connection);
            userDAO.insertUser(user1);
            db.closeConnection(true);

            FillResult compareResult = new FillResult();
            compareResult.setMessage("Successfully added 31 persons and 124 events to the database.");

            FillRequest fillRequest = new FillRequest();
            fillRequest.setGenerationNum(4);
            fillRequest.setUserName(user1.getUserName());

            FillResult fillResult = fillService.fill(fillRequest);
            assertEquals(compareResult.getMessage(), fillResult.getMessage());
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void fillServiceTestFail() throws DataAccessException {

        try {
            Database db = new Database();
            Connection connection = db.openConnection();
            UserDAO userDAO = new UserDAO(connection);
            userDAO.insertUser(user1);
            db.closeConnection(true);

            FillResult compareResult = new FillResult();
            compareResult.setMessage("Invalid username");

            FillRequest fillRequest = new FillRequest();
            fillRequest.setGenerationNum(5);
            fillRequest.setUserName("invalidName");

            FillResult fillResult = fillService.fill(fillRequest);
            assertEquals(compareResult.getMessage(), fillResult.getMessage());
            assertNotEquals(fillResult, compareResult);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}
