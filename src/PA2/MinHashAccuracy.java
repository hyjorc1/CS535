package PA2;

public class MinHashAccuracy {

	private MinHashSimilarities mhs;
	private int numPerm;

	public MinHashAccuracy() {
		this.mhs = null;
		this.numPerm = -1;
	}

	public int accuracy(String folder, int numPermutations, double epsilon) {
		if (mhs == null || numPerm != numPermutations)
			mhs = new MinHashSimilarities(folder, numPermutations);
		numPerm = numPermutations;
		String[] fileNames = mhs.allDocs();
		int count = 0;
		for (int i = 0; i < fileNames.length; i++) {
			for (int j = i + 1; j < fileNames.length; j++) {
				double exactJaccard = mhs.exactJaccard(fileNames[i], fileNames[j]);
				double approimateJaccard = mhs.approximateJaccard(fileNames[i], fileNames[j]);
				if (Math.abs(exactJaccard - approimateJaccard) > epsilon) {
					// System.out.println(fileNames[i] + " " + fileNames[j] + " " + exactJaccard + "
					// " + approimateJaccard
					// + " " + epsilon);
					count++;
				}
			}
		}
		// System.out.println(
		// "The number of pairs for which exaxt and approximate similarities differ by
		// more then epsilon: "
		// + count);
		return count;
	}
}
