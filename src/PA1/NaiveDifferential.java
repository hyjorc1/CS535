package PA1;

import static PA1.Differential.retrieveRecordFrom;

/**
 * NaiveDifferential1
 */
public class NaiveDifferential implements Differential {

    @Override
    public String retrieveRecord(String key) {
        String record = retrieveRecordFrom(key, DIFF_FILE);
        return record == null ? retrieveRecordFrom(key, DATABASE_FILE) : record;
    }

}