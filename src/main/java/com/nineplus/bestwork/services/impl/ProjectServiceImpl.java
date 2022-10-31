package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.AssignTaskReqDto;
import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.dto.ProjectAssignReqDto;
import com.nineplus.bestwork.dto.ProjectReqDto;
import com.nineplus.bestwork.dto.ProjectResponseDto;
import com.nineplus.bestwork.dto.ProjectRoleUserReqDto;
import com.nineplus.bestwork.dto.ProjectRoleUserResDto;
import com.nineplus.bestwork.dto.ProjectTaskReqDto;
import com.nineplus.bestwork.entity.AssignTask;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.entity.ProjectTypeEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.repository.AssignTaskRepository;
import com.nineplus.bestwork.repository.ProjectAssignRepository;
import com.nineplus.bestwork.repository.ProjectRepository;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.ConvertResponseUtils;
import com.nineplus.bestwork.utils.DateUtils;
import com.nineplus.bestwork.utils.Enums.ProjectStatus;
import com.nineplus.bestwork.utils.MessageUtils;
import com.nineplus.bestwork.utils.PageUtils;

@Service
@Transactional
public class ProjectServiceImpl implements IProjectService {

	private final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private PageUtils responseUtils;

	@Autowired
	private MessageUtils messageUtils;

	@Autowired
	private ConvertResponseUtils convertResponseUtils;

	@Autowired
	AssignTaskRepository assignTaskRepository;

	@Autowired
	DateUtils dateUtils;

