package Data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.Random;

public class RandomLocation {

    private String city;
    private String country;
    private float longitude;
    private float latitude;

    public RandomLocation getRandomLocation() {

        Gson gson = new Gson();
        DataImport dataImport = new DataImport();

        JsonArray locationArray = dataImport.readLocations();

        Random rand = new Random();
        int randNum = rand.nextInt(locationArray.size());
        RandomLocation randomLocation = gson.fromJson(locationArray.get(randNum), RandomLocation.class);

        return randomLocation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
