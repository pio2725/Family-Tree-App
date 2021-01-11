package DAO;

import Model.AuthToken;

import java.sql.*;

/** Interacting between the database and Authorization token data */
public class AuthTokenDAO {

    /** Connection to the database */
    private Connection connection;

    /** Make connection with the given connection to the database
     *  @param connection connection given for the database connection */
    public AuthTokenDAO(Connection connection) {
        this.connection = connection;
    }

    /** Create an token DAO */
    public AuthTokenDAO() {}

    /** Set connection with the given connection
     * @param connection the given connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /** Insert created AuthToken into the database
     *  @param token the unique token for the user
     *  @throws DataAccessException throws an exception if an error occurs
     */
    public void insertAuthToken(AuthToken token) throws DataAccessException {

        String sql = "insert into auth_tokens (authToken, userName) VALUES(?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, token.getAuthToken());
            stmt.setString(2, token.getUserName());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while inserting authToken");
        }
    }

    /** Find authorization token in the database
     *  @param accessToken the unique token used to find in the database
     *  @throws DataAccessException throws an exception if an error occurs
     *  @return return AuthToken if found, or null if not
     */
    public AuthToken findAuthToken(String accessToken) throws DataAccessException {

        AuthToken token;
        ResultSet rs = null;
        String sql = "select * from auth_tokens where authToken = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accessToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                token = new AuthToken(rs.getString("authToken"), rs.getString("userName"));
                return token;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error while finding authToken");
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /** Find if the given authToken is valid in the table
     *  @param authToken the given auth token to test
     *  @throws DataAccessException throws an exception if an error occurs
     *  @return return true if the token is valid
     */
    public boolean isValidAuthToken(String authToken) throws DataAccessException {

        String sql = "select * from auth_tokens where authToken = ?";
        ResultSet rs = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, authToken);
            rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new DataAccessException("error invalid token");
            }
            else {
                return true;
            }

        }
        catch (SQLException e) {
            throw new DataAccessException("error finding auth token");
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** Delete everything in the table
     *  @throws DataAccessException throws an exception when an error occurs
     */
    public void clearAuthTokenTable() throws DataAccessException {
        try (Statement stmt = connection.createStatement()) {
            String sql = "delete from auth_tokens";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while clearing auth_token table");
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
                stmt.executeUpdate("delete from auth_tokens where userName = '" + userName + "'");
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("error while deleting tokens associated with the username");
        }
    }

}
