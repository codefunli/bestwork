package com.nineplus.bestwork.services.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.nineplus.bestwork.exception.FileHandleException;
import com.nineplus.bestwork.services.ISftpFileService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.DateUtils;
import com.nineplus.bestwork.utils.Enums.FolderType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Configuration
@PropertySource("classpath:application.properties")
public class SftpFileServiceImpl implements ISftpFileService {

	/**
	 * The Constant SEPARATOR.
	 */
	public static final String SEPARATOR = "/";

	public static final String HYPHEN = "-";

	public static final String INVOICE_NAME_PREFIX = "(inv)";

	public static final String PACKAGE_NAME_PREFIX = "(pac)";

	/**
	 * The Constant ROOT_PATH.
	 */
	public static final String ROOT_PATH = "/home/bestwork";

	/**
	 * The Constant HOST.
	 */
	@Value("${fileServer.host}")
	private String SFTP_HOST;

	/**
	 * The Constant PORT.
	 */
	@Value("${fileServer.port}")
	private int SFTP_PORT;

	/**
	 * The Constant USER.
	 */
	@Value("${fileServer.user}")
	private String SFTP_USER;

	/**
	 * The Constant PASSWORD.
	 */
	@Value("${fileServer.password}")
	private String SFTP_PASSWORD;

	@Value("${fileServer.maxSize}")
	private float MAX_SIZE_FILE;

	private static final int INVOICE_NUMBER = 1;

	private static final int PACKAGE_NUMBER = 2;

	private static final int EVIDENCE_BEFORE_NUMBER = 3;

	private static final int EVIDENCE_AFTER_NUMBER = 4;

	public static final String INVOICE_PATH = "invoices";

	public static final String PACKAGE_PATH = "packages";

	public static final String EVIDENCE_BEFORE_PATH = "evidenceBefore";

	public static final String EVIDENCE_AFTER_PATH = "evidenceAfter";

