package ServiceTest;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Result.PersonResult;
import Service.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PersonServiceTest {

    private PersonService personService;
    private User user1;
    private Person person1;
    private Event event1;
    private AuthToken token1;
    private Database db;

    @BeforeEach
    public void setUp() throws Exception {
        personService = new PersonService();
        user1 = new User("inohpak", "dlsdh123", "pio2725@gmail.com", "inoh", "Pak", "m", "jiji123");
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
        personService = null;
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void personServiceTestPass() throws Exception {

        Database db = new Database();

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(person1);

        try {
            Connection connection = db.openConnection();
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            PersonDAO personDAO = new PersonDAO(connection);
            UserDAO userDAO = new UserDAO(connection);

            personDAO.insertPerson(person1);
            tokenDAO.insertAuthToken(token1);
            userDAO.insertUser(user1);
            db.closeConnection(true);

            PersonResult compareResult = new PersonResult();
            compareResult.setData(persons);

            PersonResult result = personService.getFamilyMembers(token1.getAuthToken());

            Person person = result.getData().get(0);

            assertEquals(persons.get(0), person);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void personServiceTestFail() throws Exception {

        Database db = new Database();

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(person1);

        try {
            Connection connection = db.openConnection();
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            PersonDAO personDAO = new PersonDAO(connection);
            UserDAO userDAO = new UserDAO(connection);

            personDAO.insertPerson(person1);
            tokenDAO.insertAuthToken(token1);
            userDAO.insertUser(user1);
            db.closeConnection(true);

            PersonResult compareResult = new PersonResult();
            compareResult.setData(persons);

            PersonResult result = personService.getFamilyMembers("invalidToken");

            //Person person = result.getData().get(0);

            assertNotEquals(result.getData(), persons);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }




}
