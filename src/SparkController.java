/**
 * Created by Parker on 5/12/2015.
 */

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

    } //End constructor

    public static void main(String[] args) {
        SparkController sparkController = new SparkController();
    }

} //End class SparkController
