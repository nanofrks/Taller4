import libs.DelayRecord;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.algs4.ST;

public class AnalisisDatos {
    public static void main(String[] args) {
        // Leer el archivo CSV y crear una lista de DelayRecord
        String filename = "548634059_T_ONTIME_REPORTING.csv";
        ArrayList<DelayRecord> delays = DelayRecord.readCSVFile(filename);
        StdOut.println("Number of records: " + delays.size());
    }

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

    public static ST<String, Integer> topMDelayed(ST<String, Integer> meanDelayRoute, int m) {
        String[] rutas = new String[m];
        int[] delays = new int[m];
    
        int i = 0;
        for (String ruta : meanDelayRoute.keys()) {
          int delay = meanDelayRoute.get(ruta);
          if (i < m || delay > delays[m - 1]) {
            // Insertar la nueva ruta y su retraso promedio en orden descendente
            int j = Math.min(i, m - 1);
            while (j > 0 && delay > delays[j - 1]) {
              rutas[j] = rutas[j - 1];
              delays[j] = delays[j - 1];
              j--;
            }
            rutas[j] = ruta;
            delays[j] = delay;
            i = Math.min(i + 1, m);
          }
        }
    
        ST<String, Integer> topM = new ST<String, Integer>();
        for (int k = 0; k < i; k++) {
          topM.put(rutas[k], delays[k]);
        }
        return topM;
      }
    
      public static void listTopMDelayed(ST<String, Integer> topM) {
        String[] rutas = new String[topM.size()];
        int[] delays = new int[topM.size()];
    
        int i = 0;
        for (String ruta : topM.keys()) {
          rutas[i] = ruta;
          delays[i] = topM.get(ruta);
          i++;
        }
    
        // Ordenar las rutas por retraso promedio en orden descendente
        for (int j = 0; j < i - 1; j++) {
          int maximo = j;
          for (int k = j + 1; k < i; k++) {
            if (delays[k] > delays[maximo]) {
              maximo = k;
            }
          }
          if (maximo != j) {
            String rutaTempo = rutas[j];
            rutas[j] = rutas[maximo];
            rutas[maximo] = rutaTempo;
    
            int tempDelay = delays[j];
            delays[j] = delays[maximo];
            delays[maximo] = tempDelay;
          }
        }
    
        // Imprimir las rutas y sus retrasos promedio en orden descendente
        for (int j = 0; j < i; j++) {
          System.out.printf("%s: %d%n", rutas[j], delays[j]);
        }
      }

      public static String longestDelayCarrier(ArrayList<DelayRecord> delays, java.util.Date from, java.util.Date to) {
        int maxDelay = Integer.MIN_VALUE;
        String aerolineaMaximoDelay = "";
        int delaySum = 0;
        int delayCount = 0;
        for (DelayRecord delay : delays) {
            if (!delay.getDate().before(from) && !delay.getDate().after(to)) {
                if (delay.getArrDelayNew() > 0) {
                    delaySum += delay.getArrDelayNew();
                    delayCount++;
                    if (delaySum / delayCount > maxDelay) {
                        maxDelay = delaySum / delayCount;
                        aerolineaMaximoDelay = delay.getCarrier();
                    }
                }
            }
        }
        return aerolineaMaximoDelay;
    }
}
