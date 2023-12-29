package com.mashreq.oa.utils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mashreq.oa.exceptions.FileStorageException;
import com.mashreq.oa.entity.PaymentData;

@Component
public class StringUtils {

	@Value("${document.servlet.openingpath}")
	private String servletPath;
	private static final String ALGORITHM = "RSA";
	private final static String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC9xKNqWdOHFzmC3kDf1qovVSHEySjhn0Tkvcredz+LgDOiFIOLehIjt3eExvZF/8f2IQM3soBJXMvv2+Urs99pZAmSSzsYLif8LpioOjxskmVri1OQ8GW4LwM9puipoiMxRDVwO7lIcwAbpJLaLTo/ItlKtsYRifEUhm5wmjslanMYJv77mgaq8PeS6JIJ4WqnSLwS2eZqQYIm0mWG+ekH/2J2u2I85fVZj79cLrZlRulBrt7nhzTT8u7ophoq/qMCFuGXzsdaantTy602xNOT2kxj/gyHI31DMUxUYp0xHK/bstu8dAgUUtuljABgVNsVXc0CI3Sx+Lag0uTUNQ5HAgMBAAECggEBAInsMRlK0AKPTq1e+6e0TVy5cyGjUqMpLtlRV/D4mqa5Ns3GOxVUU3rCDYvjT3rwvFSXCc+hXLv1RgO+voFU6jufCZXaN8kLQuR2uV0Ldn8yp6PST5o4HrYO9TwJ42/m980G1hAMWE3fx2RP6KvJ01uv6F31GWAF8cIJMpuEfRhjVV7kUocJaZ1H1blwvxj5nF8p4vMHB2irXIoSfp+iuXD3xdD+kbxq/ouqtuxjfO/QPIzdCKbqBpCBipcdI8n9WzZK7mYP6X+dy8cccgIHNybBQStqOQUftqxD4KOY55uIHn0BgOG1+w0MI/Qi0vRmsNg0WUcAPaTD7e74D++3+QECgYEA4rER6geNklYMlsANsoLJN6W8+TSdwiKsOEvhjM0McO79gZ4vksRwYcIWMQYQJWjJXC0MtyQ5OZUEkX4JCWMobkm1STl5nZQ2Rf1+636tYggEfZLaaUO48mEgNyczNhTD/v1l9a1yEZ5dZDMPyiV07xyCf2IqQMyrip7hLlON8X8CgYEA1k19jiRP9IK352Ut+2xN1HP37bWBvVkcWtpCEBgVEBze18UAkt9o9nKIw0UJcrL7YLlYAANaztEpNdA3Gw+4bK6LiMxpi7jr/9LF3w0AsTERW7DUrDXITAtJVqmms0a+BbcsfaV1NdIwtcpVThjbE/bv2PU6Gigx+9YORLFINzkCgYEAieAQeTqmzH4xoe6lZhFNuN7BFQD/gnf8LzFXuX9tNbLl1NQVMzru70ZQoPiDEX2uGrX7qdgKRg9we90gOelpScriy+p9IW5npCIN88VURu+Ba67J0IQ0FJcmNOVOrHHs00XjoY0gd77OJoc370bg3B2G8VsPP+I740/GvZpsFpkCgYAP44DqmAChlUuDSXomSPpgRRTdt/ZdjozOo54ASXjOUAWpo76OJShIFWfUanrv2RtQKY2/un/yE4nlpoFfbUP0MuC/jMBKjrRYrEzlY7ZobMXnsW2jMv2dvbx7Q2rLofQWwmT3D9xn2CSqZcz7VFZx3X4c7NmY9N/31wAJ0ccT6QKBgHnFWU/pudJwQ1fnYr/LZF554mFGjAz67JzG6KIudP/G8y6OwF7mjV+QFkhs/iyb9WYm5HxMH12oMVpkn/uE2ErnwNTtdj1ujNyC5L6eqL5PIqwWLzPSlEmEEOyI7JPAoytcLpa2FFQLT5RsEGsQktINJh1Lyyh4kNmuT9aFxsVA";

	private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

	public static boolean isNullOrEmpty(String value) {
		return "".equals(value);
	}

	public static boolean empty(final String s) {
		// Null-safe, short-circuit evaluation.
		return s == null || s.trim().isEmpty();
	}

	public static boolean checkForFileExtension(String supportedFileExtensions, String fileExtension) {
		boolean flag = false;
		String array[] = supportedFileExtensions.split(",");
		List<String> al = Arrays.asList(array);
		if (al.contains(fileExtension)) {
			System.out.println("Match");
			flag = true;
		}
		System.out.println("Final Status is::" + flag);
		return false;

	}

	public String encodeData(String url) {
		String encodedData = null;
		String truncatedPath = url.substring(url.indexOf("filename="), url.length()).replace("filename=", "").trim();
		System.out.println("Truncated String is" + truncatedPath);

		if (truncatedPath != "" || truncatedPath != null) {
			byte[] encodedByte = Base64.encodeBase64(truncatedPath.getBytes());
			String encoded = new String(encodedByte);
			System.out.println("Encoded Data is::" + encoded);
			LOGGER.info("Servlet Path::" + servletPath);
			encodedData = servletPath + encoded;
		}
		LOGGER.info("Encoded data is::" + encodedData);
		return encodedData;
	}

