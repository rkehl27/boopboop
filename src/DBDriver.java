/**
 * Created by rebeccakehl on 5/12/15.
 */

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;
import org.bson.Document;

import java.util.ArrayList;

public class DBDriver {

    private MongoClient mongoClient;
    private MongoCollection<Document> photoData;
    private MongoCollection<Document> catPhotos;

    private static final Gson gson = new Gson();

    public DBDriver() {

        mongoClient = new MongoClient("localhost");
        MongoDatabase database = mongoClient.getDatabase("catboop");

        photoData = database.getCollection("photoData");
        catPhotos = database.getCollection("catPhotos");
    }

    public void close() {
        mongoClient.close();
    }

    public CatPhoto getCat(int noseX, int noseY) {
        //Nearest neighbor search on photo data for noseX and noseY
        Double locationLongitude = new Double(noseY);
        Double locationLatitude = new Double(noseX);

        BasicDBObject query = new BasicDBObject("$near", new Double[]{locationLongitude, locationLatitude});
        Document photoDataDocument = photoData.find(query).first();

        //Document from PhotoData
        PhotoDataDoc photoDataDoc = gson.fromJson(photoDataDocument.toJson(), PhotoDataDoc.class);
        String catPhotoId = photoDataDoc.getCatPhotoId();

        //Document from CatPhoto
        Document catPhotoDocFromMongo = catPhotos.find(eq("_id",catPhotoId )).first();
        CatPhotoDoc catPhotoDoc = gson.fromJson(catPhotoDocFromMongo.toJson(), CatPhotoDoc.class);

        //Joint Document
        CatPhoto ultimateCatPhoto = new CatPhoto(catPhotoDoc, photoDataDoc);

        return ultimateCatPhoto;
    }

    public boolean insertCat(int width, int height, int noseX, int noseY, String URL) {

        if (width <= 0 || height <= 0 || noseX < 0 || noseX >= width || noseY < 0 || noseY >= height || URL == null || URL == "")
            return false;

        try {
            //Insert into CatPhotos
            ArrayList<Integer> noseArray = new ArrayList<>();
            noseArray.add(noseX);
            noseArray.add(noseY);
            Document photoDoc = new Document("width", width)
                    .append("height", height)
                    .append("oldNose", noseArray)
                    .append("URL", URL);

            catPhotos.insertOne(photoDoc);

            //Get Id from CatPhoto
            Document photoDocFromMongo = catPhotos.find(eq("URL", URL)).first();
            String photoString = photoDocFromMongo.toJson();
            System.out.println("\n\nRetrieved document:\n\n" + photoString + "\n");
            String newPhotoString = BoopUtil.jsonToCatPhoto(photoString);
            System.out.println("Cleaned document:\n\n" + newPhotoString + "\n");
            String catPhotoId = "5553bb35aa1eb28e8034bc49";
            //CatPhotoDoc catPhoto = gson.fromJson(photoString, CatPhotoDoc.class); <- this fails because the _id is an ObjectId, not a string
            //String catPhotoId = catPhoto.getId();

            double x2 = 0;
            double y2 = 0;
            double min = 0.00;
            double max = 400.0;

            //Calculate Shifts
            if (width > height) {

                y2 = (max / (double) height) * (double) noseY;

                //Find range of x-values for nose within 400x400 frame
                double xmin = max - ((max / (double) height) * (double) (width - noseX));
                xmin = (xmin < min ? min : (xmin > max ? max : xmin));
                double xmax = (max / (double) height) * (double) noseX;
                xmax = (xmax < min ? min : (xmax > max ? max : xmax));

                //Insert photoData documents for 10 points in range
                for (int i = 0; i < 10; i++) {
                    x2 = xmin + (xmax - xmin) * i / 10.0;

                    double xShift = ((max / (double) height) * (double) noseX) - x2;
                    double yShift = 0;

                    Document dataDoc = new Document("photoId", catPhotoId)
                            .append("xShift", (int) xShift)
                            .append("yShift", (int) yShift)
                            .append("nose", noseArray);

                    photoData.insertOne(dataDoc);
                }

            } else { //width <= height

                x2 = (max / (double) width) * (double) noseX;

                //Find range of y-values for nose within 400x400 frame
                double ymin = max - ((max / (double) width) * (double) (height - noseY));
                ymin = (ymin < min ? min : (ymin > max ? max : ymin));
                double ymax = (max / (double) width) * (double) noseY;
                ymax = (ymax < min ? min : (ymax > max ? max : ymax));

                //Insert photoData documents for 10 points in range
                for (int i = 0; i < 10; i++) {
                    y2 = ymin + (ymax - ymin) * i / 10.0;

                    double xShift = 0;
                    double yShift = ((max / (double) width) * (double) noseY) - y2;

                    Document dataDoc = new Document("photoId", catPhotoId)
                            .append("xShift", (int) xShift)
                            .append("yShift", (int) yShift)
                            .append("nose", noseArray);

                    photoData.insertOne(dataDoc);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    } //End insertCat()

    public void test() {
        MongoDatabase db = mongoClient.getDatabase("catboop");
        for (String s : db.listCollectionNames())
            System.out.println(s);
    }

    public static void main(String[] args) {
        DBDriver dbDriver = new DBDriver();
        dbDriver.insertCat(500, 300, 100, 200, "photos/somecat.jpg");
        dbDriver.close();
        return;
    }

}
