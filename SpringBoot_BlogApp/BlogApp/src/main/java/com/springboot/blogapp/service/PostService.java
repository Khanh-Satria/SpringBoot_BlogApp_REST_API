package com.springboot.blogapp.service;

import java.util.List;

import com.springboot.blogapp.payload.PostDto;
import com.springboot.blogapp.payload.PostResponse;

public interface PostService {

	PostDto createPost(PostDto postDto);

	PostResponse getAllPost(int pageSize, int pageNo, String sortBy,String sortDir);

	PostDto getPostById(long id);

	PostDto updatePost(long id, PostDto postDto);

	void deletePostById(long id);

	List<PostDto> getPostByCategory(Long categoryId);

}
