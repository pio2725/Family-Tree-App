package ServiceTest;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Result.EventResultSingle;
import Service.EventServiceSingle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SingleEventServiceTest {

    private EventServiceSingle eventServiceSingle;
    private User user1;
    private Person person1;
    private Event event1;
    private AuthToken token1;
    private Database db;

    @BeforeEach
    public void setUp() throws Exception {
        eventServiceSingle = new EventServiceSingle();
        user1 = new User("inohpak", "dlsdh123", "pio2725@gmail.com", "inoh", "Pak", "m", "jiji123");
        person1 = new Person("asdf123", "inohpak", "inoh", "Pak", "m", "afjdkl1", "dkdil2", "fkdji4");
        event1 = new Event("eventID123", "inohpak", "asdf123", 33.33f, 25.12f, "Korea", "Seoul", "Birth", 1994);
        token1 = new AuthToken("ckd83kd8", "inohpak");
        db = new Database();
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        eventServiceSingle = null;
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void eventSingleTestPass() throws Exception {

        Database db = new Database();

        try {
            Connection connection = db.openConnection();

            EventDAO eventDAO = new EventDAO(connection);
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            UserDAO userDAO = new UserDAO(connection);

            eventDAO.insertEvent(event1);
            tokenDAO.insertAuthToken(token1);
            userDAO.insertUser(user1);

            db.closeConnection(true);

            EventResultSingle compareResult = new EventResultSingle();
            compareResult.setAssociatedUsername(event1.getAssociatedUserName());
            compareResult.setEventID(event1.getEventID());
            compareResult.setPersonID(event1.getPersonID());
            compareResult.setLatitude(event1.getLatitude());
            compareResult.setLongitude(event1.getLongitude());
            compareResult.setCountry(event1.getCountry());
            compareResult.setCity(event1.getCity());
            compareResult.setEventType(event1.getEventType());
            compareResult.setYear(event1.getYear());

            EventResultSingle result = eventServiceSingle.getSingleEvent(event1.getEventID(), token1.getAuthToken());

            assertEquals(compareResult.getEventID(), result.getEventID());
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void eventSingleTestFail() throws Exception {

        Database db = new Database();

        try {
            Connection connection = db.openConnection();

            EventDAO eventDAO = new EventDAO(connection);
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            UserDAO userDAO = new UserDAO(connection);

            eventDAO.insertEvent(event1);
            tokenDAO.insertAuthToken(token1);
            userDAO.insertUser(user1);

            db.closeConnection(true);

            EventResultSingle compareResult = new EventResultSingle();
            compareResult.setAssociatedUsername(event1.getAssociatedUserName());
            compareResult.setEventID(event1.getEventID());
            compareResult.setPersonID(event1.getPersonID());
            compareResult.setLatitude(event1.getLatitude());
            compareResult.setLongitude(event1.getLongitude());
            compareResult.setCountry(event1.getCountry());
            compareResult.setCity(event1.getCity());
            compareResult.setEventType(event1.getEventType());
            compareResult.setYear(event1.getYear());

            EventResultSingle result = eventServiceSingle.getSingleEvent("eventid", token1.getAuthToken());

            assertNotEquals(compareResult, result);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }






}
