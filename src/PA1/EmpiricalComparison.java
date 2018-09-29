package PA1;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Arrays.sort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EmpiricalComparison {

	static File GRAMS_FILE = new File(Differential.docPath + "/grams.txt");
	static Random random = new Random();

	public static void main(String args[]) throws FileNotFoundException, IOException {
		System.out.println("Compare the performances of the programs NaiveDifferential and BloomDifferential :");

		System.out.println("Create instance from NaivDifferential and BloomDifferential :");
		NaiveDifferential naive = new NaiveDifferential();
		BloomDifferential differential = new BloomDifferential();
		BloomFilterFNV bloomFilter = differential.createFilter();

		int numKeys = countNumberLines(GRAMS_FILE);
		System.out.println(numKeys);
		int numQueries = numKeys / 10000;
		if (numQueries <= 0) {
			System.out.println("We could't make query, Number of key is Not enough ");
		}

		// System.out.println("Uniformly Randomly select Keys =" + numQueries);
		int[] RandomKeys = new int[numQueries];
		// Generates Random Numbers in the range 1 -12632196
		for (int i = 0; i < RandomKeys.length; i++) {
			RandomKeys[i] = (int) (Math.random() * numKeys + 1);
		} // end for loop

		sort(RandomKeys); // sort number make it easy to retrieve the key from file
		// System.out.println("Number of keys in Grams File: " +
		// Arrays.toString(RandomKeys));
		List<String> listKeys = retrieveKeyFrom(RandomKeys);

		// for (int i = 0; i < listKeys.size(); i++)
		// System.out.println(listKeys.get(i));

		// System.out.println("Size of list key =" + listKeys.size());
		int countFlasePositive = 0;
		if (listKeys.size() == numQueries) {
			for (int i = 0; i < listKeys.size(); i++) {
				long timeNaive = timeNaiveRetrieveRecord(naive, listKeys.get(i));
				long timeDifferential = timeDifferentialRetrieveRecord(differential, listKeys.get(i));
				System.out.print(
						"Retrieve Record #" + (i + 1) + "Time in BloomDifferential :" + timeDifferential / 1000000 + "   ");
				System.out.println("Retrieve Record #" + (i + 1) + "Time in NaiveDifferential :" + timeNaive / 1000000);

				if (bloomFilter.appears(listKeys.get(i)) != differential.isKeyinDifferentialFile(listKeys.get(i)))
					++countFlasePositive;

			}
			System.out.print("Number of False Postive is equal to" + countFlasePositive);
		}

		out.println();
		out.println("Empirical comparison is completed.");
	}

	private static long timeNaiveRetrieveRecord(NaiveDifferential naive, String key)
			throws FileNotFoundException, IOException {
		long startTime = System.nanoTime();
		String value = naive.retrieveRecord(key);
		long stopTime = System.nanoTime();
		if (value == null) {
			System.out.println("Dose not find records.");
			return 0;
		}
		return (stopTime - startTime);
	}

	private static long timeDifferentialRetrieveRecord(BloomDifferential diff, String key)
			throws FileNotFoundException, IOException {
		long startTime = System.nanoTime();
		String value = diff.retrieveRecord(key);
		long stopTime = System.nanoTime();
		if (value == null) {
			System.out.println("Dose not find records.");
			return 0;
		}
		return (stopTime - startTime);
	}

	private static List<String> retrieveKeyFrom(int[] Randomselections) throws IOException {

		List<String> keys = new ArrayList<>();
		int index = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(GRAMS_FILE))) {
			String line = null;
			int lineNumber = 0;
			while (((line = br.readLine()) != null) && index < Randomselections.length) {
				if (lineNumber == Randomselections[index]) {
					keys.add(line);
					++index;
				}
				++lineNumber;
			}
		}

		return keys;
	}

	static int countNumberLines(File path) {
		try (LineNumberReader reader = new LineNumberReader(new FileReader(path))) {
			while (reader.readLine() != null) {
			}
			return reader.getLineNumber();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
