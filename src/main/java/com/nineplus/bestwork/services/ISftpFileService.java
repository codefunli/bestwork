package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.ChannelSftp;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface ISftpFileService {
	boolean isExistFolder(ChannelSftp channel, String path);

	String createFolder(ChannelSftp channel, String path);

	void createFolderCommonRoot(List<String> folderStrings);

	byte[] getFile(String pathFileDownload) throws BestWorkBussinessException;

	String uploadInvoice(MultipartFile file, String airWayBill, long invoiceId);

	String uploadPackage(MultipartFile file, String airWayBill, long packageId);

	String uploadEvidenceBefore(MultipartFile file, String airWayBill, long evidenceBeforeId);

	String uploadEvidenceAfter(MultipartFile file, String airWayBill, long evidenceAfterId);

	boolean isValidFile(List<MultipartFile> file);

	boolean isImageFile(List<MultipartFile> file);

	String uploadConstructionDrawing(MultipartFile file, long constructionId);

	void createZipFolder(String airWayBillCode, String[] listPathFileDownload);

	List<String> downloadFileTemp(String airWayBillCode, List<String> listPathFileDownload);

	String uploadProgressImage(MultipartFile file, long progressId);
	
	boolean removeFile(String pathFileServer);

}
