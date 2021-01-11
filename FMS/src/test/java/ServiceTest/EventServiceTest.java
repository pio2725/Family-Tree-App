package ServiceTest;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Result.EventResult;
import Service.EventService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {

    private EventService eventService;
    private User user1;
    private Person person1;
    private Event event1;
    private AuthToken token1;
    private Database db;

    @BeforeEach
    public void setUp() throws Exception {
        eventService = new EventService();
        user1 = new User("inohpak", "dlsdh123", "pio2725@gmail.com", "inoh", "Pak", "m", "jiji123");
        person1 = new Person("asdf123", "inohpak", "inoh", "Pak", "m", "afjdkl1", "dkdil2", "fkdji4");
        event1 = new Event("eventID123", "inohpak", "asdf123", 33.33f, 25.12f, "Korea", "Seoul", "Birth", 1994);
        token1 = new AuthToken("ckd83kd8", "inohpak");
        db = new Database();
        db.openConnection();
        db.clearTables();
        db.createTables();
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        eventService = null;
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void eventServiceTestPass() throws Exception {

        Database db = new Database();

        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);

        try {
            Connection connection = db.openConnection();
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            PersonDAO personDAO = new PersonDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
            EventDAO eventDAO = new EventDAO(connection);

            personDAO.insertPerson(person1);
            tokenDAO.insertAuthToken(token1);
            userDAO.insertUser(user1);
            eventDAO.insertEvent(event1);
            db.closeConnection(true);

            EventResult compareResult = new EventResult();
            compareResult.setData(events);

            EventResult result = eventService.getAllEvents(token1.getAuthToken());

            Event event = result.getData().get(0);

            assertEquals(events.get(0), event);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void eventServiceTestFail() throws Exception {

        Database db = new Database();

        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);

        try {
            Connection connection = db.openConnection();
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            PersonDAO personDAO = new PersonDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
            EventDAO eventDAO = new EventDAO(connection);

            personDAO.insertPerson(person1);
            tokenDAO.insertAuthToken(token1);
            userDAO.insertUser(user1);
            eventDAO.insertEvent(event1);
            db.closeConnection(true);

            EventResult compareResult = new EventResult();
            compareResult.setData(events);

            EventResult result = eventService.getAllEvents("invalidToken");

            assertNotEquals(result.getData(), events);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}
