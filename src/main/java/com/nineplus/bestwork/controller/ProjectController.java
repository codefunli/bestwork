package com.nineplus.bestwork.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.PageResponseDTO;
import com.nineplus.bestwork.dto.ProjectRequestDto;
import com.nineplus.bestwork.dto.RProjectReqDTO;
import com.nineplus.bestwork.dto.TProjectResponseDto;
import com.nineplus.bestwork.entity.TProject;
import com.nineplus.bestwork.entity.TProjectType;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.ProjectStatus;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.services.IProjectTypeService;
import com.nineplus.bestwork.utils.CommonConstants;

/**
 * This controller use for processing with project
 * 
 * @author tuanna
 *
 */
@RestController
@RequestMapping("/api/v1/auth/projects")
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
	public ResponseEntity<? extends Object> getProjectPages(@RequestBody RProjectReqDTO prjConDto)
			throws BestWorkBussinessException {

		PageResponseDTO<TProjectResponseDto> pageProject = null;
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

		PageResponseDTO<TProjectResponseDto> pageProjects = null;
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
		TProject createdProject = null;
		try {
			createdProject = this.projectService.saveProject(projectRequestDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0004, createdProject, null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<? extends Object> getProjectById(@PathVariable("id") String id) {
		Optional<TProject> projectOptional = null;
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
		Optional<TProject> projectOptional = null;
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
		TProject updatedProject = null;

		try {
			projectOptional.get().setStatus(ProjectStatus.values()[projectRequestDto.getStatus()]);
			projectOptional.get().setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
			projectOptional.get().setProjectType(getProjectTypeById(projectRequestDto.getProjectType()));

			updatedProject = this.projectService.updateProject(projectOptional.get());
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0008, updatedProject, null);
	}

	private TProjectType getProjectTypeById(Integer projectTypeId) {
		Optional<TProjectType> projectTypeOptional = null;
		try {
			projectTypeOptional = this.projectTypeService.getProjectTypeById(projectTypeId);

			if (!projectTypeOptional.isPresent()) {
				return null;
			}
		} catch (BestWorkBussinessException ex) {
			ex.getMessage();
		}
		return projectTypeOptional.get();
	}
}
