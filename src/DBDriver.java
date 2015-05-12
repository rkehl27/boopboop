/**
 * Created by rebeccakehl on 5/12/15.
 */
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.json.JsonReader;

public class DBDriver {
    private MongoCollection<Document> photoData;
    private MongoCollection<Document> catPhotos;
}
