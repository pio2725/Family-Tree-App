package Result;

/** The response of a single event information */
public class EventResultSingle extends  ResultParent{

    /** The associated username of the user */
    private String associatedUsername;

    /** The unique event ID */
    private String eventID;

    /** The person ID which the event belongs to */
    private String personID;

    /** The latitude of the event */
    private float latitude;

    /** The longitude of the event */
    private float longitude;

    /** The country of the event */
    private String country;

    /** The city of the event */
    private String city;

    /** The type of the event */
    private String eventType;

    /** The year of the event */
    private int year;

    /** Creating a single event result with
     *  @param  associatedUsername the associated username of the user
     *  @param  eventID the eventID of the person
     *  @param personID the ID of the person this event belongs to
     *  @param  latitude the latitude of the event
     *  @param  longitude the longitude of the event
     *  @param country the country of the event
     *  @param city the city of the event
     *  @param eventType the type of the event
     *  @param year the year of the event
     */
    public EventResultSingle(String associatedUsername, String eventID, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public EventResultSingle() {}

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
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
}
