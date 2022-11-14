package com.nineplus.bestwork.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.FileStorageResDto;
import com.nineplus.bestwork.dto.PostResDto;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.repository.PostRepository;
import com.nineplus.bestwork.services.IPostService;

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

	@Override
	public PostEntity savePost(PostEntity post) throws BestWorkBussinessException {
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
				dto.setProject(postEntity.getProject());
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
			dto.setProject(postEntity.getProject());
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
