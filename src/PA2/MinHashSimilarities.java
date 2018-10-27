package PA2;

public class MinHashSimilarities {

    private int[][] termDocMat;
    private int[][] minHashMat;
    private MinHash mh;

    /**
     * Creates an instance of MinHash, and calls the methods termDocumentMatrix and
     * minHashMatrix to store the respective matrices.
     */
    public MinHashSimilarities(String folder, int numPermutations) {
        this.mh = new MinHash(folder, numPermutations);
        this.termDocMat = mh.termDocumentMatrix();
        this.minHashMat = mh.minHashMatrix();
        // System.out.println("mhs done");
    }

    /**
     * Gets names of two files (in the document collection) file1 and file2 as
     * parameters and returns the exact Jaccard Similarity of the files. Use the
     * termDocumentMatrix computed (by the constructor) for this. Return type is
     * double.
     */
    public double exactJaccard(String file1, String file2) {
        int idx1 = mh.getFileIdx(file1), idx2 = mh.getFileIdx(file2);
        double union = 0, intersection = 0;
        for (int i = 0; i < termDocMat.length; i++) {
            if (Math.min(termDocMat[i][idx1], termDocMat[i][idx2]) == 1) { // both are 1
                union++;
                intersection++;
            } else if (Math.max(termDocMat[i][idx1], termDocMat[i][idx2]) == 1) { // one of them is 1
                union++;
            }
        }
        return intersection / union;
    }

    /**
     * Estimates and returns the Jaccard similarity of documents file1 and file2 by
     * comparing the MinHash signatures of file1 and file2. Use the MinHashMatrix
     * computed by the constructor.
     */
    public double approximateJaccard(String file1, String file2) {
        int idx1 = mh.getFileIdx(file1), idx2 = mh.getFileIdx(file2);
        double count = 0;
        for (int i = 0; i < minHashMat.length; i++) {
            if (minHashMat[i][idx1] == minHashMat[i][idx2]) // min(perm(Da)) == min(perm(Db))
                count++;
        }
        return count / minHashMat.length; // l / k
    }

    /**
     * Returns the MinHash the minhash signature of the document named fileName,
     * which is an array of ints.
     */
    public int[] minHashSig(String fileName) {
        int idx = mh.getFileIdx(fileName);
        int[] sig = new int[minHashMat.length];
        for (int i = 0; i < sig.length; i++)
            sig[i] = minHashMat[i][idx];
        return sig;
    }

    public String[] allDocs() {
        return mh.allDocs();
    }
}
