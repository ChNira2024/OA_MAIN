package com.mashreq.oa.service;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mashreq.oa.dao.PaymentDataDao;
import com.mashreq.oa.entity.Buildings;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;
import com.mashreq.oa.entity.PropertyGroups;
import com.mashreq.oa.utils.StringUtils;

@Service
public class PaymentDataServiceImpl implements PaymentDataService {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(PaymentDataServiceImpl.class);

	@Autowired
	private PaymentDataDao paymentDataDao;

	@Value("${payment.excel.sheetName}")
	String sheetName;

	@Autowired
	public HttpSession session;

	@Autowired
	private StringUtils stringUtils;

	/*
	 * upload excel file data to database
	 */
	@Override
	public List<Long> uploadExcel(MultipartFile file, String username, Integer mgmtCompId) {

		logger.info("Calling uploadExcel() in PaymentServiceImpl");
		List<Long> list = null;

		try {
			logger.info("file.getInputStream()::" + file.getInputStream());
			String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
			logger.info("Uploaded File Type is::" + fileExtension);
			if (fileExtension.equalsIgnoreCase("xls")) {
				logger.info("Entered in to if-block for xls condition");
				logger.info("Calling readXlsFile Method for Insertion!! File type is:" + fileExtension);
				list = readXlsFile(file, username, mgmtCompId);
				logger.info("xls file read successfully..");

			} else if (fileExtension.equalsIgnoreCase("xlsx")) {
				logger.info("Entered in to else if block for xlsx condition");
				logger.info("Calling readXlsxFile Method for Insertion!! File type is:" + fileExtension);
				list = readXlsxFile(file, username, mgmtCompId);
				logger.info("xlsx file read successfully..");

			} else {

				logger.info("Invalid File Type!! Please Upload Valid Excel File Again!");
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Occured Due to:" + e.getMessage());
			logger.error("Cause of Exception:" + e.getCause());
			return null;
		}
		return list;

	}

	/*
	 * Get List of PaymentData
	 */
	@Override
	public List<PaymentData> getListData() {

		logger.info("Calling getListData() in PaymentServiceImpl");

		List<PaymentData> list = paymentDataDao.getListData();
		logger.info("Payment List Size is::" + list.size());
		for (PaymentData pymtreq : list) {
			// pymtreq.setNamevalue(new NameValue(pymtreq.getMatrixRefNo(),
			// pymtreq.getPymtReqId()));
			try {
				// logger.info("pymtreq.getInitiatorDate()"+pymtreq.getInitiatorDate());
				if (pymtreq.getInitiatorDate() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

					String convertedDate = sdf.format(pymtreq.getInitiatorDate());
					// logger.info("Converted Format is:::"+convertedDate);
					pymtreq.setDisplayInitiatorDate(convertedDate);
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Exception in getListData() ::: " + e.getCause());
				return null;
			}

		}

		return list;

	}

	@Override
	public List<PaymentData> getSearchList(PaymentSearchInput searchInput) {

		logger.info("Calling getSearchList() in PaymentRequestService");

		List<PaymentData> searchList = paymentDataDao.getSearchList(searchInput);
		if (searchList != null && searchList.size() > 0) {
			for (PaymentData pymtreq : searchList) {
				// pymtreq.setNamevalue(new NameValue(pymtreq.getMatrixRefNo(),
				// pymtreq.getPymtReqId()));
				try {
					logger.info("pymtreq.getInitiatorDate()" + pymtreq.getInitiatorDate());
					if (pymtreq.getInitiatorDate() != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

						String convertedDate = sdf.format(pymtreq.getInitiatorDate());
						// logger.info("Converted Format is:::"+convertedDate);
						pymtreq.setDisplayInitiatorDate(convertedDate);
					}

				} catch (Exception e) {
					e.printStackTrace();
					logger.info("Exception in getListData() ::: " + e.getCause());
					return null;
				}
			}
			return searchList;
		} else {
			logger.info("No Records for  getListData() ::: " + searchList.size());
			return null;
		}

	}

	@Override
	public List<Buildings> getBuildings(Integer mgmtCompId) {
		logger.info("Calling getBuildings() in PaymentRequestService");
		List<Buildings> bd = paymentDataDao.getBuildings(mgmtCompId);
		return bd;
	}

	public static void main(String[] args) {
		List<String> al = new ArrayList<String>();
		String dataFromExcel = "Naveen Kumar";

		al.add("NAVEEN KUMAR");
		al.replaceAll(String::toUpperCase);
		if (al.contains(dataFromExcel.toUpperCase())) {
			System.out.println(al);
		}

	}

	@Override
	public List<PropertyGroups> getProperties(Integer mgmtCompId) {
		// TODO Auto-generated method stub
		logger.info("Calling getProperties() in PaymentRequestService");
		List<PropertyGroups> bd = paymentDataDao.getProperties(mgmtCompId);

		return bd;
	}

	public List<Long> readXlsFile(MultipartFile file, String username, Integer mgmtCompId)
			throws IOException, ParseException {
		logger.info("Entered into readXlsFile() method");
		List<PaymentData> payments = null;
		List<Long> matrixList = null;
		List<Long> list = null;
		String debitAccountNoDesc = null;
		String supplierName = null;
		Long matrixNo = null;
		Map<Long, PaymentData> pymt = new HashMap();

		HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
		HSSFSheet sheet = workbook.getSheetAt(0);
		boolean checkFlag = false;
		boolean customerFlag = false;
		checkFlag = StringUtils.checkForUtilXls(file);

		logger.info("checkFlag::" + checkFlag);
		customerFlag = StringUtils.checkForCustomerXls(file);
		logger.info("customerFlag:::" + customerFlag);
		logger.info("Checking for Util Flags :::" + checkFlag + "Checking for Customer Flags :::" + customerFlag);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			if (StringUtils.isRowEmptyData(sheet.getRow(i))) {
				sheet.removeRow(sheet.getRow(i));
				// sheet.shiftRows(i+1, sheet.getLastRowNum(), -1);
				// i--;
			}
		}

		Iterator<Row> rows = sheet.iterator();

		matrixList = new ArrayList<Long>();
		payments = new ArrayList<PaymentData>();
		list = new ArrayList<Long>();

		long matrixfileRefno = System.currentTimeMillis();
		logger.info("Matrix file ref is:: " + matrixfileRefno);

		int rowNumber = 0;
		if (!checkFlag && !customerFlag) {
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				logger.info("Inside if block of excel upload");

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();
				PaymentData data = new PaymentData();

				int cellIdx = 0;

				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					logger.info("cell type:" + currentCell.getCellType());// 0 for Numeric,1 for String
					switch (cellIdx) {
					case 0:
						if (currentCell.getCellType() == 1) {
							logger.info("Mat" + currentCell.getStringCellValue());
							matrixNo = (long) Long.parseLong(currentCell.getStringCellValue());
						} else {
							matrixNo = (long) currentCell.getNumericCellValue();
						}
						data.setMatrixRefNo(matrixNo);
						break;

					case 1:
						data.setSubProduct(currentCell.getStringCellValue());
						break;

					case 2:
						debitAccountNoDesc = currentCell.getStringCellValue();
						data.setDebitAccountNumberDesc(currentCell.getStringCellValue());
						break;

					case 3:
						supplierName = currentCell.getStringCellValue().split("-")[0];
						data.setBeneficiaryName(currentCell.getStringCellValue());
						break;

					case 4:
						java.util.Date date = new SimpleDateFormat("dd/MM/yyyy")
								.parse(currentCell.getStringCellValue());
						data.setInitiatorDates(date);
						break;

					case 5:
						data.setPaymentCurrency(currentCell.getStringCellValue());
						break;

					case 6:
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(currentCell.getStringCellValue());
						Double doublevalue = number.doubleValue();
						data.setInvoiceAmount(doublevalue);
						break;

					case 7:
						data.setCustomerReference(currentCell.getStringCellValue());
						break;

					case 8:
						data.setInitiatorNameDateTime(currentCell.getStringCellValue());
						break;

					case 9:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setServiceCode(currentCell.getStringCellValue());
							logger.info("Service Code::" + data.getServiceCode());
							break;
						}

					case 10:
						if (currentCell.getNumericCellValue() != 0) {
							data.setBudgetYear(currentCell.getNumericCellValue());
							logger.info("Budget Year::" + data.getBudgetYear());
							break;
						}
					case 11:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setBifurcation(currentCell.getStringCellValue());
							logger.info("Bifurcation::" + data.getBifurcation());
							break;
						}

					default:
						break;

					}

					cellIdx++;
				}

