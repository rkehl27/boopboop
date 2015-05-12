/**
 * Created by Parker on 5/12/2015.
 */

import spark.ResponseTransformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;

public class BoopUtil {

    public static String serveFlatFile(String file) {
        String page = "";
        try {
            FileInputStream s = new FileInputStream(new File(file));
            Scanner in = new Scanner(s);

            while (in.hasNext()) {
                page += in.nextLine() + "\n";
            }
            in.close();
            s.close();
            return page;
        } catch (IOException e) {
            page = "<html><body><h1>Error:</h1><p>" + e.getMessage() + "</p></body></html>";
        }
        return page;
    } //End serveFlatFile()

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return BoopUtil::toJson;
    }

} //End class BoopUtil