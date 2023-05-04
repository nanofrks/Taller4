import libs.DelayRecord;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ST;

public class AnalisisDatos {

    public static ST<String, Integer> meanRouteDelay(ArrayList<DelayRecord> delays) {
        ST<String, Integer> resultado = new ST<String, Integer>();
        int[][] delaySumCuenta = new int[1000][1000];

        for (DelayRecord registro : delays) {
            String origen = registro.getOrigin();
            String dest = registro.getDest();
            int delay = registro.getArrDelay();
            if (delay > 0) { // Solo se consideran los retrasos positivos
                int codOrigen = origen.hashCode();
                int codDest = dest.hashCode();
                delaySumCuenta[codOrigen][codDest] += delay;
                delaySumCuenta[codOrigen][codDest + 500] += 1;
            }
        }

        for (DelayRecord record : delays) {
            String origin = record.getOrigin();
            String dest = record.getDest();
            int originCode = origin.hashCode();
            int destCode = dest.hashCode();
            if (delaySumCuenta[originCode][destCode + 500] > 0) {
                int sum = delaySumCuenta[originCode][destCode];
                int count = delaySumCuenta[originCode][destCode + 500];
                int meanDelay = Math.round((float) sum / count);
                String route = origin + "-" + dest;
                resultado.put(route, meanDelay);
            }
        }

        return resultado;
    }
    public static void main(String[] args) {
        // Leer el archivo CSV y crear una lista de DelayRecord
        String filename = "548634059_T_ONTIME_REPORTING.csv";
        ArrayList<DelayRecord> delays = DelayRecord.readCSVFile(filename);
        StdOut.println("Number of records: " + delays.size());
    }
}
