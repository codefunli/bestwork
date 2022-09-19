package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.model.ExportData;
import com.nineplus.bestwork.repository.MedicineExportRepository;

@Service
public class MedicineExportService {
	@Autowired
	private MedicineExportRepository medicineExportRepository;
	
	public List<ExportData> getExportData(int month){
		List<ExportData> data = medicineExportRepository.getExportData(month);
		return data;
	}
}
