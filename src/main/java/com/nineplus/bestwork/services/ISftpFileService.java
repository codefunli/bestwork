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

	String uploadInvoice(MultipartFile file, long awbId, long invoiceId) throws BestWorkBussinessException;

	String uploadPackage(MultipartFile file, long awbId, long packageId) throws BestWorkBussinessException;

	String uploadEvidenceBefore(MultipartFile file, long awbId, long evidenceBeforeId) throws BestWorkBussinessException;

	String uploadEvidenceAfter(MultipartFile file, long awbId, long evidenceAfterId) throws BestWorkBussinessException;

	boolean isValidFile(List<MultipartFile> file);

	boolean isImageFile(List<MultipartFile> file);

	String uploadConstructionDrawing(MultipartFile file, long constructionId);

	List<String> downloadFileTemp(long awbId, List<String> listPathFileDownload);

	String uploadProgressImage(MultipartFile file, long progressId, int type);
	
	boolean removeFile(String pathFileServer);

}
