package com.nineplus.bestwork.services;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gembox.spreadsheet.CellValueType;
import com.gembox.spreadsheet.ExcelFile;
import com.gembox.spreadsheet.ExcelRow;
import com.gembox.spreadsheet.ExcelWorksheet;
import com.gembox.spreadsheet.SpreadsheetInfo;
import com.nineplus.bestwork.entity.MedicineEntity;
import com.nineplus.bestwork.entity.MedicineExportEntity;
import com.nineplus.bestwork.exception.ResourceNotFoundException;
import com.nineplus.bestwork.model.ExportData;
import com.nineplus.bestwork.model.ReportInput;
import com.nineplus.bestwork.repository.MedicineExportRepository;
import com.nineplus.bestwork.repository.MedicineRepository;

@Service
public class ExportReportService {
	
	@Autowired
	MedicineService medicineSerivice;
	
	@Autowired
	MedicineExportService medicineExportService;
	
	@Autowired
	MedicineRepository medicineRepository;
	
	@Autowired
	MedicineExportRepository medicineExportRepository;
	
	private static final String EXTENSION = ".xlsx";	
	private static final String PATH_MONTHLY_REPORT = "src/main/resources/monthly_report.xlsx";	
	private static final String PATH_IMPORT_REPORT = "src/main/resources/import.xlsx";	
	private static final String PATH_EXPORT_REPORT = "src/main/resources/export.xlsx";
	
	private static final String PATTERN = "dd-MM-yyyy";
	SimpleDateFormat formatter = new SimpleDateFormat(PATTERN);
	
	public File export(int month) throws ResourceNotFoundException, IOException {
		SpreadsheetInfo.setLicense("FREE-LIMITED-KEY");
		ExcelFile workbook;

		List<ReportInput> data = medicineSerivice.getReportData(month);

		try {
			workbook = ExcelFile.load(PATH_MONTHLY_REPORT);
		} catch (IOException e) {
			throw new ResourceNotFoundException("File not found");
		}

		ExcelWorksheet worksheet = workbook.getWorksheet(0);
		// Find cells and set their FORMULA
//		RowColumn rowColumnPosition;
		worksheet.getCell("G58").setValue("=SUM(G11:G56)");
		worksheet.getCell("H58").setValue("=SUM(H11:H56)");
		worksheet.getCell("I58").setValue("=SUM(I11:I56)");
		worksheet.getCell("J58").setValue("=SUM(J11:J56)");
		worksheet.getCell("K58").setValue("=SUM(K11:K56)");
		worksheet.getCell("L58").setValue("=SUM(L11:L56)");
		worksheet.getCell("M58").setValue("=SUM(M11:M56)");
		worksheet.getCell("N58").setValue("=SUM(N11:N56)");

		worksheet.getCell("G59").setValue("=SUM(G58:G57)");
		worksheet.getCell("H59").setValue("=SUM(H58:H57)");
		worksheet.getCell("I59").setValue("=SUM(I58:I57)");
		worksheet.getCell("J59").setValue("=SUM(J58:J57)");
		worksheet.getCell("K59").setValue("=SUM(K58:K57)");
		worksheet.getCell("L59").setValue("=SUM(L58:L57)");
		worksheet.getCell("M59").setValue("=SUM(M58:M57)");
		worksheet.getCell("N59").setValue("=SUM(N58:N57)");
		worksheet.getCell("D60").setValue("N59");

		int rowIndex = 57;
		while (worksheet.getCell(rowIndex, 0).getValueType() != CellValueType.NULL)
			worksheet.getCell(rowIndex, 1).setFormula(worksheet.getCell(rowIndex++, 0).getStringValue());
		// Copy template row.
		int row = 11;
		worksheet.getRows().insertCopy(row, 11, worksheet.getRow(row));

		// Fill inserted rows with sample data.
		for (int i = 10; i < data.size() + 10; i++) {
			ExcelRow currentRow = worksheet.getRow(i);
			for (int j = i - 10; j < data.size();) {
				List<MedicineEntity> medicineLst = medicineRepository.getByMedicinCodeLst(data.get(j).getMedicineCode());
				List<MedicineExportEntity> medicineExportLst = medicineExportRepository.getByMedicinCodeLst(data.get(j).getMedicineCode());
				currentRow.getCell(0).setValue(j + 1);
				currentRow.getCell(1).setValue(medicineLst.get(0).getMedicineCode());
				currentRow.getCell(2).setValue(data.get(j).getMedicineName());
				currentRow.getCell(3).setValue(medicineLst.get(0).getUnit());
				currentRow.getCell(4).setValue(formatter.format(medicineLst.get(0).getExpireDate()));
				currentRow.getCell(5).setValue(formatCurrency(medicineLst.get(0).getCost()));

				long amountStartMount = 0;
				long amountEndMount = 0;
				long totalImport = 0;
				long totalExport = 0;
				for (MedicineExportEntity medEx : medicineExportLst) {
					LocalDate localdateEx = medEx.getExportDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					if(localdateEx.getMonthValue() == month) {
						totalExport+=medEx.getAmount();
					}
				}
				for (MedicineEntity med : medicineLst) {
					LocalDate localdate = med.getImportDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					if (localdate.getMonthValue() == month) {
						totalImport+= med.getAmount();
					} else if(localdate.getMonthValue() < month) {
						amountStartMount+=med.getAmount();
					}
				}
				
				totalImport = totalImport + totalExport;
				
				amountEndMount = (amountStartMount + totalImport) - totalExport;
				// Đầu kỳ
				currentRow.getCell(6).setValue(amountStartMount);
				currentRow.getCell(7).setValue(calculateVal(amountStartMount, medicineLst.get(0).getCost()));
				// Nhập kho
				currentRow.getCell(8).setValue(totalImport);
				currentRow.getCell(9).setValue(calculateVal(totalImport, medicineLst.get(0).getCost()));
				// Xuất kho
				currentRow.getCell(10).setValue(totalExport);
				currentRow.getCell(11).setValue(calculateVal(totalExport, medicineLst.get(0).getCost()));
				// Cuối kỳ
				currentRow.getCell(12).setValue(amountEndMount);
				currentRow.getCell(13).setValue(calculateVal(amountEndMount, medicineLst.get(0).getCost()));
				currentRow.getCell(14).setValue("N/A");
				break;
			}
		}
		String fileName = "Report_" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + EXTENSION;
		workbook.save(fileName);
		File file = new File(fileName);
		return file;
	}
	
