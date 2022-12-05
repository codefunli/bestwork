package com.nineplus.bestwork.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.AssignTaskReqDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.dto.ProjectDeleteByIdDto;
import com.nineplus.bestwork.dto.ProjectResDto;
import com.nineplus.bestwork.dto.ProjectRoleUserResDto;
import com.nineplus.bestwork.dto.ProjectStatusReqDto;
import com.nineplus.bestwork.dto.ProjectStatusResDto;
import com.nineplus.bestwork.dto.ProjectTaskReqDto;
import com.nineplus.bestwork.dto.ProjectTypeResDto;
import com.nineplus.bestwork.entity.ProjectTypeEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.repository.ProjectAssignRepository;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.services.IProjectTypeService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.ProjectStatus;;

/**
 * This controller use for processing with project
 * 
 * @author DiepTT
 *
 */
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController extends BaseController {

	@Autowired
	private IProjectService projectService;

	@Autowired
	private IProjectTypeService projectTypeService;

	/**
	 * 
	 * @param prjConDto     project condition field search
	 * @param pageSearchDto common condition search
	 * @return
	 * @throws BestWorkBussinessException
	 */
	@PostMapping("/list")
	public ResponseEntity<? extends Object> getProjectPages(@RequestBody PageSearchDto prjConDto)
			throws BestWorkBussinessException {

		PageResDto<ProjectResDto> pageProject = null;
		try {
			pageProject = projectService.getProjectPage(prjConDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (pageProject.getContent().isEmpty()) {
			return success(CommonConstants.MessageCode.E1X0003, pageProject, null);
		}
		return success(CommonConstants.MessageCode.S1X0006, pageProject, null);

	}

	@GetMapping("/{projectId}")
	public ResponseEntity<? extends Object> getProjectById(@PathVariable String projectId) {
		ProjectResDto project = null;
		try {
			project = projectService.getDetailProject(projectId);
			if (project == null) {
				return failed(CommonConstants.MessageCode.E1X0003, null);
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0001, project, null);
	}

	@PostMapping("/delete")
	public ResponseEntity<? extends Object> deleteMassiveProject(
			@RequestBody ProjectDeleteByIdDto projectDeleteByIdDto) {
		try {
			this.projectService.deleteProjectByIds(projectDeleteByIdDto.getId());
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
//			return failed(CommonConstants.MessageCode.S1X0012, ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0011, null, null);
	}

	@GetMapping("/types")
	public ResponseEntity<List<ProjectTypeResDto>> getAllProjectTypes() {
		List<ProjectTypeResDto> projectTypeResponseDtos = projectTypeService.getAllProjectTypes();
		if (projectTypeResponseDtos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(projectTypeResponseDtos, HttpStatus.OK);
	}

	@GetMapping("/types/{projectTypeId}")
	public ProjectTypeEntity getProjectTypeById(@PathVariable Integer projectTypeId) throws BestWorkBussinessException {
		return this.projectTypeService.getProjectTypeById(projectTypeId);
	}

	@PostMapping("/regist")
	public ResponseEntity<? extends Object> registProject(@RequestBody ProjectTaskReqDto projectTask) {
		try {
			ProjectTypeEntity projectType = this.getProjectTypeById(projectTask.getProject().getProjectType());
			if (projectType != null) {
				projectService.saveProject(projectTask, projectType);
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0004, null, null);
	}

	@PostMapping("/update/{projectId}")
	public ResponseEntity<? extends Object> updateProject(@RequestBody ProjectTaskReqDto projectTask,
			@PathVariable String projectId) {
		try {
			ProjectTypeEntity projectType = this.getProjectTypeById(projectTask.getProject().getProjectType());
			if (projectType != null) {
				projectService.updateProject(projectTask, projectType, projectId);
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0008, null, null);
	}

	@PostMapping("/assign-list-create")
	public ResponseEntity<? extends Object> getCompanyUserForAssign(@RequestBody AssignTaskReqDto assignTaskReqDto) {
		List<ProjectAssignRepository> assignList;
		try {
			assignList = projectService.getCompanyUserForAssign(assignTaskReqDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0016, assignList, null);
	}

	@PostMapping("/assign-list-edit")
	public ResponseEntity<? extends Object> getCompanyUserForAssignEdit(
			@RequestBody AssignTaskReqDto assignTaskReqDto) {
		Map<Long, List<ProjectRoleUserResDto>> assignList;
		try {
			assignList = projectService.getListAssign(assignTaskReqDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0016, assignList, null);
	}

	@GetMapping("/status")
	public ResponseEntity<? extends Object> getProgressStatus() throws BestWorkBussinessException {
		List<ProjectStatusResDto> projectStatus = new ArrayList<>();
		for (ProjectStatus status : ProjectStatus.values()) {
			ProjectStatusResDto dto = new ProjectStatusResDto();
			dto.setId(status.ordinal());
			dto.setStatus(status.getValue());
			projectStatus.add(dto);
		}
		return success(CommonConstants.MessageCode.S1X0015, projectStatus, null);
	}

	@PostMapping("{projectId}/change-status")
	public ResponseEntity<? extends Object> changeStatus(@PathVariable String projectId,
			@RequestBody ProjectStatusReqDto projectStatusReqDto) {
		try {
			projectService.changeStatus(projectId, projectStatusReqDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0017, null, null);
	}
}
