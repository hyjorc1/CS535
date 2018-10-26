package PA2;

public class MinHashTime {
    public static void timer(String folder, int numPermutations) {
        // 1
        long before = System.currentTimeMillis();
        MinHashSimilarities mhs = new MinHashSimilarities(folder, numPermutations);
        long after = System.currentTimeMillis();
        System.out.println("The time taken to construct an instance of MinHashSimilarities: " + (after - before)  + " s");

        String[] fileNames = mhs.allDocs();

        // 2
        before = System.currentTimeMillis();
        for (int i = 0; i < fileNames.length; i++) {
            for (int j = i + 1; j < fileNames.length; j++) {
                mhs.exactJaccard(fileNames[i], fileNames[j]);
            }
        }
        after = System.currentTimeMillis();
        System.out.println("Exact Jaccard Time: " + (after - before)  + " s");

        // 3
        before = System.currentTimeMillis();
        for (int i = 0; i < fileNames.length; i++) {
            for (int j = i + 1; j < fileNames.length; j++) {
                mhs.approximateJaccard(fileNames[i], fileNames[j]);
            }
        }
        after = System.currentTimeMillis();
        System.out.println("Approximate Jaccard Time: " + (after - before)  + " s");
    }
}
