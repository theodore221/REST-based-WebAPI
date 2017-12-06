import java.util.ArrayList;
/*
Image Class,
All uploaded images have a image object created to keep track of all images
 */
public class Image {

    private String name;
    private String filePath;
    private static ArrayList<Image> Images = new ArrayList<>();


    public Image(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
        Images.add(this);
    }
/*
Basic getter functions
 */
    public String getName(){
        return name;
    }

    public String getfilePath(){
        return filePath;
    }
/*
Returns list of image objects
 */
    public static ArrayList<Image> getAll() {
        return Images;
    }
}
