package com.nineplus.bestwork.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.FileStorageResponseDto;
import com.nineplus.bestwork.dto.PostCommentRequestDto;
import com.nineplus.bestwork.dto.PostRequestDto;
import com.nineplus.bestwork.dto.PostResponseDto;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IPostService;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.UserAuthUtils;

/**
 * 
 * @author DiepTT
 *
 */

@RestController
@RequestMapping("/api/v1/post")
public class PostController extends BaseController {

	@Autowired
	private IProjectService projectService;

	@Autowired
	private IPostService postService;

	@Autowired
	private IStorageService storageService;

	@Autowired
	UserAuthUtils userAuthUtils;

	@PostMapping("/create")
	public ResponseEntity<? extends Object> createPost(@Valid @RequestBody PostRequestDto postRequestDto,
			BindingResult bindingResult) throws BestWorkBussinessException {

		if (!projectService.isExistedProjectId(postRequestDto.getProjectId())) {
			return failed(CommonConstants.MessageCode.S1X0002, null);
		}

		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.S1X0005, bindingResult.getFieldErrors().toArray(), null);
		}
		PostEntity post = new PostEntity();
		post.setDescription(postRequestDto.getDescription());
		post.setEqBill(postRequestDto.getEqBill());
		post.setProject(projectService.getProjectById(postRequestDto.getProjectId()).get());
		post.setCreateDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))));

		PostEntity createdPost = null;
		try {
			createdPost = this.postService.savePost(post);
			for (String imageData : postRequestDto.getImages()) {
				this.storageService.storeFilePost(imageData, createdPost);
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0004, createdPost, null);
	}

	@GetMapping("/all")
	public ResponseEntity<? extends Object> getAllPosts() throws BestWorkBussinessException {
		List<PostResponseDto> posts = null;
		posts = postService.getAllPosts();
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<? extends Object> getPostsByProjectId(@PathVariable String projectId)
			throws BestWorkBussinessException {
		List<PostResponseDto> posts = null;
		posts = postService.getPostsByProjectId(projectId);
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}

	@GetMapping("/{projectId}/detail/{postId}")
	public ResponseEntity<? extends Object> getPostByPostId(@PathVariable String postId, @PathVariable String projectId)
			throws BestWorkBussinessException {
		PostResponseDto postResponseDto = new PostResponseDto();
		PostEntity post = new PostEntity();
		try {
			post = postService.getPostByPostIdAndProjectId(postId, projectId);
			if (post == null) {
				return failed(CommonConstants.MessageCode.EPOST0001, null);
			}
			postResponseDto.setId(postId);
			postResponseDto.setProject(post.getProject());
			postResponseDto.setDescription(post.getDescription());
			postResponseDto.setEqBill(post.getEqBill());
			postResponseDto.setCreateDate(post.getCreateDate().toString());
			postResponseDto.setComment(post.getComment());
			List<FileStorageResponseDto> fileStorageResponseDtos = new ArrayList<>();
			for (FileStorageEntity file : post.getFileStorages()) {
				FileStorageResponseDto fileStorageResponseDto = new FileStorageResponseDto();
				fileStorageResponseDto.setId(file.getId());
				fileStorageResponseDto.setName(file.getName());
				fileStorageResponseDto.setCreateDate(file.getCreateDate());
				fileStorageResponseDto.setType(file.getType());
				fileStorageResponseDto.setData(new String(file.getData()));
				fileStorageResponseDtos.add(fileStorageResponseDto);
			}
			postResponseDto.setFileStorages(fileStorageResponseDtos);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SPOST0001, postResponseDto, null);

	}

	@PatchMapping("/{postId}/project/{projectId}/comment")
	public ResponseEntity<? extends Object> addComment(@PathVariable String projectId, @PathVariable String postId,
			@Valid @RequestBody PostCommentRequestDto postCommentRequestDto, BindingResult bindingResult)
			throws BestWorkBussinessException {
		PostEntity post = new PostEntity();
		try {
			post = this.postService.getPostByPostIdAndProjectId(postId, projectId);
			if (post == null) {
				return failed(CommonConstants.MessageCode.EPOST0001, null);
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.EPOST0003, bindingResult.getFieldErrors().toArray(),
					null);
		}
		post.setComment(postCommentRequestDto.getComment());
		PostEntity commentPost = new PostEntity();
		try {
			commentPost = this.postService.updatePost(post);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SPOST0003, commentPost, null);
	}

	@PatchMapping("/{projectId}/update/{postId}")
	public ResponseEntity<? extends Object> updatePost(@PathVariable String postId, @PathVariable String projectId,
			@Valid @RequestBody PostRequestDto postRequestDto, BindingResult bindingResult)
			throws BestWorkBussinessException {
		PostEntity post = new PostEntity();
		try {
			post = this.postService.getPostByPostIdAndProjectId(postId, projectId);
			if (post == null) {
				return failed(CommonConstants.MessageCode.EPOST0001, null);
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.EPOST0002, bindingResult.getFieldErrors().toArray(),
					null);
		}
		BeanUtils.copyProperties(postRequestDto, post);
		PostEntity updatedPost = null;

		this.storageService.deleteFilesByPostId(postId);
		try {
			updatedPost = this.postService.updatePost(post);
			for (String imageData : postRequestDto.getImages()) {
				this.storageService.storeFilePost(imageData, updatedPost);
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SPOST0002, updatedPost, null);
	}
}
