package PA1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static PA1.Differential.countLines;
import static PA1.Differential.GRAMS_FILE;
import static PA1.Differential.docPath;

/**
 * EmpiricalComparison1
 */
public class EmpiricalComparison1 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Differential naive = new NaiveDifferential1();
        Differential bloom = new BloomDifferential1();

        List<String> keys = pickRandomKeys(countLines(GRAMS_FILE));
        System.out.println("Size of random keys: " + keys.size());
        ArrayList<String> data = compareDiffs(naive, bloom, keys);
        String[] headers = { "Navie", "Bloom", "InDiff", "InFilter" };
        Helper.exportNewExcel(docPath + "/analysis.xlsx", "comparation", 0, 0, headers, data);
    }

    private static ArrayList<String> compareDiffs(Differential naive, Differential bloom, List<String> keys) {
        int count = 1;
        double numFalsePositives = 0;
        int numFalses = 0;
        ArrayList<String> data = new ArrayList<String>();
        for (String key : keys) {
            int[] falsePositive = { 1, 0 };
            int naiveTime = (int) estimateDifferential(naive, key, null);
            int bloomTime = (int) estimateDifferential(bloom, key, falsePositive);
            boolean inDiff = falsePositive[0] == 1 ? true : false;
            boolean inFilter = falsePositive[1] == 1 ? true : false;
            data.add(naiveTime + " " + bloomTime + " " + falsePositive[0] + " " + falsePositive[1]);
            if (!inDiff) {
                numFalses++;
                if (inFilter)
                    numFalsePositives++;
            }
            System.out.println(count++ + "th key -- naive: " + naiveTime + " bloom: " + bloomTime + " InDiff: " + inDiff
                    + " InFilter: " + inFilter);
        }
        System.out.println("False Positive Rate: " + (numFalses == 0 ? 0d : numFalsePositives / numFalses));
        return data;
    }

    private static long estimateDifferential(Differential diff, String key, int[] falsePositive) {
        long startTime = System.nanoTime();
        String record = null;
        if (falsePositive != null) {
            BloomDifferential1 bd = (BloomDifferential1) diff;
            record = bd.retrieveRecord(key, falsePositive);
        } else {
            record = diff.retrieveRecord(key);
        }
        long stopTime = System.nanoTime();
        return record == null ? -1 : (stopTime - startTime) / 1000000;
    }

    private static List<String> pickRandomKeys(int numKeys) {
        Random r = new Random();
        int[] ranKeyIdxes = new int[numKeys / 10000];
        for (int i = 0; i < ranKeyIdxes.length; i++) {
            ranKeyIdxes[i] = (int) (r.nextDouble() * numKeys);
        }
        Arrays.sort(ranKeyIdxes);
        ArrayList<String> rankeys = new ArrayList<String>();
        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(GRAMS_FILE))) {
            String line = null;
            int lineNumber = 0;
            while (((line = br.readLine()) != null) && index < ranKeyIdxes.length) {
                if (lineNumber == ranKeyIdxes[index]) {
                    rankeys.add(line);
                    ++index;
                }
                ++lineNumber;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return rankeys;
    }
}