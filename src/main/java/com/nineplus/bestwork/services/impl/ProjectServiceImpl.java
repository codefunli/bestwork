package com.nineplus.bestwork.services.impl;

import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.dto.PageResponseDTO;
import com.nineplus.bestwork.dto.PrjConditionSearchDTO;
import com.nineplus.bestwork.dto.RProjectReqDTO;
import com.nineplus.bestwork.dto.TProjectResponseDto;
import com.nineplus.bestwork.entity.TProject;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.repository.ProjectRepository;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.MessageUtils;
import com.nineplus.bestwork.utils.PageUtils;

@Service
public class ProjectServiceImpl implements IProjectService {

	private final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private PageUtils responseUtils;

	@Autowired
	private MessageUtils messageUtils;

	@Override
	public PageResponseDTO<TProjectResponseDto> getProjectPage(RProjectReqDTO pageSearchDto)
			throws BestWorkBussinessException {
		try {
			int pageNumber = NumberUtils.toInt(pageSearchDto.getPageConditon().getPage());
			if (pageNumber > 0) {
				pageNumber = pageNumber - 1;
			}
			Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(pageSearchDto.getPageConditon().getSize()),
					Sort.by(pageSearchDto.getPageConditon().getSortDirection(),
							pageSearchDto.getPageConditon().getSortBy()));
			Page<TProject> pageTProject;

			PrjConditionSearchDTO prjConditionSearchDTO = pageSearchDto.getProjectCondition();
			pageTProject = projectRepository.findProjectWithCondition(prjConditionSearchDTO, pageable);

			return responseUtils.convertPageEntityToDTO(pageTProject, TProjectResponseDto.class);
		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0003, null), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}

	}

	@Override
	public PageResponseDTO<TProjectResponseDto> getAllProjectPages(Pageable pageable)
			throws BestWorkBussinessException {
		try {
			Page<TProject> pageTProject = projectRepository.findAll(pageable);
			return responseUtils.convertPageEntityToDTO(pageTProject, TProjectResponseDto.class);

		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0003, null), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

	@Override
	public TProjectResponseDto getProjectById(String id) throws BestWorkBussinessException {
		TProjectResponseDto projectResponseDto = new TProjectResponseDto();
		try {
			Optional<TProject> project = projectRepository.findById(id);

			if (project.isPresent()) {
				return null;
			}
			projectResponseDto.setId(project.get().getId());
			projectResponseDto.setProjectName(project.get().getProjectName());
			projectResponseDto.setDescription(project.get().getDescription());
			projectResponseDto.setProjectType(project.get().getProjectType());
			projectResponseDto.setNotificationFlag(project.get().getNotificationFlag());
			projectResponseDto.setIsPaid(project.get().getIsPaid());
			projectResponseDto.setStatus(project.get().getStatus());
			projectResponseDto.setCreateDate(project.get().getCreateDate());
			projectResponseDto.setUpdateDate(project.get().getUpdateDate());
			projectResponseDto.setComment(project.get().getComment());
			projectResponseDto.setFileStorages(project.get().getFileStorages());

			return projectResponseDto;
		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0003, null), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

	@Override
	public TProject saveProject(TProject project) {
		return this.projectRepository.save(project);
	}

}
