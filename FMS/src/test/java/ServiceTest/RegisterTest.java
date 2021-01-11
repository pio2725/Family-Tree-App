package ServiceTest;

import DAO.Database;
import Model.User;
import Reqeust.RegisterRequest;
import Result.RegisterResult;
import Service.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RegisterTest {

    private RegisterService registerService;
    private Database db;
    private User user1;

    @BeforeEach
    public void setUp() throws Exception {
        registerService = new RegisterService();
        user1 = new User("inohpak", "dlsdh123", "pio2725@gmail.com", "inoh", "Pak", "m", "asdf123");

        db = new Database();
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        registerService = null;
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void RegisterTestPass() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest("inohpak", "dlsdh123", "dlsdh@gmail.com", "inoh", "Pak", "m");

        RegisterResult compareResult = new RegisterResult();
        compareResult.setUserName("inohpak");

        RegisterResult registerResult = registerService.register(registerRequest);
        compareResult.setAuthToken(registerResult.getAuthToken());
        compareResult.setPersonID(registerResult.getPersonID());

        assertEquals(registerResult.getAuthToken(), compareResult.getAuthToken());
        assertEquals(registerResult.getUserName(), compareResult.getUserName());
        assertEquals(registerResult.getPersonID(), compareResult.getPersonID());
    }

    @Test
    public void RegisterTestFail() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest("inohpak", "dlsdh123", "dlsdh@gmail.com", "inoh", "Pak", "m");
        RegisterRequest registerRequest2 = new RegisterRequest("inohpak", "dlsdh123", "dlsdh@gmail.com", "inoh", "Pak", "m");

        RegisterResult result = registerService.register(registerRequest);
        RegisterResult result2 = registerService.register(registerRequest2);

        assertEquals("error username already taken", result2.getMessage());
    }

}
