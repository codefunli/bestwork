package com.nineplus.bestwork.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.nineplus.bestwork.dto.FileStorageResDto;
import com.nineplus.bestwork.dto.PostReqDto;
import com.nineplus.bestwork.dto.PostResDto;
import com.nineplus.bestwork.entity.ConstructionEntity;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.repository.PostRepository;
import com.nineplus.bestwork.services.IConstructionService;
import com.nineplus.bestwork.services.IPostService;
import com.nineplus.bestwork.utils.CommonConstants;

/**
 * 
 * @author DiepTT
 *
 */

@Service
@Transactional
public class PostServiceImpl implements IPostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private IConstructionService constructionService;

	@Override
	public PostEntity savePost(PostReqDto postReqDto, BindingResult bindingResult) throws BestWorkBussinessException {
		ConstructionEntity construction = this.constructionService.findCstrtById(postReqDto.getConstructionId());
		if (construction == null) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECS0007, null);
		}

		if (bindingResult.hasErrors()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.SU0003,  null);
		}
		PostEntity post = new PostEntity();
		post.setDescription(postReqDto.getDescription());
		post.setEqBill(postReqDto.getEqBill());
		post.setConstruction(constructionService.findCstrtById(postReqDto.getConstructionId()));
		post.setCreateDate(LocalDateTime.now());

		return this.postRepository.save(post);
	}

	@Override
	public List<PostResDto> getAllPosts() {
		List<PostEntity> postEntities = this.postRepository.findAll();
		List<PostResDto> postResponseDtos = new ArrayList<>();
		if (!postEntities.isEmpty()) {
			for (PostEntity postEntity : postEntities) {
				PostResDto dto = new PostResDto();
				dto.setId(postEntity.getId());
				dto.setDescription(postEntity.getDescription());
				dto.setEqBill(postEntity.getEqBill());
				dto.setCreateDate(postEntity.getCreateDate().toString());
				dto.setConstruction(postEntity.getConstruction());
				List<FileStorageResDto> fileStorageResponseDtos = new ArrayList<>();
				for (FileStorageEntity file : postEntity.getFileStorages()) {
					FileStorageResDto fileStorageResponseDto = new FileStorageResDto();
					fileStorageResponseDto.setId(file.getId());
					fileStorageResponseDto.setName(file.getName());
					fileStorageResponseDto.setCreateDate(file.getCreateDate().toString());
					fileStorageResponseDto.setType(file.getType());
					fileStorageResponseDto.setData(new String(file.getData()));
					fileStorageResponseDtos.add(fileStorageResponseDto);
				}
				dto.setFileStorages(fileStorageResponseDtos);
				postResponseDtos.add(dto);
				Collections.sort(postResponseDtos, new Comparator<PostResDto>() {
					@Override
					public int compare(PostResDto o1, PostResDto o2) {
						return (int) (Timestamp.valueOf(o2.getCreateDate()).getTime()
								- Timestamp.valueOf(o1.getCreateDate()).getTime());
					}
				});
			}
		}
		return postResponseDtos;
	}

	@Override
	public List<PostResDto> getPostsByProjectId(String projectId) {
		List<PostEntity> postEntities = this.postRepository.findPostsByProjectId(projectId);
		List<PostResDto> postResponseDtos = new ArrayList<>();
		for (PostEntity postEntity : postEntities) {
			PostResDto dto = new PostResDto();
			dto.setId(postEntity.getId());
			dto.setDescription(postEntity.getDescription());
			dto.setEqBill(postEntity.getEqBill());
			dto.setCreateDate(postEntity.getCreateDate().toString());
			dto.setConstruction(postEntity.getConstruction());
			dto.setComment(postEntity.getComment());
			List<FileStorageResDto> fileStorageResponseDtos = new ArrayList<>();
			for (FileStorageEntity file : postEntity.getFileStorages()) {
				FileStorageResDto fileStorageResponseDto = new FileStorageResDto();
				fileStorageResponseDto.setId(file.getId());
				fileStorageResponseDto.setName(file.getName());
				fileStorageResponseDto.setCreateDate(file.getCreateDate().toString());
				fileStorageResponseDto.setType(file.getType());
				fileStorageResponseDto.setData(new String(file.getData()));
				fileStorageResponseDtos.add(fileStorageResponseDto);
			}
			dto.setFileStorages(fileStorageResponseDtos);
			postResponseDtos.add(dto);
			Collections.sort(postResponseDtos, new Comparator<PostResDto>() {
				@Override
				public int compare(PostResDto o1, PostResDto o2) {
					return (int) (Timestamp.valueOf(o2.getCreateDate()).getTime()
							- Timestamp.valueOf(o1.getCreateDate()).getTime());
				}
			});
		}

		return postResponseDtos;
	}

	@Override
	public PostEntity getPostByPostIdAndProjectId(String postId, String projectId) {
		return this.postRepository.findPostByIdAndProjectId(postId, projectId);

	}

	@Override
	public PostEntity updatePost(PostEntity post) {
		return this.postRepository.save(post);
	}

	@Override
	public List<String> getAllPostIdByProject(List<String> listProjectId) throws BestWorkBussinessException {
		List<String> listPostId = null;
		if (listProjectId != null) {
			listPostId = postRepository.getAllPostIdByProject(listProjectId);
		}
		return listPostId;
	}

}
