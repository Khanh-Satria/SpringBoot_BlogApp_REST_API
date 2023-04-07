package com.springboot.blogapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blogapp.payload.CommentDto;
import com.springboot.blogapp.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CommentController {

	private CommentService commentService;

	public CommentController(CommentService commentService) {
		super();
		this.commentService = commentService;
	}
	
	
	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(
			@PathVariable long postId, @Valid @RequestBody CommentDto comment){
		
		return new ResponseEntity<>(commentService.createComment(postId, comment), HttpStatus.CREATED);
	}
	
	
	@GetMapping("/posts/{postId}/comments")
	public List<CommentDto> getCommentsByPostId(@PathVariable long postId){
		
		return commentService.getCommentsByPostId(postId);
	}
	
	@GetMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<CommentDto> getCommentById(
			@PathVariable long postId, @PathVariable long id){
		
		return new ResponseEntity<>(commentService.getCommentById(postId, id), HttpStatus.OK);
	}
	
	
	@PutMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<CommentDto> updateComment(@Valid @PathVariable long postId,
			@PathVariable long id, @RequestBody CommentDto comment){
		
		return new ResponseEntity<>(commentService.updateComment(postId, id, comment), HttpStatus.OK);
	}
	
	
	@DeleteMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable long postId,
			@PathVariable long id) {
		commentService.deleteComment(postId, id);
		
		return new ResponseEntity<String>("Comment deleted successfully", HttpStatus.OK);
	}
}
