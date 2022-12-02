package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.FileStorageResDto;
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
import com.nineplus.bestwork.services.ISftpFileService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.DateUtils;
import com.nineplus.bestwork.utils.Enums.FolderType;
import com.nineplus.bestwork.utils.UserAuthUtils;

/**
 * 
 * @author TuanNA
 *
 */
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

	@Autowired
	private ISftpFileService sftpService;

	@Override
	@Transactional
	public void registProgress(ProgressReqDto progressReqDto, List<MultipartFile> files)
			throws BestWorkBussinessException {
		this.saveProgress(progressReqDto, files, null, false);
	}

	@Override
	@Transactional
	public void updateProgress(ProgressReqDto progressReqDto, List<MultipartFile> files, Long progressId)
			throws BestWorkBussinessException {
		ProgressEntity currentProgress = progressRepo.findById(progressId).orElse(null);
		this.saveProgress(progressReqDto, files, currentProgress, true);

	}

	public void saveProgress(ProgressReqDto progressReqDto, List<MultipartFile> files, ProgressEntity progress,
			boolean isEdit) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		this.chkCurUserCanCrtUpdPrg(userAuthRoleReq, progressReqDto.getConstructionId(), isEdit);
		if (isEdit && !progress.getCreateBy().equals(userAuthRoleReq.getUsername())) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		String createUser = userAuthRoleReq.getUsername();
		try {
			if (progress == null) {
				progress = new ProgressEntity();
			}
			progress.setTitle(progressReqDto.getTitle());
			progress.setStatus(progressReqDto.getStatus());
			progress.setReport(progressReqDto.getReport());
			progress.setNote(progressReqDto.getNote());
			progress.setStartDate(progressReqDto.getStartDate());
			progress.setEndDate(progressReqDto.getEndDate());
			progress.setCreateDate(LocalDateTime.now());
			if (!isEdit) {
				long constructionId = Long.valueOf(progressReqDto.getConstructionId());
				progress.setConstruction(cstrtService.findCstrtById(constructionId));
				progress.setCreateBy(createUser);
			} else {
				progress.setUpdateBy(createUser);
			}
			progressRepo.save(progress);
			saveImage(files, progress);

		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
		}

	}

	private void chkCurUserCanCrtUpdPrg(UserAuthDetected userAuthRoleReq, long cstrtId, boolean isEdit)
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

	public void saveImage(List<MultipartFile> files, ProgressEntity progress) {
		List<String> listPath = this.storageService.getPathFileByProgressId(progress.getId());
		for (String path : listPath) {
			this.sftpService.removeFile(path);
		}

		this.storageService.deleteByProgressId(progress.getId());

		for (MultipartFile file : files) {
			String pathFile = this.sftpService.uploadProgressImage(file, progress.getId());
			storageService.storeFile(progress.getId(), FolderType.PROGRESS, pathFile);
		}
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
				this.chkCurUserCanCrtUpdPrg(userAuthRoleReq, cstrtId, true);
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
				fileDto.setProgressId(file.getProgressId());
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

	@Override
	public List<ProgressResDto> getProgressByConstruction(String constructionId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		this.chkCurUserCanViewPrg(userAuthRoleReq, Long.valueOf(constructionId));
		ConstructionEntity curCstrt = this.cstrtService.findCstrtById(Long.valueOf(constructionId));
		if (ObjectUtils.isEmpty(curCstrt)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECS0007, null);
		}
		List<ProgressResDto> progressDtoList = new ArrayList<ProgressResDto>();
		List<ProgressEntity> progressLst = progressRepo.findProgressByCstrtId(Long.valueOf(constructionId));
		if (ObjectUtils.isNotEmpty(progressLst)) {
			for (ProgressEntity prog : progressLst) {
				ProgressResDto progressDto = new ProgressResDto();
				progressDto.setId(prog.getId());
				progressDto.setTitle(prog.getTitle());
				progressDto.setStatus(prog.getStatus());
				progressDto.setNote(prog.getNote());
				progressDto.setReport(prog.getReport());
				progressDto.setCreateBy(prog.getCreateBy());
				progressDto.setStartDate(prog.getStartDate());
				progressDto.setEndDate(prog.getEndDate());
				progressDto.setCreateDate(LocalDateTime.now().toString());
				List<FileStorageResDto> lstFileDto = new ArrayList<>();
				List<FileStorageEntity> fileStorages = prog.getFileStorages();
				for (FileStorageEntity file : fileStorages) {
					FileStorageResDto fileDto = new FileStorageResDto();
					fileDto.setProgressId(file.getProgressId());
					fileDto.setId(file.getId());
					fileDto.setName(file.getName());
					fileDto.setType(file.getType());
					fileDto.setData(file.getData() != null ? new String(file.getData()) : null);
					fileDto.setCreateDate(file.getCreateDate().toString());
					// return content file if file is image
					if (Arrays.asList(CommonConstants.Image.IMAGE_EXTENSION).contains(file.getType())) {
						String pathServer = file.getPathFileServer();
						byte[] imageContent = sftpService.getFile(pathServer);
						fileDto.setContent(imageContent);
					}
					lstFileDto.add(fileDto);
				}
				progressDto.setFileStorages(lstFileDto);
				progressDtoList.add(progressDto);
			}
		}
		return progressDtoList;
	}
}
