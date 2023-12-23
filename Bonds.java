import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Bonds {

    public static Map<String, Double> CovalentRadii = new HashMap<>();

    public static double bond_thresh = 1.2;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner input = new Scanner(System.in);

        populateDictionary();

        System.out.print("File: ");
        ArrayList<ArrayList<String>> formattedXyz = formatXYZ(input.nextLine());

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

        Geometry geom = getGeometry(formattedXyz);
        ArrayList<ArrayList<Integer>> bondGraph = getBondGraph(geom);

        ArrayList<ArrayList<Double>> bonds = getBonds(geom, bondGraph);

        System.out.println();

        System.out.println("Geometry: ");

        printGeometry(geom);

        System.out.println();

        printBonds(geom, bonds);

        input.close();
    }

    public static void populateDictionary() throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("CovalentRadii.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                CovalentRadii.put(values[0], Double.valueOf(values[1]));
            }
        }
    }

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

    public static Geometry getGeometry(ArrayList<ArrayList<String>> xyzData) {

        int n_atoms = Integer.valueOf(xyzData.get(0).get(0));
        ArrayList<String> at_types = new ArrayList<String>();
        double[][] coordsArray = new double[n_atoms][3];

        for (int i = 0; i < n_atoms; i++) {
            at_types.add(xyzData.get(i + 2).get(0));
            for (int j = 0; j < 3; j++) {
                coordsArray[i][j] = Double.valueOf(xyzData.get(i + 2).get(j + 1));
            }

        }

        Geometry geom = new Geometry(at_types, coordsArray);

        return geom;
    }

    public static ArrayList<ArrayList<Integer>> getBondGraph(Geometry geom) {
        int n_atoms = geom.at_types.size();
        ArrayList<ArrayList<Integer>> bondGraph = new ArrayList<ArrayList<Integer>>();
        Object[] at_types = geom.at_types.toArray();
        double[][] coords = geom.coords;

        // initialize bondGraph
        for (int i = 0; i < n_atoms; i++) {
            ArrayList<Integer> arr = new ArrayList<Integer>();
            bondGraph.add((ArrayList<Integer>) arr);
        }

        for (int i = 0; i < n_atoms; i++) {
            double cov_rad1 = (double) CovalentRadii.get(at_types[i]);
            for (int j = i + 1; j < n_atoms; j++) {
                double cov_rad2 = (double) CovalentRadii.get(at_types[j]);
                double thresh = bond_thresh * (cov_rad1 + cov_rad2);
                double r12 = get_r12(coords[i],
                        coords[j]);

                if (r12 < thresh) {
                    bondGraph.get(i).add(j);
                    bondGraph.get(j).add(i);
                }
            }

        }

        /*
         * for (ArrayList<Integer> i : bondGraph) {
         * for (int j : i) {
         * System.out.print(j);
         * System.out.print(" ");
         * }
         * System.out.println();
         * }
         */

        return bondGraph;

    }

    public static ArrayList<ArrayList<Double>> getBonds(Geometry geom, ArrayList<ArrayList<Integer>> bondGraph) {
        int n_atoms = geom.at_types.size();
        Object[] at_types = geom.at_types.toArray();
        double[][] coords = geom.coords;

        ArrayList<ArrayList<Double>> bonds = new ArrayList<ArrayList<Double>>();

        for (int i = 0; i < n_atoms; i++) {
            for (int j = 0; j < bondGraph.get(i).size(); j++) {
                int a = bondGraph.get(i).get(j);

                if (i < a) {
                    double r12 = get_r12(coords[i], coords[a]);
                    ArrayList<Double> temp = new ArrayList<Double>();

                    temp.add((double) i);
                    temp.add((double) a);
                    temp.add(r12);
                    bonds.add(temp);

                }

            }
        }

        /*
         * for (ArrayList<Double> i : bonds) {
         * for (double j : i) {
         * System.out.print(j);
         * System.out.print(' ');
         * }
         * 
         * System.out.println();
         * }
         * System.out.println();
         */

        return bonds;

    }

    // calculate distance betwenn cartesian coordinates
    public static double get_r12(double[] coords1, double[] coords2) {
        double r2 = 0.0;
        for (int i = 0; i < 3; i++) {
            r2 += Math.pow(Double.valueOf(coords2[i]) - Double.valueOf(coords1[i]), 2);
        }
        double r = Math.sqrt(r2);
        return r;
    }

    public static void printBonds(Geometry geom, ArrayList<ArrayList<Double>> bonds) {
        ArrayList<String> at_types = geom.at_types;
        int n_bonds = bonds.size();

        System.out.println((n_bonds) + " bonds found");

        for (int i = 0; i < n_bonds; i++) {
            double r12 = bonds.get(i).get(2);
            double n1 = bonds.get(i).get(0);
            double n2 = bonds.get(i).get(1);

            String nstr = Math.round(n1 + 1) + "-" + Math.round(n2 + 1);
            String tstr = at_types.get((int) n1) + "-" + at_types.get((int) n2);

            System.out.println(nstr + " " + tstr + " " + r12);

        }

    }

    public static void printGeometry(Geometry geom) {
        Object[] at_types = geom.at_types.toArray();
        double[][] coords = geom.coords;

        for (int i = 0; i < at_types.length; i++) {
            System.out.print(at_types[i] + " ");
            for (int j = 0; j < coords[i].length; j++) {
                System.out.print(coords[i][j] + " ");
            }

            System.out.println();
        }

    }

}
