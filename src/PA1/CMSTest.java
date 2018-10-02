package PA1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CMSTest {

	public static void main(String[] args) throws IOException {
		double epsilon = (float) 0.01;
		double delta = (float) Math.pow(2, -20);
		ArrayList<String> words = getWords();
		CMS cms = new CMS(epsilon, delta, words);
		ArrayList<String> L = cms.approximateHeavyHitter(0.04, 0.03);
		
		System.out.println("cms and " + cms.approximateFrequency("and"));

		// a) Number of 0.04 heavy hitters that are in L.
		// b) Number of 0.025 heavy hitters that are in L.
		// c) Number if items in L that are not 0.04 heavy hitters.
		int count_a = 0;
		int count_b = 0;
		int count_c = 0;
		for (String word : L) {
			int f = countWord(words, word);
			if (f >= 0.04 * words.size()) {
				count_a++; 
			} else {
				count_c++;
			}
			if (f >= 0.025 * words.size())
				count_b++;
		}
		System.out.println("a) Number of 0.04 heavy hitters: " + count_a);
		System.out.println("b) Number of 0.025 heavy hitters: " + count_b);
		System.out.println("c) Number if items in L that are not 0.04 heavy hitters: " + count_c);

		// d) Total number of strings that are added to the data structure,
		// and the total number of distinct strings that are added to the data
		// structure.
		System.out.println("d) Total number of strings: " + words.size());
		System.out.println("d) Total number of distinct strings: " + cms.getItemSize());

		// e) An estimate of total memory used to store the CMS data structure.
		System.out.println("e) Memory used: " + cms.getMemorySize() + "MB");
	}
	
	public static int countWord(ArrayList<String> words, String word) {
		int count = 0;
		for (String e : words) {
			if (e.equals(word))
				count++;
		}
		return count++;
	}

	public static ArrayList<String> getWords() {
		ArrayList<String> words = new ArrayList<String>();
		File file = new File(Differential.docPath + "/shakespear.txt");
		int count = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				Pattern p = Pattern.compile("[a-zA-Z]+");
				Matcher m = p.matcher(line);
				while (m.find()) {
					String word = m.group().toLowerCase();
					if (word.equals("and"))
						count++;
					if (word.length() >= 3 
							&& !word.equals("the"))
					words.add(word);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("actucal and " + count);
		return words;
	}

}
