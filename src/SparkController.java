/**
 * Created by Parker on 5/12/2015.
 */

import java.net.URLDecoder;

import static spark.Spark.*;

public class SparkController {

    public SparkController() {

        staticFileLocation("src/main/resources/public");
        externalStaticFileLocation("src/main/resources/public");

        get("/", "text/html", (req, res) -> {
            res.redirect("index.html");
            return null;
        });

        get("/cat/:xy", "application/json", (req, res) -> {
            System.out.println("Click registered at: " + req.params(":xy"));

            String xy = req.params(":xy");
            int clickX = Integer.parseInt(xy.split(",")[0]);
            int clickY = Integer.parseInt(xy.split(",")[1]);

            DBDriver dbDriver = new DBDriver();
            CatPhoto cp = dbDriver.getCat(clickX, clickY);
            dbDriver.close();

            return cp;
        }, BoopUtil.json());

        post("/submit/:str", "text/html", (req, res) -> {
            System.out.println("Posted");
            String params = req.params(":str");
            String[] list = params.split(",");
            Integer width = Integer.parseInt(list[0]);
            Integer height = Integer.parseInt(list[1]);
            Integer noseX = Integer.parseInt(list[2]);
            Integer noseY = Integer.parseInt(list[3]);
            String URL = URLDecoder.decode(list[4], "UTF-8");
            System.out.println(width + " " + height + " " + noseX + " " + noseY + " " + URL);
            DBDriver dbDriver = new DBDriver();
            boolean b = dbDriver.insertCat(width, height, noseX, noseY, URL);
            if (!b)
                System.out.println("Post failed.");
            dbDriver.close();
            res.redirect("index.html");
            return null;
        });

    } //End constructor

    public static void main(String[] args) {
        SparkController sparkController = new SparkController();
    }

} //End class SparkController
