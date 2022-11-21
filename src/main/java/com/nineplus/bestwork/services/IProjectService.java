package com.nineplus.bestwork.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.nineplus.bestwork.dto.AssignTaskReqDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.dto.ProjectResDto;
import com.nineplus.bestwork.dto.ProjectRoleUserResDto;
import com.nineplus.bestwork.dto.ProjectStatusReqDto;
import com.nineplus.bestwork.dto.ProjectTaskReqDto;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.entity.ProjectTypeEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.repository.ProjectAssignRepository;

public interface IProjectService {

	public PageResDto<ProjectResDto> getProjectPage(PageSearchDto pageSearchDto) throws BestWorkBussinessException;

	public PageResDto<ProjectResDto> getAllProjectPages(Pageable pageable) throws BestWorkBussinessException;

	public Optional<ProjectEntity> getProjectById(String id) throws BestWorkBussinessException;

	public void deleteProjectById(List<String> list) throws BestWorkBussinessException;

	public void saveProject(ProjectTaskReqDto projectTaskDto, ProjectTypeEntity type) throws BestWorkBussinessException;

	public void updateProject(ProjectTaskReqDto projectTaskDto, ProjectTypeEntity projectType, String projectId)
			throws BestWorkBussinessException;

	public List<ProjectAssignRepository> getCompanyUserForAssign(AssignTaskReqDto assignTaskReqDto)
			throws BestWorkBussinessException;

	public Map<Long, List<ProjectRoleUserResDto>> getListAssign(AssignTaskReqDto assignTaskReqDto)
			throws BestWorkBussinessException;

	boolean isExistedProjectId(String projectId);

	public ProjectResDto getDetailProject(String projectId) throws BestWorkBussinessException;

	public void changeStatus(String projectId, ProjectStatusReqDto projectStatusReqDto)
			throws BestWorkBussinessException;

	List<String> getAllProjectIdByCompany(List<Long> listCompanyId) throws BestWorkBussinessException;

	public List<ProjectEntity> getPrjCreatedByCurUser(String curUsername);

	public List<ProjectEntity> getPrAssignedToCurUser(String curUsername);

	public ProjectEntity getProjectByConstructionId(long constructionId);

	public List<ProjectEntity> getPrj4CompanyAdmin(String curUsername);

	public List<ProjectEntity> getPrj4SysAdmin(String curUsername);

}
