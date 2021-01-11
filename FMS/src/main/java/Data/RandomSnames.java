package Data;

import com.google.gson.JsonArray;

import java.util.Random;

public class RandomSnames {

    public String getRandomSnames() {

        DataImport dataImport = new DataImport();
        JsonArray snames = dataImport.readSnames();

        Random rand = new Random();
        int randNum = rand.nextInt(snames.size());

        String str = snames.get(randNum).toString();
        StringBuilder sb = new StringBuilder();
        sb.append(str);

        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
