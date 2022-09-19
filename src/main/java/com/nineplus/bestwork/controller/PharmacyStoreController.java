package com.nineplus.bestwork.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.nineplus.bestwork.entity.MedicineEntity;
import com.nineplus.bestwork.entity.MedicineExportEntity;
import com.nineplus.bestwork.exception.ResourceNotFoundException;
import com.nineplus.bestwork.model.MedicineExportRequest;
import com.nineplus.bestwork.repository.MedicineExportRepository;
import com.nineplus.bestwork.repository.MedicineRepository;
import com.nineplus.bestwork.services.ExportReportService;
import com.nineplus.bestwork.services.MedicinePDFExporterService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin(origins = "http://119.82.130.211:8081")
@RequestMapping("/api/v1")
public class PharmacyStoreController {
	@Autowired
	private MedicineRepository medicineRepository;

	@Autowired
	private MedicineExportRepository medicineExportRepository;

	@Autowired
	private ExportReportService exportReportService;

	@GetMapping("/pharmacies")
	public List<MedicineEntity> getAllMedicine() {
		return medicineRepository.findAll();
	}

	@GetMapping("/pharmacies/{id}")
	public ResponseEntity<MedicineEntity> getEmployeeById(@PathVariable(value = "id") Long medicineId)
			throws ResourceNotFoundException {
		MedicineEntity medicine = medicineRepository.findById(medicineId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + medicineId));
		return ResponseEntity.ok().body(medicine);
	}

	/**
	 * Import medicine
	 * @param medicine
	 * @return
	 */
	@PostMapping("/pharmacies")
	public MedicineEntity createMedicine(@Valid @RequestBody MedicineEntity medicine) {
//		Medicine medicineStore = medicineRepository.getByMedicinCode(medicine.getMedicineCode());
//		if (medicine.getMedicineCode().equals(medicineStore.getMedicineCode())) {
//			long amount = medicine.getAmount() + medicineStore.getAmount();
//			medicine.setAmount(amount);
//			return medicineRepository.save(medicine);
//		}
		return medicineRepository.save(medicine);
	}

	@PutMapping("/pharmacies/{id}")
	public ResponseEntity<MedicineEntity> updateMedicine(@PathVariable(value = "id") Long medicineId,

			@Valid @RequestBody MedicineEntity medicineDetail) throws ResourceNotFoundException {
		MedicineEntity medicine = medicineRepository.findById(medicineId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + medicineId));

		medicine.setMedicineCode(medicineDetail.getMedicineCode());
		medicine.setMedicineName(medicineDetail.getMedicineName());
		medicine.setMedicineCompany(medicineDetail.getMedicineCompany());
		medicine.setLotCode(medicineDetail.getLotCode());
		medicine.setRegisterCode(medicineDetail.getRegisterCode());
		medicine.setCriteriaManufacture(medicineDetail.getCriteriaManufacture());
		medicine.setSpecPackage(medicineDetail.getSpecPackage());
		medicine.setCost(medicineDetail.getCost());
		medicine.setConcentration(medicineDetail.getConcentration());
		medicine.setUsageForm(medicineDetail.getUsageForm());		
		medicine.setCost(medicineDetail.getCost());
		medicine.setOrigin(medicineDetail.getOrigin());
		medicine.setManufactureDate(medicineDetail.getManufactureDate());
		medicine.setExpireDate(medicineDetail.getExpireDate());
		medicine.setAmount(medicineDetail.getAmount());
		medicine.setUnit(medicineDetail.getUnit());

		final MedicineEntity updatedMedicine = medicineRepository.save(medicine);
		return ResponseEntity.ok(updatedMedicine);
	}

	@DeleteMapping("/pharmacies/{id}")
	public Map<String, Boolean> deleteMedicine(@PathVariable(value = "id") Long medicineId)
			throws ResourceNotFoundException {
		MedicineEntity medicine = medicineRepository.findById(medicineId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + medicineId));

		medicineRepository.delete(medicine);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@GetMapping("/pharmacies/export")
	public List<MedicineExportEntity> getAllMedicineExport() {
		return medicineExportRepository.findAll();
	}

	@PostMapping("/pharmacies/export")
	public List<MedicineExportEntity> exportMidicineById(@Valid @RequestBody MedicineExportRequest medicineExportRequest)
			throws ResourceNotFoundException {
		long amount = medicineRepository.getByMedicinCode(medicineExportRequest.getMedicineCode(), medicineExportRequest.getLotCode()).getAmount();
		long diff = amount - medicineExportRequest.getAmount();
		// Save into Medicine Export DB
		String medicineName = medicineRepository.getByMedicinCode(medicineExportRequest.getMedicineCode(), medicineExportRequest.getLotCode()).getMedicineName();
		String medicineCompany = medicineRepository.getByMedicinCode(medicineExportRequest.getMedicineCode(), medicineExportRequest.getLotCode()).getMedicineCompany();
		
		MedicineExportEntity medicineExport = new MedicineExportEntity();
		medicineExport.setAmount(medicineExportRequest.getAmount());
		medicineExport.setMedicineName(medicineName);
		medicineExport.setMedicineCode(medicineExportRequest.getMedicineCode());
		medicineExport.setLotCode(medicineExportRequest.getLotCode());
		medicineExport.setMedicineCompany(medicineCompany);
		medicineExport.setExportDate(medicineExportRequest.getExportDate());
		medicineExportRepository.save(medicineExport);

		// Update medicine DB
		MedicineEntity medicine = medicineRepository.getByMedicinCode(medicineExportRequest.getMedicineCode(), medicineExportRequest.getLotCode());
		if (diff > 0) {
			medicine.setAmount(diff);
			medicineRepository.save(medicine);
		} else {
			medicineRepository.delete(medicine);
		}
		return medicineExportRepository.findAll();
	}
	
	@GetMapping("pharmacies/report")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
         
        List<MedicineEntity> listMedicine = medicineRepository.findAll();
         
        MedicinePDFExporterService exporter = new MedicinePDFExporterService(listMedicine);
        exporter.export(response);
         
    }
	
	@GetMapping("/pharmacies/download/{month}")
    public ResponseEntity<Resource> download(@PathVariable(value = "month") String month) throws IOException, ResourceNotFoundException {
    	File file = null;
    	int monthRes = Integer.parseInt(month);
			try {
				file = exportReportService.export(monthRes);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		if (ObjectUtils.isEmpty(file)) {
			throw new ResourceNotFoundException("Data not found");
		}
		String fileName = file.getName();
		String fileNm = fileName.replace(fileName.substring(fileName.lastIndexOf("_"), fileName.length()), "_thang_" + month);
        HttpHeaders header = new HttpHeaders();
        header.add("File-Name", fileNm);
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName );
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Response-Header","content-diposition");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource;
        try {
			 resource = new ByteArrayResource(Files.readAllBytes(path));
		} catch (IOException e) {
			throw new ResourceNotFoundException("Error!");
		}
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(resource);      
    }
	
	
	@GetMapping("/pharmacies/download/import/{month}")
    public ResponseEntity<Resource> downloadImport(@PathVariable(value = "month") String month) throws IOException, ResourceNotFoundException {
    	File file = null;
    	int monthRes = Integer.parseInt(month);
			try {
				file = exportReportService.reportImport(monthRes);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		if (ObjectUtils.isEmpty(file)) {
			throw new ResourceNotFoundException("Data not found");
		}
		String fileName = file.getName();
		String fileNm = fileName.replace(fileName.substring(fileName.lastIndexOf("_"), fileName.length()), "_thang_" + month);
        HttpHeaders header = new HttpHeaders();
        header.add("File-Name", fileNm);
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName );
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Response-Header","content-diposition");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource;
        try {
			 resource = new ByteArrayResource(Files.readAllBytes(path));
		} catch (IOException e) {
			throw new ResourceNotFoundException("Error!");
		}
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(resource);      
    }
	
	@GetMapping("/pharmacies/download/export/{month}")
    public ResponseEntity<Resource> downloadExport(@PathVariable(value = "month") String month) throws IOException, ResourceNotFoundException {
    	File file = null;
    	int monthRes = Integer.parseInt(month);
			try {
				file = exportReportService.reportExport(monthRes);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		if (ObjectUtils.isEmpty(file)) {
			throw new ResourceNotFoundException("Data not found");
		}
		String fileName = file.getName();
		String fileNm = fileName.replace(fileName.substring(fileName.lastIndexOf("_"), fileName.length()), "_thang_" + month);
        HttpHeaders header = new HttpHeaders();
        header.add("File-Name", fileNm);
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName );
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Response-Header","content-diposition");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource;
        try {
			 resource = new ByteArrayResource(Files.readAllBytes(path));
		} catch (IOException e) {
			throw new ResourceNotFoundException("Error!");
		}
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(resource);      
    }

}
