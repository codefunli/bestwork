package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.entity.MedicineEntity;
import com.nineplus.bestwork.model.ReportInput;
import com.nineplus.bestwork.repository.MedicineRepository;

@Service
public class MedicineService {
	
	@Autowired
	MedicineRepository medicineRepository;
	
	public List<ReportInput> getReportData(int month){
		List<ReportInput> data = medicineRepository.getReportData(month);
		return data;	
	}
	
	public List<MedicineEntity> getImportData(int month){
		List<MedicineEntity> data = medicineRepository.getImportData(month);
		return data;	
	}
}
