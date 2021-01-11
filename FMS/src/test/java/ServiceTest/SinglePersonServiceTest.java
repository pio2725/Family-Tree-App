package ServiceTest;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Result.PersonResultSingle;
import Service.PersonServiceSingle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SinglePersonServiceTest {

    private PersonServiceSingle personServiceSingle;
    private User user1;
    private Person person1;
    private Event event1;
    private AuthToken token1;
    private Database db;

    @BeforeEach
    public void setUp() throws Exception {
        personServiceSingle = new PersonServiceSingle();
        user1 = new User("inohpak", "dlsdh123", "pio2725@gmail.com", "inoh", "Pak", "m", "asdf123");
        person1 = new Person("asdf123", "inohpak", "inoh", "Pak", "m", "afjdkl1", "dkdil2", "fkdji4");
        event1 = new Event("eventID123", "inohPak", "asdf123", 33.33f, 25.12f, "Korea", "Seoul", "Birth", 1994);
        token1 = new AuthToken("ckd83kd8", "inohpak");
        db = new Database();
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
        personServiceSingle = null;
    }

    @Test
    public void personSingleTestPass() throws Exception {

        try {

            Connection connection = db.openConnection();

            PersonDAO personDAO = new PersonDAO(connection);
            personDAO.insertPerson(person1);

            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            tokenDAO.insertAuthToken(token1);

            UserDAO userDAO = new UserDAO(connection);
            userDAO.insertUser(user1);

            db.closeConnection(true);

            PersonResultSingle compareResult = new PersonResultSingle(person1.getAssociatedUserName(), person1.getPersonID(), person1.getFirstName(), person1.getLastName(), person1.getGender(), person1.getFatherID(), person1.getMotherID(), person1.getSpouseID());

            PersonResultSingle result = personServiceSingle.getPerson(person1.getPersonID(), token1.getAuthToken());

            assertEquals(compareResult, result);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void personSingleTestFail() throws Exception {

        try {

            Connection connection = db.openConnection();

            PersonDAO personDAO = new PersonDAO(connection);
            personDAO.insertPerson(person1);

            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            tokenDAO.insertAuthToken(token1);

            UserDAO userDAO = new UserDAO(connection);
            userDAO.insertUser(user1);

            db.closeConnection(true);

            PersonResultSingle compareResult = new PersonResultSingle(person1.getAssociatedUserName(), person1.getPersonID(), person1.getFirstName(), person1.getLastName(), person1.getGender(), person1.getFatherID(), person1.getMotherID(), person1.getSpouseID());

            PersonResultSingle result = personServiceSingle.getPerson("wrongID", token1.getAuthToken());

            assertNotEquals(compareResult.getPersonID(), result.getPersonID());
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }





}
