
package com.nineplus.bestwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.TProjectType;

@Repository
public interface ProjectTypeRepository extends JpaRepository<TProjectType, Integer>{

}
