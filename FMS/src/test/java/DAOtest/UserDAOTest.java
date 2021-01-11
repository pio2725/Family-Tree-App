package DAOtest;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private Database db;
    private User bestUser;
    private User newUser;

    @BeforeEach
    public void SetUp() throws Exception {
        db = new Database();
        bestUser = new User("inohpak", "inoh123", "pi@gmail.com",
                    "inoh", "pak", "m", "ejidfkj12345");
        newUser = new User("Jason", "jason123", "jason@gmail.com", "Jason", "Pak", "m", "jaefj123");
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
        User compareUser = null;

        try {
            Connection connection = db.openConnection();
            UserDAO uDAO = new UserDAO(connection);

            uDAO.insertUser(bestUser);
            compareUser = uDAO.findUserByName(bestUser.getUserName());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareUser);
        assertEquals(bestUser, compareUser);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection connection = db.openConnection();
            UserDAO uDAO = new UserDAO(connection);
            uDAO.insertUser(bestUser);
            uDAO.insertUser(bestUser);
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }

        assertFalse(didItWork);

        User compareUser = bestUser;
        try {
            Connection connection = db.openConnection();
            UserDAO uDAO = new UserDAO(connection);
            compareUser = uDAO.findUserByName(bestUser.getUserName());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareUser);
    }

    @Test
    public void findByUserNamePass() throws Exception {
        User compareUser = null;

        try {
            Connection connection = db.openConnection();
            UserDAO uDAO = new UserDAO(connection);

            uDAO.insertUser(bestUser);
            compareUser = uDAO.findUserByName(bestUser.getUserName());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareUser);
        assertEquals(bestUser, compareUser);
    }

    @Test
    public void findByUserNameFail() throws Exception {
        User compareUser = null;

        try {
            Connection connection = db.openConnection();
            UserDAO uDAO = new UserDAO(connection);
            uDAO.insertUser(bestUser);
            compareUser = uDAO.findUserByName("Inohpak");

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareUser);
        assertNotEquals(bestUser, compareUser);
    }

    @Test
    public void clearTablePass() throws Exception {

        boolean didItWork = false;
        try {
            Connection connection = db.openConnection();
            UserDAO uDao = new UserDAO(connection);
            uDao.insertUser(bestUser);
            uDao.insertUser(newUser);
            uDao.clearUserTable(); // clear user table

            didItWork = true;

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertTrue(didItWork);
    }

    @Test
    public void clearTableFail() throws Exception {
        User compareUser = null;

        try {
            Connection connection = db.openConnection();
            UserDAO uDao = new UserDAO(connection);
            uDao.insertUser(bestUser);
            uDao.clearUserTable();
            compareUser = uDao.findUserByName(bestUser.getUserName());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareUser);
    }

    @Test
    public void deleteDataPass() throws Exception {

        User compareUser1 = null;
        User compareUser2 = null;

        try {
            Connection connection = db.openConnection();
            UserDAO uDao = new UserDAO(connection);
            uDao.insertUser(bestUser);
            uDao.insertUser(newUser);

            uDao.deleteData(newUser.getUserName());

            compareUser1 = uDao.findUserByName(bestUser.getUserName());
            compareUser2 = uDao.findUserByName(newUser.getUserName());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareUser1);
        assertNull(compareUser2);
        assertEquals(compareUser1, bestUser);
        assertNotEquals(compareUser2, newUser);
    }

    @Test
    public void deleteDataFail() throws Exception {

        User compareUser1 = null;
        User compareUser2 = null;

        try {
            Connection connection = db.openConnection();
            UserDAO uDao = new UserDAO(connection);
            uDao.insertUser(bestUser);

            uDao.deleteData(newUser.getUserName());

            compareUser1 = uDao.findUserByName(bestUser.getUserName());
            compareUser2 = uDao.findUserByName(newUser.getUserName());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareUser1);
        assertNull(compareUser2);
        assertEquals(compareUser1, bestUser);
        assertNotEquals(compareUser2, newUser);


    }

    @Test
    public void deleteData() throws Exception {

        User user1 = new User("inohpak", "dlsdh123", "pio2725@gmail.com", "inoh", "Pak", "m", "asdf123");
        Person person1 = new Person("asdf123", "inohpak", "inoh", "Pak", "m", "afjdkl1", "dkdil2", "fkdji4");
        Person person2 = new Person("asdf456", "inohpak", "inoh", "Pak", "m", "afjdkl1", "dkdil2", "fkdji4");
        Person person3 = new Person("asdf678", "inohpak", "inoh", "Pak", "m", "afjdkl1", "dkdil2", "fkdji4");

        Event event1 = new Event("eventID123", "inohpak", "asdf123", 33.33f, 25.12f, "Korea", "Seoul", "Birth", 1994);
        AuthToken token = new AuthToken("ckd83kd8", "inohpak");

        User compareUser = null;
        Person comPerson1 = null;
        Person comPerson2 = null;
        Person comPerson3 = null;
        Event comEvent = null;
        AuthToken comToken = null;

        try {
            Connection connection = db.openConnection();
            UserDAO userDAO = new UserDAO(connection);
            PersonDAO personDAO = new PersonDAO(connection);
            EventDAO eventDAO = new EventDAO(connection);
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);

            userDAO.insertUser(user1);
            personDAO.insertPerson(person1);
            eventDAO.insertEvent(event1);
            tokenDAO.insertAuthToken(token);

            userDAO.deleteData(user1.getUserName());
            userDAO.deleteData(user1.getUserName());

            personDAO.deleteData(user1.getUserName());
            //eventDAO.deleteData(user1.getUserName());
            tokenDAO.deleteData(user1.getUserName());

            compareUser = userDAO.findUserByName(user1.getUserName());
            comPerson1 = personDAO.findPerson(person1.getPersonID());
            comPerson2 = personDAO.findPerson(person2.getPersonID());
            comPerson3 = personDAO.findPerson(person3.getPersonID());
            comEvent = eventDAO.findEvent(event1.getEventID());
            comToken = tokenDAO.findAuthToken(token.getAuthToken());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareUser);
        assertNull(comPerson1);
        assertNull(comPerson2);
        assertNull(comPerson3);
        assertNotNull(comEvent);
        assertNull(comToken);

    }

    @Test
    public void loginValidPass() throws Exception {

        boolean isValid = false;

        try {
            Connection connection = db.openConnection();
            UserDAO userDAO = new UserDAO(connection);
            userDAO.insertUser(bestUser);

            isValid = userDAO.loginAuthentication(bestUser);
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertTrue(isValid);
    }

    @Test
    public void loginValidFail() throws Exception {

        boolean isValid = false;

        try {
            Connection connection = db.openConnection();
            UserDAO userDAO = new UserDAO(connection);
            userDAO.insertUser(bestUser);

            bestUser.setPassword("inoh");
            isValid = userDAO.loginAuthentication(bestUser);
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertFalse(isValid);
    }

}
