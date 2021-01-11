package DAO;

import Data.RandomFnames;
import Data.RandomMnames;
import Data.RandomSnames;
import Model.AuthToken;
import Model.Person;
import Model.RandomGenerator;

import java.sql.*;
import java.util.ArrayList;

/** Interacting between the database and Person data */
public class PersonDAO {

    /** Connection to the database */
    private Connection connection;

    /** object of random female names */
    private RandomFnames randomFnames;

    /** object of random male names */
    private RandomMnames randomMnames;

    /** object of random last names */
    private RandomSnames randomSnames;

    /** Make connection with the given connection to the database
     *  and create new random name objects
     *  @param connection connection given for the database connection */
    public PersonDAO(Connection connection) {
        randomFnames = new RandomFnames();
        randomMnames = new RandomMnames();
        randomSnames = new RandomSnames();
        this.connection = connection;
    }

    /** Create new DAO person object with random name objects */
    public PersonDAO() {
        randomFnames = new RandomFnames();
        randomMnames = new RandomMnames();
        randomSnames = new RandomSnames();
    }

    /** Set connection with the given connection
     * @param connection the given connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /** Insert person into the database
     *  @param person the person given to insert to the database
     *  @throws DataAccessException throws an exception if an error occurs
     */
    public void insertPerson(Person person) throws DataAccessException {

        String sql = "insert into persons (personID, associatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement stmt = null;

            try {
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, person.getPersonID());
                stmt.setString(2, person.getAssociatedUserName());
                stmt.setString(3, person.getFirstName());
                stmt.setString(4, person.getLastName());
                stmt.setString(5, person.getGender());
                stmt.setString(6, person.getFatherID());
                stmt.setString(7, person.getMotherID());
                stmt.setString(8, person.getSpouseID());

                stmt.executeUpdate();
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while inserting person");
        }
    }

    /** Find person with the given person ID and return Person
     *  @param personID the unique person ID
     *  @throws DataAccessException throws an exception if an error occurs
     *  @return return Person if found in the database, or null if not
     */
    public Person findPerson(String personID) throws DataAccessException {

        Person person;

        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                String sql = "select * from persons where personID = ?";

                stmt = connection.prepareStatement(sql);
                stmt.setString(1, personID);

                rs = stmt.executeQuery();

                if (rs.next()) {
                    person = new Person(rs.getString("personID"), rs.getString("associatedUsername"), rs.getString("firstName"), rs.getString("lastName"),
                            rs.getString("gender"), rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                    return person;
                }
            }
            finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while finding person");
        }
        return null;
    }

    /** Find the user with the auth token and return the list of all family members
     *  @param accessToken the unique token to find the user
     *  @throws DataAccessException throws an exception if an error occurs
     *  @return return the list of Person that the user has
     */
    public ArrayList<Person> getAllFamilyMembers(String accessToken) throws DataAccessException {

        AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
        AuthToken token = tokenDAO.findAuthToken(accessToken);

        ArrayList<Person> personList = new ArrayList<>();

        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                String sql = "select * from persons where associatedUsername = ?";

                stmt = connection.prepareStatement(sql);
                stmt.setString(1, token.getUserName());
                rs = stmt.executeQuery();

                while (rs.next()) {

                    Person person = new Person();
                    person.setPersonID(rs.getString(1));
                    person.setAssociatedUserName(rs.getString(2));
                    person.setFirstName(rs.getString(3));
                    person.setLastName(rs.getString(4));
                    person.setGender(rs.getString(5));
                    person.setFatherID(rs.getString(6));
                    person.setMotherID(rs.getString(7));
                    person.setSpouseID(rs.getString(8));

                    personList.add(person);
                }
            }
            finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while getting families");
        }
        return personList;
    }

    /** generate generations with the given number of generations by user
     * @param numGenerations given number to generate generations
     * @param thisPerson generate generations starting this person
     * @param birthYear this person's birth year
     * @param eventDAO given connection to events table
     * @throws DataAccessException throws an error
     */
    public void generateDefaultGenerations(int numGenerations, Person thisPerson, int birthYear, EventDAO eventDAO) throws DataAccessException {

        Person mother = makeMother(thisPerson);
        Person father = makeFather(thisPerson);
        gettingMarried(mother, father.getPersonID());
        gettingMarried(father, mother.getPersonID());

        //generate event for parents
        int parentBirthDate = eventDAO.createParentsEvents(mother, father, birthYear);

        numGenerations--;
        if (numGenerations > 0) {
            generateDefaultGenerations(numGenerations, mother, parentBirthDate, eventDAO);
            generateDefaultGenerations(numGenerations, father, parentBirthDate, eventDAO);
        }
    }

    /** Find if the given personID is valid in the database
     * @param personID the given personID
     * @return return true if personID is valid
     * @throws DataAccessException throws an error
     */
    public boolean isValidPersonID(String personID) throws DataAccessException {

        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                String sql = "select * from persons where personID = ?";

                stmt = connection.prepareStatement(sql);
                stmt.setString(1, personID);
                rs = stmt.executeQuery();

                if (!rs.next()) {
                    throw new DataAccessException("error invalid personID");
                }
                else {
                    return true;
                }
            }
            finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while validating personID");
        }
    }

    /** make the person get married to another
     * @param person this person is getting married
     * @param spouseID set spouseID
     * @throws DataAccessException throws an error
     */
    public void gettingMarried(Person person, String spouseID) throws DataAccessException {

        try {
            Statement stmt = null;

            try {
                String sql = "update persons\n" + "set spouseID = '" + spouseID + "'" +
                        " where personID = '" + person.getPersonID() + "'";
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while getting married");
        }
    }

    /** make a new father person model an insert as a person into the database
     * @param thisPerson need to connect with the associated name
     * @return return new father model
     * @throws DataAccessException throws an error
     */
    public Person makeFather(Person thisPerson) throws DataAccessException {

        Person newFather = new Person();

        String fatherID = new RandomGenerator().getRandomString();
        newFather.setPersonID(fatherID);
        newFather.setAssociatedUserName(thisPerson.getAssociatedUserName());
        newFather.setFirstName(randomMnames.getRandomMnames());
        newFather.setLastName(thisPerson.getLastName());
        newFather.setGender("m");

        insertFather(thisPerson, fatherID);

        insertPerson(newFather);

        return newFather;

    }

    /** insert a new father into the database
     * @param thisPerson new father
     * @param fatherID new father's personID
     * @throws DataAccessException throws an error
     */
    public void insertFather(Person thisPerson, String fatherID) throws DataAccessException {

        try {
            Statement stmt = null;

            try {
                String sql = "update persons\n" + "set fatherID = '" + fatherID + "'" +
                        " where personID = '" + thisPerson.getPersonID() + "'";
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while inserting father");
        }
    }

    /** make a new model Person mother and insert as a person into the database
     * @param thisPerson need to connect with the associated name
     * @return return new mother model
     * @throws DataAccessException throws an error
     */
    public Person makeMother(Person thisPerson) throws DataAccessException {

        Person newMother = new Person();

        String motherID = new RandomGenerator().getRandomString();

        newMother.setPersonID(motherID);
        newMother.setAssociatedUserName(thisPerson.getAssociatedUserName());
        newMother.setFirstName(randomFnames.getRandomFnames());
        newMother.setLastName(randomSnames.getRandomSnames());
        newMother.setGender("f");

        insertMother(thisPerson, motherID);

        insertPerson(newMother);

        return newMother;
    }

    /** insert a new mother into the database
     * @param thisPerson new mother
     * @param motherID new mother's personID
     * @throws DataAccessException throws an error when it occurs
     */
    public void insertMother(Person thisPerson, String motherID) throws DataAccessException {

        try {
            Statement stmt = null;

            try {
                String sql = "update persons\n" + "set motherID = '" + motherID + "'" +
                        " where personID = '" + thisPerson.getPersonID() + "'";
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while inserting mother");
        }
    }

    /** Delete everything in the person table
     *  @throws DataAccessException throws an exception when an error occurs
     */
    public void clearPersonTable() throws DataAccessException {
        try (Statement stmt = connection.createStatement()) {
            String sql = "delete from persons";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while clearing persons table");
        }
    }

    /** Delete everything in the table associated with a certain username
     *  @param userName the username to delete data
     *  @throws DataAccessException throws an exception if an error occurs
     */
    public void deleteData(String userName) throws DataAccessException {

        try {
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                stmt.executeUpdate("delete from persons where associatedUsername = '" + userName + "'");
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while deleting persons");
        }
    }
}
