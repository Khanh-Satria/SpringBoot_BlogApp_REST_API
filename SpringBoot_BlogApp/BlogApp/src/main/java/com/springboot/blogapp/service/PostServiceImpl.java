package com.springboot.blogapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.blogapp.exception.ResourceNotFoundException;
import com.springboot.blogapp.model.Category;
import com.springboot.blogapp.model.Comment;
import com.springboot.blogapp.model.Post;
import com.springboot.blogapp.payload.PostDto;
import com.springboot.blogapp.payload.PostResponse;
import com.springboot.blogapp.repository.CategoryRepository;
import com.springboot.blogapp.repository.CommentRepository;
import com.springboot.blogapp.repository.PostRepository;

@Service
public class PostServiceImpl implements PostService{

	private PostRepository postRepository;
	private CommentRepository commentRepository;
	private CategoryRepository categoryRepository;
	private ModelMapper mapper;

	public PostServiceImpl(PostRepository postRepository,
			CommentRepository commentRepository,
			ModelMapper mapper,
			CategoryRepository categoryRepository
			) {
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
		this.mapper = mapper;
		this.categoryRepository = categoryRepository;
	}
	
	@Override
	public PostDto createPost(PostDto postDto ) {
		
		Category category = categoryRepository.findById(postDto.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));
		
		//convert dto to entity
		Post post = mapToEntity(postDto);
		post.setCategory(category);
		Post newPost = postRepository.save(post);
		
		//convert entity to dto
		PostDto postResponse = mapToDTO(newPost);
		
		return postResponse;
	}
	
	
	@Override
	public PostResponse getAllPost(int pageSize, int pageNo, String sortBy, String sortDir){
		
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending() ;
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page<Post> posts = postRepository.findAll(pageable);
		
		List<Post> listOfPost = posts.getContent();
		
		List<PostDto> content = listOfPost.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(pageNo);
		postResponse.setPageSize(pageSize);
		postResponse.setTotalElements(posts.getTotalElements());
		postResponse.setTotalPages(posts.getTotalPages());
		postResponse.setLast(posts.isLast());
		
		return postResponse;
		
	}
	
	
	@Override
	public PostDto getPostById(long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		return mapToDTO(post);
	}
	
	
	@Override
	public PostDto updatePost(long id, PostDto postDto) {
		
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		
		Category category = categoryRepository.findById(postDto.getCategoryId())
										.orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));
		
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		post.setCategory(category);
		
		Post postUpdate = postRepository.save(post);
		
		return mapToDTO(postUpdate);
	}
	
	
	@Override
	public void deletePostById(long id) {
	
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		List<Comment> comments = commentRepository.findByPostId(id);
		comments.forEach(comment -> commentRepository.delete(comment));
		postRepository.delete(post);
	}
	
	
	@Override
	public List<PostDto> getPostByCategory(Long categoryId){
		
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
		
		List<Post> posts = postRepository.findByCategoryId(categoryId);
		
		return posts.stream().map(post-> mapToDTO(post)).collect(Collectors.toList());
	}
	
	
	//convert entity to dto
	private PostDto mapToDTO(Post post) {
		
		PostDto postDto = mapper.map(post, PostDto.class);
		
		/*
		 * PostDto postDto = new PostDto(); postDto.setId(post.getId());
		 * postDto.setTitle(post.getTitle());
		 * postDto.setDescription(post.getDescription());
		 * postDto.setContent(post.getContent());
		 */
		
		return postDto;
	}
	
	//convert dto to entity
	private Post mapToEntity(PostDto postDto) {
		
		
		Post post = mapper.map(postDto, Post.class);		
		/*
		 * Post post = new Post();
		 * 
		 * post.setId(postDto.getId()); post.setTitle(postDto.getTitle());
		 * post.setDescription(postDto.getDescription());
		 * post.setContent(postDto.getContent());
		 */
		
		return post;
	}
}
