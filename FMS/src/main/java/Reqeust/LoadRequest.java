package Reqeust;

import Model.Event;
import Model.Person;
import Model.User;

/** The body of a load request with data arrays */
public class LoadRequest {

    /** The array of User objects */
    private User[] users;

    /** The array of Person objects */
    private Person[] persons;

    /** The array of Event objects */
    private Event[] events;

    /** Creating a load request with arrays of users, persons, and events
     *  @param users the array of users
     *  @param persons the array of persons
     *  @param events the array of events
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
