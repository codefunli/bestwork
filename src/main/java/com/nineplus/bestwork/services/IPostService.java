package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.dto.PostResDto;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

/**
 * 
 * @author DiepTT
 *
 */
public interface IPostService {

	PostEntity savePost(PostEntity post) throws BestWorkBussinessException;

	List<PostResDto> getAllPosts() throws BestWorkBussinessException;

	List<PostResDto> getPostsByProjectId(String projectId) throws BestWorkBussinessException;

	PostEntity getPostByPostIdAndProjectId(String postId, String projectId) throws BestWorkBussinessException;

	PostEntity updatePost(PostEntity post) throws BestWorkBussinessException;
	
	List<String> getAllPostIdByProject(List<String> listProjectId) throws BestWorkBussinessException;
	
}
