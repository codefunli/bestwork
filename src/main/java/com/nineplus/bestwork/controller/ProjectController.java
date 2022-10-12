package com.nineplus.bestwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.PageResponseDTO;
import com.nineplus.bestwork.dto.RProjectReqDTO;
import com.nineplus.bestwork.dto.TProjectResponseDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.impl.ProjectServiceImpl;
import com.nineplus.bestwork.utils.CommonConstants;

/**
 * This controller use for processing with project
 * 
 * @author tuanna
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ProjectController extends BaseController {

	@Autowired
	private ProjectServiceImpl projectService;

	/**
	 * 
	 * @param prjConDto     project condition field search
	 * @param pageSearchDto common condition search
	 * @return
	 * @throws BestWorkBussinessException
	 */
	@PostMapping("/projects/search")
	public ResponseEntity<? extends Object> getProjectPages(@RequestBody RProjectReqDTO prjConDto)
			throws BestWorkBussinessException {

		PageResponseDTO<TProjectResponseDto> pageProject = null;
		try {
			pageProject = projectService.getProjectPage(prjConDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0007, pageProject, null);

	}

	@GetMapping("/projects/page")
	public ResponseEntity<? extends Object> getAllProjectsPage(@PageableDefault Pageable pageable) {

		PageResponseDTO<TProjectResponseDto> pageProjects = null;
		try {
			pageProjects = projectService.getAllProjectPages(pageable);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0007, pageProjects, null);

	}
}
