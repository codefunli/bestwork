package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.dto.PostResponseDto;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

/**
 * 
 * @author DiepTT
 *
 */
public interface IPostService {

	PostEntity savePost(PostEntity post) throws BestWorkBussinessException;

	List<PostResponseDto> getAllPosts() throws BestWorkBussinessException;

	List<PostResponseDto> getPostsByProjectId(String projectId) throws BestWorkBussinessException;

}
