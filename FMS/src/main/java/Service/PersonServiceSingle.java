package Service;

import DAO.*;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Result.PersonResultSingle;

/** Get a single person's information */
public class PersonServiceSingle {

    /** Creating a single person service */
    public PersonServiceSingle() {

    }

    /** Get a single person's information by personID
     *  @param  personID the unique personID of the person
     *  @return the single person result with person's information
     */
    public PersonResultSingle getPerson(String personID, String authToken) {

        Database db = new Database();
        PersonResultSingle personResult = new PersonResultSingle();

        try {
            db.setConnection();
            PersonDAO personDAO = db.getPersonDAO();
            AuthTokenDAO tokenDAO = db.getTokenDAO();
            UserDAO userDAO = db.getUserDAO();

            if (tokenDAO.isValidAuthToken(authToken)) {

                AuthToken token = tokenDAO.findAuthToken(authToken);
                User user = userDAO.findUserByName(token.getUserName());

                if (personDAO.isValidPersonID(personID)) {
                    Person person = personDAO.findPerson(personID);

                    if (!person.getAssociatedUserName().equals(user.getUserName())) {
                        personResult.setMessage("error associatedUsername does not match");
                        db.closeConnection(false);
                        return personResult;
                    }

                    personResult.setAssociatedUsername(person.getAssociatedUserName());
                    personResult.setPersonID(person.getPersonID());
                    personResult.setFirstName(person.getFirstName());
                    personResult.setLastName(person.getLastName());
                    personResult.setGender(person.getGender());
                    if (person.getFatherID() != null) {
                        personResult.setFatherID(person.getFatherID());
                    }
                    if (person.getMotherID() != null) {
                        personResult.setMotherID(person.getMotherID());
                    }
                    if (person.getSpouseID() != null) {
                        personResult.setSpouseID(person.getSpouseID());
                    }
                    db.closeConnection(true);
                    return personResult;
                }
                else {
                    personResult.setMessage("error invalid personID");
                    db.closeConnection(false);
                    return personResult;
                }
            }
        }
        catch (DataAccessException e) {
            try {
                db.closeConnection(false);
                personResult.setMessage(e.getMessage());
            }
            catch (DataAccessException ex) {
                personResult.setMessage(e.getMessage());
            }
        }
        return personResult;
    }
}
