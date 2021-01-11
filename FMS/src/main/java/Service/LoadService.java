package Service;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;
import Reqeust.LoadRequest;
import Result.LoadResult;

/** Clear all data and then load data into the database */
public class LoadService {


    /** Creating a load service */
    public LoadService() {

    }

    /** Clear all data from the database, and then loads the posted person,
     *  user, and event data into the database
     *  @param r the load request with the username and the specified number of generations
     *  @return return the load result with the message
     */
    public LoadResult load(LoadRequest r) {

        Database db = new Database();
        LoadResult loadResult = new LoadResult();

        try {
            db.setConnection();
            db.clearTables();
            db.createTables();

            UserDAO userDAO = db.getUserDAO();
            PersonDAO personDAO = db.getPersonDAO();
            EventDAO eventDAO = db.getEventDAO();

            User[] users = r.getUsers();
            Person[] persons = r.getPersons();
            Event[] events = r.getEvents();

            int numUsers = users.length;
            int numPersons = persons.length;
            int numEvents = events.length;

            for (int i = 0; i < numUsers; i++) {
                userDAO.insertUser(users[i]);
            }
            for (int i = 0; i < numPersons; i++) {
                personDAO.insertPerson(persons[i]);
            }
            for (int i = 0; i < numEvents; i++) {
                eventDAO.insertEvent(events[i]);
            }
            loadResult.setMessage("Successfully added " + numUsers + " users, " + numPersons +
                    " persons, " + "and " + numEvents + " events to the database");
            db.closeConnection(true);
            return loadResult;
        }
        catch (DataAccessException e) {
            loadResult.setMessage(e.getMessage());
            try {
                db.closeConnection(false);
            }
            catch (DataAccessException ex) {
                loadResult.setMessage(ex.getMessage());
            }
        }
        return loadResult;
    }
}
