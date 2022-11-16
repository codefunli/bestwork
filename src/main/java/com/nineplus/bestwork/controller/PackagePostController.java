package com.nineplus.bestwork.controller;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.PackagePostReqDto;
import com.nineplus.bestwork.dto.PackagePostResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IPackagePostService;
import com.nineplus.bestwork.utils.CommonConstants;

@RestController
@RequestMapping("/api/v1/packages")
public class PackagePostController extends BaseController {

	@Autowired
	IPackagePostService iPackagePostService;

	@PatchMapping("/update-package/{airWayBillCode}")
	public ResponseEntity<? extends Object> update(@RequestParam("file") List<MultipartFile> mFiles,
			@RequestParam("packageDescription") String packageDes,
			@PathVariable String airWayBillCode) throws BestWorkBussinessException {
		try {
			PackagePostReqDto packagePostReqDto = new PackagePostReqDto();
			if (StringUtils.isNotBlank(packageDes)) {
				packagePostReqDto.setDescription(packageDes);
			}
			iPackagePostService.updatePackagePost(mFiles, packagePostReqDto, airWayBillCode);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sP0001, null, null);
	}
	
	@GetMapping("/detail/{packagePostId}")
	public ResponseEntity<? extends Object> getDetailPackage(@PathVariable long packagePostId)
			throws BestWorkBussinessException {
		PackagePostResDto packagePostResDto  = null;
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
		List<PackagePostResDto> listPackagePostResDto  = null;
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
	
	@GetMapping("/get-file/by")
	public ResponseEntity<? extends Object> getFile(@RequestParam(value = "packagePostId", required = true) Long packagePostId,
			@RequestParam(value = "fileId", required = false) Long fileId) throws BestWorkBussinessException {
		byte[] dataBytesFile = null;
		try {
			dataBytesFile = iPackagePostService.getFile(packagePostId, fileId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(dataBytesFile)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sF0002, dataBytesFile, null);
	}
	
}
