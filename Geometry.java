
import java.util.ArrayList;

public class Geometry {

    public ArrayList<String> at_types;
    public double[][] coords;

    public Geometry(ArrayList<String> at_types, double[][] coords) {
        this.at_types = at_types;
        this.coords = coords;
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