	public File reportImport(int month) throws ResourceNotFoundException, IOException {
		SpreadsheetInfo.setLicense("FREE-LIMITED-KEY");
		ExcelFile workbook;
		
		List<MedicineEntity> data = medicineSerivice.getImportData(month);
		
		try {
			workbook = ExcelFile.load(PATH_IMPORT_REPORT);
		} catch (IOException e) {
			throw new ResourceNotFoundException("File not found");
		}
        
        ExcelWorksheet worksheet = workbook.getWorksheet(0);
        // Find cells and set their FORMULA
        worksheet.getCell("N23").setValue("=SUM(N14:N22)");
        worksheet.getCell("P23").setValue("=SUM(P14:P22)");
        worksheet.getCell("R23").setValue("=SUM(R14:R22)");

        int rowIndex = 19;
        while (worksheet.getCell(rowIndex, 0).getValueType() != CellValueType.NULL)
        	worksheet.getCell(rowIndex, 1).setFormula(worksheet.getCell(rowIndex++, 0).getStringValue());
        // Copy template row.
        int row = 14;
        worksheet.getRows().insertCopy(row , data.size(), worksheet.getRow(row));
        
        // Fill inserted rows with sample data.
        	for (int i = row - 1; i < data.size() + (row -1); i++) {
                ExcelRow currentRow = worksheet.getRow( i);
                for(int j = i- (row -1); j < data.size();) {
                	
                	currentRow.getCell(0).setValue(j + 1);
                    currentRow.getCell(1).setValue(data.get(j).getMedicineName());
                    currentRow.getCell(2).setValue(data.get(j).getMedicineCode());
                    currentRow.getCell(3).setValue(data.get(j).getRegisterCode());
                    currentRow.getCell(4).setValue(data.get(j).getLotCode());
                    currentRow.getCell(5).setValue(formatter.format(data.get(j).getManufactureDate()));
                    currentRow.getCell(6).setValue(data.get(j).getMedicineCompany() + " - " + data.get(j).getOrigin());
                    currentRow.getCell(7).setValue(data.get(j).getCriteriaManufacture());
                    currentRow.getCell(8).setValue(data.get(j).getSpecPackage());
                    currentRow.getCell(9).setValue(data.get(j).getConcentration());
                    currentRow.getCell(10).setValue(data.get(j).getUsageForm());
                    currentRow.getCell(11).setValue(formatter.format(data.get(j).getExpireDate()));
                    currentRow.getCell(12).setValue(data.get(j).getUnit());
                    currentRow.getCell(13).setValue(data.get(j).getAmount());
                    currentRow.getCell(14).setValue(data.get(j).getAmount());
                    currentRow.getCell(15).setValue(data.get(j).getCost());
                    currentRow.getCell(16).setValue(data.get(j).getCost());
                    currentRow.getCell(17).setValue(calculateVal(data.get(j).getAmount(), data.get(j).getCost()));
                    break;
                }   
            }
        String fileName = "Report_Import_"+ LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + EXTENSION;
		workbook.save(fileName);
		File file = new File(fileName);
		return file;
	}
	
