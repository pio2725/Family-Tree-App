package Data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;

public class DataImport {

    public JsonArray readLocations() {

        String path = "C:\\Users\\pio27\\IdeaProjects\\FMS\\json\\locations.json";

        try {
            FileReader is = new FileReader(new File(path));

            BufferedReader br = new BufferedReader(is);
            StringBuilder sb = new StringBuilder();
            String readString;
            while ((readString = br.readLine()) != null) {
                sb.append(readString);
                sb.append("\n");
            }
            is.close();
            br.close();

            Gson gson = new Gson();
            JsonObject locationJson = gson.fromJson(sb.toString(), JsonObject.class);
            return locationJson.getAsJsonArray("data");
        }
        catch (IOException e) {
            System.out.println("here");
            e.printStackTrace();
        }
        return null;
    }

    public JsonArray readFnames() {

        String path = "C:\\Users\\pio27\\IdeaProjects\\FMS\\json\\fnames.json";

        try {
            FileReader is = new FileReader(new File(path));

            BufferedReader br = new BufferedReader(is);
            StringBuilder sb = new StringBuilder();
            String readString;
            while ((readString = br.readLine()) != null) {
                sb.append(readString);
                sb.append("\n");
            }
            is.close();
            br.close();

            Gson gson = new Gson();
            JsonObject fnamesJson = gson.fromJson(sb.toString(), JsonObject.class);
            return fnamesJson.getAsJsonArray("data");
        }
        catch (IOException e) {
            System.out.println("here");
        }
        return null;
    }

    public JsonArray readMnames() {

        String path = "C:\\Users\\pio27\\IdeaProjects\\FMS\\json\\mnames.json";

        try {
            FileReader is = new FileReader(new File(path));

            BufferedReader br = new BufferedReader(is);
            StringBuilder sb = new StringBuilder();
            String readString;
            while ((readString = br.readLine()) != null) {
                sb.append(readString);
                sb.append("\n");
            }
            is.close();
            br.close();

            Gson gson = new Gson();
            JsonObject mnaesJson = gson.fromJson(sb.toString(), JsonObject.class);
            return mnaesJson.getAsJsonArray("data");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("here");

        }
        return null;
    }

    public JsonArray readSnames() {

        String path = "C:\\Users\\pio27\\IdeaProjects\\FMS\\json\\snames.json";
        try {
            FileReader is = new FileReader(new File(path));
            BufferedReader br = new BufferedReader(is);
            StringBuilder sb = new StringBuilder();

            String readString;
            while ((readString = br.readLine()) != null) {
                sb.append(readString);
            }
            is.close();
            br.close();

            Gson gson = new Gson();
            JsonObject snamesJson = gson.fromJson(sb.toString(), JsonObject.class);
            return snamesJson.getAsJsonArray("data");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
