package pio2725.familymap.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private Switch mLifeStoryLineSwitch;
    private Switch mFamilyTreeLineSwitch;
    private Switch mSpouseSwitch;
    private Switch mFatherSideSwitch;
    private Switch mMotherSideSwitch;
    private Switch mMaleEventsSwitch;
    private Switch mFemaleEventsSwitch;
    private LinearLayout mLogoutLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mLifeStoryLineSwitch = (Switch) view.findViewById(R.id.life_story_switch);
        mFamilyTreeLineSwitch = (Switch) view.findViewById(R.id.family_tree_switch);
        mSpouseSwitch = (Switch) view.findViewById(R.id.spouse_line_switch);
        mFatherSideSwitch = (Switch) view.findViewById(R.id.father_side_switch);
        mMotherSideSwitch = (Switch) view.findViewById(R.id.mother_side_switch);
        mMaleEventsSwitch = (Switch) view.findViewById(R.id.male_event_switch);
        mFemaleEventsSwitch = (Switch) view.findViewById(R.id.female_event_switch);
        mLogoutLayout = (LinearLayout) view.findViewById(R.id.logout_layout);

        setCurrentSwitchSettings();

        mLogoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Log out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            saveSwitchState();
            startActivity(intent);
        }
        return true;
    }

    private void saveSwitchState() {
        FamilyMapSetting familyMapSetting = FamilyMapSetting.get();
        familyMapSetting.setLifeStoryLineOn(mLifeStoryLineSwitch.isChecked());
        familyMapSetting.setFamilyTreeLineOn(mFamilyTreeLineSwitch.isChecked());
        familyMapSetting.setSpouseLineOn(mSpouseSwitch.isChecked());
        familyMapSetting.setFatherSideOn(mFatherSideSwitch.isChecked());
        familyMapSetting.setMotherSideOn(mMotherSideSwitch.isChecked());
        familyMapSetting.setMaleEventsOn(mMaleEventsSwitch.isChecked());
        familyMapSetting.setFemaleEventsOn(mFemaleEventsSwitch.isChecked());
    }

    private void setCurrentSwitchSettings() {
        FamilyMapSetting familyMapSetting = FamilyMapSetting.get();
        mLifeStoryLineSwitch.setChecked(familyMapSetting.isLifeStoryLineOn());
        mFamilyTreeLineSwitch.setChecked(familyMapSetting.isFamilyTreeLineOn());
        mSpouseSwitch.setChecked(familyMapSetting.isSpouseLineOn());
        mFatherSideSwitch.setChecked(familyMapSetting.isFatherSideOn());
        mMotherSideSwitch.setChecked(familyMapSetting.isMotherSideOn());
        mMaleEventsSwitch.setChecked(familyMapSetting.isMaleEventsOn());
        mFemaleEventsSwitch.setChecked(familyMapSetting.isFemaleEventsOn());
    }
}
