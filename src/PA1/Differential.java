package PA1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Differential
 */
public interface Differential {

    public static String docPath = "/Users/yijiahuang/2018 Fall/535/hw/PA1";

    public static final File DIFF_FILE = new File(docPath + "/DiffFile.txt");

    public static final File DATABASE_FILE = new File(docPath + "/database.txt");

    public static final File GRAMS_FILE = new File(docPath + "/grams.txt");

    String retrieveRecord(String key);

    public static int countLines(File file) {
        int lines = -1;
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(file))) {
            lnr.skip(Long.MAX_VALUE);
            lines = lnr.getLineNumber();
            lnr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    public static String getKey(String line) {
        int cur = 0;
        for (int i = 0; i < 4; i++) {
            cur = line.indexOf(' ', cur + 1);
        }
        if (cur < 0 || cur > line.length() - 1)
            return line;
        return line.substring(0, cur);
    }

    public static String retrieveRecordFrom(String key, File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (getKey(line).equals(key))
                    return line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}