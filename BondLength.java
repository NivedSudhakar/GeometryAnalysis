
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BondLength {

    public static double bond_thresh = 1.2;

    public static ArrayList<ArrayList<Integer>> getBondGraph(Geometry geom, Map<String, Double> CovalentRadii) {
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
                double r12 = getDistance(coords[i],
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
                    double r12 = getDistance(coords[i], coords[a]);
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
    public static double getDistance(double[] coords1, double[] coords2) {
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

}
