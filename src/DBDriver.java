/**
 * Created by rebeccakehl on 5/12/15.
 */

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class DBDriver {

    private MongoClient mongoClient;
    private MongoCollection<Document> photoData;
    private MongoCollection<Document> catPhotos;

    private static final Gson gson = new Gson();

    public DBDriver() {

        mongoClient = new MongoClient("localhost");
        MongoDatabase database = mongoClient.getDatabase("catboop");

        photoData = database.getCollection("photoData");
        photoData.createIndex(new BasicDBObject("nose", "2d"));
        catPhotos = database.getCollection("catPhotos");
    }

    public void close() {
        mongoClient.close();
    }

    public CatPhoto getCat(int noseX, int noseY) {
        //Nearest neighbor search on photo data for noseX and noseY
        Double locationLongitude = new Double((double) noseY / 10.0);
        Double locationLatitude = new Double((double) noseX / 10.0);

        BasicDBObject query = new BasicDBObject("nose", new BasicDBObject("$near", new Double[]{locationLatitude, locationLongitude}));
        FindIterable<Document> photoDataDocuments = photoData.find(query).limit(10);
        MongoCursor<Document> cursor = photoDataDocuments.iterator();
        Random rand = new Random();
        int pickedNumber = rand.nextInt(10);
        for (int i=0; i<pickedNumber; i++) {
            cursor.next();
        }
        Document photoDataDocument = cursor.next();

        //Document photoDataDocument = photoData.find(query).first();
        String catPhotoId = photoDataDocument.get("photoId").toString();

        //Document from CatPhoto
        Document catPhotoDocFromMongo = catPhotos.find(eq("_id", new ObjectId(catPhotoId))).first();

        //Joint Document
        CatPhoto ultimateCatPhoto = new CatPhoto(catPhotoDocFromMongo, photoDataDocument);

        return ultimateCatPhoto;
    }

    public boolean insertCat(int width, int height, int noseX, int noseY, String URL) {

        if (width <= 0 || height <= 0 || noseX < 0 || noseX >= width || noseY < 0 || noseY >= height || URL == null || URL == "") {
            System.out.println("Arguments to insertCat failed");
            return false;
        }

        try {
            //Insert into CatPhotos
            ArrayList<Integer> oldNoseArray = new ArrayList<>();
            oldNoseArray.add(noseX);
            oldNoseArray.add(noseY);
            Document photoDoc = new Document("width", width)
                    .append("height", height)
                    .append("oldNose", oldNoseArray)
                    .append("URL", URL);

            catPhotos.insertOne(photoDoc);

            //Get Id from CatPhoto
            Document photoDocFromMongo = catPhotos.find(eq("URL", URL)).first();
            String catPhotoId = photoDocFromMongo.get("_id").toString();

            double x2 = 0;
            double y2 = 0;
            double min = 0.00;
            double max = 400.0;
            //Shifted copies of the same photo will have between minSpacing and 2*minSpacing pixels between them
            double minSpacing = 20.0;

            //Calculate Shifts
            if (width > height) {

                y2 = (max / (double) height) * (double) noseY;

                //Find range of x-values for nose within 400x400 frame
                double xmin = max - ((max / (double) height) * (double) (width - noseX));
                xmin = (xmin < min ? min : (xmin > max ? max : xmin));
                double xmax = (max / (double) height) * (double) noseX;
                xmax = (xmax < min ? min : (xmax > max ? max : xmax));

                int nPts = (int) ((xmax - xmin) / minSpacing);
                nPts = (nPts < 2 ? 2 : nPts);

                //Insert photoData documents for 10 points in range
                for (int i = 0; i <= nPts; i++) {
                    x2 = xmin + (xmax - xmin) * i / (double) nPts;

                    double xShift = ((max / (double) height) * (double) noseX) - x2;
                    double yShift = 0;
                    ArrayList<Double> noseArray = new ArrayList<>();
                    noseArray.add(x2 / 10.0); //Scale down by 10 to fit within latitude/longitude coordinate system
                    noseArray.add(y2 / 10.0);

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

                int nPts = (int) ((ymax - ymin) / minSpacing);
                nPts = (nPts < 2 ? 2 : nPts);

                //Insert photoData documents for 10 points in range
                for (int i = 0; i < nPts; i++) {
                    y2 = ymin + (ymax - ymin) * i / nPts;

                    double xShift = 0;
                    double yShift = ((max / (double) width) * (double) noseY) - y2;
                    ArrayList<Double> noseArray = new ArrayList<>();
                    noseArray.add(x2 / 10.0);
                    noseArray.add(y2 / 10.0);

                    Document dataDoc = new Document("photoId", catPhotoId)
                            .append("xShift", (int) xShift)
                            .append("yShift", (int) yShift)
                            .append("nose", noseArray);

                    photoData.insertOne(dataDoc);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR");
            return false;
        }

        return true;
    } //End insertCat()

    public static void main(String[] args) {
        DBDriver dbDriver = new DBDriver();
        dbDriver.exportPointList("C:\\Users\\Parker\\Documents\\BigData\\pointlist.csv");
        dbDriver.close();
        return;
    }

    private void exportPointList(String filepath) {
        try {
            String pointlist = "";
            FindIterable<Document> results = photoData.find();
            MongoCursor<Document> cursor = results.iterator();
            int i = 0;
            System.out.print("Reading points");
            while (cursor.hasNext()) {
                Document pdDoc = cursor.next();
                ArrayList<Double> arr = (ArrayList<Double>) pdDoc.get("nose");
                pointlist += ((int) (arr.get(0) * 10.0)) + "," + ((int) (arr.get(1) * 10.0)) + (cursor.hasNext() ? "\n" : "");
                i++;
                if (i % 720 == 0)
                    System.out.print(".");
            }

            File f = new File(filepath);
            if (!f.exists())
                f.createNewFile();
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(pointlist);
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
