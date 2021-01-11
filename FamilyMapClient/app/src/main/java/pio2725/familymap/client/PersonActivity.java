package pio2725.familymap.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class PersonActivity extends AppCompatActivity {

    private static final String EXTRA_PERSON_ID = "extra_person_Id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.person_container);

        if (fragment == null) {
            String personId = (String) getIntent().getSerializableExtra(EXTRA_PERSON_ID);
            fragment = PersonFragment.newInstance(personId);
            fm.beginTransaction().add(R.id.person_container, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context packageContent, String personId) {
        Intent intent = new Intent(packageContent, PersonActivity.class);
        intent.putExtra(EXTRA_PERSON_ID, personId);
        return intent;
    }
}
