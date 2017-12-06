import java.util.ArrayList;

public class Image {

    private String name;
    private String filePath;
    private static ArrayList<Image> Images = new ArrayList<>();


    public Image(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
        Images.add(this);
    }

    public String getName(){
        return name;
    }

    public String getfilePath(){
        return filePath;
    }

    public static ArrayList<Image> getAll() {
        return Images;
    }
}
