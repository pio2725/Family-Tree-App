package Data;

import com.google.gson.JsonArray;

import java.util.Random;

public class RandomMnames {

    public String getRandomMnames() {

        DataImport dataImport = new DataImport();
        JsonArray mnames = dataImport.readMnames();

        Random rand = new Random();
        int randNum = rand.nextInt(mnames.size());

        String str = mnames.get(randNum).toString();

        StringBuilder sb = new StringBuilder();
        sb.append(str);

        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
