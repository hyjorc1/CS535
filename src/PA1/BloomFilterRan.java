package PA1;

import java.util.BitSet;
import java.util.Random;

public class BloomFilterRan implements BloomFilter {

	private BitSet filter;
	private int numHashes;
	private int dataSize;
	private int[][] pairs;
	private int prime;

	public BloomFilterRan(int setSize, int bitsPerElement) {
		this.filter = new BitSet(setSize * bitsPerElement);
		this.numHashes = (int) Math.ceil(Math.log(2) * bitsPerElement);
		this.dataSize = 0;
		this.pairs = new int[this.numHashes][2];
		this.prime = (int) Helper.nextPrime(this.filter.size());
		Random r = new Random();
		for (int[] pair : this.pairs) {
			pair[0] = r.nextInt((int) this.prime);
			pair[1] = r.nextInt((int) this.prime);
		}
	}

	@Override
	public void add(String s) {
		s = s.toLowerCase();
		for (int[] pair : this.pairs) {
			this.filter.set(this.ranHashCode(s, pair));
		}
		this.dataSize++;
	}

	@Override
	public boolean appears(String s) {
		s = s.toLowerCase();
		for (int[] pair : this.pairs) {
			if (!this.filter.get(this.ranHashCode(s, pair))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int filterSize() {
		return this.filter.size();
	}

	@Override
	public int dataSize() {
		return this.dataSize;
	}

	@Override
	public int numHashes() {
		return this.numHashes;
	}

	@Override
	public void clearFilter() {
		this.filter.clear();
		this.dataSize = 0;
	}

	public int ranHashCode(String s, int[] pair) {
		long h = (pair[0] * s.hashCode() + pair[1]) % this.prime;
		return (int) (Math.abs(h) % this.filter.size());
	}

}
