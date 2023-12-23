import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class XYZFormatter {

    // converts .xyz file to 2D ArrayList
    public static ArrayList<ArrayList<String>> formatXYZ(String filename) throws FileNotFoundException, IOException {
        ArrayList<ArrayList<String>> xyz_data = new ArrayList<ArrayList<String>>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<String> values = new ArrayList<String>();

                for (String i : line.split("\\s+")) {
                    values.add(i);
                }

                xyz_data.add(values);

            }

        }

        return xyz_data;

    }
}
