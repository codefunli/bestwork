package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.ChannelSftp;

public interface SftpFileService {
	boolean isExistFolder(ChannelSftp channel, String path);

	boolean createFolder(ChannelSftp channel, String path);

	void createFolderCommonRoot(List<String> folderStrings);

	byte[] downloadFile(String pathFileDownload, int typeFile);

	byte[] viewFilePdf(String pathFileView);

	String uploadInvoice(MultipartFile file, String airWayBill);

	String uploadPackage(MultipartFile file, String airWayBill);

	String uploadEvidenceBefore(MultipartFile file, String airWayBill);

	String uploadEvidenceAfter(MultipartFile file, String airWayBill);

}
