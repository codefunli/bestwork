package com.nineplus.bestwork.services.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.jcraft.jsch.Session;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.nineplus.bestwork.exception.FileHandleException;
import com.nineplus.bestwork.services.SftpFileService;
import com.nineplus.bestwork.utils.DateUtils;
import com.nineplus.bestwork.utils.Enums.FolderType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SftpFileServiceImpl implements SftpFileService {

	/**
	 * The Constant SEPARATOR.
	 */
	public static final String SEPARATOR = "/";

	/**
	 * The Constant ROOT_PATH.
	 */
	public static final String ROOT_PATH = "/home/centos/";

	/**
	 * The Constant HOST.
	 */
	public static final String SFTP_HOST = "localhost";

	/**
	 * The Constant PORT.
	 */
	public static final int SFTP_PORT = 222;

	/**
	 * The Constant USER.
	 */
	public static final String SFTP_USER = "centos";

	/**
	 * The Constant PASSWORD.
	 */
	public static final String SFTP_PASSWORD = "centos";

	private static final int INVOICE_NUMBER = 1;

	private static final int PACKAGE_NUMBER = 2;

	private static final int EVIDENCE_BEFORE_NUMBER = 3;

	private static final int EVIDENCE_AFTER_NUMBER = 4;

	public static final String INVOICE_PATH = "invoicePath";

	public static final String PACKAGE_PATH = "packagePath";

	public static final String EVIDENCE_BEFORE_PATH = "evidenceBeforePath";

	public static final String EVIDENCE_AFTER_PATH = "evidenceAfterPath";

	@Override
	public boolean isExistFolder(ChannelSftp channel, String path) {
		try {
			channel.lstat(path);
			return true;
		} catch (SftpException ex) {
			return false;
		}
	}

	@Override
	public boolean createFolder(ChannelSftp channel, String path) {
		try {
			channel.mkdir(path);
			return true;
		} catch (SftpException ex) {
			throw new FileHandleException(ex.getMessage(), ex);
		}
	}

	@Override
	public void createFolderCommonRoot(List<String> folderStrings) {
		Session session = null;
		ChannelSftp channel = null;

		try {
			Pair<Session, ChannelSftp> sftpConnection = this.getConnection();
			session = sftpConnection.getFirst();
			channel = sftpConnection.getSecond();

			for (String folderStr : folderStrings) {
				if (!isExistFolder(channel, folderStr)) {
					createFolder(channel, folderStr);
				}
			}

			disconnect(session, channel);
		} catch (FileHandleException e) {
			disconnect(session, channel);
			throw new FileHandleException(e.getMessage(), e);
		}

	}

	@Override
	public byte[] downloadFile(String pathFileDownload, int typeFile) {
		byte[] resBytes = null;
		ChannelSftp channel = null;
		String fileName = null;
		Session session = null;
		try {

			if (getFolderType(typeFile) == FolderType.DEFAULT) {
				return new byte[0];
			}

			Pair<Session, ChannelSftp> sftpConnection = this.getConnection();

			session = sftpConnection.getFirst();
			channel = sftpConnection.getSecond();
			fileName = FilenameUtils.getName(pathFileDownload);

			String folderName = FilenameUtils.getPath(pathFileDownload);
			String filePath = new StringBuilder(getParentPath(getFolderType(typeFile))).append(SEPARATOR)
					.append(folderName).append(fileName).toString();

			resBytes = IOUtils.toByteArray(channel.get(filePath));

			// disconnect to sftp server.
			disconnect(session, channel);
		} catch (SftpException | IOException e) {
			// disconnect to sftp server.
			disconnect(session, channel);
			throw new FileHandleException(e.getMessage(), e);
		}

		return resBytes;
	}

	@Override
	public byte[] viewFilePdf(String pathFileView) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uploadInvoice(MultipartFile file, String airWayBill) {
		return upload(file, FolderType.INVOICE, airWayBill);
	}

	@Override
	public String uploadPackage(MultipartFile file, String airWayBill) {
		return upload(file, FolderType.PACKAGE, airWayBill);
	}

	@Override
	public String uploadEvidenceBefore(MultipartFile file, String airWayBill) {
		return upload(file, FolderType.EVIDENCE_BEFORE, airWayBill);
	}

	@Override
	public String uploadEvidenceAfter(MultipartFile file, String airWayBill) {
		return upload(file, FolderType.EVIDENCE_AFTER, airWayBill);
	}

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	private Pair<Session, ChannelSftp> getConnection() {
		try {

			JSch jsch = new JSch();
			Session session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(SFTP_PASSWORD);
			log.info("Connecting to session server SFTP");
			session.connect();

			ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			log.info("Connecting to channel server SFTP");
			return Pair.of(session, channel);
		} catch (JSchException e) {
			throw new FileHandleException(e.getMessage(), e);
		}
	}

	/**
	 * Disconnect.
	 *
	 * @param session the session
	 * @param channel the channel
	 */
	private static void disconnect(Session session, ChannelSftp channel) {
		if (channel != null) {
			channel.exit();
		}

		if (session != null) {
			session.disconnect();
		}
	}

	private String upload(MultipartFile mfile, FolderType folderType, String airWayBill) {
		String absolutePath = null;
		Session session = null;
		ChannelSftp channel = null;

		// Create folder in sftp server.
		try {

			Pair<Session, ChannelSftp> sftpConnection = this.getConnection();

			session = sftpConnection.getFirst();
			channel = sftpConnection.getSecond();

			String absolutePathInSftpServer = getPathSeverUpload(folderType);
			if (!isExistFolder(channel, absolutePathInSftpServer)) {
				this.createFolder(channel, absolutePathInSftpServer);
			}

			String fileExtension = FilenameUtils.getExtension(mfile.getOriginalFilename());
			String sftpPathFile = new StringBuilder(createFileName(folderType, airWayBill)).append('.')
					.append(fileExtension).toString();

			// pathFileUpload:
			absolutePath = String.format("%s%s%s", buildSubFolderName(folderType), SEPARATOR, sftpPathFile);

			// save file.
			channel.cd(absolutePathInSftpServer);
			channel.put(mfile.getInputStream(), sftpPathFile);
			disconnect(session, channel);
		} catch (IOException | SftpException e) {
			disconnect(session, channel);
			throw new FileHandleException(e.getMessage(), e);
		} 

		return absolutePath;
	}

	/**
	 * create path file upload.
	 *
	 * @param folderType the folder type
	 * @return the string
	 */
	public String buildSubFolderName(FolderType folderType) {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.YYYYMMDD);
		return date.format(formatter);
	}

	public String createFileName(FolderType folderType, String airWayBill) {
		String fileName = new StringBuilder().append(folderType).append(airWayBill)
				.append(buildSubFolderName(folderType)).toString();
		return fileName;
	}

	public FolderType getFolderType(int typeFile) {
		FolderType folderType;

		switch (typeFile) {
		case INVOICE_NUMBER:
			folderType = FolderType.INVOICE;
			break;
		case PACKAGE_NUMBER:
			folderType = FolderType.PACKAGE;
			break;
		case EVIDENCE_BEFORE_NUMBER:
			folderType = FolderType.EVIDENCE_BEFORE;
			break;
		case EVIDENCE_AFTER_NUMBER:
			folderType = FolderType.EVIDENCE_AFTER;
			break;
		default:
			folderType = FolderType.DEFAULT;
			break;
		}

		return folderType;
	}

	private String getParentPath(FolderType folderType) {
		String res = "";

		switch (folderType) {
		case INVOICE:
			res = INVOICE_PATH;
			break;
		case PACKAGE:
			res = PACKAGE_PATH;
			break;
		case EVIDENCE_BEFORE:
			res = EVIDENCE_BEFORE_PATH;
			break;
		case EVIDENCE_AFTER:
			res = EVIDENCE_AFTER_PATH;
			break;
		default:
			break;
		}

		return res;
	}

	private String getPathSeverUpload(FolderType folderType) {
		return ROOT_PATH + getParentPath(folderType) + SEPARATOR + buildSubFolderName(folderType);
	}

}
