package DAOtest;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {

    private Database db;
    private AuthToken bestToken;
    private AuthToken newToken;

    @BeforeEach
    public void Setup() throws Exception {
        db = new Database();
        bestToken = new AuthToken("asdf123", "inohpak");
        newToken = new AuthToken("jkl;123", "dean");
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
        AuthToken compareToken = null;
        try {
            Connection connection = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(connection);
            aDao.insertAuthToken(bestToken);
            compareToken = aDao.findAuthToken(bestToken.getAuthToken());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareToken);
        assertEquals(bestToken,compareToken);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection connection = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(connection);
            aDao.insertAuthToken(bestToken);
            aDao.insertAuthToken(bestToken);

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }

        assertFalse(didItWork);

        AuthToken compareToken = bestToken;
        try {
            Connection connection = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(connection);
            compareToken = aDao.findAuthToken(bestToken.getAuthToken());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareToken);
    }

    @Test
    public void clearTablePass() throws Exception {
        boolean didItWork = false;
        try {
            Connection connection = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(connection);
            aDao.insertAuthToken(bestToken);
            aDao.insertAuthToken(newToken);
            aDao.clearAuthTokenTable();

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
        AuthToken compareToken = null;
        try {
            Connection connection = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(connection);
            aDao.insertAuthToken(bestToken);
            aDao.clearAuthTokenTable();
            compareToken = aDao.findAuthToken(bestToken.getAuthToken());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareToken);
    }

    @Test
    public void deleteDataPass() throws Exception {
        AuthToken compareToken1 = null;
        AuthToken compareToken2 = null;
        AuthToken compareToken3 = null;

        try {
            Connection connection = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(connection);
            AuthToken token = new AuthToken("asfesdf", newToken.getUserName());
            aDao.insertAuthToken(bestToken);
            aDao.insertAuthToken(newToken);
            aDao.insertAuthToken(token);

            //delete token and new token both
            aDao.deleteData(newToken.getUserName());

            compareToken1 = aDao.findAuthToken(bestToken.getAuthToken());
            compareToken2 = aDao.findAuthToken(newToken.getAuthToken());
            compareToken3 = aDao.findAuthToken(token.getAuthToken());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareToken1);
        assertNull(compareToken2);
        assertNull(compareToken3);
        assertEquals(compareToken1, bestToken);
        assertNotEquals(compareToken2, newToken);
    }

    @Test
    public void deleteDataFail() throws Exception {
        AuthToken compareToken1 = null;
        AuthToken compareToken2 = null;

        try {
            Connection connection = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(connection);
            aDao.insertAuthToken(bestToken);
            aDao.deleteData(newToken.getUserName());

            compareToken1 = aDao.findAuthToken(bestToken.getAuthToken());
            compareToken2 = aDao.findAuthToken(newToken.getAuthToken());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareToken1);
        assertNull(compareToken2);
        assertEquals(compareToken1, bestToken);
        assertNotEquals(compareToken2, newToken);
    }

    @Test
    public void findTokenPass() throws Exception {

        AuthToken compareToken = null;

        try {
            Connection connection = db.openConnection();
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            tokenDAO.insertAuthToken(bestToken);
            compareToken = tokenDAO.findAuthToken(bestToken.getAuthToken());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareToken);
        assertEquals(bestToken, compareToken);
    }

    @Test
    public void findTokenFail() throws Exception {
        AuthToken compareToken = null;

        try {
            Connection connection = db.openConnection();
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            tokenDAO.insertAuthToken(bestToken);
            compareToken = tokenDAO.findAuthToken("eislaj");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareToken);
    }

    @Test
    public void isValidTokenPass() throws Exception {

        boolean isValid = false;

        try {
            Connection connection = db.openConnection();
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            tokenDAO.insertAuthToken(bestToken);

            isValid = tokenDAO.isValidAuthToken(bestToken.getAuthToken());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertTrue(isValid);
    }

    @Test
    public void isValidTokenFail() throws Exception {

        boolean isValid = false;

        try {
            Connection connection = db.openConnection();
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            tokenDAO.insertAuthToken(bestToken);

            isValid = tokenDAO.isValidAuthToken("asdfasdf");

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertFalse(isValid);
    }

}
