package com.springboot.blogapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.blogapp.exception.BlogAPIException;
import com.springboot.blogapp.exception.ResourceNotFoundException;
import com.springboot.blogapp.model.Comment;
import com.springboot.blogapp.model.Post;
import com.springboot.blogapp.payload.CommentDto;
import com.springboot.blogapp.repository.CommentRepository;
import com.springboot.blogapp.repository.PostRepository;

@Service
public class CommentServiceImpl implements CommentService{

	private CommentRepository commentReposiory;
	private PostRepository postRepository;
	private ModelMapper mapper;
	
	public CommentServiceImpl(CommentRepository commentReposiory, PostRepository postRepository, ModelMapper mapper) {
		super();
		this.commentReposiory = commentReposiory;
		this.postRepository = postRepository;
		this.mapper= mapper;
	}
	
	
	@Override
	public CommentDto createComment(long postId, CommentDto commentDto) {
		
		Comment comment = mapToEntity(commentDto);
		Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
		comment.setPost(post);
		Comment newComment = commentReposiory.save(comment);
		
		return mapToDto(newComment);
		
	}
	
	
	@Override
	public List<CommentDto> getCommentsByPostId(long postId){
		
		List<Comment> comments = commentReposiory.findByPostId(postId); 
		/*lấy được data theo yêu cầu nhưng đối tượng trong list là Entity cần thay đổi tất cả sang Dto xài xài stream 
		 sẽ giảm code và để thay đổi dữ liệu ta dùng function map của stream lúc này dữ liệu trả về là kiểu Stream nên cần gọi thêm
		 function collect của stream để convert data sang List, Set ....*/
																						
		return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
	}
	
	
	@Override
	public CommentDto getCommentById(long postId, long commentId) {
		
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new ResourceNotFoundException("post", "id", postId));
		
		Comment comment = commentReposiory.findById(commentId).orElseThrow(
				() -> new ResourceNotFoundException("comment", "id", commentId));
		
		if(!comment.getPost().getId().equals(post.getId())) {    // id post và id commnet trên api đều có nhưng cmt k thuộc post
			
			throw new BlogAPIException(HttpStatus.BAD_REQUEST,"comment dose not belong to post");
		}
		
		return mapToDto(comment);
	}
	
	
	@Override
	public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new ResourceNotFoundException("post", "id", postId));
		
		Comment comment = commentReposiory.findById(commentId).orElseThrow(
				() -> new ResourceNotFoundException("comment", "id", commentId));
		
		if(!comment.getPost().getId().equals(post.getId())) {    // id post và id commnet trên api đều có nhưng cmt k thuộc post
			
			throw new BlogAPIException(HttpStatus.BAD_REQUEST,"comment dose not belong to post");
		}
		comment.setName(commentDto.getName());
		comment.setEmail(commentDto.getEmail());
		comment.setBody(commentDto.getBody());
		comment.setPost(post);
		Comment commentUpdate =  commentReposiory.save(comment);
		return mapToDto(commentUpdate);
		
	}
	
	
	
	
	@Override
	public void deleteComment(long postId, long commentId) {
		
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new ResourceNotFoundException("post", "id", postId));
		
		Comment comment = commentReposiory.findById(commentId).orElseThrow(
				() -> new ResourceNotFoundException("comment", "id", commentId));
		
		if(!comment.getPost().getId().equals(post.getId())) {    // id post và id commnet trên api đều có nhưng cmt k thuộc post
			
			throw new BlogAPIException(HttpStatus.BAD_REQUEST,"comment dose not belong to post");
		}
		
		commentReposiory.delete(comment);
	}
	
	
	public Comment mapToEntity(CommentDto commentDto) {
		
		Comment comment = mapper.map(commentDto, Comment.class);
		
		/*
		 * Comment comment = new Comment(); comment.setId(commentDto.getId()); // khi
		 * map cho function thêm sẽ null; comment.setName(commentDto.getName());
		 * comment.setEmail(commentDto.getEmail());
		 * comment.setBody(commentDto.getBody());
		 */
		return comment;
	}
	
	public CommentDto mapToDto(Comment comment) {
		
		CommentDto commentDto = mapper.map(comment, CommentDto.class);
		
		/*
		 * CommentDto commentDto = new CommentDto(); commentDto.setId(comment.getId());
		 * commentDto.setName(comment.getName()); commentDto.setBody(comment.getBody());
		 * commentDto.setEmail(comment.getEmail());
		 */
		return commentDto;
	}

}
