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
        ArrayList<Integer> noseArray = new ArrayList<>();
        noseArray.add(noseX);
        noseArray.add(noseY);
        Document photoDoc = new Document("width", width)
                .append("height", height)
                .append("oldNose", noseArray)
                .append("URL", URL);

        catPhotos.insertOne(photoDoc);

        Document photoDocFromMongo = catPhotos.find(eq("URL", URL)).first();
        String photoString = photoDocFromMongo.toJson();
        Gson gson = new Gson();
        CatPhotoDoc catPhoto = gson.fromJson(photoString, CatPhotoDoc.class);
        String catPhotoId = catPhoto.getId();

        Document dataDoc = new Document("photoId", catPhotoId)
                .append("xShift", 0)
                .append("yShift", 0)
                .append("nose", noseArray);

        photoData.insertOne(dataDoc);

        return false;
    }
}