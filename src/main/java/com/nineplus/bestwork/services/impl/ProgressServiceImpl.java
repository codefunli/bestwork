package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.ConstructionResDto;
import com.nineplus.bestwork.dto.FileStorageReqDto;
import com.nineplus.bestwork.dto.FileStorageResDto;
import com.nineplus.bestwork.dto.ProgressAndConstructionResDto;
import com.nineplus.bestwork.dto.ProgressReqDto;
import com.nineplus.bestwork.dto.ProgressResDto;
import com.nineplus.bestwork.entity.ConstructionEntity;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.ProgressEntity;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.ProgressRepository;
import com.nineplus.bestwork.repository.ProjectRepository;
import com.nineplus.bestwork.repository.StorageRepository;
import com.nineplus.bestwork.services.IConstructionService;
import com.nineplus.bestwork.services.IProgressService;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.DateUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class ProgressServiceImpl implements IProgressService {
	@Autowired
	private ProgressRepository progressRepo;

	@Autowired
	private ProjectRepository projectRepo;

	@Autowired
	private StorageRepository storageRepo;

	@Autowired
	DateUtils dateUtils;

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private IStorageService storageService;

	@Autowired
	private IConstructionService cstrtService;

	@Autowired
	private IProjectService projectService;

	@Override
	public void registProgress(ProgressReqDto progressReqDto) throws BestWorkBussinessException {
		this.saveProgress(progressReqDto, null, false);

	}

	@Override
	public void updateProgress(ProgressReqDto progressReqDto, Long progressId) throws BestWorkBussinessException {
		ProgressEntity currentProgress = progressRepo.findById(progressId).orElse(null);
		this.saveProgress(progressReqDto, currentProgress, true);
	}

	public void saveProgress(ProgressReqDto progressReqDto, ProgressEntity progress, boolean isEdit)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		this.chkCurUserCanCrtUpdPrg(userAuthRoleReq, progressReqDto.getConstructionId());
		String createUser = userAuthRoleReq.getUsername();
		try {
			if (progress == null) {
				progress = new ProgressEntity();
			}
			progress.setTitle(progressReqDto.getTitle());
			progress.setStatus(progressReqDto.getStatus());
			progress.setReport(progressReqDto.getReport());
			progress.setNote(progressReqDto.getNote());
			String startDt = dateUtils.convertToUTC(progressReqDto.getStartDate());
			String endDt = dateUtils.convertToUTC(progressReqDto.getEndDate());
			progress.setStartDate(startDt);
			progress.setEndDate(endDt);
			progress.setCreateDate(LocalDateTime.now());
			if (!isEdit) {
				long constructionId = Long.valueOf(progressReqDto.getConstructionId());
				progress.setConstruction(cstrtService.findCstrtById(constructionId));
				progress.setCreateBy(createUser);
			} else {
				progress.setUpdateBy(createUser);
			}
			progressRepo.save(progress);

			saveImage(progressReqDto.getFileStorages(), progress);

		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
		}

	}

	private void chkCurUserCanCrtUpdPrg(UserAuthDetected userAuthRoleReq, long cstrtId)
			throws BestWorkBussinessException {
		ConstructionEntity curCstrt = this.cstrtService.findCstrtById(cstrtId);
		if (curCstrt == null) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECS0007, null);
		}
		ProjectEntity curPrj = this.projectRepo.findByConstructionId(cstrtId);
		if (curPrj == null) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.S1X0002, null);
		}
		if (!this.cstrtService.chkCurUserCanCreateCstrt(userAuthRoleReq, curPrj.getId())) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
	}

	public void saveImage(List<FileStorageReqDto> fileStorages, ProgressEntity progress) {
		List<Long> listIdCurrent = storageRepo.getListIdFileByProgress(progress.getId());
		List<Long> listIdUpdate = new ArrayList<>();
		for (FileStorageReqDto file : fileStorages) {
			listIdUpdate.add(file.getId());
		}
		// Get list Id image that will be removed
		List<Long> listRemoveId = listIdCurrent.stream().filter(e -> !listIdUpdate.contains(e))
				.collect(Collectors.toList());

		// Get list Id image that will be kept
		List<Long> listKeepId = listIdCurrent.stream().filter(e -> listIdUpdate.contains(e))
				.collect(Collectors.toList());

		// Delete image have in DB but not have in request
		if (listRemoveId != null && listRemoveId.size() > 0) {
			storageRepo.deleteByIdIn(listRemoveId);
		}

		fileStorages.removeIf(x -> listKeepId.contains(x.getId()));

		for (FileStorageReqDto file : fileStorages) {
			storageService.storeFileProgress(file, progress);
		}
	}

	@Override
	public List<ProgressResDto> getProgressByConstructionId(Long cstrtId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		this.chkCurUserCanViewPrg(userAuthRoleReq, cstrtId);

		List<ProgressEntity> progress = progressRepo.findProgressByCstrtId(cstrtId);
		List<ProgressResDto> progressDto = new ArrayList<>();
		for (ProgressEntity pro : progress) {
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
			List<FileStorageResDto> fsResDtos = new ArrayList<>();
			for (FileStorageEntity file : pro.getFileStorages()) {
				FileStorageResDto dto = new FileStorageResDto();
				dto.setId(file.getId());
				dto.setName(file.getName());
				dto.setCreateDate(file.getCreateDate().toString());
				dto.setType(file.getType());
				dto.setData(new String(file.getData()));
				fsResDtos.add(dto);
			}
			proDto.setFileStorages(fsResDtos);
			progressDto.add(proDto);
		}
		return progressDto;
	}

	private void chkCurUserCanViewPrg(UserAuthDetected userAuthDetected, long cstrtId)
			throws BestWorkBussinessException {
		List<ProjectEntity> prjLstCurUserCanView = this.projectService.getPrjLstByAnyUsername(userAuthDetected);
		ProjectEntity curPrj = this.projectService.getPrjByCstrtId(cstrtId);
		if (!prjLstCurUserCanView.contains(curPrj)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public void deleteProgressList(List<Long> ids) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		try {
			for (Long cstrtId : ids) {
				this.chkCurUserCanCrtUpdPrg(userAuthRoleReq, cstrtId);
			}
			progressRepo.delProgressWithId(ids);
			List<FileStorageEntity> allFiles = storageRepo.findAllByPrgListId(ids);
			if (allFiles != null) {
				storageRepo.deleteAllInBatch(allFiles);
			}
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ePu0001, null);
		}
	}

	@Override
	public ProgressResDto getProgressById(Long progressId) throws BestWorkBussinessException {

		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		ConstructionEntity curCstrt = this.cstrtService.findCstrtByPrgId(progressId);
		this.chkCurUserCanViewPrg(userAuthRoleReq, curCstrt.getId());

		ProgressEntity progress = progressRepo.findById(Long.valueOf(progressId)).orElse(null);
		ProgressResDto progressDto = null;
		if (progress != null) {
			progressDto = new ProgressResDto();
			List<FileStorageResDto> lstfileDto = new ArrayList<>();
			progressDto.setId(progress.getId());
			progressDto.setTitle(progress.getTitle());
			progressDto.setStatus(progress.getStatus());
			progressDto.setNote(progress.getNote());
			progressDto.setReport(progress.getReport());
			progressDto.setCreateBy(progress.getCreateBy());
			progressDto.setStartDate(progress.getStartDate());
			progressDto.setEndDate(progress.getEndDate());

			List<FileStorageEntity> fileStorage = progress.getFileStorages();
			for (FileStorageEntity file : fileStorage) {
				// Dto for response file storage
				FileStorageResDto fileDto = new FileStorageResDto();
				fileDto.setProgressId(file.getProgress().getId());
				fileDto.setId(file.getId());
				fileDto.setName(file.getName());
				fileDto.setType(file.getType());
				fileDto.setData(new String(file.getData()));
				fileDto.setCreateDate(file.getCreateDate().toString());
				lstfileDto.add(fileDto);
			}
			progressDto.setFileStorages(lstfileDto);
		}
		return progressDto;
	}

