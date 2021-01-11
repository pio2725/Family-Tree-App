package pio2725.familymap.client;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;

public class SearchFragment extends Fragment {

    private RecyclerView mSearchRecyclerView;
    private FamilyData mFamilyData;
    private ListAdapter mListAdapter;
    private SearchView mSearchView;

    private static int TYPE_PERSON = 1;
    private static int TYPE_EVENT = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mSearchRecyclerView = (RecyclerView) view.findViewById(R.id.search_recyler_view);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFamilyData = FamilyData.get();
        mSearchView = (SearchView) view.findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mListAdapter = new ListAdapter(mFamilyData.getSearchActivityResult());
                mSearchRecyclerView.setAdapter(mListAdapter);
                mListAdapter.getFilter().filter(newText);

                return false;
            }
        });

        return view;
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

    private class ListViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private ImageView mImageView;
        private TextView mDetailView1;
        private TextView mDetailView2;

        private Object object;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.event_marker_imageview);
            mDetailView1 = (TextView) itemView.findViewById(R.id.list_event_detail);
            mDetailView2 = (TextView) itemView.findViewById(R.id.list_person_fullname);
            itemView.setOnClickListener(this);
        }

        public void bindPerson(Person person) {
            Drawable genderIcon;
            object = person;
            if (person.getGender().toUpperCase().equals("M")) {
                genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.colorMaleIcon).sizeDp(40);
            }
            else {
                genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.colorFemaleIcon).sizeDp(40);
            }
            mImageView.setImageDrawable(genderIcon);
            mDetailView1.setText(person.getFirstName() + " " + person.getLastName());
            mDetailView2.setText("");
        }

        public void bindEvent(Event event) {
            Person person = mFamilyData.findPersonbyId(event.getPersonID());
            Drawable eventIcon = null;
            object = event;

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

            mImageView.setImageDrawable(eventIcon);
            mDetailView1.setText(event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
            mDetailView2.setText(person.getFirstName() + " " + person.getLastName());
        }

        @Override
        public void onClick(View v) {
            if (object.getClass() == Person.class) {
                Person person = (Person) object;
                Intent intent = PersonActivity.newIntent(getActivity(), person.getPersonID());
                startActivity(intent);
            }
            else {
                Event event = (Event) object;
                Intent intent = EventActivity.newIntent(getActivity(), event.getEventID());
                startActivity(intent);
            }
        }
    }

    private class ListAdapter extends RecyclerView.Adapter<ListViewHolder> implements Filterable {

        private List<Object> mObjects;

        public ListAdapter(List<Object> objects) {
            mObjects = objects;
        }

        @NonNull
        @Override
        public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_child_event, parent, false);
            return new ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_PERSON) {
                Person person = (Person) mObjects.get(position);
                holder.bindPerson(person);
            }
            else {
                Event event = (Event) mObjects.get(position);
                holder.bindEvent(event);
            }
        }

        @Override
        public int getItemCount() {
            return mObjects.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (mObjects.get(position).getClass() == Person.class) {
                return TYPE_PERSON;
            }
            else {
                return TYPE_EVENT;
            }
        }

        @Override
        public Filter getFilter() {
            return searchFilter;
        }

        private Filter searchFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Object> filteredListPerson = new ArrayList<>();
                List<Object> filteredListEvent = new ArrayList<>();
                List<Object> filteredResult = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredResult.addAll(mObjects);
                }
                else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Object object : mObjects) {

                        if (object.getClass() == Person.class) {
                            Person person = (Person) object;
                            if (person.getFirstName().toLowerCase().contains(filterPattern) || person.getLastName().toLowerCase().contains(filterPattern)) {
                                filteredListPerson.add(object);
                            }
                        }
                        else {
                            Event event = (Event) object;
                            Person person = mFamilyData.findPersonbyId(event.getPersonID());
                            String year = Integer.toString(event.getYear());
                            if (event.getEventType().toLowerCase().contains(filterPattern) || event.getCity().toLowerCase().contains(filterPattern)
                                    || event.getCountry().toLowerCase().contains(filterPattern) || "()".contains(filterPattern)
                                    || person.getFirstName().toLowerCase().contains(filterPattern) || person.getLastName().toLowerCase().contains(filterPattern) || year.contains(filterPattern)) {
                                filteredListEvent.add(object);
                            }
                        }
                    }

                    for (Object object : filteredListPerson) {
                        filteredResult.add(object);
                    }
                    for (Object object : filteredListEvent) {
                        filteredResult.add(object);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredResult;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mObjects.clear();
                mObjects.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };

    }


}
