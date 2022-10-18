package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */

@Data
@EqualsAndHashCode
public class PostRequestDto {

	private String projectId;

	private String description;

	private String[] images;
//
//	@Override
//	public boolean supports(Class<?> clazz) {
//		return false;
//	}

//	@Override
//	public void validate(Object target, Errors errors) {
//		PostRequestDto postDto = (PostRequestDto) target;
//		if (!isExistedProjectId(postDto.projectId)) {
//			errors.rejectValue("projectId", "create.notExistProjectId", "Not exist project by id.");
//		}
//
//	}
//
//	private boolean isExistedProjectId(String projectId) {
//		Optional<ProjectEntity> project = null;
//		try {
//			ProjectServiceImpl projectService = new ProjectServiceImpl();
//			project = projectService.getProjectById(projectId);
//		} catch (BestWorkBussinessException e) {
//			e.getMessage();
//		}
//		if (project.isPresent()) {
//			return true;
//		}
//		return false;
//	}

}