	@Override
	public PageResponseDto<ProjectResponseDto> getProjectPage(PageSearchDto pageSearchDto)
			throws BestWorkBussinessException {
		try {
			int pageNumber = NumberUtils.toInt(pageSearchDto.getPage());
			String mappedColumn = convertResponseUtils.convertResponseProject(pageSearchDto.getSortBy());
			Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(pageSearchDto.getSize()),
					Sort.by(pageSearchDto.getSortDirection(), mappedColumn));
			Page<ProjectEntity> pageTProject;
			int status = pageSearchDto.getStatus();
			if (status >= 0 && status < ProjectStatus.values().length) {
				pageTProject = projectRepository.findProjectWithStatus(pageSearchDto, pageable);
			} else {
				pageTProject = projectRepository.findProjectWithoutStatus(pageSearchDto, pageable);
			}
			return responseUtils.convertPageEntityToDTO(pageTProject, ProjectResponseDto.class);
		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0003, null), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}

	}

	@Override
	public PageResponseDto<ProjectResponseDto> getAllProjectPages(Pageable pageable) throws BestWorkBussinessException {
		try {
			Page<ProjectEntity> pageTProject = projectRepository.findAll(pageable);
			return responseUtils.convertPageEntityToDTO(pageTProject, ProjectResponseDto.class);

		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0003, null), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

	@Override
	public Optional<ProjectEntity> getProjectById(String id) throws BestWorkBussinessException {
		if (id == null || id.equalsIgnoreCase("")) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
		return this.projectRepository.findById(id);
	}

	private String getLastProjectId() {
		return this.projectRepository.getLastProjectIdString();
	}

	private String setProjectId() {
		String id = this.getLastProjectId();
		String prefixId = "PRJ";
		if (id == null || id == "") {
			id = "PRJ0001";
		} else {
			Integer suffix = Integer.parseInt(id.substring(prefixId.length())) + 1;
			if (suffix < 10)
				id = prefixId + "000" + suffix;
			else if (suffix < 100)
				id = prefixId + "00" + suffix;
			else if (suffix < 1000)
				id = prefixId + "0" + suffix;
			else
				id = prefixId + suffix;
		}
		return id;
	}

	@Override
	public ProjectEntity updateProject(ProjectEntity project) throws BestWorkBussinessException {
		return this.projectRepository.save(project);

	}

	@Override
	public void deleteProjectById(List<String> id) throws BestWorkBussinessException {
		this.projectRepository.deleteProjectById(id);
	}

	@Override
	public void saveProject(ProjectTaskReqDto projectTaskDto, ProjectTypeEntity projectType)
			throws BestWorkBussinessException {
		// Generate project ID
		String generateProjectId = "";
		if (projectTaskDto.getProject() != null && projectTaskDto.getRoleData() == null) {
			generateProjectId = this.setProjectId();
			// Validate project information
			this.validateProject(projectTaskDto.getProject(), false);
			registNewProject(projectTaskDto.getProject(), projectType, generateProjectId);
		} else if (projectTaskDto.getRoleData() != null) {
			generateProjectId = this.setProjectId();
			// Validate project information
			this.validateProject(projectTaskDto.getProject(), false);
			registNewProject(projectTaskDto.getProject(), projectType, generateProjectId);
			for (int i = 0; i < projectTaskDto.getRoleData().size(); i++)
				registAssign(projectTaskDto.getRoleData().get(i), projectType, generateProjectId);

		}
	}

	@Transactional(rollbackFor = { Exception.class })
	public void registNewProject(ProjectReqDto projectReqDto, ProjectTypeEntity projectType, String generateProjectId)
			throws BestWorkBussinessException {
		ProjectEntity projectRegist = new ProjectEntity();
		try {

			projectRegist.setId(generateProjectId);
			projectRegist.setProjectName(projectReqDto.getProjectName());
			projectRegist.setDescription(projectReqDto.getDescription());
			projectRegist.setNotificationFlag(projectReqDto.getNotificationFlag());
			projectRegist.setIsPaid(projectReqDto.getIsPaid());
			projectRegist.setStatus(projectReqDto.getStatus());

			// Convert date UTC to String
			String registerDate = dateUtils.convertToUTC(projectReqDto.getCreateDate());
			projectRegist.setCreateDate(registerDate);
			projectRegist.setProjectType(projectType);
			projectRepository.save(projectRegist);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.S1X0005, null);
		}
	}

	@Transactional(rollbackFor = { Exception.class })
	public void registAssign(ProjectAssignReqDto projectAssignReqDto, ProjectTypeEntity projectType,
			String generateProjectId) throws BestWorkBussinessException {
		try {
			List<ProjectRoleUserReqDto> userList = projectAssignReqDto.getUserList();
			List<AssignTask> assignTasklist = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				AssignTask assignTask = new AssignTask();
				assignTask.setCompanyId(projectAssignReqDto.getCompanyId());
				assignTask.setProjectId(generateProjectId);
				assignTask.setUserId(userList.get(i).getUserId());
				assignTask.setCanView(userList.get(i).isCanView());
				assignTask.setCanEdit(userList.get(i).isCanEdit());
				assignTasklist.add(assignTask);
			}
			assignTaskRepository.saveAll(assignTasklist);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.S1X0005, null);
		}
	}

	public void validateProject(ProjectReqDto projectReqDto, boolean isEdit) throws BestWorkBussinessException {
		// Validation register information
		String projectName = projectReqDto.getProjectName();

		// Project name can not be empty
		if (ObjectUtils.isEmpty(projectName)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.S1X0014,
					new Object[] { CommonConstants.Character.PROJECT });
		}
		// Check exists Project name in database
		if (!isEdit) {
			ProjectEntity project = projectRepository.findbyProjectName(projectName);
			if (!ObjectUtils.isEmpty(project)) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.S1X0013, new Object[] { project });
			}
		}

	}

	@Override
	public void updateProject(ProjectTaskReqDto projectTaskDto, ProjectTypeEntity projectType, String projectId)
			throws BestWorkBussinessException {
		ProjectEntity currentProject = null;

		for (int j = 0; j < projectTaskDto.getRoleData().size(); j++) {
			Long companyId = projectTaskDto.getRoleData().get(j).getCompanyId();
			currentProject = projectRepository.findbyProjectId(projectId);
			List<ProjectRoleUserReqDto> userList = projectTaskDto.getRoleData().get(j).getUserList();
			AssignTask assignTask = null;
			try {
				// Save for project
				currentProject.setProjectName(projectTaskDto.getProject().getProjectName());
				currentProject.setDescription(projectTaskDto.getProject().getDescription());
				currentProject.setNotificationFlag(projectTaskDto.getProject().getNotificationFlag());
				currentProject.setIsPaid(projectTaskDto.getProject().getIsPaid());
				currentProject.setStatus(projectTaskDto.getProject().getStatus());
				currentProject.setCreateDate(projectTaskDto.getProject().getCreateDate());
				currentProject.setUpdateDate(LocalDateTime.now());
				currentProject.setProjectType(projectType);
				projectRepository.save(currentProject);

				for (int i = 0; i < userList.size(); i++) {
					assignTask = assignTaskRepository.findbyCondition(userList.get(i).getUserId(), companyId,
							projectId);
					if (assignTask != null) {
						assignTask.setCanView(userList.get(i).isCanView());
						assignTask.setCanEdit(userList.get(i).isCanEdit());
						assignTaskRepository.save(assignTask);
					} else {
						AssignTask assignTaskNew = new AssignTask();
						assignTaskNew.setCompanyId(companyId);
						assignTaskNew.setProjectId(projectId);
						assignTaskNew.setUserId(userList.get(i).getUserId());
						assignTaskNew.setCanView(userList.get(i).isCanView());
						assignTaskNew.setCanEdit(userList.get(i).isCanEdit());
						assignTaskRepository.save(assignTaskNew);
					}

				}

			} catch (Exception ex) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0004, new Object[] {
						CommonConstants.Character.PROJECT, (projectTaskDto.getProject().getProjectName()) });
			}
		}
	}

	@Override
	public List<ProjectAssignRepository> getCompanyUserForAssign(AssignTaskReqDto assignTaskReqDto)
			throws BestWorkBussinessException {
		List<ProjectAssignRepository> lstResult = null;
		if (StringUtils.isNotBlank(assignTaskReqDto.getCompanyId())) {
			long companyId = Long.parseLong(assignTaskReqDto.getCompanyId());
			lstResult = projectRepository.GetCompanyAndRoleUserByCompanyId(companyId);
		}
		return lstResult;
	}

	public boolean isExistedProjectId(String projectId) {
		Optional<ProjectEntity> project = null;
		project = projectRepository.findById(projectId);
		if (project.isPresent()) {
			return true;
		}
		return false;
	}

	@Override
	public ProjectResponseDto getDetailProject(String projectId) throws BestWorkBussinessException {
		ProjectEntity project = projectRepository.findbyProjectId(projectId);
		ProjectResponseDto projectDto = null;
		if (project != null) {
			projectDto = new ProjectResponseDto();
			projectDto.setId(project.getId());
			projectDto.setProjectName(project.getProjectName());
			projectDto.setDescription(project.getDescription());
			projectDto.setNotificationFlag(project.getNotificationFlag());
			projectDto.setIsPaid(project.getIsPaid());
			projectDto.setProjectType(project.getProjectType());
			projectDto.setStatus(project.getStatus());
			projectDto.setCreateDate(project.getCreateDate());
			projectDto.setUpdateDate(project.getUpdateDate());
		}
		return projectDto;

	}

	@Override
	public Map<Long, List<ProjectRoleUserResDto>> getListAssign(AssignTaskReqDto assignTaskReqDto)
			throws BestWorkBussinessException {
		List<ProjectAssignRepository> listRole = null;
		if (StringUtils.isNotBlank(assignTaskReqDto.getProjectId())) {
			listRole = projectRepository.GetCompanyAndRoleUserByProject(assignTaskReqDto.getProjectId());
		}
		
		Map<Long, List<ProjectRoleUserResDto>> resultList =
				listRole
				.stream()
				.map(listR -> new ProjectRoleUserResDto(listR.getCompanyId(), listR.getUserId(),listR.getUserName(),listR.getCanView(), listR.getCanEdit()))
				.collect(Collectors.groupingBy(ProjectRoleUserResDto::getCompanyId, Collectors.toList()));

		return resultList;
	}

}
