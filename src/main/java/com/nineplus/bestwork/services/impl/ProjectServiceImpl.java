package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.AssignTaskReqDto;
import com.nineplus.bestwork.dto.NotificationReqDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.dto.ProjectAssignReqDto;
import com.nineplus.bestwork.dto.ProjectReqDto;
import com.nineplus.bestwork.dto.ProjectResDto;
import com.nineplus.bestwork.dto.ProjectRoleUserReqDto;
import com.nineplus.bestwork.dto.ProjectRoleUserResDto;
import com.nineplus.bestwork.dto.ProjectStatusReqDto;
import com.nineplus.bestwork.dto.ProjectTaskReqDto;
import com.nineplus.bestwork.entity.AssignTaskEntity;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.entity.ProjectTypeEntity;
import com.nineplus.bestwork.entity.UserEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.AssignTaskRepository;
import com.nineplus.bestwork.repository.ProjectAssignRepository;
import com.nineplus.bestwork.repository.ProjectRepository;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.services.NotificationService;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.ConvertResponseUtils;
import com.nineplus.bestwork.utils.DateUtils;
import com.nineplus.bestwork.utils.Enums.ProjectStatus;
import com.nineplus.bestwork.utils.PageUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class ProjectServiceImpl implements IProjectService {

	private final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private PageUtils responseUtils;

	@Autowired
	private ConvertResponseUtils convertResponseUtils;

	@Autowired
	AssignTaskRepository assignTaskRepository;

	@Autowired
	DateUtils dateUtils;

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	NotificationService notifyService;

	@Autowired
	UserService userService;

	@Override
	public PageResDto<ProjectResDto> getProjectPage(PageSearchDto pageSearchDto) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = getAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		try {
			Pageable pageable = convertSearch(pageSearchDto);
			Page<ProjectEntity> prjPage = null;
			if (!userAuthRoleReq.getIsSysAdmin() && !userAuthRoleReq.getIsOrgAdmin()) {
				prjPage = this.projectRepository.getPrjInvolvedByCurUser(curUsername, pageSearchDto, pageable);
			} else if (userAuthRoleReq.getIsOrgAdmin()) {
				// Projects of company admin user
				prjPage = this.projectRepository.getPrjPageByOrgAdmin(curUsername, pageSearchDto, pageable);
			} else if (userAuthRoleReq.getIsSysAdmin()) {
				// Projects of supper admin user (contain projects of company admin user)
				prjPage = this.projectRepository.getPrjPageBySysAdmin(curUsername, pageSearchDto, pageable);

			}

			return responseUtils.convertPageEntityToDTO(prjPage, ProjectResDto.class);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

	private UserAuthDetected getAuthRoleReq() throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		return userAuthRoleReq;
	}

	private Pageable convertSearch(PageSearchDto pageSearchDto) {
		if (pageSearchDto.getKeyword().equals("")) {
			pageSearchDto.setKeyword("%%");
		} else {
			pageSearchDto.setKeyword("%" + pageSearchDto.getKeyword() + "%");
		}
		if (pageSearchDto.getStatus() < 0 || pageSearchDto.getStatus() >= ProjectStatus.values().length) {
			pageSearchDto.setStatus(-1);
		}
		String mappedColumn = convertResponseUtils.convertResponseProject(pageSearchDto.getSortBy());
		return PageRequest.of(Integer.parseInt(pageSearchDto.getPage()), Integer.parseInt(pageSearchDto.getSize()),
				Sort.by(pageSearchDto.getSortDirection(), mappedColumn));
	}

	/**
	 * Private function: get all projects that current user is being involved
	 * (creating or/and being assigned)
	 * 
	 * @param curUsername
	 * @return List<ProjectEntity>
	 */
	private List<ProjectEntity> getPrjInvolvedByCurUser(String curUsername) {
		// Get projects that created by current user

		List<ProjectEntity> creatingProjectList = getPrjCreatedByCurUser(curUsername);
		// Get projects that assigned to current user
		List<ProjectEntity> assignedProjectList = getPrAssignedToCurUser(curUsername);

		Set<ProjectEntity> projectSet = new HashSet<>();
		if (creatingProjectList != null)
			projectSet.addAll(creatingProjectList);
		if (assignedProjectList != null)
			projectSet.addAll(assignedProjectList);
		return new ArrayList<>(projectSet);
	}

	@Override
	public PageResDto<ProjectResDto> getAllProjectPages(Pageable pageable) throws BestWorkBussinessException {
		try {
			Page<ProjectEntity> pageProject = projectRepository.findAll(pageable);
			return responseUtils.convertPageEntityToDTO(pageProject, ProjectResDto.class);

		} catch (Exception ex) {
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
		return this.projectRepository.getLastPrjId();
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
	public void deleteProjectById(List<String> listProjectId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = getAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		for (String id : listProjectId) {
			ProjectEntity prj = this.projectRepository.findbyProjectId(id);
			if (prj == null) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.S1X0002, null);
			}
			if (!this.chkPrjCrtByCurUser(prj, curUsername)) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
			}
		}
		try {
			this.projectRepository.deleteProjectById(listProjectId);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0002, null);
		}

	}

	@Override
	public void saveProject(ProjectTaskReqDto projectTaskDto, ProjectTypeEntity projectType)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = getAuthRoleReq();
		// User can not create new project
		if (!userAuthRoleReq.getIsOrgAdmin() && !userAuthRoleReq.getIsInvestor()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		String generateProjectId = "";

		// User can create new project
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
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		if (!userAuthRoleReq.getIsOrgAdmin() && !userAuthRoleReq.getIsInvestor()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		try {
			projectRegist.setId(generateProjectId);
			projectRegist.setProjectName(projectReqDto.getProjectName());
			projectRegist.setDescription(projectReqDto.getDescription());
			projectRegist.setNotificationFlag(projectReqDto.getNotificationFlag());
			projectRegist.setIsPaid(projectReqDto.getIsPaid());
			projectRegist.setStatus(projectReqDto.getStatus());
			projectRegist.setStartDate(projectReqDto.getStartDate());
			projectRegist.setCreateBy(userAuthRoleReq.getUsername());
			projectRegist.setUpdateBy(userAuthRoleReq.getUsername());
			projectRegist.setCreateDate(LocalDateTime.now());
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
			List<AssignTaskEntity> assignTasklist = new ArrayList<>();

			for (int i = 0; i < userList.size(); i++) {
				UserEntity user = userService.findUserByUserId(userList.get(i).getUserId());
				if (user == null) {
					throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0005, null);
				}
				if (userList.get(i).isCanEdit() || userList.get(i).isCanView()) {
					AssignTaskEntity assignTask = new AssignTaskEntity();
					assignTask.setCompanyId(projectAssignReqDto.getCompanyId());
					assignTask.setProjectId(generateProjectId);
					assignTask.setUserId(userList.get(i).getUserId());
					assignTask.setCanView(userList.get(i).isCanView());
					assignTask.setCanEdit(userList.get(i).isCanEdit());
					assignTasklist.add(assignTask);
				}
			}
			assignTaskRepository.saveAll(assignTasklist);

			for (ProjectRoleUserReqDto user : userList) {
				sendNotify(generateProjectId, user);
			}
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
		ProjectEntity curPrj = null;
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		String curUsername = userAuthRoleReq.getUsername();
		UserEntity curUser = this.userService.findUserByUsername(curUsername);

		curPrj = projectRepository.findbyProjectId(projectId);
		if (curPrj == null) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}

		// Only user creating project can edit project info and create/change/remove
		// assignment other user
		// (Other users (who are assigned as editor) can only create/update/... Material
		// Supplies )
		if (userAuthRoleReq.getIsSysAdmin() || curUser == null || !chkPrjCrtByCurUser(curPrj, curUsername)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}

		if (projectTaskDto.getRoleData() != null && projectTaskDto.getRoleData().size() > 0) {
			for (int j = 0; j < projectTaskDto.getRoleData().size(); j++) {
				Long companyId = projectTaskDto.getRoleData().get(j).getCompanyId();
				List<ProjectRoleUserReqDto> userList = projectTaskDto.getRoleData().get(j).getUserList();
				AssignTaskEntity assignTask = null;
				AssignTaskEntity originalAssignTask = new AssignTaskEntity();
				try {
					updateProject(curPrj, projectTaskDto.getProject(), projectType);
					for (int i = 0; i < userList.size(); i++) {
						UserEntity user = userService.findUserByUserId(userList.get(i).getUserId());
						if (user != null) {
							assignTask = assignTaskRepository.findbyCondition(userList.get(i).getUserId(), companyId,
									projectId);
							if (assignTask != null) {
								BeanUtils.copyProperties(assignTask, originalAssignTask);
								assignTask.setCanView(userList.get(i).isCanView());
								assignTask.setCanEdit(userList.get(i).isCanEdit());
								assignTaskRepository.save(assignTask);
								if (((originalAssignTask.isCanEdit() != userList.get(i).isCanEdit())
										|| (originalAssignTask.isCanView() != userList.get(i).isCanView()))
										&& (userList.get(i).isCanEdit() || userList.get(i).isCanView())) {
									sendChgAssignNotify(projectId, userList.get(i));
								} else if (((originalAssignTask.isCanEdit() != userList.get(i).isCanEdit())
										|| (originalAssignTask.isCanView() != userList.get(i).isCanView()))
										&& (!userList.get(i).isCanEdit() && !userList.get(i).isCanView())) {
									sendRemoveAssignNotify(projectId, userList.get(i));
								}
							} else {
								AssignTaskEntity assignTaskNew = new AssignTaskEntity();
								assignTaskNew.setCompanyId(companyId);
								assignTaskNew.setProjectId(projectId);
								assignTaskNew.setUserId(userList.get(i).getUserId());
								assignTaskNew.setCanView(userList.get(i).isCanView());
								assignTaskNew.setCanEdit(userList.get(i).isCanEdit());
								assignTaskRepository.save(assignTaskNew);
								sendNotify(projectId, userList.get(i));
							}

						} else {
							throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0005, null);
						}
					}
				} catch (Exception ex) {
					throw new BestWorkBussinessException(CommonConstants.MessageCode.S1X0009, new Object[] {
							CommonConstants.Character.PROJECT, (projectTaskDto.getProject().getProjectName()) });
				}
			}
		} else {
			updateProject(curPrj, projectTaskDto.getProject(), projectType);
		}
	}

	private boolean chkPrjCrtByCurUser(ProjectEntity curPrj, String curUsername) {
		if (curPrj.getCreateBy().equals(curUsername)) {
			return true;
		}
		return false;
	}

	private void sendNotify(String generatePrjId, ProjectRoleUserReqDto user) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = getAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		String projectName = projectRepository.findbyProjectId(generatePrjId).getProjectName();

		if (user.isCanEdit() || user.isCanView()) {
			NotificationReqDto notifyReqDto = new NotificationReqDto();
			notifyReqDto.setTitle("Assignment to project " + projectName);
			notifyReqDto.setContent(
					curUsername + " has assigned you to the project as " + (user.isCanEdit() ? "editor" : "viewer"));
			notifyReqDto.setUserId(user.getUserId());
			notifyService.createNotification(notifyReqDto);
		}
	}

	private void sendChgAssignNotify(String projectId, ProjectRoleUserReqDto user) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = getAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		String projectName = projectRepository.findbyProjectId(projectId).getProjectName();

		NotificationReqDto notificationReqDto = new NotificationReqDto();
		notificationReqDto.setTitle("Assignment to project " + projectName);
		notificationReqDto.setContent(curUsername + " has changed your assignment on the project to "
				+ (user.isCanEdit() ? "editor" : "viewer"));
		notificationReqDto.setUserId(user.getUserId());
		notifyService.createNotification(notificationReqDto);
	}

	private void sendRemoveAssignNotify(String projectId, ProjectRoleUserReqDto user)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = getAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		String projectName = projectRepository.findbyProjectId(projectId).getProjectName();

		NotificationReqDto notifyReqDto = new NotificationReqDto();
		notifyReqDto.setTitle("Remove assignment on project " + projectName);
		notifyReqDto.setContent("Your assignment on the project has been removed by " + curUsername);
		notifyReqDto.setUserId(user.getUserId());
		notifyService.createNotification(notifyReqDto);
	}

	@Override
	public List<ProjectAssignRepository> getCompanyUserForAssign(AssignTaskReqDto assignTaskReqDto)
			throws BestWorkBussinessException {
		List<ProjectAssignRepository> lstResult = null;
		if (StringUtils.isNotBlank(assignTaskReqDto.getCompanyId())) {
			long companyId = Long.parseLong(assignTaskReqDto.getCompanyId());
			lstResult = projectRepository.getCompAndRoleUserByCompId(companyId);
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
	public ProjectResDto getDetailProject(String projectId) throws BestWorkBussinessException {

		UserAuthDetected userAuthRoleReq = getAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();

		ProjectEntity project = projectRepository.findbyProjectId(projectId);
		if (project == null) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.S1X0002, null);
		}
		List<ProjectEntity> involvedPrjList = this.getPrjInvolvedByCurUser(curUsername);
		if (!involvedPrjList.contains(project)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}

		ProjectResDto projectDto = null;
		if (project != null) {
			projectDto = new ProjectResDto();
			projectDto.setId(project.getId());
			projectDto.setProjectName(project.getProjectName());
			projectDto.setDescription(project.getDescription());
			projectDto.setNotificationFlag(project.getNotificationFlag());
			projectDto.setIsPaid(project.getIsPaid());
			projectDto.setProjectType(project.getProjectType());
			projectDto.setStatus(project.getStatus());
			projectDto.setStartDate(project.getStartDate());
		}
		return projectDto;

	}

	@Override
	public Map<Long, List<ProjectRoleUserResDto>> getListAssign(AssignTaskReqDto assignTaskReqDto)
			throws BestWorkBussinessException {
		List<ProjectAssignRepository> listRole = null;
		if (StringUtils.isNotBlank(assignTaskReqDto.getProjectId())) {
			listRole = projectRepository.getCompAndRoleUserByPrj(assignTaskReqDto.getProjectId());
		}

		Map<Long, List<ProjectRoleUserResDto>> resultList = listRole.stream()
				.map(listR -> new ProjectRoleUserResDto(listR.getCompanyId(), listR.getUserId(), listR.getUserName(),
						listR.getCanView(), listR.getCanEdit()))
				.collect(Collectors.groupingBy(ProjectRoleUserResDto::getCompanyId, Collectors.toList()));

		return resultList;
	}

	@Override
	public void changeStatus(String projectId, ProjectStatusReqDto projectStatusReqDto)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = getAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		ProjectEntity currentProject = null;
		try {
			currentProject = projectRepository.findbyProjectId(projectId);
			if (currentProject != null && chkPrjCrtByCurUser(currentProject, curUsername)) {
				currentProject.setStatus(projectStatusReqDto.getToStatus());
			}
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0017, null);
		}
	}

	@Override
	public List<String> getAllProjectIdByCompany(List<Long> listCompanyId) throws BestWorkBussinessException {
		List<String> listProjectId = null;
		if (listCompanyId != null) {
			listProjectId = projectRepository.getAllPrjIdByComp(listCompanyId);
		}
		return listProjectId;
	}

	public void updateProject(ProjectEntity currentProject, ProjectReqDto projectReqDto, ProjectTypeEntity projectType)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = getAuthRoleReq();
		if (currentProject != null) {
			currentProject.setProjectName(projectReqDto.getProjectName());
			currentProject.setDescription(projectReqDto.getDescription());
			currentProject.setNotificationFlag(projectReqDto.getNotificationFlag());
			currentProject.setIsPaid(projectReqDto.getIsPaid());
			currentProject.setStatus(projectReqDto.getStatus());
			currentProject.setStartDate(projectReqDto.getStartDate());
			currentProject.setUpdateBy(userAuthRoleReq.getUsername());
			currentProject.setUpdateDate(LocalDateTime.now());
			currentProject.setProjectType(projectType);
			projectRepository.save(currentProject);
		}

	}

	/**
	 * Function get projects which are created by specific user (username) DiepTT
	 * 
	 * @param current username
	 * @return List<ProjectEntity>
	 */
	@Override
	public List<ProjectEntity> getPrjCreatedByCurUser(String curUsername) {
		return this.projectRepository.findPrjCreatedByCurUser(curUsername);
	}

	/**
	 * Function get projects which are assigned to specific user (username) DiepTT
	 * 
	 * @param current username
	 * @return List<ProjectEntity>
	 */
	@Override
	public List<ProjectEntity> getPrAssignedToCurUser(String curUsername) {
		return this.projectRepository.findPrjAssignedToCurUser(curUsername);
	}

	/**
	 * Function: get project that contains a specific construction DiepTT
	 * 
	 * @Param constructionId
	 * @return ProjectEntity
	 */
	@Override
	public ProjectEntity getPrjByCstrtId(long constructionId) {
		return this.projectRepository.findByConstructionId(constructionId);
	}

	@Override
	public List<ProjectEntity> getPrj4CompanyAdmin(String curUsername) {
		return this.projectRepository.getPrjLstByOrgAdminUsername(curUsername);
	}

	@Override
	public List<ProjectEntity> getPrj4SysAdmin(String curUsername) {
		return this.projectRepository.getPrjLstBySysAdminUsername(curUsername);
	}

	@Override
	public List<ProjectEntity> getPrjLstByAnyUsername(UserAuthDetected userAuthRoleReq) {
		List<ProjectEntity> canViewprjList = null;
		String curUsername = userAuthRoleReq.getUsername();
		if (!userAuthRoleReq.getIsSysAdmin() && !userAuthRoleReq.getIsOrgAdmin()) {
			canViewprjList = this.getPrjInvolvedByCompUser(curUsername);
		} else if (userAuthRoleReq.getIsOrgAdmin()) {
			canViewprjList = this.getPrj4CompanyAdmin(curUsername);
		} else if (userAuthRoleReq.getIsSysAdmin()) {
			canViewprjList = this.getPrj4SysAdmin(curUsername);
		}
		return canViewprjList;
	}

	/**
	 * Private function: get all projects that current user is being involved
	 * (creating or/and being assigned)
	 * 
	 * @param curUsername
	 * @return List<ProjectEntity>
	 */
	private List<ProjectEntity> getPrjInvolvedByCompUser(String curUsername) {
		List<ProjectEntity> creatingPrjList = this.getPrjCreatedByCurUser(curUsername);
		List<ProjectEntity> assignedPrjList = this.getPrAssignedToCurUser(curUsername);
		Set<ProjectEntity> projectSet = new HashSet<>();
		if (creatingPrjList != null)
			projectSet.addAll(creatingPrjList);
		if (assignedPrjList != null)
			projectSet.addAll(assignedPrjList);
		return new ArrayList<>(projectSet);
	}
}
