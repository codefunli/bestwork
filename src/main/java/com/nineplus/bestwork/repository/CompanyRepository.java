package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.CompanyEntity;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

	@Query(value = "SELECT * FROM T_COMPANY WHERE id = :companyId", nativeQuery = true)
	CompanyEntity findByCompanyId(Long companyId);

	@Query(value = "SELECT * FROM T_COMPANY WHERE company_name = :name", nativeQuery = true)
	CompanyEntity findbyCompanyName(String name);

	@Query(value = "SELECT id as Id, company_name as companyName FROM T_COMPANY", nativeQuery = true)
	List<CompanyProjection> getAllCompany();

	@Query(value = "select * from T_COMPANY", nativeQuery = true)
	Page<CompanyEntity> getPageCompany(Pageable pageable);

	@Query(value = "select * from T_COMPANY WHERE is_expired = :status AND MATCH(company_name,email,province_city,district,ward,street) AGAINST(:keyword IN BOOLEAN MODE)", nativeQuery = true)
	Page<CompanyEntity> searchCompanyPage(String keyword, int status, Pageable pageable);

	@Query(value = "select * from T_COMPANY WHERE is_expired = :status", nativeQuery = true)
	Page<CompanyEntity> searchCompanyPageWithOutKeyWord(int status, Pageable pageable);

	@Query(value = "select * from T_COMPANY WHERE MATCH(company_name,email,province_city,district,ward,street) AGAINST(:keyword IN BOOLEAN MODE)", nativeQuery = true)
	Page<CompanyEntity> searchCompanyPageWithOutStatus(String keyword, Pageable pageable);

	@Modifying
	@Query(value = "DELETE from T_COMPANY c where c.id in ?1", nativeQuery = true)
	void deleteCompaniesWithIds(List<Long> ids);

	@Query(value = "SELECT * FROM T_COMPANY c JOIN T_COMPANY_USER tcu ON c.id = tcu.company_id JOIN T_SYS_APP_USER tsu ON tsu.id = tcu.user_id WHERE tsu.id = :userId ", nativeQuery = true)
	CompanyEntity getCompanyOfUser(long userId);

	@Query(value = " select DISTINCT c.* from T_COMPANY c " 
			+ " join T_COMPANY_USER cu on cu.company_id = c.id "
			+ " join T_SYS_APP_USER u on u.id = cu.user_id " 
			+ " join PROJECT p on p.create_by = u.user_name "
			+ " where p.id in :prjIds ", nativeQuery = true)
	List<CompanyEntity> findByCrtedPrjIds(List<String> prjIds);

}
