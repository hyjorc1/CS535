package PA1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class BloomDifferential {

	private static final int bitsPerElement = 8;
	File DIFF_FILE = new File(Differential.docPath + "/DiffFile.txt");
	File DATABASE_FILE = new File(Differential.docPath + "/database.txt");

	private BloomFilterFNV filter = null;

	public BloomFilterFNV createFilter() throws FileNotFoundException, IOException {
		if (filter != null) {
			return filter;
		}
		BloomFilterFNV filter = new BloomFilterFNV(countNumberLines(DIFF_FILE), bitsPerElement);
		try (BufferedReader br = new BufferedReader(new FileReader(DIFF_FILE))) {
			String line;
			while ((line = br.readLine()) != null) {
				filter.add(getKey(line));
			}
		}
		this.filter = filter;
		return filter;
	}

	int countNumberLines(File path) {
		try (LineNumberReader reader = new LineNumberReader(new FileReader(path))) {
			while (reader.readLine() != null) {
			}
			return reader.getLineNumber();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	String getKey(String line) {
		int count = 0;
		int cur = 0;
		do {
			cur = line.indexOf(' ', cur + 1);
			count++;
		} while (0 <= cur && count < 4);

		String key, value;
		if (0 <= cur && cur < line.length()) {
			key = line.substring(0, cur);
			value = line.substring(cur + 1, line.length());
		} else {
			key = line;
			value = "";
		}
		return key;
	}

	public String retrieveRecord(String key) throws FileNotFoundException, IOException {
		if (filter == null) {
			createFilter();
		}
		if (filter.appears(key)) {
			String value = retrieveRecordFrom(key, DIFF_FILE);
			return value;
		}
		return retrieveRecordFrom(key, DATABASE_FILE);
	}

	private String retrieveRecordFrom(String key, File dIFF_FILE2) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(dIFF_FILE2))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (getKey(line).equals(key))
					return line;
			}
		}
		return null;
	}

	public boolean isKeyinDifferentialFile(String key) throws FileNotFoundException, IOException {
		boolean check = false;
		try (BufferedReader br = new BufferedReader(new FileReader(DIFF_FILE))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (getKey(line).equals(key)) {
					check = true;
					break;
				}
			}
		}
		return check;
	}
}
