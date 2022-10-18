package com.nineplus.bestwork.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.PostRequestDto;
import com.nineplus.bestwork.dto.PostResponseDto;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IPostService;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.utils.CommonConstants;

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

	@PostMapping("/create")
	public ResponseEntity<? extends Object> createPost(@Valid @RequestBody PostRequestDto postRequestDto,
			BindingResult bindingResult) throws BestWorkBussinessException {

		if (!isExistedProjectId(postRequestDto.getProjectId())) {
			return failed(CommonConstants.MessageCode.S1X0002, null);
		}

		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.S1X0005, bindingResult.getFieldErrors().toArray(), null);
		}
		PostEntity post = new PostEntity();
		post.setDescription(postRequestDto.getDescription());
		post.setProject(projectService.getProjectById(postRequestDto.getProjectId()).get());
		post.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));

		PostEntity createdPost = null;
		try {
			createdPost = this.postService.savePost(post);
			for (String image : postRequestDto.getImages()) {
				this.storageService.storeFile(image, createdPost);
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

	private boolean isExistedProjectId(String projectId) {
		Optional<ProjectEntity> project = null;
		try {
			project = projectService.getProjectById(projectId);
		} catch (BestWorkBussinessException e) {
			e.getMessage();
		}
		if (project.isPresent()) {
			return true;
		}
		return false;
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<? extends Object> getPostsByProjectId(@PathVariable String projectId)
			throws BestWorkBussinessException {
		List<PostResponseDto> posts = null;
		posts = postService.getPostsByProjectId(projectId);
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}

}
