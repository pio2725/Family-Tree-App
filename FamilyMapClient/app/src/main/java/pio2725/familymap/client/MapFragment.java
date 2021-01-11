package pio2725.familymap.client;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import Model.Event;
import Model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<MarkerClusterItem> mClusterManager;

    private TextView mNameTextView;
    private TextView mEventDetailTextView;
    private ImageView mGenderImageView;
    private LinearLayout mPersonDetailView;

    private boolean fromAnotherActivity;

    private Polyline mCurrentSpouseLine;
    private ArrayList<Polyline> mCurrentFamilyLines;
    private ArrayList<Polyline> mCurrentLifeStoryLines;
    private List<MarkerClusterItem> mClusterItemList;

    private Person mRootPerson;
    private Person mRootPersonSpouse;
    private Person thisPerson;
    private Event thisEvent;
    private Event mCurrentEvent;
    private FamilyMapSetting sFamilyMapSetting;
    private FamilyData sFamilydata;
    private static MapFragment instance;

    private static final String ARG_EVENT_ID = "extra_event_Id";

    public MapFragment() {
        fromAnotherActivity = false;
        sFamilydata = FamilyData.get();
        sFamilyMapSetting = FamilyMapSetting.get();
        mRootPerson = sFamilydata.getThisPerson();
        mCurrentFamilyLines = new ArrayList<>();
        mCurrentLifeStoryLines = new ArrayList<>();
        mClusterItemList = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (fromAnotherActivity) {
            String eventId = (String) getArguments().getSerializable(ARG_EVENT_ID);
            thisEvent = sFamilydata.getEventById(eventId);
            mCurrentEvent = thisEvent;
        }
        mRootPerson = sFamilydata.getThisPerson();
        if (mRootPerson.getSpouseID() != null) {
            mRootPersonSpouse = sFamilydata.findPersonbyId(mRootPerson.getSpouseID());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        instance = this;

        mNameTextView = (TextView) view.findViewById(R.id.name_text_view);
        mEventDetailTextView = (TextView) view.findViewById(R.id.event_detail_text_view);
        mGenderImageView = (ImageView) view.findViewById(R.id.gender_image_view);
        mPersonDetailView = (LinearLayout) view.findViewById(R.id.name_event_view_layout);

        mPersonDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thisPerson != null) {
                    Intent intent = PersonActivity.newIntent(getActivity(), thisPerson.getPersonID());
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        addMarkers();

        if (thisEvent != null) {
            if (thisEvent.getEventID().equals(thisEvent.getEventID())) {
                Person p = sFamilydata.findPersonbyId(thisEvent.getPersonID());
                LatLng tempLatLng = new LatLng(thisEvent.getLatitude(), thisEvent.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLng(tempLatLng));
                mNameTextView.setText(p.getFirstName() + " " + p.getLastName());
                mEventDetailTextView.setText(thisEvent.getEventType().toUpperCase() + ": " + thisEvent.getCity() + ", " + thisEvent.getCountry() + " (" + thisEvent.getYear() + ")");
                Drawable genderIc;
                if (p.getGender().toUpperCase().equals("M")) {
                    genderIc = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.colorMaleIcon).sizeDp(40);
                }
                else {
                    genderIc = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.colorFemaleIcon).sizeDp(40);
                }
                mGenderImageView.setImageDrawable(genderIc);
            }
        }

        updateSpouseLines(mCurrentEvent);
        if (mCurrentEvent != null) {
            updateFamilyTreeLines(mCurrentEvent, 45);
        }
        updateLifeStoryLines(mCurrentEvent);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!fromAnotherActivity) {
            inflater.inflate(R.menu.main_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search_menu:
                //search Activity
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                return true;
            case R.id.setting_menu:
                //Setting activity
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mMap != null) {
            mMap.clear();
            addMarkers();
            updateSpouseLines(mCurrentEvent);
            if (mCurrentEvent != null) {
                updateFamilyTreeLines(mCurrentEvent, 45);
            }
            updateLifeStoryLines(mCurrentEvent);
        }
    }

    public static MapFragment newInstance(String eventId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_ID, eventId);

        MapFragment fragment = new MapFragment();
        fragment.setFromAnotherActivity(true);

        fragment.setArguments(args);
        return fragment;
    }

    public void setFromAnotherActivity(boolean fromAnotherActivity) {
        this.fromAnotherActivity = fromAnotherActivity;
    }

    public void setEventDetailView(Event event) {
        thisPerson = sFamilydata.findPersonbyId(mCurrentEvent.getPersonID());

        if (mCurrentSpouseLine != null) {
            mCurrentSpouseLine.setVisible(false);
        }
        if (mCurrentFamilyLines != null) {
            for (Polyline pLine : mCurrentFamilyLines) {
                pLine.setVisible(false);
            }
        }
        if (mCurrentLifeStoryLines != null) {
            for (Polyline line : mCurrentLifeStoryLines) {
                line.setVisible(false);
            }
        }

        mNameTextView.setText(thisPerson.getFirstName() + " " + thisPerson.getLastName());
        mEventDetailTextView.setText(event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");

        if (thisPerson.getGender().equals("m")) {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.colorMaleIcon).sizeDp(40);
            mGenderImageView.setImageDrawable(genderIcon);
        }
        else {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.colorFemaleIcon).sizeDp(40);
            mGenderImageView.setImageDrawable(genderIcon);
        }
    }

    public void updateFamilyTreeLines(Event currentEvent, int width) {

        if (sFamilyMapSetting.isFamilyTreeLineOn()) {

            if (currentEvent != null) {
                Person person = sFamilydata.findPersonbyId(currentEvent.getPersonID());

                if (person.getFatherID() == null && person.getMotherID() == null) {
                    return;
                }

                if (person.getMotherID() != null) {
                    Event motherEvent = sFamilydata.getEarliestEventbyId(person.getMotherID());
                    Polyline parentLine = mMap.addPolyline(new PolylineOptions().add(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()), new LatLng(motherEvent.getLatitude(), motherEvent.getLongitude())).width(width).color(Color.BLUE));

                    mCurrentFamilyLines.add(parentLine);
                    updateFamilyTreeLines(motherEvent, width - 10);
                }

                if (person.getFatherID() != null) {
                    Event fatherEvent = sFamilydata.getEarliestEventbyId(person.getFatherID());
                    Polyline parentLine = mMap.addPolyline(new PolylineOptions().add(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()), new LatLng(fatherEvent.getLatitude(), fatherEvent.getLongitude())).width(width).color(Color.BLUE));

                    mCurrentFamilyLines.add(parentLine);
                    updateFamilyTreeLines(fatherEvent, width - 10);
                }
            }
        }
        else {
            return;
        }
    }

    public void updateLifeStoryLines(Event thisEvent) {
        if (thisEvent != null) {
            if (sFamilyMapSetting.isLifeStoryLineOn()) {

                Vector<Event> personOrderedEvents = sFamilydata.getPersonEventsById(thisEvent.getPersonID());
                for (int i = 0; i < personOrderedEvents.size() - 1; i++) {
                    Event event = personOrderedEvents.get(i);
                    Event eventToConnect = personOrderedEvents.get(i + 1);
                    Polyline lifeStoryLine = mMap.addPolyline(new PolylineOptions().add(new LatLng(event.getLatitude(), event.getLongitude()), new LatLng(eventToConnect.getLatitude(), eventToConnect.getLongitude())).width(10).color(Color.GREEN));
                    mCurrentLifeStoryLines.add(lifeStoryLine);
                }
            }
            else {
                return;
            }
        }
    }

    public void updateSpouseLines(Event thisEvent) {
        if (thisEvent != null) {
            Person currentEventPerson = sFamilydata.findPersonbyId(thisEvent.getPersonID());
            Event spouseEvent = sFamilydata.getSpouseFirstEvent(currentEventPerson);

            if (spouseEvent != null) {
                mCurrentSpouseLine = mMap.addPolyline(new PolylineOptions().add(new LatLng(thisEvent.getLatitude(), thisEvent.getLongitude()), new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude())).width(5).color(Color.RED));
            }
            if (!sFamilyMapSetting.isSpouseLineOn()) {
                if (mCurrentSpouseLine != null) {
                    mCurrentSpouseLine.setVisible(false);
                }
            }
        }
    }

    private boolean checkRootCouple(Event event) {

        if (mRootPersonSpouse != null) {
            if (event.getPersonID().equals(mRootPersonSpouse.getPersonID())) {
                return true;
            }
        }

        if (sFamilydata.isMotherSideFamily(event)) {
            //mother side
            if (sFamilyMapSetting.isMotherSideOn()) {
                return true;
            }
            else {
                return false;
            }
        } else {
            //father side
            if (sFamilyMapSetting.isFatherSideOn()) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    private void addMarkers() {

        ArrayList<Event> allEvents = sFamilydata.getAllEvents();
        sFamilydata.clearCurrentEvent();
        mClusterItemList.clear();

        mClusterManager = new ClusterManager<MarkerClusterItem>(getActivity(), mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        final ClusterRenderer renderer = new ClusterRenderer(getActivity(), mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

        for (Event event : allEvents) {

            boolean markerShouldAppear = false;

            if (!mRootPerson.getPersonID().equals(event.getPersonID())) {
                if (checkRootCouple(event)) {
                    markerShouldAppear = true;
                }
                else {
                    continue;
                }
            }

            if (sFamilydata.findPersonbyId(event.getPersonID()).getGender().equals("m")) {
                if (sFamilyMapSetting.isMaleEventsOn()) {
                    markerShouldAppear = true;
                }
                else {
                    continue;
                }
            }
            else {
                if (sFamilyMapSetting.isFemaleEventsOn()) {
                    markerShouldAppear = true;
                }
                else {
                    continue;
                }
            }

            if (markerShouldAppear) {
                MarkerClusterItem item = new MarkerClusterItem(event);
                sFamilydata.setCurrentEvent(event);
                mClusterItemList.add(item);
            }
        }

        mClusterManager.addItems(mClusterItemList);
        mClusterManager.cluster();

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerClusterItem>() {
            @Override
            public boolean onClusterItemClick(MarkerClusterItem markerClusterItem) {
                mCurrentEvent = markerClusterItem.getEvent();
                setEventDetailView(mCurrentEvent);
                updateSpouseLines(mCurrentEvent);
                updateFamilyTreeLines(mCurrentEvent, 45);
                updateLifeStoryLines(mCurrentEvent);
                return false;
            }
        });

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MarkerClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<MarkerClusterItem> cluster) {
                Collection<MarkerClusterItem> markerClusterItems = cluster.getItems();
                sFamilydata.setMarkerClusterItems(markerClusterItems);

                //open dialog
                EventDialog eventDialog = new EventDialog();
                eventDialog.show(getFragmentManager(), "event dialog");
                return false;
            }
        });
    }

    public static MapFragment getInstance() {
        return instance;
    }

    public void setCurrentEvent(Event event) {
        mCurrentEvent = event;
    }
}
