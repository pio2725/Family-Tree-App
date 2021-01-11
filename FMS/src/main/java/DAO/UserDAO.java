package DAO;

import Model.User;

import java.sql.*;

/** Interacting between the database and User data */
public class UserDAO {

    /** Connection to the database */
    private Connection connection;

    /** Make connection with the given connection to the database
     *  @param connection connection given for the database connection
     */
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /** Make an empty connection */
    public UserDAO() {}

    /** Set connection with the given connection
     * @param connection the given connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /** Insert new User into the database
     *  @param user new user that would be inserted to the database
     *  @throws SQLException throws exception if an error occurs
     */
    public void insertUser(User user) throws DataAccessException {

        String sql = "insert into users (userName, password, email, firstName, lastName, " +
                "gender, personID) VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement stmt = null;

            try {
                stmt = connection.prepareStatement(sql);

                stmt.setString(1, user.getUserName());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getFirstName());
                stmt.setString(5, user.getLastName());
                stmt.setString(6, user.getGender());
                stmt.setString(7, user.getPersonID());

                stmt.executeUpdate();
            }
            finally {
                stmt.close();
                if (stmt != null) {
                    stmt.close();
                }
            }

        }
        catch (SQLException e) {
            throw new DataAccessException("error while inserting user");
        }
    }

    /** Find the user by unique username and return User if exists
     *  @param userName the unique user name to find User in the table
     *  @throws DataAccessException throws an exception if an error occurs
     *  @return return User if found, or null
     */
    public User findUserByName (String userName) throws DataAccessException {

        User user;

        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                String sql = "select * from users where userName = ?";

                stmt = connection.prepareStatement(sql);
                stmt.setString(1, userName);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    user = new User(rs.getString("userName"), rs.getString("password"), rs.getString("email"),
                            rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("personID"));
                    return user;
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
            throw new DataAccessException("error while finding user");
        }
        return null;
    }

    /** Find if the given user's username and password is valid
     * @param user given user trying to log in
     * @return return false if username or password is not valid
     * @throws DataAccessException throws an error
     */
    public boolean loginAuthentication(User user) throws DataAccessException {

        try {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                String sql = "select * from users where userName = ? " + "AND password = ?";

                stmt = connection.prepareStatement(sql);

                stmt.setString(1, user.getUserName());
                stmt.setString(2, user.getPassword());

                rs = stmt.executeQuery();

                if (!rs.next()) {
                    throw new DataAccessException("error invalid username or password");
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
            throw new DataAccessException("error while authenticating user");
        }
    }

    /** Delete everything in the user table
     *  @throws DataAccessException throws an exception when an error occurs
     */
    public void clearUserTable() throws DataAccessException {
        try (Statement stmt = connection.createStatement()) {
            String sql = "delete from users";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while deleting user table");
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
                stmt.executeUpdate("delete from users where userName = '" + userName + "'");
            }
            finally {
                stmt.close();
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while deleting user");
        }
    }
}
