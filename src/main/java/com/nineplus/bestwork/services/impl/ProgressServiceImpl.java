package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.dto.FileStorageResponseDto;
import com.nineplus.bestwork.dto.ProgressReqDto;
import com.nineplus.bestwork.dto.ProgressResDto;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.Progress;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.ProgressRepository;
import com.nineplus.bestwork.services.IProgressService;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.DateUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
public class ProgressServiceImpl implements IProgressService {
	@Autowired
	private ProgressRepository progressRepository;

	@Autowired
	DateUtils dateUtils;

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	private IStorageService storageService;

	@Autowired
	private IProjectService projectService;

	@Override
	public void saveProgress(ProgressReqDto progressReqDto) throws BestWorkBussinessException {
		// Check role of user
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		String createUser = userAuthRoleReq.getUsername();
		try {
			Progress progress = new Progress();
			progress.setTitle(progressReqDto.getTitle());
			progress.setStatus(progressReqDto.getStatus());
			progress.setReport(progressReqDto.getReport());
			progress.setNote(progressReqDto.getNote());
			String startDt = dateUtils.convertToUTC(progressReqDto.getStartDate());
			String endDt = dateUtils.convertToUTC(progressReqDto.getEndDate());
			progress.setStartDate(startDt);
			progress.setEndDate(endDt);
			progress.setCreateBy(createUser);
			progress.setCreateDate(LocalDateTime.now());
			progress.setProject(projectService.getProjectById(progressReqDto.getProjectId()).get());
			progressRepository.save(progress);

			for (String imageData : progressReqDto.getImages()) {
				storageService.storeFile(imageData, progress);
			}

		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
		}

	}

	@Override
	public List<ProgressResDto> getAllProgress() throws BestWorkBussinessException {
		return null;
	}

	@Override
	public List<ProgressResDto> getProgressByProjectId(String projectId) throws BestWorkBussinessException {
		List<Progress> progress = progressRepository.findPostsByProjectId(projectId);
		List<ProgressResDto> progressDto = new ArrayList<>();
		for (Progress pro : progress) {
			ProgressResDto proDto = new ProgressResDto();
			proDto.setTitle(pro.getTitle());
			proDto.setStatus(pro.getStatus());
			proDto.setReport(pro.getReport());
			proDto.setNote(pro.getNote());
			proDto.setCreateBy(pro.getCreateBy());
			proDto.setStartDate(pro.getStartDate());
			proDto.setEndDate(pro.getEndDate());
			if (pro.getCreateDate() != null) {
				proDto.setCreateDate(pro.getCreateDate().toString());
			}
			List<FileStorageResponseDto> fileStorageResponseDtos = new ArrayList<>();
			for (FileStorageEntity file : pro.getFileStorages()) {
				FileStorageResponseDto fileStorageResponseDto = new FileStorageResponseDto();
				fileStorageResponseDto.setId(file.getId());
				fileStorageResponseDto.setName(file.getName());
				fileStorageResponseDto.setCreateDate(file.getCreateDate());
				fileStorageResponseDto.setType(file.getType());
				fileStorageResponseDto.setData(new String(file.getData()));
				fileStorageResponseDtos.add(fileStorageResponseDto);
			}
			proDto.setFileStorages(fileStorageResponseDtos);
			progressDto.add(proDto);
		}
		return progressDto;
	}
}