				logger.info(" Supplier Name from excel: " + supplierName);
				List<String> suppliers = paymentDataDao.getSupplierList();
				suppliers.replaceAll(String::toUpperCase);
				Integer supplierId = null;

				if (!suppliers.contains(supplierName.toUpperCase())) {
					supplierId = paymentDataDao.insertSupplier(supplierName);
					data.setSupplierId(supplierId);
				}

				else {
					supplierId = paymentDataDao.getSupplierId(supplierName);
					data.setSupplierId(supplierId);
				}

				String buildingName = debitAccountNoDesc.split("-|\\[")[1];
				logger.info(" Building Name from excel: " + buildingName);
				boolean checkBuidlingNameStatus = paymentDataDao.checkForBuildingName(buildingName);
				// List<String> buildingNames = paymentDataDao.getBuildingList();
				// buildingNames.replaceAll(String::toUpperCase);

				// logger.info("Building Names from db:: "+buildingNames);

				Integer buildingId = null;

				if (!checkBuidlingNameStatus) {
					buildingId = paymentDataDao.insertBuilding(buildingName);
					data.setBuildingId(buildingId);
				}

				else {
					buildingId = paymentDataDao.getBuildingId(buildingName);
					data.setBuildingId(buildingId);
				}

				String mcName = paymentDataDao.getManagementCompany(mgmtCompId);

