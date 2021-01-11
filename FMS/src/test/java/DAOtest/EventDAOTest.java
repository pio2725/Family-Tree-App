package DAOtest;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {

    private Database db;
    private Event bestEvent;
    private Event newEvent;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        bestEvent = new Event("Biking_123A", "inohpak", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        newEvent = new Event("inoh1234", "inohpak", "inohpak123",
                10.3f, 10.3f, "Korea", "Ushiku",
                "hahaha", 2016);
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws Exception {
        Event compareTest = null;

        try {
            Connection connection = db.openConnection();
            EventDAO eDAO = new EventDAO(connection);

            eDAO.insertEvent(bestEvent);
            compareTest = eDAO.findEvent(bestEvent.getEventID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection connection = db.openConnection();
            EventDAO eDao = new EventDAO(connection);
            eDao.insertEvent(bestEvent);
            eDao.insertEvent(bestEvent); // should fail
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }

        assertFalse(didItWork);

        Event compareTest = bestEvent;
        try {
            Connection connection = db.openConnection();
            EventDAO eDao = new EventDAO(connection);
            compareTest = eDao.findEvent(bestEvent.getEventID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareTest);
    }

    @Test
    public void findEventPass() throws Exception {

        Event compareEvent = null;

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);
            eventDAO.insertEvent(bestEvent);
            compareEvent = eventDAO.findEvent(bestEvent.getEventID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotNull(compareEvent);
        assertEquals(compareEvent, bestEvent);
    }

    @Test
    public void findEventFail() throws Exception {

        Event compareEvent = null;

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);
            eventDAO.insertEvent(bestEvent);
            compareEvent = eventDAO.findEvent("inohinoh");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareEvent);
    }

    @Test
    public void getAllEventPass() throws  Exception {

        ArrayList<Event> compareEvents = new ArrayList<>();
        ArrayList<Event> events = new ArrayList<>();
        compareEvents.add(bestEvent);
        compareEvents.add(newEvent);

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);
            AuthToken token = new AuthToken("asdf", "inohpak");
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);

            tokenDAO.insertAuthToken(token);
            eventDAO.insertEvent(bestEvent);
            eventDAO.insertEvent(newEvent);

            events = eventDAO.getAllFamilyEvents(token.getAuthToken());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertEquals(events.get(0), compareEvents.get(0));
        assertEquals(events.get(1), compareEvents.get(1));
        assertEquals(events.size(), compareEvents.size());
    }

    @Test
    public void getAllEventFail() throws Exception {

        ArrayList<Event> compareEvents = new ArrayList<>();
        ArrayList<Event> events = new ArrayList<>();
        compareEvents.add(bestEvent);
        compareEvents.add(newEvent);

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);
            AuthToken token = new AuthToken("asdf", "inohpak");
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);

            tokenDAO.insertAuthToken(token);
            eventDAO.insertEvent(bestEvent);

            events = eventDAO.getAllFamilyEvents("asdf");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotEquals(events, compareEvents);
    }

    @Test
    public void isValidEventPass() throws Exception {

        boolean isValid = false;

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);
            eventDAO.insertEvent(bestEvent);

            isValid = eventDAO.isValidEvent(bestEvent.getEventID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertTrue(isValid);
    }

    @Test
    public void isValidEventFail() throws Exception {

        boolean isValid = false;

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);
            eventDAO.insertEvent(bestEvent);

            isValid = eventDAO.isValidEvent("hello");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertFalse(isValid);
    }

    @Test
    public void createParentDataPass() throws Exception {

        Person mother;
        Person father;
        boolean isValid = true;

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);

            mother = new Person("oh123", "inohpak", "heui", "chung", "f", "sungho", "mm","mm");
            father = new Person("on123", "inohpak", "sung", "ho", "m", "sungho", "mm","mm");
            int birthYear = eventDAO.createParentsEvents(mother, father, 1994);
            if (1994 - birthYear > 35) {
                isValid = false;
            }
            db.closeConnection(true);

        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertTrue(isValid);
    }

    @Test
    public void createParentEventFail() throws Exception {

        Person mother;
        Person father;
        boolean isValid = true;

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);

            mother = new Person("oh123", "inohpak", "heui", "chung", "f", "sungho", "mm","mm");
            father = new Person("on123", "inohpak", "sung", "ho", "m", "sungho", "mm","mm");
            int birthYear = eventDAO.createParentsEvents(mother, father, 1994);
            int birthYear2 = eventDAO.createParentsEvents(mother, father, birthYear);
            if (birthYear - birthYear2 > 35) {
                isValid = false;
            }
            db.closeConnection(true);

        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertTrue(isValid);
    }

    @Test
    public void createPersonEventPass() throws Exception {

        boolean isValid = false;

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);
            PersonDAO personDAO = new PersonDAO(connection);
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);

            eventDAO.insertEvent(bestEvent);

            Person person = new Person("oh123", "inohpak", "heui", "chung", "f", "sungho", "mm","mm");
            AuthToken token = new AuthToken("asdf", "inohpak");
            tokenDAO.insertAuthToken(token);
            personDAO.insertPerson(person);

            int birthYear = eventDAO.createThisPersonEvents(person);
            ArrayList<Event> events = eventDAO.getAllFamilyEvents("asdf");
            System.out.println(events.size());
            if (events.size() == 4) {
                isValid = true;
            }
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertTrue(isValid);
    }

    @Test
    public void createPersonEventFail() throws Exception {

        boolean isValid = false;

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);
            PersonDAO personDAO = new PersonDAO(connection);
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);

            eventDAO.insertEvent(bestEvent);

            Person person = new Person("oh123", "inohpak", "heui", "chung", "f", "sungho", "mm","mm");
            AuthToken token = new AuthToken("asdf", "inohpak");
            tokenDAO.insertAuthToken(token);
            personDAO.insertPerson(person);

            int birthYear = eventDAO.createThisPersonEvents(person);
            ArrayList<Event> events = eventDAO.getAllFamilyEvents("asdf");
            System.out.println(events.size());
            if (events.size() != 4) {
                isValid = true;
            }
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertFalse(isValid);
    }

    @Test
    public void clearEventTablePass() throws Exception {

        boolean didItWork = false;

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);
            eventDAO.insertEvent(bestEvent);
            eventDAO.clearEventTable();
            db.closeConnection(true);
            didItWork = true;
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertTrue(didItWork);
    }

    @Test
    public void clearEventTableFail() throws Exception {

        Event compareEvent = null;

        try {
            Connection connection = db.openConnection();
            EventDAO eventDAO = new EventDAO(connection);
            eventDAO.insertEvent(bestEvent);
            eventDAO.clearEventTable();
            compareEvent = eventDAO.findEvent(bestEvent.getEventID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareEvent);
    }

}
