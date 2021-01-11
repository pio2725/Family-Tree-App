package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Model.Event;
import Result.EventResult;

import java.util.ArrayList;

/** Get all events for all family members of the current user */
public class EventService {

    /** Creating a event service */
    public EventService() {

    }

    /** Get all events for all family members of the current user
     *  @param authToken the unique token for the current user
     *  @return  returns the event result with a JSON objects
     */
    public EventResult getAllEvents(String authToken) {

        Database db = new Database();
        EventResult eventResult = new EventResult();

        try {
            db.setConnection();
            EventDAO eventDAO = db.getEventDAO();
            AuthTokenDAO tokenDAO = db.getTokenDAO();

            if (tokenDAO.isValidAuthToken(authToken)) {
                ArrayList<Event> events = eventDAO.getAllFamilyEvents(authToken);
                eventResult.setData(events);
                db.closeConnection(true);
                return eventResult;
            }
            else {
                eventResult.setMessage("error invalid token");
                db.closeConnection(false);
                return eventResult;
            }
        }
        catch (DataAccessException e) {
            eventResult.setMessage(e.getMessage());
            try {
                db.closeConnection(false);
            }
            catch (DataAccessException ex) {
                eventResult.setMessage(e.getMessage());
            }
        }
        return eventResult;
    }
}
