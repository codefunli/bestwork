package com.nineplus.bestwork.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.dto.PageResponseDTO;
import com.nineplus.bestwork.dto.PrjConditionSearchDTO;
import com.nineplus.bestwork.dto.ProjectRequestDto;
import com.nineplus.bestwork.dto.RProjectReqDTO;
import com.nineplus.bestwork.dto.TProjectResponseDto;
import com.nineplus.bestwork.entity.TProject;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.ProjectStatus;
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
	public Optional<TProject> getProjectById(String id) throws BestWorkBussinessException {
		if (id == null || id.equalsIgnoreCase("")) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
		return this.projectRepository.findById(id);

	}

	@Override
	public TProject saveProject(ProjectRequestDto projectRequestDto) {
		TProject project = new TProject();
		BeanUtils.copyProperties(projectRequestDto, project);

		project.setId(this.setProjectId());
		project.setStatus(ProjectStatus.values()[projectRequestDto.getStatus()]);
		project.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
		return this.projectRepository.save(project);
	}

	private String getLastProjectId() {
		return this.projectRepository.getLastProjectIdString();
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
	public TProject updateProject(TProject project) throws BestWorkBussinessException {
		return this.projectRepository.save(project);

	}

}
