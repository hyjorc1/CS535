package PA1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static PA1.Differential.countLines;
import static PA1.Differential.getKey;
import static PA1.Differential.retrieveRecordFrom;

/**
 * BloomDifferential1
 */
public class BloomDifferential implements Differential {

    private static int BITS_PER_ELEMENT = 8;
    private BloomFilter filter = null;

    public BloomDifferential() {
        createFilter();
    }

    public BloomFilter createFilter() {
        filter = new BloomFilterFNV(countLines(DIFF_FILE), BITS_PER_ELEMENT);
        try (BufferedReader br = new BufferedReader(new FileReader(DIFF_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                filter.add(getKey(line));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filter;
    }

    @Override
    public String retrieveRecord(String key) {
        if (filter == null) {
            createFilter();
        }
        String record = null;
        if (filter.appears(key)) {
            record = retrieveRecordFrom(key, DIFF_FILE);
        }
        return record == null ? retrieveRecordFrom(key, DATABASE_FILE) : record;
    }

    public String retrieveRecord(String key, int[] falsePositive) {
        if (filter == null) {
            createFilter();
        }
        String record = null;
        if (filter.appears(key)) {
            falsePositive[1] = 1;
            record = retrieveRecordFrom(key, DIFF_FILE);
        }
        if (record == null) {
            falsePositive[0] = 0;
            record = retrieveRecordFrom(key, DATABASE_FILE);
        }
        return record;
    }
}