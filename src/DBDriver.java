/**
 * Created by rebeccakehl on 5/12/15.
 */

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

import jdk.nashorn.internal.parser.JSONParser;
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

        Document catPhoto = photoData.find().first();


        return null;
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
        CatPhoto catPhoto = gson.fromJson(photoString, CatPhoto.class);

        Document dataDoc = new Document("photoId", catPhoto.getPhotoDataId())
                .append("xShift", 0)
                .append("yShift", 0)
                .append("nose", noseArray);

        photoData.insertOne(dataDoc);

        return false;
    }
}
