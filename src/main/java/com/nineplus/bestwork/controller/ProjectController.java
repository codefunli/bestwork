package com.nineplus.bestwork.controller;

import java.util.Collection;

import javax.security.auth.message.callback.SecretKeyCallback.Request;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.PageResponseDTO;
import com.nineplus.bestwork.dto.PageSearchDTO;
import com.nineplus.bestwork.dto.PrjConditionSearchDTO;
import com.nineplus.bestwork.dto.RProjectReqDTO;
import com.nineplus.bestwork.dto.TProjectResponseDto;
import com.nineplus.bestwork.entity.TProject;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.ProjectService;
import com.nineplus.bestwork.utils.CommonConstants;

/**
 * This controller use for processing with project
 * @author tuanna
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ProjectController extends BaseController {

	@Autowired
	private ProjectService projectService;

	/**
	 * Get all projects
	 * @return all projects
	 */
	@GetMapping("/projects")
	public ResponseEntity<? extends Object> getAllProject() {
		Collection<TProject> tProjects = null;
		try {
			tProjects = projectService.getAllProjects();

		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}

		return success(CommonConstants.MessageCode.S1X0001, tProjects, null);
	}

	/**
	 * 
	 * @param prjConDto  project condition field search
	 * @param pageSearchDto common condition search
	 * @return
	 * @throws BestWorkBussinessException
	 */
	@PostMapping("/projects/search")
	public ResponseEntity<? extends Object> getProjectPages(@RequestBody RProjectReqDTO prjConDto) throws BestWorkBussinessException {

		PageResponseDTO<TProjectResponseDto> pageProject = null;
		try {
			pageProject = projectService.getProjectPage(prjConDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0007, pageProject, null);

	}

}
