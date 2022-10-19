package com.nineplus.bestwork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.ProjectTypeResponseDto;
import com.nineplus.bestwork.entity.ProjectTypeEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IProjectTypeService;

@RestController
@RequestMapping("/api/v1/project-type")
public class ProjectTypeController {

	@Autowired
	private IProjectTypeService typeService;

	@GetMapping("/all")
	public ResponseEntity<List<ProjectTypeResponseDto>> getAllProjectTypes() {
		List<ProjectTypeResponseDto> projectTypeResponseDtos = typeService.getAllProjectTypes();
		if (projectTypeResponseDtos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(projectTypeResponseDtos, HttpStatus.OK);
	}

	@GetMapping("/{projectTypeId}")
	public ProjectTypeEntity getProjectTypeById(@PathVariable Integer projectTypeId) throws BestWorkBussinessException {
		return this.typeService.getProjectTypeById(projectTypeId);
	}

}
