package PA1;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Helper {

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static long nextPrime(long n) {
		BigInteger b = new BigInteger(String.valueOf(n));
		return Long.parseLong(b.nextProbablePrime().toString());
	}

	public static String getRandomString() {
		Random r = new Random();
		int length = r.nextInt(15) + 5;
		StringBuilder sb = new StringBuilder();
		while (length-- != 0) {
			int idx = (int) (r.nextDouble() * ALPHA_NUMERIC_STRING.length());
			sb.append(ALPHA_NUMERIC_STRING.charAt(idx));
		}
		return sb.toString();
	}

	@SuppressWarnings("resource")
	public static void exportNewExcel(String outputPath, String sheetName, int startRowNum, int startColNum, String[] colHeaders, List<String> dataList) {
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        Row row = sheet.createRow(startRowNum++);
        int colNum = startColNum;
        for (String header : colHeaders) 
        		row.createCell(colNum++).setCellValue(header);
        for (String line : dataList) {
        		String[] arr = line.split(" ");
        		row = sheet.createRow(startRowNum++);
        		colNum = startColNum;
        		for (String e : arr)
        			row.createCell(colNum++).setCellValue(e);
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(outputPath));
            workbook.write(out);
            out.close();
            System.out.println("export successfully on disk.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
}
