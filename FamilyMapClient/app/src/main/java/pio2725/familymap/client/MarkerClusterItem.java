package pio2725.familymap.client;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import Model.Event;

public class MarkerClusterItem implements ClusterItem {

    private LatLng mLatLng;
    private String title;
    private Event mEvent;

    public MarkerClusterItem(Event event) {
        mEvent = event;
        mLatLng = new LatLng(event.getLatitude(), event.getLongitude());
    }

    public Event getEvent() {
        return mEvent;
    }

    @Override
    public LatLng getPosition() {
        return mLatLng;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return "";
    }
}
