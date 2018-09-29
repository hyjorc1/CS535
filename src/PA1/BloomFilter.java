package PA1;

public interface BloomFilter {

	void add(String s);

	boolean appears(String s);

	int filterSize();

	int dataSize();

	int numHashes();

	void clearFilter();
}
