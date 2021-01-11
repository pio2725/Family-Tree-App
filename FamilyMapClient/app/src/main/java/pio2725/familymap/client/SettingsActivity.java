package pio2725.familymap.client;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FragmentManager fm = getSupportFragmentManager();
        Fragment settingFragment = fm.findFragmentById(R.id.settings_container);

        if (settingFragment == null) {
            settingFragment = new SettingsFragment();
            fm.beginTransaction()
                    .add(R.id.settings_container, settingFragment)
                    .commit();
        }
    }
}
