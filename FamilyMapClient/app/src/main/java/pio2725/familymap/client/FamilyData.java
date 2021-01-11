package pio2725.familymap.client;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import Model.Event;
import Model.Person;
import Result.PersonResultSingle;

public class FamilyData {

    private Map<String, Person> mPeople;
    Map<String, Float> mEventTypeToColor;

    private ArrayList<Event> mCurrentEvent;
    private ArrayList<Event> allEvents;
    private ArrayList<Person> allPeople;
    private List<Event> mClusterEvents;

    private Set<Person> motherSideFamily;
    private Set<Person> fatherSideFamily;

    private Map<String, String> personIdToRelationship;

    private Person mThisPerson;

    private static FamilyData familyDataInstance;

    private FamilyData() {
        mPeople = new HashMap<>();
        mThisPerson = new Person();
        allEvents = new ArrayList<>();
        allPeople = new ArrayList<>();
        motherSideFamily = new HashSet<>();
        fatherSideFamily = new HashSet<>();
        personIdToRelationship = new HashMap<>();
        mEventTypeToColor = new HashMap<>();
        mCurrentEvent = new ArrayList<>();
        mClusterEvents = new ArrayList<>();
    }

    public static FamilyData get() {
        if (familyDataInstance == null) {
            familyDataInstance = new FamilyData();
        }
        return familyDataInstance;
    }

    public void setThisPerson(PersonResultSingle result) {
        mThisPerson.setPersonID(result.getPersonID());
        mThisPerson.setAssociatedUserName(result.getAssociatedUsername());
        mThisPerson.setFirstName(result.getFirstName());
        mThisPerson.setLastName(result.getLastName());
        mThisPerson.setGender(result.getGender());
        mThisPerson.setFatherID(result.getFatherID());
        mThisPerson.setMotherID(result.getMotherID());
        mThisPerson.setSpouseID(result.getSpouseID());
    }

    public String getFirstName() {
        return mThisPerson.getFirstName();
    }

    public String getLastName() {
        return mThisPerson.getLastName();
    }

    public ArrayList<Event> getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(ArrayList<Event> allEvents) {
        this.allEvents = allEvents;
    }

    public void setAllPeople(ArrayList<Person> allPeople) {
        this.allPeople = allPeople;
    }

    public void setPersonIDToPersonMap(ArrayList<Person> allPerson) {
        for (Person person : allPerson) {
            mPeople.put(person.getPersonID(), person);
        }
    }

    public Person findPersonbyId(String personId) {
        Person personResult;

        for(Map.Entry<String, Person> entry : mPeople.entrySet()) {
            if (personId.equals(entry.getKey())) {
                personResult = entry.getValue();
                return personResult;
            }
        }
        return null;
    }

    public Vector<Person> getPersonFamilyById(String personId) {
        Vector<Person> persons = new Vector<>();
        Person person = findPersonbyId(personId);

        personIdToRelationship.clear();

        if (person.getFatherID() != null) {
            Person temp = findPersonbyId(person.getFatherID());
            persons.add(temp);
            personIdToRelationship.put(temp.getPersonID(), "Father");
        }
        if (person.getMotherID() != null) {
            Person temp = findPersonbyId(person.getMotherID());
            persons.add(temp);
            personIdToRelationship.put(temp.getPersonID(), "Mother");
        }
        if (person.getSpouseID() != null) {
            Person temp = findPersonbyId(person.getSpouseID());
            persons.add(temp);
            personIdToRelationship.put(temp.getPersonID(), "Spouse");
        }
        if (getChildrenPerson(person) != null) {
            for (Person p : getChildrenPerson(person)) {
                persons.add(p);
                personIdToRelationship.put(p.getPersonID(), "Child");
            }
        }
        return persons;
    }

    public ArrayList<Person> getChildrenPerson(Person currentPerson) {
        ArrayList<Person> temp = new ArrayList<>();

        for (Person p : allPeople) {
            if (p.getFatherID() != null) {
                if (p.getFatherID().equals(currentPerson.getPersonID())) {
                    temp.add(p);
                }
            }
            if (p.getMotherID() != null) {
                if (p.getMotherID().equals(currentPerson.getPersonID())) {
                    temp.add(p);
                }
            }
        }
        return temp;
    }

    public Vector<Event> getPersonEventsById(String personId) {
        Vector<Event> events = new Vector<>();
        Vector<Event> result = new Vector<>();
        Person person = findPersonbyId(personId);
        Set<String> eventIdSet = new HashSet<>();

        for (Event event : mCurrentEvent) {
            if (event.getPersonID().equals(person.getPersonID())) {
                events.add(event);
            }
        }

        ArrayList<Integer> temp = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            temp.add(events.get(i).getYear());
        }
        Collections.sort(temp);

