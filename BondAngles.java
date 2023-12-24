import java.util.ArrayList;

public class BondAngles {

    public static double[] getUnitVector(double[] coords1, double[] coords2) {
        double r12 = BondLength.getDistance(coords1, coords2);
        double[] u12 = new double[3];

        for (int i = 0; i < 3; i++) {
            u12[i] = (coords2[i] - coords1[i]) / r12;
        }

        return u12;
    }

    public static double getDotProduct(double[] u1, double[] u2) {
        double dotProduct = 0.0;

        for (int i = 0; i < 3; i++) {
            dotProduct += (u1[i] * u2[i]);
        }

        return dotProduct;
    }

    public static double getAngle(double[] coords1, double[] coords2, double[] coords3) {
        double[] vec1 = getUnitVector(coords2, coords1);
        double[] vec2 = getUnitVector(coords3, coords2);

        double dotProduct = getDotProduct(vec1, vec2);
        double angle = Math.toDegrees(Math.acos(dotProduct));

        return angle;

    }

    public static ArrayList<ArrayList<Double>> getAngles(Geometry geom, ArrayList<ArrayList<Integer>> bondGraph) {
        int n_atoms = geom.at_types.size();
        Object[] at_types = geom.at_types.toArray();
        double[][] coords = geom.coords;

        ArrayList<ArrayList<Double>> angles = new ArrayList<ArrayList<Double>>();

        for (int i = 0; i < n_atoms; i++) {
            int numBonds = bondGraph.get(i).size();
            for (int j = 0; j < numBonds; j++) {
                int a = bondGraph.get(i).get(j);

                for (int n = j + 1; n < numBonds; n++) {
                    int k = bondGraph.get(i).get(n);
                    double angle = getAngle(coords[a], coords[i], coords[k]);

                    ArrayList<Double> temp = new ArrayList<Double>();

                    temp.add((double) a);
                    temp.add((double) i);
                    temp.add((double) k);
                    temp.add(angle);

                    angles.add(temp);

                }
            }
        }

        return angles;

    }

    public static void printAngles(Geometry geom, ArrayList<ArrayList<Double>> angles) {
        ArrayList<String> at_types = geom.at_types;
        int n_bonds = angles.size();

        System.out.println((n_bonds) + " angles");

        for (int i = 0; i < n_bonds; i++) {

            double n1 = angles.get(i).get(0);
            double n2 = angles.get(i).get(1);
            double n3 = angles.get(i).get(2);
            double angle = angles.get(i).get(3);

            String nstr = Math.round(n1 + 1) + "-" + Math.round(n2 + 1) + "-" + Math.round(n3 + 1);
            String tstr = at_types.get((int) n1) + "-" + at_types.get((int) n2) + "-" + at_types.get((int) n3);

            System.out.println(nstr + " " + tstr + " " + angle);

        }

    }
}
