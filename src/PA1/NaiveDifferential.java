package PA1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class NaiveDifferential {
    File DIFF_FILE = new File(Differential.docPath + "/DiffFile.txt");
    File DATABASE_FILE = new File(Differential.docPath + "/database.txt");

    public String retrieveRecord(String key) throws FileNotFoundException, IOException {
        String value = retrieveRecordFrom(key, DIFF_FILE);
        if (value != null) {
            System.out.println("Retrive From Differential File");
            return value;
        }
        return retrieveRecordFrom(key, DATABASE_FILE);
    }

    private String retrieveRecordFrom(String key, File dIFF_FILE2) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(dIFF_FILE2))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (getKey(line).equals(key))
                    return line;
            }
        }
        return null;
    }

    static String getKey(String line) {
        int count = 0;
        int cur = 0;
        do {
            cur = line.indexOf(' ', cur + 1);
            count++;
        } while (0 <= cur && count < 4);

        String key, value;
        if (0 <= cur && cur < line.length()) {
            key = line.substring(0, cur);
            value = line.substring(cur + 1, line.length());
        } else {
            key = line;
            value = "";
        }
        return key;
    }

}
