package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** DAO.Database connection and dealing with the table */
public class Database {

    /** Connection for the driver */
    private Connection connection;
    private UserDAO userDAO;
    private PersonDAO personDAO;
    private AuthTokenDAO tokenDAO;
    private EventDAO eventDAO;

    public Database() {
        userDAO = new UserDAO();
        personDAO = new PersonDAO();
        tokenDAO = new AuthTokenDAO();
        eventDAO = new EventDAO();
    }

    /** Open a connection to make a change to the database
     *  @throws DataAccessException throws exception when it's unable to open connection to the database
     *  @return return the valid connection
     */
    public Connection openConnection() throws DataAccessException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";

            connection = DriverManager.getConnection(CONNECTION_URL);
            //System.out.println("opening");

            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }
        return connection;
    }

    public void setConnection() throws DataAccessException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";
            connection = DriverManager.getConnection(CONNECTION_URL);
            //System.out.println("opening");
            userDAO.setConnection(connection);
            personDAO.setConnection(connection);
            eventDAO.setConnection(connection);
            tokenDAO.setConnection(connection);

            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection");
        }
    }

    /** End the transaction and either commit or rollback
     *  @param commit true if want to commit the changes to the database, or false if something went wrong
     *  @throws DataAccessException throws exception when it's unable to close database connection
     */
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                connection.commit();
            }
            else {
                connection.rollback();
            }

            connection.close();
            connection = null;
            //System.out.println("closing");
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /** Getting connection
     *  @throws DataAccessException throws an exception when an error occurs
     *  @return return connection or call openConnection if the connection is null
     */
    public Connection getConnection() throws DataAccessException {
        if (connection == null) {
            return openConnection();
        }
        else {
            return connection;
        }
    }

    /** Create tables to the database
     *  @throws DataAccessException throws exception when encountered with SQL error
     */
    public void createTables() throws DataAccessException {
        try (Statement stmt = connection.createStatement()) {

            String sql = "create table if not exists events " +
                    "(" +
                    "eventID text not null unique, " +
                    "associatedUsername text not null, " +
                    "personID text not null, " +
                    "latitude float not null, " +
                    "longitude float not null, " +
                    "country text not null, " +
                    "city text not null, " +
                    "eventType text not null, " +
                    "year int not null, " +
                    "primary key (eventID), " +
                    "foreign key (associatedUsername) references users(userName), " +
                    "foreign key (personID) references persons(personID)" +
                    ")";

            String sql2 = "create table if not exists users " +
                    "(" +
                    "userName text not null unique, " +
                    "password text not null, " +
                    "email text not null, " +
                    "firstName text not null, " +
                    "lastName text not null, " +
                    "gender text not null, " +
                    "personID text not null, " +
                    "primary key (userName)" +
                    ")";

            String sql3 = "create table if not exists auth_tokens " +
                    "(" +
                    "authToken text not null unique, " +
                    "userName text not null, " +
                    "primary key (authToken)" +
                    "foreign key (userName) references users(userName) " +
                    ")";

            String sql4 = "create table if not exists persons " +
                    "(" +
                    "personID text not null unique, " +
                    "associatedUsername text not null, " +
                    "firstName text not null, " +
                    "lastName text not null, " +
                    "gender text not null, " +
                    "fatherID text, " +
                    "motherID text, " +
                    "spouseID text, " +
                    "primary key (personID), " +
                    "foreign key (associatedUsername) references users(userName) " +
                    ")";

            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
            stmt.executeUpdate(sql4);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while creating tables");
        }
    }

    /** Delete tables from the database
     *  @throws DataAccessException throws exception when SQL error occurred while clearing tables
     */
    public void clearTables() throws DataAccessException {

        try (Statement stmt = connection.createStatement()) {
            String sql = "drop table if exists events";
            String sql2 = "drop table if exists users";
            String sql3 = "drop table if exists auth_tokens";
            String sql4 = "drop table if exists persons";

            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
            stmt.executeUpdate(sql4);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while clearing tables");
        }
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public PersonDAO getPersonDAO() {
        return personDAO;
    }

    public AuthTokenDAO getTokenDAO() {
        return tokenDAO;
    }

    public EventDAO getEventDAO() {
        return eventDAO;
    }
}
