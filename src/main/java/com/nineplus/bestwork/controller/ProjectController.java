package com.nineplus.bestwork.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.ProjectRequestDto;
import com.nineplus.bestwork.dto.ProjectResponseDto;
import com.nineplus.bestwork.dto.ProjectTypeResponseDto;
import com.nineplus.bestwork.dto.RProjectReqDto;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.entity.ProjectTypeEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.ProjectStatus;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.services.IProjectTypeService;
import com.nineplus.bestwork.utils.CommonConstants;

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
	@PostMapping("/search")
	public ResponseEntity<? extends Object> getProjectPages(@RequestBody RProjectReqDto prjConDto)
			throws BestWorkBussinessException {

		PageResponseDto<ProjectResponseDto> pageProject = null;
		try {
			pageProject = projectService.getProjectPage(prjConDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (pageProject.getContent().isEmpty()) {
			return success(CommonConstants.MessageCode.E1X0003, pageProject, null);
		}
		System.out.println(pageProject.getContent());
		return success(CommonConstants.MessageCode.S1X0006, pageProject, null);

	}

	@GetMapping("/page")
	public ResponseEntity<? extends Object> getAllProjectsPage(@PageableDefault Pageable pageable) {

		PageResponseDto<ProjectResponseDto> pageProjects = null;
		try {
			pageProjects = projectService.getAllProjectPages(pageable);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (pageProjects.getContent().isEmpty()) {
			return success(CommonConstants.MessageCode.E1X0003, pageProjects, null);
		}
		return success(CommonConstants.MessageCode.S1X0006, pageProjects, null);
	}

	@PostMapping("/create")
	public ResponseEntity<? extends Object> createProject(@Valid @RequestBody ProjectRequestDto projectRequestDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.S1X0005, bindingResult.getFieldErrors().toArray(), null);
		}
		ProjectEntity createdProject = null;
		try {
			createdProject = this.projectService.saveProject(projectRequestDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0004, createdProject, null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<? extends Object> getProjectById(@PathVariable("id") String id) {
		Optional<ProjectEntity> projectOptional = null;
		try {
			projectOptional = this.projectService.getProjectById(id);
			if (!projectOptional.isPresent()) {
				return failed(CommonConstants.MessageCode.E1X0003, null);
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0001, projectOptional.get(), null);
	}

	@PatchMapping("/update/{id}")
	public ResponseEntity<? extends Object> createProject(@PathVariable String id,
			@Valid @RequestBody ProjectRequestDto projectRequestDto, BindingResult bindingResult)
			throws BestWorkBussinessException {
		Optional<ProjectEntity> projectOptional = null;
		try {
			projectOptional = this.projectService.getProjectById(id);
			if (!projectOptional.isPresent()) {
				return failed(CommonConstants.MessageCode.E1X0003, null);
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.S1X0009, bindingResult.getFieldErrors().toArray(), null);
		}
		BeanUtils.copyProperties(projectRequestDto, projectOptional.get());
		ProjectEntity updatedProject = null;

		try {
			projectOptional.get().setStatus(ProjectStatus.values()[projectRequestDto.getStatus()]);
			projectOptional.get().setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
			ProjectTypeEntity projectType = this.projectTypeService
					.getProjectTypeById(projectRequestDto.getProjectType());
			projectOptional.get().setProjectType(projectType);

			updatedProject = this.projectService.updateProject(projectOptional.get());
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0008, updatedProject, null);
	}

	@PostMapping("/delete/{ids}")
	public ResponseEntity<? extends Object> deleteMassiveProject(@PathVariable List<String> ids) {
		try {
			this.projectService.deleteProjectById(ids);
		} catch (BestWorkBussinessException ex) {
			return failed(CommonConstants.MessageCode.S1X0011, ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0010, null, null);
	}

	@GetMapping("/types")
	public ResponseEntity<List<ProjectTypeResponseDto>> getAllProjectTypes() {
		List<ProjectTypeResponseDto> projectTypeResponseDtos = projectTypeService.getAllProjectTypes();
		if (projectTypeResponseDtos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(projectTypeResponseDtos, HttpStatus.OK);
	}

	@GetMapping("/types/{projectTypeId}")
	public ProjectTypeEntity getProjectTypeById(@PathVariable Integer projectTypeId) throws BestWorkBussinessException {
		return this.projectTypeService.getProjectTypeById(projectTypeId);
	}

}
