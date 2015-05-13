
import com.google.gson.annotations.SerializedName;

/**
 * Created by rebeccakehl on 5/12/15.
 */
public class CatPhoto {
    private String id;
    @SerializedName("_id")
    private String photoDataId;
    @SerializedName("width")
    int width = 0;
    @SerializedName("height")
    int height = 0;
    @SerializedName("noseX")
    int noseX = 0;
    @SerializedName("noseY")
    int noseY = 0;
    @SerializedName("xShift")
    int xShift = 0;
    @SerializedName("yShift")
    int yShift = 0;
    @SerializedName("URL")
    String URL;

    public CatPhoto() {
        //Set nothing
    }

    public CatPhoto(String id, String URL) {
        this.id = id;
        this.URL = URL;
    }

    public CatPhoto(CatPhotoDoc cpDoc, PhotoDataDoc pdDoc) {
        this.id = cpDoc.getId();
        this.photoDataId = pdDoc.getId();
        this.width = cpDoc.getWidth();
        this.height = cpDoc.getHeight();
        this.xShift = pdDoc.getxShift();
        this.yShift = pdDoc.getyShift();
        this.URL = cpDoc.getURL();
        this.noseX = pdDoc.getNoseArray()[0];
        this.noseY = pdDoc.getNoseArray()[1];
    }

    public String getPhotoDataId() {
        return photoDataId;
    }

    public void setPhotoDataId(String photoDataId) {
        this.photoDataId = photoDataId;
    }

    public String getId() {
        return id;
    }

    public String getURL() {
        return URL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getNoseX() {
        return noseX;
    }

    public void setNoseX(int noseX) {
        this.noseX = noseX;
    }

    public int getNoseY() {
        return noseY;
    }

    public void setNoseY(int noseY) {
        this.noseY = noseY;
    }

    public int getxShift() {
        return xShift;
    }

    public void setxShift(int xShift) {
        this.xShift = xShift;
    }

    public int getyShift() {
        return yShift;
    }

    public void setyShift(int yShift) {
        this.yShift = yShift;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
