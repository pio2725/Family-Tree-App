package Reqeust;

/** The body of a fill request with username and the specified number */
public class FillRequest {

    /** The username of the user */
    private String userName;

    /** The number of generations of ancestors to be generated */
    private int generationNum;

    /** Creating a fill request with the username and the generation number
     *  @param userName the username of the user
     *  @param generationNum the number of generations to be generated
     */
    public FillRequest(String userName, int generationNum) {
        this.userName = userName;
        this.generationNum = generationNum;
    }

    public FillRequest () {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGenerationNum() {
        return generationNum;
    }

    public void setGenerationNum(int generationNum) {
        this.generationNum = generationNum;
    }
}