	public File reportExport(int month) throws ResourceNotFoundException, IOException {
		SpreadsheetInfo.setLicense("FREE-LIMITED-KEY");
		ExcelFile workbook;
		
		List<ExportData> data = medicineExportService.getExportData(month);
		
		try {
			workbook = ExcelFile.load(PATH_EXPORT_REPORT);
		} catch (IOException e) {
			throw new ResourceNotFoundException("File not found");
		}
        
        ExcelWorksheet worksheet = workbook.getWorksheet(0);
        // Find cells and set their FORMULA
        worksheet.getCell("O22").setValue("=SUM(O13:O21)");
        worksheet.getCell("Q22").setValue("=SUM(Q13:Q21)");
        worksheet.getCell("S22").setValue("=SUM(S13:S21)");

        int rowIndex = 21;
        while (worksheet.getCell(rowIndex, 0).getValueType() != CellValueType.NULL)
        	worksheet.getCell(rowIndex, 1).setFormula(worksheet.getCell(rowIndex++, 0).getStringValue());
        
        // Copy template row.
        int row = 14;
        worksheet.getRows().insertCopy(row , 1, worksheet.getRow(row));
        
        // Fill inserted rows with sample data.
        	for (int i = row - 1; i < data.size() + (row -1); i++) {
                ExcelRow currentRow = worksheet.getRow( i);
                for(int j = i- (row -1); j < data.size();) {
                	
                	currentRow.getCell(0).setValue(j + 1);
                    currentRow.getCell(1).setValue(data.get(j).getMedicineName());
                    currentRow.getCell(2).setValue(data.get(j).getMedicineCode());
                    currentRow.getCell(3).setValue(data.get(j).getRegisterCode());
                    currentRow.getCell(4).setValue(data.get(j).getLotCode());
                    currentRow.getCell(5).setValue(formatter.format(data.get(j).getManufactureDate()));
                    currentRow.getCell(6).setValue(data.get(j).getMedicineCompany() + " - " + data.get(j).getOrigin());
                    currentRow.getCell(7).setValue(data.get(j).getCriteriaManufacture());
                    currentRow.getCell(8).setValue(data.get(j).getSpecPackage());
                    currentRow.getCell(9).setValue("Cong ty cung ung");
                    currentRow.getCell(10).setValue(data.get(j).getConcentration());
                    currentRow.getCell(11).setValue(data.get(j).getUsageForm());
                    currentRow.getCell(12).setValue(formatter.format(data.get(j).getExpireDate()));
                    currentRow.getCell(13).setValue(data.get(j).getUnit());
                    currentRow.getCell(14).setValue(data.get(j).getAmount());
                    currentRow.getCell(15).setValue(data.get(j).getAmount());
                    currentRow.getCell(16).setValue(data.get(j).getCost());
                    currentRow.getCell(17).setValue(data.get(j).getCost());
                    currentRow.getCell(18).setValue(calculateVal(data.get(j).getAmount(), data.get(j).getCost()));
                    break;
                }   
            }
        String fileName = "Report_Export_"+ LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + EXTENSION;
		workbook.save(fileName);
		File file = new File(fileName);
		return file;
	}
	
    public static String formatCurrency(BigDecimal value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(5);
        format.setRoundingMode(RoundingMode.HALF_EVEN);
        return format.format(value);
    }
    
    public static BigDecimal calculateVal(Long ammount, BigDecimal cost) {
    	BigDecimal result = cost.multiply(new BigDecimal(ammount));
		return result;
    }
}
