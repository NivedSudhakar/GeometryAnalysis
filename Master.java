import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Master {

    public static Map<String, Double> CovalentRadii = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner input = new Scanner(System.in);

        populateDictionary();

        System.out.print("File: ");
        ArrayList<ArrayList<String>> formattedXyz = XYZFormatter.formatXYZ(input.nextLine());

        /*
         * for (int i = 0; i < formattedXyz.size(); i++) {
         * for (int j = 0; j < formattedXyz.get(i).size(); j++) {
         * System.out.print(formattedXyz.get(i).get(j));
         * System.out.print(" ");
         * }
         *
         * System.out.println();
         * }
         */

        // System.out.println(formattedXyz.get(3).get(0));

        Geometry geom = Geometry.getGeometry(formattedXyz);
        ArrayList<ArrayList<Integer>> bondGraph = BondLength.getBondGraph(geom, CovalentRadii);

        ArrayList<ArrayList<Double>> bonds = BondLength.getBonds(geom, bondGraph);

        System.out.println();

        System.out.println("Geometry: ");

        Geometry.printGeometry(geom);

        System.out.println();

        BondLength.printBonds(geom, bonds);

        input.close();
    }

    public static void populateDictionary() throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("Data/CovalentRadii.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                CovalentRadii.put(values[0], Double.valueOf(values[1]));
            }
        }
    }
}
