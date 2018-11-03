package PA2;

import java.util.List;

public class NearDuplicates {

	private LSH lsh;

	/**
	 * @param folder          Name of the folder containing documents
	 * @param numPermutations Number of Permutations to be used for MinHash
	 * @param threshold       Similarity threshold s, which is a double
	 */
	public NearDuplicates(String folder, int numPermutations, double threshold) {
		MinHash mh = new MinHash(folder, numPermutations);
		lsh = new LSH(mh.minHashMatrix(), mh.allDocs(), calculateNumBands(numPermutations, threshold));
	}

	/**
	 * gets (name of ) a document as parameter, which is a string. Then this method
	 * returns a list of documents that are at least s-similar to docN ame, by
	 * calling the method nearDuplicatesOf from LSH. Note that this list may contain
	 * some False Positives— Documents that are less than s-similar to docN ame.
	 */
	public List<String> nearDuplciateDetector(String docName) {
		return lsh.nearDuplicatesOf(docName);
	}

	private static int calculateNumBands(int k, double threshold) {
		int bands = 1, r = -1;
		do {
			bands++;
			r = k / bands;
		} while (Math.pow((1.0 / bands), (1.0 / r)) > threshold);
		bands -= 1;
		System.out.println();
		System.out.println("Num of Bands: " + bands);
		return bands;
	}
}
