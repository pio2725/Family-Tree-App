package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Model.AuthToken;
import Model.Event;
import Result.EventResultSingle;

/** Get a single event */
public class EventServiceSingle {

    /** Creating a single event service */
    public EventServiceSingle() {

    }

    /** Get a single event and its information
     *  @param eventID the unique eventID
     *  @return the single event result with the information
     */
    public EventResultSingle getSingleEvent(String eventID, String authToken) {

        EventResultSingle eventResult = new EventResultSingle();
        Database db = new Database();

        try {
            db.setConnection();
            EventDAO eventDAO = db.getEventDAO();
            AuthTokenDAO tokenDAO = db.getTokenDAO();

            if (tokenDAO.isValidAuthToken(authToken)) {
                AuthToken token = tokenDAO.findAuthToken(authToken);

                if (eventDAO.isValidEvent(eventID)) {
                    Event event = eventDAO.findEvent(eventID);

                    if (!event.getAssociatedUserName().equals(token.getUserName())) {
                        eventResult.setMessage("error name does not match");
                        db.closeConnection(false);
                        return eventResult;
                    }

                    EventResultSingle validResult;
                    validResult = new EventResultSingle(event.getAssociatedUserName(), event.getEventID(), event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry(), event.getCity(), event.getEventType(), event.getYear());
                    db.closeConnection(true);
                    return validResult;
                }
                else {
                    eventResult.setMessage("error invalid eventID");
                    db.closeConnection(false);
                    return eventResult;
                }
            }
            else {
                eventResult.setMessage("error Invalid token");
                db.closeConnection(false);
                return eventResult;
            }
        }
        catch (DataAccessException e) {
            try {
                db.closeConnection(false);
                eventResult.setMessage(e.getMessage());
            }
            catch (DataAccessException ex) {
                eventResult.setMessage(e.getMessage());
            }
        }
        return eventResult;
    }
}
