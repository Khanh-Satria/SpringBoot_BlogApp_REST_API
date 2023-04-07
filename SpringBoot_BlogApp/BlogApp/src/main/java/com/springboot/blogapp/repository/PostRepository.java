package com.springboot.blogapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blogapp.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
	List<Post> findByCategoryId(Long categoryId);
}
