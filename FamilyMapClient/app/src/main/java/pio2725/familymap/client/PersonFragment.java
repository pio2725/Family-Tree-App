package pio2725.familymap.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import Model.Event;
import Model.Person;

public class PersonFragment extends Fragment {

    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mListAdapter;

    private TextView mFirstNameTextView;
    private TextView mLastNameTextView;
    private TextView mGenderTextView;

    private static final String ARG_PERSON_ID = "person_id";
    private String mPersonId;
    private Person mPerson;
    private FamilyData sFamilyData;

    private List<String> titles;
    private Map<String, List<Object>> personEventAndFamilyMap;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mPersonId = (String) getArguments().getSerializable(ARG_PERSON_ID);
        sFamilyData = FamilyData.get();
        mPerson = sFamilyData.findPersonbyId(mPersonId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        mExpandableListView = (ExpandableListView) view.findViewById(R.id.person_detail_list_view);
        mFirstNameTextView = (TextView) view.findViewById(R.id.first_name_detail);
        mLastNameTextView = (TextView) view.findViewById(R.id.last_name_detail);
        mGenderTextView = (TextView) view.findViewById(R.id.gender_detail);

        fillData();

        mListAdapter = new ExpandableListAdapter(getContext(), titles, personEventAndFamilyMap);
        mExpandableListView.setAdapter(mListAdapter);

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (groupPosition == 0) {
                    Event tempEvent = (Event) personEventAndFamilyMap.get(titles.get(groupPosition)).get(childPosition);
                    Intent intent = EventActivity.newIntent(getActivity(), tempEvent.getEventID());
                    startActivity(intent);
                }
                else if (groupPosition == 1) {
                    Person clickedPerson = (Person) personEventAndFamilyMap.get(titles.get(groupPosition)).get(childPosition);
                    Intent intent = PersonActivity.newIntent(getActivity(), clickedPerson.getPersonID());
                    startActivity(intent);
                }
                return false;
            }
        });

        mFirstNameTextView.setText(mPerson.getFirstName());
        mLastNameTextView.setText(mPerson.getLastName());
        if (mPerson.getGender().toUpperCase().equals("M")) {
            mGenderTextView.setText("Male");
        }
        else {
            mGenderTextView.setText("Female");
        }
        return view;
    }

    private void fillData() {

        titles = new ArrayList<>();
        titles.add("LIFE EVENTS");
        titles.add("FAMILY");

        List<Object> events = new Vector<Object>(sFamilyData.getPersonEventsById(mPersonId));
        List<Object> familyMembers = new Vector<Object>(sFamilyData.getPersonFamilyById(mPersonId));
        personEventAndFamilyMap = new HashMap<>();

        personEventAndFamilyMap.put(titles.get(0), events);
        personEventAndFamilyMap.put(titles.get(1), familyMembers);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    public static PersonFragment newInstance(String personId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERSON_ID, personId);

        PersonFragment fragment = new PersonFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
