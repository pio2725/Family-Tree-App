package Model;

/** An Event with an ID, associated user name, person's ID,
 *  latitude and longitude, country, city, year, and the type of the event
 */
public class Event {

    /** The ID of the event */
    private String eventID;

    /** The associated user name which the person belongs to */
    private String associatedUsername;

    /** The person's ID which the event belongs to */
    private String personID;

    /** The latitude of the event's location */
    private float latitude;

    /** The longitude of the event's location */
    private float longitude;

    /** The country in which the event occurred */
    private String country;

    /** The city in which the event occurred */
    private String city;

    /** The type of the event */
    private String eventType;

    /** The year in which the event occurred */
    private int year;

    /** Creating an event with an event ID, associated user name, person's ID, latitude and longitude,
     *  country, city, year, and the type of the event
     *  @param eventID the unique ID for the event
     *  @param associatedUsername the username to which the person belongs to
     *  @param personID the ID of the person which the event belongs to
     *  @param latitude the latitude of the event's location
     *  @param longitude the longitude of the event's location
     *  @param country the country in which the event occurred
     *  @param city the city in which the event occurred
     *  @param eventType the type of the event
     *  @param year the year in which the event occurred
     */
    public Event(String eventID, String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public Event() {}

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAssociatedUserName() {
        return associatedUsername;
    }

    public void setAssociatedUserName(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Event) {
            Event oEvent = (Event) o;
            return oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getAssociatedUserName().equals(getAssociatedUserName()) &&
                    oEvent.getPersonID().equals(getPersonID()) &&
                    oEvent.getLatitude() == (getLatitude()) &&
                    oEvent.getLongitude() == (getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear() == (getYear());
        }
        else {
            return false;
        }
    }
}
