package com.nineplus.bestwork.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.TProject;

@Repository
public interface ProjectRepository extends JpaRepository<TProject, Long> {
	
//	@Query(value = "select prj from ProjectEntity prj where prj.prj_nm like %:prjName%")
//    Page<TProject> findProjectWithCondition2(String prjName, Pageable pageable);

	@Query(value = "select * from T_PROJECT where prj_nm = :prjName", nativeQuery = true)
    Page<TProject> findProjectWithCondition(String prjName, Pageable pageable);

}
