package com.nineplus.bestwork.services;

import java.util.List;
import java.util.Optional;

import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

/**
 * 
 * @author DiepTT
 *
 */
public interface IPostService {

	Optional<PostEntity> getPostById(String postId) throws BestWorkBussinessException;

	PostEntity savePost(PostEntity post) throws BestWorkBussinessException;

	List<PostEntity> getAllPosts();

}
