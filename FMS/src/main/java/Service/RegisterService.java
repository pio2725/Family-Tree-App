package Service;

import DAO.*;
import Model.AuthToken;
import Model.Person;
import Model.RandomGenerator;
import Model.User;
import Reqeust.RegisterRequest;
import Result.RegisterResult;
import Result.ResultParent;

import java.sql.Connection;

/** Register for a new user */
public class RegisterService extends ResultParent {

    /** Creating a register service */
    public RegisterService() { }

    /** Creating a user, person, and 4 generations of ancestor data, log in and return token
     *  @param r register request object given
     *  @return return register result object with token, username, and personID
     */
    public RegisterResult register(RegisterRequest r) {

        RegisterResult registerResult = new RegisterResult();
        Database db = new Database();

        try {
            db.setConnection();
            UserDAO userDAO = db.getUserDAO();
            EventDAO eventDAO = db.getEventDAO();
            PersonDAO personDAO = db.getPersonDAO();
            AuthTokenDAO tokenDAO = db.getTokenDAO();

            if (userDAO.findUserByName(r.getUserName()) != null) {
                registerResult.setMessage("error username already taken");
                db.closeConnection(false);
                return registerResult;
            }
            else {

                if (r.getGender().length() != 1 || (r.getGender().equals("m") && r.getGender().equals("f"))) {
                    registerResult.setMessage("error invalid gender");
                    db.closeConnection(false);
                    return registerResult;
                }

                if (r.getUserName() == null || r.getPassword() == null || r.getEmail() == null ||
                    r.getFirstName() == null || r.getLastName() == null || r.getGender() == null) {
                    registerResult.setMessage("error invalid request");
                    db.closeConnection(false);
                    return registerResult;
                }

                User newUser = new User(r.getUserName(), r.getPassword(), r.getEmail(), r.getFirstName(), r.getLastName(), r.getGender(), new RandomGenerator().getRandomString());
                userDAO.insertUser(newUser);

                Person thisPerson = new Person(newUser);
                personDAO.insertPerson(thisPerson);

                int birthYear = eventDAO.createThisPersonEvents(thisPerson);
                personDAO.generateDefaultGenerations(4, thisPerson, birthYear, eventDAO);

                AuthToken token = new AuthToken(new RandomGenerator().getRandomString(), r.getUserName());
                tokenDAO.insertAuthToken(token);
                db.closeConnection(true);

                registerResult.setAuthToken(token.getAuthToken());
                registerResult.setUserName(token.getUserName());
                registerResult.setPersonID(thisPerson.getPersonID());

                return registerResult;
            }
        }
        catch (DataAccessException e) {
            registerResult.setMessage(e.getMessage());
            try {
                db.closeConnection(false);
            }
            catch (DataAccessException ex) {
                registerResult.setMessage(ex.getMessage());
            }
        }
        return registerResult;
    }
}
