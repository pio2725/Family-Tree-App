package Model;

import java.util.UUID;

public class RandomGenerator {

    public String getRandomString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0,8);
    }
}
