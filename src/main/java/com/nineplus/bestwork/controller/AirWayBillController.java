package com.nineplus.bestwork.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.AirWayBillReqDto;
import com.nineplus.bestwork.dto.AirWayBillResDto;
import com.nineplus.bestwork.dto.AirWayBillStatusReqDto;
import com.nineplus.bestwork.dto.AirWayBillStatusResDto;
import com.nineplus.bestwork.dto.ChangeStatusFileDto;
import com.nineplus.bestwork.dto.CustomClearanceResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IAirWayBillService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.AirWayBillStatus;

@RestController
@RequestMapping("/api/v1/airway-bill")
public class AirWayBillController extends BaseController {

	@Autowired
	IAirWayBillService iAirWayBillService;

	@Autowired
	IStorageService iStorageService;

	private final String ZIP_EXTENSION = ".zip";

	@PostMapping("/create")
	public ResponseEntity<? extends Object> create(@RequestBody AirWayBillReqDto airWayBillReqDto)
			throws BestWorkBussinessException {
		try {
			iAirWayBillService.saveAirWayBill(airWayBillReqDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sA0001, null, null);
	}

	@GetMapping("/status")
	public ResponseEntity<? extends Object> getAirWayBillStatus() throws BestWorkBussinessException {
		List<AirWayBillStatusResDto> airWayBillStatus = new ArrayList<>();
		for (AirWayBillStatus status : AirWayBillStatus.values()) {
			AirWayBillStatusResDto dto = new AirWayBillStatusResDto();
			dto.setId(status.getStatus());
			dto.setStatus(status.getValue());
			airWayBillStatus.add(dto);
		}
		return success(CommonConstants.MessageCode.sA0002, airWayBillStatus, null);
	}

	@GetMapping("/list/by/{projectId}")
	public ResponseEntity<? extends Object> getAllAirWayBill(@PathVariable String projectId)
			throws BestWorkBussinessException {
		List<AirWayBillResDto> listAwb = null;
		try {
			listAwb = iAirWayBillService.getAllAirWayBillByProject(projectId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(listAwb)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sA0003, listAwb, null);
	}

	@PostMapping("/change-status-file")
	public ResponseEntity<? extends Object> changeStatus(@RequestBody ChangeStatusFileDto changeStatusFileDto)
			throws BestWorkBussinessException {
		try {
			iStorageService.changeStatusFile(changeStatusFileDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sF0003, null, null);
	}

	@GetMapping("/{awbId}/get-custom-clearance-doc")
	public ResponseEntity<? extends Object> getCustomClearanceDoc(@PathVariable long awbId)
			throws BestWorkBussinessException {
		CustomClearanceResDto customClearanceResDto = null;
		try {
			customClearanceResDto = iAirWayBillService.getCustomClearanceDoc(awbId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}

		if (ObjectUtils.isEmpty(customClearanceResDto.getInvoicesDoc())
				&& ObjectUtils.isEmpty(customClearanceResDto.getPackagesDoc())
				&& ObjectUtils.isEmpty(customClearanceResDto.getImageBeforeDoc())) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sA0005, customClearanceResDto, null);
	}

	@PostMapping("/{awbId}/change-status")
	public ResponseEntity<? extends Object> confirmDone(@PathVariable long awbId,
			@RequestBody AirWayBillStatusReqDto statusDto) throws BestWorkBussinessException {
		try {
			if (ObjectUtils.isNotEmpty(statusDto.getDestinationStatus())) {
				iAirWayBillService.changeStatus(awbId, statusDto.getDestinationStatus());
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sA0006, null, null);
	}
	
	@PutMapping("/{awbId}/edit")
	public ResponseEntity<? extends Object> updateAirWayBill(@PathVariable long awbId,
			@RequestBody AirWayBillReqDto airWayBillReqDto ) throws BestWorkBussinessException {
		try {
			if (ObjectUtils.isNotEmpty(airWayBillReqDto)) {
				iAirWayBillService.updateAirWayBill(awbId,airWayBillReqDto);
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sA0007, null, null);
	}

	@GetMapping(value = "/{awbId}/download-clearance-doc")
	public ResponseEntity<? extends Object> downloadZip(HttpServletResponse response, @PathVariable long awbId)
			throws BestWorkBussinessException {
		List<String> listFile = iAirWayBillService.createZipFolder(awbId);
		if (ObjectUtils.isEmpty(listFile)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		} else {
			String pathFolder = "";
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ZipOutputStream zipOutputStream = new ZipOutputStream(bos);
			ArrayList<File> files = new ArrayList<>();
			for (String path : listFile) {
				files.add(new File(path));
			}
			// package files
			for (File file : files) {
				pathFolder = FilenameUtils.getFullPathNoEndSeparator(file.getAbsolutePath());
				try {
					zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
					FileInputStream fileInputStream = new FileInputStream(file);

					IOUtils.copy(fileInputStream, zipOutputStream);

					fileInputStream.close();
					zipOutputStream.closeEntry();

					// delete temporary file
					file.delete();
				} catch (Exception e) {
					throw new BestWorkBussinessException(CommonConstants.MessageCode.eF0003, null);
				}
			}
			try {
				// Delete folder temporary
				FileUtils.deleteDirectory(new File(pathFolder));
				zipOutputStream.close();
			} catch (IOException e) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.eF0003, null);
			}

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							CommonConstants.MediaType.CONTENT_DISPOSITION
									+ awbId + ZIP_EXTENSION)
					.body(Arrays.toString(new ByteArrayInputStream(bos.toByteArray()).readAllBytes()));
		}
	}
}