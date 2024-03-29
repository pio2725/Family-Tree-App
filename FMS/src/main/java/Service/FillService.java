package Service;

import DAO.*;
import Model.Person;
import Model.RandomGenerator;
import Model.User;
import Reqeust.FillRequest;
import Result.FillResult;

/** Populate generations for a given user name */
public class FillService {

    /** Creating a fill service */
    public FillService() {

    }

    /** Populate the database with the data generated by the user
     *  @param r the fill request given with user name and the number of generations to be generated
     *  @return the fill result with the message
     */
    public FillResult fill(FillRequest r) {

        Database db = new Database();
        FillResult fillResult = new FillResult();

        try {
            db.setConnection();
            UserDAO userDAO = db.getUserDAO();
            EventDAO eventDAO = db.getEventDAO();
            PersonDAO personDAO = db.getPersonDAO();
            AuthTokenDAO tokenDAO = db.getTokenDAO();

            String userName = r.getUserName();

            if (userDAO.findUserByName(r.getUserName()) == null) {
                fillResult.setMessage("Invalid username");
                db.closeConnection(false);
                return fillResult;
            }

            User user = userDAO.findUserByName(userName);
            user.setPersonID(new RandomGenerator().getRandomString());

            // delete everything associated with the user
            userDAO.deleteData(userName);
            eventDAO.deleteData(userName);
            personDAO.deleteData(userName);
            tokenDAO.deleteData(userName);

            userDAO.insertUser(user);
            Person thisPerson = new Person(user);
            personDAO.insertPerson(thisPerson);

            int birthYear = eventDAO.createThisPersonEvents(thisPerson);

            if (r.getGenerationNum() == 4) {
                //default
                personDAO.generateDefaultGenerations(4, thisPerson, birthYear, eventDAO);
                fillResult.setMessage("Successfully added 31 persons and 124 events to the database.");
            }
            else {
                personDAO.generateDefaultGenerations(r.getGenerationNum(), thisPerson, birthYear, eventDAO);
                double numGen = r.getGenerationNum();
                double numPersons = Math.pow(2, numGen+1) - 1;
                int numPerson = (int)numPersons;
                fillResult.setMessage("Successfully added " + numPerson + " persons and " + numPerson*3 + " events to the database.");
            }
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            fillResult.setMessage(e.getMessage());
            try {
                db.closeConnection(false);
            }
            catch (DataAccessException ex) {
                fillResult.setMessage(ex.getMessage());
            }
        }
        return fillResult;
    }
}
