package com.nineplus.bestwork.services;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nineplus.bestwork.dto.PageResponseDTO;
import com.nineplus.bestwork.dto.PageSearchDTO;
import com.nineplus.bestwork.dto.PrjConditionSearchDTO;
import com.nineplus.bestwork.dto.RProjectReqDTO;
import com.nineplus.bestwork.dto.TProjectResponseDto;
import com.nineplus.bestwork.entity.TProject;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.repository.ProjectRepository;
import com.nineplus.bestwork.utils.BestWorkBeanUtils;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.MessageUtils;
import com.nineplus.bestwork.utils.PageUtils;

@Service
public class ProjectService {

	private final Logger logger = LoggerFactory.getLogger(ProjectService.class);

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private PageUtils responseUtils;

	@Autowired
	private MessageUtils messageUtils;

	@GetMapping
	public List<TProject> getAllProjects() throws BestWorkBussinessException {
		return projectRepository.findAll();

	}

	@PostMapping
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
			//long prjId = pageSearchDto.getProjectCondition().getPrjId();
			String prjName = pageSearchDto.getProjectCondition().getPrjName();

			pageTProject = projectRepository.findProjectWithCondition(prjName, pageable);

			return responseUtils.convertPageEntityToDTO(pageTProject, TProjectResponseDto.class);
		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0003, null), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}

	}

}
