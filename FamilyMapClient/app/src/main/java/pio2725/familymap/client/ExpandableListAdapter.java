package pio2725.familymap.client;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    Context mContext;
    private List<String> titles;
    private Map<String, List<Object>> personEventAndFamilyMap;


    public ExpandableListAdapter(Context context, List<String> titles, Map<String, List<Object>> personEventAndFamilyMap) {
        mContext = context;
        this.titles = titles;
        this.personEventAndFamilyMap = personEventAndFamilyMap;
    }

    @Override
    public int getGroupCount() {
        return titles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return personEventAndFamilyMap.get(titles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return personEventAndFamilyMap.get(titles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String title = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_parent, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.list_parent_textview);
        textView.setText(title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (groupPosition == 0) {
            Event event = (Event) getChild(groupPosition, childPosition);

            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_child_event, null);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.event_marker_imageview);
            FamilyData familyData = FamilyData.get();
            Drawable eventIcon = null;
            Map<String, Float> map = familyData.getEventTypeToColorMap();

            for (Map.Entry<String, Float> entry : map.entrySet()) {
                if (event.getEventType().toLowerCase().equals(entry.getKey())) {
                    if (entry.getValue() == BitmapDescriptorFactory.HUE_GREEN) {
                        eventIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorGreen).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_AZURE) {
                        eventIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorAzure).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_MAGENTA) {
                        eventIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorMagneta).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_VIOLET) {
                        eventIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorViolet).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_CYAN) {
                        eventIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorCyan).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_ROSE) {
                        eventIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorRose).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_YELLOW) {
                        eventIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorYellow).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_ORANGE) {
                        eventIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorOrange).sizeDp(40);
                    }
                    else if (entry.getValue() == BitmapDescriptorFactory.HUE_BLUE) {
                        eventIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorBlue).sizeDp(40);
                    }
                    else {
                        eventIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorRed).sizeDp(40);
                    }

                }

            }
            imageView.setImageDrawable(eventIcon);

            TextView eventDetailTextView = (TextView) convertView.findViewById(R.id.list_event_detail);
            TextView fullNameTextView = (TextView) convertView.findViewById(R.id.list_person_fullname);
            eventDetailTextView.setText(event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
            Person person = FamilyData.get().findPersonbyId(event.getPersonID());
            fullNameTextView.setText(person.getFirstName() + " " + person.getLastName());
        }
        else if (groupPosition == 1) {
            Person person = (Person) getChild(groupPosition, childPosition);

            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_child_person, null);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.gender_icon_imageview);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.family_member_fullname);
            TextView relationTextView = (TextView) convertView.findViewById(R.id.list_family_relationship);

            Drawable genderIcon;
            if (person.getGender().toUpperCase().equals("M")) {
                genderIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_male).colorRes(R.color.colorMaleIcon).sizeDp(40);
            }
            else {
                genderIcon = new IconDrawable(mContext, FontAwesomeIcons.fa_female).colorRes(R.color.colorFemaleIcon).sizeDp(40);
            }
            imageView.setImageDrawable(genderIcon);

            nameTextView.setText(person.getFirstName() + " " + person.getLastName());

            FamilyData familyData = FamilyData.get();
            Map<String, String> relationships = familyData.getPersonIdToRelationship();

            for (Map.Entry<String, String> entry : relationships.entrySet()) {
                if (person.getPersonID().equals(entry.getKey())) {
                    relationTextView.setText(entry.getValue());
                }
            }

        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
