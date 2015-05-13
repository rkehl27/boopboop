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
    private MongoCollection<Document> photoData;
    private MongoCollection<Document> catPhotos;

    public DBDriver() {
        MongoClient mongoClient = new MongoClient("localhost");

        MongoDatabase database = mongoClient.getDatabase("");

        photoData = database.getCollection("");
        catPhotos = database.getCollection("");
    }

    public CatPhoto getCat(int noseX, int noseY) {
        //Nearest neighbor search on photo data for noseX and noseY

        /**
         * String sLng = “15.5”;	Double dLng = new Double(sLng);
         String sLat = “150.11”;	Double dLat = new Double(sLat);
         String sDistance = “40”;
         DBCursor cur = collectionUM.find(new BasicDBObject(“loc”,JSON.parse(“{$near : [ ” + dLng + “,” + dLat + ” ] , $maxDistance : ” + sDistance + “}”))).limit(10);
         */

        //        ArrayList<Integer> noseArray = new ArrayList<>();
//        noseArray.add(noseX);
//        noseArray.add(noseY);
//
//        BasicDBObject query = new BasicDBObject("$near", noseArray);
//
//        Document catPhoto = photoData.find(query).first();
        Gson gson = new Gson();


        //Nearest Neighbor query on PhotoData
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
        CatPhoto ultimateCatPhoto = new CatPhoto();

        Integer[] noseArray = photoDataDoc.getNoseArray();
        ultimateCatPhoto.setNoseX(noseArray[0]);
        ultimateCatPhoto.setNoseY(noseArray[1]);

        ultimateCatPhoto.setId(catPhotoDoc.getId());
        ultimateCatPhoto.setPhotoDataId(photoDataDoc.getId());
        ultimateCatPhoto.setWidth(catPhotoDoc.getWidth());
        ultimateCatPhoto.setHeight(catPhotoDoc.getHeight());
        ultimateCatPhoto.setxShift(photoDataDoc.getxShift());
        ultimateCatPhoto.setyShift(photoDataDoc.getyShift());
        ultimateCatPhoto.setURL(catPhotoDoc.getURL());

        return ultimateCatPhoto;
    }

    public boolean insertCat(int width, int height, int noseX, int noseY, String URL) {
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
        Gson gson = new Gson();
        CatPhotoDoc catPhoto = gson.fromJson(photoString, CatPhotoDoc.class);
        String catPhotoId = catPhoto.getId();

        double x2 = 0;
        double y2 = 0;
        double min = 0.00;
        double max = 400.0;

        //Calculate Shift
        if (width > height) {

            y2 = (max/(double)height) * (double)noseY;

            double xmin = max - ((max/(double)height) * (double)(width-noseX));
            double xmax = (max/(double)height)*(double)noseX;

            double increment = (xmax - xmin)/10.0;

            for (int i = 0; i <10 ; i++) {
                x2 = noseX + increment;

                if (x2 < min ) {
                    x2 = min;
                }
                if (x2 > max) {
                    x2 = max;
                }

                double xShift = ((max/(double) height) * (double) noseX) - x2;
                double yShift = ((max/(double) width) * (double) noseY) - y2;

                Document dataDoc = new Document("photoId", catPhotoId)
                        .append("xShift", (int)xShift)
                        .append("yShift", (int)yShift)
                        .append("nose", noseArray);

                photoData.insertOne(dataDoc);
            }

        } else if (height > width) {
            x2 = (max/(double)width) * (double)noseX;

            double ymin = max - ((max/(double)width) * (double)(height-noseY));
            double ymax = (max/(double)width)*(double)noseY;

            double increment = (ymax - ymin)/10.0;

            for (int i = 0; i <10 ; i++) {
                y2 = noseY + increment;

                if (y2 < min ) {
                    y2 = min;
                }
                if (y2 > max) {
                    y2 = max;
                }

                double xShift = ((max/(double) height) * (double) noseX) - x2;
                double yShift = ((max/(double) width) * (double) noseY) - y2;

                Document dataDoc = new Document("photoId", catPhotoId)
                        .append("xShift", (int)xShift)
                        .append("yShift", (int)yShift)
                        .append("nose", noseArray);

                photoData.insertOne(dataDoc);
            }

        } else {
            //height == width
        }

        return true;
    }
}
