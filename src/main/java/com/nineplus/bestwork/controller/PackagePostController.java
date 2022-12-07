package com.nineplus.bestwork.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.PackageFileDownLoadReqDto;
import com.nineplus.bestwork.dto.PackagePostReqDto;
import com.nineplus.bestwork.dto.PackagePostResDto;
import com.nineplus.bestwork.dto.PostCommentReqDto;
import com.nineplus.bestwork.entity.PackagePost;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IPackagePostService;
import com.nineplus.bestwork.utils.CommonConstants;

@RestController
@RequestMapping("/api/v1/packages")
public class PackagePostController extends BaseController {

	@Autowired
	IPackagePostService iPackagePostService;

	@PatchMapping("/update-package/{awbId}")
	public ResponseEntity<? extends Object> update(@RequestParam("file") List<MultipartFile> mFiles,
			@RequestParam("packageDescription") String packageDes, @PathVariable long awbId)
			throws BestWorkBussinessException {
		try {
			PackagePostReqDto packagePostReqDto = new PackagePostReqDto();
			if (StringUtils.isNotBlank(packageDes)) {
				packagePostReqDto.setDescription(packageDes);
			}
			iPackagePostService.updatePackagePost(mFiles, packagePostReqDto, awbId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sP0001, null, null);
	}

	@GetMapping("/detail/{packagePostId}")
	public ResponseEntity<? extends Object> getDetailPackage(@PathVariable long packagePostId)
			throws BestWorkBussinessException {
		PackagePostResDto packagePostResDto = null;
		try {
			packagePostResDto = iPackagePostService.getDetailPackage(packagePostId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(packagePostResDto)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sP0002, packagePostResDto, null);
	}

	@GetMapping("/list/by/{airWayBillId}")
	public ResponseEntity<? extends Object> getAllPackagePost(@PathVariable String airWayBillId)
			throws BestWorkBussinessException {
		List<PackagePostResDto> listPackagePostResDto = null;
		try {
			listPackagePostResDto = iPackagePostService.getAllPackagePost(airWayBillId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(listPackagePostResDto)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sP0003, listPackagePostResDto, null);
	}

	@GetMapping("/get-file")
	public ResponseEntity<? extends Object> getFile(@RequestBody PackageFileDownLoadReqDto packageFileDownLoadReqDto)
			throws BestWorkBussinessException {
		byte[] dataBytesFile = null;
		String pathFile = "";
		try {
			dataBytesFile = iPackagePostService.getFile(packageFileDownLoadReqDto.getPackagePostId(),
					packageFileDownLoadReqDto.getFileId());
			pathFile = iPackagePostService.getPathFileToDownload(packageFileDownLoadReqDto.getPackagePostId(),
					packageFileDownLoadReqDto.getFileId());
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(dataBytesFile)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return ResponseEntity.ok()
				// Content-Disposition
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstants.MediaType.CONTENT_DISPOSITION + pathFile)
				// Content-Type
				.contentType(MediaType.parseMediaType(CommonConstants.MediaType.MEDIA_TYPE_STREAM)).body(dataBytesFile);
	}

	@PostMapping("/view-file-pdf")
	public ResponseEntity<? extends Object> viewFilePdf(
			@RequestBody PackageFileDownLoadReqDto packageFileDownLoadReqDto) throws BestWorkBussinessException {
		byte[] dataBytesFile = null;
		String pathFile = "";
		try {
			dataBytesFile = iPackagePostService.getFile(packageFileDownLoadReqDto.getPackagePostId(),
					packageFileDownLoadReqDto.getFileId());
			pathFile = iPackagePostService.getPathFileToDownload(packageFileDownLoadReqDto.getPackagePostId(),
					packageFileDownLoadReqDto.getFileId());
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(dataBytesFile)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return ResponseEntity.ok()
				// Content-Disposition
				.header(HttpHeaders.CONTENT_DISPOSITION, CommonConstants.MediaType.CONTENT_DISPOSITION + pathFile)
				// Content-Type
				.contentType(MediaType.parseMediaType(CommonConstants.MediaType.MEDIA_TYPE_PDF))
				.body(Arrays.toString(dataBytesFile));
	}

	@PatchMapping("/{postPackageId}/comment")
	public ResponseEntity<? extends Object> addComment(@PathVariable Long postPackageId,
			@RequestBody PostCommentReqDto postCommentRequestDto) throws BestWorkBussinessException {
		PackagePost packagePost = null;
		try {
			packagePost = iPackagePostService.pushComment(postPackageId, postCommentRequestDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sI0004, packagePost, null);
	}

}
