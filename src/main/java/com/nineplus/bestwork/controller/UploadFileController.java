package com.nineplus.bestwork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.entity.TFileStorage;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IStorageService;

@RestController
@RequestMapping("/api/v1/file")
public class UploadFileController {

	@Autowired
	private IStorageService storageService;

	@PostMapping("/upload")
	public ResponseEntity<Void> uploadFile(@RequestParam("files") MultipartFile[] files) {
		try {
			for(MultipartFile file : files) {
			storageService.storeFile(file);
			}
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/list/{projectId}")
	public ResponseEntity<? extends Object> getFilesByProjectId(@PathVariable("projectId") String projectId) {

		
		List<TFileStorage> fileList = null;
		try {
			fileList = storageService.getFilesByProjectId(projectId);
		} catch (BestWorkBussinessException ex) {
			ex.getMessage();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (fileList == null || fileList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(fileList, HttpStatus.OK);
	}
}
