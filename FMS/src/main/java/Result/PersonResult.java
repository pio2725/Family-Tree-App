package Result;

import Model.Person;

import java.util.ArrayList;

/** The response body of all family members */
public class PersonResult extends  ResultParent {

    /** The array of Person objects */
    private ArrayList<Person> data;

    /** Creating family result with
     *  @param data the array of family members
     */
    public PersonResult(ArrayList<Person> data) {
        this.data = data;
    }

    public PersonResult() {}

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
    }
}
