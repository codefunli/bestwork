package com.nineplus.bestwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.services.IStorageService;

@RestController
@RequestMapping("/api/v1/file")
public class UploadFileController {

	@Autowired
	private IStorageService storageService;

	@PostMapping("/upload")
	public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file) {
		try {

			storageService.storeFile(file);

			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
}
