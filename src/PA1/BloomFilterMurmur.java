package PA1;

import java.util.BitSet;

public class BloomFilterMurmur implements BloomFilter {

	private BitSet filter;
	private int numHashes;
	private int dataSize;

	public BloomFilterMurmur(int setSize, int bitsPerElement) {
		this.filter = new BitSet(setSize * bitsPerElement);
		this.numHashes = (int) Math.ceil(Math.log(2) * bitsPerElement);
		this.dataSize = 0;
	}

	@Override
	public void add(String s) {
		s = s.toLowerCase();
		for (int i = 0; i < this.numHashes; i++) {
			this.filter.set(murmurHashCode(s.getBytes(), s.length(), i));
		}
		this.dataSize++;
	}

	@Override
	public boolean appears(String s) {
		s = s.toLowerCase();
		for (int i = 0; i < this.numHashes; i++) {
			if (!this.filter.get(murmurHashCode(s.getBytes(), s.length(), i)))
				return false;
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

	/**
	 * Generates 64 bit hash from byte array of the given length and seed.
	 * 
	 * @param data   byte array to hash
	 * @param length length of the array to hash
	 * @param seed   initial seed value
	 * @return 64 bit hash of the given array
	 */
	private int murmurHashCode(final byte[] data, int length, int seed) {
		final long m = 0xc6a4a7935bd1e995L;
		final int r = 47;

		long h = (seed & 0xffffffffl) ^ (length * m);

		int length8 = length / 8;

		for (int i = 0; i < length8; i++) {
			final int i8 = i * 8;
			long k = ((long) data[i8 + 0] & 0xff) + (((long) data[i8 + 1] & 0xff) << 8)
					+ (((long) data[i8 + 2] & 0xff) << 16) + (((long) data[i8 + 3] & 0xff) << 24)
					+ (((long) data[i8 + 4] & 0xff) << 32) + (((long) data[i8 + 5] & 0xff) << 40)
					+ (((long) data[i8 + 6] & 0xff) << 48) + (((long) data[i8 + 7] & 0xff) << 56);

			k *= m;
			k ^= k >>> r;
			k *= m;

			h ^= k;
			h *= m;
		}

		switch (length % 8) {
		case 7:
			h ^= (long) (data[(length & ~7) + 6] & 0xff) << 48;
		case 6:
			h ^= (long) (data[(length & ~7) + 5] & 0xff) << 40;
		case 5:
			h ^= (long) (data[(length & ~7) + 4] & 0xff) << 32;
		case 4:
			h ^= (long) (data[(length & ~7) + 3] & 0xff) << 24;
		case 3:
			h ^= (long) (data[(length & ~7) + 2] & 0xff) << 16;
		case 2:
			h ^= (long) (data[(length & ~7) + 1] & 0xff) << 8;
		case 1:
			h ^= (long) (data[length & ~7] & 0xff);
			h *= m;
		}
		;

		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		return (int) (Math.abs(h) % this.filter.size());
	}

}