				if (mcName.toUpperCase().contains("EDACOM") || mcName.toUpperCase().contains("NAKHEEL")) {

					logger.info("Inside if mcname");
					data.setPaymentType("BULK");
				} else {
					logger.info("Inside else mcname");
					data.setPaymentType("INDIVIDUAL");
				}

				if (!mcName.toUpperCase().contains("NAKHEEL")) {
					logger.info("Checking for prop id coz mcname is not nakheel");

					Integer propId = paymentDataDao.getPropId(buildingName, mgmtCompId);

					logger.info("Prop Id in service:: " + propId);

					if (propId != 0) {
						logger.info("Inside Insert prop id");

						boolean flag = paymentDataDao.checkForMapping(buildingId, propId);

						logger.info("Boolean flag in service:: " + flag);

						if (!flag) {
							paymentDataDao.insertMappingData(buildingId, propId, mgmtCompId); // raghuram
						}
					}
				}

				data.setDebitAccountNo(debitAccountNoDesc.split("-")[0]);
				data.setMgmtCompId(mgmtCompId);
				data.setStatus("PENDING");
				data.setUploadedBy(username);
				data.setMatrixFileRefNo(matrixfileRefno);

				pymt.put(matrixNo, data);

				// payments.add(data);

			}
		} else if (customerFlag && !checkFlag) {
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				logger.info("Inside else if block of excel upload");

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();
				PaymentData data = new PaymentData();

				int cellIdx = 0;

				while (cellsInRow.hasNext()) {

					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						if (currentCell.getCellType() == 1) {
							logger.info("Mat" + currentCell.getStringCellValue());
							matrixNo = (long) Long.parseLong(currentCell.getStringCellValue());
						} else {
							matrixNo = (long) currentCell.getNumericCellValue();
						}
						data.setMatrixRefNo(matrixNo);
						break;

					case 1:
						data.setSubProduct(currentCell.getStringCellValue());
						break;

					case 2:
						debitAccountNoDesc = currentCell.getStringCellValue();
						data.setDebitAccountNumberDesc(currentCell.getStringCellValue());
						break;

					case 3:
						supplierName = currentCell.getStringCellValue().split("-")[0];
						data.setBeneficiaryName(currentCell.getStringCellValue());
						break;

					case 4:
//						java.util.Date date = new SimpleDateFormat("dd/MM/yyyy")
////								.parse(currentCell.getStringCellValue());//original
						if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
							System.out.println("String:: " + currentCell.getStringCellValue());
							java.util.Date date = new SimpleDateFormat("dd/MM/yyyy")
									.parse(currentCell.getStringCellValue());// original
						} else {
							System.out.println("DATE::: " + currentCell.getDateCellValue());
							data.setInitiatorDates(currentCell.getDateCellValue());
						}

						break;

					case 5:
						data.setPaymentCurrency(currentCell.getStringCellValue());
						break;

					case 6:
						Double doublevalue = null;
						if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
							NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
							Number number = format.parse(currentCell.getStringCellValue());
							doublevalue = number.doubleValue();
						} else {
							doublevalue = currentCell.getNumericCellValue();
						}
						data.setInvoiceAmount(doublevalue);
						break;

					case 7:
						data.setCustomerReference(currentCell.getStringCellValue());
						break;

					case 8:
						data.setInitiatorNameDateTime(currentCell.getStringCellValue());
						break;

					case 9:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setServiceCode(currentCell.getStringCellValue());
							logger.info("Service Code::" + data.getServiceCode());
							break;
						}

					case 10:
						if (currentCell.getNumericCellValue() != 0) {
							data.setBudgetYear(currentCell.getNumericCellValue());
							logger.info("Budget Year::" + data.getBudgetYear());
							break;
						}
					case 11:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setBifurcation(currentCell.getStringCellValue());
							logger.info("Bifurcation::" + data.getBifurcation());
							break;
						}
					default:
						break;

					}

					cellIdx++;
				}

				logger.info(" Supplier Name from excel: " + supplierName);
				List<String> suppliers = paymentDataDao.getSupplierList();
				suppliers.replaceAll(String::toUpperCase);
				Integer supplierId = null;

				if (!suppliers.contains(supplierName.toUpperCase())) {
					supplierId = paymentDataDao.insertSupplier(supplierName);
					data.setSupplierId(supplierId);
				}

				else {
					supplierId = paymentDataDao.getSupplierId(supplierName);
					data.setSupplierId(supplierId);
				}

				String buildingName = debitAccountNoDesc.split("-|\\[")[1];
				logger.info(" Building Name from excel: " + buildingName);
				boolean checkBuidlingNameStatus = paymentDataDao.checkForBuildingName(buildingName);
				// List<String> buildingNames = paymentDataDao.getBuildingList();
				// buildingNames.replaceAll(String::toUpperCase);

				// logger.info("Building Names from db:: "+buildingNames);

				Integer buildingId = null;

				if (!checkBuidlingNameStatus) {
					buildingId = paymentDataDao.insertBuilding(buildingName);
					data.setBuildingId(buildingId);
				}

				else {
					buildingId = paymentDataDao.getBuildingId(buildingName);
					data.setBuildingId(buildingId);
				}

				String mcName = paymentDataDao.getManagementCompany(mgmtCompId);

				if (mcName.toUpperCase().contains("EDACOM") || mcName.toUpperCase().contains("NAKHEEL")) {

					data.setPaymentType("BULK");
				} else {
					data.setPaymentType("INDIVIDUAL");
				}

				if (!mcName.toUpperCase().contains("NAKHEEL")) {
					logger.info("Checking for prop id coz mcname is not nakheel");

					Integer propId = paymentDataDao.getPropId(buildingName, mgmtCompId);
					logger.info("Prop Id in service:: " + propId);

					if (propId != 0) {
						logger.info("Inside Insert prop id");

						boolean flag = paymentDataDao.checkForMapping(buildingId, propId);

						logger.info("Boolean flag in service:: " + flag);

						if (!flag) {
							paymentDataDao.insertMappingData(buildingId, propId, mgmtCompId); // raghuram
						}
					}
				}

				data.setDebitAccountNo(debitAccountNoDesc.split("-")[0]);
				data.setMgmtCompId(mgmtCompId);
				data.setStatus("PENDING");
				data.setUploadedBy(username);
				data.setMatrixFileRefNo(matrixfileRefno);

				pymt.put(matrixNo, data);

				// payments.add(data);

			}
		}

		else {
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				logger.info("Inside else block of excel upload");
				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();
				PaymentData data = new PaymentData();

				int cellIdx = 0;

				while (cellsInRow.hasNext()) {

					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						if (currentCell.getCellType() == 1) {
							logger.info("Mat" + currentCell.getStringCellValue());
							matrixNo = (long) Long.parseLong(currentCell.getStringCellValue());
						} else {
							matrixNo = (long) currentCell.getNumericCellValue();
						}
						data.setMatrixRefNo(matrixNo);
						break;

					case 1:
						data.setSubProduct(currentCell.getStringCellValue());
						break;

					case 2:
						debitAccountNoDesc = currentCell.getStringCellValue();
						data.setDebitAccountNumberDesc(currentCell.getStringCellValue());
						break;

					case 3:
						supplierName = currentCell.getStringCellValue().split("-")[0];
						data.setBeneficiaryName(currentCell.getStringCellValue());
						break;
					case 4:

						break;
					case 5:
						java.util.Date date = new SimpleDateFormat("dd/MM/yyyy")
								.parse(currentCell.getStringCellValue());
						data.setInitiatorDates(date);
						break;

					case 6:
						data.setPaymentCurrency(currentCell.getStringCellValue());
						break;

					case 7:
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(currentCell.getStringCellValue());
						Double doublevalue = number.doubleValue();
						data.setInvoiceAmount(doublevalue);
						break;

					case 8:
						data.setInitiatiorName(currentCell.getStringCellValue());
						break;

					case 9:

						data.setInitiatorNameDateTime(data.getInitiatiorName() + currentCell.getStringCellValue());
						logger.info("InitiatorNameDateTim::" + data.getInitiatorNameDateTime());
						break;

					case 10:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setServiceCode(currentCell.getStringCellValue());
							logger.info("Service Code::" + data.getServiceCode());
							break;
						}

					case 11:
						if (currentCell.getNumericCellValue() != 0) {
							data.setBudgetYear(currentCell.getNumericCellValue());
							logger.info("Budget Year::" + data.getBudgetYear());
							break;
						}

					case 12:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setBifurcation(currentCell.getStringCellValue());
							logger.info("Bifurcation::" + data.getBifurcation());
							break;
						}
					default:
						break;

					}

					cellIdx++;
				}

				logger.info(" Supplier Name from excel: " + supplierName);
				List<String> suppliers = paymentDataDao.getSupplierList();
				suppliers.replaceAll(String::toUpperCase);
				Integer supplierId = null;

				if (!suppliers.contains(supplierName.toUpperCase())) {
					supplierId = paymentDataDao.insertSupplier(supplierName);
					data.setSupplierId(supplierId);
				}

				else {
					supplierId = paymentDataDao.getSupplierId(supplierName);
					data.setSupplierId(supplierId);
				}

				String buildingName = debitAccountNoDesc.split("-|\\[")[1];
				logger.info(" Building Name from excel: " + buildingName);
				boolean checkBuidlingNameStatus = paymentDataDao.checkForBuildingName(buildingName);
				// List<String> buildingNames = paymentDataDao.getBuildingList();
				// buildingNames.replaceAll(String::toUpperCase);

				// logger.info("Building Names from db:: "+buildingNames);

				Integer buildingId = null;

				if (!checkBuidlingNameStatus) {
					buildingId = paymentDataDao.insertBuilding(buildingName);
					data.setBuildingId(buildingId);
				}

				else {
					buildingId = paymentDataDao.getBuildingId(buildingName);
					data.setBuildingId(buildingId);
				}

				String mcName = paymentDataDao.getManagementCompany(mgmtCompId);

				if (mcName.toUpperCase().contains("EDACOM") || mcName.toUpperCase().contains("NAKHEEL")) {

					data.setPaymentType("BULK");
				} else {
					data.setPaymentType("INDIVIDUAL");
				}

				if (!mcName.toUpperCase().contains("NAKHEEL")) {
					logger.info("Checking for prop id coz mcname is not nakheel");

					Integer propId = paymentDataDao.getPropId(buildingName, mgmtCompId);
					logger.info("Prop Id in service:: " + propId);

					if (propId != 0) {
						logger.info("Inside Insert prop id");

						boolean flag = paymentDataDao.checkForMapping(buildingId, propId);

						logger.info("Boolean flag in service:: " + flag);

						if (!flag) {
							paymentDataDao.insertMappingData(buildingId, propId, mgmtCompId); // raghuram
						}
					}
				}

				data.setDebitAccountNo(debitAccountNoDesc.split("-")[0]);
				data.setMgmtCompId(mgmtCompId);
				data.setStatus("PENDING");
				data.setUploadedBy(username);
				data.setMatrixFileRefNo(matrixfileRefno);

				pymt.put(matrixNo, data);

			}
		}

		logger.info(" Full Map is: " + pymt);

		for (Long mtrixRefNo : pymt.keySet()) {
			logger.info("Matrix no from keyset: " + mtrixRefNo);

			matrixList.add(mtrixRefNo);
		}

		List<Long> outputMatrixNos = paymentDataDao.getMatrixRefNos(matrixList);

		logger.info("Db Matrix Nos. are: " + outputMatrixNos);

		list = new ArrayList<Long>();

		for (PaymentData pymtData : pymt.values()) {
			logger.info("PYMT VALUES: " + pymtData.getMatrixRefNo());

			if (!outputMatrixNos.contains(pymtData.getMatrixRefNo())) {

				payments.add(pymtData);
			} else {
				list.add(pymtData.getMatrixRefNo());
			}
		}

		logger.info("PYMT DATA TO SEND TO DB" + payments);
		logger.info("PYMT DATA Not TO SEND TO DB" + list);

		paymentDataDao.uploadExcel(payments);

		workbook.close();
		return list;

	}

	public List<Long> readXlsxFile(MultipartFile file, String username, Integer mgmtCompId)
			throws IOException, ParseException {
		logger.info("Entered into readXlsxFile() method");
		List<PaymentData> payments = null;
		List<Long> matrixList = null;
		List<Long> list = null;

		Workbook workbook = new XSSFWorkbook(file.getInputStream());

		String debitAccountNoDesc = null;
		String supplierName = null;
		Long matrixNo = null;
		Map<Long, PaymentData> pymt = new HashMap();
		Sheet sheet = workbook.getSheetAt(0);
		boolean checkFlag = false;
		boolean customerFlag = false;

		checkFlag = StringUtils.checkForUtil(file);
		logger.info("checkFlag::" + checkFlag);
		customerFlag = StringUtils.checkForCustomer(file);
		logger.info("customerFlag:::" + customerFlag);
		logger.info("Checking for Util Flags :::" + checkFlag + "Checking for Customer Flags :::" + customerFlag);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			if (StringUtils.isRowEmptyData(sheet.getRow(i))) {
				sheet.removeRow(sheet.getRow(i));
				// sheet.shiftRows(i+1, sheet.getLastRowNum(), -1);
				// i--;
			}
		}

		Iterator<Row> rows = sheet.iterator();

		matrixList = new ArrayList<Long>();
		payments = new ArrayList<PaymentData>();
		list = new ArrayList<Long>();

		long matrixfileRefno = System.currentTimeMillis();
		logger.info("Matrix file ref is:: " + matrixfileRefno);

		int rowNumber = 0;
		if (!checkFlag && !customerFlag) {
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				logger.info("Inside if block of excel upload");

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();
				PaymentData data = new PaymentData();

				int cellIdx = 0;

				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					logger.info("cell type:" + currentCell.getCellType());// 0 for Numeric,1 for String
					switch (cellIdx) {
					case 0:
						if (currentCell.getCellType() == 1) {
							logger.info("Mat" + currentCell.getStringCellValue());
							matrixNo = (long) Long.parseLong(currentCell.getStringCellValue());
						} else {
							matrixNo = (long) currentCell.getNumericCellValue();
						}
						data.setMatrixRefNo(matrixNo);
						break;

					case 1:
						data.setSubProduct(currentCell.getStringCellValue());
						break;

					case 2:
						debitAccountNoDesc = currentCell.getStringCellValue();
						data.setDebitAccountNumberDesc(currentCell.getStringCellValue());
						break;

					case 3:
						supplierName = currentCell.getStringCellValue().split("-")[0];
						data.setBeneficiaryName(currentCell.getStringCellValue());
						break;

					case 4:
						java.util.Date date = new SimpleDateFormat("dd/MM/yyyy")
								.parse(currentCell.getStringCellValue());
						data.setInitiatorDates(date);
						break;

					case 5:
						data.setPaymentCurrency(currentCell.getStringCellValue());
						break;

					case 6:
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(currentCell.getStringCellValue());
						Double doublevalue = number.doubleValue();
						data.setInvoiceAmount(doublevalue);
						break;

					case 7:
						data.setCustomerReference(currentCell.getStringCellValue());
						break;

					case 8:
						data.setInitiatorNameDateTime(currentCell.getStringCellValue());
						break;

					case 9:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setServiceCode(currentCell.getStringCellValue());
							logger.info("Service Code::" + data.getServiceCode());
							break;
						}

					case 10:
						if (currentCell.getNumericCellValue() != 0) {
							data.setBudgetYear(currentCell.getNumericCellValue());
							logger.info("Budget Year::" + data.getBudgetYear());
							break;
						}
					case 11:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setBifurcation(currentCell.getStringCellValue());
							logger.info("Bifurcation::" + data.getBifurcation());
							break;
						}

					default:
						break;

					}

					cellIdx++;
				}

				logger.info(" Supplier Name from excel: " + supplierName);
				List<String> suppliers = paymentDataDao.getSupplierList();
				suppliers.replaceAll(String::toUpperCase);
				Integer supplierId = null;

				if (!suppliers.contains(supplierName.toUpperCase())) {
					supplierId = paymentDataDao.insertSupplier(supplierName);
					data.setSupplierId(supplierId);
				}

				else {
					supplierId = paymentDataDao.getSupplierId(supplierName);
					data.setSupplierId(supplierId);
				}

				String buildingName = debitAccountNoDesc.split("-|\\[")[1];
				logger.info(" Building Name from excel: " + buildingName);
				boolean checkBuidlingNameStatus = paymentDataDao.checkForBuildingName(buildingName);
				// List<String> buildingNames = paymentDataDao.getBuildingList();
				// buildingNames.replaceAll(String::toUpperCase);

				// logger.info("Building Names from db:: "+buildingNames);

				Integer buildingId = null;

				if (!checkBuidlingNameStatus) {
					buildingId = paymentDataDao.insertBuilding(buildingName);
					data.setBuildingId(buildingId);
				}

				else {
					buildingId = paymentDataDao.getBuildingId(buildingName);
					data.setBuildingId(buildingId);
				}

				String mcName = paymentDataDao.getManagementCompany(mgmtCompId);

				if (mcName.toUpperCase().contains("EDACOM") || mcName.toUpperCase().contains("NAKHEEL")) {

					logger.info("Inside if mcname");
					data.setPaymentType("BULK");
				} else {
					logger.info("Inside else mcname");
					data.setPaymentType("INDIVIDUAL");
				}

				if (!mcName.toUpperCase().contains("NAKHEEL")) {
					logger.info("Checking for prop id coz mcname is not nakheel");

					Integer propId = paymentDataDao.getPropId(buildingName, mgmtCompId);

					logger.info("Prop Id in service:: " + propId);

					if (propId != 0) {
						logger.info("Inside Insert prop id");

						boolean flag = paymentDataDao.checkForMapping(buildingId, propId);

						logger.info("Boolean flag in service:: " + flag);

						if (!flag) {
							paymentDataDao.insertMappingData(buildingId, propId, mgmtCompId); // raghuram
						}
					}
				}

				data.setDebitAccountNo(debitAccountNoDesc.split("-")[0]);
				data.setMgmtCompId(mgmtCompId);
				data.setStatus("PENDING");
				data.setUploadedBy(username);
				data.setMatrixFileRefNo(matrixfileRefno);

				pymt.put(matrixNo, data);

				// payments.add(data);

			}
		} else if (customerFlag && !checkFlag) {
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				logger.info("Inside else if block of excel upload");

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();
				PaymentData data = new PaymentData();

				int cellIdx = 0;

				while (cellsInRow.hasNext()) {

					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						if (currentCell.getCellType() == 1) {
							logger.info("Mat" + currentCell.getStringCellValue());
							matrixNo = (long) Long.parseLong(currentCell.getStringCellValue());
						} else {
							matrixNo = (long) currentCell.getNumericCellValue();
						}
						data.setMatrixRefNo(matrixNo);
						break;

					case 1:
						data.setSubProduct(currentCell.getStringCellValue());
						break;

					case 2:
						debitAccountNoDesc = currentCell.getStringCellValue();
						data.setDebitAccountNumberDesc(currentCell.getStringCellValue());
						break;

					case 3:
						supplierName = currentCell.getStringCellValue().split("-")[0];
						data.setBeneficiaryName(currentCell.getStringCellValue());
						break;

					case 4:
//						java.util.Date date = new SimpleDateFormat("dd/MM/yyyy")
////								.parse(currentCell.getStringCellValue());//original
						if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
							System.out.println("String:: " + currentCell.getStringCellValue());
							java.util.Date date = new SimpleDateFormat("dd/MM/yyyy")
									.parse(currentCell.getStringCellValue());// original
						} else {
							System.out.println("DATE::: " + currentCell.getDateCellValue());
							data.setInitiatorDates(currentCell.getDateCellValue());
						}

						break;

					case 5:
						data.setPaymentCurrency(currentCell.getStringCellValue());
						break;

					case 6:
						Double doublevalue = null;
						if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
							NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
							Number number = format.parse(currentCell.getStringCellValue());
							doublevalue = number.doubleValue();
						} else {
							doublevalue = currentCell.getNumericCellValue();
						}
						data.setInvoiceAmount(doublevalue);
						break;

					case 7:
						data.setCustomerReference(currentCell.getStringCellValue());
						break;

					case 8:
						data.setInitiatorNameDateTime(currentCell.getStringCellValue());
						break;

					case 9:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setServiceCode(currentCell.getStringCellValue());
							logger.info("Service Code::" + data.getServiceCode());
							break;
						}

					case 10:
						if (currentCell.getNumericCellValue() != 0) {
							data.setBudgetYear(currentCell.getNumericCellValue());
							logger.info("Budget Year::" + data.getBudgetYear());
							break;
						}
					case 11:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setBifurcation(currentCell.getStringCellValue());
							logger.info("Bifurcation::" + data.getBifurcation());
							break;
						}
					default:
						break;

					}

					cellIdx++;
				}

				logger.info(" Supplier Name from excel: " + supplierName);
				List<String> suppliers = paymentDataDao.getSupplierList();
				suppliers.replaceAll(String::toUpperCase);
				Integer supplierId = null;

				if (!suppliers.contains(supplierName.toUpperCase())) {
					supplierId = paymentDataDao.insertSupplier(supplierName);
					data.setSupplierId(supplierId);
				}

				else {
					supplierId = paymentDataDao.getSupplierId(supplierName);
					data.setSupplierId(supplierId);
				}

				String buildingName = debitAccountNoDesc.split("-|\\[")[1];
				logger.info(" Building Name from excel: " + buildingName);
				boolean checkBuidlingNameStatus = paymentDataDao.checkForBuildingName(buildingName);
				// List<String> buildingNames = paymentDataDao.getBuildingList();
				// buildingNames.replaceAll(String::toUpperCase);

				// logger.info("Building Names from db:: "+buildingNames);

				Integer buildingId = null;

				if (!checkBuidlingNameStatus) {
					buildingId = paymentDataDao.insertBuilding(buildingName);
					data.setBuildingId(buildingId);
				}

				else {
					buildingId = paymentDataDao.getBuildingId(buildingName);
					data.setBuildingId(buildingId);
				}

				String mcName = paymentDataDao.getManagementCompany(mgmtCompId);

				if (mcName.toUpperCase().contains("EDACOM") || mcName.toUpperCase().contains("NAKHEEL")) {

					data.setPaymentType("BULK");
				} else {
					data.setPaymentType("INDIVIDUAL");
				}

				if (!mcName.toUpperCase().contains("NAKHEEL")) {
					logger.info("Checking for prop id coz mcname is not nakheel");

					Integer propId = paymentDataDao.getPropId(buildingName, mgmtCompId);
					logger.info("Prop Id in service:: " + propId);

					if (propId != 0) {
						logger.info("Inside Insert prop id");

						boolean flag = paymentDataDao.checkForMapping(buildingId, propId);

						logger.info("Boolean flag in service:: " + flag);

						if (!flag) {
							paymentDataDao.insertMappingData(buildingId, propId, mgmtCompId); // raghuram
						}
					}
				}

				data.setDebitAccountNo(debitAccountNoDesc.split("-")[0]);
				data.setMgmtCompId(mgmtCompId);
				data.setStatus("PENDING");
				data.setUploadedBy(username);
				data.setMatrixFileRefNo(matrixfileRefno);

				pymt.put(matrixNo, data);

				// payments.add(data);

			}
		}

		else {
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				logger.info("Inside else block of excel upload");
				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();
				PaymentData data = new PaymentData();

				int cellIdx = 0;

				while (cellsInRow.hasNext()) {

					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						if (currentCell.getCellType() == 1) {
							logger.info("Mat" + currentCell.getStringCellValue());
							matrixNo = (long) Long.parseLong(currentCell.getStringCellValue());
						} else {
							matrixNo = (long) currentCell.getNumericCellValue();
						}
						data.setMatrixRefNo(matrixNo);
						break;

					case 1:
						data.setSubProduct(currentCell.getStringCellValue());
						break;

					case 2:
						debitAccountNoDesc = currentCell.getStringCellValue();
						data.setDebitAccountNumberDesc(currentCell.getStringCellValue());
						break;

					case 3:
						supplierName = currentCell.getStringCellValue().split("-")[0];
						data.setBeneficiaryName(currentCell.getStringCellValue());
						break;
					case 4:

						break;
					case 5:
						java.util.Date date = new SimpleDateFormat("dd/MM/yyyy")
								.parse(currentCell.getStringCellValue());
						data.setInitiatorDates(date);
						break;

					case 6:
						data.setPaymentCurrency(currentCell.getStringCellValue());
						break;

					case 7:
						NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
						Number number = format.parse(currentCell.getStringCellValue());
						Double doublevalue = number.doubleValue();
						data.setInvoiceAmount(doublevalue);
						break;

					case 8:
						data.setInitiatiorName(currentCell.getStringCellValue());
						break;

					case 9:

						data.setInitiatorNameDateTime(data.getInitiatiorName() + currentCell.getStringCellValue());
						logger.info("InitiatorNameDateTim::" + data.getInitiatorNameDateTime());
						break;

					case 10:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setServiceCode(currentCell.getStringCellValue());
							logger.info("Service Code::" + data.getServiceCode());
							break;
						}

					case 11:
						if (currentCell.getNumericCellValue() != 0) {
							data.setBudgetYear(currentCell.getNumericCellValue());
							logger.info("Budget Year::" + data.getBudgetYear());
							break;
						}

					case 12:
						if (currentCell.getStringCellValue() != null && currentCell.getStringCellValue() != "") {
							data.setBifurcation(currentCell.getStringCellValue());
							logger.info("Bifurcation::" + data.getBifurcation());
							break;
						}
					default:
						break;

					}

					cellIdx++;
				}

				logger.info(" Supplier Name from excel: " + supplierName);
				List<String> suppliers = paymentDataDao.getSupplierList();
				suppliers.replaceAll(String::toUpperCase);
				Integer supplierId = null;

				if (!suppliers.contains(supplierName.toUpperCase())) {
					supplierId = paymentDataDao.insertSupplier(supplierName);
					data.setSupplierId(supplierId);
				}

				else {
					supplierId = paymentDataDao.getSupplierId(supplierName);
					data.setSupplierId(supplierId);
				}

				String buildingName = debitAccountNoDesc.split("-|\\[")[1];
				logger.info(" Building Name from excel: " + buildingName);
				boolean checkBuidlingNameStatus = paymentDataDao.checkForBuildingName(buildingName);
				// List<String> buildingNames = paymentDataDao.getBuildingList();
				// buildingNames.replaceAll(String::toUpperCase);

				// logger.info("Building Names from db:: "+buildingNames);

				Integer buildingId = null;

				if (!checkBuidlingNameStatus) {
					buildingId = paymentDataDao.insertBuilding(buildingName);
					data.setBuildingId(buildingId);
				}

				else {
					buildingId = paymentDataDao.getBuildingId(buildingName);
					data.setBuildingId(buildingId);
				}

				String mcName = paymentDataDao.getManagementCompany(mgmtCompId);

				if (mcName.toUpperCase().contains("EDACOM") || mcName.toUpperCase().contains("NAKHEEL")) {

					data.setPaymentType("BULK");
				} else {
					data.setPaymentType("INDIVIDUAL");
				}

				if (!mcName.toUpperCase().contains("NAKHEEL")) {
					logger.info("Checking for prop id coz mcname is not nakheel");

					Integer propId = paymentDataDao.getPropId(buildingName, mgmtCompId);
					logger.info("Prop Id in service:: " + propId);

					if (propId != 0) {
						logger.info("Inside Insert prop id");

						boolean flag = paymentDataDao.checkForMapping(buildingId, propId);

						logger.info("Boolean flag in service:: " + flag);

						if (!flag) {
							paymentDataDao.insertMappingData(buildingId, propId, mgmtCompId); // raghuram
						}
					}
				}

				data.setDebitAccountNo(debitAccountNoDesc.split("-")[0]);
				data.setMgmtCompId(mgmtCompId);
				data.setStatus("PENDING");
				data.setUploadedBy(username);
				data.setMatrixFileRefNo(matrixfileRefno);

				pymt.put(matrixNo, data);

			}
		}

		logger.info(" Full Map is: " + pymt);

		for (Long mtrixRefNo : pymt.keySet()) {
			logger.info("Matrix no from keyset: " + mtrixRefNo);

			matrixList.add(mtrixRefNo);
		}

		List<Long> outputMatrixNos = paymentDataDao.getMatrixRefNos(matrixList);

		logger.info("Db Matrix Nos. are: " + outputMatrixNos);

		list = new ArrayList<Long>();

		for (PaymentData pymtData : pymt.values()) {
			logger.info("PYMT VALUES: " + pymtData.getMatrixRefNo());

			if (!outputMatrixNos.contains(pymtData.getMatrixRefNo())) {

				payments.add(pymtData);
			} else {
				list.add(pymtData.getMatrixRefNo());
			}
		}

		logger.info("PYMT DATA TO SEND TO DB" + payments);
		logger.info("PYMT DATA Not TO SEND TO DB" + list);

		paymentDataDao.uploadExcel(payments);

		workbook.close();
		return list;

	}

}
