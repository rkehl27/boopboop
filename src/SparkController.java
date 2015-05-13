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
            System.out.println("Hello " + req.params(":xy"));

            String xy = req.params(":xy");
            int clickX = Integer.parseInt(xy.split(",")[0]);
            int clickY = Integer.parseInt(xy.split(",")[1]);

            CatPhoto cp = new CatPhoto("1234", "http://tonsofcats.com/wp-content/uploads/2013/10/l-Derpy-aww-550x486.jpg");
            //CatPhoto cp = DBDriver.findClosestCat(clickX, clickY);

            cp.setWidth(550);
            cp.setHeight(486);
            cp.setNoseX(clickX);
            cp.setNoseY(clickY);
            cp.setxShift(246 - clickX);
            cp.setyShift(158 - clickY);

            return cp;
        }, BoopUtil.json());

    } //End constructor

    public static void main(String[] args) {
        SparkController sparkController = new SparkController();
    }

} //End class SparkController
