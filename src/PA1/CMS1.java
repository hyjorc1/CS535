package PA1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class CMS1 {

	private int rowNum;
	private int colNum;
	private int prime;
	private int[][] cms;
	private int[][] pairs;
	HashSet<String> itemSet;

	public CMS1(double epsilon, double delta, ArrayList<String> s) {
		this.rowNum = (int) Math.abs(Math.log(1 / delta) / Math.log(2));
		this.colNum = (int) Math.abs(2 / epsilon);
		this.cms = new int[rowNum][colNum];
		this.pairs = new int[rowNum][2];
		this.itemSet = new HashSet<String>();
		this.prime = (int) Helper.nextPrime(colNum);
		Random r = new Random();
		for (int[] pair : this.pairs) {
			pair[0] = r.nextInt(this.prime);
			pair[1] = r.nextInt(this.prime);
		}
		for (String word : s) {
			add(word);
			this.itemSet.add(word);
		}
	}

	private int[] hashCodes(String s) {
		int hashes[] = new int[rowNum];
		for (int i = 0; i < pairs.length; i++) {
			int ran = (pairs[i][0] * s.hashCode() + pairs[i][1]) % prime; 
			hashes[i] = Math.abs(ran) % colNum;
		}
		return hashes;
	}

	private void add(String s) {
		s = s.toLowerCase();
		int[] hashes = hashCodes(s);
		for (int i = 0; i < hashes.length; i++) {
			cms[i][hashes[i]]++;
		}
	}
	
	public int getItemSize() {
		return this.itemSet.size();
	}

	public int approximateFrequency(String x) {
		x = x.toLowerCase();
		int[] hashes = hashCodes(x);
		int frequency = Integer.MAX_VALUE;
		for (int i = 0; i < hashes.length; i++) {
			if (cms[i][hashes[i]] < frequency) {
				frequency = cms[i][hashes[i]];
			}
		}
		return frequency;
	}

	public ArrayList<String> approximateHeavyHitter(double q, double r) {
		ArrayList<String> words = new ArrayList<String>();
		for (String e : itemSet) {
			int f = approximateFrequency(e);
			if ((f >= q * itemSet.size()) && !(f < r * itemSet.size())) {
				words.add(e);
			}
		}
		return words;
	}

}
