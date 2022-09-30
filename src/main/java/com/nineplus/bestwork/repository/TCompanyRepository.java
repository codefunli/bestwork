package com.nineplus.bestwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.TCompany;

@Repository
public interface TCompanyRepository extends JpaRepository<TCompany, Long> {
	
	//@Query(value = "SELECT * FROM T_COMPANY WHERE companyId = :companyId", nativeQuery = true)
    //TCompany findByCompanyId(Long companyId);

}
