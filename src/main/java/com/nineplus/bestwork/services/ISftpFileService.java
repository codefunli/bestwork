package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.ChannelSftp;

public interface ISftpFileService {
	boolean isExistFolder(ChannelSftp channel, String path);

	String createFolder(ChannelSftp channel, String path);

	void createFolderCommonRoot(List<String> folderStrings);

	byte[] downloadFile(String pathFileDownload);

	byte[] viewFilePdf(String pathFileView);

	String uploadInvoice(MultipartFile file, String airWayBill, long invoiceId);

	String uploadPackage(MultipartFile file, String airWayBill, long packageId);

	String uploadEvidenceBefore(MultipartFile file, String airWayBill, long evidenceBeforeId);

	String uploadEvidenceAfter(MultipartFile file, String airWayBill, long evidenceAfterId);
	
	boolean isValidFile(List<MultipartFile> file);

}
