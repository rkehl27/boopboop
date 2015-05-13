/**
 * Created by Parker on 5/12/2015.
 */

import java.io.File;


public class CatImporter {

    private static final String catPath = "C:/Users/Parker/Documents/BigData/catphotos";

    public static void main(String[] args) {
        importCats(catPath);
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
                    ntabs(tab + 1, "Found cat " + f.getName().substring(0, f.getName().indexOf('.')) + "!");
                    total++;
                }
            }
        }
        return total;

    } //End recursiveImportCats

    private static void ntabs(int tab, String message) {
        for (int i = 0; i < tab; ++i)
            System.out.print("  ");
        System.out.println(message);
    }

} //End class CatImporter
