package pio2725.familymap.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class EventActivity extends AppCompatActivity {

    private static final String EXTRA_EVENT_ID = "extra_event_Id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.event_container);
        if (fragment == null) {
            String eventId = (String) getIntent().getSerializableExtra(EXTRA_EVENT_ID);
            fragment = MapFragment.newInstance(eventId);

            fm.beginTransaction()
                    .add(R.id.event_container, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context packageContext, String eventId) {
        Intent intent = new Intent(packageContext, EventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        return intent;
    }
}
