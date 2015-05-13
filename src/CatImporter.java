/**
 * Created by Parker on 5/12/2015.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class CatImporter {

    public static final String catPath = "src/main/resources/public/photos";
    private static DBDriver dbDriver;

    public static void main(String[] args) {
        dbDriver = new DBDriver();
        importCats(catPath);
        dbDriver.close();
    } //End main()

    public static int importCats(String folderPath) {

        File top = new File(folderPath);
        if (!top.exists() || !top.isDirectory()) {
            System.err.println("Folder '" + folderPath + "' does not exist.");
            System.exit(-1);
        }
        int total = recursiveImportCats(top, 0, 0);
        System.out.println("\n\nTotal: " + total + " cats found.");
        return total;

    } //End importCats()

    private static int recursiveImportCats(File folder, int total, int tab) {

        if (folder.isDirectory()) {
            ntabs(tab, "Looking for cats in /" + folder.getName() + "...");

            File[] files = folder.listFiles();
            if (files == null)
                return total;
            for (File f : files) {
                if (f.isDirectory())
                    total = recursiveImportCats(f, total, tab + 1);
                else if (f.getName().endsWith(".jpg.cat")) {
                    //ntabs(tab + 1, "Found cat " + f.getName().substring(0, f.getName().indexOf('.')) + "!");
                    importCat(folder, f);
                    total++;
                }
            }
        }
        return total;

    } //End recursiveImportCats()

    private static void importCat(File folder, File aFile) {

        //Find .jpg file from .jpg.cat file
        String pFilename = aFile.getName().substring(0, aFile.getName().indexOf(".cat"));
        File pFile = new File(folder, pFilename);
        String URL = folder.getName() + "/" + pFile.getName();

        try {
            //Read image width/height
            BufferedImage bImg = ImageIO.read(pFile);
            int width = bImg.getWidth();
            int height = bImg.getHeight();

            //Read annotation data
            String[] annotations = BoopUtil.serveFlatFile(aFile.getPath()).split(" ");
            int noseX = Integer.parseInt(annotations[5]);
            int noseY = Integer.parseInt(annotations[6]);

            System.out.println("W,H: " + width + ", " + height + " X,Y: " + noseX + ", " + noseY + " URL: " + URL);
            //Insert cat
            boolean b = dbDriver.insertCat(width, height, noseX, noseY, URL);
            if (!b)
                System.out.println("Insertion of cat '" + URL + "' failed.");

        } catch (IOException e) {
            System.out.println("Error with photo '" + pFile.getPath() + "':");
            e.printStackTrace();
            return;
        }
    } //End importCat()

    private static void ntabs(int tab, String message) {
        for (int i = 0; i < tab; ++i)
            System.out.print("  ");
        System.out.println(message);
    } //End ntabs()

} //End class CatImporter