	public static final String CONSTRUCTION_PATH = "constructionPath";

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
	public String createFolder(ChannelSftp channel, String path) {
		try {
			channel.mkdir(path);
			return path;
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
	public byte[] getFile(String pathFileDownload) {
		byte[] resBytes = null;
		ChannelSftp channel = null;
		Session session = null;
		try {

			Pair<Session, ChannelSftp> sftpConnection = this.getConnection();

			session = sftpConnection.getFirst();
			channel = sftpConnection.getSecond();
			resBytes = IOUtils.toByteArray(channel.get(pathFileDownload));

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
	public String uploadInvoice(MultipartFile file, String airWayBill, long Id) {
		return upload(file, FolderType.INVOICE, airWayBill, Id);
	}

	@Override
	public String uploadPackage(MultipartFile file, String airWayBill, long Id) {
		return upload(file, FolderType.PACKAGE, airWayBill, Id);
	}

	@Override
	public String uploadEvidenceBefore(MultipartFile file, String airWayBill, long Id) {
		return upload(file, FolderType.EVIDENCE_BEFORE, airWayBill, Id);
	}

	@Override
	public String uploadEvidenceAfter(MultipartFile file, String airWayBill, long Id) {
		return upload(file, FolderType.EVIDENCE_AFTER, airWayBill, Id);
	}

	@Override
	public String uploadConstructionDrawing(MultipartFile file, long constructionId) {
		return uploadDrawing(file, FolderType.CONSTRUCTION, constructionId);
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
			log.info("Disconect to channel server SFTP");
			channel.exit();
		}

		if (session != null) {
			log.info("Disconect to session server SFTP");
			session.disconnect();
		}
	}

	private String upload(MultipartFile mfile, FolderType folderType, String airWayBill, Long Id) {
		Session session = null;
		ChannelSftp channel = null;
		String pathTemp = null;
		String finalPath = null;

		// Create folder in sftp server.
		try {

			Pair<Session, ChannelSftp> sftpConnection = this.getConnection();

			session = sftpConnection.getFirst();
			channel = sftpConnection.getSecond();

			String absolutePathInSftpServer = getPathSeverUpload(folderType);
			if (!isExistFolder(channel, absolutePathInSftpServer)) {
				pathTemp = this.createFolder(channel, absolutePathInSftpServer);
			} else {
				absolutePathInSftpServer = absolutePathInSftpServer + SEPARATOR + buildSubFolderName(folderType);
				if (!isExistFolder(channel, absolutePathInSftpServer)) {
					pathTemp = this.createFolder(channel, absolutePathInSftpServer);
				} else {
					pathTemp = absolutePathInSftpServer;
				}
			}
			pathTemp = pathTemp + SEPARATOR + airWayBill;
			if (!isExistFolder(channel, pathTemp)) {
				pathTemp = this.createFolder(channel, pathTemp);
			}

			pathTemp = pathTemp + SEPARATOR + Id;
			if (!isExistFolder(channel, pathTemp)) {
				pathTemp = this.createFolder(channel, pathTemp);
			}

			String fileName = FilenameUtils.getName(mfile.getOriginalFilename());

			// save file.
			channel.cd(pathTemp);
			channel.put(mfile.getInputStream(), fileName);
			finalPath = pathTemp + SEPARATOR + fileName;
			disconnect(session, channel);
		} catch (IOException | SftpException e) {
			disconnect(session, channel);
			throw new FileHandleException(e.getMessage(), e);
		} finally {
			disconnect(session, channel);
		}

		return finalPath;
	}

	private String uploadDrawing(MultipartFile mfile, FolderType folderType, Long constructionId) {
		Session session = null;
		ChannelSftp channel = null;
		String pathTemp = null;
		String finalPath = null;

		// Create folder in sftp server.
		try {

			Pair<Session, ChannelSftp> sftpConnection = this.getConnection();

			session = sftpConnection.getFirst();
			channel = sftpConnection.getSecond();

			String absolutePathInSftpServer = getPathSeverUpload(folderType);
			if (!isExistFolder(channel, absolutePathInSftpServer)) {
				pathTemp = this.createFolder(channel, absolutePathInSftpServer);
			} else {
				absolutePathInSftpServer = absolutePathInSftpServer + SEPARATOR + buildSubFolderName(folderType);
				if (!isExistFolder(channel, absolutePathInSftpServer)) {
					pathTemp = this.createFolder(channel, absolutePathInSftpServer);
				} else {
					pathTemp = absolutePathInSftpServer;
				}
			}

			pathTemp = pathTemp + SEPARATOR + constructionId;
			if (!isExistFolder(channel, pathTemp)) {
				pathTemp = this.createFolder(channel, pathTemp);
			}
			String fileName = FilenameUtils.getName(mfile.getOriginalFilename());

			// save file.
			channel.cd(pathTemp);
			channel.put(mfile.getInputStream(), fileName);
			finalPath = pathTemp + SEPARATOR + fileName;
			disconnect(session, channel);
		} catch (IOException | SftpException e) {
			disconnect(session, channel);
			throw new FileHandleException(e.getMessage(), e);
		} finally {
			disconnect(session, channel);
		}
		return finalPath;
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
		case CONSTRUCTION:
			res = CONSTRUCTION_PATH;
			break;
		default:
			break;
		}

		return res;
	}

	private String getPathSeverUpload(FolderType folderType) {
		return ROOT_PATH + SEPARATOR + getParentPath(folderType);
	}

	@Override
	public boolean isValidFile(List<MultipartFile> mFiles) {
		for (MultipartFile file : mFiles) {
			float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
			// file must be < 5MB
			if (fileSizeInMegabytes >= MAX_SIZE_FILE) {
				return false;
			}
		}
		return true;
	}

	@Override
	public File downLoadFile(String pathFileDownload) {
		ChannelSftp channel = null;
		Session session = null;
		File tempFile;
		try {

			Pair<Session, ChannelSftp> sftpConnection = this.getConnection();

			session = sftpConnection.getFirst();
			channel = sftpConnection.getSecond();
			tempFile = File.createTempFile("fileTemp", ".png");
			tempFile.deleteOnExit();
			FileUtils.copyInputStreamToFile(channel.get(pathFileDownload), tempFile);
			// resBytes = IOUtils.toByteArray(channel.get(pathFileDownload));
			// disconnect to sftp server.
			// disconnect(session, channel);
		} catch (Exception ex) {
			disconnect(session, channel);
			throw new FileHandleException(ex.getMessage(), ex);
		} finally {
			disconnect(session, channel);
		}
		return tempFile;
	}

	@Override
	public void createZipFolder(String airWayBillCode, String[] listPathFileDownload) {
		ChannelSftp channel = null;
		Session session = null;
		try {
			Pair<Session, ChannelSftp> sftpConnection = this.getConnection();

			session = sftpConnection.getFirst();
			channel = sftpConnection.getSecond();
			// create a ZipOutputStream object+
			FileOutputStream fos = new FileOutputStream(airWayBillCode);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (int i = 0; i < listPathFileDownload.length; i++) {
				File srcFile = new File(listPathFileDownload[i]);
				FileInputStream fis = new FileInputStream(srcFile);

				// Start writing a new file entry

				int length;
				// create byte buffer
				byte[] buffer = new byte[1024];

				// read and write the content of the file
				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}
				// current file entry is written and current zip entry is closed
				zos.closeEntry();

				// close the InputStream of the file
				fis.close();

			}
			// close the ZipOutputStream
			zos.close();

		} catch (Exception ex) {
			disconnect(session, channel);
			throw new FileHandleException(ex.getMessage(), ex);
		} finally {
			disconnect(session, channel);
		}
	}

	@Override
	public List<String> downloadFileTemp(String airWayBillCode, List<String> listPathFileDownload) {
		ChannelSftp channel = null;
		Session session = null;
		List<String> listPathFile = new ArrayList<>();
		try {
			String temporaryFolder = "src/main/resources/temp";
			Pair<Session, ChannelSftp> sftpConnection = this.getConnection();
			session = sftpConnection.getFirst();
			channel = sftpConnection.getSecond();
			for (String pathFile : listPathFileDownload) {
				String fileName = FilenameUtils.getName(pathFile);
				if (pathFile.contains("/invoices")) {
					fileName = INVOICE_NAME_PREFIX + fileName;
				} else if (pathFile.contains("/packages")) {
					fileName = PACKAGE_NAME_PREFIX + fileName;
				}
				byte[] buffer = new byte[1024];
				BufferedInputStream bis = new BufferedInputStream(channel.get(pathFile));
				Path path = Files.createDirectories(Paths.get(temporaryFolder + SEPARATOR + airWayBillCode));
				String pathFileSever = path + SEPARATOR + fileName;
				File newFile = new File(pathFileSever);
				listPathFile.add(pathFileSever);
				OutputStream os = new FileOutputStream(newFile);
				BufferedOutputStream bos = new BufferedOutputStream(os);
				int readCount;
				while ((readCount = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, readCount);
				}
				bis.close();
				bos.close();

			}
			// disconnect to sftp server.
			disconnect(session, channel);
		} catch (Exception ex) {
			disconnect(session, channel);
			throw new FileHandleException(ex.getMessage(), ex);
		}
		return listPathFile;
	}

	@Override
	public boolean isImageFile(List<MultipartFile> mFiles) {
		for (MultipartFile file : mFiles) {
			// file must be < 5MB
			String fileExtensions = FilenameUtils.getExtension(file.getOriginalFilename());
			if (Arrays.asList(CommonConstants.Image.IMAGE_EXTENSION).contains(fileExtensions.trim())) {
				return true;
			}
		}
		return false;
	}
}
