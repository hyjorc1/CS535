package PA1;

import java.util.HashSet;
import java.util.Set;

/**
 * FalsePositives
 */
public class FalsePositives {

    private static int setSize = 30000;
    private static int testSize = 30000;

    public static void main(String[] args) {
        int[] bitsPerElements = { 4, 8, 10 };
        for (int e : bitsPerElements) {
            System.out.println("bitsPerElement: " + e);
            System.out.println("Expected False Positive Rate: " + String.format("%.3f%%", Math.pow(0.618, e) * 100));
            BloomFilter bfFNV = new BloomFilterFNV(setSize, e);
            experiment(bfFNV, e);
            BloomFilter bfMur = new BloomFilterMurmur(setSize, e);
            experiment(bfMur, e);
            BloomFilter bfRan = new BloomFilterRan(setSize, e);
            experiment(bfRan, e);
            System.out.println();
        }
    }

    public static void experiment(BloomFilter bf, int bitsPerElement) {
        double temp = 0;
        for (int i = 0; i < 10; i++) {
            temp += falsePositiveRate(bf, setSize, testSize);
            bf.clearFilter();
        }
        System.out.println(String.format("%s avg rate: %.3f%%", bf.getClass(), temp * 10));
    }

    public static double falsePositiveRate(BloomFilter bf, int setSize, int testSize) {
        Set<String> already = new HashSet<String>();
        double numFalsePositives = 0;
        int numFalses = 0;
        for (int i = 0; i < setSize; i++) {
            String s = Helper.getRandomString();
            already.add(s);
            bf.add(s);
        }
        for (int i = 0; i < testSize; i++) {
            String s = Helper.getRandomString();
            if (!already.contains(s)) {
                numFalses++;
                if (bf.appears(s)) {
                    numFalsePositives++;
                }
            }
        }
        return numFalses == 0 ? 0d : numFalsePositives / numFalses;
    }

}