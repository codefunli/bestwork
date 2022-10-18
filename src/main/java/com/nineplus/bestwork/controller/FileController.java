package com.nineplus.bestwork.controller;

import java.util.List;
import java.util.Optional;

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

import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IPostService;
import com.nineplus.bestwork.services.IStorageService;

/**
 * 
 * @author DiepTT
 *
 */

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

	@Autowired
	private IStorageService storageService;

	@Autowired
	private IPostService postService;

	@PostMapping("/upload/{postId}")
	public ResponseEntity<Void> uploadFile(@RequestParam("files") MultipartFile[] files, @PathVariable String postId) {

		PostEntity reqpost = getPostById(postId);
		try {
//			for (MultipartFile file : files) {
//				storageService.storeFile(file, reqpost);
//			}
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/list/{postId}")
	public ResponseEntity<? extends Object> getFilesByPostId(@PathVariable String postId) {

		List<FileStorageEntity> fileList = null;
		try {
			fileList = storageService.getFilesByPostId(postId);
		} catch (BestWorkBussinessException ex) {
			ex.getMessage();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (fileList == null || fileList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(fileList, HttpStatus.OK);
	}

	private PostEntity getPostById(String postId) {
		Optional<PostEntity> postOptional = null;
		try {

			postOptional = this.postService.getPostById(postId);
		} catch (BestWorkBussinessException ex) {
			ex.getMessage();
		}
		if (!postOptional.isPresent()) {
			return null;
		}
		return postOptional.get();
	}
}
