package com.springboot.blogapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blogapp.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findByPostId(long postId); // chưa hiểu lắm.
	
	void deleteAllByPostId(long postId);
}
