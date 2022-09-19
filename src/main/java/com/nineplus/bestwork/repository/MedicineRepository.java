package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.MedicineEntity;
import com.nineplus.bestwork.model.ReportInput;

@Repository
public interface MedicineRepository extends JpaRepository<MedicineEntity, Long>{

	@Query(name = "getReportData", nativeQuery = true)
	public List<ReportInput> getReportData(@Param("month") int month);
	
	@Query(value = "select m from MedicineEntity m where m.medicineName= :name")
	public MedicineEntity findByMedicineName(String name);
	
	@Query(value =  "select * from medicine as m where MONTH(m.import_date) = :month ", nativeQuery = true)
	public List<MedicineEntity> getImportData(@Param("month") int month);

	@Query(value = "select m from MedicineEntity m where m.medicineCode= :medicineCode and m.lotCode= :lotCode")
	public MedicineEntity getByMedicinCode(String medicineCode, String lotCode);
	
	@Query(value = "select m from MedicineEntity m where m.medicineCode= :medicineCode")
	public List<MedicineEntity> getByMedicinCodeLst(String medicineCode);

}
