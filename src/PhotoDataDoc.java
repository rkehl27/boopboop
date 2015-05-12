import com.google.gson.annotations.SerializedName;

/**
 * Created by rebeccakehl on 5/12/15.
 */
public class PhotoDataDoc {
    @SerializedName("_id")
    private String id;
    @SerializedName("photoId")
    private String catPhotoId;
    @SerializedName("nose")
    private Integer[] noseArray;
    @SerializedName("xShift")
    private int xShift = 0;
    @SerializedName("yShift")
    private int yShift = 0;

    public String getId() {
        return id;
    }

    public String getCatPhotoId() {
        return catPhotoId;
    }

    public Integer[] getNoseArray() {
        return noseArray;
    }

    public int getxShift() {
        return xShift;
    }

    public int getyShift() {
        return yShift;
    }
}