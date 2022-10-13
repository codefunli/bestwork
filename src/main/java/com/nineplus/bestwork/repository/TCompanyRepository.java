package com.nineplus.bestwork.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.TCompany;
import com.nineplus.bestwork.entity.TProject;

@Repository
public interface TCompanyRepository extends JpaRepository<TCompany, Long> {
	
	@Query(value = "SELECT * FROM T_COMPANY WHERE id = :companyId", nativeQuery = true)
    TCompany findByCompanyId(Long companyId);

	@Query(value = "SELECT * FROM T_COMPANY WHERE company_name = :name", nativeQuery = true)
	TCompany findbyCompanyName(String name);

	@Query(value = "select * from T_COMPANY", nativeQuery = true)
    Page<TCompany> getPageCompany(Pageable pageable);

}
