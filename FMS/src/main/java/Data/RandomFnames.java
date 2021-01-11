package Data;

import com.google.gson.JsonArray;

import java.util.Random;

public class RandomFnames {

    public String getRandomFnames() {

        DataImport dataImport = new DataImport();
        JsonArray fnames = dataImport.readFnames();

        Random rand = new Random();
        int randNum = rand.nextInt(fnames.size());

        String str = fnames.get(randNum).toString();

        StringBuilder sb = new StringBuilder();
        sb.append(str);

        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }


}
