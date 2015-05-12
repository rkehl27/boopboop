import com.google.gson.annotations.SerializedName;

/**
 * Created by rebeccakehl on 5/12/15.
 */
public class CatPhotoDoc {
    @SerializedName("_id")
    private String id;
    @SerializedName("width")
    private int width = 0;
    @SerializedName("height")
    private int height = 0;
    @SerializedName("oldNose")
    private Integer[] noseArray;
    @SerializedName("URL")
    private String URL;

    public String getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Integer[] getNoseArray() {
        return noseArray;
    }

    public String getURL() {
        return URL;
    }
}