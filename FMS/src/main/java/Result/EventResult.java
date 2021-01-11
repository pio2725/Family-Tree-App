package Result;

import Model.Event;

import java.util.ArrayList;

/** The response body of all events */
public class EventResult extends ResultParent {

    /** The array of Event objects */
    private ArrayList<Event> data;

    /** Creating a event result
     *  @param  data the array of all events
     */
    public EventResult(ArrayList<Event> data) {
        this.data = data;
    }

    public EventResult() {}

    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }

}
