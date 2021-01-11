package pio2725.familymap.client;

public class FamilyMapSetting {

    private boolean lifeStoryLineOn;
    private boolean familyTreeLineOn;
    private boolean spouseLineOn;
    private boolean fatherSideOn;
    private boolean motherSideOn;
    private boolean maleEventsOn;
    private boolean femaleEventsOn;

    private static FamilyMapSetting sFamilyMapSetting;

    public static FamilyMapSetting get() {
        if (sFamilyMapSetting == null) {
            sFamilyMapSetting = new FamilyMapSetting();
        }
        return sFamilyMapSetting;
    }

    private FamilyMapSetting() {
        lifeStoryLineOn = false;
        familyTreeLineOn = false;
        spouseLineOn = false;
        fatherSideOn = true;
        motherSideOn = true;
        maleEventsOn = true;
        femaleEventsOn = true;
    }

    public boolean isLifeStoryLineOn() {
        return lifeStoryLineOn;
    }

    public void setLifeStoryLineOn(boolean lifeStoryLineOn) {
        this.lifeStoryLineOn = lifeStoryLineOn;
    }

    public boolean isFamilyTreeLineOn() {
        return familyTreeLineOn;
    }

    public void setFamilyTreeLineOn(boolean familyTreeLineOn) {
        this.familyTreeLineOn = familyTreeLineOn;
    }

    public boolean isSpouseLineOn() {
        return spouseLineOn;
    }

    public void setSpouseLineOn(boolean spouseLineOn) {
        this.spouseLineOn = spouseLineOn;
    }

    public boolean isFatherSideOn() {
        return fatherSideOn;
    }

    public void setFatherSideOn(boolean fatherSideOn) {
        this.fatherSideOn = fatherSideOn;
    }

    public boolean isMotherSideOn() {
        return motherSideOn;
    }

    public void setMotherSideOn(boolean motherSideOn) {
        this.motherSideOn = motherSideOn;
    }

    public boolean isMaleEventsOn() {
        return maleEventsOn;
    }

    public void setMaleEventsOn(boolean maleEventsOn) {
        this.maleEventsOn = maleEventsOn;
    }

    public boolean isFemaleEventsOn() {
        return femaleEventsOn;
    }

    public void setFemaleEventsOn(boolean femaleEventsOn) {
        this.femaleEventsOn = femaleEventsOn;
    }
}
