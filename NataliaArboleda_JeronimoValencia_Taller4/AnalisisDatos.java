import libs.DelayRecord;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.ST;

//Natalia Arboleda Arboleda 000165655
//Jeronimo Valencia Ospina 000165785

public class AnalisisDatos {

  private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
  
    public static void main(String[] args) throws ParseException {

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

        //Llama el metodo topMDelayed para sacar los top M retardos
        int m=20;   
        System.out.println("\n"+"Top M registros: "+"\n");
        ST<String, Integer> TopM = topMDelayed(meanDelays,m);
         for (String route : TopM.keys()) {
            System.out.println(route + ": " + TopM.get(route));
        }

        //Ordena la ST y la imprime
        System.out.println("\n"+"Tiempos medios de retardo ordenados: "+"\n");
        listTopMDelayed(TopM);
         for (String route : TopM.keys()) {
            System.out.println(route + ": " + TopM.get(route));
        }

        //El metodo devuelve un string con la aerolina con mayor retraso entre un rango de tiempo
        Date d1 = df.parse("01/01/2019");
        Date d2 = df.parse("05/01/2019");
        String aerolinaretraso=longestDelayCarrier(delays, d1, d2);

        System.out.println("\n"+"Aerolinea con mayor retraso entre " + d1.toString() + " y " + d2.toString()+ " es: " + aerolinaretraso + "\n");

    }

    public static ST<String, Integer> meanRouteDelay(ArrayList<DelayRecord> delays) {
        ST<String, Integer> delayTable = new ST<String, Integer>();
        ST<String, Integer> cantvuelos = new ST<String, Integer>();
        for (DelayRecord delay : delays){

            Integer depdelay, arrdelay, arrdelaynew, carrierdelay, lateaircraftdelay, nasdelay, securitydelay, weatherdelay;

            //Comprobaciones
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

            //Cuenta los vuelos hechos en cada ruta
            if(cantvuelos.get(ruta)==null){
                cantvuelos.put(ruta, count);
            }else{
                count++;
                cantvuelos.put(ruta, count);
            }

            //Calcula el promedio y lo pone en la ST
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

        //Agrega las llaves, si el tamaño de la pq es mayor a m, elimina el menor
        for (String key : meanDelayRoute.keys()) {
          pq.insert(meanDelayRoute.get(key));
          if (pq.size() > m) pq.delMin();
        }

        ST<String, Integer> topM = new ST<>();

        //pasa las llaves a una ST
        while (!pq.isEmpty()){
          Integer val=pq.delMin();
          
          for (String key : meanDelayRoute.keys()){
            if (val==meanDelayRoute.get(key)) topM.put(key, val);
          }
        }
        return topM;
      }
    
      public static void listTopMDelayed(ST<String, Integer> topM) {
        String[] rutas = new String[topM.size()];
        int[] delays = new int[topM.size()];
    
        //llena los arrays con las llaves y los valores
        int i = 0;
        for (String key : topM.keys()) {
          rutas[i] = key;
          delays[i] = topM.get(key);
          i++;
        }
    
        //ordenacion por selection sort
        for (int j = 0; j < i - 1; j++) {
          int max = j;
          for (int k = j + 1; k < i; k++) {
            if (delays[k] > delays[max]) max = k;
          if (max != j) {
            String rutaTmp = rutas[j];
            rutas[j] = rutas[max];
            rutas[max] = rutaTmp;
    
            int delayTmp = delays[j];
            delays[j] = delays[max];
            delays[max] = delayTmp;
          }
          }
        }
      }

      public static String longestDelayCarrier(ArrayList<DelayRecord> delays, Date from, Date to) {
        ST<String, Integer> delayTable = new ST<String, Integer>();
        ST<String, Integer> cantvuelos = new ST<String, Integer>();
        for (DelayRecord delay : delays){
            if(delay.getDate().before(to) && delay.getDate().after(from)){
              Integer depdelay, arrdelay, arrdelaynew, carrierdelay, lateaircraftdelay, nasdelay, securitydelay, weatherdelay;

              //Comprobaciones
              if(delay.getDepDelay()==null) depdelay = 0; else depdelay = delay.getDepDelay();
              if(delay.getArrDelay()==null) arrdelay = 0; else arrdelay = delay.getArrDelay();
              if(delay.getArrDelayNew()==null) arrdelaynew = 0; else arrdelaynew = delay.getArrDelayNew();
              if(delay.getCarrierDelay()==null) carrierdelay = 0; else carrierdelay = delay.getCarrierDelay();
              if(delay.getLateAircraftDelay()==null) lateaircraftdelay = 0; else lateaircraftdelay = delay.getLateAircraftDelay();
              if(delay.getNasDelay()==null) nasdelay = 0; else nasdelay = delay.getNasDelay();
              if(delay.getSecurityDelay()==null) securitydelay = 0; else securitydelay = delay.getSecurityDelay();
              if(delay.getWeatherDelay()==null) weatherdelay = 0; else weatherdelay = delay.getWeatherDelay();

              //Obtiene los delays en el rango de fechas y los pone en un ST
              String maxDelayCarrier = delay.getCarrier();
              Integer overallDelay = depdelay + arrdelay + arrdelaynew + carrierdelay + lateaircraftdelay + nasdelay + securitydelay + weatherdelay;
              Integer count = 1;
              if(cantvuelos.get(maxDelayCarrier)==null){
                  cantvuelos.put(maxDelayCarrier, count);
              }else{
                  count++;
                  cantvuelos.put(maxDelayCarrier, count);
              }

              if(delayTable.get(maxDelayCarrier)==null){
                  delayTable.put(maxDelayCarrier, overallDelay);
              }else{
                  overallDelay += overallDelay;
                  delayTable.put(maxDelayCarrier, overallDelay/cantvuelos.get(maxDelayCarrier));
              }
          }
        }
        String maxDelayCarrier = delayTable.max();
        return maxDelayCarrier;
    }
}
