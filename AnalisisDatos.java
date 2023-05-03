import libs.DelayRecord;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
public class AnalisisDatos {
    public static void main(String[] args) {
        // Leer el archivo CSV y crear una lista de DelayRecord
        String filename = "548634059_T_ONTIME_REPORTING.csv";
        ArrayList<DelayRecord> delays = DelayRecord.readCSVFile(filename);
        StdOut.println("Number of records: " + delays.size());
    }
}
