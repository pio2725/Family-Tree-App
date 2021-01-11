package pio2725.familymap.client;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.Map;

public class ClusterRenderer extends DefaultClusterRenderer<MarkerClusterItem> {

    private Context mContext;

    public ClusterRenderer(Context context, GoogleMap map, ClusterManager<MarkerClusterItem> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(MarkerClusterItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        String eventType = item.getEvent().getEventType();
        FamilyData familyData = FamilyData.get();
        Map<String, Float> map = familyData.getEventTypeToColorMap();

        for (Map.Entry<String, Float> entry : map.entrySet()) {
            if (eventType.toLowerCase().equals(entry.getKey())) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(entry.getValue()));
            }
        }
    }
}
