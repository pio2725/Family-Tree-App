package pio2725.familymap.client;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;

public class EventDialog extends AppCompatDialogFragment {

    private FamilyData mFamilyData;
    private RecyclerView mRecyclerView;
    private EventAdapter mEventAdapter;
    private Event mSelectedEvent;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_dialog, null);

        mFamilyData = FamilyData.get();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.dialog_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Event> events = mFamilyData.getClusterEvents();

        builder.setView(view).setTitle("Select an Event:")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MapFragment.getInstance().setCurrentEvent(mSelectedEvent);
                        MapFragment.getInstance().setEventDetailView(mSelectedEvent);
                        MapFragment.getInstance().updateSpouseLines(mSelectedEvent);
                        MapFragment.getInstance().updateFamilyTreeLines(mSelectedEvent, 45);
                        MapFragment.getInstance().updateLifeStoryLines(mSelectedEvent);
                    }
                });
        mEventAdapter = new EventAdapter(events);
        mRecyclerView.setAdapter(mEventAdapter);

        return builder.create();
    }

    private class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mEventDetailView1;
        private TextView mEventDetailView2;
        private ImageView mEventIconImageView;
        private Event mEvent;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mEventIconImageView = (ImageView) itemView.findViewById(R.id.event_marker_imageview);
            mEventDetailView1 = (TextView) itemView.findViewById(R.id.list_event_detail);
            mEventDetailView2 = (TextView) itemView.findViewById(R.id.list_person_fullname);
            itemView.setOnClickListener(this);
        }

        public void bindEvent(Event event) {
            mEvent = event;
            Person person = mFamilyData.findPersonbyId(event.getPersonID());
            Drawable eventIcon = null;
            Map<String, Float> map = mFamilyData.getEventTypeToColorMap();
            for (Map.Entry<String, Float> entry : map.entrySet()) {
                if (event.getEventType().toLowerCase().equals(entry.getKey())) {
                    if (entry.getValue() == BitmapDescriptorFactory.HUE_GREEN) {
                        eventIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorGreen).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_AZURE) {
                        eventIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorAzure).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_MAGENTA) {
                        eventIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorMagneta).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_VIOLET) {
                        eventIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorViolet).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_CYAN) {
                        eventIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorCyan).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_ROSE) {
                        eventIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorRose).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_YELLOW) {
                        eventIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorYellow).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_ORANGE) {
                        eventIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorOrange).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_BLUE) {
                        eventIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorBlue).sizeDp(40);
                    }
                    else {
                        eventIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorRed).sizeDp(40);
                    }
                }
            }
            mEventIconImageView.setImageDrawable(eventIcon);
            mEventDetailView1.setText(event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
            mEventDetailView2.setText(person.getFirstName() + " " + person.getLastName());
        }

        @Override
        public void onClick(View v) {
            mSelectedEvent = mEvent;
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

        private List<Event> mEvents;

        public EventAdapter(List<Event> mEvents) {
            this.mEvents = mEvents;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_child_event, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            holder.bindEvent(mEvents.get(position));
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }

}
