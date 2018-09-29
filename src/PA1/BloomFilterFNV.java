package PA1;

import java.math.BigInteger;
import java.util.BitSet;

public class BloomFilterFNV implements BloomFilter {

	private BitSet filter;
	private int numHashes;
	private int dataSize;
	private final long FNV64PRIME = 240 + 28 + 0xb3;
	private final long FNV_64INIT = new BigInteger("14695981039346656037").longValue();

	public BloomFilterFNV(int setSize, int bitsPerElement) {
		this.filter = new BitSet(setSize * bitsPerElement);
		this.numHashes = (int) Math.ceil(Math.log(2) * bitsPerElement);
		this.dataSize = 0;
	}

	@Override
	public void add(String s) {
		s = s.toLowerCase();
		for (int i = 0; i < this.numHashes; i++)
			this.filter.set(fnvHashCode(s, i));
		this.dataSize++;
	}

	@Override
	public boolean appears(String s) {
		s = s.toLowerCase();
		for (int i = 0; i < this.numHashes; i++)
			if (!this.filter.get(fnvHashCode(s, i)))
				return false;
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

	private int fnvHashCode(String s, int seed) {
		long h = (seed & 0xffffffffl) ^ this.FNV_64INIT;
		for (int i = 0; i < s.length(); i++) {
			h ^= s.charAt(i);
			h = (long) ((h * this.FNV64PRIME));
		}
		return (int) (Math.abs(h) % this.filter.size());
	}

}
