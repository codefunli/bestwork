package com.nineplus.bestwork.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.dto.FileStorageResponseDto;
import com.nineplus.bestwork.dto.PostResponseDto;
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
public class PostServiceImpl implements IPostService {

	@Autowired
	private PostRepository postRepository;

	@Override
	public PostEntity savePost(PostEntity post) throws BestWorkBussinessException {
		return this.postRepository.save(post);
	}

	@Override
	public List<PostResponseDto> getAllPosts() {
		List<PostEntity> postEntities = this.postRepository.findAll();
		List<PostResponseDto> postResponseDtos = new ArrayList<>();
		if (postEntities != null) {
			for (PostEntity postEntity : postEntities) {
				PostResponseDto dto = new PostResponseDto();
				dto.setId(postEntity.getId());
				dto.setDescription(postEntity.getDescription());
				dto.setCreateDate(postEntity.getCreateDate());
				dto.setProject(postEntity.getProject());
				List<FileStorageResponseDto> fileStorageResponseDtos = new ArrayList<>();
				for (FileStorageEntity file : postEntity.getFileStorages()) {
					FileStorageResponseDto fileStorageResponseDto = new FileStorageResponseDto();
					fileStorageResponseDto.setId(file.getId());
					fileStorageResponseDto.setName(file.getName());
					fileStorageResponseDto.setCreateDate(file.getCreateDate());
					fileStorageResponseDto.setType(file.getType());
					fileStorageResponseDto.setData(new String(file.getData()));
					fileStorageResponseDtos.add(fileStorageResponseDto);
				}
				dto.setFileStorages(fileStorageResponseDtos);
				postResponseDtos.add(dto);
				Collections.sort(postResponseDtos, new Comparator<PostResponseDto>() {

					@Override
					public int compare(PostResponseDto o1, PostResponseDto o2) {
						return (int) (o2.getCreateDate().getTime() - o1.getCreateDate().getTime());
					}
				});
			}
		}
		return postResponseDtos;
	}

	@Override
	public List<PostResponseDto> getPostsByProjectId(String projectId) {
		List<PostEntity> postEntities = this.postRepository.findPostsByProjectId(projectId);
		List<PostResponseDto> postResponseDtos = new ArrayList<>();
		for (PostEntity postEntity : postEntities) {
			PostResponseDto dto = new PostResponseDto();
			dto.setId(postEntity.getId());
			dto.setDescription(postEntity.getDescription());
			dto.setCreateDate(postEntity.getCreateDate());
			dto.setProject(postEntity.getProject());
			List<FileStorageResponseDto> fileStorageResponseDtos = new ArrayList<>();
			for (FileStorageEntity file : postEntity.getFileStorages()) {
				FileStorageResponseDto fileStorageResponseDto = new FileStorageResponseDto();
				fileStorageResponseDto.setId(file.getId());
				fileStorageResponseDto.setName(file.getName());
				fileStorageResponseDto.setCreateDate(file.getCreateDate());
				fileStorageResponseDto.setType(file.getType());
				fileStorageResponseDto.setData(new String(file.getData()));
				fileStorageResponseDtos.add(fileStorageResponseDto);
			}
			dto.setFileStorages(fileStorageResponseDtos);
			postResponseDtos.add(dto);
			Collections.sort(postResponseDtos, new Comparator<PostResponseDto>() {
				@Override
				public int compare(PostResponseDto o1, PostResponseDto o2) {
					return (int) (o2.getCreateDate().getTime() - o1.getCreateDate().getTime());
				}
			});
		}

		return postResponseDtos;
	}

}
