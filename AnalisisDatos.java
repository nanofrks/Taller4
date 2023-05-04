import libs.DelayRecord;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdOut;

public class AnalisisDatos {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static void main(String[] args) {
        // Leer el archivo CSV y crear una lista de DelayRecord
        String filename = "548634059_T_ONTIME_REPORTING.csv";
        ArrayList<DelayRecord> delays = DelayRecord.readCSVFile(filename);
        StdOut.println("Number of records: " + delays.size());
    }
}
