/**
 * Created by rebeccakehl on 4/20/15.
 */
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.*;

public class HelloWorld {

    private static ZipsCollection zipsCollection;
//
//    public static void main(String[] args) {
//        zipsCollection = new ZipsCollection();
//
//        get("/hello", new Route() {
//            @Override
//            public Object handle(Request request, Response response) {
//                return zipsCollection.getFirst();
//            }
//        });
//
//
//    }

    public static void main(String[] args) {
        zipsCollection = new ZipsCollection();
        get("/hello", (req, res) -> zipsCollection.getFirst());
    }
}