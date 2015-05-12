import com.google.gson.annotations.SerializedName;

/**
 * Created by rebeccakehl on 5/12/15.
 */
public class CatPhoto {
    private String id;
    private String photoDataId;
    private int width = 0;
    private int height = 0;
    private int noseX = 0;
    private int noseY = 0;
    private int xShift = 0;
    private int yShift = 0;
    private String URL;

    public CatPhoto() {
    }

    public String getId() {
        return id;
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

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPhotoDataId() {
        return photoDataId;
    }

    public void setPhotoDataId(String photoDataId) {
        this.photoDataId = photoDataId;
    }
}