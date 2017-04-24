import org.junit.Ignore;

import java.io.File;
import java.util.TreeMap;

/**
 * Created by modeg on 10/30/2016.
 */
@Ignore
public class Tester3 {
    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        File folder = new File("C:\\reoursces_old");
        File[] listOfFolders = folder.listFiles();
        for (int i = 0; i < listOfFolders.length; i++) {
            if (listOfFolders[i].isDirectory()) {
                String locale = listOfFolders[i].getName();
                builder.append(locale.toUpperCase().replace("-", "_"))
                        .append("(\"")
                        .append(locale.toLowerCase())
                        .append("\"),")
                        .append("\n");
            }
        }
        System.out.println(builder.toString());
    }
}
