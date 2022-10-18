package com.nineplus.bestwork.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public Optional<PostEntity> getPostById(String postId) throws BestWorkBussinessException {
		return this.postRepository.findById(postId);
	}

	@Override
	public PostEntity savePost(PostEntity post) throws BestWorkBussinessException {
		return this.postRepository.save(post);
	}

	@Override
	public List<PostEntity> getAllPosts() {

		return this.postRepository.findAll();
	}

}
