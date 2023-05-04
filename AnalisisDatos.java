import libs.DelayRecord;

import java.lang.reflect.Array;
import java.sql.Date;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.ST;

public class AnalisisDatos {
    public static void main(String[] args) {
        // Leer el archivo CSV y crear una lista de DelayRecord
        String filename = "548634059_T_ONTIME_REPORTING.csv";
        ArrayList<DelayRecord> delays = DelayRecord.readCSVFile(filename);
        StdOut.println("Numero de registros: " + delays.size()+"\n");

         // Se llama al método meanRouteDelay y se imprime la tabla de símbolos resultante
         System.out.println("\n"+"Tiempos medios de retardo: "+"\n");
         ST<String, Integer> meanDelays = meanRouteDelay(delays);
         for (String route : meanDelays.keys()) {
            System.out.println(route + ": " + meanDelays.get(route));
        }

        System.out.println("\n"+"Top M registros: "+"\n");
        ST<String, Integer> TopM = topMDelayed(meanDelays,20);
         for (String route : TopM.keys()) {
            System.out.println(route + ": " + TopM.get(route));
        }

        System.out.println("\n"+"Tiempos medios de retardo ordenados: "+"\n");
        listTopMDelayed(TopM);
         for (String route : TopM.keys()) {
            System.out.println(route + ": " + TopM.get(route));
        }

        Date d1 = new Date(2019, 01, 10);
        Date d2 = new Date(2019, 06, 10);
        String aerolinaretraso=longestDelayCarrier(delays, d1, d2);

        System.out.println("\n"+"Aerolinea con mayor retraso entre "+d1.toString()+" y "+d2.toString()+" es: "+aerolinaretraso+ "\n");

    }

    public static ST<String, Integer> meanRouteDelay(ArrayList<DelayRecord> delays) {
        ST<String, Integer> delayTable = new ST<String, Integer>();
        ST<String, Integer> cantvuelos = new ST<String, Integer>();
        for (DelayRecord delay : delays){

            Integer depdelay, arrdelay, arrdelaynew, carrierdelay, lateaircraftdelay, nasdelay, securitydelay, weatherdelay;

            if(delay.getDepDelay()==null) depdelay = 0; else depdelay = delay.getDepDelay();
            if(delay.getArrDelay()==null) arrdelay = 0; else arrdelay = delay.getArrDelay();
            if(delay.getArrDelayNew()==null) arrdelaynew = 0; else arrdelaynew = delay.getArrDelayNew();
            if(delay.getCarrierDelay()==null) carrierdelay = 0; else carrierdelay = delay.getCarrierDelay();
            if(delay.getLateAircraftDelay()==null) lateaircraftdelay = 0; else lateaircraftdelay = delay.getLateAircraftDelay();
            if(delay.getNasDelay()==null) nasdelay = 0; else nasdelay = delay.getNasDelay();
            if(delay.getSecurityDelay()==null) securitydelay = 0; else securitydelay = delay.getSecurityDelay();
            if(delay.getWeatherDelay()==null) weatherdelay = 0; else weatherdelay = delay.getWeatherDelay();

            String ruta = delay.getOrigin() +"-"+ delay.getDest();
            Integer overallDelay = depdelay + arrdelay + arrdelaynew + carrierdelay + lateaircraftdelay + nasdelay + securitydelay + weatherdelay;
            Integer count = 1;
            if(cantvuelos.get(ruta)==null){
                cantvuelos.put(ruta, count);
            }else{
                count++;
                cantvuelos.put(ruta, count);
            }

            if(delayTable.get(ruta)==null){
                delayTable.put(ruta, overallDelay);
            }else{
                overallDelay += overallDelay;
                delayTable.put(ruta, overallDelay/cantvuelos.get(ruta));
            }
        }
        return delayTable;
    }

    public static ST<String, Integer> topMDelayed(ST<String, Integer> meanDelayRoute, int m) {
      MinPQ<Integer> pq = new MinPQ<Integer>(m+1);

        for (String key : meanDelayRoute.keys()) {
          pq.insert(meanDelayRoute.get(key));
          if (pq.size() > m) pq.delMin();
        }

        ST<String, Integer> topM = new ST<>();

        while (!pq.isEmpty()){
          Integer val=pq.delMin();
          
          for (String key : meanDelayRoute.keys()){
            if (val==meanDelayRoute.get(key)){
              topM.put(key, val);
            }
          }

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
