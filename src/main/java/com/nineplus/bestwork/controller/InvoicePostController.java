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

import com.nineplus.bestwork.dto.InvoiceFileDownLoadReqDto;
import com.nineplus.bestwork.dto.PostCommentReqDto;
import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.dto.PostInvoiceResDto;
import com.nineplus.bestwork.entity.PostInvoice;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IAirWayBillService;
import com.nineplus.bestwork.services.IInvoicePostService;
import com.nineplus.bestwork.utils.CommonConstants;

/**
 * 
 * @author TuanNA
 *
 */
@RestController
@RequestMapping("/api/v1/invoices")
public class InvoicePostController extends BaseController {

	@Autowired
	IInvoicePostService iPostInvoiceService;
	
	@PatchMapping("/update-invoice/{awbId}")
	public ResponseEntity<? extends Object> update(@RequestParam("file") List<MultipartFile> mFiles,
			@RequestParam("invoiceDescription") String invoiceDes, @PathVariable long awbId)
			throws BestWorkBussinessException {
		try {
			PostInvoiceReqDto postInvoiceReqDto = new PostInvoiceReqDto();
			if (StringUtils.isNotBlank(invoiceDes)) {
				postInvoiceReqDto.setDescription(invoiceDes);
			}
			iPostInvoiceService.updatePostInvoice(mFiles, postInvoiceReqDto, awbId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sI0001, null, null);
	}

	@GetMapping("/detail/{invoicePostId}")
	public ResponseEntity<? extends Object> getDetailInvoice(@PathVariable long invoicePostId)
			throws BestWorkBussinessException {
		PostInvoiceResDto postInvoiceResDto = null;
		try {
			postInvoiceResDto = iPostInvoiceService.getDetailInvoice(invoicePostId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(postInvoiceResDto)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sI0002, postInvoiceResDto, null);
	}

	@GetMapping("/list/by/{awbId}")
	public ResponseEntity<? extends Object> getAllPackagePost(@PathVariable long awbId)
			throws BestWorkBussinessException {
		List<PostInvoiceResDto> listPostInvoiceResDto = null;
		try {
			listPostInvoiceResDto = iPostInvoiceService.getAllInvoicePost(awbId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(listPostInvoiceResDto)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sI0003, listPostInvoiceResDto, null);
	}

	@GetMapping("/get-file")
	public ResponseEntity<? extends Object> getFile(@RequestBody InvoiceFileDownLoadReqDto invoiceFileDownLoadReqDto)
			throws BestWorkBussinessException {
		byte[] dataBytesFile = null;
		String pathFile = "";
		try {
			dataBytesFile = iPostInvoiceService.getFile(invoiceFileDownLoadReqDto.getInvoicePostId(),
					invoiceFileDownLoadReqDto.getFileId());
			pathFile = iPostInvoiceService.getPathFileToDownload(invoiceFileDownLoadReqDto.getInvoicePostId(),
					invoiceFileDownLoadReqDto.getFileId());
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
			@RequestBody InvoiceFileDownLoadReqDto invoiceFileDownLoadReqDto) throws BestWorkBussinessException {
		byte[] dataBytesFile = null;
		String pathFile = "";
		try {
			dataBytesFile = iPostInvoiceService.getFile(invoiceFileDownLoadReqDto.getInvoicePostId(),
					invoiceFileDownLoadReqDto.getFileId());
			pathFile = iPostInvoiceService.getPathFileToDownload(invoiceFileDownLoadReqDto.getInvoicePostId(),
					invoiceFileDownLoadReqDto.getFileId());
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

	@PatchMapping("/{postInvoiceId}/comment")
	public ResponseEntity<? extends Object> addComment(@PathVariable Long postInvoiceId,
			@RequestBody PostCommentReqDto postCommentRequestDto) throws BestWorkBussinessException {
		PostInvoice postInvoice =  null;
		try {
			postInvoice = iPostInvoiceService.pushComment(postInvoiceId, postCommentRequestDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sI0004, postInvoice, null);
	}

}