        for (int i = 0; i < temp.size(); i++) {
            int year = temp.get(i);
            for (Event event : events) {
                if (year == event.getYear() && !eventIdSet.contains(event.getEventID().toLowerCase())) {
                    result.add(event);
                    eventIdSet.add(event.getEventID().toLowerCase());
                }
            }
        }
        return result;
    }

    public void setMotherSideFamily(Person currentPerson) {
        motherSideFamily.add(currentPerson);
        if (currentPerson.getFatherID() == null && currentPerson.getMotherID() == null) {
            return;
        }

        if (currentPerson.getMotherID() != null) {
            Person mother = findPersonbyId(currentPerson.getMotherID());
            setMotherSideFamily(mother);
        }
        if (currentPerson.getFatherID() != null) {
            Person father = findPersonbyId(currentPerson.getFatherID());
            setMotherSideFamily(father);
        }
    }

    public void setFatherSideFamily(Person currentPerson) {
        fatherSideFamily.add(currentPerson);
        if (currentPerson.getFatherID() == null && currentPerson.getMotherID() == null) {
            return;
        }

        if (currentPerson.getMotherID() != null) {
            Person mother = findPersonbyId(currentPerson.getMotherID());
            setFatherSideFamily(mother);
        }
        if (currentPerson.getFatherID() != null) {
            Person father = findPersonbyId(currentPerson.getFatherID());
            setFatherSideFamily(father);
        }
    }

    public boolean isMotherSideFamily(Event event) {
        for (Person p : motherSideFamily) {
            if (event.getPersonID().equals(p.getPersonID())) {
                return true;
            }
        }
        return false;
    }

    public Map<String, String> getPersonIdToRelationship() {
        return personIdToRelationship;
    }

    public Event getEventById(String eventId) {
        Event eventResult;
        for (Event event : allEvents) {
            if (event.getEventID().equals(eventId)) {
                eventResult = event;
                return eventResult;
            }
        }
        return null;
    }

    public Event getSpouseFirstEvent(Person person) {
        for (Person p : allPeople) {
            if (p.getPersonID().equals(person.getSpouseID())) {
                Event event = getEarliestEventbyId(p.getPersonID());
                if (event != null) {
                    return event;
                }
            }
        }
        return null;
    }

    public Person getThisPerson() {
        return mThisPerson;
    }

    public Event getEarliestEventbyId(String personId) {
        Person person = findPersonbyId(personId);
        Vector<Event> events = getPersonEventsById(personId);

        for (Event e : allEvents) {
            if (person.getPersonID().equals(e.getPersonID())) {
                events.add(e);
            }
        }

        ArrayList<Integer> temp = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            temp.add(events.get(i).getYear());
        }

        Collections.sort(temp);

        if (temp != null) {
            int year = temp.get(0);
            for (Event event : events) {
                if (event.getYear() == year) {
                    return event;
                }
            }
        }
        return null;
    }

    public void setCurrentEvent(Event currentEvent) {
        mCurrentEvent.add(currentEvent);
    }

    public void setCurrentEvent(ArrayList<Event> events) {
        mCurrentEvent = events;
    }

    public void clearCurrentEvent() {
        mCurrentEvent.clear();
    }

    public List<Object> getSearchActivityResult() {
        List<Object> people = new Vector<Object> (allPeople);
        List<Object> event = new Vector<Object> (mCurrentEvent);

        for (Object obj : event) {
            people.add(obj);
        }
        return people;
    }

    public List<Object> getEntireSearchResult() {
        List<Object> people = new Vector<Object>(allPeople);
        List<Object> events = new Vector<Object>(allEvents);
        for (Object obj : events) {
            people.add(obj);
        }
        return people;
    }

    public void setMarkerClusterItems(Collection<MarkerClusterItem> items) {
        mClusterEvents.clear();
        for (MarkerClusterItem item : items) {
            Event event = item.getEvent();
            mClusterEvents.add(event);
        }
    }

    public List<Event> getClusterEvents() {
        return mClusterEvents;
    }

    public void setEventTypeToColor() {
        Set<String> eventTypeSet = new HashSet<>();
        mEventTypeToColor.clear();
        int index = 0;
        float colorArray[] = {BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_AZURE, BitmapDescriptorFactory.HUE_MAGENTA,
                BitmapDescriptorFactory.HUE_VIOLET, BitmapDescriptorFactory.HUE_CYAN, BitmapDescriptorFactory.HUE_ROSE,
                BitmapDescriptorFactory.HUE_YELLOW, BitmapDescriptorFactory.HUE_ORANGE, BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_RED};

        for (Event event : allEvents) {

            if (!eventTypeSet.contains(event.getEventType().toLowerCase())) {
                eventTypeSet.add(event.getEventType().toLowerCase());

                if (index == colorArray.length) {
                    index = 0;
                }
                else {
                    mEventTypeToColor.put(event.getEventType().toLowerCase(), colorArray[index]);
                    ++index;
                }
            }
        }
    }

    public Map<String, Float> getEventTypeToColorMap() {
        return mEventTypeToColor;
    }
}
