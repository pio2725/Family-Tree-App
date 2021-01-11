package ServiceTest;

import DAO.Database;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Reqeust.LoadRequest;
import Result.LoadResult;
import Service.LoadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadTest {

    private LoadService loadService;
    private User user1;
    private Person person1;
    private Event  event1;
    private AuthToken token1;
    private Database db;

    @BeforeEach
    public void setUp() throws Exception {
        loadService = new LoadService();
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
        loadService = null;
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void loadTestPass() throws Exception {

        User[] users = new User[1];
        Person[] persons = new Person[1];
        Event[] events = new Event[1];

        users[0] = user1;
        persons[0] = person1;
        events[0] = event1;

        LoadRequest loadRequest = new LoadRequest(users, persons, events);

        LoadResult compareResult = new LoadResult();
        compareResult.setMessage("Successfully added " + 1 + " users, " + 1 +
                " persons, " + "and " + 1 + " events to the database");

        LoadResult loadResult = loadService.load(loadRequest);

        assertEquals(compareResult.getMessage(), loadResult.getMessage());
    }

    @Test
    public void loadTestFail() throws Exception {

        User[] users = new User[1];
        Person[] persons = new Person[1];
        Event[] events = new Event[1];

        users[0] = user1;
        persons[0] = person1;
        events[0] = event1;

        LoadRequest loadRequest = new LoadRequest(users, persons, events);

        LoadResult compareResult = new LoadResult();
        compareResult.setMessage("Successfully added " + 1 + " users, " + 1 +
                " persons, " + "and " + 1 + " events to the database");

        LoadResult loadResult = loadService.load(loadRequest);
        loadResult = loadService.load(loadRequest);

        assertEquals(compareResult.getMessage(), loadResult.getMessage());
    }


}
