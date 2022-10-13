package com.nineplus.bestwork.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.PageResponseDTO;
import com.nineplus.bestwork.dto.ProjectRequestDto;
import com.nineplus.bestwork.dto.RProjectReqDTO;
import com.nineplus.bestwork.dto.TProjectResponseDto;
import com.nineplus.bestwork.entity.TProject;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.ProjectStatus;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.utils.CommonConstants;

/**
 * This controller use for processing with project
 * 
 * @author tuanna
 *
 */
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController extends BaseController {

	@Autowired
	private IProjectService projectService;

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
		return success(CommonConstants.MessageCode.S1X0007, pageProject, null);

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
		return success(CommonConstants.MessageCode.S1X0007, pageProjects, null);
	}

	@PostMapping("/create")
	public ResponseEntity<? extends Object> createProject(@Valid @RequestBody ProjectRequestDto projectRequestDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.S1X0005, null, bindingResult.getFieldErrors().toArray());
		}
		TProject project = new TProject();
		TProject createdProject = new TProject();

		try {

			BeanUtils.copyProperties(projectRequestDto, project);
			project.setStatus(ProjectStatus.values()[projectRequestDto.getStatus()]);
			project.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
			createdProject = this.projectService.saveProject(project);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0004, createdProject, null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<? extends Object> getProjectById(@PathVariable("id") String id) {
		TProjectResponseDto project = null;
		try {
			project = projectService.getProjectById(id);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0006, project, null);
	}

}
