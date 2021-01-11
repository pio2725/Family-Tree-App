package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Model.Person;
import Result.PersonResult;
import java.util.ArrayList;


/** Get all family members of the current user */
public class PersonService {

    /** Creating Person service */
    public PersonService() {
    }

    /** Get all family members that the current user has
     *  @param  authToken the auth token to discern the current user
     *  @return the user's family members as JSON
     */
    public PersonResult getFamilyMembers(String authToken) {

        Database db = new Database();
        PersonResult personResult = new PersonResult();

        try {
            db.setConnection();
            PersonDAO personDAO = db.getPersonDAO();
            AuthTokenDAO tokenDAO = db.getTokenDAO();

            if (tokenDAO.isValidAuthToken(authToken)) {
                ArrayList<Person> persons = personDAO.getAllFamilyMembers(authToken);
                personResult.setData(persons);
                db.closeConnection(true);
                return personResult;
            }
            else {
                personResult.setMessage("error invalid token");
                db.closeConnection(false);
                return personResult;
            }
        }
        catch (DataAccessException e) {
            personResult.setMessage(e.getMessage());
            try {
                db.closeConnection(false);
            }
            catch (DataAccessException ex) {
                personResult.setMessage(ex.getMessage());
            }
        }
        return personResult;
    }
}