	/*
	 * public static String encodeData(String fileUrl) throws Exception {
	 * 
	 * String pubKey =
	 * "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvcSjalnThxc5gt5A39aqL1UhxMko4Z9E5L3K3nc/i4AzohSDi3oSI7d3hMb2Rf/H9iEDN7KASVzL79vlK7PfaWQJkks7GC4n/C6YqDo8bJJla4tTkPBluC8DPaboqaIjMUQ1cDu5SHMAG6SS2i06PyLZSrbGEYnxFIZucJo7JWpzGCb++5oGqvD3kuiSCeFqp0i8EtnmakGCJtJlhvnpB/9idrtiPOX1WY+/XC62ZUbpQa7e54c00/Lu6KYaKv6jAhbhl87HWmp7U8utNsTTk9pMY/4MhyN9QzFMVGKdMRyv27LbvHQIFFLbpYwAYFTbFV3NAiN0sfi2oNLk1DUORwIDAQAB";
	 * System.out.println("Public Key:" + pubKey); byte[] encryptedData = null; try
	 * { String
	 * truncatedPath=fileUrl.substring(fileUrl.indexOf("filename="),fileUrl.length()
	 * ).replace("filename=", "").trim(); encryptedData =
	 * encrypt(org.apache.commons.codec.binary.Base64.decodeBase64(pubKey),
	 * truncatedPath.getBytes()); } catch (InvalidKeyException e) {
	 * e.printStackTrace(); LOGGER.error("InvalidKeyException"); throw new
	 * FileStorageException("InvalidKeyException" + e.getMessage()); } catch
	 * (NoSuchAlgorithmException e) { e.printStackTrace();
	 * LOGGER.error("NoSuchAlgorithmException"); throw new
	 * FileStorageException("NoSuchAlgorithmException" + e.getMessage()); } catch
	 * (NoSuchPaddingException e) { e.printStackTrace();
	 * LOGGER.error("NoSuchPaddingException"); throw new
	 * FileStorageException("NoSuchPaddingException" + e.getMessage()); } catch
	 * (InvalidKeySpecException e) { e.printStackTrace();
	 * LOGGER.error("InvalidKeySpecException"); throw new
	 * FileStorageException("InvalidKeySpecException" + e.getMessage()); } catch
	 * (IllegalBlockSizeException e) { e.printStackTrace();
	 * LOGGER.error("IllegalBlockSizeException"+e.getMessage()); throw new
	 * FileStorageException("File Path + Name should not be longer than 245 chars, please rename the file name and upload it again"
	 * ); } catch (BadPaddingException e) { e.printStackTrace();
	 * LOGGER.error("BadPaddingException"); throw new
	 * FileStorageException("BadPaddingException" + e.getMessage()); } String bPath
	 * = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(
	 * encryptedData); LOGGER.info("Servlet Path::"
	 * +"http://10.210.194.4:8080/genericservice/displayImage?filename=");
	 * bPath="http://10.210.194.4:8080/genericservice/displayImage?filename="+bPath;
	 * return bPath; }
	 * 
	 * public static byte[] encrypt(byte[] publicKey, byte[] inputData) throws
	 * Exception {
	 * 
	 * PublicKey key = KeyFactory.getInstance(ALGORITHM).generatePublic(new
	 * X509EncodedKeySpec(publicKey));
	 * 
	 * Cipher cipher = Cipher.getInstance(ALGORITHM);
	 * cipher.init(Cipher.ENCRYPT_MODE, key);
	 * 
	 * byte[] encryptedBytes = cipher.doFinal(inputData);
	 * 
	 * return encryptedBytes; }
	 */
	public static boolean isRowEmptyData(Row row) {
		DataFormatter df = new DataFormatter();
		boolean isEmpty = true;
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
					&& org.apache.commons.lang3.StringUtils.isNotBlank(cell.toString())) {
				isEmpty = false;
			}
		}
		return isEmpty;
	}

	public static boolean checkForUtil(MultipartFile file) throws IOException {
//		Workbook workbook = new HSSFWorkbook(file.getInputStream());
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		boolean flag = false;
		Sheet sheet = workbook.getSheetAt(0);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			if (isRowEmptyData(sheet.getRow(i))) {
				sheet.removeRow(sheet.getRow(i));
				// sheet.shiftRows(i+1, sheet.getLastRowNum(), -1);
				// i--;
			}
		}
		Iterator<Row> rows = sheet.iterator();

		while (rows.hasNext()) {

			Row currentRow = rows.next();
			Iterator<Cell> cellsInRow = currentRow.iterator();
			PaymentData data = new PaymentData();

			int cellIdx = 0;
			while (cellsInRow.hasNext()) {

				Cell currentCell = cellsInRow.next();

				switch (cellIdx) {
				case 0:

					break;

				case 1:

					break;

				case 2:

					break;

				case 3:

					String util = "Utility Provider Name";

					if (currentCell.getStringCellValue().trim().equalsIgnoreCase(util)) {
						flag = true;
						LOGGER.info("Match" + flag);

					}
					break;

				case 4:

					break;
				case 5:
					data.setPaymentCurrency(currentCell.getStringCellValue());
					break;

				case 6:

					break;

				default:
					break;

				}

				cellIdx++;

			}

			if (flag) {
				break;
			}

		}
		LOGGER.info("Final flag is::" + flag);
		return flag;
	}

	public static boolean checkForUtilXls(MultipartFile file) throws IOException {
//		Workbook workbook = new HSSFWorkbook(file.getInputStream());
		HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
		boolean flag = false;
		HSSFSheet sheet = workbook.getSheetAt(0);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			if (isRowEmptyData(sheet.getRow(i))) {
				sheet.removeRow(sheet.getRow(i));
				// sheet.shiftRows(i+1, sheet.getLastRowNum(), -1);
				// i--;
			}
		}
		Iterator<Row> rows = sheet.iterator();

		while (rows.hasNext()) {

			Row currentRow = rows.next();
			Iterator<Cell> cellsInRow = currentRow.iterator();
		//	LOGGER.info("cellsInRow:::" + Arrays.asList(cellsInRow));
			PaymentData data = new PaymentData();

			int cellIdx = 0;
			while (cellsInRow.hasNext()) {

				Cell currentCell = cellsInRow.next();

				switch (cellIdx) {
				case 0:

					break;

				case 1:

					break;

				case 2:

					break;

				case 3:

					String util = "Utility Provider Name";

					if (currentCell.getStringCellValue().trim().equalsIgnoreCase(util)) {
						flag = true;
						LOGGER.info("Match" + flag);

					}
					break;

				case 4:

					break;
				case 5:
					data.setPaymentCurrency(currentCell.getStringCellValue());
					break;

				case 6:

					break;

				default:
					break;

				}

				cellIdx++;

			}

			if (flag) {
				break;
			}

		}
		LOGGER.info("Final flag is::" + flag);
		return flag;
	}

	public static boolean checkForCustomer(MultipartFile file) throws IOException {
//	Workbook workbook = new HSSFWorkbook(file.getInputStream());
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		boolean flag = false;
		Sheet sheet = workbook.getSheetAt(0);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			if (isRowEmptyData(sheet.getRow(i))) {
				sheet.removeRow(sheet.getRow(i));
				// sheet.shiftRows(i+1, sheet.getLastRowNum(), -1);
				// i--;
			}
		}
		Iterator<Row> rows = sheet.iterator();

		while (rows.hasNext()) {

			Row currentRow = rows.next();
			Iterator<Cell> cellsInRow = currentRow.iterator();
			PaymentData data = new PaymentData();

			int cellIdx = 0;
			while (cellsInRow.hasNext()) {

				Cell currentCell = cellsInRow.next();

				switch (cellIdx) {
				case 0:

					break;

				case 1:

					break;

				case 2:

					break;

				case 3:

					break;

				case 4:

					break;
				case 5:
					data.setPaymentCurrency(currentCell.getStringCellValue());
					break;

				case 6:

					break;
				case 7:
					String customer = "Customer Reference";

					if (currentCell.getStringCellValue().trim().equalsIgnoreCase(customer)) {
						flag = true;
						LOGGER.info("Match" + flag);

					}
					break;

				default:
					break;

				}

				cellIdx++;

			}

			if (flag) {
				break;
			}

		}
		LOGGER.info("Final flag is::" + flag);
		return flag;

	}

	public static boolean checkForCustomerXls(MultipartFile file) throws IOException {
//		Workbook workbook = new HSSFWorkbook(file.getInputStream());
		HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
		boolean flag = false;
		HSSFSheet sheet = workbook.getSheetAt(0);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			if (isRowEmptyData(sheet.getRow(i))) {
				sheet.removeRow(sheet.getRow(i));
				// sheet.shiftRows(i+1, sheet.getLastRowNum(), -1);
				// i--;
			}
		}
		Iterator<Row> rows = sheet.iterator();

		while (rows.hasNext()) {

			Row currentRow = rows.next();
			Iterator<Cell> cellsInRow = currentRow.iterator();
			PaymentData data = new PaymentData();

			int cellIdx = 0;
			while (cellsInRow.hasNext()) {

				Cell currentCell = cellsInRow.next();

				switch (cellIdx) {
				case 0:

					break;

				case 1:

					break;

				case 2:

					break;

				case 3:

					break;

				case 4:

					break;
				case 5:
					data.setPaymentCurrency(currentCell.getStringCellValue());
					break;

				case 6:

					break;
				case 7:
					String customer = "Customer Reference";

					if (currentCell.getStringCellValue().trim().equalsIgnoreCase(customer)) {
						flag = true;
						LOGGER.info("Match" + flag);

					}
					break;

				default:
					break;

				}

				cellIdx++;

			}

			if (flag) {
				break;
			}

		}
		LOGGER.info("Final flag is::" + flag);
		return flag;
	}
}
