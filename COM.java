import java.util.Map;

public class COM {

    public static double[] getCenterOfMass(Geometry geom, Map<String, Double> atomMasses) {
        int n_atoms = geom.at_types.size();
        Object[] at_types = geom.at_types.toArray();
        double[][] coords = geom.coords;

        double[] com = new double[3];
        double mass = 0.0;

        for (int i = 0; i < n_atoms; i++) {
            double at_mass = atomMasses.get(at_types[i]);
            mass += at_mass;

            for (int j = 0; j < 3; j++) {
                com[j] += at_mass * coords[i][j];
            }

        }

        for (int n = 0; n < 3; n++) {
            com[n] /= mass;
        }

        return com;

    }

    public static void printCenterOfMass(double[] com) {
        System.out.print("molecular center of mass (Angstrom)\n(X,Y,Z): ");

        for (int i = 0; i < 3; i++) {
            System.out.print(com[i] + ", ");
        }
        System.out.println();
    }
}
