/**
 * Created by rebeccakehl on 4/21/15.
 */
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.json.JsonReader;

import java.sql.Date;
import java.util.ArrayList;

public class ZipsCollection {

    public MongoCollection<Document> zips;

    public ZipsCollection() {
        MongoClient mongoClient = new MongoClient("localhost");

        MongoDatabase database = mongoClient.getDatabase("mydb");

        zips = database.getCollection("zips");
    }

    public Document getFirst() {
        Document zip = zips.find().first();
        JsonReader reader = new JsonReader(zip.toString());
        return zip;
    }
}