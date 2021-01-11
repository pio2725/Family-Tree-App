package DAOtest;

import DAO.*;
import Model.AuthToken;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {

    private Database db;
    private Person bestPerson;
    private Person newPerson;

    @BeforeEach
    public void SetUp() throws Exception {
        db = new Database();
        bestPerson = new Person("inoh123", "inohpak", "inoh", "pak", "m", "sungho", "mm","mm");
        newPerson = new Person("hello123", "hihihi", "hell", "lo", "f", "sungho", "","");

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
        Person comparePerson = null;
        try {
            Connection connection = db.openConnection();
            PersonDAO pDao = new PersonDAO(connection);
            pDao.insertPerson(bestPerson);
            comparePerson = pDao.findPerson(bestPerson.getPersonID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(comparePerson);
        assertEquals(bestPerson, comparePerson);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection connection = db.openConnection();
            PersonDAO pDao = new PersonDAO(connection);
            pDao.insertPerson(bestPerson);
            pDao.insertPerson(bestPerson);
            db.closeConnection(true);
        }
        catch(DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);

        Person comparePerson = bestPerson;
        try {
            Connection connection = db.openConnection();
            PersonDAO pDao = new PersonDAO(connection);
            comparePerson = pDao.findPerson(bestPerson.getPersonID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(comparePerson);
    }

    @Test
    public void findByPersonIDPass() throws Exception {
        Person comparePerson = null;

        try {
            Connection connection = db.openConnection();
            PersonDAO pDao = new PersonDAO(connection);
            pDao.insertPerson(bestPerson);
            comparePerson = pDao.findPerson(bestPerson.getPersonID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotNull(comparePerson);
        assertEquals(comparePerson, bestPerson);
    }

    @Test
    public void findByPersonIDFail() throws Exception {
        Person comparePerson = null;
        try {
            Connection connection = db.openConnection();
            PersonDAO pDao = new PersonDAO(connection);
            pDao.insertPerson(bestPerson);
            comparePerson = pDao.findPerson("inoh12");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(comparePerson);
    }

    @Test
    public void clearTablePass() throws Exception {
        boolean didItWork = false;
        try {
            Connection connection = db.openConnection();
            PersonDAO pDao = new PersonDAO(connection);
            pDao.insertPerson(bestPerson);
            pDao.clearPersonTable();
            db.closeConnection(true);
            didItWork = true;
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertTrue(didItWork);
    }

    @Test
    public void clearTableFail() throws Exception {
        Person comparePerson = null;

        try {
            Connection connection = db.openConnection();
            PersonDAO pDao = new PersonDAO(connection);
            pDao.insertPerson(bestPerson);
            pDao.clearPersonTable();
            comparePerson = pDao.findPerson(bestPerson.getPersonID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(comparePerson);
    }

    @Test
    public void deleteDataPass() throws Exception {

        Person comparePerson = null;

        try {
            Connection connection = db.openConnection();
            PersonDAO pDao = new PersonDAO(connection);
            Person person = bestPerson;
            person.setFirstName("inohasdf");

            pDao.insertPerson(bestPerson);
            pDao.insertPerson(person);

            //delete token and new token both
            pDao.deleteData(bestPerson.getAssociatedUserName());

            comparePerson = pDao.findPerson(bestPerson.getPersonID());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(comparePerson);
        assertNotEquals(comparePerson, bestPerson);
    }

    @Test
    public void deleteDataFail() throws Exception {

        Person comparePerson1 = null;
        Person comparePerson2 = null;

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);

            personDAO.insertPerson(bestPerson);
            personDAO.insertPerson(newPerson);

            personDAO.deleteData(newPerson.getAssociatedUserName());

            comparePerson1 = personDAO.findPerson(bestPerson.getPersonID());
            comparePerson2 = personDAO.findPerson(newPerson.getPersonID());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(comparePerson1);
        assertNull(comparePerson2);
        assertEquals(comparePerson1, bestPerson);
        assertNotEquals(comparePerson2, newPerson);
    }

    @Test
    public void getAllfamilyPass() throws Exception {

        Person bestPerson2 = new Person("efsdfwefsd", "inohpak", "inoh", "pak", "m", "sungho", "","");

        ArrayList<Person> comparePersons = new ArrayList<>();
        ArrayList<Person> persons = new ArrayList<>();
        comparePersons.add(bestPerson);
        comparePersons.add(bestPerson2);

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            AuthToken token = new AuthToken("asdf", "inohpak");
            tokenDAO.insertAuthToken(token);

            personDAO.insertPerson(bestPerson);
            personDAO.insertPerson(bestPerson2);

            persons = personDAO.getAllFamilyMembers(token.getAuthToken());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertEquals(persons.get(0), comparePersons.get(0));
        assertEquals(persons.get(1), comparePersons.get(1));
        assertEquals(persons.size(), comparePersons.size());
    }

    @Test
    public void getAllFamilyFail() throws Exception {

        Person bestPerson2 = new Person("efsdfwefsd", "inohpa", "inoh", "pak", "m", "sungho", "","");

        ArrayList<Person> comparePersons = new ArrayList<>();
        ArrayList<Person> persons = new ArrayList<>();
        comparePersons.add(bestPerson);
        comparePersons.add(bestPerson2);

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);
            AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
            AuthToken token = new AuthToken("asdf", "inohpak");
            tokenDAO.insertAuthToken(token);

            personDAO.insertPerson(bestPerson);
            personDAO.insertPerson(bestPerson2);

            persons = personDAO.getAllFamilyMembers("asdf");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotEquals(persons, comparePersons);
    }

    @Test
    public void insertMotherPass() throws Exception {

        Person newMother = new Person("hello123", "inohpak", "Iam", "mother", "f", "sungho", "esd","fes");

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);

            personDAO.insertPerson(bestPerson);
            personDAO.insertMother(bestPerson, newMother.getPersonID());
            bestPerson = personDAO.findPerson(bestPerson.getPersonID());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertEquals(bestPerson.getMotherID(), newMother.getPersonID());
        assertEquals(bestPerson.getAssociatedUserName(), newMother.getAssociatedUserName());
    }

    @Test
    public void insertMotherFail() throws Exception {

        Person newMother = new Person("hello123", "inohpak", "Iam", "mother", "m", "sungho", "esd","fes");

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);

            personDAO.insertPerson(bestPerson);
            personDAO.insertMother(bestPerson, newMother.getPersonID());

            bestPerson = personDAO.findPerson("inohpak55");

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(bestPerson);
    }

    @Test
    public void insertFatherPass() throws Exception {

        Person newFather = new Person("hello123", "inohpak", "Iam", "mother", "f", "sungho", "esd","fes");

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);

            personDAO.insertPerson(bestPerson);
            personDAO.insertMother(bestPerson, newFather.getPersonID());
            bestPerson = personDAO.findPerson(bestPerson.getPersonID());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertEquals(bestPerson.getMotherID(), newFather.getPersonID());
        assertEquals(bestPerson.getAssociatedUserName(), newFather.getAssociatedUserName());
    }

    @Test
    public void insertFatherFail() throws Exception {

        Person newFather = new Person("hello123", "inohpak", "Iam", "mother", "m", "sungho", "esd","fes");

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);

            personDAO.insertPerson(bestPerson);
            personDAO.insertMother(bestPerson, newFather.getPersonID());

            bestPerson = personDAO.findPerson("inohpak55");

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(bestPerson);
    }

    @Test
    public void makeMotherPass() throws Exception {

        Person mother = null;

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);
            personDAO.insertPerson(bestPerson);

            mother = personDAO.makeMother(bestPerson);
            bestPerson = personDAO.findPerson(bestPerson.getPersonID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertEquals(mother.getAssociatedUserName(), bestPerson.getAssociatedUserName());
        assertEquals(bestPerson.getMotherID(), mother.getPersonID());
    }

    @Test
    public void makeMotherFail() throws Exception {

        Person mother = null;

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);
            personDAO.insertPerson(bestPerson);

            mother = personDAO.makeMother(bestPerson);
            bestPerson = personDAO.findPerson("inoh55");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(bestPerson);
        assertNotNull(mother);
    }

    @Test
    public void makeFatherPass() throws Exception {
        Person father = null;

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);
            personDAO.insertPerson(bestPerson);

            father = personDAO.makeMother(bestPerson);
            bestPerson = personDAO.findPerson(bestPerson.getPersonID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertEquals(father.getAssociatedUserName(), bestPerson.getAssociatedUserName());
        assertEquals(bestPerson.getMotherID(), father.getPersonID());
    }

    @Test
    public void makeFatherFail() throws Exception {
        Person father = null;

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);
            personDAO.insertPerson(bestPerson);

            father = personDAO.makeMother(bestPerson);
            bestPerson = personDAO.findPerson("inoh55");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(bestPerson);
        assertNotNull(father);
    }

    @Test
    public void gettingMarriedPass() throws Exception {

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);

            personDAO.insertPerson(bestPerson);
            personDAO.insertPerson(newPerson);

            personDAO.gettingMarried(bestPerson, newPerson.getPersonID());
            personDAO.gettingMarried(newPerson, bestPerson.getPersonID());

            bestPerson = personDAO.findPerson(bestPerson.getPersonID());
            newPerson = personDAO.findPerson(newPerson.getPersonID());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertEquals(bestPerson.getSpouseID(), newPerson.getPersonID());
        assertEquals(bestPerson.getPersonID(), newPerson.getSpouseID());
    }

    @Test
    public void gettringMarriedFail() throws Exception {
        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);
            personDAO.insertPerson(bestPerson);
            personDAO.insertPerson(newPerson);

            personDAO.gettingMarried(bestPerson, newPerson.getPersonID());

            bestPerson = personDAO.findPerson(bestPerson.getPersonID());
            newPerson = personDAO.findPerson(newPerson.getPersonID());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotEquals(bestPerson.getPersonID(), newPerson.getSpouseID());
    }

    @Test
    public void isValidPersonIDPass() throws Exception {

        boolean isValid = false;

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);
            personDAO.insertPerson(bestPerson);

            isValid = personDAO.isValidPersonID(bestPerson.getPersonID());

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertTrue(isValid);
    }

    @Test
    public void isValidPersonIDFail() throws Exception {

        boolean isValid = false;

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);
            personDAO.insertPerson(bestPerson);

            isValid = personDAO.isValidPersonID("iii");

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertFalse(isValid);
    }

    @Test
    public void generateGenerationsPass() throws Exception {

        Person father = null;
        Person mother = null;
        Person grandFather = null;
        Person grandMother = null;

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);
            EventDAO eventDAO = new EventDAO(connection);
            personDAO.insertPerson(bestPerson);

            personDAO.generateDefaultGenerations(4, bestPerson, 1994, eventDAO);
            bestPerson = personDAO.findPerson(bestPerson.getPersonID());

            father = personDAO.findPerson(bestPerson.getFatherID());
            mother = personDAO.findPerson(bestPerson.getMotherID());

            grandFather = personDAO.findPerson(father.getFatherID());
            grandMother = personDAO.findPerson(father.getMotherID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertEquals(bestPerson.getFatherID(), father.getPersonID());
        assertEquals(bestPerson.getMotherID(), mother.getPersonID());
        assertEquals(father.getFatherID(), grandFather.getPersonID());
        assertEquals(father.getMotherID(), grandMother.getPersonID());
    }

    @Test
    public void generateGenerationsFail() throws Exception {
        Person father = null;
        Person mother = null;
        Person grandFather = null;
        Person grandMother = null;

        try {
            Connection connection = db.openConnection();
            PersonDAO personDAO = new PersonDAO(connection);
            EventDAO eventDAO = new EventDAO(connection);
            personDAO.insertPerson(bestPerson);

            personDAO.generateDefaultGenerations(1, bestPerson, 1994, eventDAO);
            bestPerson = personDAO.findPerson(bestPerson.getPersonID());

            father = personDAO.findPerson(bestPerson.getFatherID());
            mother = personDAO.findPerson(bestPerson.getMotherID());

            grandFather = personDAO.findPerson(father.getFatherID());
            grandMother = personDAO.findPerson(father.getMotherID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(father);
        assertNotNull(mother);
        assertNull(grandFather);
        assertNull(grandMother);
    }
}
