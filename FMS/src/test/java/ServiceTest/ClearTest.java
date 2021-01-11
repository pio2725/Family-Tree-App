package ServiceTest;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Result.ClearResult;
import Service.ClearService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClearTest {

    private ClearService clearService;
    private User user1;
    private Person person1;
    private AuthToken token1;
    private Event event1;
    private Database db;

    @BeforeEach
    public void setUp() throws Exception {
        clearService = new ClearService();
        db = new Database();
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
        user1 = new User("inohPak", "dlsdh123", "pio2725@gmail.com", "inoh", "Pak", "m", "asdf123");
        person1 = new Person("asdf123", "inohPak", "inoh", "Pak", "m", "afjdkl1", "dkdil2", "fkdji4");
        event1 = new Event("eventID123", "inohPak", "asdf123", 33.33f, 25.12f, "Korea", "Seoul", "Birth", 1994);
        token1 = new AuthToken("ckd83kd8", "inohPak");
    }

    @AfterEach
    public void tearDown() throws Exception {
        clearService = null;
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void ClearTestPass() throws Exception {

        try {
            Connection connection = db.openConnection();

            UserDAO userDAO = new UserDAO(connection);
            PersonDAO personDAO = new PersonDAO(connection);
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            EventDAO eventDAO = new EventDAO(connection);

            userDAO.insertUser(user1);
            personDAO.insertPerson(person1);
            tokenDAO.insertAuthToken(token1);
            eventDAO.insertEvent(event1);
            db.closeConnection(true);

            ClearResult compareResult = new ClearResult();
            compareResult.setMessage("Clear succeeded");

            ClearResult clearResult = clearService.clear();
            assertEquals(clearResult.getMessage(), compareResult.getMessage());
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void clearTestFail() throws Exception {

        User user2 = new User("inohPak", "dlsdh123", "pio2725@gmail.com", "inoh", "Pak", "m", "asdf123");
        Person person2 = new Person("asdf123", "inohPak", "inoh", "Pak", "m", "afjdkl1", "dkdil2", "fkdji4");
        Event event2 = new Event("eventID123", "inohPak", "asdf123", 33.33f, 25.12f, "Korea", "Seoul", "Birth", 1994);
        AuthToken token2 = new AuthToken("ckd83kd8", "inohPak");

        try {
            Connection connection = db.openConnection();

            UserDAO userDAO = new UserDAO(connection);
            PersonDAO personDAO = new PersonDAO(connection);
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            EventDAO eventDAO = new EventDAO(connection);

            userDAO.insertUser(user1);
            personDAO.insertPerson(person1);
            tokenDAO.insertAuthToken(token1);
            eventDAO.insertEvent(event1);
            userDAO.insertUser(user2);
            personDAO.insertPerson(person2);
            eventDAO.insertEvent(event2);
            tokenDAO.insertAuthToken(token2);
            db.closeConnection(true);

            ClearResult compareResult = new ClearResult();
            compareResult.setMessage("Clear succeeded");

            ClearResult clearResult;
            clearResult = clearService.clear();
            assertEquals(clearResult.getMessage(), compareResult.getMessage());
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}
