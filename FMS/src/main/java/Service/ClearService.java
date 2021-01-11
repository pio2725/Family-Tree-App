package Service;

import DAO.*;
import Result.ClearResult;

/** Clear all data from the database, including
 *  accounts, tokens, person, and event data */
public class ClearService {

    /** Creating a clear service */
    public ClearService() {}

    /** Delete all data from the database
     *  @return return a clear result body with the message */
    public ClearResult clear() {

        ClearResult clearResult = new ClearResult();
        Database db = new Database();
        try {
            db.setConnection();
            db.clearTables();
            db.createTables();

            clearResult.setMessage("Clear succeeded");
            db.closeConnection(true);
            return clearResult;
        }
        catch (DataAccessException e) {
            try {
                db.closeConnection(false);
                clearResult.setMessage(e.getMessage());
            }
            catch (DataAccessException ex) {
                clearResult.setMessage(ex.getMessage());
            }
        }
        return clearResult;
    }
}
