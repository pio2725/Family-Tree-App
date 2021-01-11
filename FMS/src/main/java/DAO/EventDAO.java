package DAO;

import Data.RandomLocation;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.RandomGenerator;

import java.sql.*;
import java.util.ArrayList;

/** Accessing and interacting with Event data in the database */
public class EventDAO {

    /** Connection to the database */
    private Connection connection;

    /** Make connection with the given connection to the database
     *  @param connection connection given for the database connection */
    public EventDAO(Connection connection) {
        this.connection = connection;
    }

    /** Make an empty event connection */
    public EventDAO() {
    }

    /** Set connection with the given connection
     * @param connection the given connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /** Insert new Event into the database
     *  @param event the event that would be inserted to the database
     *  @throws SQLException throws exception if unable to open connection
     */
    public void insertEvent(Event event) throws DataAccessException {

        String sql = "insert into events (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement stmt = null;

            try {
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, event.getEventID());
                stmt.setString(2, event.getAssociatedUserName());
                stmt.setString(3, event.getPersonID());
                stmt.setFloat(4, event.getLatitude());
                stmt.setFloat(5, event.getLongitude());
                stmt.setString(6, event.getCountry());
                stmt.setString(7, event.getCity());
                stmt.setString(8, event.getEventType());
                stmt.setInt(9, event.getYear());

                stmt.executeUpdate();
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while inserting event");
        }
    }

    /** Find the event with the eventID from the database and return
     *  @param eventID the unique eventID to find Event in the database
     *  @throws DataAccessException throws exception if an error occured finding event
     *  @return return the event if it was found, null if not
     */
    public Event findEvent(String eventID) throws DataAccessException {

        Event event;

        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                String sql = "SELECT * FROM events WHERE eventID = ?";

                stmt = connection.prepareStatement(sql);
                stmt.setString(1, eventID);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"), rs.getString("personID"),
                            rs.getFloat("latitude"), rs.getFloat("longitude"), rs.getString("country"), rs.getString("city"),
                            rs.getString("eventType"), rs.getInt("year"));
                    return event;
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
            throw new DataAccessException("error while finding event");
        }
        return null;
    }

    /** Find all events with the unique token and return all events
     *  @param authToken the unique token of the user
     *  @return  return list of all the events for all family members of the user*/
    public ArrayList<Event> getAllFamilyEvents(String authToken) throws DataAccessException {

        AuthTokenDAO tokenDAO = new AuthTokenDAO(connection);
        AuthToken token;

        try {
            token = tokenDAO.findAuthToken(authToken);
        }
        catch (DataAccessException e) {
            throw new DataAccessException("error finding token");
        }

        ArrayList<Event> events = new ArrayList<>();

        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                String sql = "select * from events where associatedUsername = ?";

                stmt = connection.prepareStatement(sql);
                stmt.setString(1, token.getUserName());
                rs = stmt.executeQuery();

                while (rs.next()) {

                    Event event = new Event();
                    event.setEventID(rs.getString(1));
                    event.setAssociatedUserName(rs.getString(2));
                    event.setPersonID(rs.getString(3));
                    event.setLatitude(rs.getFloat(4));
                    event.setLongitude(rs.getFloat(5));
                    event.setCountry(rs.getString(6));
                    event.setCity(rs.getString(7));
                    event.setEventType(rs.getString(8));
                    event.setYear(rs.getInt(9));

                    events.add(event);
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
            e.printStackTrace();
        }
        return events;
    }

    /** Find if the given eventID is valid in the database
     * @param eventID the given eventID
     * @return return true if the given eventID is valid
     * @throws DataAccessException throws an error
     */
    public boolean isValidEvent(String eventID) throws DataAccessException {

        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                String sql = "select * from events where eventID = ?";

                stmt = connection.prepareStatement(sql);
                stmt.setString(1, eventID);
                rs = stmt.executeQuery();

                if (!rs.next()) {
                    throw new DataAccessException("error invalid eventID");
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
            throw new DataAccessException("error invalid event");
        }
    }

    /** Generating parents' event data (3 each)
     * @param mother given mother
     * @param father given father
     * @param thisPersonBirthYear root person's birth year
     * @return return birth year
     * @throws DataAccessException throws an error
     */
    public int createParentsEvents(Person mother, Person father, int thisPersonBirthYear) throws DataAccessException {

        Event birth = new Event();
        int parentBirthYear = thisPersonBirthYear - 35;

        //mom1
        birth.setEventID(new RandomGenerator().getRandomString());
        birth.setAssociatedUserName(mother.getAssociatedUserName());
        birth.setPersonID(mother.getPersonID());

        RandomLocation randomLocation = new RandomLocation().getRandomLocation();

        birth.setLatitude(randomLocation.getLatitude());
        birth.setLongitude(randomLocation.getLongitude());
        birth.setCountry(randomLocation.getCountry());
        birth.setCity(randomLocation.getCity());

        birth.setEventType("Birth");
        birth.setYear(parentBirthYear);

        insertEvent(birth);

        //dad1
        birth.setEventID(new RandomGenerator().getRandomString());
        birth.setAssociatedUserName(father.getAssociatedUserName());
        birth.setPersonID(father.getPersonID());

        randomLocation = randomLocation.getRandomLocation();

        birth.setLatitude(randomLocation.getLatitude());
        birth.setLongitude(randomLocation.getLongitude());
        birth.setCountry(randomLocation.getCountry());
        birth.setCity(randomLocation.getCity());

        birth.setEventType("Birth");
        birth.setYear(parentBirthYear);

        insertEvent(birth);

        //mom death
        Event death = new Event();

        death.setEventID(new RandomGenerator().getRandomString());
        death.setAssociatedUserName(mother.getAssociatedUserName());
        death.setPersonID(mother.getPersonID());

        randomLocation = randomLocation.getRandomLocation();

        death.setLatitude(randomLocation.getLatitude());
        death.setLongitude(randomLocation.getLongitude());
        death.setCountry(randomLocation.getCountry());
        death.setCity(randomLocation.getCity());
        death.setEventType("Death");
        death.setYear(parentBirthYear + 80);

        insertEvent(death);

        //dad death
        death.setEventID(new RandomGenerator().getRandomString());
        death.setAssociatedUserName(father.getAssociatedUserName());
        death.setPersonID(father.getPersonID());

        randomLocation = randomLocation.getRandomLocation();

        death.setLatitude(randomLocation.getLatitude());
        death.setLongitude(randomLocation.getLongitude());
        death.setCountry(randomLocation.getCountry());
        death.setCity(randomLocation.getCity());
        death.setEventType("Death");
        death.setYear(parentBirthYear + 80);

        insertEvent(death);

        //mom marriage
        Event marriage = new Event();

        marriage.setEventID(new RandomGenerator().getRandomString());
        marriage.setAssociatedUserName(mother.getAssociatedUserName());
        marriage.setPersonID(mother.getPersonID());

        randomLocation = randomLocation.getRandomLocation();

        marriage.setLatitude(randomLocation.getLatitude());
        marriage.setLongitude(randomLocation.getLongitude());
        marriage.setCountry(randomLocation.getCountry());
        marriage.setCity(randomLocation.getCity());
        marriage.setEventType("Marriage");
        marriage.setYear(parentBirthYear + 25);

        insertEvent(marriage);

        //dad marriage
        marriage.setEventID(new RandomGenerator().getRandomString());
        marriage.setAssociatedUserName(father.getAssociatedUserName());
        marriage.setPersonID(father.getPersonID());

        //randomLocation = randomLocation.getRandomLocation();

        marriage.setLatitude(randomLocation.getLatitude());
        marriage.setLongitude(randomLocation.getLongitude());
        marriage.setCountry(randomLocation.getCountry());
        marriage.setCity(randomLocation.getCity());
        marriage.setEventType("Marriage");
        marriage.setYear(parentBirthYear + 25);

        insertEvent(marriage);

        return parentBirthYear;
    }

    /** Create person's basic 3 events
     * @param thisPerson creating thisPerson's events
     * @return return person's birth year
     * @throws DataAccessException throws an error
     */
    public int createThisPersonEvents(Person thisPerson) throws DataAccessException {

        int birthYear = 1994;

        Event birth = new Event();
        RandomGenerator randomGenerator = new RandomGenerator();

        birth.setEventID(randomGenerator.getRandomString());
        birth.setAssociatedUserName(thisPerson.getAssociatedUserName());
        birth.setPersonID(thisPerson.getPersonID());

        RandomLocation randomLocation = new RandomLocation();
        randomLocation = randomLocation.getRandomLocation();

        birth.setLatitude(randomLocation.getLatitude());
        birth.setLongitude(randomLocation.getLongitude());
        birth.setCountry(randomLocation.getCountry());
        birth.setCity(randomLocation.getCity());
        birth.setEventType("Birth");
        birth.setYear(birthYear);

        insertEvent(birth);

        //2
        Event trip = new Event();

        trip.setEventID(randomGenerator.getRandomString());
        trip.setAssociatedUserName(thisPerson.getAssociatedUserName());
        trip.setPersonID(thisPerson.getPersonID());

        randomLocation = randomLocation.getRandomLocation();

        trip.setLatitude(randomLocation.getLatitude());
        trip.setLongitude(randomLocation.getLongitude());
        trip.setCountry(randomLocation.getCountry());
        trip.setCity(randomLocation.getCity());
        trip.setEventType("Trip");
        trip.setYear(birthYear + 25);

        insertEvent(trip);

        //3.
        Event baptism = new Event();

        baptism.setEventID(randomGenerator.getRandomString());
        baptism.setAssociatedUserName(thisPerson.getAssociatedUserName());
        baptism.setPersonID(thisPerson.getPersonID());

        randomLocation = randomLocation.getRandomLocation();

        baptism.setLatitude(randomLocation.getLatitude());
        baptism.setLongitude(randomLocation.getLongitude());
        baptism.setCountry(randomLocation.getCountry());
        baptism.setCity(randomLocation.getCity());
        baptism.setEventType("Baptism");
        baptism.setYear(birthYear + 9);

        insertEvent(baptism);

        return birthYear;
    }

    /** Delete everything in the table
     *  @throws DataAccessException throws an exception when an error occurs
     */
    public void clearEventTable() throws DataAccessException {
        try (Statement stmt = connection.createStatement()) {
            String sql = "delete from events";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while deleting events table");
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
                stmt.executeUpdate("delete from events where associatedUsername = '" + userName + "'");
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while deleting events");
        }
    }
}