//	@Override
//	public List<Long> getAllProgressByProject(List<String> listProjectId) {
//		List<Long> listProgressId = null;
//		if (listProjectId != null) {
//			listProgressId = progressRepository.getAllProgressByProject(listProjectId);
//		}
//		return listProgressId;
//	}

	@Override
	public ProgressAndConstructionResDto getProgressByConstruction(String constructionId)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		this.chkCurUserCanViewPrg(userAuthRoleReq, Long.valueOf(constructionId));

		ProgressAndConstructionResDto dto = new ProgressAndConstructionResDto();
		List<ProgressEntity> progress = progressRepo.findProgressByCstrtId(Long.valueOf(constructionId));
		List<ProgressResDto> progressDtoList = new ArrayList<ProgressResDto>();
		if (progress != null) {
			for (ProgressEntity prog : progress) {
				ProgressResDto progressDto = new ProgressResDto();
				List<FileStorageResDto> lstFileDto = new ArrayList<>();
				progressDto.setId(prog.getId());
				progressDto.setTitle(prog.getTitle());
				progressDto.setStatus(prog.getStatus());
				progressDto.setNote(prog.getNote());
				progressDto.setReport(prog.getReport());
				progressDto.setCreateBy(prog.getCreateBy());
				progressDto.setStartDate(prog.getStartDate());
				progressDto.setEndDate(prog.getEndDate());
				progressDto.setCreateDate(LocalDateTime.now().toString());
				List<FileStorageEntity> fileStorages = prog.getFileStorages();
				for (FileStorageEntity file : fileStorages) {
					FileStorageResDto fileDto = new FileStorageResDto();
					fileDto.setProgressId(file.getProgress().getId());
					fileDto.setId(file.getId());
					fileDto.setName(file.getName());
					fileDto.setType(file.getType());
					fileDto.setData(new String(file.getData()));
					fileDto.setCreateDate(file.getCreateDate().toString());
					lstFileDto.add(fileDto);
				}
				progressDto.setFileStorages(lstFileDto);
				progressDtoList.add(progressDto);
			}
			dto.setConstruction(modelMapper.map(progress.get(0).getConstruction(), ConstructionResDto.class));
			dto.setProgress(progressDtoList);
		}
		return dto;
	}
}
